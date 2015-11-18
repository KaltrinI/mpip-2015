package mk.ukim.finki.mpip.listviewshowcase.app;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.*;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends ActionBarActivity {

  ListView itemsList;
  ArrayAdapter<String> adapter;
  SearchView searchView;

  List<Person> persons = new ArrayList<Person>();

  CustomAdapter customAdapter;

  List<String> items = new ArrayList<String>();
  private RestTemplate restTemplate;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    doInject();
  }


  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.menu_main, menu);
    MenuItem item = menu.findItem(R.id.action_load);

    item.setOnMenuItemClickListener(new ReloadClickListener());


    final SharedPreferences preferences = MainActivity.this.getSharedPreferences("ReloadPreferences",
      Activity.MODE_PRIVATE);

    MenuItem activateDeactivateReload = menu.findItem(R.id.activate_deactivate_reload);
    MenuItem reload10 = menu.findItem(R.id.reload_10);
    MenuItem reload30 = menu.findItem(R.id.reload_30);

    activateDeactivateReload.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
      @Override
      public boolean onMenuItemClick(MenuItem item) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("activeReload",
          !preferences.getBoolean("activeReload", true));

        editor.commit();
        return false;
      }
    });

    reload10.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
      @Override
      public boolean onMenuItemClick(MenuItem item) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("reloadInterval", 10);
        editor.commit();
        return false;
      }
    });


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

  public RestTemplate getRestTemplate() {
    if (restTemplate == null) {
      restTemplate = new RestTemplate();
      restTemplate.getMessageConverters().add(new MappingJacksonHttpMessageConverter());
      restTemplate.getMessageConverters().add(new FormHttpMessageConverter());
    }
    return restTemplate;
  }

  public class ReloadClickListener
    implements ActionMenuView.OnMenuItemClickListener, MenuItem.OnMenuItemClickListener {

    @Override
    public boolean onMenuItemClick(MenuItem item) {
//      if (reloadPersonsTask.getStatus().equals(AsyncTask.Status.FINISHED)) {
//        reloadPersonsTask = new ReloadPersons();
//      }
//      if (reloadPersonsTask.getStatus().equals(AsyncTask.Status.PENDING))
//        reloadPersonsTask.execute();
      startService(new Intent(MainActivity.this, DownloadPersonsService.class));
      return false;
    }
  }

  public class ReloadPersons extends AsyncTask<Void, Void, Person[]> {

    @Override
    protected void onPreExecute() {

      super.onPreExecute();
    }

    @Override
    protected void onCancelled() {
      Context context = MainActivity.this;
      Toast.makeText(
        context,
        context.getResources().getString(
          R.string.no_internet_connection), Toast.LENGTH_LONG)
        .show();
      super.onCancelled();
    }

    @Override
    protected Person[] doInBackground(Void... params) {

      Context context = MainActivity.this;
      if (!hasInternetConnection(context)) {
        cancel(true);
        return null;
      }
      // Don't do this at home :)
      RestTemplate template = getRestTemplate();
      ResponseEntity<Person[]> result = template.exchange(
        getString(R.string.persons_url),
        HttpMethod.GET, null, Person[].class);
      Person[] persons = result.getBody();
      return persons;
    }

    @Override
    protected void onPostExecute(Person[] persons) {
      super.onPostExecute(persons);
      if (persons != null) {
        customAdapter.clear();
        for (Person p : persons)
          customAdapter.add(p);
      }
    }
  }

  private ReloadPersons reloadPersonsTask = new ReloadPersons();

  public boolean hasInternetConnection(Context context) {
    ConnectivityManager cm = (ConnectivityManager) context
      .getSystemService(Context.CONNECTIVITY_SERVICE);

    NetworkInfo ni = cm.getActiveNetworkInfo();


    if (ni == null) {
      return false;
    } else {
      try {
        ResponseEntity<Boolean> result = getRestTemplate().exchange(getString(R.string.ping_url), HttpMethod.GET, null, Boolean.class);
        if (result.getStatusCode().equals(HttpStatus.OK)) {
          return result.getBody();
        } else
          return false;
      } catch (ResourceAccessException ex) {
        return false;
      }
    }
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

  private PersonsReceiver receiver;

  @Override
  protected void onResume() {
    super.onResume();
    receiver = new PersonsReceiver();
    IntentFilter intentFilter = new IntentFilter("mk.ukim.finki.mpip.PersonsData");
    registerReceiver(receiver, intentFilter);
  }

  @Override
  protected void onPause() {
    super.onPause();
    unregisterReceiver(receiver);
  }
}

