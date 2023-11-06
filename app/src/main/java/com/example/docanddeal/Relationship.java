package com.example.docanddeal;

import java.io.Serializable;

public class Relationship implements Serializable {
    private String source,target, type, id;

    public Relationship(String source, String target, String type, String id) {
        this.source = source;
        this.target = target;
        this.type = type;
        this.id = id;
    }

    public Relationship() {
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
