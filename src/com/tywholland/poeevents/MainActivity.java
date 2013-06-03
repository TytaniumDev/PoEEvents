package com.tywholland.poeevents;

import android.app.ListActivity;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.Menu;

public class MainActivity extends ListActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Make cursor adapter
		PoEEventsDataSource db = new PoEEventsDataSource(this);
		Cursor cursor = db.getAllEvents();
		// The desired columns to be bound
		String[] columns = new String[] { PoEEvent.TAG_EVENT_NAME,
				PoEEvent.TAG_DESCRIPTION, PoEEvent.TAG_START_TIME,
				PoEEvent.TAG_END_TIME };

		// the XML defined views which the data will be bound to
		int[] to = new int[] { R.id.event_name, R.id.event_description,
				R.id.event_start_time, R.id.event_end_time, };
		getListView().setAdapter(
				new SimpleCursorAdapter(this, R.layout.event_listitem, cursor,
						columns, to, 0));

		new EventRequestTask().execute(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		new PoEEventsDataSource(this).close();
	}
}
