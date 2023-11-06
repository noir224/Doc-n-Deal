package com.example.docanddeal;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

public class RowAdapter extends RecyclerView.Adapter {
    ArrayList<Row> arr;
    Context context;
    RelativeLayout rl;
    Version ver;
    UseCase uc;
    ProjectC pro;
    ArrayList<Abbrev> aexplination;
    Alternative alt;
    SRSDocument srsd;
    String from,cameFrom,docID;


    public RowAdapter(ArrayList<Row> arr, Context context, RelativeLayout rl, Version ver, UseCase uc,
                      ProjectC pro, ArrayList<Abbrev> aexplination, Alternative alt, SRSDocument srsd,String from,String cameFrom, String docID) {
        this.arr = arr;
        this.context = context;
        this.rl = rl;
        this.ver = ver;
        this.uc = uc;
        this.pro = pro;
        this.aexplination = aexplination;
        this.alt = alt;
        this.from=from;
        this.srsd = srsd;
        this.docID = docID;
        this.cameFrom = cameFrom;
    }

    public RowAdapter(ArrayList<Row> arr, Context context, RelativeLayout rl, Version ver, UseCase uc,
                      ProjectC pro, ArrayList<Abbrev> aexplination, SRSDocument srsd,String from,String cameFrom, String docID) {
        this.arr = arr;
        this.context = context;
        this.rl = rl;
        this.ver = ver;
        this.uc = uc;
        this.pro = pro;
        this.aexplination = aexplination;
        this.srsd = srsd;
        this.from=from;
        this.docID = docID;
        this.cameFrom = cameFrom;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_item,parent,false);
        return new RowAdapter.RowViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Row row = arr.get(position);
        int pos = position;
        Row rowcopy = row;
        int num = row.getNum();
        if(row.getType().equals("Main")){
            ((RowAdapter.RowViewHolder ) holder).uatext.setText("UA"+num);
            ((RowAdapter.RowViewHolder ) holder).srtext.setText("SR"+num);
        }else{
            ((RowAdapter.RowViewHolder ) holder).uatext.setText("A"+alt.getAltnum()+".UA"+num);
            ((RowAdapter.RowViewHolder ) holder).srtext.setText("A"+alt.getAltnum()+".SR"+num);
        }
        ((RowAdapter.RowViewHolder ) holder).ua.setText(row.getUa());
        ((RowAdapter.RowViewHolder ) holder).sr.setText(row.getSr());
        GoToRowScreen(((RowAdapter.RowViewHolder ) holder).ua,row);
        GoToRowScreen(((RowAdapter.RowViewHolder ) holder).sr,row);
        GoToRowScreen(((RowAdapter.RowViewHolder ) holder).editrow,row);
        ((RowViewHolder ) holder).deleterow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                arr.remove(position);
                for(int i=0;i<arr.size();i++){
                    arr.get(i).setNum(i+1);
                }
                if(row.getType().equals("Main")){
                    ((RowAdapter.RowViewHolder ) holder).uatext.setText("UA"+row.getNum());
                    ((RowAdapter.RowViewHolder ) holder).srtext.setText("SR"+row.getNum());
                }else{
                    ((RowAdapter.RowViewHolder ) holder).uatext.setText("A"+alt.getAltnum()+".UA"+row.getNum());
                    ((RowAdapter.RowViewHolder ) holder).srtext.setText("A"+alt.getAltnum()+".SR"+row.getNum());
                }
                notifyDataSetChanged();
                Snackbar.make(rl,"The row is deleted", Snackbar.LENGTH_LONG).setAction("UNDO", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        arr.add(pos,rowcopy);
                        for(int i=0;i<arr.size();i++){
                            arr.get(i).setNum(i+1);
                        }
                        if(row.getType().equals("Main")){
                            ((RowAdapter.RowViewHolder ) holder).uatext.setText("UA"+row.getNum());
                            ((RowAdapter.RowViewHolder ) holder).srtext.setText("SR"+row.getNum());
                        }else{
                            ((RowAdapter.RowViewHolder ) holder).uatext.setText("A"+alt.getAltnum()+".UA"+row.getNum());
                            ((RowAdapter.RowViewHolder ) holder).srtext.setText("A"+alt.getAltnum()+".SR"+row.getNum());
                        }
                        notifyDataSetChanged();
                    }
                }).show();

            }
        });

    }

    @Override
    public int getItemCount() {
        return arr.size();
    }

    private void GoToRowScreen(View v,Row r){
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context,Row_Screen.class);
                i.putExtra("row",r);
                if(r.getType().equals("Alt"))
                    i.putExtra("alt",alt);
                i.putExtra("from",from);
                i.putExtra("uc",uc);
                i.putExtra("Version",ver);
                i.putExtra("exp",aexplination);
                i.putExtra("Project",pro);
                i.putExtra("sd",srsd);
                i.putExtra("cameFrom",cameFrom);
                i.putExtra("docID",docID);
                context.startActivity(i);
            }
        });
    }

    private static class RowViewHolder extends RecyclerView.ViewHolder{
        TextView uatext,ua,srtext,sr;
        ImageView deleterow,editrow;
        View v;
        public RowViewHolder(@NonNull View itemView) {
            super(itemView);
            v = itemView;
            uatext = (TextView) itemView.findViewById(R.id.uatext);
            ua = (TextView) itemView.findViewById(R.id.ua);
            srtext = (TextView) itemView.findViewById(R.id.srtext);
            sr = (TextView) itemView.findViewById(R.id.sr);
            deleterow = (ImageView) itemView.findViewById(R.id.deleterow);
            editrow = (ImageView) itemView.findViewById(R.id.editrow);
        }
    }

}
