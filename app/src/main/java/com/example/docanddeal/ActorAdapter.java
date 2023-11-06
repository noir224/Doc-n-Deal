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

public class ActorAdapter  extends RecyclerView.Adapter {
    ArrayList<Actor> arr;
    Context context;
    RelativeLayout rl;
//    ArrayList<UseCase> ucs;
//    UseCase uc;


    public ActorAdapter(ArrayList<Actor> arr, Context context, RelativeLayout rl) {
        this.arr = arr;
        this.context = context;
        this.rl = rl;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.actor_item,parent,false);
        return new ActorAdapter.ActorViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Actor a = arr.get(position);
        ((ActorAdapter.ActorViewHolder ) holder).actor.setText(a.getName()+"");

        ((ActorAdapter.ActorViewHolder ) holder).deleteactor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                arr.remove(position);
                //ucs.get((uc.getUcnum()-1)).getActors().remove(position);
                notifyDataSetChanged();
                Snackbar.make(rl,"Actor is deleted", Snackbar.LENGTH_LONG).setAction("Undo", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        arr.add(position,a);
                        //ucs.get((uc.getUcnum()-1)).getActors().add(position,a);
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
    private static class ActorViewHolder extends RecyclerView.ViewHolder{
        TextView actor;
        ImageView deleteactor;
        View v;
        public ActorViewHolder(@NonNull View itemView) {
            super(itemView);
            v = itemView;
            actor = (TextView) itemView.findViewById(R.id.actor);
            deleteactor = (ImageView) itemView.findViewById(R.id.deleteactor);

        }
    }
}
