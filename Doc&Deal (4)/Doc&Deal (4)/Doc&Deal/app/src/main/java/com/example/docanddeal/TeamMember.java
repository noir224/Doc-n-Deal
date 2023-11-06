package com.example.docanddeal;
import java.io.Serializable;
import java.util.ArrayList;
public class TeamMember implements Serializable {
    private String name;
    private ArrayList<String> capabilities;
    public TeamMember(String name, ArrayList<String> capabilities) {
        // this.docid = docid;
        this.name = name;
        this.capabilities = capabilities;
    }



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<String> getCapabilities() {
        return capabilities;
    }

    public void setCapabilities(ArrayList<String> capabilities) {
        this.capabilities = capabilities;
    }
}
