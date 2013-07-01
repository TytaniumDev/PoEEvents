package com.tywholland.poeevents;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import android.widget.ListView;

public class MainActivity extends Activity {
	private ListView mListView;
	private MenuItem mRefreshMenuItem;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.poeevents_listview);
		mListView = (ListView) findViewById(android.R.id.list);
		mListView.setEmptyView(findViewById(android.R.id.empty));
		PoEEventsDataSource db = new PoEEventsDataSource(this);
		mListView.setAdapter(PoEUtil.getCursorAdapter(this, db.getAllEvents()));
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
			mRefreshMenuItem.setActionView(R.layout.menu_refresh);
			EventRequestTask task = new EventRequestTask(this, mListView,
					mRefreshMenuItem);
			task.execute();
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		new PoEEventsDataSource(this).close();
	}
}
