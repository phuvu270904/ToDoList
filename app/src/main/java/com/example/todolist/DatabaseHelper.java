package com.example.todolist;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "todo_database";
    private static final String TABLE_NAME = "tasks";
    private static final String ID_COLUMN_NAME = "task_id";
    private static final String NAME_COLUMN_NAME = "name";
    private static final String DEADLINE_COLUMN_NAME = "deadline";
    private static final String DURATION_COLUMN_NAME = "duration";
    private static final String DESCRIPTION_COLUMN_NAME = "description";
    private SQLiteDatabase database;
    private static final String DATABASE_CREATE_QUERY = String.format(
            "CREATE TABLE %s (" +
            "%s INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "%s TEXT, " +
            "%s TEXT, " +
            "%s INTEGER, " +
            "%s TEXT)",
            TABLE_NAME, ID_COLUMN_NAME, NAME_COLUMN_NAME, DEADLINE_COLUMN_NAME, DURATION_COLUMN_NAME, DESCRIPTION_COLUMN_NAME);

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
        database = getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DATABASE_CREATE_QUERY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        Log.w(this.getClass().getName(), TABLE_NAME + " database upgrade to version "
                + newVersion + " - old data lost");
        onCreate(db);
    }

    public long insertTask(String name, Date deadline, int duration, String description) {
        ContentValues rowValues = new ContentValues();
        rowValues.put(NAME_COLUMN_NAME, name);
        rowValues.put(DEADLINE_COLUMN_NAME, dateFormat.format(deadline));
        rowValues.put(DURATION_COLUMN_NAME, duration);
        rowValues.put(DESCRIPTION_COLUMN_NAME, description);
        return database.insertOrThrow(TABLE_NAME, null, rowValues);
    }

    public long insertTask(Task task) {
        return insertTask(task.title, task.dueDate, task.estimatedHours, task.details);
    }

    public ArrayList<Task> getAllTasks() {
        ArrayList<Task> taskList = new ArrayList<>();
        Cursor results = database.query(TABLE_NAME,
                new String[] {ID_COLUMN_NAME, NAME_COLUMN_NAME, DEADLINE_COLUMN_NAME, DURATION_COLUMN_NAME, DESCRIPTION_COLUMN_NAME},
                null, null, null, null, NAME_COLUMN_NAME);

        if (results.moveToFirst()) {
            do {
                int id = results.getInt(0);
                String name = results.getString(1);
                String deadlineStr = results.getString(2);
                int duration = results.getInt(3);
                String description = results.getString(4);

                try {
                    Date deadline = dateFormat.parse(deadlineStr);
                    Task task = new Task(name, deadline, duration, description);
                    taskList.add(task);
                } catch (ParseException e) {
                    Log.e("DatabaseHelper", "Error parsing date: " + deadlineStr, e);
                }
            } while (results.moveToNext());
        }
        results.close();
        return taskList;
    }

    @Override
    public void close() {
        if (database != null) {
            database.close();
        }
        super.close();
    }
}
