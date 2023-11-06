package com.example.docanddeal;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import java.util.ArrayList;

public class PublicProjectAdapter extends RecyclerView.Adapter<PublicProjectAdapter.ViewHolder>{
    private static final String TAG = "RecyclerViewAdapter";
    private ArrayList<String> pNames= new ArrayList<>();
    private Context pContext;

    public PublicProjectAdapter(ArrayList<String> pNames, Context pContext) {
        this.pNames = pNames;
        this.pContext = pContext;
    }

    @NonNull
    @Override
    public PublicProjectAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d(TAG, "ocCreateViewHoldre: called.");
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.project_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PublicProjectAdapter.ViewHolder holder, int position) {
        Log.d(TAG, "ocBindViewHoldre: called.");
        /*Glide.with(pContext)
                .asBitmap()
                .load(pImageURLs)
                .into(holder.pimg);*/
        holder.pname.setText(pNames.get(position));

       /* holder.pimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: clicked on an image: called."+pNames.get(position));
                Toast.makeText(pContext, pNames.get(position), Toast.LENGTH_SHORT).show();
            }
        });*/

    }

    @Override
    public int getItemCount() {
        return pNames.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView pimg;
        TextView pname;
        public ViewHolder(View itemView){
            super(itemView);
            pimg= itemView.findViewById(R.id.projIcon);
            pname=itemView.findViewById(R.id.projName);

        }
    }
}
