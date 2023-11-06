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

public class ApiAdapter extends RecyclerView.Adapter implements Callback {
    ArrayList<API> arr;
    Context context;
    RelativeLayout pro2;
    static ArrayList<Abbrev> aexplination;

    public ApiAdapter(ArrayList<API> arr, Context context, RelativeLayout pro2, ArrayList<Abbrev> aexplination) {
        this.arr = arr;
        this.context = context;
        this.pro2 = pro2;
        this.aexplination = aexplination;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.api_proposal,parent,false);
        return new ApiAdapter.cViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((ApiAdapter.cViewHolder ) holder).link.setText(arr.get(position).getLink()+"");
        ((ApiAdapter.cViewHolder ) holder).name.setText(arr.get(position).getName()+"");
        ((ApiAdapter.cViewHolder) holder).delExp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int actualPosition = holder.getAdapterPosition();
                API b = arr.get(actualPosition);
                arr.remove(actualPosition);
                notifyItemRemoved(actualPosition);
                notifyItemRangeChanged(actualPosition, arr.size());
                Snackbar.make(pro2,"The item is deleted", Snackbar.LENGTH_LONG).setAction("UNDO", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        arr.add(b);
                        // notifydatasetchanged(holder.getAdapterPosition());
                        notifyDataSetChanged();
                        notifyItemRangeChanged(holder.getAdapterPosition(), arr.size());
                    }
                }).show();
            }
        });
        ((ApiAdapter.cViewHolder ) holder).v.setOnLongClickListener(new View.OnLongClickListener() {
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
        EditText name;
        EditText link;
        ImageView delExp;
        View v;
        public cViewHolder(@NonNull View itemView) {
            super(itemView);
            v = itemView;
            name = (EditText) itemView.findViewById(R.id.APIName);
            link = (EditText) itemView.findViewById(R.id.apiLink);
            delExp = (ImageView) itemView.findViewById(R.id.deleteapi);
            name.setCustomSelectionActionModeCallback(new algAdapter.AbrCallback(name));
            link.setCustomSelectionActionModeCallback(new algAdapter.AbrCallback(link));
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

