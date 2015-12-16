package mk.ukim.finki.mpip.listviewshowcase.app;

import android.content.Intent;
import android.location.Address;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.ActionBarActivity;
import android.widget.Toast;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import mk.ukim.finki.mpip.listviewshowcase.app.tasks.GeocoderAddressListener;
import mk.ukim.finki.mpip.listviewshowcase.app.tasks.GeocodingTask;


public class MapActivity extends ActionBarActivity implements LocationListener {


  private GoogleMap map;
  // Declaring a Location Manager
  protected LocationManager locationManager;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_map);

    setUpMap();

    locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
    boolean gpsEnabled = locationManager
      .isProviderEnabled(LocationManager.GPS_PROVIDER);
    boolean netEnabled = locationManager
      .isProviderEnabled(LocationManager.NETWORK_PROVIDER);

    if (!gpsEnabled && !netEnabled) {
      startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
    }
  }

  private void setUpMap() {
    map = ((SupportMapFragment) getSupportFragmentManager()
      .findFragmentById(R.id.map)).getMap();

    GeocodingTask task = new GeocodingTask(this,
      new GeocoderAddressListener() {

        @Override
        public void onAddressObtained(Address address) {
          if (address != null) {
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(
              new LatLng(address.getLatitude(), address
                .getLongitude()), 10));
          } else {
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(
              new LatLng(42.0062403, 21.3559413), 10));
          }

        }
      });

    task.execute("Skopje");

    map.addMarker(new MarkerOptions().position(
      new LatLng(42.0062403, 21.3559413)).title("Marker"));

    map.setOnMapClickListener(
      new GoogleMap.OnMapClickListener() {

        @Override
        public void onMapClick(final LatLng point) {

          GeocodingTask task = new GeocodingTask(MapActivity.this,
            new GeocoderAddressListener() {

              @Override
              public void onAddressObtained(Address address) {
                if (address != null) {
                  map.addMarker(new MarkerOptions()
                    .position(point)
                    .title("" + address.getAddressLine(0))
                    .snippet(
                      address.getCountryCode()
                        + " - "
                        + address
                        .getCountryName())
                    .infoWindowAnchor(0.5f, 0.5f));
                }

              }
            });

          task.execute(point);
        }
      });

  }


  @Override
  protected void onResume() {
    super.onResume();

    locationManager.requestLocationUpdates(
      LocationManager.NETWORK_PROVIDER,
      1000,
      1,
      this);
    locationManager.requestLocationUpdates(
      LocationManager.PASSIVE_PROVIDER,
      1000,
      1,
      this);
  }

  @Override
  protected void onPause() {
    super.onPause();
    locationManager.removeUpdates(this);
  }

  @Override
  public void onLocationChanged(Location location) {
    Toast.makeText(this,
      location.getProvider() + ": " +
        location.getLatitude() + "-" + location.getLongitude()
        + "(" + location.getAccuracy() + "m)",
      Toast.LENGTH_LONG).show();
  }

  @Override
  public void onStatusChanged(String provider, int status, Bundle extras) {

  }

  @Override
  public void onProviderEnabled(String provider) {

  }

  @Override
  public void onProviderDisabled(String provider) {

  }
}
