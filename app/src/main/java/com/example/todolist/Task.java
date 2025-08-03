package com.example.todolist;

import java.util.Date;

public class Task {
    public String name;
    public Date deadline;
    public int duration;
    public String description;
    
    public Task(String n, Date dl, int d, String des) {
        name = n;
        deadline = dl;
        duration = d;
        description = des;
    }
}
