package com.rollwithmicole.accessibilityfinder;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Http {
	static String TAG = "Http Class";

	public String read(String httpUrl) throws IOException {
		Log.d(TAG, "Starting Http Read - " + httpUrl);
		String httpData = "";
		InputStream inputStream = null;
		HttpURLConnection httpURLConnection = null;
		try {
			URL url = new URL(httpUrl);
			httpURLConnection = (HttpURLConnection) url.openConnection();
			httpURLConnection.connect();
			inputStream = httpURLConnection.getInputStream();
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
			StringBuffer stringBuffer = new StringBuffer();
			String line = "";
			while ((line = bufferedReader.readLine()) != null) {
				stringBuffer.append(line);
			}
			httpData = stringBuffer.toString();
			bufferedReader.close();
		} catch (Exception e) {
			Log.d("reading Http url", e.toString());
		} finally {
			inputStream.close();
			httpURLConnection.disconnect();
		}
		Log.d(TAG, "Finished Http Read");
		Log.d(TAG, httpData);
		return httpData;
	}
}