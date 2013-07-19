package com.tywholland.poeevents;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class WidgetService extends RemoteViewsService {
	@Override
	public RemoteViewsFactory onGetViewFactory(Intent intent) {
		return new WidgetFactory(getApplicationContext());
	}
}

class WidgetFactory implements RemoteViewsService.RemoteViewsFactory {
	private Context mContext;
	private Cursor mCursor;

	public WidgetFactory(Context context) {
		this.mContext = context;
	}

	@Override
	public int getCount() {
		return mCursor != null ? mCursor.getCount() : 0;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public RemoteViews getLoadingView() {
		return null;
	}

	@Override
	public RemoteViews getViewAt(int position) {
		return PoEUtil.getRemoteView(mContext, mCursor, position);
	}

	@Override
	public int getViewTypeCount() {
		return 1;
	}

	@Override
	public boolean hasStableIds() {
		return true;
	}

	@Override
	public void onCreate() {
		// Set up cursors/connections to data source
		pullData();
	}

	@Override
	public void onDataSetChanged() {
		PoEUtil.showProgress(mContext, true);
		pullData();
	}

	private void pullData() {
		PoEEventsDataSource db = new PoEEventsDataSource(mContext);
		mCursor = db.getAllEvents();
		new EventRequestTask(mContext).execute();
	}

	@Override
	public void onDestroy() {
		mCursor = null;
	}
}
