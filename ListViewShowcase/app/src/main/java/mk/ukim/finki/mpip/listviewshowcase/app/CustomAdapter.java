package mk.ukim.finki.mpip.listviewshowcase.app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ristes on 10/14/15.
 */
public class CustomAdapter extends BaseAdapter {

  private List<Person> persons;
  private Context ctx;

  private LayoutInflater inflater;

  public CustomAdapter(Context ctx, List<Person> data) {
    this.ctx = ctx;
    inflater =
      (LayoutInflater)
        ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    if (data != null) {
      persons = data;
    } else {
      persons = new ArrayList<Person>();
    }
  }

  public void add(Person person) {
    persons.add(person);
    notifyDataSetChanged();
  }

  @Override
  public int getCount() {
    return persons.size();
  }

  @Override
  public Object getItem(int position) {
    return persons.get(position);
  }

  @Override
  public long getItemId(int position) {
    return position;
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    return getViewFastest(position, convertView, parent);
  }

  public View getViewSlow(int position,
                          View convertView,
                          ViewGroup parent) {

    RelativeLayout layout = (RelativeLayout) inflater.inflate(R.layout.item_person, null);
    ImageView picture = (ImageView) layout.findViewById(R.id.person_image);
    TextView name = (TextView) layout.findViewById(R.id.name);
    TextView lastName = (TextView) layout.findViewById(R.id.last_name);
    TextView visits = (TextView) layout.findViewById(R.id.visits);

    Person person = (Person) getItem(position);
    name.setText(person.name);
    lastName.setText(person.lastName);
    visits.setText("" + person.visits);

    if (convertView != null) {
      System.out.println(convertView.getTag() + " : " + position);
    }

    layout.setTag(position);

    return layout;
  }

  public View getViewFast(int position,
                          View convertView,
                          ViewGroup parent) {
    RelativeLayout layout;
    if (convertView != null) {
      layout = (RelativeLayout) convertView;
    } else {
      layout = (RelativeLayout) inflater.inflate(R.layout.item_person, null);
    }
    ImageView picture = (ImageView) layout.findViewById(R.id.person_image);
    TextView name = (TextView) layout.findViewById(R.id.name);
    TextView lastName = (TextView) layout.findViewById(R.id.last_name);
    TextView visits = (TextView) layout.findViewById(R.id.visits);

    Person person = (Person) getItem(position);
    name.setText(person.name);
    lastName.setText(person.lastName);
    visits.setText("" + person.visits);

    if (convertView != null) {
      System.out.println(convertView.getTag() + " : " + position);
    }

    layout.setTag(position);

    return layout;
  }


  public View getViewFastest(int position,
                             View convertView,
                             ViewGroup parent) {
    Holder holder;
    if (convertView == null) {
      holder = new Holder();
      convertView =
        holder.layout =
          (RelativeLayout) inflater.inflate(R.layout.item_person, null);
      holder.picture = (ImageView) holder.layout.findViewById(R.id.person_image);
      holder.name = (TextView) holder.layout.findViewById(R.id.name);
      holder.lastName = (TextView) holder.layout.findViewById(R.id.last_name);
      holder.visits = (TextView) holder.layout.findViewById(R.id.visits);

      convertView.setTag(holder);
    }

    holder = (Holder) convertView.getTag();


    Person person = (Person) getItem(position);
    holder.name.setText(person.name);
    holder.lastName.setText(person.lastName);
    holder.visits.setText("" + person.visits);

    return convertView;
  }

  static class Holder {
    RelativeLayout layout;
    ImageView picture;
    TextView name;
    TextView lastName;
    TextView visits;
  }


}
