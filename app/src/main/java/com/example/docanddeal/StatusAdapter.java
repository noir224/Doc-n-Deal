package com.example.docanddeal;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class StatusAdapter extends RecyclerView.Adapter {
    ArrayList<Stage> arr;
    Context context;
    SharedProject sp;
    ProjectC p;
    Version ver;

    public StatusAdapter(ArrayList<Stage> arr, Context context, SharedProject sp, ProjectC p,Version ver) {
        this.arr = arr;
        this.context = context;
        this.sp=sp;
        this.p=p;
        this.ver =ver;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.status_item,parent,false);
        return new StatusAdapter.VerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        int n = arr.get(position).getNum();
        if(n==1){
            ((StatusAdapter.VerViewHolder ) holder).stage.setText("Stage 1 (Proposal)");
            if(arr.get(position).getStatus().equals("accept_with_revision"))
                ((VerViewHolder ) holder).comment.setText(sp.getComment1());
            else
                ((VerViewHolder ) holder).comment.setText("There is no feedback");
        }else if(n==2){
            ((StatusAdapter.VerViewHolder ) holder).stage.setText("Stage 2 (Vision)");
            if(arr.get(position).getStatus().equals("accept_with_revision"))
                ((StatusAdapter.VerViewHolder ) holder).comment.setText(sp.getComment2());
            else
                ((VerViewHolder ) holder).comment.setText("There is no feedback");
        }else{
            ((StatusAdapter.VerViewHolder ) holder).stage.setText("Stage 3 (SRS)");
            if(arr.get(position).getStatus().equals("accept_with_revision"))
                ((StatusAdapter.VerViewHolder ) holder).comment.setText(sp.getComment3());
            else
                ((VerViewHolder ) holder).comment.setText("There is no feedback");
        }
        String color = getColor(arr.get(position).getStatus());
        ((StatusAdapter.VerViewHolder ) holder).cv.setCardBackgroundColor(Color.parseColor(color));

        if(arr.get(position).getStatus().equals("accepted")){
            ((StatusAdapter.VerViewHolder ) holder).status.setText("Accepted");
            ((StatusAdapter.VerViewHolder ) holder).viewDoc.setVisibility(View.VISIBLE);
        }
        if(arr.get(position).getStatus().equals("accept_with_revision")){
            ((StatusAdapter.VerViewHolder ) holder).status.setText("Accepted With Revision");
        }
        if(arr.get(position).getStatus().equals("rejected")){
            ((StatusAdapter.VerViewHolder ) holder).status.setText("Rejected");
        }
        if(arr.get(position).getStatus().equals("pending")){
            ((StatusAdapter.VerViewHolder ) holder).status.setText("Pending");
        }
        ((StatusAdapter.VerViewHolder ) holder).viewDoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(n==1){
                    Intent i = new Intent(context, ProposalPdf.class);
                    i.putExtra("docID",sp.getPnewId());
                    i.putExtra("logo",p.getLogo());
                    i.putExtra("Project",p);
                    i.putExtra("ver",ver);
                    context.startActivity(i);
                }else if(n==2){
                    Intent i = new Intent(context, VisionPdf.class);
                    i.putExtra("docID",sp.getVnewId());
                    i.putExtra("logo",p.getLogo());
                    i.putExtra("Project",p);
                    i.putExtra("ver",ver);
                    context.startActivity(i);
                }
                else if(n==3){
                    Intent i = new Intent(context, SRSPdf.class);
                    i.putExtra("docID",sp.getSnewId());
                    i.putExtra("logo",p.getLogo());
                    i.putExtra("Project",p);
                    i.putExtra("ver",ver);
                    context.startActivity(i);}
            }
        });
    }

    @Override
    public int getItemCount() {
        return arr.size();
    }

    private static class VerViewHolder extends RecyclerView.ViewHolder{
        CardView cv;
        TextView stage,comment,status;
        View v;
        Button viewDoc;
        public VerViewHolder(@NonNull View itemView) {
            super(itemView);
            v = itemView;
            cv =  (CardView) itemView.findViewById(R.id.stage1item);
            stage = (TextView)itemView.findViewById(R.id.stage1title);
            status = (TextView)itemView.findViewById(R.id.StageStatus);
            comment = (TextView)itemView.findViewById(R.id.stage1Comment);
            viewDoc = (Button)itemView.findViewById(R.id.viewDoc);
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

