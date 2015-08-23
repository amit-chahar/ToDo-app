package com.example.amit.todo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;


public class MainActivity extends ActionBarActivity {
    ListView list;
    DbHelper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        list = (ListView) findViewById(R.id.listView);
        updateUI();
    }

    private void updateUI() {
        helper = new DbHelper(this);
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.query("todotable", new String[] {"_id", "task"},null, null, null, null, null);
        cursor.moveToFirst();

        SimpleCursorAdapter listAdapter = new SimpleCursorAdapter(this, R.layout.single_row, cursor, new String[] {"task"}, new int[] {R.id.textView}, 0);
        list.setAdapter(listAdapter);
    }

    public void additem(View v) {
        EditText editText = (EditText) findViewById(R.id.editText);
        String userInput = editText.getText().toString();
        if(userInput.equals("")) return;

        helper = new DbHelper(this);
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.clear();
        cv.put("task", userInput);
        db.insertWithOnConflict("todotable",null,cv, SQLiteDatabase.CONFLICT_IGNORE);
        updateUI();
        editText.setText("");
    }

    public void doneClicked(View view)
    {
        View v = (View) view.getParent();
        TextView tv = (TextView) v.findViewById(R.id.textView);
        String task = tv.getText().toString();

        String sql = String.format("delete from todotable where task = '%s'", task);
        helper = new DbHelper(this);
        SQLiteDatabase db = helper.getWritableDatabase();
        db.execSQL(sql);
        updateUI();
    }
}

