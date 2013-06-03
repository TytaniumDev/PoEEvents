package com.tywholland.poeevents;

import java.lang.reflect.Type;
import java.util.Collection;
import java.util.List;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class EventRequestTask extends
		AsyncTask<Context, String, List<PoEEvent>> {
	private Context mContext;

	@Override
	protected List<PoEEvent> doInBackground(Context... params) {
		if (params != null) {
			mContext = (Context) params[0];
		}
		JSONParser parser = new JSONParser();
		String json = parser.getEventsJSON();
		Gson gson = new Gson();
		Type collectionType = new TypeToken<Collection<PoEEvent>>() {
		}.getType();
		List<PoEEvent> events = gson.fromJson(json, collectionType);
		for (PoEEvent event : events) {
			Log.i("POE", "Event name: " + event.getName());
		}
		return events;
	}

	@Override
	protected void onPostExecute(List<PoEEvent> result) {
		// Insert into database
		PoEEventsDataSource db = new PoEEventsDataSource(mContext);
		db.insertEvents(result);
		super.onPostExecute(result);
	}

}
