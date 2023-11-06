package com.example.docanddeal;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.text.Html;
import android.text.InputType;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.CharacterStyle;
import android.text.style.ForegroundColorSpan;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;

public class UseCaseAdapter  extends RecyclerView.Adapter {
    ArrayList<UseCase> arr;
    Context context;
    ArrayList<Abbrev> aexplination;
    Version ver;
    ProjectC pro;
    SRSDocument srsd;
    String cameFrom, docID;



    public UseCaseAdapter(ArrayList<UseCase> arr, Context context, ArrayList<Abbrev> aexplination,
                          Version ver, ProjectC pro, SRSDocument srsd, String cameFrom, String docID) {
        this.arr = arr;
        this.context = context;
        this.aexplination = aexplination;
        this.ver = ver;
        this.pro = pro;
        this.srsd = srsd;
        this.cameFrom = cameFrom;
        this.docID = docID;

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.usecase_list_item,parent,false);
        return new UseCaseAdapter.UCViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        UseCase uc = arr.get(position);
        ((UseCaseAdapter.UCViewHolder) holder).ucnumname.setText("Usecase"+(position+1)+" ("+uc.getName()+")");
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        ((UseCaseAdapter.UCViewHolder) holder).deleteuc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                builder.setTitle("Delete Project");
                builder.setMessage("Please type \"" + arr.get(position).getName() + "\" below to delete the project");
                final EditText ucname = new EditText(context);
                ucname.setHint(arr.get(position).getName());
                ucname.setInputType(InputType.TYPE_CLASS_TEXT);
                builder.setView(ucname);
                builder.setCancelable(false).setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (ucname.getText().toString().trim().equals(arr.get(position).getName().trim())) {
                            String id = arr.get(position).getId();
                            arr.remove(position);
                            for(int i=0;i<arr.size();i++){
                                ArrayList<Relationship> rels = arr.get(i).getRelationships();
                                for(int j=0;j<rels.size();j++){
                                    Relationship r = rels.get(j);
                                    if(r.getSource().equals(id) || r.getTarget().equals(id)){
                                        rels.remove(j);
                                        j--;
                                    }

                                }
                            }
                            for(int i=position;i<arr.size();i++){
                                int num = arr.get(i).getUcnum()-1;
                                arr.get(i).setUcnum(num);
                            }

                            notifyDataSetChanged();
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

            }
        });
        ((UseCaseAdapter.UCViewHolder) holder).edituc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context,EditUsecase.class);
                i.putExtra("uc",uc);
                i.putExtra("sd",srsd);
                i.putExtra("ucs",arr);
                i.putExtra("Version",ver);
                i.putExtra("exp",aexplination);
                i.putExtra("Project",pro);
                i.putExtra("cameFrom", cameFrom);
                if(docID != null)
                    i.putExtra("docID", docID);
                context.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return arr.size();
    }
    private static class UCViewHolder extends RecyclerView.ViewHolder{
        TextView ucnumname;
        ImageView edituc,deleteuc;
        View v;
        public UCViewHolder(@NonNull View itemView) {
            super(itemView);
            v = itemView;
            ucnumname= (TextView) v.findViewById(R.id.ucnumname);
            edituc= (ImageView) v.findViewById(R.id.edituc);
            deleteuc= (ImageView) v.findViewById(R.id.deleteuc);
        }
    }


}
