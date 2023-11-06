package com.example.docanddeal;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class AppliedToAdapter extends RecyclerView.Adapter {
    ArrayList<SharedWith> arr;
    Context context;
    TextView empty;

    public AppliedToAdapter(ArrayList<SharedWith> arr, Context context, TextView empty) {
        this.arr = arr;
        this.context = context;
        this.empty = empty;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.appliedto_item,parent,false);
        return new AppliedToAdapter.PViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        String sponsname = arr.get(position).getSponsor().getUsername();
        ((AppliedToAdapter.PViewHolder ) holder).sname.setText(sponsname);
        Picasso.get().load(Uri.parse(arr.get(position).getSponsor().getImagepath())).resize(50,50)
                .centerCrop().into(((AppliedToAdapter.PViewHolder ) holder).img);
        String color1 = getColor(arr.get(position).getSd().getStage1());
        String color2 = getColor(arr.get(position).getSd().getStage2());
        String color3 = getColor(arr.get(position).getSd().getStage3());
        ((AppliedToAdapter.PViewHolder ) holder).st1.setColorFilter(Color.parseColor(color1));
        ((AppliedToAdapter.PViewHolder ) holder).st2.setColorFilter(Color.parseColor(color2));
        ((AppliedToAdapter.PViewHolder ) holder).st3.setColorFilter(Color.parseColor(color3));
        ((AppliedToAdapter.PViewHolder ) holder).v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent i = new Intent(context, AppliedToDetails.class);
//                i.putExtra("appliedto",arr.get(position));
//                context.startActivity(i);
                FirebaseFirestore fs = FirebaseFirestore.getInstance();
                AlertDialog.Builder mDialog = new AlertDialog.Builder(context);
                final View shareview = LayoutInflater.from(context).inflate(R.layout.status_popup,null);
                mDialog.setView(shareview);
                RecyclerView rv = shareview.findViewById(R.id.statusRV);
                rv.setHasFixedSize(true);
                RecyclerView.LayoutManager lm = new LinearLayoutManager(mDialog.getContext());
                rv.setLayoutManager(lm);
                final shareAdapter[] da = new shareAdapter[1];
                ArrayList<Stage> stages = new ArrayList<>();
                Stage stage1 = new Stage(1, "none");
                stages.add(0,stage1);
                Stage stage2 = new Stage(2, "none");
                stages.add(1,stage2);
                Stage stage3 = new Stage(3, "none");
                stages.add(2,stage3);
                fs.collection("shared_documents").whereEqualTo("pid",arr.get(position).getSd().getPid()).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                        for (DocumentSnapshot ds :queryDocumentSnapshots) {
                            SharedDoc sdoc = ds.toObject(SharedDoc.class);
                            Stage stage;
                            if(sdoc.getType().equals("proposal")){
                                stages.get(0).setStatus(sdoc.getStage1());
                            }else if(sdoc.getType().equals("vision")){
                                stages.get(1).setStatus(sdoc.getStage1());
                            }else{
                                stages.get(2).setStatus(sdoc.getStage1());
                            }

                        }
                        if(stages!=null) {
                            StatusAdapter da = new StatusAdapter(stages, context);
                            rv.setAdapter(da);

                        }
                    }
                });

                AlertDialog dialog = mDialog.create();
                dialog.show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return arr.size();
    }

    private static class PViewHolder extends RecyclerView.ViewHolder{
        CircleImageView img;
        TextView sname;
        ImageView st1,st2,st3;
        View v;
        public PViewHolder(@NonNull View itemView) {
            super(itemView);
            v = itemView;
            img = itemView.findViewById(R.id.sponsorProPic);
            sname = (TextView)itemView.findViewById(R.id.sponsorName);
            st1 = (ImageView) itemView.findViewById(R.id.stage1Cir);
            st2 = (ImageView) itemView.findViewById(R.id.stage2Cir);
            st3 = (ImageView) itemView.findViewById(R.id.stage3Cir);
            //st1.setImageDrawable();
        }
    }
    private String getColor(String status){
        if(status.equals("none"))
            return "#c6c9cf";
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