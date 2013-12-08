package com.tywholland.poeevents;

import java.util.Date;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;

/**
 * This class creates or cancels alarms for events. It should be called when the
 * phone is powered on, and whenever the user checks the checkbox.
 * 
 * @author tylerholland
 * 
 */
public class EventAlarmService extends IntentService {

	public static final String CREATE = "CREATE";
	public static final String CREATE_SINGLE = "CREATESINGLE";
	public static final String CANCEL = "CANCEL";
	public static final String CANCEL_SINGLE = "CANCELSINGLE";
	public static final String NAME_EXTRA = "NAME";
	public static final String START_TIME_EXTRA = "STARTTIME";
	public static final String REG_TIME_EXTRA = "REGTIME";
	public static final String WEB_LINK_EXTRA = "WEBLINK";

	private AlarmManager mAlarmManager;
	
	public EventAlarmService() {
		super("EventAlarmService");
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		mAlarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
		String action = intent.getAction();
		if (CREATE.equals(action)) {
			executeCreate();
			return;
		} else if (CANCEL.equals(action)) {
			executeCancel();
			return;
		}

		String name = null;
		String startTime = null;
		String regTime = null;
		String webLink = null;
		if (intent.getExtras() != null) {
			if (intent.getExtras().get(NAME_EXTRA) != null
					&& intent.getExtras().get(START_TIME_EXTRA) != null
					&& intent.getExtras().get(WEB_LINK_EXTRA) != null) {
				name = intent.getExtras().getString(NAME_EXTRA);
				startTime = intent.getExtras().getString(START_TIME_EXTRA);
				webLink = intent.getExtras().getString(WEB_LINK_EXTRA);
				if (CANCEL_SINGLE.equals(action)) {
					executeCancelSingle(name, startTime, webLink);
					return;
				}
				if (intent.getExtras().get(REG_TIME_EXTRA) != null) {
					regTime = intent.getExtras().getString(REG_TIME_EXTRA);
					if (CREATE_SINGLE.equals(action)) {
						executeCreateSingle(name, startTime, webLink, regTime);
						return;
					}
				}
			}
		}
	}

	private void executeCreate() {
		Cursor cursor = getAllEventItemsCursor();
		if (cursor.moveToFirst()) {
			do {
				// Get data
				String name = cursor.getString(cursor
						.getColumnIndexOrThrow(PoEEvent.TAG_NORMAL_NAME));
				String startTime = cursor.getString(cursor
						.getColumnIndexOrThrow(PoEEvent.TAG_START_TIME));
				String webLink = cursor.getString(cursor
						.getColumnIndexOrThrow(PoEEvent.TAG_WEB_LINK));
				String regTime = cursor.getString(cursor
						.getColumnIndexOrThrow(PoEEvent.TAG_REGISTER_TIME));
				setAlarm(name, startTime, webLink, regTime);
			} while (cursor.moveToNext());
		}
		cursor.close();
	}

	private void executeCreateSingle(String name, String startTime,
			String webLink, String regTime) {
		Log.d("PoEEvents", "Creating alarm");
		setAlarm(name, startTime, webLink, regTime);
	}

	private void executeCancel() {
		Cursor cursor = getAllEventItemsCursor();
		if (cursor.moveToFirst()) {
			do {
				// Get data
				String name = cursor.getString(cursor
						.getColumnIndexOrThrow(PoEEvent.TAG_NORMAL_NAME));
				String startTime = cursor.getString(cursor
						.getColumnIndexOrThrow(PoEEvent.TAG_START_TIME));
				String webLink = cursor.getString(cursor
						.getColumnIndexOrThrow(PoEEvent.TAG_WEB_LINK));
				cancelAlarm(name, startTime, webLink);
			} while (cursor.moveToNext());
		}
		cursor.close();
	}

	private void executeCancelSingle(String name, String startTime,
			String webLink) {
		Log.d("PoEEvents", "Cancel alarm");
		cancelAlarm(name, startTime, webLink);
	}

	private void setAlarm(String name, String startTime, String webLink,
			String regTime) {
		// Make sure item isn't in the past
		Date now = new Date();
		if (now.before(PoEUtil.parseDbTimeIntoDate(startTime))) {
			long notificationTime = PoEUtil.parseDbTimeIntoDate(regTime)
					.getTime();
			mAlarmManager.set(AlarmManager.RTC_WAKEUP, notificationTime,
					createPendingIntent(name, startTime, webLink));
		}
	}

	private void cancelAlarm(String name, String startTime, String webLink) {
		mAlarmManager.cancel(createPendingIntent(name, startTime, webLink));
	}

	private Cursor getAllEventItemsCursor() {
		return new PoEEventsDataSource(this).getAllAlarmEvents();
	}

	private PendingIntent createPendingIntent(String name, String startTime,
			String webLink) {
		Intent i = new Intent(this, EventAlarmReceiver.class);
		i.setAction(webLink);
		i.putExtra(EventAlarmReceiver.ALARM_KEY_NAME, name);
		i.putExtra(EventAlarmReceiver.ALARM_KEY_START_TIME, startTime);
		i.putExtra(EventAlarmReceiver.ALARM_KEY_WEB_LINK, webLink);
		return PendingIntent.getBroadcast(this,
				PoEUtil.getIdFromWebLink(webLink), i,
				PendingIntent.FLAG_UPDATE_CURRENT);
	}
}
