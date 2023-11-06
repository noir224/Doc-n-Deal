package com.example.docanddeal;

import java.io.Serializable;

public class Version implements Serializable {
    private String version, date;

    public Version(String version, String date) {
        this.version = version;
        this.date = date;

    }

    public Version() {
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

}
