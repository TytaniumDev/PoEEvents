package com.tywholland.poeevents;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;

public class EventAlarmReceiver extends BroadcastReceiver {
	public static String ALARM_KEY_NAME = "name";
	public static String ALARM_KEY_START_TIME = "starttime";
	public static String ALARM_KEY_WEB_LINK = "link";

	@Override
	public void onReceive(Context context, Intent intent) {
		String name = intent.getStringExtra(ALARM_KEY_NAME);
		String webLink = intent.getStringExtra(ALARM_KEY_WEB_LINK);
		String startTimeString = intent.getStringExtra(ALARM_KEY_START_TIME);
		// ID is the last part of forum link
		int id = PoEUtil.getIdFromWebLink(webLink);

		final Intent forumLinkIntent = new Intent(Intent.ACTION_VIEW,
				Uri.parse(webLink));

		PendingIntent forumLinkPendingIntent = PendingIntent
				.getActivity(context, id, forumLinkIntent,
						PendingIntent.FLAG_UPDATE_CURRENT);

		NotificationCompat.Builder eventNotificationBuilder = new NotificationCompat.Builder(
				context);
		eventNotificationBuilder
				.setSmallIcon(R.drawable.ic_notification)
				.setContentTitle(name)
				.setContentText(
						context.getResources()
								.getString(
										R.string.starts_at,
										PoEUtil.parseDbTimeIntoNotificationString(startTimeString)))
				.setDefaults(Notification.DEFAULT_ALL).setAutoCancel(true)
				.setContentIntent(forumLinkPendingIntent);

		Notification eventNotification = eventNotificationBuilder.build();

		NotificationManager nm = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);
		nm.notify(id, eventNotification);
	}
}
