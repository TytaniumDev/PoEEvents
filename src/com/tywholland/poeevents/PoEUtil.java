package com.tywholland.poeevents;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import android.annotation.SuppressLint;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.widget.CursorAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;

public class PoEUtil {

	public static CursorAdapter getCursorAdapter(Context context, Cursor cursor) {
		return new PoECursorAdapter(context, cursor, 0);
	}

	@SuppressLint("SimpleDateFormat")
	private static class PoECursorAdapter extends CursorAdapter {
		private static SimpleDateFormat mZDateFormat = new SimpleDateFormat(
				"yyyy-MM-dd'T'HH:mm:ss'Z'");
		private static SimpleDateFormat mLocalDateFormat = new SimpleDateFormat(
				"MMMM d, h:mm aa");

		public PoECursorAdapter(Context context, Cursor cursor, int flags) {
			super(context, cursor, flags);
			mZDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
			mLocalDateFormat.setTimeZone(TimeZone.getDefault());
		}

		@Override
		public void bindView(View view, final Context context, Cursor cursor) {
			// Get data
			String eventName = cursor.getString(cursor
					.getColumnIndexOrThrow(PoEEvent.TAG_EVENT_NAME));
			String startTime = cursor.getString(cursor
					.getColumnIndexOrThrow(PoEEvent.TAG_START_TIME));
			String endTime = cursor.getString(cursor
					.getColumnIndexOrThrow(PoEEvent.TAG_END_TIME));

			// Get views
			TextView eventNameView = (TextView) view
					.findViewById(R.id.event_name);
			TextView startTimeView = (TextView) view
					.findViewById(R.id.event_start_time);
			TextView endTimeView = (TextView) view
					.findViewById(R.id.event_end_time);

			// Set views
			eventNameView.setText(eventName);

			// Convert times to local
			String startTimeText = context.getResources().getString(
					R.string.error);
			String endTimeText = context.getResources().getString(
					R.string.error);
			try {
				Date startDate = mZDateFormat.parse(startTime);
				Date endDate = mZDateFormat.parse(endTime);

				startTimeText = mLocalDateFormat.format(startDate);
				endTimeText = mLocalDateFormat.format(endDate);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			startTimeView.setText(startTimeText);
			endTimeView.setText(endTimeText);

			// Add forum link on click
			String forumLink = cursor.getString(cursor
					.getColumnIndexOrThrow(PoEEvent.TAG_WEB_LINK));
			if (forumLink != null && !forumLink.isEmpty()) {
				final Intent forumLinkIntent = new Intent(Intent.ACTION_VIEW,
						Uri.parse(forumLink));
				view.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						context.startActivity(forumLinkIntent);
					}
				});
			} else {
				view.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						Toast.makeText(context, R.string.no_forum_link,
								Toast.LENGTH_SHORT).show();
					}
				});
			}
		}

		@Override
		public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
			return LayoutInflater.from(context).inflate(
					R.layout.event_listitem, viewGroup, false);
		}
	}

	public static void updateWidget(Context context) {
		AppWidgetManager appWidgetManager = AppWidgetManager
				.getInstance(context);
		int[] appWidgetIds = appWidgetManager
				.getAppWidgetIds(new ComponentName(context,
						WidgetProvider.class));
		// update each of the app widgets with the remote adapter
		for (int i = 0; i < appWidgetIds.length; ++i) {
			appWidgetManager.updateAppWidget(appWidgetIds[i],
					PoEUtil.getFullRemoteViews(context, appWidgetIds[i]));
		}
	}

	@SuppressWarnings("deprecation")
	public static RemoteViews getFullRemoteViews(Context context,
			int appWidgetId) {
		Log.d("POE", "in getFullRemoteViews");
		// Set up the intent that starts the StackViewService, which will
		// provide the views for this collection.
		Intent intent = new Intent(context, WidgetService.class);
		// Add the app widget ID to the intent extras.
		intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
		intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));
		// Instantiate the RemoteViews object for the App Widget layout.
		RemoteViews rv = new RemoteViews(context.getPackageName(),
				R.layout.widget);
		// Set up the RemoteViews object to use a RemoteViews adapter.
		// This adapter connects
		// to a RemoteViewsService through the specified intent.
		// This is how you populate the data.
		rv.setRemoteAdapter(appWidgetId, R.id.widget_listview, intent);

		// The empty view is displayed when the collection has no items.
		// It should be in the same layout used to instantiate the
		// RemoteViews
		// object above.
		rv.setEmptyView(R.id.widget_listview, R.id.empty_view);
		return rv;
	}

	public static RemoteViews getRemoteView(Context context, Cursor cursor,
			int position) {
		Log.d("POE", "in getRemoteView");
		RemoteViews row = new RemoteViews(context.getPackageName(),
				R.layout.event_listitem);
		if (cursor.moveToPosition(position)) {
			row.setTextViewText(R.id.event_name, cursor.getString(cursor
					.getColumnIndexOrThrow(PoEEvent.TAG_EVENT_NAME)));

			row.setTextViewText(R.id.event_start_time, cursor.getString(cursor
					.getColumnIndexOrThrow(PoEEvent.TAG_EVENT_NAME)));
			
			row.setTextViewText(R.id.event_end_time, cursor.getString(cursor
					.getColumnIndexOrThrow(PoEEvent.TAG_EVENT_NAME)));
			
//			row.setOnClickFillInIntent(
//					R.id.agenda_widget_listview,
//					ItemDetailsFragmentActivity.createItemDetailsIntent(
//							mCursor.getInt(IItemsQuery.ITEM_ID), mContext));
		}
		return row;
	}
}
