package com.tywholland.poeevents;

import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
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

	public void insertEvents(List<PoEEvent> events) {
		for (PoEEvent event : events) {
			insertEvent(event);
		}
	}

	public void insertEvent(PoEEvent event) {
		if (database == null) {
			open();
		}
		ContentValues values = new ContentValues();
		values.put(PoEEvent.TAG_EVENT_NAME, event.getName());
		values.put(PoEEvent.TAG_DESCRIPTION, event.getDescription());
		values.put(PoEEvent.TAG_WEB_LINK, event.getWebLink());
		values.put(PoEEvent.TAG_REGISTER_TIME, event.getRegisterTime());
		values.put(PoEEvent.TAG_START_TIME, event.getStartTime());
		values.put(PoEEvent.TAG_END_TIME, event.getEndTime());
		database.insertWithOnConflict(SQLiteHelper.TABLE_EVENTS, null, values, SQLiteDatabase.CONFLICT_REPLACE);
	}

	public Cursor getAllEvents() {
		if (database == null) {
			open();
		}
		String select = "SELECT * from " + SQLiteHelper.TABLE_EVENTS;
		return database.rawQuery(select, null);
	}
}
