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
    TextView username, field, email, phone, logout, home, projectIdeas, notifications, description, SprojectPerc, RprojectPerc, r,r1;
    ImageView Sinstagram, Stwitter, Sfacebook, SImage, sideimg,helpicon, cross;
    Button edit, ADDSP;
    Uri Simageurl;
    DocumentReference doc;
    TextView empty;
    String s;
    RecyclerView rv;


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

        rv = findViewById(R.id.RVsponsoredPro);
        rv.setHasFixedSize(true);
        RecyclerView.LayoutManager lm = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        rv.setLayoutManager(lm);

        doc.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                s = task.getResult().getString("type");
                if ((s.compareTo("sponsor") == 0)) {
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
                else{
                    Bundle b= getIntent().getExtras();
                    String sponsorID= b.getString("sponsorID");
                    home.setVisibility(View.GONE);
                    projectIdeas.setVisibility(View.GONE);
                    notifications.setVisibility(View.GONE);
                    logout.setVisibility(View.GONE);
                    sideimg.setVisibility(View.GONE);
                    helpicon.setVisibility(View.GONE);
                    ADDSP.setVisibility(View.GONE);
                    edit.setVisibility(View.GONE);
                    r1.setVisibility(View.GONE);
                    r.setBackgroundColor(0xFFFAFAFA);
                    RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) r.getLayoutParams();
                    params.width= 230;
                    r.setLayoutParams(params);
                    DisplayRecyclerView(sponsorID);
                    Displayinfo(sponsorID);
                    cross.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            startActivity(new Intent(SponsorProfile.this, SearchSponsors.class));

                        }
                    });
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
                }
            }
        });

    }
    public void DisplayRecyclerView(String id){
        fStore.collection("users").document(id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.get("sponsored_projects_IDs<>")!=null){
                    ArrayList<String> ppids = ((ArrayList<String>) documentSnapshot.get("sponsored_projects_IDs<>"));
                    if(ppids.isEmpty())
                        empty.setVisibility(View.VISIBLE);
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
                                    SponsoredProjectsAdapter da = new SponsoredProjectsAdapter(projects, SponsorProfile.this,empty);
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