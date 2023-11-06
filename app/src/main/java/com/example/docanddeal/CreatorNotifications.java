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
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import de.hdodenhof.circleimageview.CircleImageView;

public class CreatorNotifications extends AppCompatActivity {

    TextView logout,empty,sponsors,home;
    FirebaseFirestore fs;
    FirebaseAuth fAuth;
    DocumentReference dref;
    //ArrayList<Notification> notifications;
    ArrayList<String> sharedprojectsIDs;
    String userID;
    RecyclerView rv;

    CircleImageView profilepic;
    TextView uname;
    Uri IOimageurl;
    FirebaseStorage store;
    StorageReference storageReference;
    ArrayList<SharedProject> sharedprojects;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creator_notifications);
        logout = findViewById(R.id.creatornotifLogoutBtn);
        profilepic = findViewById(R.id.profilePicCnoti);
        empty = findViewById(R.id.ifnullcreatornoti);
        sponsors = findViewById(R.id.sponsorscreatornoti);
        home = findViewById(R.id.homecreatornoti);
        sharedprojects = new ArrayList<>();


        empty.setVisibility(View.GONE);
        fAuth = FirebaseAuth.getInstance();
        userID = fAuth.getCurrentUser().getUid();
        fs = FirebaseFirestore.getInstance();
        rv = findViewById(R.id.creatornotiRV);
        sharedprojects = new ArrayList<>();
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


        rv.setHasFixedSize(true);
        RecyclerView.LayoutManager lm = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        rv.setLayoutManager(lm);

        fs.collection("shared_projects")
                .whereEqualTo("creatorID", userID)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                SharedProject d = document.toObject(SharedProject.class);
                                if (d.getType().equalsIgnoreCase("proposal")){
                                    if(d.getStage1().equalsIgnoreCase("accepted")||d.getStage1().equalsIgnoreCase("accept_with_revision")||
                                        d.getStage1().equalsIgnoreCase("rejected")){
                                    sharedprojects.add(d);
                                    DocumentReference doc = fs.collection("shared_projects").document(d.getSharedprojectid());
                                    doc.update("openedcreator",true);
                                }}
                                //new
                                if (d.getType().equalsIgnoreCase("vision")){
                                    if (d.getStage1().equalsIgnoreCase("accepted")&d.getStage2().equalsIgnoreCase("accepted")
                                            ||d.getStage2().equalsIgnoreCase("accept_with_revision")){
                                    sharedprojects.add(d);
                                    DocumentReference doc = fs.collection("shared_projects").document(d.getSharedprojectid());
                                    doc.update("openedcreator",true);
                                }}
                                if (d.getType().equalsIgnoreCase("srs")){
                                    if(d.getStage2().equalsIgnoreCase("accepted")&d.getStage3().equalsIgnoreCase("accepted")
                                            ||d.getStage3().equalsIgnoreCase("accept_with_revision")){
                                    sharedprojects.add(d);
                                    DocumentReference doc = fs.collection("shared_projects").document(d.getSharedprojectid());
                                    doc.update("openedcreator",true);}
                                }


                            }
                            if(sharedprojects!=null) {
                                empty.setVisibility(View.GONE);
                                Collections.sort(sharedprojects);
                                Collections.reverse(sharedprojects);
                                NotificationsAdapter da = new NotificationsAdapter(sharedprojects, CreatorNotifications.this);
                                rv.setAdapter(da);
                            }
                        }

                        if(sharedprojects.isEmpty())
                            empty.setVisibility(View.VISIBLE);

                    }
                });














        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userid = fAuth.getCurrentUser().getUid();
                fs.collection("tokens").document(userid).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            DocumentReference dref = fs.collection("tokens").document(userid);
                            dref.update("token", null);
                        }
                    }
                });
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(CreatorNotifications.this, Login.class));
                finish();

            }
        });
        sponsors.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(CreatorNotifications.this, SearchSponsors.class);
                startActivity(i);
            }
        });



        profilepic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(CreatorNotifications.this, IdeaOwnerProfilePage.class);
                startActivity(i);
            }
        });

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(CreatorNotifications.this, CreatorMainProjects.class);
                startActivity(i);
            }
        });


    }
}