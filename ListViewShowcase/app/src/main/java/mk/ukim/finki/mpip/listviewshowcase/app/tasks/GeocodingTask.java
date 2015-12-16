package mk.ukim.finki.mpip.listviewshowcase.app.tasks;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import com.google.android.gms.maps.model.LatLng;

import java.util.List;

public class GeocodingTask extends AsyncTask<Object, Void, Address> {

	private Geocoder gCoder;
	private GeocoderAddressListener listener;

	public GeocodingTask(Context context, GeocoderAddressListener listener) {
		gCoder = new Geocoder(context);
		this.listener = listener;
	}

	@Override
	protected Address doInBackground(Object... params) {

		List<Address> addresses = null;
		Address clickedAddress = null;
		try {

			if (params.length > 0) {
				if (params[0] instanceof LatLng) {
					LatLng point = (LatLng) params[0];
					addresses = gCoder.getFromLocation(point.latitude,
							point.longitude, 1);
				} else if (params[0] instanceof String) {
					addresses = gCoder.getFromLocationName((String) params[0],
							1);
				} else {
					System.err.println("Invalid paramethers");
				}
			}
			if (addresses != null && addresses.size() > 0) {
				clickedAddress = addresses.get(0);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return clickedAddress;
	}

	@Override
	protected void onPostExecute(Address result) {
		super.onPostExecute(result);
		listener.onAddressObtained(result);
	}

}
