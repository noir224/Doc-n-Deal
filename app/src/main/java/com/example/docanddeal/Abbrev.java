package com.example.docanddeal;

import java.io.Serializable;

public class Abbrev implements Serializable {
    private String  Name, Expi;

    public Abbrev(String name, String expi) {
        Name = name;
        Expi = expi;
    }
    public Abbrev() {

    }


    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getExpi() {
        return Expi;
    }

    public void setExpi(String expi) {
        Expi = expi;
    }
}
