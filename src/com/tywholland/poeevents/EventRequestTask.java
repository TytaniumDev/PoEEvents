package com.tywholland.poeevents;

import java.lang.reflect.Type;
import java.util.Collection;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.view.MenuItem;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class EventRequestTask extends AsyncTask<Object, String, List<PoEEvent>> {
    private Context context;
    private ListView listView;
    private MenuItem refreshMenuItem;

    public EventRequestTask(Context context, ListView listView,
            MenuItem refreshMenuItem) {
        this.context = context;
        this.listView = listView;
        this.refreshMenuItem = refreshMenuItem;
    }

    public EventRequestTask(Context context) {
        this.context = context;
        this.listView = null;
        this.refreshMenuItem = null;
    }

    @Override
    protected List<PoEEvent> doInBackground(Object... params) {
        JSONParser parser = new JSONParser();
        String json = parser.getEventsJSON();
        Gson gson = new Gson();
        Type collectionType = new TypeToken<Collection<PoEEvent>>() {
        }.getType();
        List<PoEEvent> events = gson.fromJson(json, collectionType);
        if (events != null && events.size() > 0) {
            // Insert into database
            PoEEventsDataSource db = new PoEEventsDataSource(context);
            db.insertEventsAndClean(events);
            db.close();
        }
        return events;
    }

    @SuppressLint("NewApi")
    @Override
    protected void onPostExecute(List<PoEEvent> result) {
        PoEEventsDataSource db = new PoEEventsDataSource(context);
        if (listView != null) {

            // Make cursor adapter
            Cursor cursor = db.getAllEvents();
            listView.setAdapter(PoEUtil.getCursorAdapter(context, cursor));
        }
        if (refreshMenuItem != null && android.os.Build.VERSION.SDK_INT > 8) {
            // Stop spinning refresh icon
            refreshMenuItem.setActionView(null);
        }
        PoEUtil.updateWidget(context);
        super.onPostExecute(result);
    }
}
