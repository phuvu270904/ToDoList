package com.example.todolist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class TaskAdapter extends ArrayAdapter<Task> {
    public TaskAdapter(Context appContext, ArrayList<Task> todoItems) { 
        super(appContext, 0, todoItems); 
    }
    
    public View getView(int itemPosition, View recycledView, ViewGroup parentContainer) {
        Task currentTask = getItem(itemPosition);
        if (recycledView == null)
            recycledView = LayoutInflater.from(getContext()).inflate(R.layout.task_item, parentContainer, false);
        TextView textViewTitle = (TextView) recycledView.findViewById(R.id.tvTaskName);
        TextView textViewDueDate = (TextView) recycledView.findViewById(R.id.tvDeadline);
        TextView textViewHours = (TextView) recycledView.findViewById(R.id.tvDuration);
        TextView textViewDetails = (TextView) recycledView.findViewById(R.id.tvDescriptions);
        textViewTitle.setText(currentTask.title);
        textViewDueDate.setText(currentTask.dueDate.toString().substring(0,10));
        textViewHours.setText(String.valueOf(currentTask.estimatedHours));
        textViewDetails.setText(String.valueOf(currentTask.details));
        return recycledView;
    }
}
