package com.example.docanddeal;
import java.io.Serializable;
public class NonFunc implements Serializable {
    String text;
    public NonFunc(String text) {
        this.text = text;
    }

    public NonFunc() {
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}


