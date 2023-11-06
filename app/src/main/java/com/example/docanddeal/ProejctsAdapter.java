package com.example.docanddeal;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
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
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.progressindicator.CircularProgressIndicator;
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
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProejctsAdapter extends RecyclerView.Adapter {
    ArrayList<ProjectC> arr;
    Context context;
    String from;
    TextView empty;
    SponsorUser spon;

    public ProejctsAdapter(ArrayList<ProjectC> arr, Context context, TextView empty,String from, SponsorUser spon) {
        this.arr = arr;
        this.context = context;
        this.from = from;
        this.empty = empty;
        this.spon = spon;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.project_item,parent,false);
        return new ProejctsAdapter.PViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        String n = arr.get(position).getName();
        ((ProejctsAdapter.PViewHolder) holder).sname.setText(n);
        Picasso.get().load(Uri.parse(arr.get(position).getLogo())).resize(50,50)
                .centerCrop().into(((ProejctsAdapter.PViewHolder ) holder).img);
        ((ProejctsAdapter.PViewHolder) holder).v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!from.equals("Search")) {
                    DocumentReference dr = FirebaseFirestore.getInstance().collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getUid());
                    dr.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            String s = task.getResult().getString("type");
                            if ((s.compareTo("creator") == 0)) {
                                if (context.getClass().equals(IdeaOwnerProfilePage.class)) {
                                    try {
                                        Intent b = new Intent(Intent.ACTION_VIEW, Uri.parse(arr.get(holder.getAdapterPosition()).getLink()));
                                        context.startActivity(b);
                                    } catch (Exception e) {
                                        Toast.makeText(context, "The link is invalid", Toast.LENGTH_LONG).show();
                                    }
                                } else {
                                    Intent i = new Intent(context, CreatorMainVersions.class);
                                    i.putExtra("Project", arr.get(holder.getAdapterPosition()));
//                                i.putExtra("pid", arr.get(position));
                                    context.startActivity(i);
                                }
                            } else {
                                try {
                                    Intent b = new Intent(Intent.ACTION_VIEW, Uri.parse(arr.get(position).getLink()));
                                    context.startActivity(b);
                                } catch (Exception e) {
                                    Toast.makeText(context, "The link is invalid", Toast.LENGTH_LONG).show();
                                }
                            }
                        }
                    });
                    ;
                } else {
                    AlertDialog.Builder mDialog = new AlertDialog.Builder(context);
                    final View verview = LayoutInflater.from(context).inflate(R.layout.versionspopup, null);
                    mDialog.setView(verview);
                    RecyclerView rv = verview.findViewById(R.id.versionsRVpopup);
                    CircularProgressIndicator progressBar = verview.findViewById(R.id.progBarProjects);
                    rv.setHasFixedSize(true);
                    RecyclerView.LayoutManager lm = new GridLayoutManager(mDialog.getContext(), 3);
                    rv.setLayoutManager(lm);
                    ArrayList<Version> versionslist = arr.get(position).getVersions();
                    VersionAdapter da = new VersionAdapter(versionslist, context, arr.get(position), "Search", spon);
                    rv.setAdapter(da);
                    progressBar.setVisibility(View.GONE);
                    AlertDialog dialog = mDialog.create();
                    dialog.show();

                }
            }
        });

        ((ProejctsAdapter.PViewHolder) holder).v.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (from.equals("Main")) {
                    FirebaseAuth fAuth = FirebaseAuth.getInstance();
                    FirebaseFirestore fs = FirebaseFirestore.getInstance();
                    DocumentReference project, userprojects;
                    CollectionReference docs;
                    project = fs.collection("projects").document(arr.get(position).getPid());
                    userprojects = fs.collection("users").document(fAuth.getCurrentUser().getUid());
                    CollectionReference sharedprojects = fs.collection("shared_projects");
                    PopupMenu pm = new PopupMenu(context, v);
                    pm.getMenuInflater().inflate(R.menu.project_popup_menu, pm.getMenu());

                    final AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    pm.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            int sitem = item.getItemId();
                            if (sitem == R.id.deleteproj) {
                                builder.setTitle("Delete Project");
                                builder.setMessage("Please type \"" + arr.get(position).getName() + "\" below to delete the project");
                                final EditText projname = new EditText(context);
                                projname.setHint(arr.get(position).getName());
                                projname.setInputType(InputType.TYPE_CLASS_TEXT);
                                builder.setView(projname);
                                builder.setCancelable(false).setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        if (projname.getText().toString().trim().equals(arr.get(position).getName().trim())) {
                                            sharedprojects.whereEqualTo("pid", arr.get(position).getPid()).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                                @Override
                                                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                                    if (queryDocumentSnapshots == null) {
                                                        project.delete().addOnSuccessListener(new OnSuccessListener<Void>() {

                                                            @Override
                                                            public void onSuccess(Void aVoid) {

                                                            }
                                                        }).addOnFailureListener(new OnFailureListener() {
                                                            @Override
                                                            public void onFailure(@NonNull Exception e) {
                                                                Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
                                                            }
                                                        });
                                                        userprojects.update("projects", FieldValue.arrayRemove(arr.get(position).getPid())).addOnFailureListener(new OnFailureListener() {
                                                            @Override
                                                            public void onFailure(@NonNull Exception e) {
                                                                Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
                                                            }
                                                        });
                                                        userprojects.update("pnames", FieldValue.arrayRemove(arr.get(position).getName())).addOnFailureListener(new OnFailureListener() {
                                                            @Override
                                                            public void onFailure(@NonNull Exception e) {
                                                                Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
                                                            }
                                                        });
                                                        CollectionReference d;
                                                        d = fs.collection("documents");
                                                        d.whereEqualTo("pid", arr.get(position).getPid()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                                if (task.isSuccessful()) {
                                                                    DocumentReference specificdoc;
                                                                    ArrayList<Document> ids = new ArrayList<>();
                                                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                                                        Document d2 = new Document(document.get("type").toString(), document.getId().toString());
                                                                        ids.add(d2);
                                                                    }

                                                                    for (int i = 0; i < ids.size(); i++) {
                                                                        String type = ids.get(i).getType();
                                                                        String docid = ids.get(i).getDocid();
                                                                        if (type.equals("proposal"))
                                                                            specificdoc = fs.collection("proposalDoc").document(docid);
                                                                        else if (type.equals("vision"))
                                                                            specificdoc = fs.collection("visionDoc").document(docid);
                                                                        else if (type.equals("srs"))
                                                                            specificdoc = fs.collection("srsDoc").document(docid);
                                                                        else
                                                                            specificdoc = fs.collection("fullDoc").document(docid);
                                                                        d.document(docid).delete();
                                                                        specificdoc.delete();
                                                                    }
                                                                    arr.remove(position);
                                                                    notifyDataSetChanged();
                                                                    if (arr.isEmpty())
                                                                        empty.setVisibility(View.VISIBLE);

                                                                } else {
                                                                    Toast.makeText(context, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                                                }
                                                            }

                                                        });


                                                    } else {
                                                        if(queryDocumentSnapshots.isEmpty()){
                                                            project.delete().addOnSuccessListener(new OnSuccessListener<Void>() {

                                                                @Override
                                                                public void onSuccess(Void aVoid) {

                                                                }
                                                            }).addOnFailureListener(new OnFailureListener() {
                                                                @Override
                                                                public void onFailure(@NonNull Exception e) {
                                                                    Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
                                                                }
                                                            });
                                                            userprojects.update("projects", FieldValue.arrayRemove(arr.get(position).getPid())).addOnFailureListener(new OnFailureListener() {
                                                                @Override
                                                                public void onFailure(@NonNull Exception e) {
                                                                    Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
                                                                }
                                                            });
                                                            userprojects.update("pnames", FieldValue.arrayRemove(arr.get(position).getName())).addOnFailureListener(new OnFailureListener() {
                                                                @Override
                                                                public void onFailure(@NonNull Exception e) {
                                                                    Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
                                                                }
                                                            });
                                                            CollectionReference d;
                                                            d = fs.collection("documents");
                                                            d.whereEqualTo("pid", arr.get(position).getPid()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                                    if (task.isSuccessful()) {
                                                                        DocumentReference specificdoc;
                                                                        ArrayList<Document> ids = new ArrayList<>();
                                                                        for (QueryDocumentSnapshot document : task.getResult()) {
                                                                            Document d2 = new Document(document.get("type").toString(), document.getId().toString());
                                                                            ids.add(d2);
                                                                        }

                                                                        for (int i = 0; i < ids.size(); i++) {
                                                                            String type = ids.get(i).getType();
                                                                            String docid = ids.get(i).getDocid();
                                                                            if (type.equals("proposal"))
                                                                                specificdoc = fs.collection("proposalDoc").document(docid);
                                                                            else if (type.equals("vision"))
                                                                                specificdoc = fs.collection("visionDoc").document(docid);
                                                                            else if (type.equals("srs"))
                                                                                specificdoc = fs.collection("srsDoc").document(docid);
                                                                            else
                                                                                specificdoc = fs.collection("fullDoc").document(docid);
                                                                            d.document(docid).delete();
                                                                            specificdoc.delete();
                                                                        }
                                                                        arr.remove(position);
                                                                        notifyDataSetChanged();
                                                                        if (arr.isEmpty())
                                                                            empty.setVisibility(View.VISIBLE);

                                                                    } else {
                                                                        Toast.makeText(context, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                                                    }
                                                                }

                                                            });
                                                        }
                                                        else
                                                            Toast.makeText(context, "You cannot delete a shared project", Toast.LENGTH_LONG).show();

                                                    }
                                                }
                                            });

                                        }

                                    }
                                });
                                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                });

                                AlertDialog ad = builder.create();
                                ad.show();
                                return true;
                            } else if (sitem == R.id.editproj) {
                                Intent i = new Intent(context, EditProject.class);
                                i.putExtra("Project", arr.get(position));
                                i.putExtra("type", "creator");
                                context.startActivity(i);
                                return true;
                            } else
                                return false;
                        }
                    });
                    pm.show();
                    return true;
                } else
                    return false;

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
        View v;
        public PViewHolder(@NonNull View itemView) {
            super(itemView);
            v = itemView;
            img = itemView.findViewById(R.id.ProjectLogo);
            sname = (TextView)itemView.findViewById(R.id.projName);
        }
    }
}
