package com.example.todolist;

import java.util.Date;

public class Task {
    public String title;
    public Date dueDate;
    public int estimatedDays;
    public String details;
    
    public Task(String taskTitle, Date deadline, int timeRequired, String taskDetails) {
        title = taskTitle;
        dueDate = deadline;
        estimatedDays = timeRequired;
        details = taskDetails;
    }
}
