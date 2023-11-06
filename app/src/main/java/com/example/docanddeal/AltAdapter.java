package com.example.docanddeal;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;

public class AltAdapter extends RecyclerView.Adapter {
    ArrayList<Alternative> arr;
    Context context;
    RelativeLayout rl;
    Version ver;
    UseCase uc;
    ProjectC pro;
    ArrayList<Abbrev> aexplination;
    SRSDocument srsd;
    ArrayList<Row> rows;
    AltSeqAdapter altSeqAdapter;
    String from,cameFrom,docID;
    public AltAdapter(ArrayList<Alternative> arr, Context context, RelativeLayout rl, Version ver,
                      UseCase uc, ProjectC pro, ArrayList<Abbrev> aexplination, SRSDocument srsd, AltSeqAdapter altSeqAdapter,
                      String from, String cameFrom, String docID) {
        this.arr = arr;
        this.context = context;
        this.rl = rl;
        this.ver = ver;
        this.uc = uc;
        this.pro = pro;
        this.aexplination = aexplination;
        this.srsd = srsd;
        this.altSeqAdapter =altSeqAdapter;
        this.from=from;
        this.docID = docID;
        this.cameFrom = cameFrom;

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.alt_item,parent,false);
        return new AltAdapter.AltViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        int pos = position;
        Alternative alt = arr.get(position);
        Alternative altcopy = alt;
        //recycler view for rows
        if(alt.getAltrows() != null){
            rows = alt.getAltrows();
            ((AltAdapter.AltViewHolder ) holder).altrowlistrv.setHasFixedSize(true);
            ((AltAdapter.AltViewHolder ) holder).altrowlistrv.setVisibility(View.VISIBLE);
            RecyclerView.LayoutManager lm = new LinearLayoutManager(context);
            ((AltAdapter.AltViewHolder ) holder).altrowlistrv.setLayoutManager(lm);
            RowAdapter da = new RowAdapter(alt.getAltrows(), context,rl,ver,uc,pro,aexplination,alt,srsd,from,cameFrom,docID);
            ((AltAdapter.AltViewHolder ) holder).altrowlistrv.setAdapter(da);
            ItemTouchHelper itemTouchHelperAlt = new ItemTouchHelper(simpleCallbackAlt);
            itemTouchHelperAlt.attachToRecyclerView(((AltAdapter.AltViewHolder ) holder).altrowlistrv);

        }

        ((AltAdapter.AltViewHolder ) holder).altnum.setText("Alternative"+(position+1));
        ((AltAdapter.AltViewHolder ) holder).altname.setText(alt.getAltname());
        ((AltAdapter.AltViewHolder ) holder).deletealt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("////"+pos);
                arr.remove(position);
                for(int i=0;i<arr.size();i++){
                    arr.get(i).setAltnum(i+1);
                }
                ((AltAdapter.AltViewHolder ) holder).altnum.setText("Alternative"+(position+1));
                ((AltAdapter.AltViewHolder ) holder).altrowlistrv.getAdapter().notifyDataSetChanged();
                altSeqAdapter.notifyDataSetChanged();
                notifyDataSetChanged();
                Snackbar.make(rl,"The row is deleted", Snackbar.LENGTH_LONG).setAction("UNDO", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        arr.add(pos,altcopy);
                        for(int i=0;i<arr.size();i++){
                            arr.get(i).setAltnum(i+1);
                        }
                        ((AltAdapter.AltViewHolder ) holder).altnum.setText("Alternative"+(position+1));
                        notifyDataSetChanged();
                        altSeqAdapter.notifyDataSetChanged();
                        ((AltAdapter.AltViewHolder ) holder).altrowlistrv.getAdapter().notifyDataSetChanged();
                    }
                }).show();
            }
        });
        ((AltAdapter.AltViewHolder ) holder).altname.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                arr.get(position).setName(s.toString());
            }
        });


        ((AltAdapter.AltViewHolder ) holder).plusaltrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context,Row_Screen.class);
                i.putExtra("alt",alt);
                i.putExtra("from",from);
                i.putExtra("uc",uc);
                i.putExtra("rowtype","Alt");
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

    @Override
    public int getItemCount() {
        return arr.size();
    }

    private static class AltViewHolder extends RecyclerView.ViewHolder{
        TextView altnum;
        EditText altname;
        ImageView deletealt;
        ImageView plusaltrow;
        RecyclerView altrowlistrv;
        LinearLayout altrowlist;
        View v;
        public AltViewHolder(@NonNull View itemView) {
            super(itemView);
            v = itemView;
            altnum = (TextView) itemView.findViewById(R.id.altnum);
            altname = (EditText) itemView.findViewById(R.id.altname);
            deletealt = (ImageView) itemView.findViewById(R.id.deletealt);
            plusaltrow = (ImageView) itemView.findViewById(R.id.plusaltrow);
            altrowlistrv = (RecyclerView) itemView.findViewById(R.id.altrowlistrv);
            altrowlist = (LinearLayout) itemView.findViewById(R.id.altrowlist);
        }
    }
    ItemTouchHelper.SimpleCallback simpleCallbackAlt = new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.START | ItemTouchHelper.END, 0) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {

            int fromPosition = viewHolder.getAdapterPosition();
            int toPosition = target.getAdapterPosition();
            Collections.swap(rows, fromPosition, toPosition);
            recyclerView.getAdapter().notifyItemMoved(fromPosition, toPosition);
            for(int i=0;i<rows.size();i++) {
                rows.get(i).setNum((i+1));
            }
            recyclerView.getAdapter().notifyDataSetChanged();
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

        }
    };


}