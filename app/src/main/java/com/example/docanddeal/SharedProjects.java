package com.example.docanddeal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
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
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class SharedProjects extends AppCompatActivity {
    CircleImageView profilepic;
    TextView home,notification,logout,empty1, empty2, empty3, empty4;
    RecyclerView rv,rv2,rv3,rv4;
    CircularProgressIndicator progBar1, progBar2,progBar3,progBar4;
    CollectionReference sharedDocsRef,projectsRef;
    FirebaseFirestore fs;
    FirebaseAuth fAuth;
    DocumentReference dref;
    String userID;
    FirebaseStorage store;
    StorageReference storageReference;
    ArrayList<SingleSharedProject> projects1;
    ArrayList<SingleSharedProject> projects2;
    ArrayList<SingleSharedProject> projects3;
    ArrayList<SingleSharedProject> projects4;
    ArrayList<String>shareddocsStrings;
    ImageView helpicon, colorcodeicon;
    boolean notiopen;
    ImageView notired;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shared_projects);
        profilepic = findViewById(R.id.profilePicCSharedProjects);
        home = findViewById(R.id.homeSharedProjects);
        notification = findViewById(R.id.NotiSharedProjects);
        logout = findViewById(R.id.SponsorLogoutBtnSharedProjects);
        empty1 = findViewById(R.id.nullSharedProjects);
        empty2=findViewById(R.id.null91);
        empty3=findViewById(R.id.null92);
        empty4=findViewById(R.id.null922);
        rv = findViewById(R.id.newProjectsrv);
        rv2=findViewById(R.id.InProgressProjectsrv);
        rv3=findViewById(R.id.FinishedProjectsrv);
        rv4=findViewById(R.id.RejectedProjectsrv);
        progBar1 = findViewById(R.id.progBarNew);
        progBar2=findViewById(R.id.progBarInprog);
        progBar3= findViewById(R.id.progBarFinished);
        progBar4= findViewById(R.id.progBarRejected);
        notired = findViewById(R.id.newnotiSStatus);

        fAuth = FirebaseAuth.getInstance();
        userID = fAuth.getCurrentUser().getUid();
        fs = FirebaseFirestore.getInstance();
        dref = fs.collection("users").document(userID);
        store = FirebaseStorage.getInstance();
        storageReference = store.getReference();
        sharedDocsRef = fs.collection("shared_projects");
        projectsRef = fs.collection("projects");
        projects1= new ArrayList<>();
        projects2 = new ArrayList<>();
        projects3 = new ArrayList<>();
        projects4 = new ArrayList<>();

        shareddocsStrings = new ArrayList<>();

        ArrayList<SharedProject> list = new ArrayList<>();
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


        rv.setHasFixedSize(true);
        RecyclerView.LayoutManager lm = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        rv.setLayoutManager(lm);

        rv2.setHasFixedSize(true);
        RecyclerView.LayoutManager lm2 = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        rv2.setLayoutManager(lm2);

        rv3.setHasFixedSize(true);
        RecyclerView.LayoutManager lm3 = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        rv3.setLayoutManager(lm3);

        rv4.setHasFixedSize(true);
        RecyclerView.LayoutManager lm4 = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        rv4.setLayoutManager(lm4);
        //get arraylist of shared docs id & user info
        dref.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                CreatorUser user = documentSnapshot.toObject(CreatorUser.class);
                Picasso.get().load(Uri.parse(user.getImagepath())).resize(50,50).centerCrop().into(profilepic);
                shareddocsStrings = user.getsharedProjects();
                ArrayList<String> pids = new ArrayList<>();
                //go through ids
                if(shareddocsStrings==null){

                }else{
                    for (String d:shareddocsStrings) {
                        sharedDocsRef.document(d).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                SharedProject sd = documentSnapshot.toObject(SharedProject.class);
                                projectsRef.document(sd.getPid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                    @Override
                                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                                        ProjectC p = documentSnapshot.toObject(ProjectC.class);
                                        Version ver =new Version();
                                        ver.setVersion(sd.getProjectVersion());
                                        SingleSharedProject ssd = new SingleSharedProject(p,sd,ver);
                                        if(sd.getType().equals("proposal")){
                                            if(sd.getStage1().equals("pending")){
                                                projects1.add(ssd);
                                            }
                                            else if(sd.getStage1().equals("accept_with_revision")){
                                                projects1.add(ssd);
                                            }
                                            else if(sd.getStage1().equals("accepted")){
                                                projects2.add(ssd);
                                            }
                                            else if(sd.getStage1().equals("rejected")){
                                                projects4.add(ssd);
                                            }
                                        }
                                        else if(sd.getType().equals("vision")){
                                            if(sd.getStage2().equals("pending")) {
                                                projects2.add(ssd);
                                            }
                                            else if(sd.getStage2().equals("accept_with_revision")){
                                                projects2.add(ssd);
                                            }
                                            else if(sd.getStage2().equals("accepted")){
                                                projects2.add(ssd);
                                            }
                                        }
                                        else if(sd.getType().equals("srs")){
                                            if(sd.getStage3().equals("pending")){
                                                projects2.add(ssd);
                                            }
                                            else if(sd.getStage3().equals("accept_with_revision")){
                                                projects2.add(ssd);
                                            }
                                            else if(sd.getStage3().equals("accepted")){
                                                projects3.add(ssd);
                                            }
                                        }
                                        if(projects1.size() != 0){
                                            empty1.setVisibility(View.GONE);
                                            progBar1.setVisibility(View.GONE);
                                            SharedProjectsAdapter spa = new SharedProjectsAdapter(projects1,SharedProjects.this,empty1);
                                            rv.setAdapter(spa);
                                        }
                                        else{
                                            empty1.setVisibility(View.VISIBLE);
                                            progBar1.setVisibility(View.GONE);
                                        }
                                        if(projects2.size() != 0){
                                            empty2.setVisibility(View.GONE);
                                            progBar2.setVisibility(View.GONE);
                                            SharedProjectsAdapter spa = new SharedProjectsAdapter(projects2,SharedProjects.this,empty2);
                                            rv2.setAdapter(spa);
                                        }
                                        else{
                                            empty2.setVisibility(View.VISIBLE);
                                            progBar2.setVisibility(View.GONE);
                                        }
                                        if(projects3.size() != 0){
                                            empty3.setVisibility(View.GONE);
                                            progBar3.setVisibility(View.GONE);
                                            SharedProjectsAdapter spa = new SharedProjectsAdapter(projects3,SharedProjects.this,empty3);
                                            rv3.setAdapter(spa);
                                        }
                                        else{
                                            empty3.setVisibility(View.VISIBLE);
                                            progBar3.setVisibility(View.GONE);
                                        }
                                        if(projects4.size() != 0){
                                            empty4.setVisibility(View.GONE);
                                            progBar4.setVisibility(View.GONE);
                                            SharedProjectsAdapter spa = new SharedProjectsAdapter(projects4,SharedProjects.this,empty4);
                                            rv4.setAdapter(spa);
                                        }
                                        else{
                                            empty4.setVisibility(View.VISIBLE);
                                            progBar4.setVisibility(View.GONE);
                                        }

                                    }
                                });

                            }
                        });
                    }
                }

            }
        });


        helpicon = findViewById(R.id.help);
        helpicon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog customdialog= new Dialog(SharedProjects.this);
                customdialog.getWindow().setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
                customdialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                customdialog.getWindow().getAttributes().windowAnimations
                        = android.R.style.Animation_Dialog;
                customdialog.setContentView(R.layout.help_dialog);
                Button ok = customdialog.findViewById(R.id.okButton);
                ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        customdialog.cancel();
                    }
                });
                customdialog.show();
            }
        });
/*
        colorcodeicon = findViewById(R.id.colorcodeicon1);

        colorcodeicon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog customdialog= new Dialog(SharedProjects.this);
                customdialog.getWindow().setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
                customdialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                customdialog.getWindow().getAttributes().windowAnimations
                        = android.R.style.Animation_Dialog;
                customdialog.setContentView(R.layout.color_dialog);
                Button ok = customdialog.findViewById(R.id.okButton1);
                ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        customdialog.cancel();
                    }
                });
                customdialog.show();
            }
        });
*/

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fs.collection("tokens").document(userID).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            DocumentReference dref = fs.collection("tokens").document(userID);
                            dref.update("token", null);
                        }
                    }
                });
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
        notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SharedProjects.this, SponsorNotifications.class));

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
//    private ArrayList<SingleSharedProject> getAllVersions(ArrayList<SingleSharedProject> arr){
//        ArrayList<SingleSharedProject> sspArr= new ArrayList<>();
//        for(int i=0;i<arr.size();i++){
//            ArrayList<SharedProject> docs = arr.get(i).getSharedProjects();
//            ArrayList<String> versions = new ArrayList<>();
//            for(int j=0; j<docs.size();j++){
//                if(versions.contains(docs.get(j).getProjectVersion()))
//                    continue;
//                else{
//                    versions.add(docs.get(j).getProjectVersion());
//                }
//            }
//            for(String v : versions){
//                ArrayList<SharedProject> docsOfVer = new ArrayList<>();
//                if(v.equals(docs.get(i).getProjectVersion())) {
//                    docsOfVer.add(docs.get(i));
//                }
//               /* for(int j=0; j<docs.size();j++){
//
//                }*/
//                SingleSharedProject ssp = new SingleSharedProject(arr.get(i).getProject(),docsOfVer,v);
//                sspArr.add(ssp);
//            }
//        }
//        return sspArr;
//    }
}