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

public class Vision2 extends AppCompatActivity implements View.OnClickListener, Callback {
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
    VisionDocument vdn;
    ArrayList<Abbrev> abbr = new ArrayList<>();
    RecyclerView rv;
    AbbrevAdapter da;
    RelativeLayout viss2;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vision2);
        viss2  = findViewById(R.id.vis2);
        b = getIntent().getExtras();
        p = (Version) b.getSerializable("Version");
        ver = p.getVersion();
        pro = (ProjectC) b.getSerializable("Project");
        pid = pro.getPid();
        abbr = (ArrayList<Abbrev>) b.getSerializable("exp");
        done =  findViewById(R.id.visionFinish);
        plusExplination = findViewById(R.id.plusExplination);
        ExplinationList = findViewById(R.id.Explinationlist);
        aexplination = new ArrayList<>();
        vdn= (VisionDocument) b.getSerializable("vis");
        rv = findViewById(R.id.ExpRV);

        rv.setHasFixedSize(true);
        RecyclerView.LayoutManager lm = new LinearLayoutManager(this);
        rv.setLayoutManager(lm);

        if(abbr!=null) {
            da = new AbbrevAdapter(abbr, Vision2.this,viss2);
            rv.setAdapter(da);
        }


        plusExplination.setOnClickListener(this);

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(Vision2.this)
                        .setTitle("Save")
                        .setMessage("Do you want to save the Document?")
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent i = new Intent(Vision2.this, ProjectDetails.class);
                                i.putExtra("Version",p);
                                i.putExtra("Project",pro);
                                startActivity(i);
                                finish();
                            }
                        })
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface arg0, int arg1) {
                                Vision2.super.onBackPressed();
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
                                vdn.setExplination(aexplination);
                                fAuth =FirebaseAuth.getInstance();
                                fs =  FirebaseFirestore.getInstance();
                                userID = fAuth.getCurrentUser().getUid();
                                fs.collection("documents").whereEqualTo("pid",pid).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                    @Override
                                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                        for (DocumentSnapshot ds: queryDocumentSnapshots) {
                                            Document d = ds.toObject(Document.class);
                                            if(d.getName().equals(vdn.getDname()) && d.getVerid().equals(p.getVersion()))
                                                Toast.makeText(Vision2.this,"Document name already exists",Toast.LENGTH_LONG).show();
                                            else{
                                                fs.collection("visionDoc").add(vdn).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                    @Override
                                                    public void onSuccess(DocumentReference documentReference) {
                                                        //public Document(String uid, String type, String name, String docid, String pid, String verid)
                                                        Document d = new Document(userID,"vision",vdn.getDname(),documentReference.getId(),pid,ver,null);
                                                        fs.collection("documents").document(documentReference.getId()).set(d);
                                                        Intent i = new Intent(Vision2.this, ProjectDetails.class);
                                                        i.putExtra("Version",p);
                                                        i.putExtra("Project",pro);
                                                        //i.putExtra("pid",pid);
                                                        startActivity(i);
                                                        finish();
                                                    }
                                                });
                                                break;
                                            }

                                        }


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
                String s1 = expli.getText().toString();
                String s2 = abbrev.getText().toString();
                removeViewExp(expview,s1,s2);
            }
        });
        ExplinationList.addView(expview);
    }

    private void addViewExp(String sp1, String sp2) {
        View expview = getLayoutInflater().inflate(R.layout.explination_proposal,null,false);
        EditText expli = (EditText) expview.findViewById(R.id.exp);
        expli.setText(sp1);
        EditText abbrev = (EditText) expview.findViewById(R.id.abbName);
        abbrev.setText(sp2);
        ImageView delExp = (ImageView) expview.findViewById(R.id.deleteexp);
        delExp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s1 = expli.getText().toString();
                String s2 = abbrev.getText().toString();
                removeViewExp(expview,s1,s2);
            }
        });
        ExplinationList.addView(expview);
    }

    private void removeViewExp(View view, String s1, String s2){
        ExplinationList.removeView(view);
        Snackbar.make(viss2,"The item is deleted", Snackbar.LENGTH_LONG).setAction("UNDO", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (s1.isEmpty() && s2.isEmpty()){
                    addViewExp();
                }
                else {
                    addViewExp(s1, s2);
                }            }
        }).show();

    }
    @Override
    public void onBackPressed() {
        Vision2.super.onBackPressed();
        Intent i = new Intent(Vision2.this, VisionDoc.class);
        i.putExtra("Version",p);
        i.putExtra("Project",pro);
        i.putExtra("pid",pid);
        i.putExtra("exp",aexplination);
    }
}
