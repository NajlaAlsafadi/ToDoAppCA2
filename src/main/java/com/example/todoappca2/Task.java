package com.example.todoappca2;

public class Task {

    private String tag;
    private String description;
    private String deadline;
    private String status;
    private String id;
    public Task() {

    }

    public Task(String tag, String description, String deadline, String status) {
        this.tag = tag;
        this.description = description;
        this.deadline = deadline;
        this.status = status;
    }
    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public String getDeadline() {
        return deadline;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }






}