package com.tywholland.poeevents;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.widget.RemoteViews;

public class PoEUtil {
	@SuppressLint("SimpleDateFormat")
	private static SimpleDateFormat mZDateFormat = new SimpleDateFormat(
			"yyyy-MM-dd'T'HH:mm:ss'Z'");
	@SuppressLint("SimpleDateFormat")
	private static SimpleDateFormat mLocalDateFormat = new SimpleDateFormat(
			"EE MMM d, h:mm aa");
	private static DateFormat mNotificationDateFormat = SimpleDateFormat
			.getTimeInstance(SimpleDateFormat.SHORT);

	static {
		mZDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
		mLocalDateFormat.setTimeZone(TimeZone.getDefault());
		mNotificationDateFormat.setTimeZone(TimeZone.getDefault());
	}

	public static String getClassification(String letter, Resources res) {
		String letterCompare = letter.toLowerCase(Locale.US);
		if ("c".equals(letterCompare)) {
			return res.getString(R.string.competitive);
		} else if ("s".equals(letterCompare)) {
			return res.getString(R.string.signature);
		} else if ("f".equals(letterCompare)) {
			return res.getString(R.string.fun);
		} else {
			return "";
		}
	}

	public static Date parseDbTimeIntoDate(String dbTime) {
		try {
			return mZDateFormat.parse(dbTime);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return new Date();
	}

	public static String parseDbTimeIntoNotificationString(String dbTime) {
		try {
			return mNotificationDateFormat.format(mZDateFormat.parse(dbTime));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return "ERROR";
	}

	public static int getIdFromWebLink(String forumLink) {
		String[] forumLinkSplit = forumLink.split("\\/");
		return Integer.parseInt(forumLinkSplit[forumLinkSplit.length - 1]);
	}

	public static String getLocalTimeString(String dbTime, Context context) {
		String localTime = context.getResources().getString(R.string.error);
		Date date = parseDbTimeIntoDate(dbTime);
		localTime = mLocalDateFormat.format(date);
		return localTime;
	}

	public static void updateWidget(Context context) {
		AppWidgetManager appWidgetManager = AppWidgetManager
				.getInstance(context);
		// update each of the app widgets with the remote adapter
		appWidgetManager.updateAppWidget(new ComponentName(context,
				WidgetProvider.class), getFullRemoteViews(context));
	}

	@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
	public static RemoteViews getFullRemoteViews(Context context) {
		// Set up the intent that starts the StackViewService, which will
		// provide the views for this collection.
		Intent intent = new Intent(context, WidgetService.class);
		// Add the app widget ID to the intent extras.
		intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));
		// Instantiate the RemoteViews object for the App Widget layout.
		RemoteViews rv = new RemoteViews(context.getPackageName(),
				R.layout.widget);
		// Set up the RemoteViews object to use a RemoteViews adapter.
		// This adapter connects
		// to a RemoteViewsService through the specified intent.
		// This is how you populate the data.
		rv.setRemoteAdapter(R.id.widget_listview, intent);
		return rv;
	}

	public static RemoteViews getRemoteView(Context context, Cursor cursor,
			int position) {
		RemoteViews row = new RemoteViews(context.getPackageName(),
				R.layout.widget_event_listitem);
		if (cursor.moveToPosition(position)) {
			row.setTextViewText(R.id.event_name, cursor.getString(cursor
					.getColumnIndexOrThrow(PoEEvent.TAG_NORMAL_NAME)));

			row.setTextViewText(
					R.id.event_start_time,
					getLocalTimeString(cursor.getString(cursor
							.getColumnIndexOrThrow(PoEEvent.TAG_START_TIME)),
							context));

			row.setTextViewText(
					R.id.event_end_time,
					getLocalTimeString(cursor.getString(cursor
							.getColumnIndexOrThrow(PoEEvent.TAG_END_TIME)),
							context));
		}
		return row;
	}
}
