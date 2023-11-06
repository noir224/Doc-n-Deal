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
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
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

import java.util.ArrayList;

public class IdeaOwnerProfilePage extends AppCompatActivity {
    TextView username,field,email,phone,logout,home,empty,sponsors;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String userID;
    Button edit;
    ImageView IOImage, sideimg;
    Uri IOimageurl;
    FirebaseFirestore fs;
    DocumentReference dr;
    FirebaseStorage store;
    StorageReference storageReference;
    DocumentReference doc;
    ArrayList<ProjectC> PublicProjects;
    CircularProgressIndicator progressBar;
    TextView helptext;
    boolean helpclicked;
    ImageView arrow,helpicon;
    ImageView notired;
    boolean notiopen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_idea_owner_profile_page);
        home = findViewById(R.id.homeProfile);
        logout = findViewById(R.id.creatorLogoutBtnProfile);
        IOImage = findViewById(R.id.IOprofilePicProfile);
        sideimg = findViewById(R.id.profilePicCProfile);
        username = findViewById(R.id.IOuserName);
        field = findViewById(R.id.IOfield);
        email = findViewById(R.id.IOemail);
        phone = findViewById(R.id.IOphoneNumber);
        edit = findViewById(R.id.creatorEdit);
        empty = findViewById(R.id.ifnullProfile);
        progressBar = findViewById(R.id.progBarProfile);
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        userID = fAuth.getCurrentUser().getUid();
        store = FirebaseStorage.getInstance();
        storageReference = store.getReference();
        doc = fStore.collection("users").document(userID);
        sponsors = findViewById(R.id.sponsorsProfile);
        PublicProjects=new ArrayList<>();
        notired = findViewById(R.id.newnotiCprofile);
        fs = FirebaseFirestore.getInstance();

        RecyclerView r = findViewById(R.id.RVPublicPro);
        r.setHasFixedSize(true);
        RecyclerView.LayoutManager lm = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        r.setLayoutManager(lm);


        helpicon = findViewById(R.id.helpCprof);
        arrow = findViewById(R.id.arrowCprof);
        helptext = findViewById(R.id.texthelpCprof);
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




        fStore.collection("users").document(userID).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                ArrayList<String> ppids = ((ArrayList<String>) documentSnapshot.get("projects"));
                if(ppids==null)
                    empty.setVisibility(View.VISIBLE);
                else{
                    if(ppids.isEmpty()){
                        empty.setVisibility(View.VISIBLE);
                    }else{
                        progressBar.setVisibility(View.GONE);
                        for (String id : ppids){
                            fStore.collection("projects").document(id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                    ProjectC p = documentSnapshot.toObject(ProjectC.class);
                                    if(p.getIsprivate().equals("false")){
                                        PublicProjects.add(p);
                                    }
                                    if(PublicProjects!=null) {
                                        ProejctsAdapter da = new ProejctsAdapter(PublicProjects, IdeaOwnerProfilePage.this,empty,"Profile",null);
                                        r.setAdapter(da);
                                        empty.setVisibility(View.GONE);

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

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });



        doc.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    /*  s = task.getResult().getString("type");
                      if ((s.compareTo("sponsor") == 0)) {

                       }*/
                    SpannableString p = new SpannableString(phone.getText().toString());
                    p.setSpan(new UnderlineSpan(), 0, p.length(), 0);
                    phone.setText(p);
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

                    DocumentSnapshot ds = task.getResult();
                    phone.setText(ds.getString("phone"));
                    email.setText(ds.getString("email"));
                    field.setText(ds.getString("field"));
                    username.setText(ds.getString("username"));
                    Picasso.get().load(Uri.parse(ds.getString("imagepath"))).resize(50,50).centerCrop().into(sideimg);
                    Picasso.get().load(Uri.parse(ds.getString("imagepath"))).resize(50,50).centerCrop().into(IOImage);
                }
            }
        });
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(IdeaOwnerProfilePage.this, IdeaOwnerEditProfile.class);
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
                startActivity(new Intent(IdeaOwnerProfilePage.this, Login.class));
                finish();

            }
        });
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(IdeaOwnerProfilePage.this, CreatorMainProjects.class));
            }
        });
        sponsors.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(IdeaOwnerProfilePage.this, SearchSponsors.class);
                startActivity(i);
            }
        });


    }
}