package mk.ukim.finki.mpip.listviewshowcase.app.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import mk.ukim.finki.mpip.listviewshowcase.app.tasks.ReloadPersonsTask;

public class DownloadPersonsService extends Service {

  public DownloadPersonsService() {
  }

  @Override
  public int onStartCommand(Intent intent, int flags, int startId) {
    final Context context = this;
    ReloadPersonsTask reloadPersons = new ReloadPersonsTask(
      this,
      new OnDownloadPersonDatabaseNotification(this)
    );
    reloadPersons.execute();

    return super.onStartCommand(intent, flags, startId);
  }

  @Override
  public IBinder onBind(Intent intent) {
    // TODO: Return the communication channel to the service.
    throw new UnsupportedOperationException("Not yet implemented");
  }


}
