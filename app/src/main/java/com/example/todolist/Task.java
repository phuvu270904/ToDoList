package com.example.todolist;

import java.util.Date;

public class Task {
    public String title;
    public Date dueDate;
    public int estimatedHours;
    public String details;
    
    public Task(String taskTitle, Date deadline, int timeRequired, String taskDetails) {
        title = taskTitle;
        dueDate = deadline;
        estimatedHours = timeRequired;
        details = taskDetails;
    }
}
