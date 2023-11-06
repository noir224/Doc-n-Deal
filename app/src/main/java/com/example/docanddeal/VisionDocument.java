package com.example.docanddeal;

import java.io.Serializable;
import java.util.ArrayList;

public class VisionDocument implements Serializable {
    private String uid, Purpose,dname,Scope,Perspective,Conclusion;
    private ArrayList<String>  BusinessOpportunity,stakeholders,users,Alternatives,Assumptions,refrences  ;
    private ArrayList<Problemstatment> props;
    private ArrayList<Abbrev> explination;
    private ProductPosition prodpos;
    private ArrayList<VPriority> vPriorities;
    private ArrayList<VEffort> vEffort;
    private ArrayList<VRisk> vrisk;
    private ArrayList<VStability> vstability;
    private ArrayList<VCapabilities> vCapabilities;
    private ArrayList<KeyNeedsStake> vKeyNeed;
    private ArrayList<VProductFeature> vProductFeat;

    public VisionDocument() {
    }

    public VisionDocument(String uid, String purpose, String dname, String scope, String perspective, String conclusion, ArrayList<String> businessOpportunity, ArrayList<String> stakeholders, ArrayList<String> users, ArrayList<String> alternatives, ArrayList<String> assumptions, ArrayList<String> refrences, ArrayList<Problemstatment> props, ArrayList<Abbrev> explination, ProductPosition prodpos, ArrayList<VPriority> vPriorities, ArrayList<VEffort> vEffort, ArrayList<VRisk> vrisk, ArrayList<VStability> vstability, ArrayList<VCapabilities> vCapabilities, ArrayList<KeyNeedsStake> vKeyNeed, ArrayList<VProductFeature> vProductFeat) {
        this.uid = uid;
        Purpose = purpose;
        this.dname = dname;
        Scope = scope;
        Perspective = perspective;
        Conclusion = conclusion;
        BusinessOpportunity = businessOpportunity;
        this.stakeholders = stakeholders;
        this.users = users;
        Alternatives = alternatives;
        Assumptions = assumptions;
        this.refrences = refrences;
        this.props = props;
        this.explination = explination;
        this.prodpos = prodpos;
        this.vPriorities = vPriorities;
        this.vEffort = vEffort;
        this.vrisk = vrisk;
        this.vstability = vstability;
        this.vCapabilities = vCapabilities;
        this.vKeyNeed = vKeyNeed;
        this.vProductFeat = vProductFeat;
    }

    public ArrayList<VProductFeature> getvProductFeat() {
        return vProductFeat;
    }

    public void setvProductFeat(ArrayList<VProductFeature> vProductFeat) {
        this.vProductFeat = vProductFeat;
    }

    public ArrayList<KeyNeedsStake> getvKeyNeed() {
        return vKeyNeed;
    }

    public void setvKeyNeed(ArrayList<KeyNeedsStake> vKeyNeed) {
        this.vKeyNeed = vKeyNeed;
    }

    public ArrayList<VPriority> getvPriorities() {
        return vPriorities;
    }

    public void setvPriorities(ArrayList<VPriority> vPriorities) {
        this.vPriorities = vPriorities;
    }

    public ArrayList<VEffort> getvEffort() {
        return vEffort;
    }

    public void setvEffort(ArrayList<VEffort> vEffort) {
        this.vEffort = vEffort;
    }

    public ArrayList<VRisk> getVrisk() {
        return vrisk;
    }

    public void setVrisk(ArrayList<VRisk> vrisk) {
        this.vrisk = vrisk;
    }

    public ArrayList<VStability> getVstability() {
        return vstability;
    }

    public void setVstability(ArrayList<VStability> vstability) {
        this.vstability = vstability;
    }

    public ArrayList<VCapabilities> getvCapabilities() {
        return vCapabilities;
    }

    public void setvCapabilities(ArrayList<VCapabilities> vCapabilities) {
        this.vCapabilities = vCapabilities;
    }


    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getPurpose() {
        return Purpose;
    }

    public void setPurpose(String purpose) {
        Purpose = purpose;
    }

    public String getDname() {
        return dname;
    }

    public void setDname(String dname) {
        this.dname = dname;
    }

    public String getScope() {
        return Scope;
    }

    public void setScope(String scope) {
        Scope = scope;
    }

    public String getPerspective() {
        return Perspective;
    }

    public void setPerspective(String perspective) {
        Perspective = perspective;
    }

    public String getConclusion() {
        return Conclusion;
    }

    public void setConclusion(String conclusion) {
        Conclusion = conclusion;
    }

    public ArrayList<String> getBusinessOpportunity() {
        return BusinessOpportunity;
    }

    public void setBusinessOpportunity(ArrayList<String> businessOpportunity) {
        BusinessOpportunity = businessOpportunity;
    }

    public ArrayList<String> getStakeholders() {
        return stakeholders;
    }

    public void setStakeholders(ArrayList<String> stakeholders) {
        this.stakeholders = stakeholders;
    }

    public ArrayList<String> getUsers() {
        return users;
    }

    public void setUsers(ArrayList<String> users) {
        this.users = users;
    }

    public ArrayList<String> getAlternatives() {
        return Alternatives;
    }

    public void setAlternatives(ArrayList<String> alternatives) {
        Alternatives = alternatives;
    }

    public ArrayList<String> getAssumptions() {
        return Assumptions;
    }

    public void setAssumptions(ArrayList<String> assumptions) {
        Assumptions = assumptions;
    }

    public ArrayList<String> getRefrences() {
        return refrences;
    }

    public void setRefrences(ArrayList<String> refrences) {
        this.refrences = refrences;
    }

    public ArrayList<Problemstatment> getProps() {
        return props;
    }

    public void setProps(ArrayList<Problemstatment> props) {
        this.props = props;
    }

    public ArrayList<Abbrev> getExplination() {
        return explination;
    }

    public void setExplination(ArrayList<Abbrev> explination) {
        this.explination = explination;
    }

    public ProductPosition getProdpos() {
        return prodpos;
    }

    public void setProdpos(ProductPosition prodpos) {
        this.prodpos = prodpos;
    }
}
