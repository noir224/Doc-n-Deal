package com.example.docanddeal;

import java.io.Serializable;

public class ImagePath implements Serializable {
    String imagename,imagepath;

    public ImagePath(String imagename, String imagepath) {
        this.imagename = imagename;
        this.imagepath = imagepath;
    }

    public ImagePath() {
    }

    public String getImagename() {
        return imagename;
    }

    public void setImagename(String imagename) {
        this.imagename = imagename;
    }

    public String getImagepath() {
        return imagepath;
    }

    public void setImagepath(String imagepath) {
        this.imagepath = imagepath;
    }
}
