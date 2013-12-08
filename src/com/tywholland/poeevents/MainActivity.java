package com.tywholland.poeevents;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.View;
import android.widget.ListView;

public class MainActivity extends Activity {
	private ListView mListView;
	private MenuItem mRefreshMenuItem;
	private PoEEventsDataSource mDB;
	private View mListLoadingSpinner;
	private View mListEmptyText;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.poeevents_listview);
		mListLoadingSpinner = findViewById(android.R.id.empty);
		mListEmptyText = findViewById(R.id.list_empty_text);
		mListView = (ListView) findViewById(android.R.id.list);
		mListView.setEmptyView(mListLoadingSpinner);

		mDB = new PoEEventsDataSource(this);
		mListView.setAdapter(new PoECursorAdapter(this, mDB.getAllEvents(), 0));
		startEventRequestTask();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		mRefreshMenuItem = menu.findItem(R.id.refresh);
		mRefreshMenuItem
				.setOnMenuItemClickListener(new OnMenuItemClickListener() {
					@Override
					public boolean onMenuItemClick(MenuItem item) {
						startEventRequestTask();
						return false;
					}
				});
		startEventRequestTask();
		return true;
	}

	private void startEventRequestTask() {
		if (mRefreshMenuItem != null && mListView != null) {
			// Everything is created
			startLoadingSpinners();
			final Context context = this;
			EventRequestTask task = new EventRequestTask(this,
					new EventRequestTask.RequestCompleteCallback() {
						@Override
						public void onEventRequestTaskSuccess() {
							mListView.setAdapter(new PoECursorAdapter(context,
									mDB.getAllEvents(), 0));
							stopLoadingSpinners();
						}

						@Override
						public void onEventRequestTaskFail() {
							stopLoadingSpinners();
						}
					});
			task.execute();
		}
	}

	@SuppressLint("NewApi")
	private void startLoadingSpinners() {
		if (android.os.Build.VERSION.SDK_INT > 8) {
			mRefreshMenuItem.setActionView(R.layout.menu_refresh);
		}
		mListEmptyText.setVisibility(View.GONE);
		mListLoadingSpinner.setVisibility(View.VISIBLE);
		mListView.setEmptyView(mListLoadingSpinner);
	}

	@SuppressLint("NewApi")
	private void stopLoadingSpinners() {
		if (android.os.Build.VERSION.SDK_INT > 8) {
			mRefreshMenuItem.setActionView(null);
		}
		mListEmptyText.setVisibility(View.VISIBLE);
		mListLoadingSpinner.setVisibility(View.GONE);
		mListView.setEmptyView(mListEmptyText);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		new PoEEventsDataSource(this).close();
	}
}
