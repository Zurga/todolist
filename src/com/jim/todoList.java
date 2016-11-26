package com.jim;

import android.app.Activity;
import android.os.Bundle;
import android.content.Context;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.CursorAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.view.View;
import android.widget.Toast;
import android.util.Log;
import android.view.ViewGroup.LayoutParams;

public class todoList extends Activity
{
    public SQLiteDatabase db;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        //Set up the database.
        db = openOrCreateDatabase("todo.db", SQLiteDatabase.CREATE_IF_NECESSARY, null);
        db.execSQL("CREATE TABLE IF NOT EXISTS todo (_id INTEGER PRIMARY KEY AUTOINCREMENT, text TEXT NOT NULL, done INTEGER DEFAULT 0);");
        db.setVersion(1);
        db.setLockingEnabled(true);
        getTasks();
    }

        //Get the current tasks.
    public void getTasks(){
        Cursor current_tasks = db.query("todo", null, null, null, null, null, null);
        current_tasks.moveToFirst();

        // Do all the listview stuff.
        ListView taskLV = (ListView) findViewById(R.id.tasks);
        // Create a LongClickListener for deleting tasks.
        taskLV.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view,
                int position, long id) {
            TextView _id = (TextView) view.findViewById(R.id._id);
            String ids = _id.getText().toString();
            db.delete("todo", "_id="+ ids, null);
            getTasks();
            return false;
            }
        });

        String[] from = {"_id", "text"};
        int[] to = {R.id._id, R.id.text};
        CursorAdapter todoAdapter = new SimpleCursorAdapter(this, R.layout.todo, current_tasks,
                from, to);
        taskLV.setAdapter(todoAdapter);
    }

    public void addTodo(View view) {
        ContentValues contentValues = new ContentValues();
        EditText input = (EditText) findViewById(R.id.todoText);
        String task = input.getText().toString();
        if(! task.equals("")){
            contentValues.put("text", task);

            db.insert("todo", null, contentValues);
            getTasks();
        }
    }

    public void taskDone(String id) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("done", 1);
        db.update("todos", contentValues, "_id =" + id, null);
        getTasks();
    }
}
