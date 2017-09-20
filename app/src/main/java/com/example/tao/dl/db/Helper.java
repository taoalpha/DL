package com.example.tao.dl.db;

/**
 * Created by tao on 9/20/17.
 */

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class Helper extends SQLiteOpenHelper {

    public Helper(Context context) {
        super(context, Schema.DB_NAME, null, Schema.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + Schema.TaskEntry.TABLE + " ( " +
                Schema.TaskEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                Schema.TaskEntry.COL_TASK_TITLE + " TEXT NOT NULL);";

        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + Schema.TaskEntry.TABLE);
        onCreate(db);
    }
}
