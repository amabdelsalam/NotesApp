package com.amdroid.notesapp;

import android.content.Context;
import android.content.Intent;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import java.util.HashSet;


import static com.amdroid.notesapp.MainActivity.notes;
import static com.amdroid.notesapp.MainActivity.set;
import android.content.SharedPreferences;


public class EditNote extends AppCompatActivity {

    int noteId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_note);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("com.amdroid.notesapp", Context.MODE_PRIVATE);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        EditText editText = (EditText)findViewById(R.id.editText);

        Intent intent = getIntent();
        noteId = intent.getIntExtra("nodeId", -1);

        Log.i("noteId:2", Integer.toString(noteId));

        if (noteId != -1){
            String note = notes.get(noteId);

            Log.i("note", note);

            editText.setText(note);
        }

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Log.i("noteId:3", Integer.toString(noteId));

                notes.set(noteId, String.valueOf(charSequence));

                MainActivity.arrayAdapter.notifyDataSetChanged();

                if (set == null){
                    set = new HashSet<>();
                }
                set.clear();
                //Log.i("onTextChanged:Notes", Integer.toString(notes.size()));


                set.addAll(notes);

                SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("com.amdroid.notesapp", Context.MODE_PRIVATE);
                //Log.i("onTextChanged:Set", Integer.toString(set.size()));

                sharedPreferences.edit().remove("notes").apply();
                sharedPreferences.edit().putStringSet("notes", set).apply();


            }

            @Override
            public void afterTextChanged(Editable editable) {


            }
        });

    }

}
