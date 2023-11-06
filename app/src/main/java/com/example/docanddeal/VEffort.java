package com.example.docanddeal;

import java.io.Serializable;

public class VEffort implements Serializable {
    String name, desc;
    public VEffort(String name, String desc) {
        this.name = name;
        this.desc = desc;
    }

    public VEffort(){}

    public String getName() {
        return name;
    }

    public String getDesc() {
        return desc;
    }
}
