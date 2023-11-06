package com.example.docanddeal;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.Serializable;
import java.sql.SQLOutput;
import java.util.ArrayList;

public class Proposal2 extends AppCompatActivity implements View.OnClickListener {
    ImageView plusExplination;
    LinearLayout ExplinationList;
    FirebaseAuth fAuth;
    FirebaseFirestore fs;
    ArrayList<Abbrev> aexplination;
    Bundle b;
    Version p;
    ProjectC pro;
    String pid;
    String ver;
    ImageView done;
    String userID;
    ProposalDocument pdn;
    ArrayList<Abbrev> abbr = new ArrayList<>();
    RecyclerView rv;
    AbbrevAdapter da;
    RelativeLayout pro2;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_proposal2);
        pro2  = findViewById(R.id.prop2);



        b = getIntent().getExtras();

        p = (Version) b.getSerializable("Version");
        ver = p.getVersion();
        System.out.println(p.getVersion()+"hfff");
        pro = (ProjectC) b.getSerializable("Project");
        System.out.println(pro.getName()+"fff");
        pid = pro.getPid();
        abbr = (ArrayList<Abbrev>) b.getSerializable("exp");
        done =  findViewById(R.id.proposalFinish);
        plusExplination = findViewById(R.id.plusExplination);
        ExplinationList = findViewById(R.id.Explinationlist);
        aexplination = new ArrayList<>();
        pdn= (ProposalDocument) b.getSerializable("prop");
        rv = findViewById(R.id.ExpRV);

        rv.setHasFixedSize(true);
        RecyclerView.LayoutManager lm = new LinearLayoutManager(this);
        rv.setLayoutManager(lm);

        if(abbr!=null) {
            da = new AbbrevAdapter(abbr, Proposal2.this,pro2);
            rv.setAdapter(da);
        }


        plusExplination.setOnClickListener(this);

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(Proposal2.this)
                        .setTitle("Save")
                        .setMessage("Do you want to save the Document?")
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent i = new Intent(Proposal2.this, ProjectDetails.class);
                                i.putExtra("Version",p);
                                i.putExtra("Project",pro);
                                //i.putExtra("pid",pid);
                                startActivity(i);
                                finish();
                            }
                        })
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface arg0, int arg1) {
                                Proposal2.super.onBackPressed();
                                for (int i = 0; i < ExplinationList.getChildCount(); i++) {
                                    View v1 = ExplinationList.getChildAt(i);
                                    EditText abbrev = (EditText) v1.findViewById(R.id.abbName);
                                    String Abb = abbrev.getText().toString().trim();
                                    EditText expli = (EditText) v1.findViewById(R.id.exp);
                                    String Explination = expli.getText().toString().trim();
                                    aexplination.add(new Abbrev(Abb,Explination));
                                    //fs.collection("exp").add(Explination);

                                }
                                for (int i = 0; i < da.getItemCount(); i++) {
                                    View v1 = rv.getChildAt(i);
                                    EditText abbrev = (EditText) v1.findViewById(R.id.abbName);
                                    String Abb = abbrev.getText().toString().trim();
                                    EditText expli = (EditText) v1.findViewById(R.id.exp);
                                    String Explination = expli.getText().toString().trim();
                                    aexplination.add(new Abbrev(Abb,Explination));
                                }
                                pdn.setExplination(aexplination);
                                fAuth =FirebaseAuth.getInstance();
                                fs =  FirebaseFirestore.getInstance();
                                userID = fAuth.getCurrentUser().getUid();
                                fs.collection("proposalDoc").add(pdn).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                    @Override
                                    public void onSuccess(DocumentReference documentReference) {
                                        //public Document(String uid, String type, String name, String docid, String pid, String verid)
                                        Document d = new Document(userID,"proposal",pdn.getDname(),documentReference.getId(),pid,ver);
                                        fs.collection("documents").document(documentReference.getId()).set(d);
                                        Intent i = new Intent(Proposal2.this, ProjectDetails.class);
                                        i.putExtra("Version",p);
                                        i.putExtra("Project",pro);
                                        //i.putExtra("pid",pid);
                                        startActivity(i);
                                        finish();
                                    }
                                });
                            }
                        }).create().show();
            }
        });


    }
    @Override
    public void onClick(View v) {addViewExp();}

    private void addViewExp() {
        View expview = getLayoutInflater().inflate(R.layout.explination_proposal,null,false);
        EditText expli = (EditText) expview.findViewById(R.id.exp);
        EditText abbrev = (EditText) expview.findViewById(R.id.abbName);
        ImageView delExp = (ImageView) expview.findViewById(R.id.deleteexp);
        delExp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeViewExp(expview);
            }
        });
        ExplinationList.addView(expview);
    }

    private void removeViewExp(View view){
        ExplinationList.removeView(view);
    }
//    @Override
//    public void onBackPressed() {
//        new AlertDialog.Builder(this)
//                .setTitle("Save")
//                .setMessage("Do you want to save the Document?")
//                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        Intent i = new Intent(Proposal2.this, ProjectDetails.class);
//                        i.putExtra("Version",p);
//                        i.putExtra("Project",pro);
//                        i.putExtra("pid",pid);
//                        startActivity(i);
//                        finish();
//                    }
//                })
//                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface arg0, int arg1) {
//                        Proposal2.super.onBackPressed();
//                        for (int i = 0; i < ExplinationList.getChildCount(); i++) {
//                            View v1 = ExplinationList.getChildAt(i);
//                            EditText abbrev = (EditText) v1.findViewById(R.id.abbName);
//                            String Abb = abbrev.getText().toString().trim();
//                            EditText expli = (EditText) v1.findViewById(R.id.exp);
//                            String Explination = expli.getText().toString().trim();
//                            aexplination.add(new Abbrev(Abb,Explination));
//                            //fs.collection("exp").add(Explination);
//                        }
//                        fAuth =FirebaseAuth.getInstance();
//                        fs =  FirebaseFirestore.getInstance();
//                        userID = fAuth.getCurrentUser().getUid();
//                        fs.collection("proposalDoc").add(pdn).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
//                            @Override
//                            public void onSuccess(DocumentReference documentReference) {
//                                // public Document(String uid, String type, String name, String docid, String pid, String verid)
//                                Document d = new Document(userID,"proposal",pdn.getDname(),documentReference.getId(),pid,ver);
//                                fs.collection("documents").document(documentReference.getId()).set(d);
//                                Intent i = new Intent(Proposal2.this, ProjectDetails.class);
//                                //  i.putExtra("Version",p);
//                                i.putExtra("Project",pro);
//                                // i.putExtra("pid",pid);
//                                startActivity(i);
//                                finish();
//                            }
//                        });
//                    }
//                }).create().show();
//    }
}