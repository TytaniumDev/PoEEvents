package com.example.poeevents;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

public class JSONParser {
	private static final String POE_EVENTS_URL = "http://api.pathofexile.com/leagues?type=event";

	public String getEventsJSON() {
		// Making HTTP request
		try {
			// defaultHttpClient
			DefaultHttpClient httpClient = new DefaultHttpClient();
			HttpGet httpGet = new HttpGet(POE_EVENTS_URL);

			HttpResponse httpResponse = httpClient.execute(httpGet);
			HttpEntity httpEntity = httpResponse.getEntity();
			return EntityUtils.toString(httpEntity);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
