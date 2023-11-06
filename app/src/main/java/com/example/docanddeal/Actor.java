package com.example.docanddeal;
import java.io.Serializable;
public class Actor implements Serializable {
    private String id, name;
    public Actor(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public Actor() {
    }

    public Actor(String id) {
        this.id=id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}