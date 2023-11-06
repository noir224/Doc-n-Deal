package com.example.docanddeal;

import java.io.Serializable;

public class VCapabilities implements Serializable {
    String Benifit,feature;

    public VCapabilities(String benifit, String feature) {
        Benifit = benifit;
        this.feature = feature;
    }

    public VCapabilities(){}

    public String getBenifit() {
        return Benifit;
    }

    public void setBenifit(String benifit) {
        Benifit = benifit;
    }

    public String getFeature() {
        return feature;
    }

    public void setFeature(String feature) {
        this.feature = feature;
    }
}
