package com.example.docanddeal;

import java.io.Serializable;

public class SharedWith implements Serializable {
    private SharedProject sp;
    private SponsorUser sponsor;

    public SharedWith(SharedProject sp, SponsorUser sponsor) {
        this.sp = sp;
        this.sponsor = sponsor;
    }

    public SharedProject getSp() {
        return sp;
    }

    public void setSp(SharedProject sp) {
        this.sp = sp;
    }

    public SponsorUser getSponsor() {
        return sponsor;
    }

    public void setSponsor(SponsorUser sponsor) {
        this.sponsor = sponsor;
    }
}
