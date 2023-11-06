package com.example.docanddeal;

import android.annotation.SuppressLint;
import android.content.Context;
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



    public StatusAdapter(ArrayList<Stage> arr, Context context) {
        this.arr = arr;
        this.context = context;
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
        String com = arr.get(position).getComment();
        if(com==null)
            com= "No comment yet";
        if(n==1){
            ((StatusAdapter.VerViewHolder ) holder).stage.setText("Stage 1 (Proposal)");
            ((VerViewHolder ) holder).comment.setText(com);
        }else if(n==2){
            ((StatusAdapter.VerViewHolder ) holder).stage.setText("Stage 2 (Vision)");
            ((StatusAdapter.VerViewHolder ) holder).comment.setText(com);
        }else{
            ((StatusAdapter.VerViewHolder ) holder).stage.setText("Stage 3 (SRS)");
            ((StatusAdapter.VerViewHolder ) holder).comment.setText(com);
        }
        String color = getColor(arr.get(position).getStatus());
        ((StatusAdapter.VerViewHolder ) holder).cv.setCardBackgroundColor(Color.parseColor(color));


    }

    @Override
    public int getItemCount() {
        return arr.size();
    }

    private static class VerViewHolder extends RecyclerView.ViewHolder{
        CardView cv;
        TextView stage,comment;
        View v;
        public VerViewHolder(@NonNull View itemView) {
            super(itemView);
            v = itemView;
            cv =  (CardView) itemView.findViewById(R.id.cvitem);
            stage = (TextView)itemView.findViewById(R.id.stagetitle);
            comment = (TextView)itemView.findViewById(R.id.stageComment);
        }
    }

    private String getColor(String status){
        if(status.equals("none"))
            return "#9599A4";
        else if(status.equals("pending"))
            return "#6E85B2";
        else if(status.equals("rejected"))
            return "#FD4C4C";
        else if(status.equals("accepted"))
            return "#8AC984";
        else
            return "#E7A808";

    }

}

