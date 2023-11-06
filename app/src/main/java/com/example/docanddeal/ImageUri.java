package com.example.docanddeal;

import java.io.Serializable;

public class ImageUri implements Serializable {
    private String Imagename,uri,pid,phototype;

    public ImageUri(String imagename, String uri, String pid, String phototype) {
        Imagename = imagename;
        this.uri = uri;
        this.pid = pid;
        this.phototype = phototype;
    }

    public ImageUri() {
    }

    public String getImagename() {
        return Imagename;
    }

    public String getPhototype() {
        return phototype;
    }

    public void setPhototype(String phototype) {
        this.phototype = phototype;
    }

    public void setImagename(String imagename) {
        Imagename = imagename;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }
}
