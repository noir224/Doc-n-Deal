package com.example.docanddeal;

import java.util.ArrayList;

public class SponsorUser extends User{
    private String facebook,description, instagram,phone,twitter,field;
    private ArrayList<String> sponsoredProjects, rejectedProjects;

    public SponsorUser(String email, String imagepath, String type, String username, String id, ArrayList<String> sharedDocs,
                       String facebook, String description, String instagram, String phone, String twitter, String field,
                       ArrayList<String> sponsoredProjects, ArrayList<String> rejectedProjects) {
        super(email, imagepath, type, username, id, sharedDocs);
        this.facebook = facebook;
        this.description = description;
        this.instagram = instagram;
        this.phone = phone;
        this.twitter = twitter;
        this.field = field;
        this.sponsoredProjects = sponsoredProjects;
        this.rejectedProjects = rejectedProjects;
    }

    public SponsorUser() {
    }



    public ArrayList<String> getSponsoredProjects() {
        return sponsoredProjects;
    }

    public void setSponsoredProjects(ArrayList<String> sponsoredProjects) {
        this.sponsoredProjects = sponsoredProjects;
    }

    public ArrayList<String> getRejectedProjects() {
        return rejectedProjects;
    }

    public void setRejectedProjects(ArrayList<String> rejectedProjects) {
        this.rejectedProjects = rejectedProjects;
    }

    public SponsorUser(String email, String imagepath, String type, String username, String id, String field) {
        super(email, imagepath, type, username, id);
        this.field = field;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }



    public String getFacebook() {
        return facebook;
    }

    public void setFacebook(String facebook) {
        this.facebook = facebook;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getInstagram() {
        return instagram;
    }

    public void setInstagram(String instagram) {
        this.instagram = instagram;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getTwitter() {
        return twitter;
    }

    public void setTwitter(String twitter) {
        this.twitter = twitter;
    }




}
