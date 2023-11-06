package com.example.docanddeal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class SponsorProfile extends AppCompatActivity {


    FirebaseFirestore fStore;
    FirebaseAuth fAuth;
    String userID;
    ArrayList<SponsoredProjects> projects;
    Uri IOimageurl;
    FirebaseStorage store;
    StorageReference storageReference;
    TextView username, field, email, phone, logout, home, projectIdeas, notifications, description, SprojectPerc, RprojectPerc;
    ImageView Sinstagram, Stwitter, Sfacebook;
    Button edit, ADDSP;
    ImageView SImage, sideimg;
    Uri Simageurl;
    DocumentReference doc;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sponsor_profile);
        home = findViewById(R.id.ShomeProfile);
        logout = findViewById(R.id.SponsorLogoutBtnProfile);
        SImage = findViewById(R.id.SprofilePicProfile);
        sideimg = findViewById(R.id.SprofilePicCProfile);
        username = findViewById(R.id.SponsorUserName);
        field = findViewById(R.id.SponsorField);
        email = findViewById(R.id.SponsorEmail);
        phone = findViewById(R.id.SponsorPhoneNumber);
        edit = findViewById(R.id.SponsorEdit);
        description = findViewById(R.id.Description);
        SprojectPerc = findViewById(R.id.SponsoredProjects);
        RprojectPerc = findViewById(R.id.RejectedProjects);
        projectIdeas = findViewById(R.id.ProjectIdeas);
        notifications = findViewById(R.id.SnotiProfile);
        ADDSP= findViewById(R.id.addSP);
        Sinstagram = findViewById(R.id.Sinsta);
        Stwitter = findViewById(R.id.Stwt);
        Sfacebook = findViewById(R.id.Sfb);
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        userID = fAuth.getCurrentUser().getUid();
        store = FirebaseStorage.getInstance();
        storageReference = store.getReference();
        doc = fStore.collection("users").document(userID);
        doc = fStore.collection("users").document(userID);
        projects = new ArrayList<>();

        RecyclerView rv = findViewById(R.id.RVsponsoredPro);
        rv.setHasFixedSize(true);
        RecyclerView.LayoutManager lm = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        rv.setLayoutManager(lm);

        fStore.collection("users").document(userID).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.get("sponsored_projects_IDs<>")!=null){
                    ArrayList<String> ppids = ((ArrayList<String>) documentSnapshot.get("sponsored_projects_IDs<>"));
                    for (String id : ppids){
                        fStore.collection("sponsored_projects").document(id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                SponsoredProjects p = documentSnapshot.toObject(SponsoredProjects.class);
                                projects.add(p);
                                DecimalFormat df = new DecimalFormat("###.###");
                                SprojectPerc.setText(projects.size()/projects.size()*100+"");
                                RprojectPerc.setText(0+"");
                                if (projects != null) {
                                    SponsoredProjectsAdapter da = new SponsoredProjectsAdapter(projects, SponsorProfile.this);
                                    rv.setAdapter(da);
                                }

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                            }
                        });
                    }
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });

        doc.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {

                    DocumentSnapshot ds = task.getResult();
                    description.setText(ds.getString("description"));
                    SprojectPerc.setText(ds.getString("SponsoredProjects"));
                    RprojectPerc.setText(ds.getString("RejectedProjects"));
                    phone.setText(ds.getString("phone"));
                    email.setText(ds.getString("email"));
                    field.setText(ds.getString("field"));
                    username.setText(ds.getString("username"));
                    Picasso.get().load(Uri.parse(ds.getString("imagepath"))).resize(50, 50).centerCrop().into(sideimg);
                    Picasso.get().load(Uri.parse(ds.getString("imagepath"))).resize(50, 50).centerCrop().into(SImage);
                    /*  s = task.getResult().getString("type");
                      if ((s.compareTo("creator") == 0)) {

                       }*/
                    SpannableString p = new SpannableString(phone.getText().toString());
                    p.setSpan(new UnderlineSpan(), 0, p.length(), 0);
                    phone.setText(p);
                    System.out.println("maha");
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
                //startActivity(new Intent(SponsorProfile.this, SponsorProjectIdeas.class));
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

    private void gotoUrl(String s) {
        Uri uri = Uri.parse(s);
        startActivity(new Intent(Intent.ACTION_VIEW, uri));
    }



}