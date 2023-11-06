package com.example.docanddeal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class SharedProjects extends AppCompatActivity {
    CircleImageView profilepic;
    TextView home,notification,logout,empty;
    RecyclerView rv;
    CircularProgressIndicator progBar;
    CollectionReference sharedDocsRef,projectsRef;
    FirebaseFirestore fs;
    FirebaseAuth fAuth;
    DocumentReference dref;
    String userID;
    FirebaseStorage store;
    StorageReference storageReference;
    ArrayList<SingleSharedProject> projects;
    ArrayList<SharedDoc>  shareddocs;
    ArrayList<String>shareddocsStrings;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shared_projects);
        profilepic = findViewById(R.id.profilePicCSharedProjects);
        home = findViewById(R.id.homeSharedProjects);
        notification = findViewById(R.id.NotiSharedProjects);
        logout = findViewById(R.id.SponsorLogoutBtnSharedProjects);
        empty = findViewById(R.id.nullSharedProjects);
        rv = findViewById(R.id.SharedProjectsrv);
        progBar = findViewById(R.id.progBarSharedProjects);

        fAuth = FirebaseAuth.getInstance();
        userID = fAuth.getCurrentUser().getUid();
        fs = FirebaseFirestore.getInstance();
        dref = fs.collection("users").document(userID);
        store = FirebaseStorage.getInstance();
        storageReference = store.getReference();
        sharedDocsRef = fs.collection("shared_documents");
        projectsRef = fs.collection("projects");
        projects= new ArrayList<>();
        shareddocs = new ArrayList<>();
        shareddocsStrings = new ArrayList<>();


        rv.setHasFixedSize(true);
        RecyclerView.LayoutManager lm = new GridLayoutManager(this,5);
        rv.setLayoutManager(lm);
        //get arraylist of shared docs id & user info
        dref.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                CreatorUser user = documentSnapshot.toObject(CreatorUser.class);
                Picasso.get().load(Uri.parse(user.getImagepath())).resize(50,50).centerCrop().into(profilepic);
                shareddocsStrings = user.getSharedDocs();
                ArrayList<String> pids = new ArrayList<>();
                //go through ids
                if(shareddocsStrings==null){
                    empty.setVisibility(View.VISIBLE);
                }else{
                    for (String d:shareddocsStrings) {
                        sharedDocsRef.document(d).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                SharedDoc sd = documentSnapshot.toObject(SharedDoc.class);
                                shareddocs.add(sd);
                                projectsRef.document(sd.getPid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                    @Override
                                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                                        if(pids.contains(sd.getPid())){
                                            int pos = findProject(sd.getPid(),projects);
                                            projects.get(pos).getSharedDocs().add(sd);
                                        }else{
                                            ProjectC p = documentSnapshot.toObject(ProjectC.class);
                                            ArrayList<SharedDoc> sdarr = new ArrayList<>();
                                            sdarr.add(sd);
                                            SingleSharedProject ssd = new SingleSharedProject(p,sdarr,null);
                                            projects.add(ssd);
                                        }
                                        ArrayList<SingleSharedProject> ssprojs = getAllVersions(projects);
                                        SharedProjectsAdapter spa = new SharedProjectsAdapter(ssprojs,SharedProjects.this,empty);
                                        rv.setAdapter(spa);
                                        empty.setVisibility(View.GONE);
                                    }
                                });
                            }
                        });
                    }
                }

            }
        });


        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(SharedProjects.this, Login.class));
                finish();

            }
        });
        profilepic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SharedProjects.this, SponsorProfile.class);
                startActivity(i);
            }
        });
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SharedProjects.this, SponsorMain.class);
                startActivity(i);
            }
        });

    }

    private int findProject(String pid,ArrayList<SingleSharedProject> arr){
        for (int i=0;i<arr.size();i++) {
            if(arr.get(i).getProject().getPid().equals(pid))
                return i;
        }
        return -1;
    }
    private ArrayList<SingleSharedProject> getAllVersions(ArrayList<SingleSharedProject> arr){
        ArrayList<SingleSharedProject> sspArr= new ArrayList<>();
        for(int i=0;i<arr.size();i++){
            ArrayList<SharedDoc> docs = arr.get(i).getSharedDocs();
            ArrayList<String> versions = new ArrayList<>();
            for(int j=0; j<docs.size();j++){
                if(versions.contains(docs.get(j).getProjectVersion()))
                    continue;
                else{
                    versions.add(docs.get(j).getProjectVersion());
                }
            }
            for(String v : versions){
                ArrayList<SharedDoc> docsOfVer = new ArrayList<>();
                for(int j=0; j<docs.size();j++){
                    if(v.equals(docs.get(i).getProjectVersion()))
                        docsOfVer.add(docs.get(i));
                }
                SingleSharedProject ssp = new SingleSharedProject(arr.get(i).getProject(),docsOfVer,v);
                sspArr.add(ssp);
            }
        }
        return sspArr;
    }
}