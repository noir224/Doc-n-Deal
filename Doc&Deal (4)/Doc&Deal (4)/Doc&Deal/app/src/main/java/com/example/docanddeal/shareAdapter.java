package com.example.docanddeal;

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

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

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
            sendToSponsor(type,position);
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
        FirebaseAuth fAuth = FirebaseAuth.getInstance();
        FirebaseFirestore fs = FirebaseFirestore.getInstance();
        DocumentReference specificdoc,sponRef,createRef;
        CollectionReference specificdocColl,sharedRef;
        sharedRef = fs.collection("shared_documents");
        sponRef = fs.collection("users").document(arr.get(position).getId()); //shared with
        createRef = fs.collection("users").document(fAuth.getCurrentUser().getUid()); // creator info
        if(type.equals("full"))
            Toast.makeText(context,"You cannot share full document",Toast.LENGTH_LONG).show();
        else{
            sharedPreviously(position,type);
//            if(sharedPreviously(position,type)){
//                Toast.makeText(context,"You already shared this document with this sponsor",Toast.LENGTH_LONG).show();
//            }else{
//                //passed previous stages -> send
//
//                if(projPassesStage(position, type)==false)
//                    Toast.makeText(context,"You haven't passed previous stage yet",Toast.LENGTH_LONG).show();
//                else{
//                    String collectionname = type+"Doc";
//                    specificdocColl = fs.collection(collectionname);
//                    specificdoc = fs.collection(collectionname).document(doc.getDocid());
//                    specificdoc.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
//                        @Override
//                        public void onSuccess(DocumentSnapshot documentSnapshot) {
//                            specificdocColl.add(documentSnapshot).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
//                                @Override
//                                public void onSuccess(DocumentReference documentReference) {
//                                    SharedDoc sd;
//                                    if(type.equals("proposal")) {
//                                        sd = new SharedDoc(doc.getDocid(), documentReference.getId(), doc.getPid(), fAuth.getCurrentUser().getUid()
//                                                , arr.get(position).getId(), type, doc.getVerid(), "pending", "none", "none");
//                                    }else if(type.equals("vision")){
//                                        sd = new SharedDoc(doc.getDocid(), documentReference.getId(), doc.getPid(), fAuth.getCurrentUser().getUid()
//                                                , arr.get(position).getId(), type, doc.getVerid(), "passed", "pending", "none");
//                                    }
//                                    else{
//                                        sd = new SharedDoc(doc.getDocid(), documentReference.getId(), doc.getPid(), fAuth.getCurrentUser().getUid()
//                                                , arr.get(position).getId(), type, doc.getVerid(), "passed", "passed", "pending");
//                                    }
//                                        sharedRef.add(sd).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
//                                            @Override
//                                            public void onSuccess(DocumentReference documentReference) {
//                                                sponRef.update("sharedDocs", FieldValue.arrayUnion(documentReference.getId()));
//                                                createRef.update("sharedDocs", FieldValue.arrayUnion(documentReference.getId()));
//                                                Toast.makeText(context, "Document was shared successfully",Toast.LENGTH_LONG).show();
//                                            }
//                                        });
//
//
//
//
//
//                                }
//                            });
//                        }
//                    });
//                }
//            }
        }


    }
    private void sharedPreviously(int position,String type){
        FirebaseAuth fAuth = FirebaseAuth.getInstance();
        FirebaseFirestore fs = FirebaseFirestore.getInstance();
        DocumentReference sponRef,createRef;
        CollectionReference sharedRef;
        sharedRef = fs.collection("shared_documents");
        sponRef = fs.collection("users").document(arr.get(position).getId()); //shared with
        sponRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                //setBooleansharedPreviously(false);
                SponsorUser su = documentSnapshot.toObject(SponsorUser.class);
                //add if nulll statement
                ArrayList<String> sdoc = su.getSharedDocs();
                if(sdoc==null)
                    finalsend(type,position);
                   // setBooleansharedPreviously(false);
                else{
                    for(String s : sdoc){
                        sharedRef.document(s).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                SharedDoc sdd = documentSnapshot.toObject(SharedDoc.class);
                                String currentdocid = doc.getDocid();
                                String currentsponsorid = arr.get(position).getId();
                                String sponsorid = sdd.getSharedWith();
                                String docid = sdd.getOriginalId();
                                if(type.equals("proposal")){
                                    if(currentdocid.equals(docid) && currentsponsorid.equals(sponsorid) &&
                                            !sdd.getStage1().equals("none"))
                                       // setBooleansharedPreviously(true);
                                        Toast.makeText(context,"You already shared this document with this sponsor",Toast.LENGTH_LONG).show();
                                    else
                                        projPassesStage(position, type);
                                }else if(type.equals("vision")){
                                    if(currentdocid.equals(docid) && currentsponsorid.equals(sponsorid) &&
                                            !sdd.getStage2().equals("none"))
                                       // setBooleansharedPreviously(true);
                                        Toast.makeText(context,"You already shared this document with this sponsor",Toast.LENGTH_LONG).show();
                                    else
                                        projPassesStage(position, type);
                                }else if(type.equals("srs")){
                                    if(currentdocid.equals(docid) && currentsponsorid.equals(sponsorid) &&
                                            !sdd.getStage3().equals("none"))
                                        Toast.makeText(context,"You already shared this document with this sponsor",Toast.LENGTH_LONG).show();
                                        //setBooleansharedPreviously(true);
                                    else
                                        projPassesStage(position, type);
                                }

                            }
                        });
                    }
                }
            }
        });
//        System.out.println("///+++" +test);
//        return sharedPreviously;
    }



    private void projPassesStage(int position,String type){
        FirebaseAuth fAuth = FirebaseAuth.getInstance();
        FirebaseFirestore fs = FirebaseFirestore.getInstance();
        DocumentReference specificdoc,sponRef,createRef;
        CollectionReference specificdocColl,sharedRef;
        sharedRef = fs.collection("shared_documents");
        sponRef = fs.collection("users").document(arr.get(position).getId()); //shared with
        createRef = fs.collection("users").document(fAuth.getCurrentUser().getUid()); // creator info
        createRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                CreatorUser su = documentSnapshot.toObject(CreatorUser.class);
                ArrayList<String> sdoc = su.getSharedDocs();
                if(sdoc == null) {
                    //booleanStageStatus(true);
                    finalsend(type, position);

                }
                else{
                    for(String s : sdoc){
                        sharedRef.document(s).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                SharedDoc sdd = documentSnapshot.toObject(SharedDoc.class);
                                if(doc.getDocid().equals(sdd.getOriginalId())&&
                                        arr.get(position).getId().equals(sdd.getSharedWith())){
                                    String stage1 = sdd.getStage1();
                                    String stage2 = sdd.getStage2();
                                    if(type.equals("proposal")) {
                                        //booleanStageStatus(true);
                                        finalsend(type, position);
                                    }
                                    else if(type.equals("vision")){
                                        if(!stage1.equals("passed"))
                                            //booleanStageStatus(false);
                                            Toast.makeText(context,"You haven't passed previous stage yet",Toast.LENGTH_LONG).show();
                                        else
                                            finalsend(type, position);
                                    }else if(type.equals("srs")){
                                        if(!stage2.equals("passed"))
                                            //booleanStageStatus(false);
                                            Toast.makeText(context,"You haven't passed previous stage yet",Toast.LENGTH_LONG).show();
                                        else
                                            finalsend(type, position);
                                    }
                                }
//                                else
//                                    finalsend(type, position);

                            }
                        });
                    }
                }
            }
        });
//        System.out.println("////"+test);
//        return stageStatus;
    }



    private void finalsend(String type,int position){
        FirebaseAuth fAuth = FirebaseAuth.getInstance();
        FirebaseFirestore fs = FirebaseFirestore.getInstance();
        DocumentReference specificdoc,sponRef,createRef;
        CollectionReference specificdocColl,sharedRef;
        sharedRef = fs.collection("shared_documents");
        sponRef = fs.collection("users").document(arr.get(position).getId()); //shared with
        createRef = fs.collection("users").document(fAuth.getCurrentUser().getUid()); // creator info
        String collectionname = type+"Doc";
        specificdocColl = fs.collection(collectionname);
        specificdoc = fs.collection(collectionname).document(doc.getDocid());
        specificdoc.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                specificdocColl.add(documentSnapshot).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        SharedDoc sd;
                        if(type.equals("proposal")) {
                            sd = new SharedDoc(doc.getDocid(), documentReference.getId(), doc.getPid(), fAuth.getCurrentUser().getUid()
                                    , arr.get(position).getId(), type, doc.getVerid(), "pending", "none", "none");
                        }else if(type.equals("vision")){
                            sd = new SharedDoc(doc.getDocid(), documentReference.getId(), doc.getPid(), fAuth.getCurrentUser().getUid()
                                    , arr.get(position).getId(), type, doc.getVerid(), "passed", "pending", "none");
                        }
                        else{
                            sd = new SharedDoc(doc.getDocid(), documentReference.getId(), doc.getPid(), fAuth.getCurrentUser().getUid()
                                    , arr.get(position).getId(), type, doc.getVerid(), "passed", "passed", "pending");
                        }
                        sharedRef.add(sd).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                sponRef.update("sharedDocs", FieldValue.arrayUnion(documentReference.getId()));
                                createRef.update("sharedDocs", FieldValue.arrayUnion(documentReference.getId()));
                                Toast.makeText(context, "Document was shared successfully",Toast.LENGTH_LONG).show();
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
