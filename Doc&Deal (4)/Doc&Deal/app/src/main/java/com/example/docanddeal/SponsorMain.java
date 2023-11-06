package com.example.docanddeal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class SponsorMain extends AppCompatActivity {
    TextView home, projectIdeas, notifications, logout;
    CircleImageView profilepic;
    FirebaseStorage store;
    StorageReference storageReference;
    FirebaseFirestore fs;
    FirebaseAuth fAuth;
    String userID;
    DocumentReference dref;
    RecyclerView rv;
    ArrayList<SponsoredProjects> projects;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sponsor_main);
        logout = findViewById(R.id.MainSponsorLogoutBtnProfile);
        home = findViewById(R.id.MainhomeProfile);
        projectIdeas = findViewById(R.id.MainProjectIdeas);
        notifications = findViewById(R.id.MainNotiProfile);
        profilepic = findViewById(R.id.MainprofilePicCProfile);
        fAuth = FirebaseAuth.getInstance();
        fs = FirebaseFirestore.getInstance();
        rv = findViewById(R.id.mainsponrv);
        dref = fs.collection("users").document(fAuth.getCurrentUser().getUid());
        userID = fAuth.getCurrentUser().getUid();
        store = FirebaseStorage.getInstance();
        storageReference = store.getReference();
        projects = new ArrayList<>();
        rv.setHasFixedSize(true);
        RecyclerView.LayoutManager lm = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        rv.setLayoutManager(lm);
        dref.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot ds = task.getResult();
                    Picasso.get().load(Uri.parse(ds.getString("imagepath"))).resize(50,50).centerCrop().into(profilepic);
                }
            }
        });
//        fs.collection("sponsored_projects")
//                .whereEqualTo("sponsorid",userID)
//                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                if (task.isSuccessful()) {
//                    for (QueryDocumentSnapshot document : task.getResult()) {
//                        SponsoredProjects p = document.toObject(SponsoredProjects.class);
//                        projects.add(p);
//                    }
//                    if (projects != null) {
//                        SponsoredProjectsAdapter da = new SponsoredProjectsAdapter(projects, SponsorMain.this);
//                        rv.setAdapter(da);
//                    }
//                }
//            }
//        });
        fs.collection("users").document(userID).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.get("sponsored_projects_IDs<>")!=null){
                    ArrayList<String> ppids = ((ArrayList<String>) documentSnapshot.get("sponsored_projects_IDs<>"));
                    for (String id : ppids){
                        fs.collection("sponsored_projects").document(id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                SponsoredProjects p = documentSnapshot.toObject(SponsoredProjects.class);
                                projects.add(p);
                                if (projects != null) {
                                    SponsoredProjectsAdapter da = new SponsoredProjectsAdapter(projects, SponsorMain.this);
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


        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(SponsorMain.this, Login.class));
                finish();

            }
        });
        profilepic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SponsorMain.this, SponsorProfile.class));

            }
        });
        projectIdeas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //startActivity(new Intent(SponsorMain.this, SponsorProjectIdeas.class));

            }
        });
        notifications.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SponsorMain.this, SponsorNotifications.class));

            }
        });
    }
}

