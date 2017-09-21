package com.example.tao.dl;

import android.content.Intent;
import android.content.DialogInterface;
import android.content.ContentValues;

import android.os.Bundle;
import android.support.v4.view.NestedScrollingChildHelper;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ArrayAdapter;
import android.view.Menu;
import android.view.View;
import android.view.MenuItem;

import android.support.design.widget.Snackbar;


import android.util.Log;
import java.util.ArrayList;


import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.tao.dl.db.Schema;
import com.example.tao.dl.db.Helper;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private Helper dHelper;
    private ListView mItemListView;
    private ArrayAdapter<String> mAdapter;
    private EditText itemText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        dHelper = new Helper(this);
        mItemListView = (ListView) findViewById(R.id.item_list);
        itemText = (EditText) findViewById(R.id.editText);

        //click item to edit detail of items
        mItemListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                                 @Override
                                                 public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                                     String clickedItem = (String) mItemListView.getItemAtPosition(i);
                                                     Intent intent = new Intent(MainActivity.this, EditItem.class);
                                                     intent.putExtra("title", clickedItem);
                                                     startActivity(intent);
                                                     }
                                                 }
                                             );
                updateUI();


    }

    // add an item to DB
    public void addItem(View view) {
        String item = itemText.getText().toString().trim();
        if(item.isEmpty() || item.length() == 0 || item.equals("") || item == null) {
            Snackbar mySnackbar = Snackbar.make(findViewById(R.id.addBtn),
                    "Item can not be empty!", Snackbar.LENGTH_SHORT);
            mySnackbar.show();
        } else {
            SQLiteDatabase db = dHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(Schema.ItemEntry.TITLE, item);
            db.insertWithOnConflict(Schema.ItemEntry.TABLE,
                    null,
                    values,
                    SQLiteDatabase.CONFLICT_REPLACE);
            db.close();
            updateUI();
        }
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
            case R.id.action_settings:
                Log.d(TAG, "settings");
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void updateUI() {
        ArrayList<String> itemList = new ArrayList<>();
        SQLiteDatabase db = dHelper.getReadableDatabase();


//        Cursor c = db.query(Schema.ItemEntry.TABLE, null, null, null, null, null, null);
//        Log.d(TAG, c.getColumnNames().length + "");

        Cursor cursor = db.query(Schema.ItemEntry.TABLE,
                new String[]{Schema.ItemEntry._ID, Schema.ItemEntry.TITLE, Schema.ItemEntry.CREATED},
                null, null, null, null, null);
        while (cursor.moveToNext()) {
            int idx = cursor.getColumnIndex(Schema.ItemEntry.TITLE);
//            String t = cursor.getString(cursor.getColumnIndex(Schema.ItemEntry.CREATED));
//            Log.d(TAG, t);
            itemList.add(cursor.getString(idx));
        }

        if (mAdapter == null) {
            mAdapter = new ArrayAdapter<>(this,
                    R.layout.item,
                    R.id.item_title,
                    itemList);
            mItemListView.setAdapter(mAdapter);
        } else {
            mAdapter.clear();
            mAdapter.addAll(itemList);
            mAdapter.notifyDataSetChanged();
        }

        cursor.close();
        db.close();
    }

    public void deleteItem(View view) {
        View parent = (View) view.getParent();
        TextView itemTextView = (TextView) parent.findViewById(R.id.item_title);
        String item = String.valueOf(itemTextView.getText());
        SQLiteDatabase db = dHelper.getWritableDatabase();
        db.delete(Schema.ItemEntry.TABLE,
                Schema.ItemEntry.TITLE + " = ?",
                new String[]{item});
        db.close();
        updateUI();
    }
}
