package com.example.docanddeal;

import java.io.Serializable;
import java.util.ArrayList;

public class UseCase implements Serializable, Comparable<UseCase>{
    private int ucnum=-1;
    private String name,id,author,priority,date,precond,postcond;
    private ImagePath stdscreen,mainflowseq;
    private ArrayList<Actor> actors= new ArrayList<>();
    private ArrayList<Relationship> relationships= new ArrayList<>();
    private ArrayList<Row> mainflowrows= new ArrayList<>();
    private ArrayList<Alternative> alternatives= new ArrayList<>();


    public UseCase(String name, String id) {
        this.name = name;
        this.id = id;
        actors = new ArrayList<>();
        relationships = new ArrayList<>();
    }

    public UseCase(int ucnum, String name, String id, String author, String priority, String date,
                   String precond, String postcond, ImagePath stdscreen, ImagePath mainflowseq,
                   ArrayList<Actor> actors, ArrayList<Relationship> relationships, ArrayList<Row> mainflowrows,
                   ArrayList<Alternative> alternatives) {
        this.ucnum = ucnum;
        this.name = name;
        this.id = id;
        this.author = author;
        this.priority = priority;
        this.date = date;
        this.precond = precond;
        this.postcond = postcond;
        this.stdscreen = stdscreen;
        this.mainflowseq = mainflowseq;
        this.actors = actors;
        this.relationships = relationships;
        this.mainflowrows = mainflowrows;
        this.alternatives = alternatives;
    }

    public UseCase(int ucnum, String date) {
        this.ucnum = ucnum;
        name = null;
        id = 5000+ucnum+"";
        author = null;
        priority = null;
        this.date = date;
        precond = null;
        postcond = null;
        stdscreen = null;
        mainflowseq = null;
        actors = new ArrayList<>();
        relationships = new ArrayList<>();
        mainflowrows = new ArrayList<>();
        alternatives = new ArrayList<>();
    }

    public UseCase() {
    }

    public int getUcnum() {
        return ucnum;
    }

    public void setUcnum(int ucnum) {
        this.ucnum = ucnum;
    }

    public ArrayList<Row> getMainflowrows() {
        return mainflowrows;
    }

    public void setMainflowrows(ArrayList<Row> mainflowrows) {
        this.mainflowrows = mainflowrows;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ArrayList<Actor> getActors() {
        return actors;
    }

    public void setActors(ArrayList<Actor> actors) {
        this.actors = actors;
    }

    public ArrayList<Relationship> getRelationships() {
        return relationships;
    }

    public void setRelationships(ArrayList<Relationship> relationships) {
        this.relationships = relationships;
    }


    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPrecond() {
        return precond;
    }

    public void setPrecond(String precond) {
        this.precond = precond;
    }

    public String getPostcond() {
        return postcond;
    }

    public void setPostcond(String postcond) {
        this.postcond = postcond;
    }


    public ImagePath getStdscreen() {
        return stdscreen;
    }

    public void setStdscreen(ImagePath stdscreen) {
        this.stdscreen = stdscreen;
    }

    public ImagePath getMainflowseq() {
        return mainflowseq;
    }

    public void setMainflowseq(ImagePath mainflowseq) {
        this.mainflowseq = mainflowseq;
    }

    public ArrayList<Alternative> getAlternatives() {
        return alternatives;
    }

    public void setAlternatives(ArrayList<Alternative> alternatives) {
        this.alternatives = alternatives;
    }

    @Override
    public int compareTo(UseCase o) {
        if(ucnum<o.getUcnum())
            return -1;
        else if(ucnum>o.getUcnum())
            return 1;
        else
            return 0;
    }
}
