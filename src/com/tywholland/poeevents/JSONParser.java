package com.tywholland.poeevents;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.util.Log;

public class JSONParser {
	private static final String POE_EVENTS_URL = "http://api.pathofexile.com/leagues?type=event";

	public String getEventsJSON() throws BadStatusException {
		// Making HTTP request
		try {
			// defaultHttpClient
			DefaultHttpClient httpClient = new DefaultHttpClient();
			HttpGet httpGet = new HttpGet(POE_EVENTS_URL);

			HttpResponse httpResponse = httpClient.execute(httpGet);
			if (httpResponse.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
				throw new BadStatusException(httpResponse.getStatusLine().getStatusCode());
			}
			HttpEntity httpEntity = httpResponse.getEntity();
			return EntityUtils.toString(httpEntity);
		} catch (BadStatusException e) {
			Log.e("PoEEvents", "Response was " + e.statusCode);
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public class BadStatusException extends Exception {
		public int statusCode;
		public BadStatusException(int statusCode) {
			this.statusCode = statusCode;
		}

		private static final long serialVersionUID = 5083316331870499653L;
	}

}
