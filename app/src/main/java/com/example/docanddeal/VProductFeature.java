package com.example.docanddeal;

import java.io.Serializable;

public class VProductFeature implements Serializable {
    String name,priority,effort,risk,stability;

    public VProductFeature(String name, String priority, String effort, String risk, String stability) {
        this.name = name;
        this.priority = priority;
        this.effort = effort;
        this.risk = risk;
        this.stability = stability;
    }

    public VProductFeature() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getEffort() {
        return effort;
    }

    public void setEffort(String effort) {
        this.effort = effort;
    }

    public String getRisk() {
        return risk;
    }

    public void setRisk(String risk) {
        this.risk = risk;
    }

    public String getStability() {
        return stability;
    }

    public void setStability(String stability) {
        this.stability = stability;
    }
}
