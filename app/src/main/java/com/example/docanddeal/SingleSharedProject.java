package com.example.docanddeal;

import java.io.Serializable;
import java.util.ArrayList;

public class SingleSharedProject implements Serializable {
    private ProjectC project;
    private SharedProject sharedDocs;
    private Version version;

    public SingleSharedProject(ProjectC project, SharedProject sharedDocs, Version version) {
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

    public SharedProject getSharedProjects() {
        return sharedDocs;
    }

    public void setSharedProjects(SharedProject sharedDocs) {
        this.sharedDocs = sharedDocs;
    }

    public Version getVersion() {
        return version;
    }

    public void setVersion(Version version) {
        this.version = version;
    }
}
