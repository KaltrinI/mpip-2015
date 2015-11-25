package mk.ukim.finki.mpip.listviewshowcase.app;

import android.app.FragmentManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import mk.ukim.finki.mpip.listviewshowcase.app.db.PersonDataAccessObject;
import mk.ukim.finki.mpip.listviewshowcase.app.itemListeners.SearchItemListener;
import mk.ukim.finki.mpip.listviewshowcase.app.services.OnDownloadPersonBroadcastNotification;
import mk.ukim.finki.mpip.listviewshowcase.app.services.OnDownloadPersonDatabaseNotification;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends ActionBarActivity {

  ListView itemsList;
  ArrayAdapter<String> adapter;

  SearchItemListener searchItemListener;
  List<Person> persons = new ArrayList<Person>();

  CustomAdapter customAdapter;

  List<String> items = new ArrayList<String>();

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    doInject();
  }


  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.menu_main, menu);
    searchItemListener.initializeMenu(menu);
    return true;
  }


  private void doInject() {
    itemsList = (ListView) findViewById(R.id.items_list);


    adapter = new ArrayAdapter<String>(
      this,
      android.R.layout.simple_list_item_1,
      items);

    final FragmentManager fragmentManager = getFragmentManager();

    customAdapter = new CustomAdapter(this, persons);
    itemsList.setAdapter(customAdapter);


    itemsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

        fragmentManager.beginTransaction()
          .replace(R.id.add_item,
            PersonFragment.create(customAdapter, (Person) customAdapter.getItem(position), position))
          .commit();

      }
    });

    loadPersons();


    searchItemListener = new SearchItemListener(this, customAdapter);

    PersonFragment personFragment = PersonFragment.create(customAdapter, null, null);
    fragmentManager
      .beginTransaction()
      .add(R.id.add_item, personFragment)
      .commit();
  }


  private class PersonsReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
      List<Person> persons = intent.getParcelableArrayListExtra("persons");
      for (Person p : persons) {
        customAdapter.add(p);
      }
    }
  }

  private class PersonsDatabaseChangeReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
      loadPersons();
    }
  }

  private void loadPersons() {
    PersonDataAccessObject personDao = new PersonDataAccessObject(MainActivity.this);
    personDao.open();
    List<Person> persons = personDao.getAllItems();
    customAdapter.clear();
    for (Person p : persons) {
      customAdapter.add(p);
    }
    personDao.close();
  }

  private PersonsReceiver receiver;
  private PersonsDatabaseChangeReceiver dbReceiver;

  @Override
  protected void onResume() {
    super.onResume();
    receiver = new PersonsReceiver();
    IntentFilter intentFilter = new IntentFilter(
      OnDownloadPersonBroadcastNotification.INTENT_ACTION
    );
    registerReceiver(receiver, intentFilter);

    dbReceiver = new PersonsDatabaseChangeReceiver();
    IntentFilter intentFilterDb = new IntentFilter(
      OnDownloadPersonDatabaseNotification.INTENT_ACTION
    );
    registerReceiver(dbReceiver, intentFilterDb);
  }

  @Override
  protected void onPause() {
    super.onPause();
    unregisterReceiver(receiver);
    unregisterReceiver(dbReceiver);
  }
}

