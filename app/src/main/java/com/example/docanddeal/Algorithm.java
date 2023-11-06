package com.example.docanddeal;

import java.io.Serializable;

public class Algorithm implements Serializable {
    private String  Alg, Name;
    public Algorithm(String alg, String name) {
        // this.docid = docid;
        Alg = alg;
        Name = name;
    }
    public Algorithm() {

    }




    public String getAlg() {
        return Alg;
    }

    public void setAlg(String alg) {
        Alg = alg;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }
}
