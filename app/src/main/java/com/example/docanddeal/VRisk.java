package com.example.docanddeal;

import java.io.Serializable;

public class VRisk implements Serializable {
    String name, desc;
    public VRisk(String name, String desc) {
        this.name = name;
        this.desc = desc;
    }

    public VRisk(){}

    public String getName() {
        return name;
    }

    public String getDesc() {
        return desc;
    }
}
