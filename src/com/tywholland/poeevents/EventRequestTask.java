package com.tywholland.poeevents;

import java.lang.reflect.Type;
import java.util.Collection;
import java.util.List;

import android.app.ListActivity;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.Log;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class EventRequestTask extends
		AsyncTask<Context, String, List<PoEEvent>> {
	private Context mContext;
	private ListView mListView;

	@Override
	protected List<PoEEvent> doInBackground(Context... params) {
		Log.i("POE", "doInBackground");
		if (params != null) {
			mContext = (Context) params[0];
			mListView = ((ListActivity) params[0]).getListView();
		}

		JSONParser parser = new JSONParser();
		String json = parser.getEventsJSON();
		Log.i("POE", "got events json");
		Gson gson = new Gson();
		Type collectionType = new TypeToken<Collection<PoEEvent>>() {
		}.getType();
		List<PoEEvent> events = gson.fromJson(json, collectionType);
		Log.i("POE", "got events list from json");
		for (PoEEvent event : events) {
			Log.i("POE", "Event name: " + event.getName());
		}
		return events;
	}

	@Override
	protected void onPostExecute(List<PoEEvent> result) {
		Log.i("POE", "onPostExecute");
		// Insert into database
		PoEEventsDataSource db = new PoEEventsDataSource(mContext);
		Log.i("POE", "before delete/insert");
		if (result.size() > 0) {
			db.deleteAllEvents();
			Log.i("POE", "deleted all events");
		}
		db.insertEvents(result);
		Log.i("POE", "inserted all events");

		// Make cursor adapter
		Cursor cursor = db.getAllEvents();
		Log.i("POE", "got all events");
		// The desired columns to be bound
		String[] columns = new String[] { PoEEvent.TAG_EVENT_NAME,
				PoEEvent.TAG_DESCRIPTION, PoEEvent.TAG_START_TIME,
				PoEEvent.TAG_END_TIME };

		// the XML defined views which the data will be bound to
		int[] to = new int[] { R.id.event_name, R.id.event_description,
				R.id.event_start_time, R.id.event_end_time, };
		mListView.setAdapter(new SimpleCursorAdapter(mContext,
				R.layout.event_listitem, cursor, columns, to, 0));
		Log.i("POE", "adapter set");

		super.onPostExecute(result);
	}

}
