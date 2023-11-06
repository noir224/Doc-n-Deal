package com.example.docanddeal;




import com.google.firebase.Timestamp;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class SharedProject implements Serializable, Comparable<SharedProject> {
    private String proposalname, poriginalId,pnewId, voriginalId,vnewId, soriginalId,snewId, pid,creatorID,
            sharedWith,type,projectVersion,stage1,stage2,stage3,sharedprojectid,comment1,comment2,comment3;
    private boolean openedsponsor, openedcreator;
    private String time;



    public SharedProject(String proposalname, String poriginalId, String pnewId, String voriginalId, String vnewId, String soriginalId, String snewId, String pid, String creatorID, String sharedWith, String type, String projectVersion, String stage1, String stage2, String stage3, String sharedprojectid, String comment1, String comment2, String comment3,boolean openedsponsor, boolean openedcreator,String time) {
        this.proposalname = proposalname;
        this.poriginalId = poriginalId;
        this.pnewId = pnewId;
        this.voriginalId = voriginalId;
        this.vnewId = vnewId;
        this.soriginalId = soriginalId;
        this.snewId = snewId;
        this.pid = pid;
        this.creatorID = creatorID;
        this.sharedWith = sharedWith;
        this.type = type;
        this.projectVersion = projectVersion;
        this.stage1 = stage1;
        this.stage2 = stage2;
        this.stage3 = stage3;
        this.sharedprojectid = sharedprojectid;
        this.comment1 = comment1;
        this.comment2 = comment2;
        this.comment3 = comment3;
        this.openedsponsor = openedsponsor;
        this.openedcreator = openedcreator;
        this.time = time;

    }

    public SharedProject() {
    }

    public void setComment1(String comment1) {
        this.comment1 = comment1;
    }

    public void setComment2(String comment2) {
        this.comment2 = comment2;
    }

    public void setComment3(String comment3) {
        this.comment3 = comment3;
    }

    public String getComment1() {
        return comment1;
    }

    public String getComment2() {
        return comment2;
    }

    public String getComment3() {
        return comment3;
    }

    public String getProposalname() {
        return proposalname;
    }

    public void setProposalname(String proposalname) {
        this.proposalname = proposalname;
    }

    public String getPoriginalId() {
        return poriginalId;
    }

    public void setPoriginalId(String poriginalId) {
        this.poriginalId = poriginalId;
    }

    public String getPnewId() {
        return pnewId;
    }

    public void setPnewId(String pnewId) {
        this.pnewId = pnewId;
    }

    public String getVoriginalId() {
        return voriginalId;
    }

    public void setVoriginalId(String voriginalId) {
        this.voriginalId = voriginalId;
    }

    public String getVnewId() {
        return vnewId;
    }

    public void setVnewId(String vnewId) {
        this.vnewId = vnewId;
    }

    public String getSoriginalId() {
        return soriginalId;
    }

    public void setSoriginalId(String soriginalId) {
        this.soriginalId = soriginalId;
    }

    public String getSnewId() {
        return snewId;
    }

    public void setSnewId(String snewId) {
        this.snewId = snewId;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getCreatorID() {
        return creatorID;
    }

    public void setCreatorID(String creatorID) {
        this.creatorID = creatorID;
    }

    public String getSharedWith() {
        return sharedWith;
    }

    public void setSharedWith(String sharedWith) {
        this.sharedWith = sharedWith;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getProjectVersion() {
        return projectVersion;
    }

    public void setProjectVersion(String projectVersion) {
        this.projectVersion = projectVersion;
    }

    public String getStage1() {
        return stage1;
    }

    public void setStage1(String stage1) {
        this.stage1 = stage1;
    }

    public String getStage2() {
        return stage2;
    }

    public void setStage2(String stage2) {
        this.stage2 = stage2;
    }

    public String getStage3() {
        return stage3;
    }

    public void setStage3(String stage3) {
        this.stage3 = stage3;
    }

    public String getSharedprojectid() {
        return sharedprojectid;
    }

    public boolean isOpenedsponsor() {
        return openedsponsor;
    }

    public void setOpenedsponsor(boolean opened) {
        this.openedsponsor = opened;
    }

    public boolean isOpenedcreator() {
        return openedcreator;
    }

    public void setOpenedcreator(boolean openedcreator) {
        this.openedcreator = openedcreator;
    }

    public void setSharedprojectid(String sharedprojectid) {
        this.sharedprojectid = sharedprojectid;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Date getDateTime(){
        if (!time.isEmpty()){
        Date date=new Date(Long.parseLong(time));
        return date;}
        else{
            return null;
        }
    }
    public String getstringdate(){
        Date currentDate = getDateTime();
        DateFormat df = new SimpleDateFormat("dd:MM:yy:HH:mm:ss");
        Calendar cal = Calendar.getInstance();
        if (!time.isEmpty()){
        cal.setTimeInMillis(Long.parseLong(time));
        return ("Milliseconds to Date using Calendar:"
                + df.format(cal.getTime()));}
        else {return ("no date");}
    }

    @Override
    public int compareTo(SharedProject s) {
        if (getDateTime() == null || s.getDateTime() == null)
            return 0;
        return getDateTime().compareTo(s.getDateTime());
    }
}
