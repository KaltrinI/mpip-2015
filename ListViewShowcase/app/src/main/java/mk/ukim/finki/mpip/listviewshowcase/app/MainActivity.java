package mk.ukim.finki.mpip.listviewshowcase.app;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends Activity {

  EditText newItemText;
  ListView itemsList;
  ArrayAdapter<String> adapter;

  List<Person> persons = new ArrayList<Person>();

  CustomAdapter customAdapter;

  List<String> items = new ArrayList<String>();

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    doInject();
  }

  private void doInject() {
    newItemText = (EditText) findViewById(R.id.item_value);
    itemsList = (ListView) findViewById(R.id.items_list);

    adapter = new ArrayAdapter<String>(
      this,
      android.R.layout.simple_list_item_1,
      items);

    customAdapter = new CustomAdapter(this, persons);
    itemsList.setAdapter(customAdapter);
  }

  public void addItem(View view) {
    String itemText = newItemText.getText().toString();
//    items.add(itemText);
//    newItemText.setText("");
//    adapter.notifyDataSetChanged();
    System.out.println("adding: " + itemText);

    for (int i = 0; i < 1000; i++) {
      Person p = new Person();
      p.name = itemText + " " + i;
      p.lastName = itemText + " " + i;
      p.visits = 0;

      customAdapter.add(p);
    }

  }


}

