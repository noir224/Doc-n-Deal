package com.example.docanddeal;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

public class RelatedAdapter  extends RecyclerView.Adapter {
    ArrayList<Relationship> arr;
    Context context;
    RelativeLayout rl;
    UseCase uc;
    ArrayList<UseCase> ucs;


    public RelatedAdapter(ArrayList<Relationship> arr, Context context, RelativeLayout rl, UseCase uc, ArrayList<UseCase> ucs) {
        this.arr = arr;
        this.context = context;
        this.rl = rl;
        this.uc = uc;
        this.ucs = ucs;

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.relateduc_item,parent,false);
        return new RelatedAdapter.RelatedViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Relationship r = arr.get(position);
        String target = r.getTarget();
        String source = r.getSource();
        String targetName ="";
        String sourceName="";
        String type ="";

        for (UseCase uc1 : ucs) {
            if(uc1.getId().equals(target)) {
                targetName = uc1.getName();

            }
            if (uc1.getId().equals(source)) {
                sourceName = uc1.getName();

            }

        }
        if(target.equals(uc.getId())){
            if(r.getType().equals("extend")) {
                type = "Extended by " + sourceName + ".";
                ((RelatedAdapter.RelatedViewHolder ) holder).related.setText(type);
            }
            else if(r.getType().equals("include")) {
                type = "Included by " + sourceName + ".";
                ((RelatedAdapter.RelatedViewHolder ) holder).related.setText(type);
            }
            else
                type="";
        }else{
            if(r.getType().equals("extend")) {
                type = "Extends " + targetName + ".";
                ((RelatedAdapter.RelatedViewHolder ) holder).related.setText(type);
            }
            else if(r.getType().equals("include")) {
                type = "Include " + targetName + ".";
                ((RelatedAdapter.RelatedViewHolder ) holder).related.setText(type);
            }else{

            }
        }


        ((RelatedAdapter.RelatedViewHolder ) holder).deleterelatedr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                arr.remove(position);
                notifyDataSetChanged();
                Snackbar.make(rl,"Actor is deleted", Snackbar.LENGTH_LONG).setAction("Undo", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        arr.add(position,r);
                        notifyDataSetChanged();
                    }
                }).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return arr.size();
    }
    private static class RelatedViewHolder extends RecyclerView.ViewHolder{
        TextView related;
        ImageView deleterelatedr;
        View v;
        public RelatedViewHolder(@NonNull View itemView) {
            super(itemView);
            v = itemView;
            related = (TextView) itemView.findViewById(R.id.related);
            deleterelatedr = (ImageView) itemView.findViewById(R.id.deleterelatedr);

        }
    }
}
