package com.example.docanddeal;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintJob;
import android.print.PrintManager;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class VisionPdf extends AppCompatActivity {
    String docID;
    FirebaseFirestore fs;
    FirebaseAuth fAuth;
    DocumentReference dref;
    ImageView done,download;
    private Bitmap bitmap;
    RelativeLayout toPDF ;
    VisionDocument vd;
    WebView wview;
    String logourl;
    ProjectC pro;
    Version ver;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vision_pdf);
        Bundle b = getIntent().getExtras();
        docID = b.get("docID").toString();
        logourl = b.get("logo").toString();
        ver = (Version) b.get("ver");
        pro = (ProjectC) b.getSerializable("Project");
        wview = findViewById(R.id.PRODVIEW);
        fAuth = FirebaseAuth.getInstance();
        fs = FirebaseFirestore.getInstance();
        dref = fs.collection("visionDoc").document(docID);
        download = findViewById(R.id.visionDownload);
        done = findViewById(R.id.visionFinish);
        toPDF = findViewById(R.id.VISPDF);

        dref.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                vd = documentSnapshot.toObject(VisionDocument.class);
                ArrayList<String> tm = pro.getTeammebers();
                StringBuffer sb = new StringBuffer();
                if (tm.size()!=0){
                    for (String s : tm) {
                        sb.append(s);
                        sb.append(System.getProperty("line.separator"));
                        sb.append(System.getProperty("line.separator"));
                    }
                    sb.append(System.getProperty("line.separator"));
                    sb.append(System.getProperty("line.separator"));
                    sb.append(System.getProperty("line.separator"));

                }
                else
                {
                    sb.append(System.getProperty("line.separator"));
                    sb.append(System.getProperty("line.separator"));
                    sb.append(System.getProperty("line.separator"));
                    sb.append(System.getProperty("line.separator"));
                    sb.append(System.getProperty("line.separator"));
                    sb.append(System.getProperty("line.separator"));
                    sb.append(System.getProperty("line.separator"));
                    sb.append(System.getProperty("line.separator"));
                    sb.append(System.getProperty("line.separator"));
                    sb.append(System.getProperty("line.separator"));
                    sb.append(System.getProperty("line.separator"));


                }
                String prepareBy = sb.toString();

                String purpose = " \t\t"+vd.getPurpose();
                String scope = " \t\t"+vd.getScope();

                StringBuffer sb1 = new StringBuffer();
                ArrayList<Abbrev> abb = vd.getExplination();
                for (Abbrev s : abb) {
                    sb1.append("\t-\t"+s.getName()+": "+s.getExpi());
                    sb1.append(System.getProperty("line.separator"));
                }
                String abbrev = sb1.toString();

                String name = vd.getDname();

                ArrayList<Problemstatment>  pt= vd.getProps();
                String problemStatment = "";
                if(pt.size()!=0){
                 problemStatment ="<table style= \"width:90%; border: 1px solid black; border-collapse: collapse;margin-left: auto; margin-right: auto;\">\n" +
                        "  <tr style= \"width:90%; border: 1px solid black; border-collapse: collapse;margin-left: auto; margin-right: auto;\">\n" +
                        "    <td style= \"width:50%; border: 1px solid black; border-collapse: collapse;font-size:14px,font-family:Times New Roman;height:50px;margin-left: auto; margin-right: auto;text-align: center\">The Problem of</td>\n" +
                        "    <td style= \"width:50%; border: 1px solid black; border-collapse: collapse;font-size:14px,font-family:Times New Roman;height:50px;margin-left: auto; margin-right: auto;text-align: center\">"+pt.get(0).getProblem()+"</td>\n" +
                        "  "+ "<tr style= \"width:90%; border: 1px solid black; border-collapse: collapse;margin-left: auto; margin-right: auto;\">\n" +
                        "    <td style= \"width:50%; border: 1px solid black; border-collapse: collapse;font-size:14px,font-family:Times New Roman;height:50px;margin-left: auto; margin-right: auto;text-align: center\">Affects</td>\n" +
                        "    <td style= \"width:50%; border: 1px solid black; border-collapse: collapse;font-size:14px,font-family:Times New Roman;height:50px;margin-left: auto; margin-right: auto;text-align: center\">"+pt.get(0).getAffect()+"</td>\n" +
                        "<tr style= \"width:90%; border: 1px solid black; border-collapse: collapse;margin-left: auto; margin-right: auto;\">\n" +
                        "    <td style= \"width:50%; border: 1px solid black; border-collapse: collapse;font-size:14px,font-family:Times New Roman;height:50px;margin-left: auto; margin-right: auto;text-align: center\">The Impact of Which is</td>\n" +
                        "    <td style= \"width:50%; border: 1px solid black; border-collapse: collapse;font-size:14px,font-family:Times New Roman;height:50px;margin-left: auto; margin-right: auto;text-align: center\">"+pt.get(0).getImpact()+"</td>\n" +
                        "<tr style= \"width:90%; border: 1px solid black; border-collapse: collapse;margin-left: auto; margin-right: auto;\">\n" +
                        "    <td style= \"width:50%; border: 1px solid black; border-collapse: collapse;font-size:14px,font-family:Times New Roman;height:50px;margin-left: auto; margin-right: auto;text-align: center\">A Successful Solution Would be</td>\n" +
                        "    <td style= \"width:50%; border: 1px solid black; border-collapse: collapse;font-size:14px,font-family:Times New Roman;height:50px;margin-left: auto; margin-right: auto;text-align: center\">"+pt.get(0).getSolution()+"</td>\n" +
                        "" +"</table>";
                pt.remove(0);}


                String problemStatments = "";
                if(pt.size()!=0){
                int count = 0;
                for (Problemstatment s : pt) {
                    count = count+1;
                    problemStatments = problemStatments+"\n\n<table style= \"width:90%; border: 1px solid black; border-collapse: collapse;margin-left: auto; margin-right: auto;\">\n" +
                            "  <tr style= \"width:90%; border: 1px solid black; border-collapse: collapse;margin-left: auto; margin-right: auto;\">\n" +
                            "    <td style= \"width:50%; border: 1px solid black; border-collapse: collapse;font-size:14px,font-family:Times New Roman;height:50px;margin-left: auto; margin-right: auto;text-align: center\">The Sub Problem "+count+" of</td>\n" +
                            "    <td style= \"width:50%; border: 1px solid black; border-collapse: collapse;font-size:14px,font-family:Times New Roman;height:50px;margin-left: auto; margin-right: auto;text-align: center\">"+s.getProblem()+"</td>\n" +
                            "  "+ "<tr style= \"width:90%; border: 1px solid black; border-collapse: collapse;margin-left: auto; margin-right: auto;\">\n" +
                            "    <td style= \"width:50%; border: 1px solid black; border-collapse: collapse;font-size:14px,font-family:Times New Roman;height:50px;margin-left: auto; margin-right: auto;text-align: center\">Affects</td>\n" +
                            "    <td style= \"width:50%; border: 1px solid black; border-collapse: collapse;font-size:14px,font-family:Times New Roman;height:50px;margin-left: auto; margin-right: auto;text-align: center\">"+s.getAffect()+"</td>\n" +
                            "<tr style= \"width:90%; border: 1px solid black; border-collapse: collapse;margin-left: auto; margin-right: auto;\">\n" +
                            "    <td style= \"width:50%; border: 1px solid black; border-collapse: collapse;font-size:14px,font-family:Times New Roman;height:50px;margin-left: auto; margin-right: auto;text-align: center\">The Impact of Which is</td>\n" +
                            "    <td style= \"width:50%; border: 1px solid black; border-collapse: collapse;font-size:14px,font-family:Times New Roman;height:50px;margin-left: auto; margin-right: auto;text-align: center\">"+s.getImpact()+"</td>\n" +
                            "<tr style= \"width:90%; border: 1px solid black; border-collapse: collapse;margin-left: auto; margin-right: auto;\">\n" +
                            "    <td style= \"width:50%; border: 1px solid black; border-collapse: collapse;font-size:14px,font-family:Times New Roman;height:50px;margin-left: auto; margin-right: auto;text-align: center\">A Successful Solution Would be</td>\n" +
                            "    <td style= \"width:50%; border: 1px solid black; border-collapse: collapse;font-size:14px,font-family:Times New Roman;height:50px;margin-left: auto; margin-right: auto;text-align: center\">"+s.getSolution()+"</td>\n" +
                            "" +"</table>";
                }}


                StringBuffer sb2 = new StringBuffer();
                ArrayList<String>  buis= vd.getBusinessOpportunity();
                for (String s : buis) {
                    sb2.append("\t-\t"+s+" ");
                    sb2.append(System.getProperty("line.separator"));
                }
                String buisness = sb2.toString();

                ProductPosition pp= vd.getProdpos();
                String prodpos = "";
                if(!pp.equals(null)){
                 prodpos ="<table style= \"width:90%; border: 1px solid black; border-collapse: collapse;margin-left: auto; margin-right: auto;\">\n" +
                        "  <tr style= \"width:90%; border: 1px solid black; border-collapse: collapse;margin-left: auto; margin-right: auto;\">\n" +
                        "    <td style= \"width:30%; border: 1px solid black; border-collapse: collapse;font-size:14px,font-family:Times New Roman;height:50px;margin-left: auto; margin-right: auto;text-align: center\">For</td>\n" +
                        "    <td style= \"width:70%; border: 1px solid black; border-collapse: collapse;font-size:14px,font-family:Times New Roman;height:50px;margin-left: auto; margin-right: auto;text-align: center\">"+pp.getFor()+"</td>\n" +
                        "  "+ "<tr style= \"width:90%; border: 1px solid black; border-collapse: collapse;margin-left: auto; margin-right: auto;\">\n" +
                        "    <td style= \"width:30%; border: 1px solid black; border-collapse: collapse;font-size:14px,font-family:Times New Roman;height:50px;margin-left: auto; margin-right: auto;text-align: center\">Who</td>\n" +
                        "<td style= \"width:70%; border: 1px solid black; border-collapse: collapse;font-size:14px,font-family:Times New Roman;height:50px;margin-left: auto; margin-right: auto;text-align: center\">"+pp.getWho()+"</td>\n" +
                         "<tr style= \"width:90%; border: 1px solid black; border-collapse: collapse;margin-left: auto; margin-right: auto;\">\n" +
                        "    <td style= \"width:30%; border: 1px solid black; border-collapse: collapse;font-size:14px,font-family:Times New Roman;height:50px;text-align: center\">The "+name+"</td>\n" +
                        "    <td style= \"width:70%; border: 1px solid black; border-collapse: collapse;font-size:14px,font-family:Times New Roman;height:50px;text-align: center\">"+pp.getWhatIs()+"</td>\n" +
                        "<tr style= \"width:90%; border: 1px solid black; border-collapse: collapse;\">\n" +
                        "    <td style= \"width:30%; border: 1px solid black; border-collapse: collapse;font-size:14px,font-family:Times New Roman;height:50px;text-align: center\">That</td>\n" +
                        "    <td style= \"width:70%; border: 1px solid black; border-collapse: collapse;font-size:14px,font-family:Times New Roman;height:50px;text-align: center\">"+pp.getThat()+"</td>\n" +
                        "<tr style= \"width:90%; border: 1px solid black; border-collapse: collapse;\">\n" +
                        "    <td style= \"width:30%; border: 1px solid black; border-collapse: collapse;font-size:14px,font-family:Times New Roman;height:50px;text-align: center\">Unlike</td>\n" +
                        "    <td style= \"width:70%; border: 1px solid black; border-collapse: collapse;font-size:14px,font-family:Times New Roman;height:50px;text-align: center\">"+pp.getUnlike()+"</td>\n" +
                        "<tr style= \"width:90%; border: 1px solid black; border-collapse: collapse;\">\n" +
                        "    <td style= \"width:30%; border: 1px solid black; border-collapse: collapse;font-size:14px,font-family:Times New Roman;height:50px;text-align: center\">Our product</td>\n" +
                        "    <td style= \"width:70%; border: 1px solid black; border-collapse: collapse;font-size:14px,font-family:Times New Roman;height:50px;text-align: center\">"+pp.getOurProduct()+"</td>\n" +
                        "" +"</table>";}

                StringBuffer sb3 = new StringBuffer();
                ArrayList<String>  stake= vd.getStakeholders();
                for (String s : stake) {
                    sb3.append("\t-\t"+s+" ");
                    sb3.append(System.getProperty("line.separator"));
                }
                String stakeholder = sb3.toString();

                StringBuffer sb4 = new StringBuffer();
                ArrayList<String>  user= vd.getStakeholders();
                for (String s : user) {
                    sb4.append("\t-\t"+s+" ");
                    sb4.append(System.getProperty("line.separator"));
                }
                String users = sb4.toString();

                ArrayList<KeyNeedsStake>  keyn= vd.getvKeyNeed();
                String keyNeed = "";
                if(keyn.size()!=0){
                 keyNeed ="<table style= \"width:90%; border: 1px solid black; border-collapse: collapse;margin-left: auto; margin-right: auto;\">\n" +
                        "  <tr style= \"width:90%; border: 1px solid black; border-collapse: collapse;\">\n" +
                        "    <th style= \"width:22%; border: 1px solid black; border-collapse: collapse;font-size:14px,font-family:Times New Roman;height:50px;text-align: center\">Need</th>\n" +
                        "    <th style= \"width:10%; border: 1px solid black; border-collapse: collapse;font-size:14px,font-family:Times New Roman;height:50px;text-align: center\">Priority</th>\n" +
                        "    <th style= \"width:22%; border: 1px solid black; border-collapse: collapse;font-size:14px,font-family:Times New Roman;height:50px;text-align: center\">Concerns</th>\n" +
                        "    <th style= \"width:23%; border: 1px solid black; border-collapse: collapse;font-size:14px,font-family:Times New Roman;height:50px;text-align: center\">Current Solution</th>\n" +
                        "    <th style= \"width:23%; border: 1px solid black; border-collapse: collapse;font-size:14px,font-family:Times New Roman;height:50px;text-align: center\">Proposed Solution</th>\n" +
                        "  ";
                for (KeyNeedsStake s:keyn){

                    keyNeed = keyNeed+ "<tr style= \"width:90%; border: 1px solid black; border-collapse: collapse;\">\n" +
                            "<td style= \"width:22%; border: 1px solid black; border-collapse: collapse;font-size:12px,font-family:Times New Roman;height:50px;text-align: center\">"+s.getNeed()+"</td>\n" +
                            "<td style= \"width:10%; border: 1px solid black; border-collapse: collapse;font-size:12px,font-family:Times New Roman;height:50px;text-align: center\">"+s.getPriority()+"</td>\n" +
                            "<td style= \"width:22%; border: 1px solid black; border-collapse: collapse;font-size:12px,font-family:Times New Roman;height:50px;text-align: center\">"+s.getConcerns()+"</td>\n" +
                            "<td style= \"width:23%; border: 1px solid black; border-collapse: collapse;font-size:12px,font-family:Times New Roman;height:50px;text-align: center\">"+s.getCsolutin()+"</td>\n" +
                            "<td style= \"width:23%; border: 1px solid black; border-collapse: collapse;font-size:12px,font-family:Times New Roman;height:50px;text-align: center\">"+s.getPsolution()+"</td>\n" +
                            "";
                }
                keyNeed = keyNeed+"</table>";}

                StringBuffer sb5 = new StringBuffer();
                ArrayList<String>  alter= vd.getAlternatives();
                for (String s : alter) {
                    sb5.append("\t-\t"+s+" ");
                    sb5.append(System.getProperty("line.separator"));
                }
                String alternative = sb5.toString();

                String prodpres = vd.getPerspective();

                ArrayList<VCapabilities>  cap= vd.getvCapabilities();
                String capability = "";
                if(cap.size()!=0){
                 capability ="<table style= \"width:90%; border: 1px solid black; border-collapse: collapse;margin-left: auto; margin-right: auto;\">\n" +
                        "  <tr style= \"width:90%; border: 1px solid black; border-collapse: collapse;\">\n" +
                        "    <th style= \"width:50%; border: 1px solid black; border-collapse: collapse;font-size:14px,font-family:Times New Roman;height:50px;text-align: center\">Customer Benefit</th>\n" +
                        "    <th style= \"width:50%; border: 1px solid black; border-collapse: collapse;font-size:14px,font-family:Times New Roman;height:50px;text-align: center\">Supporting Features</th>\n" +
                        "  ";
                for (VCapabilities s:cap){
                    capability = capability+ "<tr style= \"width:90%; border: 1px solid black; border-collapse: collapse;\">\n" +
                            "<td style= \"width:50%; border: 1px solid black; border-collapse: collapse;font-size:12px,font-family:Times New Roman;height:50px;text-align: center\">"+s.getBenifit()+"</td>\n" +
                            "<td style= \"width:50%; border: 1px solid black; border-collapse: collapse;font-size:12px,font-family:Times New Roman;height:50px;text-align: center\">"+s.getFeature()+"</td>\n" +
                            "";
                }
                capability = capability+"</table>";}

                StringBuffer sb6 = new StringBuffer();
                ArrayList<String>  assump= vd.getAssumptions();
                for (String s : assump) {
                    sb6.append("\t-\t"+s+" ");
                    sb6.append(System.getProperty("line.separator"));
                }
                String assumption = sb6.toString();

                ArrayList<VProductFeature>  profeat= vd.getvProductFeat();
                String prorfeature = "";
                if(profeat.size()!=0){
                 prorfeature ="<table style= \"width:90%; border: 1px solid black; border-collapse: collapse;margin-left: auto; margin-right: auto;\">\n" +
                        "  <tr style= \"width:90%; border: 1px solid black; border-collapse: collapse;\">\n" +
                        "    <th style= \"width:20%; border: 1px solid black; border-collapse: collapse;font-size:14px,font-family:Times New Roman;height:50px;text-align: center\">Feature</th>\n" +
                        "    <th style= \"width:20%; border: 1px solid black; border-collapse: collapse;font-size:14px,font-family:Times New Roman;height:50px;text-align: center\">Priority</th>\n" +
                        "    <th style= \"width:20%; border: 1px solid black; border-collapse: collapse;font-size:14px,font-family:Times New Roman;height:50px;text-align: center\">Effort</th>\n" +
                        "    <th style= \"width:20%; border: 1px solid black; border-collapse: collapse;font-size:14px,font-family:Times New Roman;height:50px;text-align: center\">Risk</th>\n" +
                        "    <th style= \"width:20%; border: 1px solid black; border-collapse: collapse;font-size:14px,font-family:Times New Roman;height:50px;text-align: center\">Stability</th>\n" +
                        "  ";
                for (VProductFeature s: profeat){
                    prorfeature = prorfeature+ "<tr style= \"width:90%; border: 1px solid black; border-collapse: collapse;\">\n" +
                            "<td style= \"width:20%; border: 1px solid black; border-collapse: collapse;font-size:12px,font-family:Times New Roman;height:50px;text-align: center\">"+s.getName()+"</td>\n" +
                            "<td style= \"width:20%; border: 1px solid black; border-collapse: collapse;font-size:12px,font-family:Times New Roman;height:50px;text-align: center\">"+s.getPriority()+"</td>\n" +
                            "<td style= \"width:20%; border: 1px solid black; border-collapse: collapse;font-size:12px,font-family:Times New Roman;height:50px;text-align: center\">"+s.getEffort()+"</td>\n" +
                            "<td style= \"width:20%; border: 1px solid black; border-collapse: collapse;font-size:12px,font-family:Times New Roman;height:50px;text-align: center\">"+s.getRisk()+"</td>\n" +
                            "<td style= \"width:20%; border: 1px solid black; border-collapse: collapse;font-size:12px,font-family:Times New Roman;height:50px;text-align: center\">"+s.getStability()+"</td>\n" +
                            "";
                }
                prorfeature = prorfeature+"</table>";}

                String con= vd.getConclusion();

                ArrayList<VPriority> proi = vd.getvPriorities();
                String priority1 = "";
                if(proi.size()!=0){
                 priority1 ="<table style= \"width:90%; border: 1px solid black; border-collapse: collapse;margin-left: auto; margin-right: auto;\">\n" +
                        "<tr style= \"width:90%; border: 1px solid black; border-collapse: collapse;\">\n" +
                        "    <td style= \"width:20%; border: 1px solid black; border-collapse: collapse;font-size:14px,font-family:Times New Roman;height:50px;text-align: center\">"+proi.get(0).getName()+"</td>\n" +
                        "    <td style= \"width:80%; border: 1px solid black; border-collapse: collapse;font-size:14px,font-family:Times New Roman;height:50px;text-align: center\">"+proi.get(0).getDesc()+"</td>\n" +
                        "<tr style= \"width:90%; border: 1px solid black; border-collapse: collapse;\">\n" +
                        "    <td style= \"width:20%; border: 1px solid black; border-collapse: collapse;font-size:14px,font-family:Times New Roman;height:50px;text-align: center\">"+proi.get(1).getName()+"</td>\n" +
                        "    <td style= \"width:80%; border: 1px solid black; border-collapse: collapse;font-size:14px,font-family:Times New Roman;height:50px;text-align: center\">"+proi.get(1).getDesc()+"</td>\n" +
                        "<tr style= \"width:90%; border: 1px solid black; border-collapse: collapse;\">\n" +
                        "    <td style= \"width:20%; border: 1px solid black; border-collapse: collapse;font-size:14px,font-family:Times New Roman;height:50px;text-align: center\">"+proi.get(2).getName()+"</td>\n" +
                        "    <td style= \"width:80%; border: 1px solid black; border-collapse: collapse;font-size:14px,font-family:Times New Roman;height:50px;text-align: center\">"+proi.get(2).getDesc()+"</td>\n" +
                        "" +"</table>";}

                ArrayList<VEffort> eff = vd.getvEffort();
                String effort = "";
                if(eff.size()!=0){
                 effort ="<table style= \"width:90%; border: 1px solid black; border-collapse: collapse;margin-left: auto; margin-right: auto;\">\n" +
                        "<tr style= \"width:90%; border: 1px solid black; border-collapse: collapse;\">\n" +
                        "    <td style= \"width:20%; border: 1px solid black; border-collapse: collapse;font-size:14px,font-family:Times New Roman;height:50px;text-align: center\">"+eff.get(0).getName()+"</td>\n" +
                        "    <td style= \"width:80%; border: 1px solid black; border-collapse: collapse;font-size:14px,font-family:Times New Roman;height:50px;text-align: center\">"+eff.get(0).getDesc()+"</td>\n" +
                        "<tr style= \"width:90%; border: 1px solid black; border-collapse: collapse;\">\n" +
                        "    <td style= \"width:20%; border: 1px solid black; border-collapse: collapse;font-size:14px,font-family:Times New Roman;height:50px;text-align: center\">"+eff.get(1).getName()+"</td>\n" +
                        "    <td style= \"width:80%; border: 1px solid black; border-collapse: collapse;font-size:14px,font-family:Times New Roman;height:50px;text-align: center\">"+eff.get(1).getDesc()+"</td>\n" +
                        "<tr style= \"width:90%; border: 1px solid black; border-collapse: collapse;\">\n" +
                        "    <td style= \"width:20%; border: 1px solid black; border-collapse: collapse;font-size:14px,font-family:Times New Roman;height:50px;text-align: center\">"+eff.get(2).getName()+"</td>\n" +
                        "    <td style= \"width:80%; border: 1px solid black; border-collapse: collapse;font-size:14px,font-family:Times New Roman;height:50px;text-align: center\">"+eff.get(2).getDesc()+"</td>\n" +
                        "" +"</table>";}

                ArrayList<VRisk> ris = vd.getVrisk();
                String risk = "";
                if(ris.size()!=0){
                 risk ="<table style= \"width:90%; border: 1px solid black; border-collapse: collapse;margin-left: auto; margin-right: auto;\">\n" +
                        "<tr style= \"width:90%; border: 1px solid black; border-collapse: collapse;\">\n" +
                        "    <td style= \"width:20%; border: 1px solid black; border-collapse: collapse;font-size:14px,font-family:Times New Roman;height:50px;text-align: center\">"+ris.get(0).getName()+"</td>\n" +
                        "    <td style= \"width:80%; border: 1px solid black; border-collapse: collapse;font-size:14px,font-family:Times New Roman;height:50px;text-align: center\">"+ris.get(0).getDesc()+"</td>\n" +
                        "<tr style= \"width:90%; border: 1px solid black; border-collapse: collapse;\">\n" +
                        "    <td style= \"width:20%; border: 1px solid black; border-collapse: collapse;font-size:14px,font-family:Times New Roman;height:50px;text-align: center\">"+ris.get(1).getName()+"</td>\n" +
                        "    <td style= \"width:80%; border: 1px solid black; border-collapse: collapse;font-size:14px,font-family:Times New Roman;height:50px;text-align: center\">"+ris.get(1).getDesc()+"</td>\n" +
                        "<tr style= \"width:90%; border: 1px solid black; border-collapse: collapse;\">\n" +
                        "    <td style= \"width:0%; border: 1px solid black; border-collapse: collapse;font-size:14px,font-family:Times New Roman;height:50px;text-align: center\">"+ris.get(2).getName()+"</td>\n" +
                        "    <td style= \"width:80%; border: 1px solid black; border-collapse: collapse;font-size:14px,font-family:Times New Roman;height:50px;text-align: center\">"+ris.get(2).getDesc()+"</td>\n" +
                        "" +"</table>";}

                ArrayList<VStability> stab = vd.getVstability();
                String stability = "";
                if(stab.size()!=0){
                    stability ="<table style= \"width:90%; border: 1px solid black; border-collapse: collapse;margin-left: auto; margin-right: auto;\">\n" +
                        "<tr style= \"width:90%; border: 1px solid black; border-collapse: collapse;\">\n" +
                        "    <td style= \"width:20%; border: 1px solid black; border-collapse: collapse;font-size:14px,font-family:Times New Roman;height:50px;text-align: center\">"+stab.get(0).getName()+"</td>\n" +
                        "    <td style= \"width:80%; border: 1px solid black; border-collapse: collapse;font-size:14px,font-family:Times New Roman;height:50px;text-align: center\">"+stab.get(0).getDesc()+"</td>\n" +
                        "<tr style= \"width:90%; border: 1px solid black; border-collapse: collapse;\">\n" +
                        "    <td style= \"width: 20%; border: 1px solid black; border-collapse: collapse;font-size:14px,font-family:Times New Roman;height:50px;text-align: center\">"+stab.get(1).getName()+"</td>\n" +
                        "    <td style= \"width:80%; border: 1px solid black; border-collapse: collapse;font-size:14px,font-family:Times New Roman;height:50px;text-align: center\">"+stab.get(1).getDesc()+"</td>\n" +
                        "<tr style= \"width:90%; border: 1px solid black; border-collapse: collapse;\">\n" +
                        "    <td style= \"width:20%; border: 1px solid black; border-collapse: collapse;font-size:14px,font-family:Times New Roman;height:50px;text-align: center\">"+stab.get(2).getName()+"</td>\n" +
                        "    <td style= \"width:80%; border: 1px solid black; border-collapse: collapse;font-size:14px,font-family:Times New Roman;height:50px;text-align: center\">"+stab.get(2).getDesc()+"</td>\n" +
                        "" +"</table>";}

                ArrayList<String>  ref= vd.getRefrences();
                StringBuffer sb7 = new StringBuffer();
                for (String s : ref) {
                    sb7.append("\t-\t"+s+" ");
                    sb7.append(System.getProperty("line.separator"));
                }
                String reference = sb7.toString();


                String date1 = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());

                String html="<!DOCTYPE html>\n" +
                        "<html>\n" +
                        "<head>\n" +
                        "\t<title></title>\n" +
                        "\t<link rel=\"stylesheet\" type=\"text/css\" href=\"file:android_asset/CreatePdf.css\">\n" +
                        "</head>\n" +
                        "<style>\n"+
                        ".fot {"+
                        "font-size:12px;"+
                        "font-family:Times New Roman;"+
                        "color:gray"+
                        "}"+
                        "</style>\n"+
                        "<body>\n" +

                        "<p><strong>&nbsp;</strong></p>\n"+
                        "<p><strong>&nbsp;</strong></p>\n"+
                        "<p><strong>&nbsp;</strong></p>\n"+
                        "<p><strong>&nbsp;</strong></p>\n"+

                        "<p style=\"text-align:center;\"><img src="+logourl+"\" width=\"200\" height=\"200\" style= \"border-radius: 50%\"></p>\n"+

                        "<p style= \"font-size:26px;font-family:Times New Roman;text-align:center;color:blue;\"><strong>"+name+"</strong></p>\n"+

                        "<p style= \"font-size:22px,font-family:Times New Roman;text-align:center;color:blue;\"><strong>Project Vision</strong></p>\n"+
                        "<p><strong>&nbsp;</strong></p>\n"+
                        "<p style= \" font-size:22px,font-family:Times New Roman;text-align:center;color:blue;\" ><strong>Prepared by:</strong></p>\n"+
                        "<pre style= \" font-size:12px,font-family:Times New Roman;text-align:center\">"+prepareBy+"</pre>\n"+

                        "<pre style=\"font-size:16px;font-family:Times New Roman;text-align:center\"><strong><span style=\"color:blue\">Date: </span></strong>&nbsp;"+date1+"</pre>\n"+

                        "<pre style= \"font-size:16px;font-family:Times New Roman;text-align:center\"><strong><span style=\"color:blue\">Version: </span></strong>&nbsp;"+ver.getVersion()+"</pre>\n"+
                        "<p><strong>&nbsp;</strong></p>\n"+
                        "<p><strong>&nbsp;</strong></p>\n"+
                        "<p><strong>&nbsp;</strong></p>\n"+
                        "<p><strong>&nbsp;</strong></p>\n"+
                        "<p><strong>&nbsp;</strong></p>\n"+
                        "<p><strong>&nbsp;</strong></p>\n"+
                        "<p><strong>&nbsp;</strong></p>\n"+
                        "<p><strong>&nbsp;</strong></p>\n"+
                        "<p><strong>&nbsp;</strong></p>\n"+
                        "<p><strong>&nbsp;</strong></p>\n"+
                        "<p><strong>&nbsp;</strong></p>\n"+


                        "<pre style= \"font-size:14px;font-family:Times New Roman;text-align:center\"><strong>Table of Contents</strong></pre></ref>\n"+

                        "<pre style= \"font-size:12px;font-family:Times New Roman;text-align:center\"><a href=\"#C1\">1.&nbsp;&nbsp;&nbsp; Introduction   .................................................................................................................</a></pre>\n"+

                        "<pre style= \"font-size:12px;font-family:Times New Roman;text-align:center\"><a href=\"#C1.1\">   1.1.&nbsp;&nbsp;&nbsp; Purpose   .......................................................................................................................</a></pre>\n"+

                        "<pre style= \"font-size:12px;font-family:Times New Roman;text-align:center\"><a href=\"#C1.2\">   1.2.&nbsp;&nbsp;&nbsp; Scope   ...........................................................................................................................</a></pre>\n"+

                        "<pre style= \"font-size:12px;font-family:Times New Roman;text-align:center\"><a href=\"#C1.3\">   1.3.&nbsp;&nbsp;&nbsp; Definitions, Acronyms and Abbreviations   ......................................................</a></pre>\n"+

                        "<pre style= \"font-size:12px;font-family:Times New Roman;text-align:center\"><a href=\"#C2\">2.&nbsp;&nbsp;&nbsp; Problem Statement   ....................................................................................................</a></pre>\n"+

                        "<pre style= \"font-size:12px;font-family:Times New Roman;text-align:center\"><a href=\"#C2.1\">   2.1.&nbsp;&nbsp;&nbsp; Problem Decomposition   .......................................................................................</a></pre>\n"+

                        "<pre style= \"font-size:12px;font-family:Times New Roman;text-align:center\"><a href=\"#C3\">3.&nbsp;&nbsp;&nbsp; Positioning   ...................................................................................................................</a></pre>\n"+

                        "<pre style= \"font-size:12px;font-family:Times New Roman;text-align:center\"><a href=\"#C3.1\">   3.1.&nbsp;&nbsp;&nbsp; Business Opportunity   ...........................................................................................</a></pre>\n"+

                        "<pre style= \"font-size:12px;font-family:Times New Roman;text-align:center\"><a href=\"#C3.2\">   3.2.&nbsp;&nbsp;&nbsp; Product Position Statement   .................................................................................</a></pre>\n"+

                        "<pre style= \"font-size:12px;font-family:Times New Roman;text-align:center\"><a href=\"#C4\">4.&nbsp;&nbsp;&nbsp; Stakeholder and User Descriptions   ......................................................................</a></pre>\n"+

                        "<pre style= \"font-size:12px;font-family:Times New Roman;text-align:center\"><a href=\"#C4.1\">     4.1.&nbsp;&nbsp;&nbsp; Stakeholder Summary   ............................................................................................</a></pre>\n"+

                        "<pre style= \"font-size:12px;font-family:Times New Roman;text-align:center\"><a href=\"#C4.2\">     4.2.&nbsp;&nbsp;&nbsp; Key Stakeholder / User Needs   .............................................................................</a></pre>\n"+

                        "<pre style= \"font-size:12px;font-family:Times New Roman;text-align:center\"><a href=\"#C4.3\">     4.3.&nbsp;&nbsp;&nbsp; Alternatives and Competition    .............................................................................</a></pre>\n"+

                        "<pre style= \"font-size:12px;font-family:Times New Roman;text-align:center\"><a href=\"#C5\">5.&nbsp;&nbsp;&nbsp; Product Overview   ........................................................................................................</a></pre>\n"+

                        "<pre style= \"font-size:12px;font-family:Times New Roman;text-align:center\"><a href=\"#C5.1\">     5.1.&nbsp;&nbsp;&nbsp; Product Perspective   .................................................................................................</a></pre>\n"+

                        "<pre style= \"font-size:12px;font-family:Times New Roman;text-align:center\"><a href=\"#C5.2\">     5.2.&nbsp;&nbsp;&nbsp; Summary of Capabilities   .......................................................................................</a></pre>\n"+

                        "<pre style= \"font-size:12px;font-family:Times New Roman;text-align:center\"><a href=\"#C5.3\">     5.3.&nbsp;&nbsp;&nbsp; Assumptions and Dependencies   .........................................................................</a></pre>\n"+

                        "<pre style= \"font-size:12px;font-family:Times New Roman;text-align:center\"><a href=\"#C6\">6.&nbsp;&nbsp;&nbsp; Product Features   ..........................................................................................................</a></pre>\n"+

                        "<pre style= \"font-size:12px;font-family:Times New Roman;text-align:center\"><a href=\"#C7\">7.&nbsp;&nbsp;&nbsp; Conclusion   ....................................................................................................................</a></pre>\n"+

                        "<pre style= \"font-size:12px;font-family:Times New Roman;text-align:center\"><a href=\"#C8\">8.&nbsp;&nbsp;&nbsp; Appendix 1 - Feature Attributes   .............................................................................</a></pre>\n"+

                        "<pre style= \"font-size:12px;font-family:Times New Roman;text-align:center\"><a href=\"#C8.1\">     8.1.&nbsp;&nbsp;&nbsp; Priority   .........................................................................................................................</a></pre>\n"+

                        "<pre style= \"font-size:12px;font-family:Times New Roman;text-align:center\"><a href=\"#C8.2\">     8.2.&nbsp;&nbsp;&nbsp; Effort   ............................................................................................................................</a></pre>\n"+

                        "<pre style= \"font-size:12px;font-family:Times New Roman;text-align:center\"><a href=\"#C8.3\">     8.3.&nbsp;&nbsp;&nbsp; Risk   ...............................................................................................................................</a></pre>\n"+

                        "<pre style= \"font-size:12px;font-family:Times New Roman;text-align:center\"><a href=\"#C8.4\">     8.4.&nbsp;&nbsp;&nbsp; Stability   .......................................................................................................................</a></pre>\n"+

                        "<pre style= \"font-size:12px;font-family:Times New Roman;text-align:center\"><a href=\"#C9\">9.&nbsp;&nbsp;&nbsp; References   ......................................................................................................................</a></pre>\n"+
                        "<p><strong>&nbsp;</strong></p>\n"+
                        " <p><strong>&nbsp;</strong></p>\n"+
                        " <p><strong>&nbsp;</strong></p>\n"+
                        "<p><strong>&nbsp;</strong></p>\n"+
                        " <p><strong>&nbsp;</strong></p>\n"+
                        " <p><strong>&nbsp;</strong></p>\n"+


                        "<pre id=\"C1\"style= \"font-size:14px;font-family:Times New Roman;color:blue;\">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<strong>1.&nbsp;&nbsp;&nbsp; Introduction</strong></pre>\n"+
                        "<pre id=\"C1.1\"style= \"font-size:14px;font-family:Times New Roman;color:blue;\">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<strong>1.1.&nbsp;&nbsp;&nbsp; Purpose</strong></pre>\n"+
                        "<pre style= \"font-size:12px;font-family:Times New Roman;word-wrap:break-word;word-break:break-all;white-space:pre-wrap;line-height:2;margin-left: 30px;\">"+purpose+"</pre>\n"+
                        "<pre id=\"C1.2\"style= \"font-size:14px;font-family:Times New Roman;color:blue;\">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<strong>1.2.&nbsp;&nbsp;&nbsp; Scope</strong></pre>\n"+
                        "<pre style= \"font-size:12px;font-family:Times New Roman;word-wrap:break-word;word-break:break-all;white-space:pre-wrap;line-height:2;margin-left: 30px;\">"+scope+"</pre>\n"+
                        "<pre id=\"C1.3\"style= \"font-size:14px;font-family:Times New Roman;color:blue;\">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<strong>1.3.&nbsp;&nbsp;&nbsp; Definitions, Acronyms and Abbreviations</strong></pre>\n"+
                        "<pre style= \"font-size:12px;font-family:Times New Roman;word-wrap:break-word;word-break:break-all;white-space:pre-wrap;line-height:2;margin-left: 30px;\">"+abbrev+"</pre>\n"+
                        "<pre id=\"C2\"style= \"font-size:14px;font-family:Times New Roman;color:blue;\">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<strong>2.&nbsp;&nbsp;&nbsp; Problem Statement</strong></pre>\n"+
                        "<pre style= \"font-size:12px;font-family:Times New Roman;word-wrap:break-word;word-break:break-all;white-space:pre-wrap;line-height:2;margin-left: 30px;\">"+problemStatment+"</pre>\n"+
                        "<pre id=\"C2.1\"style= \"font-size:14px;font-family:Times New Roman;color:blue;\">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<strong>2.1.&nbsp;&nbsp;&nbsp; Problem Decomposition</strong></pre>\n"+
                        "<pre style= \"font-size:12px;font-family:Times New Roman;word-wrap:break-word;word-break:break-all;white-space:pre-wrap;line-height:2;margin-left: 30px;\">"+problemStatments+"</pre>\n"+
                        "<pre id=\"C3\"style= \"font-size:14px;font-family:Times New Roman;color:blue;\">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<strong>3.&nbsp;&nbsp;&nbsp; Positioning</strong></pre>\n"+
                        "<pre id=\"C3.1\"style= \"font-size:14px;font-family:Times New Roman;color:blue;\">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<strong>3.1.&nbsp;&nbsp;&nbsp; Business Opportunity</strong></pre>\n"+
                        "<pre style= \"font-size:12px;font-family:Times New Roman;word-wrap:break-word;word-break:break-all;white-space:pre-wrap;line-height:2;margin-left: 30px;\">"+buisness+"</pre>\n"+
                        "<pre id=\"C3.2\"style= \"font-size:14px;font-family:Times New Roman;color:blue;\">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<strong>3.2.&nbsp;&nbsp;&nbsp; Product Position Statement</strong></pre>\n"+
                        "<pre style= \"font-size:12px;font-family:Times New Roman;word-wrap:break-word;word-break:break-all;white-space:pre-wrap;line-height:2;margin-left: 30px;\">"+prodpos+"</pre>\n"+
                        "<pre id=\"C4\"style= \"font-size:14px;font-family:Times New Roman;color:blue;\">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<strong>4.&nbsp;&nbsp;&nbsp; Stakeholder and User Descriptions</strong></pre>\n"+
                        "<pre id=\"C4.1\"style= \"font-size:14px;font-family:Times New Roman;color:blue;\">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<strong>4.1.&nbsp;&nbsp;&nbsp; Stakeholder Summary</strong></pre>\n"+
                        "<pre style= \"font-size:12px;font-family:Times New Roman;word-wrap:break-word;word-break:break-all;white-space:pre-wrap;line-height:2;margin-left: 30px;\">"+stakeholder+"</pre>\n"+
                        "<pre id=\"C4.2\"style= \"font-size:14px;font-family:Times New Roman;color:blue;\">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<strong>4.2.&nbsp;&nbsp;&nbsp; User Summary</strong></pre>\n"+
                        "<pre style= \"font-size:12px;font-family:Times New Roman;word-wrap:break-word;word-break:break-all;white-space:pre-wrap;line-height:2;margin-left: 30px;\">"+users+"</pre>\n"+
                        "<pre id=\"C4.3\"style= \"font-size:14px;font-family:Times New Roman;color:blue;\">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<strong>4.3.&nbsp;&nbsp;&nbsp; Key Stakeholder / User Needs</strong></pre>\n"+
                        "<pre style= \"font-size:12px;font-family:Times New Roman;word-wrap:break-word;word-break:break-all;white-space:pre-wrap;line-height:2;margin-left: 30px;\">"+keyNeed+"</pre>\n"+
                        "<pre id=\"C4.4\"style= \"font-size:14px;font-family:Times New Roman;color:blue;\">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<strong>4.4.&nbsp;&nbsp;&nbsp; Alternatives and Competition</strong></pre>\n"+
                        "<pre style= \"font-size:12px;font-family:Times New Roman;word-wrap:break-word;word-break:break-all;white-space:pre-wrap;line-height:2;margin-left: 30px;\">"+alternative+"</pre>\n"+
                        "<pre id=\"C5\"style= \"font-size:14px;font-family:Times New Roman;color:blue;\">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<strong>5.&nbsp;&nbsp;&nbsp; Product Overview</strong></pre>\n"+
                        "<pre id=\"C5.1\"style= \"font-size:14px;font-family:Times New Roman;color:blue;\">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<strong>5.1.&nbsp;&nbsp;&nbsp; Product Perspective</strong></pre>\n"+
                        "<pre style= \"font-size:12px;font-family:Times New Roman;word-wrap:break-word;word-break:break-all;white-space:pre-wrap;line-height:2;margin-left: 30px;\">"+prodpres+"</pre>\n"+
                        "<pre id=\"C5.2\"style= \"font-size:14px;font-family:Times New Roman;color:blue;\">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<strong>5.2.&nbsp;&nbsp;&nbsp; Summary of Capabilities</strong></pre>\n"+
                        "<pre style= \"font-size:12px;font-family:Times New Roman;word-wrap:break-word;word-break:break-all;white-space:pre-wrap;line-height:2;margin-left: 30px;\">"+capability+"</pre>\n"+
                        "<pre id=\"C5.3\"style= \"font-size:14px;font-family:Times New Roman;color:blue;\">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<strong>5.3.&nbsp;&nbsp;&nbsp; Assumptions and Dependencies</strong></pre>\n"+
                        "<pre style= \"font-size:12px;font-family:Times New Roman;word-wrap:break-word;word-break:break-all;white-space:pre-wrap;line-height:2;margin-left: 30px;\">"+assumption+"</pre>\n"+
                        "<pre id=\"C6\"style= \"font-size:14px;font-family:Times New Roman;color:blue;\">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<strong>6.&nbsp;&nbsp;&nbsp; Product Features</strong></pre>\n"+
                        "<pre style= \"font-size:12px;font-family:Times New Roman;word-wrap:break-word;word-break:break-all;white-space:pre-wrap;line-height:2;margin-left: 30px;\">"+prorfeature+"</pre>\n"+
                        "<pre id=\"C7\"style= \"font-size:14px;font-family:Times New Roman;color:blue;\">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<strong>7.&nbsp;&nbsp;&nbsp; Conclusion</strong></pre>\n"+
                        "<pre style= \"font-size:12px;font-family:Times New Roman;word-wrap:break-word;word-break:break-all;white-space:pre-wrap;line-height:2;margin-left: 30px;\">"+con+"</pre>\n"+
                        "<pre id=\"C8\"style= \"font-size:14px;font-family:Times New Roman;color:blue;\">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<strong>8.&nbsp;&nbsp;&nbsp; Appendix 1 - Feature Attributes</strong></pre>\n"+
                        "<pre id=\"C8.1\"style= \"font-size:14px;font-family:Times New Roman;color:blue;\">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<strong>8.1.&nbsp;&nbsp;&nbsp; Priority</strong></pre>\n"+
                        "<pre style= \"font-size:12px;font-family:Times New Roman;word-wrap:break-word;word-break:break-all;white-space:pre-wrap;line-height:2;margin-left: 30px;\">"+priority1+"</pre>\n"+
                        "<pre id=\"C8.2\"style= \"font-size:14px;font-family:Times New Roman;color:blue;\">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<strong>8.2.&nbsp;&nbsp;&nbsp; Effort</strong></pre>\n"+
                        "<pre style= \"font-size:12px;font-family:Times New Roman;word-wrap:break-word;word-break:break-all;white-space:pre-wrap;line-height:2;margin-left: 30px;\">"+effort+"</pre>\n"+
                        "<pre id=\"C8.3\"style= \"font-size:14px;font-family:Times New Roman;color:blue;\">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<strong>8.3.&nbsp;&nbsp;&nbsp; Risk</strong></pre>\n"+
                        "<pre style= \"font-size:12px;font-family:Times New Roman;word-wrap:break-word;word-break:break-all;white-space:pre-wrap;line-height:2;margin-left: 30px;\">"+risk+"</pre>\n"+
                        "<pre id=\"C8.4\"style= \"font-size:14px;font-family:Times New Roman;color:blue;\">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<strong>8.4.&nbsp;&nbsp;&nbsp; Stability</strong></pre>\n"+
                        "<pre style= \"font-size:12px;font-family:Times New Roman;word-wrap:break-word;word-break:break-all;white-space:pre-wrap;line-height:2;margin-left: 30px;\">"+stability+"</pre>\n"+
                        "<pre id=\"CC9\"style= \"font-size:14px;font-family:Times New Roman;color:blue;\"><strong>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;9.&nbsp;&nbsp;&nbsp; References</strong></pre>\n"+
                        "<pre style= \"font-size:12px;font-family:Times New Roman;word-wrap:break-word;word-break:break-all;white-space:pre-wrap;line-height:2;margin-left: 30px;\">"+reference+"</pre>\n"+
                        "</body>\n" +
                        "</html>";
                wview.loadDataWithBaseURL(null,html,"text/html","utf-8",null);
                download.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        bitmap = LoadBitmap( toPDF , toPDF.getWidth(), toPDF.getHeight());
                        //createPdf();
                        CreatePdf(wview);
                    }});
               done.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(VisionPdf.this, ProjectDetails.class);
                        i.putExtra("Version", ver);
                        i.putExtra("Project", pro);
                        VisionPdf.this.startActivity(i);
                    }});




            }
        });

    }
    private Bitmap LoadBitmap(View v, int width, int height) {
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        v.draw(canvas);
        return bitmap;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void CreatePdf(View view){
        Context context=VisionPdf.this;
        PrintManager printManager=(PrintManager)VisionPdf.this.getSystemService(context.PRINT_SERVICE);
        PrintDocumentAdapter adapter=null;
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.KITKAT){
            adapter=wview.createPrintDocumentAdapter();
        }
        String JobName=getString(R.string.app_name) +"Document";
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.KITKAT){
            PrintJob printJob=printManager.print(JobName,adapter,new PrintAttributes.Builder().build());
        }
        openPdf();
    }

    private void openPdf() {
        File file = new File(vd.getDname()+".pdf");
        if (file.exists()) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            Uri uri = Uri.fromFile(file);
            intent.setDataAndType(uri, "application/pdf");
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            try {
                startActivity(intent);
            } catch (ActivityNotFoundException e) {
                Toast.makeText(this, "No Application for pdf view", Toast.LENGTH_SHORT).show();
            }
        }
    }
    private StringBuffer fromDB1(ArrayList<String> s){
        StringBuffer sb = new StringBuffer();
        for(int i=0;i>s.size();i++) {
            sb.append("\t\t\t"+s.get(i)+" ");
            sb.append(System.getProperty("line.separator"));
        }
        return sb;
    }
}
