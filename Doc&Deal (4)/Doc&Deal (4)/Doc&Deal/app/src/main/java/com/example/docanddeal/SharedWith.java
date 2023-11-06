package com.example.docanddeal;

import java.io.Serializable;

public class SharedWith implements Serializable {
    private SharedDoc sd;
    private SponsorUser sponsor;

    public SharedWith(SharedDoc sd, SponsorUser sponsor) {
        this.sd = sd;
        this.sponsor = sponsor;
    }

    public SharedDoc getSd() {
        return sd;
    }

    public void setSd(SharedDoc sd) {
        this.sd = sd;
    }

    public SponsorUser getSponsor() {
        return sponsor;
    }

    public void setSponsor(SponsorUser sponsor) {
        this.sponsor = sponsor;
    }
}
