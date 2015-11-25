package mk.ukim.finki.mpip.listviewshowcase.app.services;

import android.content.Intent;
import mk.ukim.finki.mpip.listviewshowcase.app.Person;
import mk.ukim.finki.mpip.listviewshowcase.app.db.PersonDataAccessObject;
import mk.ukim.finki.mpip.listviewshowcase.app.tasks.ReloadPersonsTask;

/**
 * Created by ristes on 11/25/15.
 */
public class OnDownloadPersonDatabaseNotification implements ReloadPersonsTask.OnPersonsResult {


  public static final String INTENT_ACTION = "mk.ukim.finki.mpip.PersonsDatabaseData";
  private DownloadPersonsService downloadPersonService;

  public OnDownloadPersonDatabaseNotification(DownloadPersonsService downloadPersonService) {
    this.downloadPersonService = downloadPersonService;
  }

  @Override
  public void onResult(Person[] persons) {
    if (persons != null) {
      PersonDataAccessObject personDao = new PersonDataAccessObject(downloadPersonService);
      personDao.open();
      for (Person person : persons) {
        personDao.insert(person);
      }
      personDao.close();


      Intent personsBroadcastIntent = new Intent(INTENT_ACTION);
      downloadPersonService.sendBroadcast(personsBroadcastIntent);
    }

    RescheduleService.rescheduleService(
      downloadPersonService,
      "ReloadPreferences",
      "reloadInterval",
      "activeReload"
    );

    downloadPersonService.stopSelf();
  }

}
