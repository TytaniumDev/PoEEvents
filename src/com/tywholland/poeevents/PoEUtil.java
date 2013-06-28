package com.tywholland.poeevents;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.SimpleCursorAdapter;

public class PoEUtil {

	public static SimpleCursorAdapter getCursorAdapter(Context context,
			Cursor cursor) {
		// The desired columns to be bound
		String[] columns = new String[] { PoEEvent.TAG_EVENT_NAME,
				PoEEvent.TAG_DESCRIPTION, PoEEvent.TAG_START_TIME,
				PoEEvent.TAG_END_TIME };

		// the XML defined views which the data will be bound to
		int[] to = new int[] { R.id.event_name, R.id.event_description,
				R.id.event_start_time, R.id.event_end_time, };
		return new SimpleCursorAdapter(context, R.layout.event_listitem,
				cursor, columns, to, 0);
	}
}
