package com.tywholland.poeevents;

import java.lang.reflect.Type;
import java.util.Collection;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class EventRequestTask extends AsyncTask<Object, String, List<PoEEvent>> {
	private Context context;
	private RequestCompleteCallback callback;

	public EventRequestTask(Context context, RequestCompleteCallback callback) {
		this.context = context;
		this.callback = callback;
	}

	@Override
	protected List<PoEEvent> doInBackground(Object... params) {
		JSONParser parser = new JSONParser();
		String json;
		List<PoEEvent> events = null;
		try {
			json = parser.getEventsJSON();
			Gson gson = new Gson();
			Type collectionType = new TypeToken<Collection<PoEEvent>>() {
			}.getType();
			events = gson.fromJson(json, collectionType);
			if (events != null) {
				// Insert into database
				PoEEventsDataSource db = new PoEEventsDataSource(context);
				db.insertEventsAndClean(events, context);
				db.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return events;
	}

	@SuppressLint("NewApi")
	@Override
	protected void onPostExecute(List<PoEEvent> result) {
		if (result != null) {
			callback.onEventRequestTaskSuccess();
		} else {
			callback.onEventRequestTaskFail();
		}
		PoEUtil.updateWidget(context);
		super.onPostExecute(result);
	}

	public interface RequestCompleteCallback {
		public void onEventRequestTaskSuccess();

		public void onEventRequestTaskFail();
	}
}
