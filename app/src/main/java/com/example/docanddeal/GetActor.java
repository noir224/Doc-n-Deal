package com.example.docanddeal;

import java.util.ArrayList;

public class GetActor {
    private Actor a;
    private ArrayList<UseCase> ucs;
    private ArrayList<String> ucsids;


    public GetActor(Actor a) {
        this.a = a;
        ucs=new ArrayList<>();
        ucsids=new ArrayList<>();
        ucsids=new ArrayList<>();
    }

    public Actor getA() {
        return a;
    }

    public void setA(Actor a) {
        this.a = a;
    }

    public ArrayList<UseCase> getUcs() {
        return ucs;
    }

    public void setUcs(ArrayList<UseCase> ucs) {
        this.ucs = ucs;
    }

    public ArrayList<String> getUcsids() {
        return ucsids;
    }

    public void setUcsids(ArrayList<String> ucsids) {
        this.ucsids = ucsids;
    }
}


