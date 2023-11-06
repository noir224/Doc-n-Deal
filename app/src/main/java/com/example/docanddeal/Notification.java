package com.example.docanddeal;

import android.widget.TextView;

public class Notification {

    private String title, description, time,imagepath;

    public Notification(String title, String description, String time, String imagepath) {
        this.title = title;
        this.description = description;
        this.time = time;
        this.imagepath = imagepath;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getTime() {
        return time;
    }

    public String getImagepath() {
        return imagepath;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setImagepath(String imagepath) {
        this.imagepath = imagepath;
    }
}
