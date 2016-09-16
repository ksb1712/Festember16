package com.festember16.app;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class NotificationDBHandler extends SQLiteOpenHelper{

    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_NAME = "NotificationsDB";

    private static final String TABLE_NOTIFICATIONS = "Notifications";

    private static final String KEY_TEXT = "text";
    private static final String KEY_TITLE = "title";
    private static final String KEY_CLUSTER = "cluster";
    private static final String KEY_TYPE = "type";
    private static final String KEY_EVENT = "event";
    private static final String KEY_TIME = "time";

    public NotificationDBHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_NOTIFICATIONS_TABLE = "CREATE TABLE " + TABLE_NOTIFICATIONS + "("
                + KEY_TEXT + " TEXT PRIMARY KEY," + KEY_TITLE + " TEXT,"
                + KEY_CLUSTER + " TEXT," + KEY_TYPE + " TEXT,"
                + KEY_EVENT + " TEXT," + KEY_TIME + " TEXT" + ")";
        db.execSQL(CREATE_NOTIFICATIONS_TABLE);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NOTIFICATIONS);
        onCreate(db);
    }

    /*
    public void addNotification(NotificationDetails notifs) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_TEXT, notifs.text);
        values.put(KEY_TITLE, notifs.title);
        values.put(KEY_CLUSTER, notifs.cluster);
        values.put(KEY_TYPE, notifs.type);
        values.put(KEY_EVENT, notifs.event);
        values.put(KEY_TIME, notifs.time);

        db.insert(TABLE_NOTIFICATIONS, null, values);
        db.close();
    }

    public List<NotificationDetails> getAllNotifications() {
        List<NotificationDetails> notificationsList = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NOTIFICATIONS, null);
        if (cursor.moveToFirst()) {
            do {
                NotificationDetails notifs = new NotificationDetails();

                notifs.text(cursor.getInt(0));
                notifs.title(cursor.getString(1));
                notifs.cluster(cursor.getString(2));
                notifs.type(cursor.getString(3));
                notifs.event(cursor.getString(4));
                notifs.time(cursor.getString(5));

                notificationsList.add(notifs);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return notificationsList;
    }

    public List<NotificationDetails> getByCluster(String cluster) {
        List<NotificationDetails> notificationsList = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();

        String selectQuery = "SELECT * FROM Notifications WHERE cluster is " + "\""+ cluster + "\"";
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                NotificationDetails notifs = new NotificationDetails();

                notifs.text(cursor.getInt(0));
                notifs.title(cursor.getString(1));
                notifs.cluster(cursor.getString(2));
                notifs.type(cursor.getString(3));
                notifs.event(cursor.getString(4));
                notifs.time(cursor.getString(5));

                notificationsList.add(notifs);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return notificationsList;
    }
    */
}
