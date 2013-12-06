package com.tywholland.poeevents;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;

public class WidgetProvider extends AppWidgetProvider {

	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {

		new EventRequestTask(context,
				new EventRequestTask.RequestCompleteCallback() {
					@Override
					public void onEventRequestTaskSuccess() {
					}

					@Override
					public void onEventRequestTaskFail() {
					}
				}).execute();

		super.onUpdate(context, appWidgetManager, appWidgetIds);
	}
}
