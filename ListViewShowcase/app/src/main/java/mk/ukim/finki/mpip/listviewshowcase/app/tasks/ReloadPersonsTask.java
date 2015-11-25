package mk.ukim.finki.mpip.listviewshowcase.app.tasks;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.widget.Toast;
import mk.ukim.finki.mpip.listviewshowcase.app.Person;
import mk.ukim.finki.mpip.listviewshowcase.app.R;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

/**
 * Created by ristes on 11/25/15.
 */
public class ReloadPersonsTask extends AsyncTask<Void, Void, Person[]> {

  public interface OnPersonsResult {
    void onResult(Person[] persons);
  }

  private Context context;

  private OnPersonsResult listener;

  public ReloadPersonsTask(Context context, OnPersonsResult listener) {
    this.context = context;
    this.listener = listener;
  }

  @Override
  protected void onPreExecute() {

    super.onPreExecute();
  }

  @Override
  protected void onCancelled() {
    Toast.makeText(
      context,
      context.getResources().getString(
        R.string.no_internet_connection), Toast.LENGTH_LONG)
      .show();
    super.onCancelled();
  }

  @Override
  protected Person[] doInBackground(Void... params) {

    if (!hasInternetConnection(context)) {
      cancel(true);
      return null;
    }
    RestTemplate template = getRestTemplate();

    ResponseEntity<Person[]> result = template.exchange(
      context.getString(R.string.persons_url),
      HttpMethod.GET,
      null,
      Person[].class
    );
    Person[] persons = result.getBody();
    return persons;
  }

  @Override
  protected void onPostExecute(Person[] persons) {
    super.onPostExecute(persons);

  }


  public RestTemplate getRestTemplate() {
    if (restTemplate == null) {
      restTemplate = new RestTemplate();
      restTemplate.getMessageConverters().add(new MappingJacksonHttpMessageConverter());
      restTemplate.getMessageConverters().add(new FormHttpMessageConverter());
    }
    return restTemplate;
  }


  public boolean hasInternetConnection(Context context) {
    ConnectivityManager cm = (ConnectivityManager) context
      .getSystemService(Context.CONNECTIVITY_SERVICE);

    NetworkInfo ni = cm.getActiveNetworkInfo();


    if (ni == null) {
      return false;
    } else {
      try {
        ResponseEntity<Boolean> result = getRestTemplate().exchange(
          context.getString(R.string.ping_url),
          HttpMethod.GET,
          null,
          Boolean.class
        );
        if (result.getStatusCode().equals(HttpStatus.OK)) {
          return result.getBody();
        } else
          return false;
      } catch (ResourceAccessException ex) {
        return false;
      }
    }
  }

  private RestTemplate restTemplate;
}
