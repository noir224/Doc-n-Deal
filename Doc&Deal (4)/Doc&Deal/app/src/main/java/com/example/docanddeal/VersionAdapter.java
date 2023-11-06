package com.example.docanddeal;

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
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class VersionAdapter extends RecyclerView.Adapter {
    ArrayList<Version> arr;
    Context context;
    ProjectC p;
    //String pid;


    public VersionAdapter(ArrayList<Version> arr, Context context,ProjectC p) {
        this.arr = arr;
        this.context = context;
        this.p = p;

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.project_item,parent,false);
        return new VersionAdapter.VerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        String n = arr.get(position).getVersion();
        ((VersionAdapter.VerViewHolder ) holder).sname.setText(n);
        ((VersionAdapter.VerViewHolder ) holder).v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, ProjectDetails.class);
                i.putExtra("Version",arr.get(position));
                i.putExtra("Project",p);
                //i.putExtra("pid",pid);
                context.startActivity(i);
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
