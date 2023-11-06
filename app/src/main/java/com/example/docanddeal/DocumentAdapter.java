package com.example.docanddeal;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.protobuf.Timestamp;


import java.sql.Time;
import java.util.ArrayList;

public class DocumentAdapter extends RecyclerView.Adapter {
    ArrayList<Document> arr;
    Context context;
    TextView empty;
    ProjectC p;
    Version ver;
    String from;
    SponsorUser spon;
    SharedWith sw;
    String logourl;

    public DocumentAdapter(ArrayList<Document> arr, Context context, TextView empty, ProjectC p, Version ver, String from, SponsorUser spon, SharedWith sw, String logourl) {
        this.arr = arr;
        this.context = context;
        this.empty = empty;
        this.p = p;
        this.ver = ver;
        this.from = from;
        this.spon = spon;
        this.sw = sw;
        this.logourl = logourl;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.doc_item,parent,false);
        return new DocumentAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        ((DocumentAdapter.ViewHolder ) holder).sname.setText(arr.get(position).getName()+"");
        ((DocumentAdapter.ViewHolder ) holder).v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String type = arr.get(position).getType();
                String docid =arr.get(position).getDocid();
                if(from.equals("Details")){
                    if(type.equals("proposal")){
                        Intent i = new Intent(context, EditProposal.class);
                        i.putExtra("docID",docid);
                        i.putExtra("document", arr.get(holder.getAdapterPosition()));
                        i.putExtra("Version",ver);
                        i.putExtra("Project",p);
                        context.startActivity(i);}

                    else if(type.equals("vision")){
                        Intent i = new Intent(context, EditVisionDoc.class);
                        i.putExtra("docID",docid);
                        i.putExtra("document", arr.get(holder.getAdapterPosition()));
                        i.putExtra("Version",ver);
                        i.putExtra("Project",p);
                        context.startActivity(i);}

                    else if(type.equals("srs")){
                        Intent i = new Intent(context, EditSRSdoc1.class);
                        i.putExtra("docID",docid);
                        i.putExtra("document", arr.get(holder.getAdapterPosition()));
                        i.putExtra("Version",ver);
                        i.putExtra("Project",p);
                        i.putExtra("cameFrom1","Adapter");
                        context.startActivity(i);}}



                else if (from.equals("Search")){

                    sendToSponsor(type,spon,arr.get(position));
                }else if (from.equals("AppliedToDetails")){
                    if(type.equals("proposal")){
                        Intent i = new Intent(context, EditProposal.class);
                        i.putExtra("docID",sw.getSp().getPnewId());
                        i.putExtra("document", arr.get(holder.getAdapterPosition()));
                        i.putExtra("Version",ver);
                        i.putExtra("Project",p);
                        context.startActivity(i);}

                    else if(type.equals("vision")){
                        Intent i = new Intent(context, EditVisionDoc.class);
                        i.putExtra("docID",sw.getSp().getVnewId());
                        i.putExtra("document", arr.get(holder.getAdapterPosition()));
                        i.putExtra("Version",ver);
                        i.putExtra("Project",p);
                        context.startActivity(i);}

                    else if(type.equals("srs")){
                        Intent i = new Intent(context, EditSRSdoc1.class);
                        i.putExtra("docID",sw.getSp().getSnewId());
                        i.putExtra("document", arr.get(holder.getAdapterPosition()));
                        i.putExtra("cameFrom1","Adapter");
                        context.startActivity(i);}

                }

            }
        });
        ((DocumentAdapter.ViewHolder ) holder).v.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if(from.equals("Details")){
                    FirebaseAuth fAuth = FirebaseAuth.getInstance();
                    FirebaseFirestore fs = FirebaseFirestore.getInstance();
                    DocumentReference d,specificdoc;
                    String type = arr.get(position).getType();
                    String docid =arr.get(position).getDocid();
                    d = fs.collection("documents").document(docid);
                    if(type.equals("proposal"))
                        specificdoc = fs.collection("proposalDoc").document(docid);
                    else if(type.equals("vision"))
                        specificdoc = fs.collection("visionDoc").document(docid);
                    else if(type.equals("srs"))
                        specificdoc = fs.collection("srsDoc").document(docid);
                    else
                        specificdoc = fs.collection("fullDoc").document(docid);

                    PopupMenu pm = new PopupMenu(context,v);
                    pm.getMenuInflater().inflate(R.menu.document_popup_menu,pm.getMenu());

                    final AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    pm.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            int sitem = item.getItemId();
                            if(sitem == R.id.deletedoc){
                                builder.setTitle("Delete Document");
                                builder.setMessage("Are you sure you want to delete "+arr.get(position).getName()+"?");
                                builder.setCancelable(false).setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        FirebaseFirestore fs = FirebaseFirestore.getInstance();
                                        fs.collection("shared_projects").whereEqualTo("poriginalId",arr.get(position).getDocid()).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                            @Override
                                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                                if(queryDocumentSnapshots.size()==0){
                                                    specificdoc.delete();
                                                    d.delete();
                                                    arr.remove(position);
                                                    notifyDataSetChanged ();
                                                    if(arr.isEmpty())
                                                        empty.setVisibility(View.VISIBLE);
                                                }else{
                                                    Toast.makeText(context,"You cannot delete a shared document",Toast.LENGTH_LONG).show();
                                                }
                                            }
                                        });

                                    }
                                });

                                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                });

                                AlertDialog ad = builder.create();
                                ad.show();
                                return true;
                            }
                            else if (sitem == R.id.download){
                                if(type.equals("proposal")){
                                    Intent i = new Intent(context, ProposalPdf.class);
                                    i.putExtra("docID",docid);
                                    i.putExtra("document", arr.get(holder.getAdapterPosition()));
                                    i.putExtra("logo",logourl);
                                    i.putExtra("Project",p);
                                    i.putExtra("ver",ver);
                                    context.startActivity(i);}

                                else if(type.equals("vision")){
                                    Intent i = new Intent(context, VisionPdf.class);
                                    i.putExtra("docID",docid);
                                    i.putExtra("document", arr.get(holder.getAdapterPosition()));
                                    i.putExtra("logo",logourl);
                                    i.putExtra("Project",p);
                                    i.putExtra("ver",ver);
                                    context.startActivity(i);}

                                else if(type.equals("srs")){
                                    Intent i = new Intent(context, SRSPdf.class);
                                    i.putExtra("docID",docid);
                                    i.putExtra("document", arr.get(holder.getAdapterPosition()));
                                    i.putExtra("logo",logourl);
                                    i.putExtra("Project",p);
                                    i.putExtra("ver",ver);
                                    context.startActivity(i);}

                                return true;
                            }

                            else if (sitem == R.id.shareemail){
                                Intent e=new Intent(Intent.ACTION_SEND);
                                e.setType("text/plain");
                                context.startActivity(Intent.createChooser(e,"send email:"));
                                return true;
                            }



                            else if (sitem == R.id.sharesponsor){
                                AlertDialog.Builder mDialog = new AlertDialog.Builder(context);
                                final View shareview = LayoutInflater.from(context).inflate(R.layout.sharepopup,null);
                                mDialog.setView(shareview);
                                EditText search = shareview.findViewById(R.id.searchForDocShare);
                                RecyclerView rv = shareview.findViewById(R.id.shareRV);
                                rv.setHasFixedSize(true);
                                RecyclerView.LayoutManager lm = new LinearLayoutManager(mDialog.getContext());
                                rv.setLayoutManager(lm);
                                final shareAdapter[] da = new shareAdapter[1];
                                fs.collection("users").whereEqualTo("type","sponsor").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                    @Override
                                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                        ArrayList<SponsorUser> sponsorUsers = new ArrayList<>();
                                        for (DocumentSnapshot doc:queryDocumentSnapshots ) {
                                            SponsorUser su = doc.toObject(SponsorUser.class);
                                            sponsorUsers.add(su);
                                        }
                                        if(sponsorUsers!=null) {
                                            da[0] = new shareAdapter(sponsorUsers, mDialog.getContext(),
                                                    arr.get(position),p,ver);
                                            rv.setAdapter(da[0]);
                                        }
                                        search.addTextChangedListener(new TextWatcher() {
                                            @Override
                                            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                                            }

                                            @Override
                                            public void onTextChanged(CharSequence s, int start, int before, int count) {

                                            }

                                            @Override
                                            public void afterTextChanged(Editable s) {
                                                ArrayList<SponsorUser> filteredlist = new ArrayList<>();
                                                for (SponsorUser suser : sponsorUsers) {
                                                    if(suser.getUsername().trim().toLowerCase().contains(s.toString().trim().toLowerCase())){
                                                        filteredlist.add(suser);
                                                    }
                                                }
                                                da[0].filterlist(filteredlist);
                                            }
                                        });
                                    }
                                });
                                AlertDialog dialog = mDialog.create();
                                dialog.show();

                                return true;
                            }
                            else
                                return false;
                        }
                    });
                    pm.show();
                    return true;
                }else if(from.equals("AppliedToDetails")){
                    PopupMenu pm = new PopupMenu(context,v);
                    pm.getMenuInflater().inflate(R.menu.not_proposal_menu,pm.getMenu());

                    pm.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            int sitem = item.getItemId();
                            if (sitem == R.id.download1){
                                return true;
                            }
                            else if (sitem == R.id.reapply){
                                reapply(arr.get(position));
                                return true;
                            }
                            else
                                return false;
                        }
                    });
                    pm.show();
                    return true;

                }else{
                    return false;
                }

            }
        });
    }

    private void sendToSponsor(String type, SponsorUser spon,Document doc){
        FirebaseFirestore fs = FirebaseFirestore.getInstance();
        CollectionReference sharedRef;
        sharedRef = fs.collection("shared_projects");
        sharedRef.whereEqualTo("poriginalId",doc.getDocid()).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                boolean flag = true;
                if(queryDocumentSnapshots.size()==0)
                    finalsend(spon,doc);
                else{
                    for( DocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                        SharedProject sdd = documentSnapshot.toObject(SharedProject.class);
                        String currentdocid = doc.getDocid();
                        String currentsponsorid = spon.getId();
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
                                    }).create().show();flag = false;
                            break;
                        }
                    }
                    if (flag)
                        finalsend(spon,doc);

                }
            }
        });


    }

    private void finalsend(SponsorUser spon, Document doc){
        FirebaseAuth fAuth = FirebaseAuth.getInstance();
        FirebaseFirestore fs = FirebaseFirestore.getInstance();
        DocumentReference specificdoc,sponRef,createRef;
        CollectionReference specificdocColl,sharedRef,docsCol;
        docsCol = fs.collection("documents");
        sharedRef = fs.collection("shared_projects");
        sponRef = fs.collection("users").document(spon.getId()); //shared with
        createRef = fs.collection("users").document(fAuth.getCurrentUser().getUid()); // creator info
        specificdocColl = fs.collection("proposalDoc");
        specificdoc = fs.collection("proposalDoc").document(doc.getDocid());
        specificdoc.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                specificdocColl.add(documentSnapshot).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference dr) {
                        String time = System.currentTimeMillis()+"";
                        SharedProject sd = new SharedProject(doc.getName(), doc.getDocid(), dr.getId(),null,null,null,null, doc.getPid(), fAuth.getCurrentUser().getUid()
                                , spon.getId(), "proposal", doc.getVerid(), "pending", "none", "none",null,null,null,null,false,false,time);
                        sharedRef.add(sd).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                sharedRef.document(documentReference.getId()).update("sharedprojectid",documentReference.getId());
                                docsCol.document(doc.getDocid()).update("copies", FieldValue.arrayUnion(dr.getId()));
                                sponRef.update("sharedProjects", FieldValue.arrayUnion(documentReference.getId()));
                                createRef.update("sharedProjects", FieldValue.arrayUnion(documentReference.getId()));
                                sharedRef.document(documentReference.getId()).update("time",time);
                                Toast.makeText(context, "Document was shared successfully",Toast.LENGTH_LONG).show();

                                //notif
                                String sponID = spon.getId();
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

                                Intent i;
                                if(from.equals("Details")) {
                                    i = new Intent(context, ProjectDetails.class);
                                    i.putExtra("Project",p);
                                    i.putExtra("Version",ver);
                                    context.startActivity(i);
                                }
                                else if(from.equals("Search")) {
                                    i = new Intent(context, SearchSponsors.class);
                                    context.startActivity(i);
                                }



                            }
                        });


                    }
                });
            }
        });
    }
    private void reapply(Document doc){
        String type = doc.getType();
        String stage1 = sw.getSp().getStage1();
        String stage2 = sw.getSp().getStage2();
        String stage3 = sw.getSp().getStage3();

        if(type.equals("proposal")){
            if(stage1.equals("pending") || stage1.equals("rejected")){
                Toast.makeText(context,"You cannot apply/re-apply the document when its status is "+stage1,Toast.LENGTH_LONG).show();
            }else if(stage1.equals("accept_with_revision")){
                reapply1(doc);
            }
        }else if(type.equals("vision")){
            if(stage2.equals("pending") || stage2.equals("rejected")){
                Toast.makeText(context,"You cannot apply/re-apply the document when its status is "+stage2,Toast.LENGTH_LONG).show();
            }else if(stage2.equals("none")){
                sendNonProposal(doc);
            }else if(stage2.equals("accept_with_revision")){
                reapply1(doc);
            }
        }else if(type.equals("srs")){
            if(stage3.equals("pending") || stage3.equals("rejected")){
                Toast.makeText(context,"You cannot apply/re-apply the document when its status is "+stage3,Toast.LENGTH_LONG).show();
            }else if(stage3.equals("none")){
                sendNonProposal(doc);
            }else if(stage3.equals("accept_with_revision")){
                reapply1(doc);
            }
        }

//        Intent i =new Intent(context,ProjectDetails.class);
//        i.putExtra("Project",p);
//        i.putExtra("Version",ver);
//        context.startActivity(i);


    }
    private void sendNonProposal(Document doc){
        FirebaseFirestore fs = FirebaseFirestore.getInstance();
        DocumentReference specificdoc,sharedRef,document;
        CollectionReference specificdocColl;
        sharedRef = fs.collection("shared_projects").document(sw.getSp().getSharedprojectid());
        String type = doc.getType();
        String path = type+"Doc";
        String parentdocid = doc.getDocid();
        specificdocColl = fs.collection(path);
        document = fs.collection("documents").document(parentdocid);
        specificdoc = fs.collection(path).document(parentdocid);
        specificdoc.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(type.equals("vision")) {
                    VisionDocument vd = documentSnapshot.toObject(VisionDocument.class);
                    specificdocColl.add(vd).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            String time = System.currentTimeMillis()+"";
                            sharedRef.update("voriginalId",parentdocid);
                            sharedRef.update("vnewId",documentReference.getId());
                            sharedRef.update("stage2","pending");
                            sharedRef.update("type","vision");
                            sharedRef.update("openedcreator", false);
                            sharedRef.update("time",time);
                            document.update("copies", FieldValue.arrayUnion(documentReference.getId()));
                            Toast.makeText(context, "Document was applied successfully",Toast.LENGTH_LONG).show();
//                            String sponID = spon.getId();
//                            fs.collection("tokens").document(sponID)
//                                    .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
//                                @Override
//                                public void onSuccess(DocumentSnapshot documentSnapshot) {
//                                    if(documentSnapshot.exists()){
//                                        Token t = documentSnapshot.toObject(Token.class);
//                                        //String neededtoken = t.getToken();
//                                        String neededtoken = "eh-Uptp7QSGp0shVX8LJMC:APA91bFprNLLSlJ9JxLq4kltF0Ex8wL61tUwmRhHah_CwGUTv5c5Tlf6UmziwZxBWZNPNr34tpPDHby6bJlI9N3-VJmT7wvAqyITfchzWm5tLYxEDom2EK-G0Sc3wMhTwiWvmIUMeiW2";
//                                        String doctype = type;
//                                        FcmNotificationsSender notificationsSender = new FcmNotificationsSender(neededtoken,
//                                                "New "+doctype+" Document","Check out the new Document shared " +
//                                                "with you", context.getApplicationContext());
//                                        notificationsSender.SendNotifications();
//                                    }
//                                    else{
//                                        System.out.println("no spon so no token");
//                                    }
//                                }
//                            });
                            backToProDetails();
                        }
                    });
                }else{
                    SRSDocument vd = documentSnapshot.toObject(SRSDocument.class);
                    specificdocColl.add(vd).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            String time = System.currentTimeMillis()+"";
                            sharedRef.update("soriginalId",parentdocid);
                            sharedRef.update("snewId",documentReference.getId());
                            sharedRef.update("stage3","pending");
                            sharedRef.update("type","srs");
                            sharedRef.update("openedcreator", false);
                            sharedRef.update("time",time);
                            document.update("copies", FieldValue.arrayUnion(documentReference.getId()));
                            Toast.makeText(context, "Document was applied successfully",Toast.LENGTH_LONG).show();
//                            String sponID = spon.getId();
//                            fs.collection("tokens").document(sponID)
//                                    .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
//                                @Override
//                                public void onSuccess(DocumentSnapshot documentSnapshot) {
//                                    if(documentSnapshot.exists()){
//                                        Token t = documentSnapshot.toObject(Token.class);
//                                        //String neededtoken = t.getToken();
//                                        String neededtoken = "eh-Uptp7QSGp0shVX8LJMC:APA91bFprNLLSlJ9JxLq4kltF0Ex8wL61tUwmRhHah_CwGUTv5c5Tlf6UmziwZxBWZNPNr34tpPDHby6bJlI9N3-VJmT7wvAqyITfchzWm5tLYxEDom2EK-G0Sc3wMhTwiWvmIUMeiW2";
//                                        String doctype = "srs";
//                                        FcmNotificationsSender notificationsSender = new FcmNotificationsSender(neededtoken,
//                                                "New SRS Document","Check out the new Document shared " +
//                                                "with you", context.getApplicationContext());
//                                        notificationsSender.SendNotifications();
//                                    }
//                                    else{
//                                        System.out.println("no spon so no token");
//                                    }
//                                }
//                            });

                            backToProDetails();
                        }
                    });
                }
            }
        });
    }
    private void backToProDetails(){
        Intent i =new Intent(context,ProjectDetails.class);
        i.putExtra("Project",p);
        i.putExtra("Version",ver);
        context.startActivity(i);
    }
    private void reapply1(Document doc){
        FirebaseFirestore fs = FirebaseFirestore.getInstance();
        DocumentReference sharedRef;
        sharedRef = fs.collection("shared_projects").document(sw.getSp().getSharedprojectid());
        String type = doc.getType();
        if(type.equals("proposal")){
            sharedRef.update("stage1","pending").addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    String time = System.currentTimeMillis()+"";
                    sharedRef.update("time",time);
                    Toast.makeText(context, "Document was applied successfully",Toast.LENGTH_LONG).show();
                    backToProDetails();
                }
            });}
        else if(type.equals("vision")){
            sharedRef.update("stage2","pending").addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    String time = System.currentTimeMillis()+"";
                    sharedRef.update("time",time);
                    Toast.makeText(context, "Document was applied successfully",Toast.LENGTH_LONG).show();
                    backToProDetails();
                }
            });
        }else{
            sharedRef.update("stage3","pending").addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    String time = System.currentTimeMillis()+"";
                    sharedRef.update("time",time);
                    Toast.makeText(context, "Document was applied successfully",Toast.LENGTH_LONG).show();
                    backToProDetails();
                }
            });
        }


    }

    @Override
    public int getItemCount() {
        return arr.size();
    }

    private static class ViewHolder extends RecyclerView.ViewHolder{
        ImageView img;
        TextView sname;
        View v;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            v = itemView;
            img = itemView.findViewById(R.id.docIcon);
            sname = (TextView)itemView.findViewById(R.id.docName);
        }
    }


}
