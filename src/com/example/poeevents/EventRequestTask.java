package com.example.poeevents;

import java.lang.reflect.Type;
import java.util.Collection;
import java.util.List;

import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class EventRequestTask extends AsyncTask<String, String, String>{

	@Override
	protected String doInBackground(String... params) {
		JSONParser parser = new JSONParser();
		String json = parser.getEventsJSON();
		Gson gson = new Gson();
		Type collectionType = new TypeToken<Collection<PoEEvent>>(){}.getType();
		List<PoEEvent> events = gson.fromJson(json, collectionType);
		for (PoEEvent event : events) {
			Log.i("POE", "Event name: " + event.getName());
		}
		return null;
	}

}
