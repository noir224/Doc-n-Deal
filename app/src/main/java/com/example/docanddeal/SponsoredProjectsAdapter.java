package com.example.docanddeal;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Parcelable;
import android.text.InputType;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.URLUtil;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Array;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class SponsoredProjectsAdapter extends RecyclerView.Adapter{
    private ArrayList<SRProjects> itemm  = new ArrayList<>();
    private Context mContext;
    private String pid;
    TextView empty, SP,RP;

    public SponsoredProjectsAdapter(ArrayList<SRProjects> itemm, Context mContext,TextView empty, TextView SP, TextView RP) {
        this.itemm = itemm;
        this.mContext = mContext;
        this.empty = empty;
        this.SP = SP;
        this.RP = RP;
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
        pid = itemm.get(position).getSpid();

        ((ViewHolder) holder).cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if((URLUtil.isValidUrl(itemm.get(position).getLink()))){
                    Intent i = new Intent(Intent.ACTION_VIEW,Uri.parse(itemm.get(position).getLink()));
                    mContext.startActivity(i);}
                else if (itemm.get(position).getLink().isEmpty()){
                    Toast toast = Toast.makeText(mContext,"This project doesn't have a link",Toast.LENGTH_SHORT);
                    toast.show();
                }
                else {
                    Toast toast = Toast.makeText(mContext,"This project doesn't have a valid link",Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });

        ((ViewHolder) holder).cardView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                FirebaseAuth fAuth = FirebaseAuth.getInstance();
                FirebaseFirestore fs = FirebaseFirestore.getInstance();
                DocumentReference project,userprojects;
                CollectionReference docs;
                project = fs.collection("sponsored_projects").document(pid);
                userprojects = fs.collection("users").document(fAuth.getCurrentUser().getUid());

                PopupMenu menu = new PopupMenu(view.getContext(),view);
                menu.getMenuInflater().inflate(R.menu.edit_delete_projects,menu.getMenu());
                final AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        int sitem = menuItem.getItemId();
                        if(sitem == R.id.editProjMenu){
                            Toast.makeText(mContext,"edit",Toast.LENGTH_SHORT).show();
                            Intent i = new Intent(mContext, EditProject.class);
                            i.putExtra("sProject", itemm.get(position));
                            i.putExtra("type","sponsor");

                            mContext.startActivity(i);
                            return true;
                        }
                        else if (sitem == R.id.deleteProjMenu){
                            final EditText projname = new EditText(mContext);
                            projname.setHint(itemm.get(position).getName());
                            projname.setInputType(InputType.TYPE_CLASS_TEXT);
                            final AlertDialog.Builder builder = new android.app.AlertDialog.Builder(mContext);
                            builder.setTitle("Delete")
                                    .setMessage("Do you want to Delete this Project Permanently?")
                                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {

                                        }
                                    }).setPositiveButton("Delete Permanently", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    project.delete();
                                    userprojects.update("sponsoredProjects", FieldValue.arrayRemove(itemm.get(position).getSpid()));
                                    itemm.remove(position);
                                    notifyDataSetChanged();
                                    if(itemm.size()==0)
                                        SP.setText(0+"");
                                    else {
                                        SP.setText(itemm.size() / itemm.size() * 100 + "");
                                        RP.setText(0 + "");
                                    }
                                    if(itemm.isEmpty())
                                        empty.setVisibility(View.VISIBLE);
                                    Toast.makeText(mContext,"item deleted",Toast.LENGTH_SHORT).show();

                                }
                            });

                            AlertDialog ad = builder.create();
                            ad.show();
                            return true;
                        }
                        else
                            return false;
                    }

                });
                menu.show();
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return itemm.size(); }

    private class ViewHolder extends RecyclerView.ViewHolder{
        CircleImageView image;
        TextView name, type;
        View v;
        CardView cardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            v = itemView;
            image = itemView.findViewById(R.id.s1image);
            name = itemView.findViewById(R.id.Projectforcompany);
            type = itemView.findViewById(R.id.descOfproject);
            cardView = itemView.findViewById(R.id.cardviewProj);
        }
    }
}