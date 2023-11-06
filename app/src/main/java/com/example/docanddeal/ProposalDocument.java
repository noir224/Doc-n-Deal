package com.example.docanddeal;
import java.io.Serializable;
import java.util.ArrayList;
public class ProposalDocument implements Serializable {
    private String uid, Objectives,dname;
    private ArrayList<String> UN, needs, outputs, features, indi, Soc,  Org, Ref;
    private ArrayList<TeamMember> team;
    ArrayList<Algorithm> Alg;
    ArrayList<API> API;
    ArrayList<Abbrev> explination;

    public ProposalDocument(String uid, String objectives, String dname, ArrayList<Abbrev> explination, ArrayList<String> UN,  ArrayList<String> needs, ArrayList<String> outputs, ArrayList<String> features, ArrayList<String> indi, ArrayList<String> soc, ArrayList<String> org, ArrayList<String> ref, ArrayList<TeamMember> team, ArrayList<Algorithm> alg, ArrayList<com.example.docanddeal.API> API) {
        this.uid = uid;
        Objectives = objectives;
        this.dname = dname;
        this.UN = UN;
        this.explination=explination;


        this.needs = needs;
        this.outputs = outputs;
        this.features = features;
        this.indi = indi;
        Soc = soc;
        Org = org;
        Ref = ref;
        this.team = team;
        Alg = alg;
        this.API = API;
    }
    public ProposalDocument(){}

    public ArrayList<Abbrev> getExplination() {
        return explination;
    }

    public void setExplination(ArrayList<Abbrev> explination) {
        this.explination = explination;
    }

    public ArrayList<String> getNeeds() {
        return needs;
    }

    public void setNeeds(ArrayList<String> needs) {
        this.needs = needs;
    }

    public ArrayList<String> getOutputs() {
        return outputs;
    }

    public void setOutputs(ArrayList<String> outputs) {
        this.outputs = outputs;
    }

    public ArrayList<String> getFeatures() {
        return features;
    }

    public void setFeatures(ArrayList<String> features) {
        this.features = features;
    }

    public ArrayList<String> getIndi() {
        return indi;
    }

    public void setIndi(ArrayList<String> indi) {
        this.indi = indi;
    }

    public ArrayList<String> getSoc() {
        return Soc;
    }

    public void setSoc(ArrayList<String> soc) {
        Soc = soc;
    }

    public ArrayList<String> getOrg() {
        return Org;
    }

    public void setOrg(ArrayList<String> org) {
        Org = org;
    }

    public ArrayList<String> getRef() {
        return Ref;
    }

    public void setRef(ArrayList<String> ref) {
        Ref = ref;
    }

    public ArrayList<TeamMember> getTeam() {
        return team;
    }

    public void setTeam(ArrayList<TeamMember> team) {
        this.team = team;
    }

    public ArrayList<Algorithm> getAlg() {
        return Alg;
    }

    public void setAlg(ArrayList<Algorithm> alg) {
        Alg = alg;
    }

    public ArrayList<com.example.docanddeal.API> getAPI() {
        return API;
    }

    public void setAPI(ArrayList<com.example.docanddeal.API> API) {
        this.API = API;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getObjectives() {
        return Objectives;
    }

    public void setObjectives(String objectives) {
        Objectives = objectives;
    }


    public ArrayList<String> getUN() {
        return UN;
    }

    public void setUN(ArrayList<String> UN) {
        this.UN = UN;
    }


    public String getDname() {
        return dname;
    }

    public void setDname(String dname) {
        this.dname = dname;
    }

}