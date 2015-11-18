package mk.ukim.finki.mpip.listviewshowcase.app;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.IBinder;
import android.widget.Toast;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Calendar;

public class DownloadPersonsService extends Service {

  public DownloadPersonsService() {
  }

  @Override
  public int onStartCommand(Intent intent, int flags, int startId) {
    ReloadPersons reloadPersons = new ReloadPersons();
    reloadPersons.execute();

    return super.onStartCommand(intent, flags, startId);
  }

  @Override
  public IBinder onBind(Intent intent) {
    // TODO: Return the communication channel to the service.
    throw new UnsupportedOperationException("Not yet implemented");
  }


  public class ReloadPersons extends AsyncTask<Void, Void, Person[]> {

    @Override
    protected void onPreExecute() {

      super.onPreExecute();
    }

    @Override
    protected void onCancelled() {
      Context context = DownloadPersonsService.this;
      Toast.makeText(
        context,
        context.getResources().getString(
          R.string.no_internet_connection), Toast.LENGTH_LONG)
        .show();
      super.onCancelled();
    }

    @Override
    protected Person[] doInBackground(Void... params) {

      Context context = DownloadPersonsService.this;
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

      Context ctx = DownloadPersonsService.this;
      Intent personsBroadcastIntent = new Intent("mk.ukim.finki.mpip.PersonsData");
      ArrayList<Person> list = new ArrayList<Person>(persons.length);
      for (Person p : persons) {
        list.add(p);
      }
      personsBroadcastIntent.putParcelableArrayListExtra("persons",
        list);
      ctx.sendBroadcast(personsBroadcastIntent);


      SharedPreferences preferences = ctx.getSharedPreferences("ReloadPreferences",
        Activity.MODE_PRIVATE);

      int reloadInterval = preferences.getInt("reloadInterval", 30);
      boolean activeReload = preferences.getBoolean("activeReload", true);

      if (activeReload) {
        //setup calendar to run this service again in 30 seconds
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.SECOND, reloadInterval);

        Intent intent = new Intent(ctx, DownloadPersonsService.class);

        PendingIntent pintent = PendingIntent.getService(
          ctx, 0, intent, 0);

        AlarmManager alarm = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarm.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pintent);
      }

      DownloadPersonsService.this.stopSelf();
    }
  }

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

  public RestTemplate getRestTemplate() {
    if (restTemplate == null) {
      restTemplate = new RestTemplate();
      restTemplate.getMessageConverters().add(new MappingJacksonHttpMessageConverter());
      restTemplate.getMessageConverters().add(new FormHttpMessageConverter());
    }
    return restTemplate;
  }

  private RestTemplate restTemplate;
}
