package com.example.todolist;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    public static ArrayList<Task> todoItemsCollection = new ArrayList<Task>();
    private DatabaseHelper dbManager;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        
        // Initialize database helper
        dbManager = new DatabaseHelper(this);
    }

    protected void onStart() {
        super.onStart();
        // Load tasks from database
        todoItemsCollection = dbManager.getAllTasks();
        
        ListView taskListView = findViewById(R.id.listViewTask);
        TaskAdapter listAdapter = new TaskAdapter(this, todoItemsCollection);
        taskListView.setAdapter(listAdapter);
    }

    public void handleAddButtonClick(View viewElement) {
        Intent navigationIntent = new Intent(getApplicationContext(), AddTaskActivity.class);
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