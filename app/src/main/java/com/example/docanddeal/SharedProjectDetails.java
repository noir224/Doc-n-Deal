package com.example.docanddeal;

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
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class SharedProjectDetails extends AppCompatActivity {
    CircleImageView logo;
    TextView name,desc;
    RecyclerView rv;
    CircularProgressIndicator progBar;
    TextView helptext;
    boolean helpclicked;
    ImageView arrow,helpicon,colorcodeicon;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shared_project_details);
        logo = findViewById(R.id.SharedProjectDetailsLogo);
        name = findViewById(R.id.SharedProjectDetailsTitle);
        desc = findViewById(R.id.SharedProjectDetailsDesc);
        rv = findViewById(R.id.shareddocdetailsRV);
        progBar = findViewById(R.id.progBarSharedProjectDetails);
        FirebaseFirestore fs = FirebaseFirestore.getInstance();
        Bundle b = getIntent().getExtras();
        SingleSharedProject ssp = (SingleSharedProject) b.getSerializable("project");
        ssp.getSharedProjects().getPnewId();
        name.setText(ssp.getProject().getName());
        desc.setText(ssp.getProject().getDescription());

        Picasso.get().load(Uri.parse(ssp.getProject().getLogo())).resize(50, 50).centerCrop().into(logo);


        helpicon = findViewById(R.id.helpsharedprojectdetails);
        arrow = findViewById(R.id.arrowsharedprojectdetails);
        helptext = findViewById(R.id.texthelpsharedprojectdetails);
        helpclicked = false;

        helptext.setVisibility(View.GONE);
        arrow.setVisibility(View.GONE);

        helpicon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (helpclicked == false) {
                    helptext.setVisibility(View.VISIBLE);
                    arrow.setVisibility(View.VISIBLE);
                    helpclicked = true;
                } else {
                    helptext.setVisibility(View.GONE);
                    arrow.setVisibility(View.GONE);
                    helpclicked = false;
                }
            }
        });

        colorcodeicon = findViewById(R.id.colorcodeicon2);


        colorcodeicon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog customdialog= new Dialog(SharedProjectDetails.this);
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


        rv.setHasFixedSize(true);
        RecyclerView.LayoutManager lm = new LinearLayoutManager(this);
        rv.setLayoutManager(lm);
        ArrayList<Stage> stages = new ArrayList<>();
        Stage stage1 = new Stage(1, "none");
        stages.add(0, stage1);
        Stage stage2 = new Stage(2, "none");
        stages.add(1, stage2);
        Stage stage3 = new Stage(3, "none");
        stages.add(2, stage3);
        fs.collection("shared_projects").whereEqualTo("pid", ssp.getProject().getPid()).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                for (DocumentSnapshot ds : queryDocumentSnapshots) {
                    SharedProject sdoc = ds.toObject(SharedProject.class);
                    Stage stage;
                    if (sdoc.getType().equals("proposal")) {
                        stages.get(0).setStatus(sdoc.getStage1());
                    } else if (sdoc.getType().equals("vision")) {
                        stages.get(0).setStatus(sdoc.getStage1());
                        stages.get(1).setStatus(sdoc.getStage2());

                    } else {
                        stages.get(0).setStatus(sdoc.getStage1());
                        stages.get(1).setStatus(sdoc.getStage2());
                        stages.get(2).setStatus(sdoc.getStage3());
                    }

                }
                if (stages != null) {
                    SponsorStagesAdapter da = new SponsorStagesAdapter(stages, SharedProjectDetails.this, ssp);
                    rv.setAdapter(da);

                }
            }
        });
    }

    @Override
    public void onBackPressed(){
        Intent i = new Intent(SharedProjectDetails.this, SharedProjects.class);
        startActivity(i);
        super.onBackPressed();
    }


}