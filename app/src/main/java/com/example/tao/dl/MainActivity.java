package com.example.tao.dl;

import android.content.Intent;
import android.content.ContentValues;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.widget.EditText;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.view.Menu;
import android.view.View;
import android.view.MenuItem;

import android.support.design.widget.Snackbar;
import android.graphics.Paint;


import android.util.Log;
import java.util.ArrayList;
import java.util.List;


import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.tao.dl.db.Schema;
import com.example.tao.dl.db.Helper;

import com.mcxtzhang.swipemenulib.SwipeMenuLayout;

import com.mcxtzhang.commonadapter.lvgv.CommonAdapter;
import com.mcxtzhang.commonadapter.lvgv.ViewHolder;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private Helper dHelper;
    private ListView mItemListView;
    private List<ListItem> itemList;
    private CommonAdapter mAdapter;
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

        // enter to save
        itemText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
                    switch (i) {
                        case KeyEvent.KEYCODE_DPAD_CENTER:
                        case KeyEvent.KEYCODE_ENTER:
                            addItem(view);
                            return true;
                        default:
                            break;
                    }
                }
                return false;
            }
        });

        // initialize list data
        initData();

        mItemListView.setAdapter(new CommonAdapter<ListItem>(this, itemList, R.layout.item) {
            @Override
            public void convert(final ViewHolder holder, ListItem item, final int position) {
                //((SwipeMenuLayout)holder.getConvertView()).setIos(false);
                holder.setText(R.id.list_item, item.title);
                holder.setOnClickListener(R.id.list_item, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        editItem(v);
                    }
                });

                holder.setOnClickListener(R.id.btnDelete, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ((SwipeMenuLayout) holder.getConvertView()).quickClose();
                        ListItem item = itemList.remove(position);
                        deleteItem(item);
                        notifyDataSetChanged();
                    }
                });
            }
        });
    }

    // add an item to DB
    public void addItem(View view) {
        String item = itemText.getText().toString().trim();
        if(item.isEmpty() || item.length() == 0 || item.equals("") || item == null) {
            Snackbar mySnackbar = Snackbar.make(findViewById(R.id.editText),
                    "Item can not be empty!", Snackbar.LENGTH_SHORT);
            mySnackbar.show();
        } else {
            SQLiteDatabase db = dHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(Schema.ItemEntry.TITLE, item);

            // all initial status default to ongoing
            values.put(Schema.ItemEntry.STATUS, "ongoing");
            db.insertWithOnConflict(Schema.ItemEntry.TABLE,
                    null,
                    values,
                    SQLiteDatabase.CONFLICT_REPLACE);
            db.close();
            itemText.setText("");
            itemList.add(new ListItem(item, "", "ongoing"));
            if (mAdapter == null) {
                mAdapter = (CommonAdapter)mItemListView.getAdapter();
            }
            mAdapter.notifyDataSetChanged();
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

    private void initData() {
        itemList = new ArrayList<>();
        SQLiteDatabase db = dHelper.getReadableDatabase();
        Cursor cursor = db.query(Schema.ItemEntry.TABLE,
                new String[]{Schema.ItemEntry._ID, Schema.ItemEntry.TITLE, Schema.ItemEntry.DESC, Schema.ItemEntry.STATUS},
                null, null, null, null, null);
        while (cursor.moveToNext()) {
            int t_id = cursor.getColumnIndex(Schema.ItemEntry.TITLE);
            int d_id = cursor.getColumnIndex(Schema.ItemEntry.DESC);
            int s_id = cursor.getColumnIndex(Schema.ItemEntry.STATUS);
            itemList.add(new ListItem(cursor.getString(t_id), cursor.getString(t_id), cursor.getString(t_id)));
        }

        cursor.close();
        db.close();
    }

    public void editItem(View view) {
        View parent = (View) view.getParent();
        TextView itemTextView = (TextView) parent.findViewById(R.id.list_item);
        String item = String.valueOf(itemTextView.getText());
        Intent intent = new Intent(MainActivity.this, EditItem.class);
        intent.putExtra("title", item);
        startActivity(intent);
    }

    public void markDone(View view) {

        boolean checked = ((CheckBox) view).isChecked();
        View parent = (View) view.getParent();
        TextView itemTextView = (TextView) parent.findViewById(R.id.list_item);

        // strike through
        if (checked) {
            itemTextView.setPaintFlags(itemTextView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        } else {
            itemTextView.setPaintFlags(itemTextView.getPaintFlags() & (~ Paint.STRIKE_THRU_TEXT_FLAG));
        }

        String item = String.valueOf(itemTextView.getText());
        SQLiteDatabase db = dHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Schema.ItemEntry.STATUS, checked ? "completed" : "ongoing");
        db.update(Schema.ItemEntry.TABLE, values, Schema.ItemEntry.TITLE + " = ?", new String[]{item});
        db.close();
    }

    public void deleteItem(ListItem item) {
        String title = String.valueOf(item.title);
        SQLiteDatabase db = dHelper.getWritableDatabase();
        db.delete(Schema.ItemEntry.TABLE,
                Schema.ItemEntry.TITLE + " = ?",
                new String[]{title});
        db.close();
    }
}
