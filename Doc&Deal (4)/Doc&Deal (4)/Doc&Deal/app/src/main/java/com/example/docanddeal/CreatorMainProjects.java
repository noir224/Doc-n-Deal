package com.example.docanddeal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

public class CreatorMainProjects extends AppCompatActivity {
    TextView logout,empty,sponsors;
    Button createNewProject;
    CircularProgressIndicator progressBar;
    FirebaseFirestore fs;
    FirebaseAuth fAuth;
    DocumentReference dref;
    ArrayList<ProjectC> projects;
    ArrayList<String> pids;
    String userID;

    CircleImageView profilepic;
    TextView uname;
    Uri IOimageurl;
    FirebaseStorage store;
    StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creator_main_projects);
        logout = findViewById(R.id.creatorProjectsLogoutBtn);
        createNewProject = findViewById(R.id.createNewProject);
        profilepic = findViewById(R.id.profilePicCMainProjects);
        empty = findViewById(R.id.ifnullProjects);
        progressBar = findViewById(R.id.progBarProjects);
        sponsors = findViewById(R.id.sponsorsProjects);

        projects = new ArrayList<>();
        pids = new ArrayList<>();
        empty.setVisibility(View.GONE);
        fAuth = FirebaseAuth.getInstance();
        userID = fAuth.getCurrentUser().getUid();
        fs = FirebaseFirestore.getInstance();
        dref = fs.collection("users").document(userID);
        store = FirebaseStorage.getInstance();
        storageReference = store.getReference();
        dref.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot ds = task.getResult();
                    Picasso.get().load(Uri.parse(ds.getString("imagepath"))).resize(50,50).centerCrop().into(profilepic);
                }
            }
        });

        RecyclerView rv = findViewById(R.id.projectsRV);
        rv.setHasFixedSize(true);
        RecyclerView.LayoutManager lm = new GridLayoutManager(this,5);
        rv.setLayoutManager(lm);
        fs.collection("users").document(userID).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.get("projects")!=null){
                    ArrayList<String> ppids = ((ArrayList<String>) documentSnapshot.get("projects"));
                    if(ppids.isEmpty())
                        empty.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);
                    for (String id : ppids){
                        fs.collection("projects").document(id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                ProjectC p = documentSnapshot.toObject(ProjectC.class);
                                projects.add(p);
                                if(projects!=null) {
                                    ProejctsAdapter da = new ProejctsAdapter(projects, CreatorMainProjects.this,empty);
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
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });

        /*fs.collection("projects")
                .whereEqualTo("uid", userID)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                ProjectC p = document.toObject(ProjectC.class);
                                projects.add(p);
                                pids.add(document.getId());
                                if(projects!=null) {
                                    ProejctsAdapter da = new ProejctsAdapter(projects, CreatorMainProjects.this,pids,empty);
                                    rv.setAdapter(da);
                                }
                                //progressBar.setVisibility(View.GONE);
                            }
                            progressBar.setVisibility(View.GONE);
                            if(projects.isEmpty())
                                empty.setVisibility(View.VISIBLE);


                        }
                    }
                });*/




        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(CreatorMainProjects.this, Login.class));
                finish();

            }
        });
        sponsors.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(CreatorMainProjects.this, SearchSponsors.class);
                startActivity(i);
            }
        });

        createNewProject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(CreatorMainProjects.this, NewProject.class);
                startActivity(i);
            }
        });

        profilepic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(CreatorMainProjects.this, IdeaOwnerProfilePage.class);
                startActivity(i);
            }
        });

    }
    @Override
    public void onBackPressed() {
        startActivity(getIntent());
        super.onBackPressed();
    }

}