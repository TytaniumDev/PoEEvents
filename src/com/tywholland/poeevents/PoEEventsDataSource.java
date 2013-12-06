package com.tywholland.poeevents;

import java.text.ParseException;
import java.util.Date;
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

	public void insertEventsAndClean(List<PoEEvent> events) {
		// Insert events with updated field = 1. Events in the past will not be
		// replaced, and their updated field will be 0. After insert/updating,
		// remove all rows with updated = 0, and then update all rows and set
		// updated = 0 so they can be cleaned during the next insert/update.
		insertEvents(events);
		deleteAllNonUpdatedEvents();
		setAllEventsToNotUpdated();
	}

	private void insertEvents(List<PoEEvent> events) {
		Date currentTime = new Date();
		for (PoEEvent event : events) {
			try {
				Date eventEndDate = PoEUtil.parseDbTimeIntoDate(event
						.getEndTime());
				// Only add events that haven't happened already, sometimes GGG
				// returns old events
				if (currentTime.before(eventEndDate)) {
					insertEvent(event);
				}
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
	}

	private void insertEvent(PoEEvent event) {
		if (database == null) {
			open();
		}
		ContentValues values = new ContentValues();
		values.put(PoEEvent.TAG_EVENT_NAME, event.getName());
		values.put(PoEEvent.TAG_WEB_LINK, event.getWebLink());
		values.put(PoEEvent.TAG_REGISTER_TIME, event.getRegisterTime());
		values.put(PoEEvent.TAG_START_TIME, event.getStartTime());
		values.put(PoEEvent.TAG_END_TIME, event.getEndTime());
		// Set updated to 1
		values.put(PoEEvent.TAG_UPDATED, 1);
		database.insertWithOnConflict(SQLiteHelper.TABLE_EVENTS, null, values,
				SQLiteDatabase.CONFLICT_REPLACE);
	}

	public Cursor getAllEvents() {
		if (database == null) {
			open();
		}
		String select = "SELECT * from " + SQLiteHelper.TABLE_EVENTS
				+ " ORDER BY " + PoEEvent.TAG_START_TIME + " ASC";
		return database.rawQuery(select, null);
	}

	private void deleteAllNonUpdatedEvents() {
		if (database == null) {
			open();
		}
		// Non updated events will have an updated value of 0
		database.delete(SQLiteHelper.TABLE_EVENTS, PoEEvent.TAG_UPDATED + "=?",
				new String[] { "0" });
	}

	private void setAllEventsToNotUpdated() {
		if (database == null) {
			open();
		}
		// Set all existing events to updated = 0 so they will be cleaned
		ContentValues values = new ContentValues();
		values.put(PoEEvent.TAG_UPDATED, 0);
		database.update(SQLiteHelper.TABLE_EVENTS, values, null, null);
	}
}
