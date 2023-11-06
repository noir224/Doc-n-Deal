package com.example.docanddeal;

import java.io.Serializable;

public class KeyNeedsStake implements Serializable {
    String Need, priority,concerns,csolutin,psolution;

    public KeyNeedsStake(String need, String priority, String concerns, String csolutin, String psolution) {
        Need = need;
        this.priority = priority;
        this.concerns = concerns;
        this.csolutin = csolutin;
        this.psolution = psolution;
    }

    public KeyNeedsStake(){}

    public String getNeed() {
        return Need;
    }

    public void setNeed(String need) {
        Need = need;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getConcerns() {
        return concerns;
    }

    public void setConcerns(String concerns) {
        this.concerns = concerns;
    }

    public String getCsolutin() {
        return csolutin;
    }

    public void setCsolutin(String csolutin) {
        this.csolutin = csolutin;
    }

    public String getPsolution() {
        return psolution;
    }

    public void setPsolution(String psolution) {
        this.psolution = psolution;
    }
}
