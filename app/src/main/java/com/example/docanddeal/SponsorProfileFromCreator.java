package com.example.docanddeal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class SponsorProfileFromCreator extends AppCompatActivity {


    FirebaseFirestore fStore;
    FirebaseAuth fAuth;
    String userID;
    ArrayList<SRProjects> projects;
    Uri IOimageurl;
    FirebaseStorage store;
    StorageReference storageReference;
    TextView username, field, email, phone, description, SprojectPerc, RprojectPerc, r,r1;
    ImageView Sinstagram, Stwitter, Sfacebook, SImage, sideimg,helpicon, cross;
    Button edit, ADDSP,applyto;
    Uri Simageurl;
    DocumentReference doc;
    TextView empty;
    String s;
    RecyclerView rv;
    TextView helptext;
    boolean helpclicked;
    ImageView arrow;
    Integer ss;
    FirebaseFirestore fs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sponsor_profile_from_creator);
        empty = findViewById(R.id.nullProjects);
        SImage = findViewById(R.id.SprofilePicProfile);
        username = findViewById(R.id.SponsorUserName);
        field = findViewById(R.id.SponsorField);
        email = findViewById(R.id.SponsorEmail);
        phone = findViewById(R.id.SponsorPhoneNumber);

        description = findViewById(R.id.Description);
        SprojectPerc = findViewById(R.id.SponsoredProjects);
        RprojectPerc = findViewById(R.id.RejectedProjects);
        applyto = findViewById(R.id.applyTobtn);
        Sinstagram = findViewById(R.id.Sinsta);
        Stwitter = findViewById(R.id.Stwt);
        Sfacebook = findViewById(R.id.Sfb);
        r=findViewById(R.id.textView3);
        r1=findViewById(R.id.inv);
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        userID = fAuth.getCurrentUser().getUid();
        store = FirebaseStorage.getInstance();
        storageReference = store.getReference();
        doc = fStore.collection("users").document(userID);
        projects = new ArrayList<>();
        helpicon = findViewById(R.id.projectrvhelppic);
        arrow = findViewById(R.id.arrowsponsorprofile);
        helptext = findViewById(R.id.texthelpsponsorprofile);
        helpclicked = false;

        helptext.setVisibility(View.GONE);
        arrow.setVisibility(View.GONE);

        helpicon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (helpclicked==false){
                    helptext.setVisibility(View.VISIBLE);
                    arrow.setVisibility(View.VISIBLE);
                    helpclicked = true;
                }
                else {
                    helptext.setVisibility(View.GONE);
                    arrow.setVisibility(View.GONE);
                    helpclicked = false;
                }
            }
        });

        ArrayList<SharedProject> list = new ArrayList<>();
        fs = FirebaseFirestore.getInstance();
        rv = findViewById(R.id.RVsponsoredPro);
        rv.setHasFixedSize(true);
        RecyclerView.LayoutManager lm = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        rv.setLayoutManager(lm);

        doc.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {



                    Bundle b= getIntent().getExtras();
                    String sponsorID= b.getString("sponsorID");
                    String from = b.getString("from");
                    if(from.equals("Search"))
                        applyto.setVisibility(View.VISIBLE);

                    r.setBackgroundColor(0xFFFAFAFA);
                    RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) r.getLayoutParams();
                    params.width= 230;
                    r.setLayoutParams(params);
                    DisplayRecyclerView(sponsorID);
                    Displayinfo(sponsorID);
                    cross.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            startActivity(new Intent(SponsorProfileFromCreator.this, SearchSponsors.class));

                        }
                    });
                    SpannableString p = new SpannableString(phone.getText().toString());
                    p.setSpan(new UnderlineSpan(), 0, p.length(), 0);
                    phone.setText(p);
                    phone.setTextColor(Color.parseColor("#5C527F"));
                    phone.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent p=new Intent(Intent.ACTION_DIAL);
                            p.setData(Uri.parse("tel:"+phone.getText().toString()));
                            startActivity(p);
                        }
                    });
                    SpannableString e = new SpannableString(email.getText().toString());
                    e.setSpan(new UnderlineSpan(), 0, e.length(), 0);
                    email.setText(e);
                    email.setTextColor(Color.parseColor("#5C527F"));
                    email.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent e=new Intent(Intent.ACTION_SEND);
                            e.putExtra(Intent.EXTRA_EMAIL,email.getText().toString());
                            e.setType("text/plain");
                            startActivity(Intent.createChooser(e,"send email:"));
                        }
                    });
                    applyto.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            AlertDialog.Builder mDialog = new AlertDialog.Builder(SponsorProfileFromCreator.this);
                            final View projview = LayoutInflater.from(SponsorProfileFromCreator.this).inflate(R.layout.projectspopup,null);
                            mDialog.setView(projview);
                            RecyclerView rv = projview.findViewById(R.id.projectsRVpopup);
                            TextView empty = projview.findViewById(R.id.ifnullProjects);
                            CircularProgressIndicator progressBar = projview.findViewById(R.id.progBarProjects);
                            rv.setHasFixedSize(true);
                            RecyclerView.LayoutManager lm = new GridLayoutManager(mDialog.getContext(),2);
                            rv.setLayoutManager(lm);
                            ArrayList<ProjectC> projects = new ArrayList<>();
                            fStore.collection("users").document(userID).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(DocumentSnapshot documentSnapshot1) {
                                    fStore.collection("users").document(sponsorID).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                        @Override
                                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                                            SponsorUser su = documentSnapshot.toObject(SponsorUser.class);
                                            if(documentSnapshot1.get("projects")!=null){
                                                ArrayList<String> ppids = ((ArrayList<String>) documentSnapshot1.get("projects"));
                                                if(ppids.isEmpty())
                                                    empty.setVisibility(View.VISIBLE);
                                                progressBar.setVisibility(View.GONE);
                                                for (String id : ppids){
                                                    fStore.collection("projects").document(id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                                        @Override
                                                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                            ProjectC p = documentSnapshot.toObject(ProjectC.class);
                                                            projects.add(p);
                                                            if(projects!=null) {
                                                                ProejctsAdapter da = new ProejctsAdapter(projects, SponsorProfileFromCreator.this,empty,"Search",su);
                                                                rv.setAdapter(da);
                                                                empty.setVisibility(View.GONE);
                                                            }

                                                        }
                                                    }).addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {

                                                        }
                                                    });
                                                }
                                            }else{
                                                empty.setVisibility(View.VISIBLE);
                                                progressBar.setVisibility(View.GONE);
                                            }
                                        }
                                    });


                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {

                                }
                            });
                            AlertDialog dialog = mDialog.create();
                            dialog.show();

                        }
                    });
                }

        });}


    public void DisplayRecyclerView(String id){
        fStore.collection("users").document(id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.get("sponsoredProjects")!=null){
                    ArrayList<String> ppids = ((ArrayList<String>) documentSnapshot.get("sponsoredProjects"));
                    if(ppids.isEmpty())
                        empty.setVisibility(View.VISIBLE);
                    for (String id : ppids){
                        fStore.collection("sponsored_projects").document(id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                SRProjects p = documentSnapshot.toObject(SRProjects.class);
                                projects.add(p);
                                if (projects != null) {
                                    SponsoredProjectsAdapter da = new SponsoredProjectsAdapter(projects, SponsorProfileFromCreator.this,empty,SprojectPerc, RprojectPerc);
                                    rv.setAdapter(da);
                                    empty.setVisibility(View.GONE);
                                }
                            }
                        });
                    }
                }
                else {
                    empty.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    public void Displayinfo(String id){
        fStore.collection("users").document(id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    ArrayList<String> sp = (ArrayList<String>) task.getResult().get("sponsoredProjects");
                    ArrayList<String> rp = (ArrayList<String>) task.getResult().get("rejectedProjects");
                    int ss= 0;
                    int rr=0;
                    double SP=0;
                    double RP=0;
                    if(sp != null)
                        if(!sp.isEmpty())
                            ss= sp.size();
                    if(rp != null)
                        if(!rp.isEmpty())
                            rr= rp.size();
                    if(ss!=0)
                        SP= ((double) ss/(ss+rr))*100;
                    if(rr != 0)
                        RP=((double) rr/(ss+rr))*100;
                    SP= Math.round(SP*100.0)/100.0;
                    RP = Math.round(RP*100.0)/100.0;
                    SprojectPerc.setText(""+SP);
                    RprojectPerc.setText(""+RP);
                    DocumentSnapshot ds = task.getResult();
                    description.setText(ds.getString("description"));
                    phone.setText(ds.getString("phone"));
                    email.setText(ds.getString("email"));
                    field.setText(ds.getString("field"));
                    username.setText(ds.getString("username"));
                    Picasso.get().load(Uri.parse(ds.getString("imagepath"))).resize(50, 50).centerCrop().into(sideimg);
                    Picasso.get().load(Uri.parse(ds.getString("imagepath"))).resize(50, 50).centerCrop().into(SImage);
                    Sinstagram.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            gotoUrl(ds.getString("instagram"));
                        }
                    });
                    Stwitter.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            gotoUrl(ds.getString("twitter"));
                        }
                    });
                    Sfacebook.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            gotoUrl(ds.getString("facebook"));
                        }
                    });

                }
            }
        });
    }
    private void gotoUrl(String s) {
        Uri uri = Uri.parse(s);
        startActivity(new Intent(Intent.ACTION_VIEW, uri));
    }



}