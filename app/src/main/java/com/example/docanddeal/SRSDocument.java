package com.example.docanddeal;

import android.net.Uri;

import java.io.Serializable;
import java.util.ArrayList;

public class SRSDocument implements Serializable {
    private String uid,dname;
    private String ucdimg,ucdaml;
    private ArrayList<UseCase> ucs;
    private ArrayList<NonFunc> nfrs;
    private ArrayList<Reference> refs;
    private ArrayList<Abbrev> explination;
    private ArrayList<Actor> allactors;

    public SRSDocument(String uid, String dname, String ucdimg, String ucdaml, ArrayList<UseCase> ucs,
                       ArrayList<NonFunc> nfrs, ArrayList<Reference> refs, ArrayList<Abbrev> explination, ArrayList<Actor> allactors) {
        this.uid = uid;
        this.dname = dname;
        this.ucdimg = ucdimg;
        this.ucdaml = ucdaml;
        this.ucs = ucs;
        this.nfrs = nfrs;
        this.refs = refs;
        this.explination = explination;
        this.allactors = allactors;
    }

    public SRSDocument(String uid) {
        this.uid = uid;
        dname = "Untitled";
        ucdimg=null;
        ucdaml=null;
        ucs = new ArrayList<>();
        nfrs = new ArrayList<>();
        refs = new ArrayList<>();
        explination = new ArrayList<>();
        allactors = new ArrayList<>();
    }

    public SRSDocument() {
    }

    public ArrayList<Actor> getAllactors() {
        return allactors;
    }

    public void setAllactors(ArrayList<Actor> allactors) {
        this.allactors = allactors;
    }

    public String getUcdimg() {
        return ucdimg;
    }

    public void setUcdimg(String ucdimg) {
        this.ucdimg = ucdimg;
    }

    public String getUcdaml() {
        return ucdaml;
    }

    public void setUcdaml(String ucdaml) {
        this.ucdaml = ucdaml;
    }

    public ArrayList<Reference> getRefs() {
        return refs;
    }

    public void setRefs(ArrayList<Reference> refs) {
        this.refs = refs;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getDname() {
        return dname;
    }

    public void setDname(String dname) {
        this.dname = dname;
    }


    public ArrayList<UseCase> getUcs() {
        return ucs;
    }

    public void setUcs(ArrayList<UseCase> ucs) {
        this.ucs = ucs;
    }

    public ArrayList<NonFunc> getNfrs() {
        return nfrs;
    }

    public void setNfrs(ArrayList<NonFunc> nfrs) {
        this.nfrs = nfrs;
    }

    public ArrayList<Abbrev> getExplination() {
        return explination;
    }

    public void setExplination(ArrayList<Abbrev> explination) {
        this.explination = explination;
    }
}