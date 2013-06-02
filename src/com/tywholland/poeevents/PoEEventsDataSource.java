package com.tywholland.poeevents;

import android.content.ContentValues;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class PoEEventsDataSource {
	// Database fields
	private SQLiteDatabase database;
	private SQLiteHelper dbHelper;

	public PoEEventsDataSource(Context context) {
		dbHelper = new SQLiteHelper(context);
	}

	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}

	public void close() {
		dbHelper.close();
	}

	public void createEvent(PoEEvent event) {
		ContentValues values = new ContentValues();
		values.put(PoEEvent.TAG_EVENT_NAME, event.getName());
		values.put(PoEEvent.TAG_DESCRIPTION, event.getDescription());
		values.put(PoEEvent.TAG_WEB_LINK, event.getWebLink());
		values.put(PoEEvent.TAG_REGISTER_TIME, event.getRegisterTime());
		values.put(PoEEvent.TAG_START_TIME, event.getStartTime());
		values.put(PoEEvent.TAG_END_TIME, event.getEndTime());
		database.insert(SQLiteHelper.TABLE_EVENTS, null,
				values);
	}
}
