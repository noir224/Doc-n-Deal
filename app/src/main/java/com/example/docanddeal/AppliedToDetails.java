package com.example.docanddeal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class AppliedToDetails extends AppCompatActivity {
    RecyclerView docsRV, statusRV;
    TextView empty,textView21;
    CircularProgressIndicator progressBar;
    FirebaseFirestore fs;
    FirebaseAuth fAuth;
    ProjectC p;
    Version ver;
    SharedWith sw;
    SharedProject sp;
    String pid,v,currenttype,stage1,stage2,stage3;
    ArrayList<Document> docs;
    String currentstatus;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_applied_to_details);
        docsRV = findViewById(R.id.appliddocsRV);
        statusRV = findViewById(R.id.statusRV);
        empty = findViewById(R.id.ifnullProjectDetails);
        progressBar = findViewById(R.id.progBarProjectDetails);
        textView21 = findViewById(R.id.textView21);
        Bundle b = getIntent().getExtras();
        ver = (Version) b.getSerializable("Version");
        p = (ProjectC) b.getSerializable("Project");
        sw = (SharedWith) b.getSerializable("appliedto");
        pid = p.getPid();
        v = ver.getVersion();
        fs = FirebaseFirestore.getInstance();
        fAuth = FirebaseAuth.getInstance();
        //check current stage
        stage1 = sw.getSp().getStage1();
        stage2 = sw.getSp().getStage2();
        stage3 = sw.getSp().getStage3();
        sp = (SharedProject) sw.getSp();
        currentstatus = "";
//        if(stage3.equals("accepted") || stage3.equals("pending") || stage3.equals("accept_with_revision") || stage3.equals("rejected")){
//            currenttype ="srs";
//            currentstatus = stage3;
//        }
//        else if(stage2.equals("accepted") || stage2.equals("pending") || stage2.equals("accept_with_revision") || stage2.equals("rejected")) {
//            currenttype = "vision";
//            currentstatus = stage2;
//        }
//        else if(stage1.equals("accepted") || stage1.equals("pending") || stage1.equals("accept_with_revision") || stage2.equals("rejected")) {
//            currenttype = "proposal";
//            currentstatus = stage1;
//        }
        if(stage1.equals("pending") || stage1.equals("accept_with_revision")) {
            currenttype = "proposal";
            currentstatus = stage1;
        }
        else if (stage2.equals("pending") || stage2.equals("accept_with_revision") || stage2.equals("none")) {
            currenttype = "vision";
            currentstatus = stage2;
        }
        else if(stage3.equals("pending") || stage3.equals("accept_with_revision") || stage3.equals("none") ||
                stage3.equals("accepted")) {
            currenttype = "srs";
            currentstatus = stage3;
        }
        empty.setText("You do not have any "+currenttype+" documents for current stage");
        if(stage3.equals("accepted")){
            empty.setVisibility(View.GONE);
            docsRV.setVisibility(View.GONE);
        }
        //if(oldDoc){
        //display related docs of current stage
        if(!currentstatus.equals("rejected")) {
            docs = new ArrayList<>();
            docsRV.setHasFixedSize(true);
            RecyclerView.LayoutManager gm = new GridLayoutManager(this,5);
            docsRV.setLayoutManager(gm);
            fs.collection("documents")
                    .whereEqualTo("pid", pid)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    if(v.equals(document.get("verid").toString())){
                                        Document d = document.toObject(Document.class);
                                        if(currenttype.equals(d.getType()) && !currentstatus.equals("accepted") )
                                            docs.add(d);

                                    }
                                    if(docs!=null) {
                                        DocumentAdapter da = new DocumentAdapter(docs, AppliedToDetails.this,empty,p,ver,"AppliedToDetails",sw.getSponsor(),sw,null);
                                        docsRV.setAdapter(da);
                                    }
                                }
                                progressBar.setVisibility(View.GONE);
                                if(docs.isEmpty())
                                    empty.setVisibility(View.VISIBLE);

                            }
                        }
                    });
        }else {
            docsRV.setVisibility(View.GONE);
            textView21.setVisibility(View.GONE);
        }
//        }else{
//            DocumentReference newdoc;
//            if(currenttype.equals("proposal"))
//                newdoc = fs.collection("documents").document(sw.getSp().getPoriginalId());
//            else if(currenttype.equals("vision"))
//                newdoc = fs.collection("documents").document(sw.getSp().getVoriginalId());
//            else
//                newdoc = fs.collection("documents").document(sw.getSp().getSoriginalId());
//            newdoc.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
//                    @Override
//                    public void onSuccess(DocumentSnapshot documentSnapshot) {
//                        Document d = documentSnapshot.toObject(Document.class);
//                        ArrayList<String> newids = d.getCopies();
//
//                    }
//                });
//        }


        // status rv
        statusRV.setHasFixedSize(true);
        RecyclerView.LayoutManager lm = new LinearLayoutManager(AppliedToDetails.this);
        statusRV.setLayoutManager(lm);

        ArrayList<Stage> stages = new ArrayList<>();
        stages.add(new Stage(1, stage1));
        stages.add(new Stage(2, stage2));
        stages.add(new Stage(3, stage3));
        if(stages!=null) {
            StatusAdapter da = new StatusAdapter(stages, AppliedToDetails.this, sp, p, ver);
            statusRV.setAdapter(da);
        }

        //add a code to re-apply
        //take from bundle and resend
    }
}