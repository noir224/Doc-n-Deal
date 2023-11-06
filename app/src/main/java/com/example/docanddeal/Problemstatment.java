package com.example.docanddeal;

import java.io.Serializable;

public class Problemstatment implements Serializable {
    private String problem,affect,impact,solution;
    private static int num = -1;

    public Problemstatment(String problem, String affect, String impact, String solution) {
        this.problem = problem;
        this.affect = affect;
        this.impact = impact;
        this.solution = solution;
        this.num= num+1;
    }

    public Problemstatment(){
    }

    public String getProblem() {
        return problem;
    }

    public void setProblem(String problem) {
        this.problem = problem;
    }

    public String getAffect() {
        return affect;
    }

    public void setAffect(String affect) {
        this.affect = affect;
    }

    public String getImpact() {
        return impact;
    }

    public void setImpact(String impact) {
        this.impact = impact;
    }

    public String getSolution() {
        return solution;
    }

    public void setSolution(String solution) {
        this.solution = solution;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }
}
