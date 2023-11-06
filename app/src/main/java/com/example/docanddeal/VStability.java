package com.example.docanddeal;

import java.io.Serializable;

public class VStability implements Serializable {
    String name, desc;
    public VStability(String name, String desc) {
        this.name = name;
        this.desc = desc;
    }

    public VStability(){}

    public String getName() {
        return name;
    }

    public String getDesc() {
        return desc;
    }
}
