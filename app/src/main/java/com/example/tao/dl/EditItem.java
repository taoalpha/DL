package com.example.tao.dl;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.ContextMenu;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.support.v4.app.Fragment;
import android.widget.TextView;

import com.example.tao.dl.db.Helper;
import com.example.tao.dl.db.Schema;


import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.sql.Timestamp;

import static android.R.attr.description;
import static android.R.attr.onClick;
import static com.example.tao.dl.R.id.list_item;

/**
 * Created by mengyingfan on 9/20/17.
 */

public class EditItem extends AppCompatActivity{
    private Helper dHelper;
    private String item_title;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        dHelper = new Helper(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_page);
        //get item title to be edited
        Intent intent = getIntent();
        item_title = intent.getStringExtra("title");
        //display item data in database
        EditText title = (EditText) findViewById(R.id.edit_title);
        title.setText(item_title);
        SQLiteDatabase db = dHelper.getReadableDatabase();
        String[] item = {Schema.ItemEntry.TITLE, Schema.ItemEntry.DESC, Schema.ItemEntry.TAG, Schema.ItemEntry.DDL};
        String selection = Schema.ItemEntry.TITLE + "=?";
        String[] args = {item_title};
        Cursor cursor = db.query(Schema.ItemEntry.TABLE, item, selection, args, null, null, null);
        while(cursor.moveToNext()) {
            String description = cursor.getString(cursor.getColumnIndex(Schema.ItemEntry.DESC));
            EditText edit_description = (EditText) findViewById(R.id.edit_description);
            edit_description.setText(description);
            String tag = cursor.getString(cursor.getColumnIndex(Schema.ItemEntry.TAG));
            EditText edit_tag = (EditText) findViewById(R.id.edit_tag);
            edit_tag.setText(tag);
            Long duetimelong = cursor.getLong(cursor.getColumnIndex(Schema.ItemEntry.DDL));
            if (duetimelong != 0) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(duetimelong);
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH) + 1;
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                String duedatestring = year + "-" + month + "-" + day;
                EditText edit_ddl = (EditText) findViewById(R.id.due_date);
                edit_ddl.setText(duedatestring);

            }
        }

        //set listener on EditText of ddl
        EditText addDueDate = (EditText) findViewById(R.id.due_date);
        addDueDate.setInputType(InputType.TYPE_NULL);
        addDueDate.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                showDatePickerDialog(v);
            }
        });
        addDueDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus) {
                    showDatePickerDialog(v);
                }
            }
        });

        // set listener on save and cancel buttons
        Button saveEdit = (Button) findViewById(R.id.saveEdit);
        saveEdit.setOnClickListener(new View.OnClickListener() {
            Date  editDueDate;
            public void onClick(View v) {
                //get input data
                EditText title = (EditText) findViewById(R.id.edit_title);
                String edit_title = title.getText().toString();
                EditText desc = (EditText) findViewById(R.id.edit_description);
                String edit_desc = desc.getText().toString();
                EditText tag = (EditText) findViewById(R.id.edit_tag);
                String edit_tag = tag.getText().toString();
                EditText due_date = (EditText) findViewById(R.id.due_date);
                String date = due_date.getText().toString();
                SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd");
                editDueDate = new Date();
                try {
                    editDueDate = (Date) dateformat.parse(date);

                } catch (ParseException except) {
                    Log.d("due date parse error", "date parse");
                }
                //System.out.println(editDueDate);
                Timestamp datetimestamp = new Timestamp(editDueDate.getTime());
                System.out.println(datetimestamp);
                //update data in database
                SQLiteDatabase db = dHelper.getWritableDatabase();
                ContentValues values = new ContentValues();
                values.put(Schema.ItemEntry.TITLE, edit_title);
                values.put(Schema.ItemEntry.DESC, edit_desc);
                values.put(Schema.ItemEntry.TAG, edit_tag);
                values.put(Schema.ItemEntry.DDL, datetimestamp.getTime());
                String selection = Schema.ItemEntry.TITLE + "=?";
                String[] args = {item_title};
                db.update(Schema.ItemEntry.TABLE, values, selection, args);
                db.close();
                finish();
                Intent intent = new Intent(EditItem.this, MainActivity.class);
                startActivity(intent);
            }
        });

        Button cancelEdit = (Button) findViewById(R.id.cancelEdit);
        cancelEdit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });
    }

    //date picker
    public void showDatePickerDialog(View v) {
        DateDialog newFragment = new DateDialog();
        newFragment.show(getFragmentManager(), "datePicker");
    }





}


