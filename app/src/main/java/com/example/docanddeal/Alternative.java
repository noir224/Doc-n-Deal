package com.example.docanddeal;

import android.net.Uri;

import java.io.Serializable;
import java.util.ArrayList;

public class Alternative implements Serializable {
    private int altnum,ucnum;
    private String altname;
    private ArrayList<Row> altrows;
    private ImagePath altseq;

    public Alternative(int altnum, int ucnum, String altname, ArrayList<Row> altrows, ImagePath altseq) {
        this.altnum = altnum;
        this.ucnum = ucnum;
        this.altname = altname;
        this.altrows = altrows;
        this.altseq = altseq;
    }

    public Alternative() {
    }

    public Alternative(int altnum, int ucnum) {
        this.altnum = altnum;
        this.ucnum = ucnum;
        altrows = new ArrayList<>();
    }


    public int getUcnum() {
        return ucnum;
    }

    public void setUcnum(int ucnum) {
        this.ucnum = ucnum;
    }

    public String getAltname() {
        return altname;
    }

    public void setAltname(String altname) {
        this.altname = altname;
    }

    public ImagePath getAltseq() {
        return altseq;
    }

    public void setAltseq(ImagePath altseq) {
        this.altseq = altseq;
    }

    public int getAltnum() {
        return altnum;
    }

    public void setAltnum(int altnum) {
        this.altnum = altnum;
    }

    public String getName() {
        return altname;
    }

    public void setName(String altname) {
        this.altname = altname;
    }

    public ArrayList<Row> getAltrows() {
        return altrows;
    }

    public void setAltrows(ArrayList<Row> altrows) {
        this.altrows = altrows;
    }
}
