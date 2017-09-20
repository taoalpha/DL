package com.example.tao.dl.db;

/**
 * Created by tao on 9/20/17.
 */

import android.provider.BaseColumns;

public class Schema {
    public static final String DB_NAME = "com.example.tao.db";
    public static final int DB_VERSION = 1;

    public class ItemEntry implements BaseColumns {
        public static final String TABLE = "items";

        public static final String TITLE = "title";
        public static final String DESC = "description";
        public static final String CREATED = "created";
        public static final String UPDATED = "updated";
        public static final String STATUS = "status";
        public static final String HISTORY = "history";
        public static final String TAG = "tags";
        public static final String DDL = "ddl";
        public static final String RESOURCE = "resources";
    }
}