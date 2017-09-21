package com.example.tao.dl;

import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.support.v4.app.Fragment;


/**
 * Created by mengyingfan on 9/20/17.
 */

public class EditItem extends AppCompatActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_page);
        EditText addDueDate = (EditText) findViewById(R.id.due_date);
        addDueDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    showDatePickerDialog(v);
                }
            }
        });

        Button saveEdit = (Button) findViewById(R.id.saveEdit);
        saveEdit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

            }
        });

        Button cancelEdit = (Button) findViewById(R.id.cancelEdit);
        cancelEdit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {


            }
        });
    }

    public void showDatePickerDialog(View v) {
        DateDialog newFragment = new DateDialog();
        newFragment.show(getFragmentManager(), "datePicker");
    }



}
