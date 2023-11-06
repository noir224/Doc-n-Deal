package com.example.docanddeal;
import java.util.ArrayList;
public class CreatorUser extends User{
    private ArrayList<String> projects;
    private ArrayList<String> pnames;
    public CreatorUser(String email, String imagepath, String type, String username, String id, ArrayList<String> sharedProjects, ArrayList<String> projects, ArrayList<String> pnames) {
        super(email, imagepath, type, username, id, sharedProjects);
        this.projects = projects;
        this.pnames = pnames;
    }

    public CreatorUser() {
    }

    public ArrayList<String> getProjects() {
        return projects;
    }

    public void setProjects(ArrayList<String> projects) {
        this.projects = projects;
    }

    public ArrayList<String> getPnames() {
        return pnames;
    }

    public void setPnames(ArrayList<String> pnames) {
        this.pnames = pnames;
    }
}