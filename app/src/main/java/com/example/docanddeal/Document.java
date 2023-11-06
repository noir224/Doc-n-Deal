package com.example.docanddeal;

import java.io.Serializable;
import java.util.ArrayList;

public class Document implements Serializable {
    private String uid,type,name,docid,pid,verid;
    private ArrayList<String> copies;


    public Document(String uid, String type, String name, String docid, String pid, String verid, ArrayList<String> copies) {
        this.uid = uid;
        this.type = type;
        this.name = name;
        this.docid = docid;
        this.pid = pid;
        this.verid = verid;
        this.copies = copies;
    }

    public ArrayList<String> getCopies() {
        return copies;
    }

    public void setCopies(ArrayList<String> copies) {
        this.copies = copies;
    }

    public Document() {
    }

    public Document(String type, String docid) {
        this.type = type;
        this.docid = docid;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDocid() {
        return docid;
    }

    public void setDocid(String docid) {
        this.docid = docid;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getVerid() {
        return verid;
    }

    public void setVerid(String verid) {
        this.verid = verid;
    }
}
