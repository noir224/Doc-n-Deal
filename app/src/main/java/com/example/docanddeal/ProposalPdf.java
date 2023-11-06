package com.example.docanddeal;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintJob;
import android.print.PrintManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class ProposalPdf extends AppCompatActivity {
    String docID;
    FirebaseFirestore fs;
    FirebaseAuth fAuth;
    DocumentReference dref,project;
    ImageView done,download;
    private Bitmap bitmap;
    RelativeLayout toPDF ;
    ProposalDocument pd;
    WebView wview;
    String logourl;
    ProjectC pro;
    Version ver;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_proposal_pdf);
        Bundle b = getIntent().getExtras();
        docID = b.get("docID").toString();
        logourl = b.get("logo").toString();
        ver = (Version) b.get("ver");
        pro = (ProjectC) b.getSerializable("Project");

        wview = findViewById(R.id.PRODVIEW);
        fAuth = FirebaseAuth.getInstance();
        fs = FirebaseFirestore.getInstance();
        dref = fs.collection("proposalDoc").document(docID);
        download = findViewById(R.id.proposalDownload);
        done = findViewById(R.id.proposalFinish);
        toPDF = findViewById(R.id.PRODPDF);

        dref.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                pd = documentSnapshot.toObject(ProposalDocument.class);
                ArrayList<TeamMember> tm = pd.getTeam();
                StringBuffer sb = new StringBuffer();
                if (tm.size()!=0){
                    for (TeamMember s : tm) {
                        sb.append(s.getName());
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
                StringBuffer sb1 = new StringBuffer();
                ArrayList<Abbrev> abb = pd.getExplination();

                for (Abbrev s : abb) {
                    sb1.append("\t-\t"+s.getName()+": "+s.getExpi());
                    sb1.append(System.getProperty("line.separator"));
                }
                String abbrev = sb1.toString();
                String name = pd.getDname();
                String objective = " \t\t"+pd.getObjectives();
//                StringBuffer sss = fromDB1(pd.getNeeds());
//                String need = sss.toString();
//                System.out.println(need+"seso");
//                System.out.println(sss+"seso2");
                StringBuffer sb2 = new StringBuffer();
                ArrayList<String>  nd= pd.getNeeds();

                for (String s : nd) {
                    sb2.append("\t-\t"+s+" ");
                    sb2.append(System.getProperty("line.separator"));
                }
                String need = sb2.toString();
                StringBuffer sb3 = new StringBuffer();
                ArrayList<String>  out= pd.getOutputs();

                for (String s : out) {
                    sb3.append("\t-\t"+s+" ");
                    sb3.append(System.getProperty("line.separator"));
                }
                String output = sb3.toString();
                StringBuffer sb4 = new StringBuffer();
                ArrayList<String>  fet= pd.getFeatures();

                sb4.append("\tThere are many defining key features this app will be offering for its users:");
                sb4.append(System.getProperty("line.separator"));
                for (String s : fet) {
                    sb4.append("\t-\t"+s+" ");
                    sb4.append(System.getProperty("line.separator"));
                }
                String feature = sb4.toString();
                StringBuffer sb5 = new StringBuffer();
                ArrayList<String>  UN= pd.getUN();
                for (String s : UN) {
                    sb5.append("\t-\t"+s+" ");
                    sb5.append(System.getProperty("line.separator"));
                    //  sb5.append(System.getProperty("line.separator"));
                }
                String UNs = sb5.toString();
                StringBuffer sb6 = new StringBuffer();
                ArrayList<Algorithm>  alg= pd.getAlg();

                for (Algorithm s : alg) {
                    sb6.append("\t-\t"+s.getAlg()+": "+s.getName());
                    sb6.append(System.getProperty("line.separator"));
                }
                String algorithm = sb6.toString();
                StringBuffer sb7 = new StringBuffer();
                ArrayList<API>  ap= pd.getAPI();

                for (API s : ap) {
                    sb7.append("\t-\t"+s.getName()+": "+s.getLink());
                    sb7.append(System.getProperty("line.separator"));
                }
                String apis = sb7.toString();

                StringBuffer sb8 = new StringBuffer();
                ArrayList<String>  in= pd.getIndi();

                for (String s : in) {
                    sb8.append("\t-\t"+s+" ");
                    sb8.append(System.getProperty("line.separator"));
                }
                String individual = sb8.toString();

                StringBuffer sb9 = new StringBuffer();
                ArrayList<String>  org= pd.getOrg();

                for (String s : org) {
                    sb9.append("\t-\t"+s+" ");
                    sb9.append(System.getProperty("line.separator"));
                }
                String organization = sb9.toString();

                StringBuffer sb10 = new StringBuffer();
                ArrayList<String>  soc= pd.getSoc();

                for (String s : soc) {
                    sb10.append("\t-\t"+s+" ");
                    sb10.append(System.getProperty("line.separator"));
                }
                String society = sb10.toString();

                StringBuffer sb11 = new StringBuffer();
                ArrayList<String>  ref= pd.getRef();

                for (String s : ref) {
                    sb11.append("\t-\t"+s+" ");
                    sb11.append(System.getProperty("line.separator"));
                }
                String reference = sb11.toString();


                ArrayList<TeamMember>  tc= pd.getTeam();

                String tcf ="<table style= \"width:90%; border: 1px solid black; border-collapse: collapse;margin-left: auto; margin-right: auto;\">\n" +
                        "  <tr style= \"width:90%; border: 1px solid black; border-collapse: collapse;\">\n" +
                        "    <th style= \"width:50%; border: 1px solid black; border-collapse: collapse;font-size:14px,font-family:Times New Roman;height:50px\">Name</th>\n" +
                        "    <th style= \"width:50%; border: 1px solid black; border-collapse: collapse;font-size:14px,font-family:Times New Roman;height:50px\">Capabilities</th>\n" +
                        "  ";
                for (TeamMember s : tc) {
                    ArrayList<String> cap = new ArrayList<>();
                    cap = s.getCapabilities();
                    StringBuffer sb12 = new StringBuffer();
                    for (String c:cap){
                        sb12.append("\t" + c + " ");
                        sb12.append(System.getProperty("line.separator"));
                        //sb12.append(System.getProperty("line.separator"));
                    }
                    String cc = sb12.toString();


                    tcf = tcf+ "<tr style= \"width:90%; border: 1px solid black; border-collapse: collapse;\">\n" +
                            "<td style= \"width:50%; border: 1px solid black; border-collapse: collapse;font-size:12px,font-family:Times New Roman;height:50px\">"+"     "+s.getName()+"</td>\n" +
                            "<td style= \"width:50%; border: 1px solid black; border-collapse: collapse;font-size:12px,font-family:Times New Roman;height:50px\">"+cc+"</td>\n" +
                            "";
                }
                tcf = tcf+"</table>";




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

                        "<p style= \"font-size:22px,font-family:Times New Roman;text-align:center;color:blue;\"><strong>Project Proposal</strong></p>\n"+
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


                        "<pre style= \"font-size:14px;font-family:Times New Roman;text-align:center\"><strong>Table of Contents</strong></pre>\n"+

                        "<pre style= \"font-size:12px;font-family:Times New Roman;text-align:center\"><a href=\"#C1\">1.&nbsp;&nbsp;&nbsp; Project Abbreviation .....................................................................................................</a></pre>\n"+

                        "<pre style= \"font-size:12px;font-family:Times New Roman;text-align:center\"><a href=\"#C2\">2.&nbsp;&nbsp;&nbsp; Project objectives   .........................................................................................................</a></pre>\n"+

                        "<pre style= \"font-size:12px;font-family:Times New Roman;text-align:center\"><a href=\"#C3\">3.&nbsp;&nbsp;&nbsp; The need of the project   ..............................................................................................</a></pre>\n"+

                        "<pre style= \"font-size:12px;font-family:Times New Roman;text-align:center\"><a href=\"#C4\">4.&nbsp;&nbsp;&nbsp; Expected outputs   ........................................................................................................</a></pre>\n"+

                        "<pre style= \"font-size:12px;font-family:Times New Roman;text-align:center\"><a href=\"#C5\">5.&nbsp;&nbsp;&nbsp; UN - Sustainable Development Goals   ...................................................................</a></pre>\n"+

                        "<pre style= \"font-size:12px;font-family:Times New Roman;text-align:center\"><a href=\"#C6\">6.&nbsp;&nbsp;&nbsp; Description of the key features of the project   ...................................................</a></pre>\n"+

                        "<pre style= \"font-size:12px;font-family:Times New Roman;text-align:center\"><a href=\"#C7\">7.&nbsp;&nbsp;&nbsp; Algorithms/APIs to be used   .....................................................................................</a></pre>\n"+

                        "<pre style= \"font-size:12px;font-family:Times New Roman;text-align:center\"><a href=\"#C8\">8.&nbsp;&nbsp;&nbsp; Description of the team members & capabilities   .............................................</a></pre>\n"+

                        "<pre style= \"font-size:12px;font-family:Times New Roman;text-align:center\"><a href=\"#C9\">9.&nbsp;&nbsp;&nbsp; Impact of the project   .................................................................................................</a></pre>\n"+

                        "<pre style= \"font-size:12px;font-family:Times New Roman;text-align:center\"><a href=\"#C9.1\">     9.1.&nbsp;&nbsp;&nbsp; Impact on individuals   .......................................................................................</a></pre>\n"+

                        "<pre style= \"font-size:12px;font-family:Times New Roman;text-align:center\"><a href=\"#C9.2\">     9.2.&nbsp;&nbsp;&nbsp; Impact on organization   ....................................................................................</a></pre>\n"+

                        "<pre style= \"font-size:12px;font-family:Times New Roman;text-align:center\"><a href=\"#C9.3\">     9.3.&nbsp;&nbsp;&nbsp; Impact on society   ................................................................................................</a></pre>\n"+

                        "<pre style= \"font-size:12px;font-family:Times New Roman;text-align:center\"><a href=\"#C10\">10.&nbsp;&nbsp;&nbsp; References   ....................................................................................................................</a></pre>\n"+
                        "<p><strong>&nbsp;</strong></p>\n"+
                        " <p><strong>&nbsp;</strong></p>\n"+
                        " <p><strong>&nbsp;</strong></p>\n"+
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
                        "<p><strong>&nbsp;</strong></p>\n"+
                        " <p><strong>&nbsp;</strong></p>\n"+


                        "<pre id=\"C1\" style= \"font-size:14px;font-family:Times New Roman;color:blue;\">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<strong>1.&nbsp;&nbsp;&nbsp;1.&nbsp;&nbsp;&nbsp; Project Abbrevition</strong></pre>\n"+
                        "<pre style= \"font-size:12px;font-family:Times New Roman;word-wrap:break-word;word-break:break-all;white-space:pre-wrap;line-height:2;margin-left: 30px;\">"+abbrev+"</pre>\n"+
                        "<pre id=\"C2\" style= \"font-size:14px;font-family:Times New Roman;color:blue;\">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<strong>2.&nbsp;&nbsp;&nbsp; Project objectives</strong></pre>\n"+
                        "<pre style= \"font-size:12px;font-family:Times New Roman;word-wrap:break-word;word-break:break-all;white-space:pre-wrap;line-height:2;margin-left: 30px;\">"+objective+"</pre>\n"+
                        "<pre id=\"C3\"style= \"font-size:14px;font-family:Times New Roman;color:blue;\"><strong>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;3.&nbsp;&nbsp;&nbsp;The need of the project</strong></pre>\n"+
                        "<pre style= \"font-size:12px;font-family:Times New Roman;word-wrap:break-word;word-break:break-all;white-space:pre-wrap;line-height:2;margin-left: 30px;\">"+need+"</pre>\n"+
                        "<pre id=\"C4\"style= \"font-size:14px;font-family:Times New Roman;color:blue;\"><strong>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;4.&nbsp;&nbsp;&nbsp; Expected outputs</strong></pre>\n"+
                        "<pre style= \"font-size:12px;font-family:Times New Roman;word-wrap:break-word;word-break:break-all;white-space:pre-wrap;line-height:2;margin-left: 30px;\">"+output+"</pre>\n"+
                        "<pre id=\"C5\"style= \"font-size:14px;font-family:Times New Roman;color:blue;\"><strong>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;5.&nbsp;&nbsp;&nbsp; UN - Sustainable Development Goals</strong></pre>\n"+
                        "<pre style= \"font-size:12px;font-family:Times New Roman;word-wrap:break-word;word-break:break-all;white-space:pre-wrap;line-height:2;margin-left: 30px;\">"+UNs+"</pre>\n"+
                        "<pre id=\"C6\"style= \"font-size:14px;font-family:Times New Roman;color:blue;\"><strong>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;6.&nbsp;&nbsp;&nbsp;Description of the key features of the project</strong></pre>\n"+
                        "<pre style= \"font-size:12px;font-family:Times New Roman;word-wrap:break-word;word-break:break-all;white-space:pre-wrap;line-height:2;margin-left: 30px;\">"+feature+"</pre>\n"+
                        "<pre id=\"C7\"style= \"font-size:14px;font-family:Times New Roman;color:blue;\"><strong>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;7.&nbsp;&nbsp;&nbsp; Algorithms/APIs to be used</strong></pre>\n"+
                        "<pre style= \"font-size:14px;font-family:Times New Roman;color:blue;\"><strong>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; Algorithms: </strong></pre>\n"+
                        "<pre style= \"font-size:12px;font-family:Times New Roman;word-wrap:break-word;word-break:break-all;white-space:pre-wrap;line-height:2;margin-left: 30px;\">"+algorithm+"</pre>\n"+
                        "<pre style= \"font-size:14px;font-family:Times New Roman;color:blue;\"><strong>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; APIs: </strong></pre>\n"+
                        "<pre style= \"font-size:12px;font-family:Times New Roman;word-wrap:break-word;word-break:break-all;white-space:pre-wrap;line-height:2;margin-left: 30px;\">"+apis+"</pre>\n"+
                        "<pre id=\"C8\"style= \"font-size:14px;font-family:Times New Roman;color:blue;\"><strong>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;8.&nbsp;&nbsp;&nbsp; Description of the team members&rsquo; capabilities</strong></pre>\n"+
                        "<pre style= \"font-size:12px;font-family:Times New Roman;word-wrap:break-word;word-break:break-all;white-space:pre-wrap;line-height:2;margin-left: 30px;\">"+tcf+"</pre>\n"+
                        "<pre id=\"C9\"style= \"font-size:14px;font-family:Times New Roman;color:blue;\"><strong>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;9.&nbsp;&nbsp;&nbsp; Impact of the project</strong></pre>\n"+
                        "<pre id=\"C9.1\"style= \"font-size:14px;font-family:Times New Roman;color:blue;\"><strong>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;9.1.&nbsp;&nbsp;&nbsp; Impact on individuals</strong></pre>\n"+
                        "<pre style= \"font-size:12px;font-family:Times New Roman;word-wrap:break-word;word-break:break-all;white-space:pre-wrap;line-height:2;margin-left: 30px;\">"+individual+"</pre>\n"+
                        "<pre id=\"C9.2\"style= \"font-size:14px;font-family:Times New Roman;color:blue;\"><strong>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;9.2.&nbsp;&nbsp;&nbsp; Impact on organization</strong></pre>\n"+
                        "<pre style= \"font-size:12px;font-family:Times New Roman;word-wrap:break-word;word-break:break-all;white-space:pre-wrap;line-height:2;margin-left: 30px;\">"+organization+"</pre>\n"+
                        "<pre id=\"C9.3\"style= \"font-size:14px;font-family:Times New Roman;color:blue;\"><strong>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;9.3.&nbsp;&nbsp;&nbsp; Impact on society</strong></pre>\n"+
                        "<pre style= \"font-size:12px;font-family:Times New Roman;word-wrap:break-word;word-break:break-all;white-space:pre-wrap;line-height:2;margin-left: 30px;\">"+society+"</pre>\n"+
                        "<pre id=\"C10\"style= \"font-size:14px;font-family:Times New Roman;color:blue;\"><strong>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;10.&nbsp;&nbsp;&nbsp; References</strong></pre>\n"+
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
                        Intent i = new Intent(ProposalPdf.this, ProjectDetails.class);
                        i.putExtra("Version", ver);
                        i.putExtra("Project", pro);
                        ProposalPdf.this.startActivity(i);
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
        Context context=ProposalPdf.this;
        PrintManager printManager=(PrintManager)ProposalPdf.this.getSystemService(context.PRINT_SERVICE);
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
        File file = new File(pd.getDname()+".pdf");
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
