package com.amdroid.notesapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    static ArrayList<String> notes = new ArrayList<>();
    static ArrayAdapter arrayAdapter;
    static Set<String> set;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addNewNote();


            }
        });

        ListView listView = (ListView)findViewById(R.id.listView);

        SharedPreferences sharedPreferences = this.getSharedPreferences("com.amdroid.notesapp", Context.MODE_PRIVATE);

        set = sharedPreferences.getStringSet("notes", null);

        notes.clear();

        if (set != null){
            Log.i("SET", "Set is NOT null, loading notes");
            Log.i("Size of Loaded Set", Integer.toString(set.size()));

            notes.addAll(set);

            for (int i=0;i<notes.size();i++){
                Log.i("Notes", notes.get(i));
            }

        }else{
            Log.i("SET", "Set is null, creating new set");
            notes.add("Example Note");
            set = new HashSet<>();
            set.addAll(notes);
            sharedPreferences.edit().remove("notes").apply();
            sharedPreferences.edit().putStringSet("notes", set).apply();
        }

        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, notes);
        listView.setAdapter(arrayAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Log.i("nodeId:1", Integer.toString(position));
                Intent i = new Intent(getApplicationContext(), EditNote.class);
                i.putExtra("nodeId", position);

                startActivity(i);
            }
        });


        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int position, long l) {
                new AlertDialog.Builder(MainActivity.this)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Are you sure?")
                        .setMessage("Do you definitely want to delete this note?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                removeNote(position);
                            }
                        })
                        .setNegativeButton("No", null)
                        .show();
                return true;
            }
        });

    }

    private void removeNote(int position) {
        notes.remove(position);

        if (set == null){
            set = new HashSet<>();
        }else{
            set.clear();
        }
        set.addAll(notes);
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("com.amdroid.notesapp", Context.MODE_PRIVATE);

        sharedPreferences.edit().remove("notes").apply();
        sharedPreferences.edit().putStringSet("notes", set).apply();

        MainActivity.arrayAdapter.notifyDataSetChanged();
    }

    private void addNewNote() {
        notes.add("");
        if (set == null){
            set = new HashSet<>();
        }else{
            set.clear();
        }
        set.addAll(notes);
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("com.amdroid.notesapp", Context.MODE_PRIVATE);

        sharedPreferences.edit().remove("notes").apply();
        sharedPreferences.edit().putStringSet("notes", set).apply();

        Intent i = new Intent(getApplicationContext(), EditNote.class);
        i.putExtra("nodeId", notes.size()-1);
        MainActivity.arrayAdapter.notifyDataSetChanged();
        startActivity(i);
    }
    
    

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.add) {

            addNewNote();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}
