package com.rollwithmicole.accessibilityfinder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Places {

	public List<HashMap<String, String>> parse(JSONObject jsonObject) {
		JSONArray jsonArray = null;
		try {
			jsonArray = jsonObject.getJSONArray("results");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return getPlaces(jsonArray);
	}

	private List<HashMap<String, String>> getPlaces(JSONArray jsonArray) {
		int placesCount = jsonArray.length();
		List<HashMap<String, String>> placesList = new ArrayList<HashMap<String, String>>();
		HashMap<String, String> placeMap = null;

		for (int i = 0; i < placesCount; i++) {
			try {
				placeMap = getPlace((JSONObject) jsonArray.get(i));
				if(placeMap.get("hasDisabilityNotes") == "yes"){
					placesList.add(placeMap);
				}

			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return placesList;
	}

	private HashMap<String, String> getPlace(JSONObject googlePlaceJson) {
		HashMap<String, String> googlePlaceMap = new HashMap<String, String>();
		String placeName = "-NA-";
		String vicinity = "-NA-";
		String latitude = "";
		String longitude = "";
		String reference = "";
		String place_id = "";
		boolean hasDisabilityNotes = false;

		try {
			if (!googlePlaceJson.isNull("name")) {
				placeName = googlePlaceJson.getString("name");
			}
			if (!googlePlaceJson.isNull("vicinity")) {
				vicinity = googlePlaceJson.getString("vicinity");
			}
			latitude = googlePlaceJson.getJSONObject("geometry").getJSONObject("location").getString("lat");
			longitude = googlePlaceJson.getJSONObject("geometry").getJSONObject("location").getString("lng");
			reference = googlePlaceJson.getString("reference");
			place_id = googlePlaceJson.getString("place_id");
			googlePlaceMap.put("place_name", placeName);
			googlePlaceMap.put("vicinity", vicinity);
			googlePlaceMap.put("lat", latitude);
			googlePlaceMap.put("lng", longitude);
			googlePlaceMap.put("reference", reference);
			googlePlaceMap.put("place_id", place_id);
			JSONObject details = getPlaceDetails(place_id);
			if(details != null) {
				JSONArray reviews = details.getJSONArray("reviews");
				for (int i = 0; i < reviews.length(); i++) {
					JSONObject review = (JSONObject) reviews.get(i);
					hasDisabilityNotes = hasDisabilityNotes || review.getString("text").contains("ADA Notes");
				}
			}
			googlePlaceMap.put("hasDisabilityNotes", hasDisabilityNotes?"yes":"no");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return googlePlaceMap;
	}


	private JSONObject getPlaceDetails(String placeId){

		Http http = new Http();
		StringBuilder googlePlaceUrl = new StringBuilder("https://maps.googleapis.com/maps/api/place/details/json?");
		googlePlaceUrl.append("placeid=" + placeId);
		googlePlaceUrl.append("&key=" + R.string.web_api_key);
		try {
			String googlePlaceData = http.read(googlePlaceUrl.toString());
			return new JSONObject(googlePlaceData).getJSONObject("result");
		}
		catch (Exception e){

		}
		return null;
	}
}