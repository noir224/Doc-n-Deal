package com.example.docanddeal;

import java.io.Serializable;
import java.util.ArrayList;

public class Document implements Serializable {
    private String uid,type,name,docid,pid,verid;
    private ArrayList<SharedDoc> sharedDocs;

    public Document(String uid, String type, String name, String docid, String pid, String verid) {
        this.uid = uid;
        this.type = type;
        this.name = name;
        this.docid = docid;
        this.pid = pid;
        this.verid = verid;
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
