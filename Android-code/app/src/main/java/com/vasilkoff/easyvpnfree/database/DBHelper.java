package com.vasilkoff.easyvpnfree.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.vasilkoff.easyvpnfree.model.Server;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kusenko on 29.09.2016.
 */

public class DBHelper  extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Records.db";
    public static final String TABLE_SERVERS = "servers";
    private static final String TAG = "DBHelper";

    private static final String KEY_ID = "_id";
    private static final String KEY_HOST_NAME = "hostName";
    private static final String KEY_IP = "ip";
    private static final String KEY_SCORE = "score";
    private static final String KEY_PING = "ping";
    private static final String KEY_SPEED = "speed";
    private static final String KEY_COUNTRY_LONG = "countryLong";
    private static final String KEY_COUNTRY_SHORT = "countryShort";
    private static final String KEY_NUM_VPN_SESSIONS = "numVpnSessions";
    private static final String KEY_UPTIME = "uptime";
    private static final String KEY_TOTAL_USERS = "totalUsers";
    private static final String KEY_TOTAL_TRAFFIC = "totalTraffic";
    private static final String KEY_LOG_TYPE = "logType";
    private static final String KEY_OPERATOR = "operator";
    private static final String KEY_MESSAGE = "message";
    private static final String KEY_CONFIG_DATA = "configData";


    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_SERVERS + "("
                + KEY_ID + " integer primary key,"
                + KEY_HOST_NAME + " text,"
                + KEY_IP + " text,"
                + KEY_SCORE + " text,"
                + KEY_PING + " text,"
                + KEY_SPEED + " text,"
                + KEY_COUNTRY_LONG + " text,"
                + KEY_COUNTRY_SHORT + " text,"
                + KEY_NUM_VPN_SESSIONS + " text,"
                + KEY_UPTIME + " text,"
                + KEY_TOTAL_USERS + " text,"
                + KEY_TOTAL_TRAFFIC + " text,"
                + KEY_LOG_TYPE + " text,"
                + KEY_OPERATOR + " text,"
                + KEY_MESSAGE + " text,"
                + KEY_CONFIG_DATA + " text"
                + ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " + TABLE_SERVERS);
        onCreate(db);
    }

    public void clearTable() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_SERVERS, null, null);
        db.close();
    }

    public void putLine(String line) {
        String[] data = line.split(",");
        if (data.length == 15) {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();

            contentValues.put(KEY_HOST_NAME, data[0]);
            contentValues.put(KEY_IP, data[1]);
            contentValues.put(KEY_SCORE, data[2]);
            contentValues.put(KEY_PING, data[3]);
            contentValues.put(KEY_SPEED, data[4]);
            contentValues.put(KEY_COUNTRY_LONG, data[5]);
            contentValues.put(KEY_COUNTRY_SHORT, data[6]);
            contentValues.put(KEY_NUM_VPN_SESSIONS, data[7]);
            contentValues.put(KEY_UPTIME, data[8]);
            contentValues.put(KEY_TOTAL_USERS, data[9]);
            contentValues.put(KEY_TOTAL_TRAFFIC, data[10]);
            contentValues.put(KEY_LOG_TYPE, data[11]);
            contentValues.put(KEY_OPERATOR, data[12]);
            contentValues.put(KEY_MESSAGE, data[13]);
            contentValues.put(KEY_CONFIG_DATA, data[14]);

            db.insert(TABLE_SERVERS, null, contentValues);
            db.close();
        }
    }

    public List<String> getCountries() {
        List<String> countryList = new ArrayList<String>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT DISTINCT "
                + KEY_COUNTRY_LONG
                + " FROM "
                + TABLE_SERVERS
                + " ORDER BY "
                + KEY_COUNTRY_LONG
                + " ASC", null);

        if (cursor.moveToFirst()) {
            do {
                countryList.add(cursor.getString(0));
            } while (cursor.moveToNext());
        } else {
            Log.d(TAG ,"0 rows");
        }

        cursor.close();
        db.close();

        return countryList;
    }

    public List<Server> getServersByCountry(String country) {
        List<Server> serverList = new ArrayList<Server>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(TABLE_SERVERS, null, KEY_COUNTRY_LONG + "=?", new String[]{country}, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                serverList.add(new Server(
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getString(4),
                        cursor.getString(5),
                        cursor.getString(6),
                        cursor.getString(7),
                        cursor.getString(8),
                        cursor.getString(9),
                        cursor.getString(10),
                        cursor.getString(11),
                        cursor.getString(12),
                        cursor.getString(13),
                        cursor.getString(14),
                        cursor.getString(15)
                ));
            } while (cursor.moveToNext());
        } else {
            Log.d(TAG ,"0 rows");
        }

        cursor.close();
        db.close();

        return serverList;
    }
}
