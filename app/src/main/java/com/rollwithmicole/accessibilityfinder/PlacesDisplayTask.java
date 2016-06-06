package com.rollwithmicole.accessibilityfinder;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

public class PlacesDisplayTask extends AsyncTask<Object, Integer, List<HashMap<String, String>>> {

	JSONObject googlePlacesJson;
	GoogleMap googleMap;
	static String TAG = "Places Display Task";

	@Override
	protected List<HashMap<String, String>> doInBackground(Object... inputObj) {
		Log.d(TAG, "starting task");
		List<HashMap<String, String>> googlePlacesList = null;
		Places placeJsonParser = new Places();

		try {
			googleMap = (GoogleMap) inputObj[0];
			googlePlacesJson = new JSONObject((String) inputObj[1]);
			googlePlacesList = placeJsonParser.parse(googlePlacesJson);
		} catch (Exception e) {
			Log.d("Exception", e.toString());
		}
		return googlePlacesList;
	}

	@Override
	protected void onPostExecute(List<HashMap<String, String>> list) {
		googleMap.clear();
		for (int i = 0; i < list.size(); i++) {
			try {
				MarkerOptions markerOptions = new MarkerOptions();
				HashMap<String, String> googlePlace = list.get(i);
				double lat = Double.parseDouble(googlePlace.get("lat"));
				double lng = Double.parseDouble(googlePlace.get("lng"));
				String placeName = googlePlace.get("place_name");
				String vicinity = googlePlace.get("vicinity");
				LatLng latLng = new LatLng(lat, lng);
				markerOptions.position(latLng);
				markerOptions.title(placeName + " : " + vicinity);
				Marker marker = googleMap.addMarker(markerOptions);
				MapActivity.addMarker(marker, googlePlace.get("place_id"));
			} catch (Exception e) {

			}
		}
	}
}