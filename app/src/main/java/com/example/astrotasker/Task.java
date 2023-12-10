package com.example.astrotasker;

// Task.java
public class Task {
    // Fields
    private String userId; // Added field for user ID
    private String taskText;
    private String category;
    private int xp;

    // Required default constructor for Firebase
    public Task() {
    }

    // Constructor for creating a new task
    public Task(String taskText, String category, int xp) {
        this.userId = userId;
        this.taskText = taskText;
        this.category = category;
        this.xp = xp;
    }

    // Constructor for completed tasks (can remove userId if unnecessary)
    public Task(String taskText, int xp) {
        this.taskText = taskText;
        this.xp = xp;
    }


    // Getters and setters

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTaskText() {
        return taskText;
    }

    public void setTaskText(String taskText) {
        this.taskText = taskText;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getXp() {
        return xp;
    }

    public void setXp(int xp) {
        this.xp = xp;
    }
}
