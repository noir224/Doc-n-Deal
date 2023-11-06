package com.example.docanddeal;

import java.io.Serializable;

public class SRProjects implements Serializable {

    private String name,logo,type,link, sharedWith,spid,isPrivate;

    public SRProjects(){

    }

    public SRProjects(String name, String logo, String type, String link, String sharedWith, String spid, String isPrivate) {
        this.name = name;
        this.logo = logo;
        this.type = type;
        this.link = link;
        this.sharedWith = sharedWith;
        this.spid = spid;
    }

    public String getIsPrivate() {
        return isPrivate;
    }

    public void setIsPrivate(String isPrivate) {
        this.isPrivate = isPrivate;
    }

    public String getSpid() {
        return spid;
    }

    public void setSpid(String spid) {
        this.spid = spid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getSharedWith() {
        return sharedWith;
    }

    public void setSharedWith(String sharedWith) {
        this.sharedWith = sharedWith;
    }

}
