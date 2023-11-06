package com.example.docanddeal;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
public class ProjectC implements Serializable {

    private String name,logo, description,type,isprivate,link,uid,pid;
    private ArrayList<Version> versions;
    public ProjectC() {
    }

    public ProjectC(String name, String logo, String description, String type, String isprivate, String link, String uid, String pid, ArrayList<Version> versions) {
        this.name = name;
        this.logo = logo;
        this.description = description;
        this.type = type;
        this.isprivate = isprivate;
        this.link = link;
        this.uid = uid;
        this.pid = pid;
        this.versions = versions;
    }

    public String getIsprivate() {
        return isprivate;
    }

    public void setIsprivate(String isprivate) {
        this.isprivate = isprivate;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
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

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public ArrayList<Version> getVersions() {
        return versions;
    }

    public void setVersions(ArrayList<Version> versions) {
        this.versions = versions;
    }
}