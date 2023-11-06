package com.example.docanddeal;

import java.io.Serializable;
import java.util.ArrayList;

public class SponsoredProjects implements Serializable {

    private String name,logo,type,link,sponsorid,spid,isPrivate;

    public SponsoredProjects(){

    }

    public SponsoredProjects(String name, String logo, String type, String link, String sponsorid, String spid, String isPrivate) {
        this.name = name;
        this.logo = logo;
        this.type = type;
        this.link = link;
        this.sponsorid = sponsorid;
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

    public String getSponsorid() {
        return sponsorid;
    }

    public void setSponsorid(String sponsorid) {
        this.sponsorid = sponsorid;
    }

}
