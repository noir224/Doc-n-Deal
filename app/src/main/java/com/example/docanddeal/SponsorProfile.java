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

public class SponsorProfile extends AppCompatActivity {


    FirebaseFirestore fStore;
    FirebaseAuth fAuth;
    String userID;
    ArrayList<SRProjects> projects;
    Uri IOimageurl;
    FirebaseStorage store;
    StorageReference storageReference;
    TextView username, field, email, phone, logout, home, projectIdeas, notifications, description, SprojectPerc, RprojectPerc, r,r1;
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
    boolean notiopen;
    ImageView notired;
    FirebaseFirestore fs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sponsor_profile);
        home = findViewById(R.id.ShomeProfile);
        empty = findViewById(R.id.nullProjects);
        logout = findViewById(R.id.SponsorLogoutBtnProfile);
        SImage = findViewById(R.id.SprofilePicProfile);
        sideimg = findViewById(R.id.SprofilePicCProfile);
        username = findViewById(R.id.SponsorUserName);
        field = findViewById(R.id.SponsorField);
        email = findViewById(R.id.SponsorEmail);
        phone = findViewById(R.id.SponsorPhoneNumber);
        edit = findViewById(R.id.SponsorEdit);
        cross= findViewById(R.id.crossy);
        description = findViewById(R.id.Description);
        SprojectPerc = findViewById(R.id.SponsoredProjects);
        RprojectPerc = findViewById(R.id.RejectedProjects);
        projectIdeas = findViewById(R.id.ProjectIdeas);
        notifications = findViewById(R.id.SnotiProfile);
        ADDSP= findViewById(R.id.addSP);
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
        notired = findViewById(R.id.newnotiSprofile);

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
        notiopen = true;
        notired.setVisibility(View.GONE);
        fs.collection("shared_projects").whereEqualTo("sharedWith",userID).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        SharedProject p = document.toObject(SharedProject.class);
                        list.add(p);
                    }
                } else { }
                if (!list.isEmpty()){
                    for (SharedProject m: list){
                        if (m.isOpenedsponsor()==false){
                            notiopen = false;
                        }
                    }
                    if (notiopen==false){
                        notired.setVisibility(View.VISIBLE);
                    }
                    else {
                        notired.setVisibility(View.GONE);
                    }
                }
            }
        });

        rv = findViewById(R.id.RVsponsoredPro);
        rv.setHasFixedSize(true);
        RecyclerView.LayoutManager lm = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        rv.setLayoutManager(lm);

        doc.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {


                s = task.getResult().getString("type");

                DisplayRecyclerView(userID);
                Displayinfo(userID);
                cross.setVisibility(View.GONE);
                helpicon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(SponsorProfile.this, "Long hold on card to edit or delete", Toast.LENGTH_LONG).show();
                        return;
                    }
                });
                edit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(SponsorProfile.this, SponsorEditProfile.class);
                        startActivity(i);
                    }
                });
                logout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String userid = fAuth.getCurrentUser().getUid();
                        fStore.collection("tokens").document(userid).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                if (documentSnapshot.exists()) {
                                    DocumentReference dref = fStore.collection("tokens").document(userid);
                                    dref.update("token", null);
                                }
                            }
                        });
                        FirebaseAuth.getInstance().signOut();
                        startActivity(new Intent(SponsorProfile.this, Login.class));
                        finish();

                    }
                });
                home.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(SponsorProfile.this, SponsorMain.class));
                    }
                });
                projectIdeas.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivity(new Intent(SponsorProfile.this, SharedProjects.class));
                    }
                });
                notifications.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivity(new Intent(SponsorProfile.this, SponsorNotifications.class));
                    }
                });
                ADDSP.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivity(new Intent(SponsorProfile.this, NewProject.class));
                    }
                });



            }
        });

    }
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
                                    SponsoredProjectsAdapter da = new SponsoredProjectsAdapter(projects, SponsorProfile.this,empty,SprojectPerc, RprojectPerc);
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