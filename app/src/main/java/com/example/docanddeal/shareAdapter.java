package com.example.docanddeal;

import static com.google.firebase.firestore.FieldValue.serverTimestamp;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Collection;

public class shareAdapter extends RecyclerView.Adapter {
    ArrayList<SponsorUser> arr;
    Context context;
    Document doc;
    ProjectC p;
    Version v;


    public shareAdapter(ArrayList<SponsorUser> arr, Context context, Document doc, ProjectC p, Version v) {
        this.arr = arr;
        this.context = context;
        this.doc = doc;
        this.p = p;
        this.v = v;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.shareitem,parent,false);
        return new shareAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        ((shareAdapter.ViewHolder ) holder).sname.setText(arr.get(position).getUsername()+"");
        Picasso.get().load(Uri.parse(arr.get(position).getImagepath())).resize(50,50).centerCrop().into(((shareAdapter.ViewHolder ) holder).img);
        ((ViewHolder ) holder).sharebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            String type = doc.getType();
            if(type.equals("proposal"))
                sendToSponsor(type,position);
            else if(type.equals("full"))
                Toast.makeText(context,"You cannot share full document",Toast.LENGTH_LONG).show();
            else {
                String s = "You cannot apply a "+type+" document from this screen. To apply a non-proposal document, please press on the sponsor that the proposal was applied to previosly";
                new AlertDialog.Builder(context)
                        .setTitle("Save")
                        .setMessage(s)
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        }).create().show();
            }
            }
        });
        ((shareAdapter.ViewHolder ) holder).v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i= new Intent(context, CreatorSponsorProfile.class);
                i.putExtra("sponsorID",arr.get(position).getId());
                i.putExtra("from","ProjectDetails");
                context.startActivity(i);


            }
        });

    }

    @Override
    public int getItemCount() {
        return arr.size();
    }

    public void filterlist(ArrayList<SponsorUser> filteredlist){
        arr = filteredlist;
        notifyDataSetChanged();
    }

    private void sendToSponsor(String type, int position){
        FirebaseFirestore fs = FirebaseFirestore.getInstance();
        CollectionReference sharedRef;
        sharedRef = fs.collection("shared_projects");
        sharedRef.whereEqualTo("poriginalId",doc.getDocid()).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                boolean flag = true;
                if(queryDocumentSnapshots.size()==0)
                    finalsend(position);
                else{
                    for( DocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                        SharedProject sdd = documentSnapshot.toObject(SharedProject.class);
                        String currentdocid = doc.getDocid();
                        String currentsponsorid = arr.get(position).getId();
                        String sponsorid = sdd.getSharedWith();
                        String docid = sdd.getPoriginalId();
                        if(currentdocid.equals(docid) && currentsponsorid.equals(sponsorid) &&
                                !sdd.getStage1().equals("none")) {
                            String s = "You cannot apply a "+type+" document from this screen. To re-apply a proposal document, please press on the sponsor that the proposal was applied to previosly in project details screen";
                            new AlertDialog.Builder(context)
                                    .setTitle("Save")
                                    .setMessage(s)
                                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {

                                        }
                                    }).create().show();
                            flag = false;
                            break;
                        }
                    }
                    if (flag)
                        finalsend(position);

                }
            }
        });

    }


    private void finalsend(int position){
        FirebaseAuth fAuth = FirebaseAuth.getInstance();
        FirebaseFirestore fs = FirebaseFirestore.getInstance();
        DocumentReference specificdoc,sponRef,createRef;
        CollectionReference specificdocColl,sharedRef,docsCol;
        docsCol = fs.collection("documents");
        sharedRef = fs.collection("shared_projects");
        sponRef = fs.collection("users").document(arr.get(position).getId()); //shared with
        createRef = fs.collection("users").document(fAuth.getCurrentUser().getUid()); // creator info
        specificdocColl = fs.collection("proposalDoc");
        specificdoc = fs.collection("proposalDoc").document(doc.getDocid());
        specificdoc.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                ProposalDocument pd = documentSnapshot.toObject(ProposalDocument.class);
                specificdocColl.add(pd).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference dr) {
                        String time = System.currentTimeMillis()+"";
                        SharedProject sd = new SharedProject(doc.getName(), doc.getDocid(), dr.getId(),null,null,null,null, doc.getPid(), fAuth.getCurrentUser().getUid()
                                    , arr.get(position).getId(), "proposal", doc.getVerid(), "pending", "none", "none",null,null,null,null,false,false, time) ;
                        sharedRef.add(sd).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                sharedRef.document(documentReference.getId()).update("sharedprojectid",documentReference.getId());
                                docsCol.document(doc.getDocid()).update("copies", FieldValue.arrayUnion(dr.getId()));
                                sponRef.update("sharedProjects", FieldValue.arrayUnion(documentReference.getId()));
                                createRef.update("sharedProjects", FieldValue.arrayUnion(documentReference.getId()));
                                sharedRef.document(documentReference.getId()).update("time",time);
                                System.out.println(time);
                                Toast.makeText(context, "Document was shared successfully",Toast.LENGTH_LONG).show();

                                //notif
                                String sponID = arr.get(position).getId();
                                fs.collection("tokens").document(sponID)
                                        .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                    @Override
                                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                                        if(documentSnapshot.exists()){
                                            Token t = documentSnapshot.toObject(Token.class);
                                            String neededtoken = t.getToken();
                                            //String neededtoken = "eh-Uptp7QSGp0shVX8LJMC:APA91bFprNLLSlJ9JxLq4kltF0Ex8wL61tUwmRhHah_CwGUTv5c5Tlf6UmziwZxBWZNPNr34tpPDHby6bJlI9N3-VJmT7wvAqyITfchzWm5tLYxEDom2EK-G0Sc3wMhTwiWvmIUMeiW2";
                                            String doctype = sd.getType();
                                            FcmNotificationsSender notificationsSender = new FcmNotificationsSender(neededtoken,
                                                    "New "+doctype+" Document","Check out the new Document shared " +
                                                    "with you", context.getApplicationContext());
                                            notificationsSender.SendNotifications();
                                        }
                                        else{
                                            System.out.println("no spon so no token");
                                        }
                                    }
                                });

                                Intent i = new Intent(context,ProjectDetails.class);
                                i.putExtra("Project",p);
                                i.putExtra("Version",v);
                                context.startActivity(i);

                            }
                        });


                    }
                });
            }
        });
    }


    private static class ViewHolder extends RecyclerView.ViewHolder{
        ImageView img;
        TextView sname;
        Button sharebtn;
        View v;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            v = itemView;
            img = itemView.findViewById(R.id.shareimg);
            sname = (TextView)itemView.findViewById(R.id.sharesponsorname);
            sharebtn = itemView.findViewById(R.id.sharesponsorbtn);
        }
    }
}
