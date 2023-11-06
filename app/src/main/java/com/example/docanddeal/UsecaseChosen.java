package com.example.docanddeal;

public class UsecaseChosen {
    private String ucname;
    private boolean chosen;

    public UsecaseChosen(String ucname) {
        this.ucname = ucname;
        chosen=false;
    }

    public String getUcname() {
        return ucname;
    }

    public void setUcname(String ucname) {
        this.ucname = ucname;
    }

    public boolean isChosen() {
        return chosen;
    }

    public void setChosen(boolean chosen) {
        this.chosen = chosen;
    }
}
