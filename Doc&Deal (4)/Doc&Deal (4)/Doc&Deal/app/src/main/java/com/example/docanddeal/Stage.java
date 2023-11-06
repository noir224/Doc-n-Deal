package com.example.docanddeal;

public class Stage {
    private int num;
    private String status,comment;

    public Stage(int num, String status) {
        this.num = num;
        this.status = status;
    }

    public Stage(int num, String status, String comment) {
        this.num = num;
        this.status = status;
        this.comment = comment;
    }


    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
