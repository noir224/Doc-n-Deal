package com.example.docanddeal;

import java.util.ArrayList;

public class User {
    private String email,imagepath,type,username,id;
    private ArrayList<String> sharedDocs;



    public User(String email, String imagepath, String type, String username, String id) {
        this.email = email;
        this.imagepath = imagepath;
        this.type = type;
        this.username = username;
        this.id = id;
    }

    public User(String email, String imagepath, String type, String username, String id, ArrayList<String> sharedDocs) {
        this.email = email;
        this.imagepath = imagepath;
        this.type = type;
        this.username = username;
        this.id = id;
        this.sharedDocs = sharedDocs;
    }

    public User() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImagepath() {
        return imagepath;
    }

    public void setImagepath(String imagepath) {
        this.imagepath = imagepath;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public ArrayList<String> getSharedDocs() {
        return sharedDocs;
    }

    public void setSharedDocs(ArrayList<String> sharedDocs) {
        this.sharedDocs = sharedDocs;
    }
}
