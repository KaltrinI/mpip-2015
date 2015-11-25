package mk.ukim.finki.mpip.listviewshowcase.app;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by ristes on 10/14/15.
 */
public class Person implements Parcelable {

  public Long id;

  public String name;

  public String lastName;

  public String imageUrl;

  public int visits;

  public Person(){}


  public Person(Parcel in) {
    readFromParcel(in);
  }

  @Override
  public String toString() {
    return name + " " + lastName;
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(name);
    dest.writeString(lastName);
    dest.writeString(imageUrl);
    dest.writeInt(visits);
  }

  private void readFromParcel(Parcel in) {
    name = in.readString();
    lastName = in.readString();
    imageUrl = in.readString();
    visits = in.readInt();
  }


  public static final Parcelable.Creator<Person> CREATOR
    = new Parcelable.Creator<Person>() {

    public Person createFromParcel(Parcel in) {
      return new Person(in);
    }

    public Person[] newArray(int size) {
      return new Person[size];
    }
  };
}
