package com.example.docanddeal;

import java.io.Serializable;

public class Reference implements Serializable {
    private String text;

    public Reference(String text) {
        this.text = text;
    }

    public Reference() {
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
