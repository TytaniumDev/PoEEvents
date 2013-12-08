package com.tywholland.poeevents;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLiteHelper extends SQLiteOpenHelper {
	private static final int DATABASE_VERSION = 2;
	private static final String DATABASE_NAME = "events.db";
	public static final String TABLE_EVENTS = "events";

	private static final String DATABASE_CREATE = "create table "
			+ TABLE_EVENTS + "(_id integer, " + PoEEvent.TAG_EVENT_NAME
			+ " text primary key not null, " + PoEEvent.TAG_NORMAL_NAME
			+ " text, " + PoEEvent.TAG_CLASSIFICATION + " text, "
			+ PoEEvent.TAG_WEB_LINK + " text, " + PoEEvent.TAG_REGISTER_TIME
			+ " text, " + PoEEvent.TAG_START_TIME + " text, "
			+ PoEEvent.TAG_END_TIME + " text, " + PoEEvent.TAG_UPDATED
			+ " bit not null default 0, " + PoEEvent.TAG_ALERT
			+ " bit not null default 0" + ");";

	public SQLiteHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase database) {
		database.execSQL(DATABASE_CREATE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase database, int oldVersion,
			int newVersion) {
		database.execSQL("DROP TABLE IF EXISTS " + TABLE_EVENTS);
		onCreate(database);
	}
}
