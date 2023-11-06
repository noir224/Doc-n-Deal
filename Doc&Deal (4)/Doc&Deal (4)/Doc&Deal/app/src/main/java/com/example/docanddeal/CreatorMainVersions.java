package com.example.docanddeal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

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
import java.util.Calendar;

import de.hdodenhof.circleimageview.CircleImageView;

public class CreatorMainVersions extends AppCompatActivity {
    TextView home, logout,title,desc,sponsors;
    Button newVersion;
    CircleImageView logo,profile;
    RecyclerView versions;
    FirebaseStorage store;
    StorageReference storageReference;
    FirebaseFirestore fs;
    FirebaseAuth fAuth;
    DocumentReference dref,dref1;
    CollectionReference cref;
    String userID;
    CircularProgressIndicator progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creator_main_versions);
        home = findViewById(R.id.homeProjectVersions);
        logout = findViewById(R.id.ProjectVersionsLogoutBtn);
        profile = findViewById(R.id.profilePicProjectVersions);
        logo = findViewById(R.id.projectVersionsLogo);
        title = findViewById(R.id.projectVersionsTitle);
        desc = findViewById(R.id.projectVersionsDesc);
        versions = findViewById(R.id.versionsRV);
        newVersion = findViewById(R.id.newVersion);
        progressBar = findViewById(R.id.progBarVersions);
        sponsors = findViewById(R.id.sponsorsProjectVersions);

        versions.setHasFixedSize(true);
        RecyclerView.LayoutManager lm = new GridLayoutManager(this,5);
        versions.setLayoutManager(lm);
        fAuth = FirebaseAuth.getInstance();
        fs = FirebaseFirestore.getInstance();
        dref = fs.collection("users").document(fAuth.getCurrentUser().getUid());
        userID = fAuth.getCurrentUser().getUid();
        store = FirebaseStorage.getInstance();
        storageReference = store.getReference();

        Bundle b = getIntent().getExtras();
        ProjectC p = (ProjectC) b.getSerializable("Project");
        String pid = p.getPid();
        title.setText(p.getName());
        desc.setText(p.getDescription());
        Picasso.get().load(Uri.parse(p.getLogo())).resize(50,50).centerCrop().into(logo);
        ArrayList<Version> versionslist = p.getVersions();
        dref1 = fs.collection("projects").document(pid);

        dref.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot ds = task.getResult();
                    Picasso.get().load(Uri.parse(ds.getString("imagepath"))).resize(50,50).centerCrop().into(profile);
                }
            }
        });



            VersionAdapter da = new VersionAdapter(versionslist, CreatorMainVersions.this,p);
            versions.setAdapter(da);
            progressBar.setVisibility(View.GONE);


        /*fs.collection("projects").document(p.getProjid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                        ProjectC p = document.toObject(ProjectC.class);
                        p.setProjid(document.getId());
                        ArrayList<Version> verfromdb = p.getVersions();
                        for(int i=0;i<verfromdb.size();i++) {
                            verfromdb.get(i).setPid(document.getId());
                            versionslist.add(verfromdb.get(i));

                        }
                        System.out.println("///"+versionslist.get(0).getVersion());
                        if(versionslist!=null) {
                            VersionAdapter da = new VersionAdapter(versionslist, CreatorMainVersions.this);
                            versions.setAdapter(da);
                        }
            }
        }
        });
        */
                /*.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                                //Date date1=new SimpleDateFormat("dd/MM/yyyy").parse(d);
                                //double ver = Double.parseDouble(document.get("version").toString());
                              //  Version v1 = new Version(v,d,p.getProjid());
                              //  versionslist.add(v1);


                            }

                        }
                    }
                });*/


        final AlertDialog.Builder builder = new AlertDialog.Builder(CreatorMainVersions.this);
        newVersion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                builder.setTitle("New Version");
                final EditText ver = new EditText(CreatorMainVersions.this);
                ver.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);

                builder.setView(ver);
                builder.setCancelable(false).setPositiveButton("Create", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String sver = ver.getText().toString();
                        if(sver.equals(""))
                            Toast.makeText(CreatorMainVersions.this,"Please add a version number",Toast.LENGTH_LONG).show();
                        else {
                            String sverformat = ver.getText().toString();
                            boolean flag=true;
                            for(int i=0;i<sverformat.length();i++){
                                char vchar = sverformat.charAt(i);
                                if(!Character.isDigit(vchar) && vchar !='.')
                                    flag = false;
                            }
                            if(flag){
                                double dver = Double.parseDouble(sver);
                                int size = p.getVersions().size();
                                double currentver = Double.parseDouble(p.getVersions().get(size-1).getVersion());
                                if(dver<currentver) {
                                    Toast.makeText(CreatorMainVersions.this,"You cannot create a lower version",Toast.LENGTH_LONG).show();
                                }else {
                                    Calendar c = Calendar.getInstance();
                                    final int year = c.get(Calendar.YEAR);
                                    final int month = c.get(Calendar.MONTH) + 1;
                                    final int day = c.get(Calendar.DAY_OF_MONTH);
                                    String date = day + "/" + month + "/" + year;
                                    Version newver = new Version(sver,date);
                                    versionslist.add(newver);
                                    dref1.update("versions",versionslist).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            VersionAdapter da = new VersionAdapter(versionslist, CreatorMainVersions.this,p);
                                            versions.setAdapter(da);
                                        }
                                    });
                                }
                            }else{
                                Toast.makeText(CreatorMainVersions.this,"You need a number for the version",Toast.LENGTH_LONG).show();
                            }

                        }
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                AlertDialog ad = builder.create();
                ad.show();

            }
        });


        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(CreatorMainVersions.this, Login.class));
                finish();

            }
        });
        sponsors.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(CreatorMainVersions.this, SearchSponsors.class);
                startActivity(i);
            }
        });


        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(CreatorMainVersions.this, IdeaOwnerProfilePage.class);
                startActivity(i);
            }
        });
    }
    @Override
    public void onBackPressed() {
        Intent i = new Intent(CreatorMainVersions.this, CreatorMainProjects.class);
        startActivity(i);
        super.onBackPressed();
    }
}