package com.example.tao.dl.db;

/**
 * Created by tao on 9/20/17.
 */

import android.provider.BaseColumns;

public class Schema {
    public static final String DB_NAME = "com.example.tao.db";
    public static final int DB_VERSION = 1;

    public class TaskEntry implements BaseColumns {
        public static final String TABLE = "tasks";

        public static final String COL_TASK_TITLE = "title";
    }
}