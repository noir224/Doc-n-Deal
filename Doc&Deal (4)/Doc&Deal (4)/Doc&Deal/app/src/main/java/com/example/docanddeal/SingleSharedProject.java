package com.example.docanddeal;

import java.io.Serializable;
import java.util.ArrayList;

public class SingleSharedProject implements Serializable {
    private ProjectC project;
    private ArrayList<SharedDoc> sharedDocs;
    private String version;

    public SingleSharedProject(ProjectC project, ArrayList<SharedDoc> sharedDocs, String version) {
        this.project = project;
        this.sharedDocs = sharedDocs;
        this.version = version;
    }

    public ProjectC getProject() {
        return project;
    }

    public void setProject(ProjectC project) {
        this.project = project;
    }

    public ArrayList<SharedDoc> getSharedDocs() {
        return sharedDocs;
    }

    public void setSharedDocs(ArrayList<SharedDoc> sharedDocs) {
        this.sharedDocs = sharedDocs;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}
