package com.example.docanddeal;

import android.content.Context;
import android.graphics.Color;
import android.text.Html;
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
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

import javax.security.auth.callback.Callback;

public class StakeholderAdp extends RecyclerView.Adapter implements Callback {
    ArrayList<String> arr;
    Context context;
    RelativeLayout viss;
    static ArrayList<Abbrev> aexplination;

    public StakeholderAdp(ArrayList<String> arr, Context context, RelativeLayout viss, ArrayList<Abbrev> aexplination) {
        this.arr = arr;
        this.context = context;
        this.viss = viss;
        this.aexplination=aexplination;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.stakeholder_summary,parent,false);
        return new StakeholderAdp.cViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((cViewHolder) holder).stake.setText(arr.get(position)+"");
        ((StakeholderAdp.cViewHolder) holder).delExp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int actualPosition = holder.getAdapterPosition();
                String n = arr.get(holder.getAdapterPosition());
                arr.remove(holder.getAdapterPosition());
                notifyItemRemoved(actualPosition);
                notifyItemRangeChanged(actualPosition, arr.size());
                Snackbar.make(viss,"The item is deleted", Snackbar.LENGTH_LONG).setAction("UNDO", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        arr.add(n);
                        notifyDataSetChanged();
                        notifyItemRangeChanged(holder.getAdapterPosition(), arr.size());

                    }
                }).show();
            }
        });
        ((StakeholderAdp.cViewHolder ) holder).v.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return false;
            }
        });
    }
    @Override
    public int getItemCount() {
        return arr.size();
    }

    private static class cViewHolder extends RecyclerView.ViewHolder{
        EditText stake;
        ImageView delExp;
        View v;
        public cViewHolder(@NonNull View itemView) {
            super(itemView);
            v = itemView;
            stake = (EditText) v.findViewById(R.id.StakeHolder);
            delExp = (ImageView) itemView.findViewById(R.id.deleteStakeHolder);
            stake.setCustomSelectionActionModeCallback(new algAdapter.AbrCallback(stake));
        }
    }
    static class AbrCallback implements ActionMode.Callback {
        EditText et;

        public AbrCallback(EditText et) {
            this.et = et;
        }

        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            MenuInflater inflater = mode.getMenuInflater();
            inflater.inflate(R.menu.text_menu, menu);
            return true;
        }

        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            CharacterStyle cs;
            int start = et.getSelectionStart();
            int end = et.getSelectionEnd();
            String s = et.getText().toString().substring(start, end);
            SpannableStringBuilder ssb = new SpannableStringBuilder(s);
            final ForegroundColorSpan fcs = new ForegroundColorSpan(Color.BLUE);
            ssb.setSpan(fcs, 0, ssb.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
            String currenttext= et.getText().toString();
            String replace= "<span style='background-color:yellow'>"+s+"</span>";
            String newText = currenttext.replaceAll(s,replace);
            et.setText(Html.fromHtml(newText));


            switch (item.getItemId()) {
                case R.id.Abbrv:
                    Abbrev ab = new Abbrev(ssb.toString(), "");
                    boolean flag=true;
                    for(Abbrev ab1 : aexplination){
                        if(ab1.getName().trim().toLowerCase().equals(ab.getName().trim().toLowerCase()))
                            flag = false;
                    }
                    if(flag)
                        aexplination.add(ab);
                    return true;

            }
            return false;
        }

        public void onDestroyActionMode(ActionMode mode) {

        }
    }

}
