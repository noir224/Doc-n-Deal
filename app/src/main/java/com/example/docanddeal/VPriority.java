package com.example.docanddeal;

import java.io.Serializable;

public class VPriority implements Serializable {
    String name, desc;

    public VPriority(String name, String desc) {
        this.name = name;
        this.desc = desc;
    }

    public VPriority(){}

    public String getName() {
        return name;
    }

    public String getDesc() {
        return desc;
    }
}
