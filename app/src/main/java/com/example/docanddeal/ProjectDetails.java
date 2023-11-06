package com.example.docanddeal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
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

import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProjectDetails extends AppCompatActivity{
    TextView home, logout,title,desc,empty,empty1,sponsors, notification;
    CircularProgressIndicator progressBar,progressBar1;
    Button toProposal,toVision,toSRS;
    CircleImageView logo,profile;
    RecyclerView docsrv, appliedto;
    FirebaseStorage store;
    StorageReference storageReference;
    FirebaseFirestore fs;
    FirebaseAuth fAuth;
    DocumentReference dref,project;
    String userID;
    ArrayList<Document> docs;
    Bundle b1;
    ProjectC pro;
    String pid;
    String ver;
    Version p1;
    ArrayList<SharedWith> appliedtoarr;
    ArrayList<SharedProject>  shareddocs;
    CollectionReference sharedDocsRef,projectsRef,sponRef;
    TextView helptext;
    boolean helpclicked;
    ImageView arrow,helpicon;
    ImageView notired;
    boolean notiopen;
    ArrayList<Abbrev> aexplination;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_details);
        home = findViewById(R.id.homeProjectDetails);
        logout = findViewById(R.id.ProjectDetailsLogoutBtn);
        profile = findViewById(R.id.profilePicProjectDetails);
        title = findViewById(R.id.projectDetailsTitle);
        desc = findViewById(R.id.projectDetailsDesc);
        toProposal = findViewById(R.id.toProposal);
        toVision = findViewById(R.id.toVision);
        toSRS = findViewById(R.id.toSRS);
        notification = findViewById(R.id.notiProjectDetails);
        logo = findViewById(R.id.projectDetailsLogo);
        docsrv = findViewById(R.id.docRV);
        appliedto = findViewById(R.id.appliedRV);
        empty = findViewById(R.id.ifnullProjectDetails);
        progressBar = findViewById(R.id.progBarProjectDetails);
        empty1 = findViewById(R.id.ifnullProjectDetails2);
        progressBar1 = findViewById(R.id.progBarProjectDetails2);
        sponsors = findViewById(R.id.sponsorsProjectDetails);
        empty.setVisibility(View.GONE);
        notired = findViewById(R.id.newnotiCpdetails);

        b1 = getIntent().getExtras();
        pro = (ProjectC) b1.getSerializable("Project");
        title.setText(pro.getName());
        desc.setText(pro.getDescription());
        pid = pro.getPid();
        p1 = (Version) b1.getSerializable("Version");
        ver = p1.getVersion();
        Picasso.get().load(Uri.parse(pro.getLogo())).resize(50, 50).centerCrop().into(logo);

        fAuth = FirebaseAuth.getInstance();
        fs = FirebaseFirestore.getInstance();
        dref = fs.collection("users").document(fAuth.getCurrentUser().getUid());
        project = fs.collection("projects").document(pid);
        sharedDocsRef = fs.collection("shared_projects");
        projectsRef = fs.collection("projects");
        sponRef = fs.collection("users");
        userID = fAuth.getCurrentUser().getUid();
        store = FirebaseStorage.getInstance();
        storageReference = store.getReference();

        helpicon = findViewById(R.id.helpprojdetail);
        arrow = findViewById(R.id.arrowprojdetail);
        helptext = findViewById(R.id.texthelpprojdetail);
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
        notiopen = true;
        notired.setVisibility(View.GONE);
        fs.collection("shared_projects")
                .whereEqualTo("creatorID", userID)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                SharedProject d = document.toObject(SharedProject.class);
                                if (d.getStage1().equalsIgnoreCase("accepted")||d.getStage1().equalsIgnoreCase("accept_with_revision")||
                                        d.getStage1().equalsIgnoreCase("rejected")){
                                    list.add(d);
                                }


                            }
                            if(list!=null) {
                                for (SharedProject m: list){
                                    if (m.isOpenedcreator()==false){
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
                    }
                });



        docs = new ArrayList<>();
        appliedtoarr = new ArrayList<>();
        shareddocs = new ArrayList<>();
        appliedto.setHasFixedSize(true);
        RecyclerView.LayoutManager lm2 = new GridLayoutManager(this,4);
        appliedto.setLayoutManager(lm2);
        dref.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                SponsorUser user = documentSnapshot.toObject(SponsorUser.class);
                Picasso.get().load(Uri.parse(user.getImagepath())).resize(50,50).centerCrop().into(profile);
                ArrayList<String> shareddocsStrings = user.getsharedProjects();
                ArrayList<SharedWith> appliedtoarr = new ArrayList<>();
                if(shareddocsStrings==null){

                }else{
                    empty1.setVisibility(View.GONE);
                    for (String d:shareddocsStrings) {
                        sharedDocsRef.document(d).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                SharedProject sd = documentSnapshot.toObject(SharedProject.class);
                                if(sd != null) {
                                    if(sd.getPid().equals(pid)){
                                        if(sd.getProjectVersion().equals(ver)){
                                            shareddocs.add(sd);
                                            sponRef.document(sd.getSharedWith()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                                @Override
                                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                    SponsorUser p = documentSnapshot.toObject(SponsorUser.class);
                                                    SharedWith sw = new SharedWith(sd,p);
                                                    appliedtoarr.add(sw);
                                                    AppliedToAdapter spa = new AppliedToAdapter(appliedtoarr,
                                                            ProjectDetails.this,empty,pro,p1);
                                                    appliedto.setAdapter(spa);
                                                }
                                            });
                                        }

                                    }
                                }

                            }
                        });
                    }
                }

            }
        });

        RecyclerView rv = findViewById(R.id.docRV1);
        rv.setHasFixedSize(true);
        RecyclerView.LayoutManager lm = new GridLayoutManager(this,5);
        rv.setLayoutManager(lm);

        fs.collection("documents")
                .whereEqualTo("pid", pid)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                if(ver.equals(document.get("verid").toString())){
                                    Document d = document.toObject(Document.class);
                                    docs.add(d);

                                }
                                if(docs!=null) {
                                    DocumentAdapter da = new DocumentAdapter(docs, ProjectDetails.this,empty,pro,p1,"Details",null,null,pro.getLogo());
                                    rv.setAdapter(da);
                                }
                            }
                            progressBar.setVisibility(View.GONE);
                            if(docs.isEmpty())
                                empty.setVisibility(View.VISIBLE);

                        }
                    }
                });



        sponsors.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ProjectDetails.this, SearchSponsors.class);
                startActivity(i);
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
                startActivity(new Intent(ProjectDetails.this, Login.class));
                finish();

            }
        });

        toProposal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ProjectDetails.this, proposalDoc.class);
                i.putExtra("Version",p1);
                i.putExtra("Project",pro);
                //i.putExtra("pid",pid);
                startActivity(i);
            }
        });
        aexplination = new ArrayList<>();
        toVision.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ProjectDetails.this, VisionDoc.class);
                i.putExtra("Version",p1);
                i.putExtra("Project",pro);
                i.putExtra("exp",aexplination);
                //i.putExtra("pid",pid);
                startActivity(i);
            }
        });

        toSRS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ProjectDetails.this, srsdoc1.class);
                i.putExtra("Version",p1);
                i.putExtra("Project",pro);
                i.putExtra("fromDetails","fromDetails");
                startActivity(i);
            }
        });

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ProjectDetails.this, IdeaOwnerProfilePage.class);
                startActivity(i);
            }
        });

        notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ProjectDetails.this, CreatorNotifications.class);
                startActivity(i);
            }
        });


    }


    @Override
    public void onBackPressed() {
        Intent i = new Intent(ProjectDetails.this, CreatorMainVersions.class);
        i.putExtra("Project",pro);
        //i.putExtra("pid",pid);
        startActivity(i);
        super.onBackPressed();
    }


}