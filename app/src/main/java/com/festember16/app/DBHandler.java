package com.festember16.app;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class DBHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_NAME = "EventsDB";

    private static final String TABLE_EVENTS = "Events";

    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_START_TIME = "startTime";
    private static final String KEY_END_TIME = "endTime";
    private static final String KEY_VENUE = "venue";
    private static final String KEY_DESCRIPTION = "description";
    private static final String KEY_LAST_UPDATE_TIME = "lastUpdateTime";
    private static final String KEY_LOCATION_X = "locationX";
    private static final String KEY_LOCATION_Y = "locationY";
    private static final String KEY_MAX_LIMIT = "maxLimit";
    private static final String KEY_CLUSTER = "cluster";
    private static final String KEY_DATE = "date";

    public DBHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_EVENTS_TABLE = "CREATE TABLE " + TABLE_EVENTS + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT,"
                + KEY_START_TIME + " TEXT," + KEY_END_TIME + " TEXT,"
                + KEY_VENUE + " TEXT," + KEY_DESCRIPTION + " TEXT,"
                + KEY_LAST_UPDATE_TIME + " TEXT," + KEY_LOCATION_X + " TEXT,"
                + KEY_LOCATION_Y + " TEXT," + KEY_MAX_LIMIT + " TEXT,"
                + KEY_CLUSTER + " TEXT," + KEY_DATE + " TEXT" + ")";
        db.execSQL(CREATE_EVENTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EVENTS);
        onCreate(db);
    }

    public void addEvent(Events events) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ID, events.getId());
        values.put(KEY_NAME, events.getName());
        values.put(KEY_START_TIME, events.getStartTime());
        values.put(KEY_END_TIME, events.getEndTime());
        values.put(KEY_VENUE, events.getVenue());
        values.put(KEY_DESCRIPTION, events.getDescription());
        values.put(KEY_LAST_UPDATE_TIME, events.getLastUpdateTime());
        values.put(KEY_LOCATION_X, events.getLocationX());
        values.put(KEY_LOCATION_Y, events.getLocationY());
        values.put(KEY_MAX_LIMIT, events.getMaxLimit());
        values.put(KEY_CLUSTER, events.getCluster() );
        values.put(KEY_DATE, events.getDate());

        db.insert(TABLE_EVENTS, null, values);
        db.close();
    }

    public List<Events> getAllEvents() {
        List<Events> eventsList = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_EVENTS, null);
        if (cursor.moveToFirst()) {
            do {
                Events events = new Events();

                events.setId(cursor.getInt(0));
                events.setName(cursor.getString(1));
                events.setStartTime(cursor.getString(2));
                events.setEndTime(cursor.getString(3));
                events.setVenue(cursor.getString(4));
                events.setDescription(cursor.getString(5));
                events.setLastUpdateTime(cursor.getString(6));
                events.setLocationX(cursor.getString(7));
                events.setLocationY(cursor.getString(8));
                events.setMaxLimit(cursor.getString(9));
                events.setCluster(cursor.getString(10));
                events.setDate(cursor.getString(11));

                eventsList.add(events);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return eventsList;
    }

    public Events getEvent(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM Events WHERE id is " + id, null);

        Events event = new Events();
        cursor.moveToFirst();
        if(cursor != null) {
            event.setId(cursor.getInt(0));
            event.setName(cursor.getString(1));
            event.setStartTime(cursor.getString(2));
            event.setEndTime(cursor.getString(3));
            event.setVenue(cursor.getString(4));
            event.setDescription(cursor.getString(5));
            event.setLastUpdateTime(cursor.getString(6));
            event.setLocationX(cursor.getString(7));
            event.setLocationY(cursor.getString(8));
            event.setMaxLimit(cursor.getString(9));
            event.setCluster(cursor.getString(10));
            event.setDate(cursor.getString(11));
        }

        db.close();
        if (cursor != null) {
            cursor.close();
        }
        return event;
    }

<<<<<<< HEAD
    public List<Events> getEventsByCluster(String cluster) {
        List<Events> eventsList = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();

        String selectQuery = "SELECT * FROM Events WHERE cluster is " + "\""+ cluster + "\"";
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Events events = new Events();

                events.setId(cursor.getInt(0));
                events.setName(cursor.getString(1));
                events.setStartTime(cursor.getString(2));
                events.setEndTime(cursor.getString(3));
                events.setVenue(cursor.getString(4));
                events.setDescription(cursor.getString(5));
                events.setLastUpdateTime(cursor.getString(6));
                events.setLocationX(cursor.getString(7));
                events.setLocationY(cursor.getString(8));
                events.setMaxLimit(cursor.getString(9));
                events.setCluster(cursor.getString(10));
                events.setDate(cursor.getString(11));

                eventsList.add(events);
            } while (cursor.moveToNext());
        }

        db.close();
        cursor.close();

        return eventsList;
    }

    public String[] getClusters() {
        String[] clusters = new String[100];
        int i = 0;
=======
    public void getCluster() {
>>>>>>> 2cc3b6cf5cc9a1fccef0994ea8354e12f6e899b9
        SQLiteDatabase db = this.getReadableDatabase();

        String select = "SELECT DISTINCT " + KEY_CLUSTER + " FROM " + TABLE_EVENTS;
        Cursor cursor = db.rawQuery(select, null);
        do {
            clusters[i] = cursor.getString(i);
            i++;
        } while (cursor.getString(i)!=null);

<<<<<<< HEAD
        db.close();
        cursor.close();
        return clusters;
=======

        db.close();
        c.close();

>>>>>>> 2cc3b6cf5cc9a1fccef0994ea8354e12f6e899b9
    }
}
