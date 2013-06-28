package com.tywholland.poeevents;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.Menu;
import android.widget.ProgressBar;

public class MainActivity extends ListActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ProgressBar progressBar = new ProgressBar(this);
		progressBar.setIndeterminate(true);
		getListView().setEmptyView(progressBar);
		PoEEventsDataSource db = new PoEEventsDataSource(this);
		getListView().setAdapter(PoEUtil.getCursorAdapter(this, db.getAllEvents()));
		new EventRequestTask().execute(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		new PoEEventsDataSource(this).close();
	}
}
