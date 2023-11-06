package com.example.docanddeal;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintJob;
import android.print.PrintManager;
import android.print.pdf.PrintedPdfDocument;
import android.view.Menu;
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
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class SRSPdf extends AppCompatActivity {
    String docID;
    FirebaseFirestore fs;
    FirebaseAuth fAuth;
    DocumentReference dref;
    ImageView done,download;
    private Bitmap bitmap;
    RelativeLayout toPDF ;
    SRSDocument sd;
    WebView wview;
    String logourl;
    ProjectC pro;
    Version ver;
    Uri uri;
    OutputStream os;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_srspdf);

        Bundle b = getIntent().getExtras();
        docID = b.get("docID").toString();
        logourl = b.get("logo").toString();
        ver = (Version) b.get("ver");
        pro = (ProjectC) b.getSerializable("Project");
        wview = findViewById(R.id.SRSVIEW);
        fAuth = FirebaseAuth.getInstance();
        fs = FirebaseFirestore.getInstance();
        dref = fs.collection("srsDoc").document(docID);
        download = findViewById(R.id.SRSDownload);
        done = findViewById(R.id.SRSFinish);
        toPDF = findViewById(R.id.SRSPDF);

        dref.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                sd = documentSnapshot.toObject(SRSDocument.class);
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
                else {
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
                String purpose = "\t\tThe results of the requirements elicitation and the analysis activities are documented in this Software Requirements Specification (SRS) document. This document completely describes the system in terms of functional and nonfunctional requirements.";



                StringBuffer sb1 = new StringBuffer();

                ArrayList<Abbrev> abb = sd.getExplination();
                for (Abbrev s : abb) {
                    sb1.append("\t-\t"+s.getName()+": "+s.getExpi());
                    sb1.append(System.getProperty("line.separator"));
                }
                String abbrev = sb1.toString();

                String name = sd.getDname();

                ArrayList<NonFunc>  nonfun= sd.getNfrs();
                StringBuffer sb6 = new StringBuffer();
                for (NonFunc s : nonfun) {
                    sb6.append("\t-\t"+s.getText()+" ");
                    sb6.append(System.getProperty("line.separator"));
                }
                String nonfunction = sb6.toString();

                ArrayList<Reference>  ref= sd.getRefs();
                StringBuffer sb7 = new StringBuffer();
                for (Reference s : ref) {
                    sb7.append("\t-\t"+s.getText()+" ");
                    sb7.append(System.getProperty("line.separator"));
                }
                String reference = sb7.toString();

                ArrayList<UseCase>  ucs = sd.getUcs();
                String usecases ="";
                String STDs ="";
                String seqDiagrams ="";
                String prototypes ="";
                String ucn="";
                for (UseCase u:ucs){
                    String ID = "U"+u.getUcnum();
                    ucn = ucn+ "<pre style= \"font-size:12px;font-family:Times New Roman;text-align:center\"><a href= \"#"+ID+"\">UC"+u.getUcnum()+":("+u.getName()+")</a></pre>\n";
                }

                for (UseCase u : ucs){
                    usecases = usecases + "<pre id=\"T"+u.getUcnum()+"\"style= \"font-size:12px;font-family:Times New Roman;word-wrap:break-word;word-break:break-all;white-space:pre-wrap;line-height:2;margin-left: 30px;\"><a href= \"#USTD"+u.getUcnum()+"\">UC" + u.getUcnum() + ":(" + u.getName() + ")</a></pre>\n" ;
                    if(u.getStdscreen()!=null) {
                        STDs = STDs + "<pre id=\"USTD"+u.getUcnum()+"\"style= \"font-size:12px;font-family:Times New Roman;word-wrap:break-word;word-break:break-all;white-space:pre-wrap;line-height:2;margin-left: 30px;\"><a href= \"#T"+u.getUcnum()+"\">UC" + u.getUcnum() + ":(" + u.getName() + ")</a></pre>\n" +
                                "<pre style= \"font-size:12px;font-family:Times New Roman;word-wrap:break-word;word-break:break-all;white-space:pre-wrap;line-height:2;margin-left: 30px;\"><img src=" + u.getStdscreen().getImagepath() + "\"  style= \"border-radius: 0%\"></pre>\n";
                    }
                    if(u.getMainflowseq()!=null) {
                        seqDiagrams = seqDiagrams + "<pre style= \"font-size:12px;font-family:Times New Roman;word-wrap:break-word;word-break:break-all;white-space:pre-wrap;line-height:2;margin-left: 30px;\">UC" + u.getUcnum() + ":(" + u.getName() + ")</pre>\n" +
                                "<pre id=\"SEQ"+u.getUcnum()+"\" style= \"font-size:12px;font-family:Times New Roman;word-wrap:break-word;word-break:break-all;white-space:pre-wrap;line-height:2;margin-left: 30px;\"><a href=\"#Mainflow"+ u.getUcnum()+"\">Main Flow Sequence Diagram</a></pre>\n" +
                                "<pre style= \"font-size:12px;font-family:Times New Roman;word-wrap:break-word;word-break:break-all;white-space:pre-wrap;line-height:2;margin-left: 30px;\"><img src=" + u.getMainflowseq().getImagepath() + "\"  style= \"border-radius: 0%\"></pre>\n";

                        ArrayList<Alternative> a =u.getAlternatives();
                        if(a.size()!=0){
                            seqDiagrams = seqDiagrams + "<pre style= \"font-size:12px;font-family:Times New Roman;word-wrap:break-word;word-break:break-all;white-space:pre-wrap;line-height:2;margin-left: 30px;\">Alternative Sequence diagram</pre>\n";
                            for (Alternative al:a){
                                if(al.getAltseq()!=null){
                                    seqDiagrams = seqDiagrams + "<pre id=\"SEQ"+u.getUcnum()+al.getAltnum()+"\" style= \"font-size:12px;font-family:Times New Roman;word-wrap:break-word;word-break:break-all;white-space:pre-wrap;line-height:2;margin-left: 30px;\"><a href=\"#Mainflow"+ u.getUcnum() + al.getAltnum()+"\">A"+al.getAltnum()+"("+al.getName()+")</a></pre>\n" +
                                            "<pre style= \"font-size:12px;font-family:Times New Roman;word-wrap:break-word;word-break:break-all;white-space:pre-wrap;line-height:2;margin-left: 30px;\"><img src=" + u.getMainflowseq().getImagepath() + "\"  style= \"border-radius: 0%\"></pre>\n";
                                }
                            }
                        }
                        seqDiagrams = seqDiagrams + "<pre id=\"SEQ"+u.getUcnum()+"\" style= \"font-size:12px;font-family:Times New Roman;word-wrap:break-word;word-break:break-all;white-space:pre-wrap;line-height:2;margin-left: 30px;\"><a href=\"#Mainflow"+u.getUcnum()+"\">UC" + u.getUcnum() + ":(" + u.getName() + ") </a></pre>\n" +
                                "<pre style= \"font-size:12px;font-family:Times New Roman;word-wrap:break-word;word-break:break-all;white-space:pre-wrap;line-height:2;margin-left: 30px;\"><img src=" + u.getMainflowseq().getImagepath() + "\"  style= \"border-radius: 0%\"></pre>\n";
                    }
                    ArrayList<Actor> act = u.getActors();
                    String actors = "";
                    for(Actor a: act){
                        actors = actors + a.getName() + ", ";
                    }
                    ArrayList<Relationship> rela = u.getRelationships();
                    String relation = "";
                    for(Relationship r: rela){
                        String target = r.getTarget();
                        String source = r.getSource();
                        String targetName ="";
                        String sourceName="";
                        String type ="";
                        for (UseCase uc1 : ucs) {
                            if(uc1.getId().equals(target)) {
                                targetName = uc1.getName();
                            }
                            if (uc1.getId().equals(source)) {
                                sourceName = uc1.getName();
                            }
                        }
                        if(target.equals(u.getId())){
                            if(r.getType().equals("extend")) {
                                type = "Extended by " + sourceName + ".";
                                relation = relation + type +"\n";
                            }
                            else if(r.getType().equals("include")) {
                                type = "Included by " + sourceName + ".";
                                relation = relation + type +"\n";
                            }
                            else
                                type="";
                        }else{
                            if(r.getType().equals("extend")) {
                                type = "Extends " + targetName + ".";
                                relation = relation + type +"\n";
                            }
                            else if(r.getType().equals("include")) {
                                type = "Include " + targetName + ".";
                                relation = relation + type +"\n";
                            }
                            else {

                            }
                        }
                    }

                    String ID = "U"+u.getUcnum();

                    usecases = usecases +
                            "<table id=\""+ID+"\"style= \"width:90%; border: 1px solid black; border-collapse: collapse;margin-left: auto; margin-right: auto;\">\n" +
                            "    <tr style= \"width:90%; border: 1px solid black; border-collapse: collapse;\">\n" +
                            "    <td style= \"width:30%; border: 1px solid black; border-collapse: collapse;font-size:14px,font-family:Times New Roman;height:50px;text-align: center\">Use Case Number</td>\n" +
                            "    <td style= \"width:70%; border: 1px solid black; border-collapse: collapse;font-size:14px,font-family:Times New Roman;height:50px;text-align: center\">"+u.getUcnum()+"</td>\n" +
                            "    <tr style= \"width:90%; border: 1px solid black; border-collapse: collapse;\">\n" +
                            "    <td style= \"width:30%; border: 1px solid black; border-collapse: collapse;font-size:14px,font-family:Times New Roman;height:50px;text-align: center\">Use Case Name</td>\n" +
                            "    <td style= \"width:70%; border: 1px solid black; border-collapse: collapse;font-size:14px,font-family:Times New Roman;height:50px;text-align: center\">"+u.getName()+"</td>\n" +
                            "    <tr style= \"width:90%; border: 1px solid black; border-collapse: collapse;\">\n" +
                            "    <td style= \"width:30%; border: 1px solid black; border-collapse: collapse;font-size:14px,font-family:Times New Roman;height:50px;text-align: center\">Author/Source</td>\n" +
                            "    <td style= \"width:70%; border: 1px solid black; border-collapse: collapse;font-size:14px,font-family:Times New Roman;height:50px;text-align: center\">"+u.getAuthor()+"</td>\n" +
                            "    <tr style= \"width:90%; border: 1px solid black; border-collapse: collapse;\">\n" +
                            "    <td style= \"width:30%; border: 1px solid black; border-collapse: collapse;font-size:14px,font-family:Times New Roman;height:50px;text-align: center\">Date of Creation</td>\n" +
                            "    <td style= \"width:70%; border: 1px solid black; border-collapse: collapse;font-size:14px,font-family:Times New Roman;height:50px;text-align: center\">"+u.getDate()+"</td>\n" +
                            "    <tr style= \"width:90%; border: 1px solid black; border-collapse: collapse;\">\n" +
                            "    <td style= \"width:30%; border: 1px solid black; border-collapse: collapse;font-size:14px,font-family:Times New Roman;height:50px;text-align: center\">Precondition(s)</td>\n" +
                            "    <td style= \"width:70%; border: 1px solid black; border-collapse: collapse;font-size:14px,font-family:Times New Roman;height:50px;text-align: center\">"+u.getPrecond()+"</td>\n" +
                            "    <tr style= \"width:90%; border: 1px solid black; border-collapse: collapse;\">\n" +
                            "    <td style= \"width:30%; border: 1px solid black; border-collapse: collapse;font-size:14px,font-family:Times New Roman;height:50px;text-align: center\">Successful Post Condition</td>\n" +
                            "    <td style= \"width:70%; border: 1px solid black; border-collapse: collapse;font-size:14px,font-family:Times New Roman;height:50px;text-align: center\">"+u.getPostcond()+"</td>\n" +
                            "    <tr style= \"width:90%; border: 1px solid black; border-collapse: collapse;\">\n" +
                            "    <td style= \"width:30%; border: 1px solid black; border-collapse: collapse;font-size:14px,font-family:Times New Roman;height:50px;text-align: center\">Actors</td>\n" +
                            "    <td style= \"width:70%; border: 1px solid black; border-collapse: collapse;font-size:14px,font-family:Times New Roman;height:50px;text-align: center\">"+actors+"</td>\n" +
                            "    <tr style= \"width:90%; border: 1px solid black; border-collapse: collapse;\">\n" +
                            "    <td style= \"width:30%; border: 1px solid black; border-collapse: collapse;font-size:14px,font-family:Times New Roman;height:50px;text-align: center\">Priority</td>\n" +
                            "    <td style= \"width:70%; border: 1px solid black; border-collapse: collapse;font-size:14px,font-family:Times New Roman;height:50px;text-align: center\">"+u.getPriority()+"</td>\n" +
                            "    <tr style= \"width:90%; border: 1px solid black; border-collapse: collapse;\">\n" +
                            "    <td style= \"width:30%; border: 1px solid black; border-collapse: collapse;font-size:14px,font-family:Times New Roman;height:50px;text-align: center\">Related Use Cases</td>\n" +
                            "    <td style= \"width:70%; border: 1px solid black; border-collapse: collapse;font-size:14px,font-family:Times New Roman;height:50px;text-align: center\">"+relation+"</td></table>" ;

                    ArrayList<Row> main = u.getMainflowrows();

                    if(main.size()!=0){
                        usecases = usecases +  "<table style= \"width:90%; border: 1px solid black; border-collapse: collapse;margin-left: auto; margin-right: auto;\">" +
                                "   <tr style= \"width:90%; border: 1px solid black; border-collapse: collapse;\">" +
                                "   <td style= \"width:100%; border: 1px solid black; border-collapse: collapse;font-size:14px,font-family:Times New Roman;height:50px;text-align: center\">Flow of Events</td></tr>" +
                                "   <tr style= \"width:90%; border: 1px solid black; border-collapse: collapse;\">" +
                                "   <td id = \"Mainflow"+u.getUcnum()+"\" style= \"width:100%; border: 1px solid black; border-collapse: collapse;font-size:14px,font-family:Times New Roman;height:50px;text-align: center\"><a href=\"#SEQ"+u.getUcnum()+"\">Main Flow</a></td></tr>"+
                                "   <table style= \"width:90%; border: 1px solid black; border-collapse: collapse;margin-left: auto; margin-right: auto;>"+"<tr style= \"width:90%; border: 1px solid black; border-collapse: collapse;\">" +
                                "   <td style= \"width:50%; border: 1px solid black; border-collapse: collapse;font-size:14px,font-family:Times New Roman;height:50px;text-align: center\">User Action</td>" +
                                "   <td style= \"width:50%; border: 1px solid black; border-collapse: collapse;font-size:14px,font-family:Times New Roman;height:50px;text-align: center\">System Response</td></table>" ;


                        String mainf = "<table style= \"width:90%; border: 1px solid black; border-collapse: collapse;margin-left: auto; margin-right: auto;\">" ;

                        for(Row m : main){
                            String mID = m.getUcnum()+""+m.getNum();
                            mainf = mainf +
                                    "   <tr style= \"width:90%; border: 1px solid black; border-collapse: collapse;\">\n" +
                                    "   <td id=\"UA"+mID+"\" style= \"width:10%; border: 1px solid black; border-collapse: collapse;font-size:14px,font-family:Times New Roman;height:50px;text-align: center\"><a href=\"#UAP"+mID+"\">UA"+m.getNum()+"</a></td>\n" +
                                    "   <td style= \"width:40%; border: 1px solid black; border-collapse: collapse;font-size:14px,font-family:Times New Roman;height:50px;text-align: center\">"+m.getUa()+"</td>\n" +
                                    "   <td id=\"SR"+mID+"\" style= \"width:10%; border: 1px solid black; border-collapse: collapse;font-size:14px,font-family:Times New Roman;height:50px;text-align: center\"><a href=\"#SRP"+mID+"\">SR"+m.getNum()+"</a></td>\n" +
                                    "   <td style= \"width:40%; border: 1px solid black; border-collapse: collapse;font-size:14px,font-family:Times New Roman;height:50px;text-align: center\">"+m.getSr()+"</td>\n" ;

                            if(m.getUascreen()!=null||m.getSrscreen()!=null){
                                prototypes = prototypes + "<pre style= \"font-size:12px;font-family:Times New Roman;word-wrap:break-word;word-break:break-all;white-space:pre-wrap;line-height:2;margin-left: 30px;\">UC"+u.getUcnum()+":("+u.getName()+")</pre>\n"+
                                        "<pre id=\"UAP"+mID+"\"style= \"font-size:12px;font-family:Times New Roman;word-wrap:break-word;word-break:break-all;white-space:pre-wrap;line-height:2;margin-left: 30px;\"><a href=\"#UA"+mID+"\">UA"+m.getNum()+"</a></pre>\n"+
                                        "<pre style= \"font-size:12px;font-family:Times New Roman;word-wrap:break-word;word-break:break-all;white-space:pre-wrap;line-height:2;margin-left: 30px;\"><img src="+m.getUascreen().getImagepath()+"\"  style= \"border-radius: 0%\"></pre>\n"+
                                        "<pre id=\"SRP"+mID+"\"style= \"font-size:12px;font-family:Times New Roman;word-wrap:break-word;word-break:break-all;white-space:pre-wrap;line-height:2;margin-left: 30px;\"><a href=\"#SR"+mID+"\">SR"+m.getNum()+"</a></pre>\n"+
                                        "<pre style= \"font-size:12px;font-family:Times New Roman;word-wrap:break-word;word-break:break-all;white-space:pre-wrap;line-height:2;margin-left: 30px;\"><img src="+m.getSrscreen().getImagepath()+"\"  style= \"border-radius: 0%\"></pre>\n";
                            }
                        }
                        mainf = mainf + "</table>";
                        usecases = usecases + mainf;

                    }


                    ArrayList<Alternative> alter = u.getAlternatives();
                    if(alter.size()!=0){
                        String ALTERS = "";
                        for(Alternative alt : alter){
                            ALTERS = ALTERS + "<table style= \"width:90%; border: 1px solid black; border-collapse: collapse;margin-left: auto; margin-right: auto;>"+"<tr style= \"width:90%; border: 1px solid black; border-collapse: collapse;\">" +
                                    "    <td id = \"Mainflow"+u.getUcnum()+alt.getAltnum()+"\" style= \"width:90%; border: 1px solid black; border-collapse: collapse;font-size:14px,font-family:Times New Roman;height:50px;text-align: center\"><a href=\"#SEQ"+u.getUcnum()+alt.getAltnum()+"\">Alternative"+alt.getAltnum()+": "+alt.getAltname()+"</a></td></table>" ;

                            ALTERS = ALTERS + "<table  style= \"width:90%; border: 1px solid black; border-collapse: collapse;margin-left: auto; margin-right: auto;>"
                                    +"<tr style= \"width:90%; border: 1px solid black; border-collapse: collapse;\">" +
                                    "    <td style= \"width:50%; border: 1px solid black; border-collapse: collapse;font-size:14px,font-family:Times New Roman;height:50px;text-align: center\">User Action</td>" +
                                    "    <td style= \"width:50%; border: 1px solid black; border-collapse: collapse;font-size:14px,font-family:Times New Roman;height:50px;text-align: center\">System Response</td></table>" ;

                            ArrayList<Row> altrow = alt.getAltrows();
                            for(Row m : altrow){
                                String mID = m.getUcnum()+""+m.getNum();
                                if(m.getUascreen()!=null||m.getSrscreen()!=null) {
                                    prototypes = prototypes + "<pre style= \"font-size:12px;font-family:Times New Roman;word-wrap:break-word;word-break:break-all;white-space:pre-wrap;line-height:2;margin-left: 30px;\">UC"+u.getUcnum()+":("+u.getName()+")</pre>\n"+
                                            "<pre id=\"UAAP"+mID+"\"style= \"font-size:12px;font-family:Times New Roman;word-wrap:break-word;word-break:break-all;white-space:pre-wrap;line-height:2;margin-left: 30px;\"><a href=\"#UAA"+mID+"\">A"+alt.getAltnum()+".UA"+m.getNum()+"</a></pre>\n"+
                                            "<pre style= \"font-size:12px;font-family:Times New Roman;word-wrap:break-word;word-break:break-all;white-space:pre-wrap;line-height:2;margin-left: 30px;\"><img src="+m.getUascreen().getImagepath()+"\"  style= \"border-radius: 0%\"></pre>\n"+
                                            "<pre id=\"SRAP"+mID+"\"style= \"font-size:12px;font-family:Times New Roman;word-wrap:break-word;word-break:break-all;white-space:pre-wrap;line-height:2;margin-left: 30px;\"><a href=\"#SRA"+mID+"\">A"+alt.getAltnum()+".SR"+m.getNum()+"</a></pre>\n"+
                                            "<pre style= \"font-size:12px;font-family:Times New Roman;word-wrap:break-word;word-break:break-all;white-space:pre-wrap;line-height:2;margin-left: 30px;\"><img src="+m.getSrscreen().getImagepath()+"\"  style= \"border-radius: 0%\"></pre>\n";
                                }
                                ALTERS = ALTERS + "<table style= \"width:90%; border: 1px solid black; border-collapse: collapse;margin-left: auto; margin-right: auto;\">"+"<tr style= \"width:90%; border: 1px solid black; border-collapse: collapse;\">" +
                                        "    <td id=\"UAA"+mID+"\" style= \"width:10%; border: 1px solid black; border-collapse: collapse;font-size:14px,font-family:Times New Roman;height:50px;text-align: center\"><a href=\"#UAAP"+mID+"\">A"+alt.getAltnum()+".UA"+m.getNum()+"</a></td>\n" +
                                        "    <td  style= \"width:40%; border: 1px solid black; border-collapse: collapse;font-size:14px,font-family:Times New Roman;height:50px;text-align: center\">"+m.getUa()+"</td>\n" +
                                        "    <td id=\"SRA"+mID+"\" style= \"width:10%; border: 1px solid black; border-collapse: collapse;font-size:14px,font-family:Times New Roman;height:50px;text-align: center\"><a href=\"#SRAP"+mID+"\">A"+alt.getAltnum()+".SR"+m.getNum()+"</a></td>\n" +
                                        "    <td style= \"width:40%; border: 1px solid black; border-collapse: collapse;font-size:14px,font-family:Times New Roman;height:50px;text-align: center\">"+m.getSr()+"</td></table>\n" ;
                            }
                            usecases = usecases +ALTERS+"</table>";}


                    }

                    usecases = usecases + "\n\n";
                }
                System.out.println("pro"+prototypes);





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
                        "<p><strong>&nbsp;</strong></p>\n"+
                        "<p><strong>&nbsp;</strong></p>\n"+
                        "<p><strong>&nbsp;</strong></p>\n"+
                        "<p><strong>&nbsp;</strong></p>\n"+
                        "<p><strong>&nbsp;</strong></p>\n"+

                        "<p style=\"text-align:center;\"><img src="+logourl+"\" width=\"200\" height=\"200\" style= \"border-radius: 50%\"></p>\n"+

                        "<p style= \"font-size:26px;font-family:Times New Roman;text-align:center;color:blue;\"><strong>"+name+"</strong></p>\n"+

                        "<p style= \"font-size:22px,font-family:Times New Roman;text-align:center;color:blue;\"><strong>Project SRS</strong></p>\n"+
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



                        "<pre style= \"font-size:14px;font-family:Times New Roman;text-align:center\"><strong>Table of Contents</strong></pre>\n"+

                        "<pre style= \"font-size:12px;font-family:Times New Roman;text-align:center\"><a href=\"#C1\">1.&nbsp;&nbsp;&nbsp; Introduction   ..................................................................................................................</a></pre>\n"+

                        "<pre style= \"font-size:12px;font-family:Times New Roman;text-align:center\"><a href=\"#C1.1\">   1.1.&nbsp;&nbsp;&nbsp; Purpose   .....................................................................................................................</a></pre>\n"+

                        "<pre style= \"font-size:12px;font-family:Times New Roman;text-align:center\"><a href=\"#C1.2\">   1.2.&nbsp;&nbsp;&nbsp; Definitions, Acronyms and Abbreviations   ........................................................</a></pre>\n"+

                        "<pre style= \"font-size:12px;font-family:Times New Roman;text-align:center\"><a href=\"#C2\">2.&nbsp;&nbsp;&nbsp; Use Case Modeling   .....................................................................................................</a></pre>\n"+

                        "<pre style= \"font-size:12px;font-family:Times New Roman;text-align:center\"><a href=\"#C2.1\">   2.1.&nbsp;&nbsp;&nbsp; Use Case Diagram   .................................................................................................</a></pre>\n"+

                        "<pre style= \"font-size:12px;font-family:Times New Roman;text-align:center\"><a href=\"#C2.2\">   2.2.&nbsp;&nbsp;&nbsp; Use Cases Descriptions (Scenarios)   ..................................................................</a></pre>\n"+ ucn +

                        "<pre style= \"font-size:12px;font-family:Times New Roman;text-align:center\"><a href=\"#C3\">3.&nbsp;&nbsp;&nbsp; State Transition Diagrams (STDs)   ...........................................................................</a></pre>\n"+

                        "<pre style= \"font-size:12px;font-family:Times New Roman;text-align:center\"><a href=\"#C4\">4.&nbsp;&nbsp;&nbsp; Sequence Diagrams   ....................................................................................................</a></pre>\n"+

                        "<pre style= \"font-size:12px;font-family:Times New Roman;text-align:center\"><a href=\"#C5\">4.&nbsp;&nbsp;&nbsp; Non-functional Requirements   ..................................................................................</a></pre>\n"+

                        "<pre style= \"font-size:12px;font-family:Times New Roman;text-align:center\"><a href=\"#C6\">4.&nbsp;&nbsp;&nbsp; Prototype (Screens Layout)   ......................................................................................</a></pre>\n"+

                        "<pre style= \"font-size:12px;font-family:Times New Roman;text-align:center\"><a href=\"#C7\">4.&nbsp;&nbsp;&nbsp; References   ....................................................................................................................</a></pre>\n"+
                        "<p><strong>&nbsp;</strong></p>\n"+
                        " <p><strong>&nbsp;</strong></p>\n"+
                        " <p><strong>&nbsp;</strong></p>\n"+
                        "<p><strong>&nbsp;</strong></p>\n"+
                        " <p><strong>&nbsp;</strong></p>\n"+
                        " <p><strong>&nbsp;</strong></p>\n"+
                        " <p><strong>&nbsp;</strong></p>\n"+
                        "<p><strong>&nbsp;</strong></p>\n"+
                        " <p><strong>&nbsp;</strong></p>\n"+
                        " <p><strong>&nbsp;</strong></p>\n"+
                        " <p><strong>&nbsp;</strong></p>\n"+
                        "<p><strong>&nbsp;</strong></p>\n"+
                        " <p><strong>&nbsp;</strong></p>\n"+
                        " <p><strong>&nbsp;</strong></p>\n"+
                        " <p><strong>&nbsp;</strong></p>\n"+
                        " <p><strong>&nbsp;</strong></p>\n"+
                        " <p><strong>&nbsp;</strong></p>\n"+

                        "<pre id=\"C1\"style= \"font-size:14px;font-family:Times New Roman;color:blue;\">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<strong>1.&nbsp;&nbsp;&nbsp; Introduction</strong></pre>\n"+
                        "<pre id=\"C1.1\"style= \"font-size:14px;font-family:Times New Roman;color:blue;\">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<strong>1.1.&nbsp;&nbsp;&nbsp; Purpose</strong></pre>\n"+
                        "<pre style= \"font-size:12px;font-family:Times New Roman;word-wrap:break-word;word-break:break-all;white-space:pre-wrap;line-height:2;margin-left: 30px;\">"+purpose+"</pre>\n"+
                        "<pre id=\"C1.2\"style= \"font-size:14px;font-family:Times New Roman;color:blue;\">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<strong>1.2.&nbsp;&nbsp;&nbsp; Definitions, Acronyms and Abbreviations</strong></pre>\n"+
                        "<pre style= \"font-size:12px;font-family:Times New Roman;word-wrap:break-word;word-break:break-all;white-space:pre-wrap;line-height:2;margin-left: 30px;\">"+abbrev+"</pre>\n"+
                        "<pre id=\"C2\"style= \"font-size:14px;font-family:Times New Roman;color:blue;\">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<strong>2.&nbsp;&nbsp;&nbsp; Use Case Modeling</strong></pre>\n"+
                        "<pre id=\"C2.1\"style= \"font-size:14px;font-family:Times New Roman;color:blue;\">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<strong>2.1.&nbsp;&nbsp;&nbsp; Use Case Diagram</strong></pre>\n"+
                        "<pre style= \"font-size:12px;font-family:Times New Roman;word-wrap:break-word;word-break:break-all;white-space:pre-wrap;line-height:2;margin-left: 30px;\"><img src="+sd.getUcdimg()+"\"  style= \"border-radius: 0%\"></pre>\n"+
                        "<pre id=\"C2.2\"style= \"font-size:14px;font-family:Times New Roman;color:blue;\">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<strong>2.2.&nbsp;&nbsp;&nbsp; Use Cases Descriptions (Scenarios)</strong></pre>\n"+
                        "<pre style= \"font-size:12px;font-family:Times New Roman;word-wrap:break-word;word-break:break-all;white-space:pre-wrap;line-height:2;margin-left: 30px;\">"+usecases+"</pre>\n"+
                        "<pre id=\"C3\"style= \"font-size:14px;font-family:Times New Roman;color:blue;\">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<strong>3.&nbsp;&nbsp;&nbsp; State Transition Diagrams (STDs)</strong></pre>\n"+
                        "<pre style= \"font-size:12px;font-family:Times New Roman;word-wrap:break-word;word-break:break-all;white-space:pre-wrap;line-height:2;margin-left: 30px;\">"+STDs+"</pre>\n"+
                        "<pre id=\"C4\"style= \"font-size:14px;font-family:Times New Roman;color:blue;\">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<strong>4.&nbsp;&nbsp;&nbsp; Sequence Diagrams</strong></pre>\n"+
                        "<pre style= \"font-size:12px;font-family:Times New Roman;word-wrap:break-word;word-break:break-all;white-space:pre-wrap;line-height:2;margin-left: 30px;\">"+seqDiagrams+"</pre>\n"+
                        "<pre id=\"C5\"style= \"font-size:14px;font-family:Times New Roman;color:blue;\">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<strong>5.&nbsp;&nbsp;&nbsp; Non-functional Requirements</strong></pre>\n"+
                        "<pre style= \"font-size:12px;font-family:Times New Roman;word-wrap:break-word;word-break:break-all;white-space:pre-wrap;line-height:2;margin-left: 30px;\">"+nonfunction+"</pre>\n"+
                        "<pre id=\"C6\"style= \"font-size:14px;font-family:Times New Roman;color:blue;\">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<strong>6.&nbsp;&nbsp;&nbsp; Prototype (Screens Layout)</strong></pre>\n"+
                        "<pre style= \"font-size:12px;font-family:Times New Roman;word-wrap:break-word;word-break:break-all;white-space:pre-wrap;line-height:2;margin-left: 30px;\">"+prototypes+"</pre>\n"+
                        "<pre id=\"C7\"style= \"font-size:14px;font-family:Times New Roman;color:blue;\"><strong>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;7.&nbsp;&nbsp;&nbsp; References</strong></pre>\n"+
                        "<pre style= \"font-size:12px;font-family:Times New Roman;word-wrap:break-word;word-break:break-all;white-space:pre-wrap;line-height:2;margin-left: 30px;\">"+reference+"</pre>\n"+
                        "</body>\n" +
                        "</html>";
                wview.loadDataWithBaseURL(null,html,"text/html","utf-8",null);
                download.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        bitmap = LoadBitmap( toPDF , toPDF.getWidth(), toPDF.getHeight());
                        try {
                            CreatePdf(wview);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }});
                done.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(SRSPdf.this, ProjectDetails.class);
                        i.putExtra("Version", ver);
                        i.putExtra("Project", pro);
                        SRSPdf.this.startActivity(i);

                    }});




            }
        });

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    private Bitmap LoadBitmap(View v, int width, int height) {
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        v.draw(canvas);
        return bitmap;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void CreatePdf(View view) throws IOException {
        Context context=SRSPdf.this;
        PrintManager printManager=(PrintManager)SRSPdf.this.getSystemService(context.PRINT_SERVICE);
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
        File file = new File(sd.getDname()+".pdf");
        // uri = Uri.fromFile(file);
        if (file.exists()) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            uri = Uri.fromFile(file);
            intent.setDataAndType(uri, "application/pdf");
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            try {
                startActivity(intent);
            } catch (ActivityNotFoundException e) {
                Toast.makeText(this, "No Application for pdf view", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
