package com.example.docanddeal;

import android.app.AlertDialog;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
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

public class AbbrevAdapter extends RecyclerView.Adapter {
    ArrayList<Abbrev> arr;
    Context context;
    RelativeLayout pro2;

    public AbbrevAdapter(ArrayList<Abbrev> arr, Context context,RelativeLayout prop2) {
        this.arr = arr;
        this.context = context;
        this.pro2=prop2;

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.explination_proposal,parent,false);
        return new AbbrevAdapter.cViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((AbbrevAdapter.cViewHolder ) holder).abbrev.setText(arr.get(position).getName()+"");
        ((cViewHolder ) holder).delExp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int actualPosition = holder.getAdapterPosition();
                Abbrev b = arr.get(actualPosition);
                arr.remove(actualPosition);
                notifyItemRemoved(actualPosition);
                notifyItemRangeChanged(actualPosition, arr.size());
                Snackbar.make(pro2,"The item is deleted", Snackbar.LENGTH_LONG).setAction("UNDO", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        arr.add(b);
                       // notifydatasetchanged(holder.getAdapterPosition());
                        notifyDataSetChanged();
                        notifyItemRangeChanged(holder.getAdapterPosition(), arr.size());
                                  }
                }).show();
            }
        });
        ((AbbrevAdapter.cViewHolder ) holder).v.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return false;
            }
        });


    }

    @Override
    public int getItemCount() {
        return arr.size();
    }

    private static class cViewHolder extends RecyclerView.ViewHolder{
        EditText expli;
        EditText abbrev;
        ImageView delExp;
        View v;
        public cViewHolder(@NonNull View itemView) {
            super(itemView);
            v = itemView;
            expli = (EditText) itemView.findViewById(R.id.exp);
            abbrev = (EditText) itemView.findViewById(R.id.abbName);
            delExp = (ImageView) itemView.findViewById(R.id.deleteexp);
        }
    }
}
