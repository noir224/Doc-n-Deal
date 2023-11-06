package com.example.docanddeal;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class SponsorStagesAdapter extends RecyclerView.Adapter {
    ArrayList<Stage> arr;
    Context context;
    SingleSharedProject ssp;


    public SponsorStagesAdapter(ArrayList<Stage> arr, Context context, SingleSharedProject ssp) {
        this.arr = arr;
        this.context = context;
        this.ssp = ssp;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.docstage_item,parent,false);
        return new SponsorStagesAdapter.VerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        int n = arr.get(position).getNum();
        if(n==1) {
            ((SponsorStagesAdapter.VerViewHolder) holder).name.setText("Proposal Document");
            ((SponsorStagesAdapter.VerViewHolder) holder).stage.setText("Stage 1");
            ((SponsorStagesAdapter.VerViewHolder) holder).viewmore.setText("View Proposal");
            ((SponsorStagesAdapter.VerViewHolder ) holder).viewmore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(arr.get(position).getStatus().equals("none")){
                        Toast.makeText(context,"Document not applied yet", Toast.LENGTH_LONG).show();
                    }
                    else{
                        Intent i = new Intent(context, ProposalPdf.class);
                        i.putExtra("docID",ssp.getSharedProjects().getPnewId());
                        i.putExtra("logo",ssp.getProject().getLogo());
                        i.putExtra("ver",ssp.getVersion());
                        i.putExtra("Project",ssp.getProject());
                        context.startActivity(i);
                    }
                }
            });
        }
        else if(n==2){
            ((SponsorStagesAdapter.VerViewHolder ) holder).name.setText("Vision Document");
            ((SponsorStagesAdapter.VerViewHolder ) holder).stage.setText("Stage 2");
            ((SponsorStagesAdapter.VerViewHolder ) holder).viewmore.setText("View Vision");
            ((VerViewHolder ) holder).reject.setVisibility(View.GONE);
            ((SponsorStagesAdapter.VerViewHolder ) holder).viewmore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(arr.get(position).getStatus().equals("none")){
                        Toast.makeText(context,"Document not applied yet", Toast.LENGTH_LONG).show();
                    }
                    else{
                        Intent i = new Intent(context, VisionPdf.class);
                        i.putExtra("docID",ssp.getSharedProjects().getVnewId());
                        i.putExtra("logo",ssp.getProject().getLogo());
                        i.putExtra("ver",ssp.getVersion());
                        i.putExtra("Project",ssp.getProject());
                        context.startActivity(i);
                    }
                }
            });
        }else{

            ((SponsorStagesAdapter.VerViewHolder ) holder).name.setText("SRS Document");
            ((SponsorStagesAdapter.VerViewHolder ) holder).stage.setText("Stage 3");
            ((SponsorStagesAdapter.VerViewHolder ) holder).viewmore.setText("View SRS");
            ((VerViewHolder ) holder).reject.setVisibility(View.GONE);
            ((SponsorStagesAdapter.VerViewHolder ) holder).viewmore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(arr.get(position).getStatus().equals("none")){
                        Toast.makeText(context,"Document not applied yet", Toast.LENGTH_LONG).show();
                    }
                    else{
                        Intent i = new Intent(context, SRSPdf.class);
                        i.putExtra("docID",ssp.getSharedProjects().getSnewId());
                        i.putExtra("logo",ssp.getProject().getLogo());
                        i.putExtra("ver",ssp.getVersion());
                        i.putExtra("Project",ssp.getProject());
                        context.startActivity(i);
                    }
                }
            });
        }

        ((SponsorStagesAdapter.VerViewHolder ) holder).accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(arr.get(position).getStatus().equals("none")){
                    Toast.makeText(context,"Document not applied yet", Toast.LENGTH_LONG).show();
                }
                else if (arr.get(position).getStatus().equals("accepted")){
                    Toast.makeText(context,"Document already accepted", Toast.LENGTH_LONG).show();
                }
                else if( arr.get(position).getStatus().equals("accept_with_revision")){
                    Toast.makeText(context,"Document already accepted with revision", Toast.LENGTH_LONG).show();
                }
                else if(arr.get(position).getStatus().equals("rejected")){
                    Toast.makeText(context,"Document already rejected", Toast.LENGTH_LONG).show();
                }
                else {
                    ShowAcceptDialog();
                }
            }

            private void ShowAcceptDialog() {
                Dialog customdialog= new Dialog(context);
                customdialog.getWindow().setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
                customdialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                customdialog.getWindow().getAttributes().windowAnimations
                        = android.R.style.Animation_Dialog;
                customdialog.setContentView(R.layout.accept_dialog);
                Button accept = customdialog.findViewById(R.id.acceptButton);
                Button cancel =customdialog.findViewById(R.id.cancelButton2);

                accept.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        FirebaseFirestore fs = FirebaseFirestore.getInstance();
                        FirebaseFirestore fStore = FirebaseFirestore.getInstance();
                        DocumentReference doc = fStore.collection("shared_projects").document(ssp.getSharedProjects().getSharedprojectid());
                        fs.collection("shared_projects").document(ssp.getSharedProjects().getSharedprojectid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                DocumentSnapshot ds = task.getResult();

                                if(ds.getString("type").equals("proposal")){
                                    doc.update("stage1","accepted");
                                    doc.update("time",System.currentTimeMillis()+"");
                                }
                                else if (ds.getString("type").equals("vision")){
                                    doc.update("stage2","accepted");
                                    doc.update("time",System.currentTimeMillis()+"");
                                }
                                else{
                                    doc.update("stage3","accepted");
                                    doc.update("time",System.currentTimeMillis()+"");
                                }
                                //notif
                                String creatorID = ds.getString("creatorID");
                                fs.collection("tokens").document(creatorID)
                                        .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                    @Override
                                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                                        if(documentSnapshot.exists()){
                                            Token t = documentSnapshot.toObject(Token.class);
                                            String neededtoken = t.getToken();
                                            //String neededtoken = "eh-Uptp7QSGp0shVX8LJMC:APA91bFprNLLSlJ9JxLq4kltF0Ex8wL61tUwmRhHah_CwGUTv5c5Tlf6UmziwZxBWZNPNr34tpPDHby6bJlI9N3-VJmT7wvAqyITfchzWm5tLYxEDom2EK-G0Sc3wMhTwiWvmIUMeiW2";
                                            String doctype = ds.getString("type");
                                            FcmNotificationsSender notificationsSender = new FcmNotificationsSender(neededtoken,
                                                    "Your "+doctype+" Document got Accepted","Check out your "+doctype+" document that got accepted"
                                                    ,context.getApplicationContext());
                                            notificationsSender.SendNotifications();
                                        }
                                        else{
                                            System.out.println("no creator so no token");
                                        }
                                    }
                                });
                                if(arr.get(position).getNum()==1){
                                    SRProjects p2 = new SRProjects(ssp.getProject().getName(), ssp.getProject().getLogo() + "", ssp.getProject().getType(), ssp.getProject().getLink(), ssp.getSharedProjects().getSharedWith(), null,ssp.getProject().getIsprivate());

                                    fs.collection("sponsored_projects").add(p2).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                        @Override
                                        public void onSuccess(DocumentReference documentReference) {
                                            String id = documentReference.getId();
                                            fs.collection("users").document(ssp.getSharedProjects().getSharedWith()).update("sponsoredProjects",
                                                    FieldValue.arrayUnion(id));
                                            documentReference.update("spid", id);
                                        }
                                    });
                                }
                                Intent i =new Intent(context,SharedProjectDetails.class);
                                i.putExtra("project",ssp);
                                context.startActivity(i);
                            }
                        });

                    }
                });
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        customdialog.cancel();
                    }
                });
                customdialog.show();
            }
        });

        ((VerViewHolder ) holder).reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(arr.get(position).getStatus().equals("none")){
                    Toast.makeText(context,"Document not applied yet", Toast.LENGTH_LONG).show();
                }
                else if (arr.get(position).getStatus().equals("accepted")){
                    Toast.makeText(context,"Document already accepted", Toast.LENGTH_LONG).show();
                }
                else if( arr.get(position).getStatus().equals("accept_with_revision")){
                    Toast.makeText(context,"Document already accepted with revision", Toast.LENGTH_LONG).show();
                }
                else if(arr.get(position).getStatus().equals("rejected")){
                    Toast.makeText(context,"Document already rejected", Toast.LENGTH_LONG).show();
                }
                else{
                    ShowRejectDialog();
                }
            }

            private void ShowRejectDialog() {
                Dialog customdialog= new Dialog(context);
                customdialog.getWindow().setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
                customdialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                customdialog.getWindow().getAttributes().windowAnimations
                        = android.R.style.Animation_Dialog;
                customdialog.setContentView(R.layout.reject_dialog);
                Button reject = customdialog.findViewById(R.id.rejectButton);
                Button cancel =customdialog.findViewById(R.id.cancelButton3);

                reject.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        FirebaseFirestore fs = FirebaseFirestore.getInstance();
                        FirebaseFirestore fStore = FirebaseFirestore.getInstance();
                        DocumentReference doc = fStore.collection("shared_projects").document(ssp.getSharedProjects().getSharedprojectid());
                        fs.collection("shared_projects").document(ssp.getSharedProjects().getSharedprojectid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                DocumentSnapshot ds = task.getResult();
                                if(ds.getString("type").equals("proposal")){
                                    doc.update("stage1","rejected");
                                    doc.update("time",System.currentTimeMillis()+"");
                                }
                                else if (ds.getString("type").equals("vision")){
                                    doc.update("stage2","rejected");
                                    doc.update("time",System.currentTimeMillis()+"");
                                }
                                else{
                                    doc.update("stage3","rejected");
                                    doc.update("time",System.currentTimeMillis()+"");
                                }
                                //notif
                                String creatorID = ds.getString("creatorID");
                                fs.collection("tokens").document(creatorID)
                                        .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                    @Override
                                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                                        if(documentSnapshot.exists()){
                                            Token t = documentSnapshot.toObject(Token.class);
                                            String neededtoken = t.getToken();
                                            //String neededtoken = "eh-Uptp7QSGp0shVX8LJMC:APA91bFprNLLSlJ9JxLq4kltF0Ex8wL61tUwmRhHah_CwGUTv5c5Tlf6UmziwZxBWZNPNr34tpPDHby6bJlI9N3-VJmT7wvAqyITfchzWm5tLYxEDom2EK-G0Sc3wMhTwiWvmIUMeiW2";
                                            String doctype = ds.getString("type");
                                            FcmNotificationsSender notificationsSender = new FcmNotificationsSender(neededtoken,
                                                    "Your "+doctype+" Document got Rejected","Check out your "+doctype+" document that got Rejected"
                                                    ,context.getApplicationContext());
                                            notificationsSender.SendNotifications();
                                        }
                                        else{
                                            System.out.println("no creator so no token");
                                        }
                                    }
                                });
                                if(arr.get(position).getNum()==1){
                                    SRProjects p2 = new SRProjects(ssp.getProject().getName(), ssp.getProject().getLogo() + "", ssp.getProject().getType(), ssp.getProject().getLink(), ssp.getSharedProjects().getSharedWith(), null,ssp.getProject().getIsprivate());
                                    fs.collection("rejected_projects").add(p2).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                        @Override
                                        public void onSuccess(DocumentReference documentReference) {
                                            String id = documentReference.getId();
                                            fs.collection("users").document(ssp.getSharedProjects().getSharedWith()).update("rejectedProjects",
                                                    FieldValue.arrayUnion(id));
                                            documentReference.update("spid", id);
                                        }
                                    });
                                }

                                Intent i =new Intent(context,SharedProjectDetails.class);
                                i.putExtra("project",ssp);
                                context.startActivity(i);
                            }
                        });

                    }
                });
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        customdialog.cancel();
                    }
                });
                customdialog.show();
            }
        });
        ((VerViewHolder ) holder).acceptwr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(arr.get(position).getStatus().equals("none")){
                    Toast.makeText(context,"Document not applied yet", Toast.LENGTH_LONG).show();
                }
                else if (arr.get(position).getStatus().equals("accepted")){
                    Toast.makeText(context,"Document already accepted", Toast.LENGTH_LONG).show();
                }
                else if( arr.get(position).getStatus().equals("accept_with_revision")){
                    Toast.makeText(context,"Document already accepted with revision", Toast.LENGTH_LONG).show();
                }
                else if(arr.get(position).getStatus().equals("rejected")){
                    Toast.makeText(context,"Document already rejected", Toast.LENGTH_LONG).show();
                }
                else{
                    ShowAcceptWRdialog();
                }
            }

            private void ShowAcceptWRdialog() {
                Dialog customdialog= new Dialog(context);
                customdialog.getWindow().setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
                customdialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                customdialog.getWindow().getAttributes().windowAnimations
                        = android.R.style.Animation_Dialog;
                customdialog.setContentView(R.layout.acceptwr_dialog);
                EditText comment = customdialog.findViewById(R.id.comment);
                Button confirm = customdialog.findViewById(R.id.confirmButton);
                Button cancel =customdialog.findViewById(R.id.cancelButton);

                confirm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(comment.getText().toString().isEmpty()){
                            Toast.makeText(context,"PLEASE Add Your Feedback", Toast.LENGTH_LONG).show();
                        }
                        else{
                            FirebaseFirestore fs = FirebaseFirestore.getInstance();
                            FirebaseFirestore fStore = FirebaseFirestore.getInstance();
                            DocumentReference doc = fStore.collection("shared_projects").document(ssp.getSharedProjects().getSharedprojectid());
                            fs.collection("shared_projects").document(ssp.getSharedProjects().getSharedprojectid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    DocumentSnapshot ds = task.getResult();

                                    if(ds.getString("type").equals("proposal")){
                                        doc.update("stage1","accept_with_revision");
                                        doc.update("comment1",comment.getText().toString());
                                        doc.update("time",System.currentTimeMillis()+"");
                                    }
                                    else if (ds.getString("type").equals("vision")){
                                        doc.update("stage2","accept_with_revision");
                                        doc.update("comment2",comment.getText().toString());
                                        doc.update("time",System.currentTimeMillis()+"");
                                    }
                                    else{
                                        doc.update("stage3","accept_with_revision");
                                        doc.update("comment3",comment.getText().toString());
                                        doc.update("time",System.currentTimeMillis()+"");
                                    }
                                    //notif
                                    String creatorID = ds.getString("creatorID");
                                    fs.collection("tokens").document(creatorID)
                                            .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                        @Override
                                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                                            if(documentSnapshot.exists()){
                                                Token t = documentSnapshot.toObject(Token.class);
                                                String neededtoken = t.getToken();
                                                //String neededtoken = "eh-Uptp7QSGp0shVX8LJMC:APA91bFprNLLSlJ9JxLq4kltF0Ex8wL61tUwmRhHah_CwGUTv5c5Tlf6UmziwZxBWZNPNr34tpPDHby6bJlI9N3-VJmT7wvAqyITfchzWm5tLYxEDom2EK-G0Sc3wMhTwiWvmIUMeiW2";
                                                String doctype = ds.getString("type");
                                                FcmNotificationsSender notificationsSender = new FcmNotificationsSender(neededtoken,
                                                        "Your "+doctype+" Document got Accepted with revision","Check out your "+doctype+" document that got accepted with revision"
                                                        ,context.getApplicationContext());
                                                notificationsSender.SendNotifications();
                                            }
                                            else{
                                                System.out.println("no creator so no token");
                                            }
                                        }
                                    });

                                    Intent i =new Intent(context,SharedProjectDetails.class);
                                    i.putExtra("project",ssp);
                                    context.startActivity(i);
                                }
                            });
                        }
                    }
                });
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        customdialog.cancel();
                    }
                });
                customdialog.show();
            }

        });

        String color = getColor(arr.get(position).getStatus());
        ((SponsorStagesAdapter.VerViewHolder ) holder).cv.setCardBackgroundColor(Color.parseColor(color));
    }





    @Override
    public int getItemCount() {
        return arr.size();
    }

    private static class VerViewHolder extends RecyclerView.ViewHolder{
        CardView cv;
        TextView stage, name;
        Button viewmore;
        View v;
        Button accept,reject,acceptwr;
        public VerViewHolder(@NonNull View itemView) {
            super(itemView);
            v = itemView;
            cv =  (CardView) itemView.findViewById(R.id.cardviewitem);
            stage = (TextView)itemView.findViewById(R.id.stage);
            name = (TextView)itemView.findViewById(R.id.name);
            viewmore = (Button)itemView.findViewById(R.id.viewDoc);
            accept= (Button)itemView.findViewById(R.id.accept);
            reject=(Button)itemView.findViewById(R.id.reject);
            acceptwr= (Button)itemView.findViewById(R.id.acceptwithre);
        }
    }

    private String getColor(String status){
        if(status.equals("none"))
            return "#9599A4";
        else if(status.equals("pending"))
            return "#C5CEE0";
        else if(status.equals("rejected"))
            return "#feb7b7";
        else if(status.equals("accepted"))
            return "#c4e4c1";
        else
            return "#f3d383";

    }

}
