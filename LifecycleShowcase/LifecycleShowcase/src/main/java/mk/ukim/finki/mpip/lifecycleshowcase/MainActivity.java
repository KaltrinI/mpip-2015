package mk.ukim.finki.mpip.lifecycleshowcase;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;


public class MainActivity extends Activity {

  public static final String TAG = "Lifecycle-MainActivity";

  TextView helloText;
  Button button;
  CheckBox checkBox;
  RadioGroup radioGroup;
  RadioButton radioButton1;
  RadioButton radioButton2;

  Switch switchView;
  ImageView imageView;
  EditText editText;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    Log.e(TAG, "onCreate invoked");

    doInject();

    switchView.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
      @Override
      public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        refreshStateText();
        try {
          Thread.sleep(10000);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
        switchView.setText(
          "Switch view" + (switchView.isChecked() ? "on" : "off")
        );
      }
    });

    button.setOnClickListener(
      new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          refreshStateText();


        }
      });


  }

  public void refreshStateText() {
    StringBuilder sb = new StringBuilder();
    sb.append("Check Box value: ");
    sb.append(checkBox.isChecked());
    sb.append("\n");
    sb.append("Radio Button 1 value: ");
    sb.append(radioButton1.isChecked());
    sb.append("\n");
    sb.append("Radio Button 2 value: ");
    sb.append(radioButton2.isChecked());
    sb.append("\n");
    sb.append("Switch view value: ");
    sb.append(switchView.isChecked());
    sb.append("\n");
    sb.append("Edit Text value: ");
    sb.append(editText.getText());
    sb.append("\n");

    imageView.setImageDrawable(
      getDrawable(R.drawable.ic_launcher)
    );

    helloText.setText(sb.toString());
  }

  private void doInject() {
    helloText = (TextView) findViewById(R.id.hello_text);
    button = (Button) findViewById(R.id.start_b);
    checkBox = (CheckBox) findViewById(R.id.checkBox);
    radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
    radioButton1 = (RadioButton) findViewById(R.id.radioButton1);
    radioButton2 = (RadioButton) findViewById(R.id.radioButton2);

    switchView = (Switch) findViewById(R.id.switch2);
    imageView = (ImageView) findViewById(R.id.imageView);
    editText = (EditText) findViewById(R.id.editText);

  }

  @Override
  protected void onRestoreInstanceState(Bundle savedInstanceState) {
    super.onRestoreInstanceState(savedInstanceState);
    Toast.makeText(this, "" + savedInstanceState.getBoolean("povikan"), Toast.LENGTH_LONG).show();
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


  @Override
  protected void onSaveInstanceState(Bundle outState) {
    outState.putBoolean("povikan", true);
    super.onSaveInstanceState(outState);
    Toast.makeText(this, "save instance state", Toast.LENGTH_LONG).show();
  }

  public void startActivityB(View view) {

    Intent activityBIntent = new Intent(this, ActivityB.class);
    startActivity(activityBIntent);

  }
}
