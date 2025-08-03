package com.example.todolist;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AddTaskActivity extends AppCompatActivity {

    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_task);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        
        // Initialize database helper
        databaseHelper = new DatabaseHelper(this);
    }

    public void onClickAddTask(View v) throws ParseException {
        String n, des;
        Date dl;
        int d;
        n = ((EditText)findViewById(R.id.etTaskName)).getText().toString();
        des = ((EditText)findViewById(R.id.etmDescriptions)).getText().toString();
        DatePicker dp = (DatePicker) findViewById(R.id.dpDeadline);
        d = Integer.valueOf(((EditText)findViewById(R.id.etDuration)).getText().toString());
        String dateText = String.valueOf(dp.getDayOfMonth()) + "/" +
                            String.valueOf(dp.getMonth() + 1) + "/" +
                            String.valueOf(dp.getYear());
        
        com.example.todolist.Task t = new Task(n, new SimpleDateFormat("dd/MM/yyyy").parse(dateText), d, des);
        System.out.println("Task new");
        System.out.println(t);
        
        // Save task to database instead of static list
        long taskId = databaseHelper.insertTask(t);
        if (taskId != -1) {
            Toast.makeText(getApplicationContext(), "Task saved successfully!", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getApplicationContext(), "Error saving task", Toast.LENGTH_LONG).show();
        }
        
        Intent i = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(i);
    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (databaseHelper != null) {
            databaseHelper.close();
        }
    }
}