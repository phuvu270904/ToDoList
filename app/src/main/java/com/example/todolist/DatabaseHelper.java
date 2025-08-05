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
    private static final String DB_NAME = "task_management_db";
    private static final String TASK_TABLE = "todo_items";
    private static final String COLUMN_ID = "item_id";
    private static final String COLUMN_TITLE = "task_title";
    private static final String COLUMN_DUE_DATE = "due_date";
    private static final String COLUMN_HOURS = "estimated_hours";
    private static final String COLUMN_DETAILS = "task_details";
    private SQLiteDatabase dbInstance;
    private static final String CREATE_TABLE_QUERY = String.format(
            "CREATE TABLE %s (" +
            "%s INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "%s TEXT, " +
            "%s TEXT, " +
            "%s INTEGER, " +
            "%s TEXT)",
            TASK_TABLE, COLUMN_ID, COLUMN_TITLE, COLUMN_DUE_DATE, COLUMN_HOURS, COLUMN_DETAILS);

    private static final SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy");

    public DatabaseHelper(Context appContext) {
        super(appContext, DB_NAME, null, 1);
        dbInstance = getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase dbConnection) {
        dbConnection.execSQL(CREATE_TABLE_QUERY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase dbConnection, int previousVersion, int currentVersion) {
        dbConnection.execSQL("DROP TABLE IF EXISTS " + TASK_TABLE);
        Log.w(this.getClass().getName(), TASK_TABLE + " database upgrade to version "
                + currentVersion + " - old data lost");
        onCreate(dbConnection);
    }

    public long addTaskToDatabase(String taskTitle, Date dueDate, int hours, String taskDetails) {
        ContentValues dataValues = new ContentValues();
        dataValues.put(COLUMN_TITLE, taskTitle);
        dataValues.put(COLUMN_DUE_DATE, dateFormatter.format(dueDate));
        dataValues.put(COLUMN_HOURS, hours);
        dataValues.put(COLUMN_DETAILS, taskDetails);
        return dbInstance.insertOrThrow(TASK_TABLE, null, dataValues);
    }

    public long addTaskToDatabase(Task taskItem) {
        return addTaskToDatabase(taskItem.title, taskItem.dueDate, taskItem.estimatedHours, taskItem.details);
    }

    public ArrayList<Task> retrieveAllTasks() {
        ArrayList<Task> todoCollection = new ArrayList<>();
        Cursor queryResults = dbInstance.query(TASK_TABLE,
                new String[] {COLUMN_ID, COLUMN_TITLE, COLUMN_DUE_DATE, COLUMN_HOURS, COLUMN_DETAILS},
                null, null, null, null, COLUMN_TITLE);

        if (queryResults.moveToFirst()) {
            do {
                int taskId = queryResults.getInt(0);
                String taskTitle = queryResults.getString(1);
                String dueDateString = queryResults.getString(2);
                int estimatedHours = queryResults.getInt(3);
                String taskDetails = queryResults.getString(4);

                try {
                    Date dueDate = dateFormatter.parse(dueDateString);
                    Task taskItem = new Task(taskTitle, dueDate, estimatedHours, taskDetails);
                    todoCollection.add(taskItem);
                } catch (ParseException parseError) {
                    Log.e("DatabaseHelper", "Error parsing date: " + dueDateString, parseError);
                }
            } while (queryResults.moveToNext());
        }
        queryResults.close();
        return todoCollection;
    }

    @Override
    public void close() {
        if (dbInstance != null) {
            dbInstance.close();
        }
        super.close();
    }
}
