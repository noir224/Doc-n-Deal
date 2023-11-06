package com.example.docanddeal;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class NotificationsAdapter extends RecyclerView.Adapter {

    ArrayList<SharedProject> notilist;
    ArrayList<SharedProject> notificationslist;
    Context context;
    TextView empty;
    FirebaseStorage store;
    StorageReference storageReference;
    FirebaseFirestore fs;
    FirebaseAuth fAuth;
    DocumentReference dref,project;
    ArrayList<Version> v;
    Version desiredversion;

    public NotificationsAdapter(ArrayList<SharedProject> notilist, Context context) {
        this.notilist = notilist;
        this.context = context;
        this.notificationslist = new ArrayList<>(notilist);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notification_item,parent,false);
        return new NotificationsAdapter.NViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        String type = notilist.get(position).getType();
        String statusstage1 = notilist.get(position).getStage1();
        String statusstage2 = notilist.get(position).getStage2();
        String statusstage3 = notilist.get(position).getStage3();
        String pid = notilist.get(position).getPid();
        String sponsorid = notilist.get(position).getSharedWith();
        String creatorid = notilist.get(position).getCreatorID();
        String version = notilist.get(position).getProjectVersion();
        String sharedprojID = notilist.get(position).getSharedprojectid();
        boolean isopenedsponsor = notilist.get(position).isOpenedsponsor();
        boolean isopenedcreator = notilist.get(position).isOpenedcreator();




        fAuth = FirebaseAuth.getInstance();
        String userID = fAuth.getCurrentUser().getUid();
        FirebaseFirestore fs = FirebaseFirestore.getInstance();
        FirebaseFirestore fStore = FirebaseFirestore.getInstance();
        fs.collection("projects").document(pid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                ProjectC project = task.getResult().toObject(ProjectC.class);
                v = project.getVersions();
                for (Version num : v) {
                    if (num.getVersion().equalsIgnoreCase(version)) {
                        desiredversion = num;
                    }
                }
                SingleSharedProject sss = new SingleSharedProject(project, notilist.get(position),desiredversion);

                String projectname = project.getName();
                Picasso.get().load(Uri.parse(project.getLogo())).resize(50,50).centerCrop().into(((NotificationsAdapter.NViewHolder)holder).img);

                fs.collection("users").document(userID).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        DocumentSnapshot userds = task.getResult();
                        String usertype = userds.getString("type");

                        if(usertype.equalsIgnoreCase("creator")){
                            fs.collection("users").document(sponsorid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    DocumentSnapshot sponsords = task.getResult();
                                    String sponsorname = sponsords.getString("username");
                                    if(type.equalsIgnoreCase("proposal")){
                                        String thetitle = "Your proposal document got "+statusstage1;
                                        String thedesc = sponsorname+" has just "+statusstage1+" your "+type+" document in project "+projectname;
                                        ((NotificationsAdapter.NViewHolder ) holder).title.setText(thetitle);
                                        ((NotificationsAdapter.NViewHolder ) holder).description.setText(thedesc);
                                        if(isopenedcreator==false){
                                            ((NotificationsAdapter.NViewHolder ) holder).dot.setVisibility(View.VISIBLE);
                                        }
                                    }
                                    if(type.equalsIgnoreCase("vision")){
                                        if (statusstage1.equalsIgnoreCase("accepted")&statusstage2.equalsIgnoreCase("accepted")||statusstage2.equalsIgnoreCase("accept_with_revision")){
                                            String thetitle2 = "Your vision document got "+statusstage2;
                                            String thedesc2 = sponsorname+" has just "+statusstage2+" your vision document in project "+projectname;
                                            if(isopenedcreator==false){
                                                ((NotificationsAdapter.NViewHolder ) holder).dot.setVisibility(View.VISIBLE);
                                            }
                                            ((NotificationsAdapter.NViewHolder ) holder).title.setText(thetitle2);
                                            ((NotificationsAdapter.NViewHolder ) holder).description.setText(thedesc2);}
                                    }
                                    if(type.equalsIgnoreCase("srs")){
                                        if (statusstage2.equalsIgnoreCase("accepted")&statusstage3.equalsIgnoreCase("accepted")||statusstage3.equalsIgnoreCase("accept_with_revision")){
                                            String thetitle3 = "Your SRS document got "+statusstage3;
                                            String thedesc3 = sponsorname+" has just "+statusstage3+" your SRS document in project "+projectname;
                                            if(isopenedcreator==false){
                                                ((NotificationsAdapter.NViewHolder ) holder).dot.setVisibility(View.VISIBLE);
                                            }
                                            ((NotificationsAdapter.NViewHolder ) holder).title.setText(thetitle3);
                                            ((NotificationsAdapter.NViewHolder ) holder).description.setText(thedesc3);}
                                    }

                                }
                            });
                            /*fs.collection("users").document(sponsorid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    DocumentSnapshot sponsords = task.getResult();
                                    String sponsorname = sponsords.getString("username");


                                }
                            });
                            fs.collection("users").document(sponsorid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    DocumentSnapshot sponsords = task.getResult();
                                    String sponsorname = sponsords.getString("username");


                                }
                            });*/
                            ((NotificationsAdapter.NViewHolder) holder).card.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    DocumentReference dr= FirebaseFirestore.getInstance().collection("projects").document(pid);
                                    dr.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                            //change this to project stuff and see if you need to send sth w it

                                            Intent i= new Intent(context, ProjectDetails.class);
                                            i.putExtra("Project",project);
                                            i.putExtra("Version",desiredversion);
                                            context.startActivity(i);
                                        }
                                    });
                                }
                            });

                        }
                        if(usertype.equalsIgnoreCase("sponsor")){
                            fs.collection("users").document(creatorid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    DocumentSnapshot creator = task.getResult();
                                    String creatorname = creator.getString("username");
                                    String thetitle = "New "+type+" document";
                                    String thedesc = creatorname+" shared a "+type+" document with you from "+projectname+" project";
                                    if(isopenedsponsor==false){
                                        ((NotificationsAdapter.NViewHolder ) holder).dot.setVisibility(View.VISIBLE);
                                    }
                                    ((NotificationsAdapter.NViewHolder ) holder).title.setText(thetitle);
                                    ((NotificationsAdapter.NViewHolder ) holder).description.setText(thedesc);
                                }
                            });

                            ((NotificationsAdapter.NViewHolder) holder).card.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    DocumentReference dr= FirebaseFirestore.getInstance().collection("projects").document(pid);
                                    dr.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                            //change this to project stuff and see if you need to send sth w it

                                            Intent i= new Intent(context, SharedProjectDetails.class);
                                            i.putExtra("project",sss);
                                            context.startActivity(i);
                                        }
                                    });
                                }
                            });

                        }
                    }
                });

            }

        } );





    }

    @Override
    public int getItemCount() {
        return notilist.size();
    }

    private static class NViewHolder extends RecyclerView.ViewHolder {
        CircleImageView img;
        TextView title, description,time;
        View v;
        CardView card;
        ImageView dot;

        public NViewHolder(@NonNull View itemView) {
            super(itemView);
            v = itemView;
            img = itemView.findViewById(R.id.logoOFnoti);
            title = (TextView) itemView.findViewById(R.id.notititleitem);
            description = (TextView) itemView.findViewById(R.id.notidescription);
            time = (TextView) itemView.findViewById(R.id.notitime);
            card = (CardView) itemView.findViewById(R.id.cardviewnoti);
            dot = (ImageView) itemView.findViewById(R.id.neww);
        }

    }
}
