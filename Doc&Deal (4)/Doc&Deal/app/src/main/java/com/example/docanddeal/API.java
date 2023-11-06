package com.example.docanddeal;

import java.io.Serializable;

public class API implements Serializable {
    private String Name, Link;
    public API( String name, String link) {
        //this.docid = docid;
        Name = name;
        Link = link;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getLink() {
        return Link;
    }

    public void setLink(String link) {
        Link = link;
    }
}


