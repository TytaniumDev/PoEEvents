package com.tywholland.poeevents;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class EventAlarmSetter extends BroadcastReceiver {
	@Override
	public void onReceive(Context context, Intent intent) {
		Intent service = new Intent(context, EventAlarmService.class);
		service.setAction(EventAlarmService.CREATE);
		context.startService(service);
	}
}
