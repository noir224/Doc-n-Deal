package com.example.docanddeal;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
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
        SingleSharedProject ssp = (SingleSharedProject)b.getSerializable("project");
        name.setText(ssp.getProject().getName());
        desc.setText(ssp.getProject().getDescription());
        Picasso.get().load(Uri.parse(ssp.getProject().getLogo())).resize(50,50).centerCrop().into(logo);
        rv.setHasFixedSize(true);
        RecyclerView.LayoutManager lm = new LinearLayoutManager(this);
        rv.setLayoutManager(lm);
        ArrayList<Stage> stages = new ArrayList<>();
        Stage stage1 = new Stage(1, "none");
        stages.add(0,stage1);
        Stage stage2 = new Stage(2, "none");
        stages.add(1,stage2);
        Stage stage3 = new Stage(3, "none");
        stages.add(2,stage3);
        fs.collection("shared_documents").whereEqualTo("pid",ssp.getProject().getPid()).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                for (DocumentSnapshot ds :queryDocumentSnapshots) {
                    SharedDoc sdoc = ds.toObject(SharedDoc.class);
                    Stage stage;
                    if(sdoc.getType().equals("proposal")){
                        stages.get(0).setStatus(sdoc.getStage1());
                    }else if(sdoc.getType().equals("vision")){
                        stages.get(1).setStatus(sdoc.getStage1());
                    }else{
                        stages.get(2).setStatus(sdoc.getStage1());
                    }

                }
                if(stages!=null) {
                    SponsorStagesAdapter da = new SponsorStagesAdapter(stages, SharedProjectDetails.this);
                    rv.setAdapter(da);

                }
            }
        });


    }
}