package com.example.docanddeal;

import java.io.Serializable;

public class Chosen implements Serializable {
    private String ucname;


    public Chosen(String ucname) {
        this.ucname = ucname;
    }

    public String getUcname() {
        return ucname;
    }

    public void setUcname(String ucname) {
        this.ucname = ucname;
    }
}
