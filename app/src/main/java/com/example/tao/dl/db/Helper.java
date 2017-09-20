package com.example.tao.dl.db;

/**
 * Created by tao on 9/20/17.
 */

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class Helper extends SQLiteOpenHelper {

    private static final String TAG = "MainActivity";

    public Helper(Context context) {
        super(context, Schema.DB_NAME, null, Schema.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(TAG, "create db");
        String createTable = "CREATE TABLE " + Schema.ItemEntry.TABLE + " ( " +
                Schema.ItemEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                Schema.ItemEntry.TITLE + " TEXT NOT NULL, " +
                Schema.ItemEntry.DESC + " TEXT, " +
                Schema.ItemEntry.CREATED + " TIMESTAMP DEFAULT (datetime('now','localtime')), " +
                Schema.ItemEntry.UPDATED + " TIMESTAMP DEFAULT (datetime('now','localtime')), " +
                Schema.ItemEntry.STATUS + " TEXT, " +
                Schema.ItemEntry.HISTORY + " TEXT, " +
                Schema.ItemEntry.RESOURCE + " BLOB, " +
                Schema.ItemEntry.TAG + " TEXT, " +
                Schema.ItemEntry.DDL + " TIMESTAMP DEFAULT NULL); ";

        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + Schema.ItemEntry.TABLE);
        onCreate(db);
    }
}
