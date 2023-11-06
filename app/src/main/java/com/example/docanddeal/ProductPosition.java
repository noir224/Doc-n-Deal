package com.example.docanddeal;

import java.io.Serializable;

public class ProductPosition implements Serializable {
    private String For ,who,whatIs,that, unlike, ourProduct;

    public ProductPosition(){
    }

    public ProductPosition(String aFor, String who, String whatIs, String that, String unlike, String ourProduct) {
        For = aFor;
        this.who = who;
        this.whatIs = whatIs;
        this.that = that;
        this.unlike = unlike;
        this.ourProduct = ourProduct;
    }

    public String getFor() {
        return For;
    }

    public void setFor(String aFor) {
        For = aFor;
    }

    public String getWho() {
        return who;
    }

    public void setWho(String who) {
        this.who = who;
    }

    public String getWhatIs() {
        return whatIs;
    }

    public void setWhatIs(String whatIs) {
        this.whatIs = whatIs;
    }

    public String getThat() {
        return that;
    }

    public void setThat(String that) {
        this.that = that;
    }

    public String getUnlike() {
        return unlike;
    }

    public void setUnlike(String unlike) {
        this.unlike = unlike;
    }

    public String getOurProduct() {
        return ourProduct;
    }

    public void setOurProduct(String ourProduct) {
        this.ourProduct = ourProduct;
    }
}
