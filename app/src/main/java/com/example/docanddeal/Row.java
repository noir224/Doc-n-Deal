package com.example.docanddeal;

import android.net.Uri;

import java.io.Serializable;

public class Row implements Serializable {
    private String ua,sr,type;
    private ImagePath uascreen,srscreen;
    private int num,ucnum;

    public Row() {
    }

    public Row(String ua, String sr, String type, ImagePath uascreen, ImagePath srscreen, int num, int ucnum) {
        this.ua = ua;
        this.sr = sr;
        this.type = type;
        this.uascreen = uascreen;
        this.srscreen = srscreen;
        this.num = num;
        this.ucnum = ucnum;
    }

    public Row(String type,int num, int ucnum) {
        this.type = type;
        this.num = num;
        this.ucnum = ucnum;
    }

    public int getUcnum() {
        return ucnum;
    }

    public void setUcnum(int ucnum) {
        this.ucnum = ucnum;
    }

    public ImagePath getUascreen() {
        return uascreen;
    }

    public void setUascreen(ImagePath uascreen) {
        this.uascreen = uascreen;
    }

    public ImagePath getSrscreen() {
        return srscreen;
    }

    public void setSrscreen(ImagePath srscreen) {
        this.srscreen = srscreen;
    }

    public String getUa() {
        return ua;
    }

    public void setUa(String ua) {
        this.ua = ua;
    }

    public String getSr() {
        return sr;
    }

    public void setSr(String sr) {
        this.sr = sr;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }


}
