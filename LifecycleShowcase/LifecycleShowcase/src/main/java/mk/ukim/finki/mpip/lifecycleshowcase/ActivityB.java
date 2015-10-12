package mk.ukim.finki.mpip.lifecycleshowcase;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

/**
 * Created by ristes on 10/2/15.
 */
public class ActivityB extends Activity {


  public static final String TAG = "Lifecycle-Activity B";
  TextView helloText;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    Log.e(TAG, "onCreate invoked");

    helloText = (TextView) findViewById(R.id.hello_text);
    helloText.setText(
      getString(R.string.hello_from)
        + " "
        + "Activity B"
    );
  }

  @Override
  protected void onRestart() {
    super.onRestart();
    Log.e(TAG, "onRestart invoked");
  }

  @Override
  protected void onStart() {
    super.onStart();
    Log.e(TAG, "onStart invoked");
  }

  @Override
  protected void onResume() {
    super.onResume();
    Log.e(TAG, "onResume invoked");
  }

  @Override
  protected void onPause() {
    super.onPause();
    Log.e(TAG, "onPause invoked");
  }

  @Override
  protected void onStop() {
    super.onStop();
    Log.e(TAG, "onStop invoked");
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    Log.e(TAG, "onDestroy invoked");
  }

}
