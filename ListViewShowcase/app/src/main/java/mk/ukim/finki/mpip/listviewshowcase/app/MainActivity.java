package mk.ukim.finki.mpip.listviewshowcase.app;

import android.app.FragmentManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends ActionBarActivity {

  ListView itemsList;
  ArrayAdapter<String> adapter;
  SearchView searchView;

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
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
      MenuItem searchItem = menu.findItem(R.id.action_search);
      searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
      searchView.setOnCloseListener(new SearchView.OnCloseListener() {
        @Override
        public boolean onClose() {
          customAdapter.search(null);
          return false;
        }
      });
      searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
        @Override
        public boolean onQueryTextSubmit(String s) {
          customAdapter.search(s);
          return false;
        }

        @Override
        public boolean onQueryTextChange(String s) {
          customAdapter.search(s);
          return true;
        }
      });
    }
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


    PersonFragment personFragment = PersonFragment.create(customAdapter, null, null);
    fragmentManager
      .beginTransaction()
      .add(R.id.add_item, personFragment)
      .commit();
  }

}

