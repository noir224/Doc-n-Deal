package com.example.docanddeal;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import javax.security.auth.callback.Callback;

public class EditVision2 extends AppCompatActivity implements View.OnClickListener, Callback {
    ImageView plusExplination;
    LinearLayout ExplinationList;
    FirebaseAuth fAuth;
    FirebaseFirestore fs;
    ArrayList<Abbrev> aexplination,eexplination;
    Bundle b;
    Version p;
    ProjectC pro;
    String pid;
    String ver;
    ImageView done;
    String userID;
    VisionDocument pdn;
    ArrayList<Abbrev> abbr = new ArrayList<>();
    RecyclerView rv;
    AbbrevAdapter da;
    RelativeLayout viss2;
    String docID;
    DocumentReference dref;
    ArrayList<Abbrev> e,allexp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_vision2);
        viss2  = findViewById(R.id.vis2);
        b = getIntent().getExtras();
        p = (Version) b.getSerializable("Version");
        ver = p.getVersion();
        pro = (ProjectC) b.getSerializable("Project");
        docID = b.get("docID").toString();
        fAuth =FirebaseAuth.getInstance();
        fs =  FirebaseFirestore.getInstance();
        userID = fAuth.getCurrentUser().getUid();

        dref = fs.collection("visionDoc").document(docID);
        abbr = (ArrayList<Abbrev>) b.getSerializable("exp");
        allexp = new ArrayList<>();
        rv = findViewById(R.id.ExpRV);

        rv.setHasFixedSize(true);
        RecyclerView.LayoutManager lm = new LinearLayoutManager(this);
        rv.setLayoutManager(lm);
        pdn= (VisionDocument) b.getSerializable("vis");

        if(abbr!=null) {
            da = new AbbrevAdapter(abbr, EditVision2.this,viss2);
            rv.setAdapter(da);
        }

        pid = pro.getPid();

        done =  findViewById(R.id.visionFinish);
        plusExplination = findViewById(R.id.plusExplination);
        ExplinationList = findViewById(R.id.Explinationlist);
        aexplination = new ArrayList<>();
        plusExplination.setOnClickListener(this);

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(EditVision2.this)
                        .setTitle("Save")
                        .setMessage("Do you want to save the Document?")
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent i = new Intent(EditVision2.this, ProjectDetails.class);
                                i.putExtra("Version",p);
                                i.putExtra("Project",pro);
                                startActivity(i);
                                finish();
                            }
                        })
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface arg0, int arg1) {
                                EditVision2.super.onBackPressed();
                                for (int i = 0; i < ExplinationList.getChildCount(); i++) {
                                    View v1 = ExplinationList.getChildAt(i);
                                    EditText abbrev = (EditText) v1.findViewById(R.id.abbName);
                                    String Abb = abbrev.getText().toString().trim();
                                    EditText expli = (EditText) v1.findViewById(R.id.exp);
                                    String Explination = expli.getText().toString().trim();
                                    allexp.add(new Abbrev(Abb,Explination));
                                    //fs.collection("exp").add(Explination);

                                }
                                for (int i = 0; i < da.getItemCount(); i++) {
                                    View v1 = rv.getChildAt(i);
                                    EditText abbrev = (EditText) v1.findViewById(R.id.abbName);
                                    String Abb = abbrev.getText().toString().trim();
                                    EditText expli = (EditText) v1.findViewById(R.id.exp);
                                    String Explination = expli.getText().toString().trim();
                                    allexp.add(new Abbrev(Abb,Explination));
                                }
                                //pdn.setExplination(aexplination);
                                fAuth =FirebaseAuth.getInstance();
                                fs =  FirebaseFirestore.getInstance();
                                userID = fAuth.getCurrentUser().getUid();
                                //if(!pd.getExplination().equals(allexp)){
                                dref.update("explination",allexp);//}
                                dref.update("alternatives",pdn.getAlternatives());
                                dref.update("assumptions",pdn.getAssumptions());
                                dref.update("businessOpportunity",pdn.getBusinessOpportunity());
                                dref.update("conclusion",pdn.getConclusion());
                                dref.update("dname",pdn.getDname());
                                dref.update("explination",aexplination);
                                dref.update("perspective",pdn.getPerspective());
                                dref.update("props",pdn.getProps());
                                dref.update("purpose",pdn.getPurpose());
                                dref.update("refrences",pdn.getRefrences());
                                dref.update("scope",pdn.getScope());
                                dref.update("stakeholders",pdn.getStakeholders());
                                dref.update("users",pdn.getUsers());
                                dref.update("vCapabilities",pdn.getvCapabilities());
                                dref.update("vEffort",pdn.getvEffort());
                                dref.update("vPriorities",pdn.getvPriorities());
                                dref.update("vrisk",pdn.getVrisk());
                                dref.update("vstability",pdn.getVstability());
                                dref.update("vKeyNeed",pdn.getvKeyNeed());
                                dref.update("vProductFeat",pdn.getvProductFeat());
                                Intent i = new Intent(EditVision2.this, ProjectDetails.class);
                                i.putExtra("Version",p);
                                i.putExtra("Project",pro);
                                startActivity(i);
                                finish();
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
    @Override
    public void onBackPressed() {
        EditVision2.super.onBackPressed();
        for (int i = 0; i < ExplinationList.getChildCount(); i++) {
            View v1 = ExplinationList.getChildAt(i);
            EditText abbrev = (EditText) v1.findViewById(R.id.abbName);
            String Abb = abbrev.getText().toString().trim();
            EditText expli = (EditText) v1.findViewById(R.id.exp);
            String Explination = expli.getText().toString().trim();
            allexp.add(new Abbrev(Abb,Explination));
            //fs.collection("exp").add(Explination);

        }
        for (int i = 0; i < da.getItemCount(); i++) {
            View v1 = rv.getChildAt(i);
            EditText abbrev = (EditText) v1.findViewById(R.id.abbName);
            String Abb = abbrev.getText().toString().trim();
            EditText expli = (EditText) v1.findViewById(R.id.exp);
            String Explination = expli.getText().toString().trim();
            allexp.add(new Abbrev(Abb,Explination));
        }
        fAuth =FirebaseAuth.getInstance();
        fs =  FirebaseFirestore.getInstance();
        userID = fAuth.getCurrentUser().getUid();
        //if(!pd.getExplination().equals(allexp)){
        dref.update("explination",allexp);//}
        Intent i = new Intent(EditVision2.this, EditProposal.class);
        i.putExtra("Version",p);
        i.putExtra("Project",pro);
        i.putExtra("docID",docID);
        startActivity(i);
        finish();


    }
}
