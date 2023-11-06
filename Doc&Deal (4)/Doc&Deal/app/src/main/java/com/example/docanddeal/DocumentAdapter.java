package com.example.docanddeal;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class DocumentAdapter extends RecyclerView.Adapter {
    ArrayList<Document> arr;
    Context context;
    TextView empty;

    public DocumentAdapter(ArrayList<Document> arr, Context context, TextView empty) {
        this.arr = arr;
        this.context = context;
        this.empty = empty;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.doc_item,parent,false);
        return new DocumentAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
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
                            return true;
                        }
                        else if (sitem == R.id.sharesponsor){
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
