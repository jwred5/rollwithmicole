package com.rollwithmicole.accessibilityfinder;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.GoogleMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class GooglePlaceReadTask extends AsyncTask<Object, Integer, String> {
	Activity activity = null;
	String googlePlaceData;

	@Override
	protected String doInBackground(Object... inputObj) {
		try {
			activity = (Activity) inputObj[0];
			String googlePlaceUrl = (String) inputObj[1];
			Http http = new Http();
			googlePlaceData = http.read(googlePlaceUrl);
		} catch (Exception e) {
			Log.d("Google Place Read Task", e.toString());
		}
		return googlePlaceData;
	}

	@Override
	protected void onPostExecute(String result){
		AlertDialog.Builder builder = new AlertDialog.Builder(activity);
		String description = "";
		try {
			JSONObject parser = new JSONObject(googlePlaceData);
			JSONObject details = parser.getJSONObject("result");
			if(details != null) {
				JSONArray reviews = details.getJSONArray("reviews");
				for (int i = 0; i < reviews.length(); i++) {
					JSONObject review = (JSONObject) reviews.get(i);
					description += i + ":" + review.getString("text");
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		builder.setMessage(description)
				.setNegativeButton("Close", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						// User cancelled the dialog
					}
				});
		// Create the AlertDialog object and return it
		builder.create().show();
	}
}