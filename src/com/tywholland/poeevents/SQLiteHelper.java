package com.tywholland.poeevents;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLiteHelper extends SQLiteOpenHelper {
	private static final int DATABASE_VERSION = 1;
	private static final String DATABASE_NAME = "events.db";
	public static final String TABLE_EVENTS = "events";
	private static final String COLUMN_ID = "_id";

	private static final String DATABASE_CREATE = "create table "
			+ TABLE_EVENTS + "(" + COLUMN_ID
			+ " integer primary key autoincrement, " + PoEEvent.TAG_EVENT_NAME
			+ " text not null, " + PoEEvent.TAG_DESCRIPTION
			+ " text not null, " + PoEEvent.TAG_WEB_LINK + " text not null, "
			+ PoEEvent.TAG_REGISTER_TIME + " text not null, "
			+ PoEEvent.TAG_START_TIME + " text not null, "
			+ PoEEvent.TAG_END_TIME + " text not null);";

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
