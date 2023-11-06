package com.example.docanddeal;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.lang.reflect.Array;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class SponsoredProjectsAdapter extends RecyclerView.Adapter{
    private ArrayList<SponsoredProjects> itemm  = new ArrayList<>();
    private Context mContext;
    TextView empty;

    public SponsoredProjectsAdapter(ArrayList<SponsoredProjects> itemm, Context mContext, TextView empty) {
        this.itemm = itemm;
        this.mContext = mContext;
        this.empty = empty;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sponsored_projects_profile,parent,false);
        return new SponsoredProjectsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        ((SponsoredProjectsAdapter.ViewHolder ) holder).name.setText(itemm.get(position).getName());
        ((SponsoredProjectsAdapter.ViewHolder ) holder).type.setText(itemm.get(position).getType());
        Picasso.get().load(Uri.parse(itemm.get(position).getLogo())).resize(50,50).centerCrop().into(((SponsoredProjectsAdapter.ViewHolder ) holder).image);

        ((SponsoredProjectsAdapter.ViewHolder ) holder).v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_VIEW,Uri.parse(itemm.get(position).getLink()));
                mContext.startActivity(i);
            }
        });

    }

    @Override
    public int getItemCount() {
        return itemm.size();
    }


    private class ViewHolder extends RecyclerView.ViewHolder{
        CircleImageView image;
        TextView name, type;
        View v;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            v = itemView;
            image = itemView.findViewById(R.id.s1image);
            name = itemView.findViewById(R.id.Projectforcompany);
            type = itemView.findViewById(R.id.descOfproject);

        }
    }
}