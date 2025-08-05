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

    private DatabaseHelper dbManager;

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
        dbManager = new DatabaseHelper(this);
    }

    public void handleTaskSubmission(View viewElement) throws ParseException {
        String taskTitle, taskDescription;
        Date dueDate;
        int estimatedDuration;
        taskTitle = ((EditText)findViewById(R.id.editTextTaskTitle)).getText().toString();
        taskDescription = ((EditText)findViewById(R.id.editTextTaskDescription)).getText().toString();
        DatePicker datePicker = (DatePicker) findViewById(R.id.datePickerDeadline);
        estimatedDuration = Integer.valueOf(((EditText)findViewById(R.id.editTextTaskDuration)).getText().toString());
        String formattedDate = String.valueOf(datePicker.getDayOfMonth()) + "/" +
                            String.valueOf(datePicker.getMonth() + 1) + "/" +
                            String.valueOf(datePicker.getYear());
        
        com.example.todolist.Task newTaskItem = new Task(taskTitle, new SimpleDateFormat("dd/MM/yyyy").parse(formattedDate), estimatedDuration, taskDescription);
        System.out.println("Task new");
        System.out.println(newTaskItem);
        
        // Save task to database instead of static list
        long insertedTaskId = dbManager.addTaskToDatabase(newTaskItem);
        if (insertedTaskId != -1) {
            Toast.makeText(getApplicationContext(), "Task saved successfully!", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getApplicationContext(), "Error saving task", Toast.LENGTH_LONG).show();
        }
        
        Intent navigationIntent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(navigationIntent);
    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (dbManager != null) {
            dbManager.close();
        }
    }
}