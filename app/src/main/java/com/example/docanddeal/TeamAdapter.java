package com.example.docanddeal;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

public class TeamAdapter extends RecyclerView.Adapter {
    ArrayList<TeamMember> arr;
    Context context;
    RelativeLayout pro2;

    public TeamAdapter(ArrayList<TeamMember> arr, Context context,RelativeLayout prop2) {
        this.arr = arr;
        this.context = context;
        this.pro2=prop2;

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.team_member,parent,false);
        return new TeamAdapter.cViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((TeamAdapter.cViewHolder ) holder).tmName.setText(arr.get(position).getName()+"");
        ArrayList<String> c = arr.get(position).getCapabilities();
        if(c.contains(((TeamAdapter.cViewHolder ) holder).TC1.getText().toString())){
            ((TeamAdapter.cViewHolder ) holder).TC1.setChecked(true);}
        if(c.contains(((TeamAdapter.cViewHolder ) holder).TC2.getText().toString())){
            ((TeamAdapter.cViewHolder ) holder).TC2.setChecked(true);}
        if(c.contains(((TeamAdapter.cViewHolder ) holder).TC3.getText().toString())){
            ((TeamAdapter.cViewHolder ) holder).TC3.setChecked(true);}
        if(c.contains(((TeamAdapter.cViewHolder ) holder).TC4.getText().toString())){
            ((TeamAdapter.cViewHolder ) holder).TC4.setChecked(true);}
        if(c.contains(((TeamAdapter.cViewHolder ) holder).TC5.getText().toString())){
            ((TeamAdapter.cViewHolder ) holder).TC5.setChecked(true);}
        ((TeamAdapter.cViewHolder) holder).delExp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int actualPosition = holder.getAdapterPosition();
                TeamMember b = arr.get(actualPosition);
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
        ((TeamAdapter.cViewHolder ) holder).v1.setOnLongClickListener(new View.OnLongClickListener() {
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
        EditText tmName ;
        CheckBox TC1  ;
        CheckBox TC2 ;
        CheckBox TC3  ;
        CheckBox TC4  ;
        CheckBox TC5  ;
        ImageView delExp;
        View v1;
        public cViewHolder(@NonNull View itemView) {
            super(itemView);
            v1 = itemView;
            tmName = (EditText) v1.findViewById(R.id.teamMemberName);
            TC1 = (CheckBox) v1.findViewById(R.id.tc1);
            TC2 = (CheckBox) v1.findViewById(R.id.tc2);
            TC3 = (CheckBox) v1.findViewById(R.id.tc3);
            TC4 = (CheckBox) v1.findViewById(R.id.tc4);
            TC5 = (CheckBox) v1.findViewById(R.id.tc5);
            delExp  = (ImageView) itemView.findViewById(R.id.deleteMember);
        }
    }
}
