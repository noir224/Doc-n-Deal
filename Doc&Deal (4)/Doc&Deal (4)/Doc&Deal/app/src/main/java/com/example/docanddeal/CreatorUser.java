package com.example.docanddeal;

import java.util.ArrayList;

public class CreatorUser extends User{
    ArrayList<String> projects;

    public CreatorUser(String email, String imagepath, String type, String username, String id, ArrayList<String> sharedDocs, ArrayList<String> projects) {
        super(email, imagepath, type, username, id, sharedDocs);
        this.projects = projects;
    }

    public CreatorUser() {
    }

    public ArrayList<String> getProjects() {
        return projects;
    }

    public void setProjects(ArrayList<String> projects) {
        this.projects = projects;
    }
}
