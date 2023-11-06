package com.example.docanddeal;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class SponsorStagesAdapter extends RecyclerView.Adapter {
    ArrayList<Stage> arr;
    Context context;



    public SponsorStagesAdapter(ArrayList<Stage> arr, Context context) {
        this.arr = arr;
        this.context = context;
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
        if(n==1){
            ((SponsorStagesAdapter.VerViewHolder ) holder).stage.setText("Stage 1 (Proposal)");
            ((SponsorStagesAdapter.VerViewHolder ) holder).viewmore.setText("View Proposal");
        }else if(n==2){
            ((SponsorStagesAdapter.VerViewHolder ) holder).stage.setText("Stage 2 (Vision)");
            ((SponsorStagesAdapter.VerViewHolder ) holder).viewmore.setText("View Vision");
        }else{
            ((SponsorStagesAdapter.VerViewHolder ) holder).stage.setText("Stage 3 (SRS)");
            ((SponsorStagesAdapter.VerViewHolder ) holder).viewmore.setText("View SRS");
        }
        String color = getColor(arr.get(position).getStatus());
        ((SponsorStagesAdapter.VerViewHolder ) holder).cv.setCardBackgroundColor(Color.parseColor(color));


    }

    @Override
    public int getItemCount() {
        return arr.size();
    }

    private static class VerViewHolder extends RecyclerView.ViewHolder{
        CardView cv;
        TextView stage;
        Button viewmore;
        View v;
        public VerViewHolder(@NonNull View itemView) {
            super(itemView);
            v = itemView;
            cv =  (CardView) itemView.findViewById(R.id.cardviewitem);
            stage = (TextView)itemView.findViewById(R.id.stage);
            viewmore = (Button)itemView.findViewById(R.id.viewDoc);
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
