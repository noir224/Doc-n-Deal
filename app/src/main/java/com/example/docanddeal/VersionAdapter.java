package com.example.docanddeal;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class VersionAdapter extends RecyclerView.Adapter {
    ArrayList<Version> arr;
    Context context;
    ProjectC p;
    String from;
    SponsorUser spon;

    public VersionAdapter(ArrayList<Version> arr, Context context, ProjectC p, String from, SponsorUser spon) {
        this.arr = arr;
        this.context = context;
        this.p = p;
        this.from = from;
        this.spon = spon;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.proj_item2,parent,false);
        return new VersionAdapter.VerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        String n = arr.get(position).getVersion();
        ((VersionAdapter.VerViewHolder ) holder).sname.setText(n);
        ((VersionAdapter.VerViewHolder ) holder).v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(from.equals("Main")){
                    Intent i = new Intent(context, ProjectDetails.class);
                    i.putExtra("Version",arr.get(position));
                    i.putExtra("Project",p);
                    //i.putExtra("pid",pid);
                    context.startActivity(i);
                }
                else{
                    AlertDialog.Builder mDialog = new AlertDialog.Builder(context);
                    final View verview = LayoutInflater.from(context).inflate(R.layout.docspopup,null);
                    mDialog.setView(verview);
                    RecyclerView rv = verview.findViewById(R.id.docsRVpopup);
                    TextView empty = verview.findViewById(R.id.ifnullProjects);
                    CircularProgressIndicator progressBar = verview.findViewById(R.id.progBarProjects);
                    rv.setHasFixedSize(true);
                    RecyclerView.LayoutManager lm = new GridLayoutManager(mDialog.getContext(),2);
                    rv.setLayoutManager(lm);
                    ArrayList<Document> docs = new ArrayList<>();
                    rv.setLayoutManager(lm);
                    FirebaseFirestore fs = FirebaseFirestore.getInstance();
                    fs.collection("documents")
                            .whereEqualTo("pid", p.getPid())
                            .get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful()) {
                                        for (QueryDocumentSnapshot document : task.getResult()) {
                                            if(arr.get(position).getVersion().equals(document.get("verid").toString())){
                                                Document d = document.toObject(Document.class);
                                                if(d.getType().equals("proposal"))
                                                    docs.add(d);
                                            }
                                            if(docs!=null) {
                                                DocumentAdapter da = new DocumentAdapter(docs, context,empty,p,arr.get(position),"Search",spon,null,null);
                                                rv.setAdapter(da);
                                            }
                                        }
                                        progressBar.setVisibility(View.GONE);
                                        if(docs.isEmpty())
                                            empty.setVisibility(View.VISIBLE);

                                    }
                                }
                            });

                    AlertDialog dialog = mDialog.create();
                    dialog.show();
                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return arr.size();
    }

    private static class VerViewHolder extends RecyclerView.ViewHolder{
        ImageView img;
        TextView sname;
        View v;
        public VerViewHolder(@NonNull View itemView) {
            super(itemView);
            v = itemView;
            img = itemView.findViewById(R.id.projIcon);
            sname = (TextView)itemView.findViewById(R.id.projName);
        }
    }

}
