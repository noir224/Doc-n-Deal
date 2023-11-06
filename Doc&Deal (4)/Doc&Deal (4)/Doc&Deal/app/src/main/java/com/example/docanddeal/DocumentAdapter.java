package com.example.docanddeal;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class DocumentAdapter extends RecyclerView.Adapter {
    ArrayList<Document> arr;
    Context context;
    TextView empty;
    ProjectC p;
    Version ver;

    public DocumentAdapter(ArrayList<Document> arr, Context context, TextView empty, ProjectC p, Version ver) {
        this.arr = arr;
        this.context = context;
        this.empty = empty;
        this.p = p;
        this.ver = ver;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.doc_item,parent,false);
        return new DocumentAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        ((DocumentAdapter.ViewHolder ) holder).sname.setText(arr.get(position).getName()+"");
        ((DocumentAdapter.ViewHolder ) holder).v.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                FirebaseAuth fAuth = FirebaseAuth.getInstance();
                FirebaseFirestore fs = FirebaseFirestore.getInstance();
                DocumentReference d,specificdoc;
                String type = arr.get(position).getType();
                String docid =arr.get(position).getDocid();
                d = fs.collection("documents").document(docid);
                if(type.equals("proposal"))
                    specificdoc = fs.collection("proposalDoc").document(docid);
                else if(type.equals("vision"))
                    specificdoc = fs.collection("visionDoc").document(docid);
                else if(type.equals("srs"))
                    specificdoc = fs.collection("srsDoc").document(docid);
                else
                    specificdoc = fs.collection("fullDoc").document(docid);

                PopupMenu pm = new PopupMenu(context,v);
                pm.getMenuInflater().inflate(R.menu.document_popup_menu,pm.getMenu());

                final AlertDialog.Builder builder = new AlertDialog.Builder(context);
                pm.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        int sitem = item.getItemId();
                        if(sitem == R.id.deletedoc){
                            builder.setTitle("Delete Document");
                            builder.setMessage("Are you sure you want to delete "+arr.get(position).getName()+"?");
                            builder.setCancelable(false).setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    specificdoc.delete();
                                    d.delete();
                                    arr.remove(position);
                                    notifyDataSetChanged ();
                                    if(arr.isEmpty())
                                        empty.setVisibility(View.VISIBLE);
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
                        }
                        else if (sitem == R.id.download){
                            return true;
                        }
                        else if (sitem == R.id.shareemail){
                            Intent e=new Intent(Intent.ACTION_SEND);
                            e.setType("text/plain");
                            context.startActivity(Intent.createChooser(e,"send email:"));
                            return true;
                        }
                        else if (sitem == R.id.sharesponsor){
                            AlertDialog.Builder mDialog = new AlertDialog.Builder(context);
                            final View shareview = LayoutInflater.from(context).inflate(R.layout.sharepopup,null);
                            mDialog.setView(shareview);
                            EditText search = shareview.findViewById(R.id.searchForDocShare);
                            RecyclerView rv = shareview.findViewById(R.id.shareRV);
                            rv.setHasFixedSize(true);
                            RecyclerView.LayoutManager lm = new LinearLayoutManager(mDialog.getContext());
                            rv.setLayoutManager(lm);
                            final shareAdapter[] da = new shareAdapter[1];
                            fs.collection("users").whereEqualTo("type","sponsor").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                @Override
                                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                    ArrayList<SponsorUser> sponsorUsers = new ArrayList<>();
                                    for (DocumentSnapshot doc:queryDocumentSnapshots ) {
                                        SponsorUser su = doc.toObject(SponsorUser.class);
                                        sponsorUsers.add(su);
                                    }
                                    if(sponsorUsers!=null) {
                                        da[0] = new shareAdapter(sponsorUsers, mDialog.getContext(),
                                                arr.get(position),p,ver);
                                        rv.setAdapter(da[0]);
                                    }
                                    search.addTextChangedListener(new TextWatcher() {
                                        @Override
                                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                                        }

                                        @Override
                                        public void onTextChanged(CharSequence s, int start, int before, int count) {

                                        }

                                        @Override
                                        public void afterTextChanged(Editable s) {
                                            ArrayList<SponsorUser> filteredlist = new ArrayList<>();
                                            for (SponsorUser suser : sponsorUsers) {
                                                if(suser.getUsername().trim().toLowerCase().contains(s.toString().trim().toLowerCase())){
                                                    filteredlist.add(suser);
                                                }
                                            }
                                            da[0].filterlist(filteredlist);
                                        }
                                    });
                                }
                            });
                            AlertDialog dialog = mDialog.create();
                            dialog.show();

                            return true;
                        }
                        else
                            return false;
                    }
                });
                pm.show();
                return true;
            }
        });
    }

    private void filter(String s) {

    }

    @Override
    public int getItemCount() {
        return arr.size();
    }

    private static class ViewHolder extends RecyclerView.ViewHolder{
        ImageView img;
        TextView sname;
        View v;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            v = itemView;
            img = itemView.findViewById(R.id.docIcon);
            sname = (TextView)itemView.findViewById(R.id.docName);
        }
    }


}
