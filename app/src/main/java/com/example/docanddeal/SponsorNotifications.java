package com.example.docanddeal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
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
import java.util.Collections;

import de.hdodenhof.circleimageview.CircleImageView;

public class SponsorNotifications extends AppCompatActivity {
    TextView home, projectIdeas, notifications, logout,empty;
    CircleImageView profilepic;
    FirebaseStorage store;
    StorageReference storageReference;
    FirebaseFirestore fs;
    FirebaseAuth fAuth;
    String userID;
    DocumentReference dref;
    RecyclerView rv;
    ArrayList<SharedProject> sharedprojects;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sponsor_notifications);
        empty = findViewById(R.id.nullNotifSponsor);
        logout = findViewById(R.id.NotifSponsorLogoutBtnProfile);
        home = findViewById(R.id.notifhome);
        projectIdeas = findViewById(R.id.notifsharedprojects);
        notifications = findViewById(R.id.notifnotifspon);
        profilepic = findViewById(R.id.NotifSponprofilePicCProfile);
        fAuth = FirebaseAuth.getInstance();
        fs = FirebaseFirestore.getInstance();
        rv = findViewById(R.id.notifsponrv);
        dref = fs.collection("users").document(fAuth.getCurrentUser().getUid());
        userID = fAuth.getCurrentUser().getUid();
        store = FirebaseStorage.getInstance();
        storageReference = store.getReference();
        sharedprojects = new ArrayList<>();


        rv.setHasFixedSize(true);
        RecyclerView.LayoutManager lm = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
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

        fs.collection("shared_projects")
                .whereEqualTo("sharedWith", userID)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                SharedProject d = document.toObject(SharedProject.class);
                                sharedprojects.add(d);
                                DocumentReference doc = fs.collection("shared_projects").document(d.getSharedprojectid());
                                doc.update("openedsponsor",true);


                            }
                                if(sharedprojects!=null) {
                                    empty.setVisibility(View.GONE);
                                    Collections.sort(sharedprojects);
                                    Collections.reverse(sharedprojects);
                                    NotificationsAdapter da = new NotificationsAdapter(sharedprojects, SponsorNotifications.this);
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
                startActivity(new Intent(SponsorNotifications.this, Login.class));
                finish();

            }
        });
        profilepic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SponsorNotifications.this, SponsorProfile.class));

            }
        });
        projectIdeas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SponsorNotifications.this, SharedProjects.class));

            }
        });
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SponsorNotifications.this, SponsorMain.class));

            }
        });
        notifications.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SponsorNotifications.this,SponsorNotifications.class));
            }
        });

    }
}