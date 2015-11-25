package mk.ukim.finki.mpip.listviewshowcase.app.services;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import java.util.Calendar;

/**
 * Created by ristes on 11/25/15.
 */
public class RescheduleService {


  public static void rescheduleService(Service downloadPersonService,
                                       String preferencesName,
                                       String reloadIntervalParameter,
                                       String activeReloadParameter) {

    SharedPreferences preferences =
      downloadPersonService.getSharedPreferences(preferencesName,
        Activity.MODE_PRIVATE);

    int reloadInterval = preferences.getInt(reloadIntervalParameter, 30);
    boolean activeReload = preferences.getBoolean(activeReloadParameter, true);

    if (activeReload) {
      //setup calendar to run this service again in 30 seconds
      Calendar cal = Calendar.getInstance();
      cal.add(Calendar.SECOND, reloadInterval);

      Intent intent = new Intent(downloadPersonService, DownloadPersonsService.class);

      PendingIntent pintent = PendingIntent.getService(
        downloadPersonService, 0, intent, 0);

      AlarmManager alarm = (AlarmManager) downloadPersonService.getSystemService(Context.ALARM_SERVICE);
      alarm.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pintent);
    }
  }
}
