package com.example.docanddeal;

public class Stage {
    private int num;
    private String status;

    public Stage(int num, String status) {
        this.num = num;
        this.status = status;
    }

    public Stage(int num, String status, String comment1, String comment2, String comment3) {
        this.num = num;
        this.status = status;
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

}
