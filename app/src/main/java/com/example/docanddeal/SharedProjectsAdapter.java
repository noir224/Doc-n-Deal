package com.example.docanddeal;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class SharedProjectsAdapter extends RecyclerView.Adapter {
    ArrayList<SingleSharedProject> arr;
    Context context;
    TextView empty;
    SingleSharedProject ssp;

    public SharedProjectsAdapter(ArrayList<SingleSharedProject> arr, Context context, TextView empty) {
        this.arr = arr;
        this.context = context;
        this.empty = empty;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sharedproj_item,parent,false);
        return new SharedProjectsAdapter.PViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        String projectname = arr.get(position).getProject().getName();
        ((PViewHolder ) holder).stype.setText(arr.get(position).getSharedProjects().getType());
        String ver = arr.get(position).getSharedProjects().getProjectVersion();
        ((SharedProjectsAdapter.PViewHolder ) holder).sname.setText(projectname);
        ((SharedProjectsAdapter.PViewHolder ) holder).ver.setText(ver);
        Picasso.get().load(Uri.parse(arr.get(position).getProject().getLogo())).resize(50,50)
                .centerCrop().into(((SharedProjectsAdapter.PViewHolder ) holder).img);
        ((SharedProjectsAdapter.PViewHolder ) holder).v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, SharedProjectDetails.class);
                i.putExtra("project",arr.get(position));
                context.startActivity(i);
            }
        });
        if((arr.get(position).getSharedProjects().getType().equals("proposal"))) {
            if ((arr.get(position).getSharedProjects().getStage1().equals("pending")))
                ((PViewHolder) holder).img.setBorderColor(Color.parseColor("#3E2C41")); //semipurple
            if ((arr.get(position).getSharedProjects().getStage1().equals("accepted")))
                ((PViewHolder ) holder).img.setBorderColor(Color.parseColor("#8AC984")); //green
            if ((arr.get(position).getSharedProjects().getStage1().equals("accept_with_revision")))
                ((PViewHolder ) holder).img.setBorderColor(Color.parseColor("#E7A808")); //yellow
            if ((arr.get(position).getSharedProjects().getStage1().equals("rejected")))
                ((PViewHolder ) holder).img.setBorderColor(Color.parseColor("#FD4C4C")); //red
        }
        if((arr.get(position).getSharedProjects().getType().equals("vision"))) {
            if ((arr.get(position).getSharedProjects().getStage2().equals("pending")))
                ((PViewHolder) holder).img.setBorderColor(Color.parseColor("#3E2C41")); //semipurple
            if ((arr.get(position).getSharedProjects().getStage2().equals("accepted")))
                ((PViewHolder ) holder).img.setBorderColor(Color.parseColor("#8AC984")); //green
            if ((arr.get(position).getSharedProjects().getStage2().equals("accept_with_revision")))
                ((PViewHolder ) holder).img.setBorderColor(Color.parseColor("#E7A808")); //yellow
        }
        if((arr.get(position).getSharedProjects().getType().equals("srs"))) {
            if ((arr.get(position).getSharedProjects().getStage3().equals("pending")))
                ((PViewHolder) holder).img.setBorderColor(Color.parseColor("#3E2C41")); //semipurple
            if ((arr.get(position).getSharedProjects().getStage3().equals("accepted")))
                ((PViewHolder ) holder).img.setBorderColor(Color.parseColor("#8AC984")); //green
            if ((arr.get(position).getSharedProjects().getStage3().equals("accept_with_revision")))
                ((PViewHolder ) holder).img.setBorderColor(Color.parseColor("#E7A808")); //yellow
        }

    }

    @Override
    public int getItemCount() {
        return arr.size();
    }

    private static class PViewHolder extends RecyclerView.ViewHolder{
        CircleImageView img;
        TextView sname,ver,stype;
        View v;
        public PViewHolder(@NonNull View itemView) {
            super(itemView);
            v = itemView;
            img = itemView.findViewById(R.id.sharedLogo);
            sname = (TextView)itemView.findViewById(R.id.sharedName);
            stype= (TextView) itemView.findViewById(R.id.sharedType);
            ver = (TextView)itemView.findViewById(R.id.sharedVer);
            img.setBorderWidth(12);


        }
    }
}
