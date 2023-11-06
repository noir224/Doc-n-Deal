package com.example.docanddeal;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class SponsorsAdapter extends RecyclerView.Adapter implements Filterable {
    ArrayList<SponsorUser> sponsorsALL;
    ArrayList<SponsorUser> sponsors;
    Context context;
    TextView empty;


    public SponsorsAdapter(ArrayList<SponsorUser> sponsors, Context context, TextView empty) {
        this.sponsorsALL = sponsors;
        this.context = context;
        this.empty = empty;
        this.sponsors=new ArrayList<>(sponsorsALL);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sponsor_item,parent,false);
        return new SponsorsAdapter.SViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        String n = sponsors.get(position).getUsername();
        String c = sponsors.get(position).getField();
        ((SponsorsAdapter.SViewHolder ) holder).sname.setText(n);
        ((SponsorsAdapter.SViewHolder ) holder).scateg.setText(c);

        Picasso.get().load(Uri.parse(sponsors.get(position).getImagepath())).resize(50,50).centerCrop().into(((SponsorsAdapter.SViewHolder ) holder).img);
        ((SponsorsAdapter.SViewHolder) holder).VM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DocumentReference dr= FirebaseFirestore.getInstance().collection("users").document(sponsors.get(holder.getAdapterPosition()).getId());
                dr.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        Intent i= new Intent(context, SponsorProfile.class);
                        i.putExtra("sponsorID",sponsors.get(position).getId());
                        context.startActivity(i);
                    }
                });
            }
        });

    }

    @Override
    public int getItemCount() {
        return sponsors.size();
    }

    Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            ArrayList<SponsorUser> filteredList= new ArrayList<>();
            if (charSequence.toString().isEmpty()){
                filteredList.addAll(sponsorsALL);
            }
            else{
                for(SponsorUser s : sponsorsALL){
                    if(s.getUsername().toLowerCase().contains(charSequence.toString().toLowerCase())){
                        filteredList.add(s);
                    }
                }
            }
            FilterResults filterResults= new FilterResults();
            filterResults.values=filteredList;
            return filterResults;
        }
        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            sponsors.clear();
            sponsors.addAll((Collection<? extends SponsorUser>) filterResults.values);
            notifyDataSetChanged();
        }
    };

    @Override
    public Filter getFilter() {

        return filter;
    }

    private static class SViewHolder extends RecyclerView.ViewHolder{
        CircleImageView img;
        TextView sname, scateg;
        Button VM;
        View v;
        public SViewHolder(@NonNull View itemView) {
            super(itemView);
            v = itemView;
            img = itemView.findViewById(R.id.logoOFsponsor);
            sname = (TextView)itemView.findViewById(R.id.nameOFspons);
            scateg = (TextView)itemView.findViewById(R.id.categOFspons);
            VM = (Button)itemView.findViewById(R.id.viewmore);
        }
    }
    public ArrayList<SponsorUser> getList(){
        return sponsorsALL;
    }

}
