package com.example.android.location;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by mengyingfan on 8/4/17.
 */

public class SQLiteLocation extends SQLiteOpenHelper {
    public SQLiteLocation(Context context) {
            super(context, DBcontract.locationTable.DB_NAME, null, DBcontract.locationTable.DB_VERSION);
        }

        // create a table
        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(DBcontract.locationTable.SQL_CREATE_LOCATION_TABLE);
        }

        // upgrade table if change db version, drop old table and create a new one
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL(DBcontract.locationTable.SQL_DROP_LOCATION_TABLE);
            onCreate(db);
        }

    }

