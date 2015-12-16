package mk.ukim.finki.mpip.listviewshowcase.app.tasks;

import android.location.Address;

public interface GeocoderAddressListener {

  void onAddressObtained(Address address);
}
