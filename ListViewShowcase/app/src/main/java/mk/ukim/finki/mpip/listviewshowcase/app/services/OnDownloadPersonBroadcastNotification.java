package mk.ukim.finki.mpip.listviewshowcase.app.services;

import android.content.Intent;
import mk.ukim.finki.mpip.listviewshowcase.app.Person;
import mk.ukim.finki.mpip.listviewshowcase.app.tasks.ReloadPersonsTask;

import java.util.ArrayList;

/**
 * Created by ristes on 11/25/15.
 */
public class OnDownloadPersonBroadcastNotification implements ReloadPersonsTask.OnPersonsResult {

  public static final String INTENT_ACTION = "mk.ukim.finki.mpip.PersonsData";
  private DownloadPersonsService downloadPersonService;

  public OnDownloadPersonBroadcastNotification(DownloadPersonsService downloadPersonService) {
    this.downloadPersonService = downloadPersonService;
  }

  @Override
  public void onResult(Person[] persons) {

    Intent personsBroadcastIntent = new Intent(INTENT_ACTION);
    ArrayList<Person> list = new ArrayList<Person>(persons.length);
    for (Person p : persons) {
      list.add(p);
    }
    personsBroadcastIntent.putParcelableArrayListExtra("persons",
      list);
    downloadPersonService.sendBroadcast(personsBroadcastIntent);

    RescheduleService.rescheduleService(
      downloadPersonService,
      "ReloadPreferences",
      "reloadInterval",
      "activeReload"
    );

    downloadPersonService.stopSelf();
  }
}
