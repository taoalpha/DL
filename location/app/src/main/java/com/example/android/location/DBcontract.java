package com.example.android.location;

import android.provider.BaseColumns;

/**
 * Created by mengyingfan on 8/4/17.
 */

public final class DBcontract {
        private DBcontract() {};

        // contract class used to define some constants of database including table_name, column names.
        public final class locationTable implements BaseColumns {
            public static final String DB_NAME = "week6_db";
            public static final String TABLE_NAME = "location_table";
            public static final String COLUMN_NAME_NAME = "name";
            public static final String COLUMN_NAME_latitude = "latitude";
            public static final String COLUMN_NAME_longitude = "longitude";
            public static final int DB_VERSION = 4;

            // string used to create a table
            public static final String SQL_CREATE_LOCATION_TABLE = "CREATE TABLE " + locationTable.TABLE_NAME
                    + " (" + locationTable._ID + " INTEGER PRIMARY KEY NOT NULL, " +
                    locationTable.COLUMN_NAME_NAME + " VARCHAR(255)," +
                    locationTable.COLUMN_NAME_latitude + " VARCHAR(255)," +
                    locationTable.COLUMN_NAME_longitude + " VARCHAR(255));";
            // string used to drop a table
            public static final String SQL_DROP_LOCATION_TABLE = "DROP TABLE IF EXISTS" + locationTable.TABLE_NAME;

        }
}

