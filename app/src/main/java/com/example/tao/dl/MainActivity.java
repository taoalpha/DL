package com.example.tao.dl;

import android.content.Intent;
import android.content.DialogInterface;
import android.content.ContentValues;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ArrayAdapter;
import android.view.Menu;
import android.view.View;
import android.view.MenuItem;


import android.util.Log;
import java.util.ArrayList;


import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.tao.dl.db.Schema;
import com.example.tao.dl.db.Helper;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private Helper dHelper;
    private ListView mTaskListView;
    private ArrayAdapter<String> mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        dHelper = new Helper(this);
        mTaskListView = (ListView) findViewById(R.id.list_todo);

        updateUI();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case R.id.action_add_task:
                showAlert();
                return true;

            case R.id.action_settings:
                Log.d(TAG, "settings");
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void showAlert() {
        final EditText taskEditText = new EditText(this);
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Add a new task")
                .setMessage("What do you want to do next?")
                .setView(taskEditText)
                .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String task = String.valueOf(taskEditText.getText());
                        SQLiteDatabase db = dHelper.getWritableDatabase();
                        ContentValues values = new ContentValues();
                        values.put(Schema.TaskEntry.COL_TASK_TITLE, task);
                        db.insertWithOnConflict(Schema.TaskEntry.TABLE,
                                null,
                                values,
                                SQLiteDatabase.CONFLICT_REPLACE);
                        db.close();
                        updateUI();
                    }
                })
                .setNegativeButton("Cancel", null)
                .create();
        dialog.show();
    }

    private void updateUI() {
        ArrayList<String> taskList = new ArrayList<>();
        SQLiteDatabase db = dHelper.getReadableDatabase();
        Cursor cursor = db.query(Schema.TaskEntry.TABLE,
                new String[]{Schema.TaskEntry._ID, Schema.TaskEntry.COL_TASK_TITLE},
                null, null, null, null, null);
        while (cursor.moveToNext()) {
            int idx = cursor.getColumnIndex(Schema.TaskEntry.COL_TASK_TITLE);
            taskList.add(cursor.getString(idx));
        }

        if (mAdapter == null) {
            mAdapter = new ArrayAdapter<>(this,
                    R.layout.item,
                    R.id.task_title,
                    taskList);
            mTaskListView.setAdapter(mAdapter);
        } else {
            mAdapter.clear();
            mAdapter.addAll(taskList);
            mAdapter.notifyDataSetChanged();
        }

        cursor.close();
        db.close();
    }

    public void deleteTask(View view) {
        View parent = (View) view.getParent();
        TextView taskTextView = (TextView) parent.findViewById(R.id.task_title);
        String task = String.valueOf(taskTextView.getText());
        SQLiteDatabase db = dHelper.getWritableDatabase();
        db.delete(Schema.TaskEntry.TABLE,
                Schema.TaskEntry.COL_TASK_TITLE + " = ?",
                new String[]{task});
        db.close();
        updateUI();
    }
}
