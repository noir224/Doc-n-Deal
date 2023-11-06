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
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

import javax.security.auth.callback.Callback;

public class ProFeatAdp extends RecyclerView.Adapter implements Callback {
    ArrayList<VProductFeature> arr;
    Context context;
    RelativeLayout viss;
    static ArrayList<Abbrev> aexplination;


    public ProFeatAdp(ArrayList<VProductFeature> arr, Context context, RelativeLayout viss, ArrayList<Abbrev> aexplination) {
        this.arr = arr;
        this.context = context;
        this.viss = viss;
        this.aexplination=aexplination;
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_features,parent,false);
        return new ProFeatAdp.cViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ArrayList<String> pri = new ArrayList<>();
        pri.add("Critical");
        pri.add("Important");
        pri.add("Useful");
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(context,R.layout.list_item,pri);
        ArrayList<String> pri2 = new ArrayList<>();
        pri2.add("High");
        pri2.add("Medium");
        pri2.add("Low");
        ArrayAdapter<String> arrayAdapter2 = new ArrayAdapter<>(context,R.layout.list_item,pri2);
        ((ProFeatAdp.cViewHolder) holder).name.setText(arr.get(position).getName()+"");
        ((ProFeatAdp.cViewHolder) holder).prior.setAdapter(arrayAdapter);
        ((ProFeatAdp.cViewHolder) holder).prior.setSelection(arrayAdapter.getPosition(arr.get(position).getPriority()));
        ((cViewHolder) holder).effort.setAdapter(arrayAdapter2);
        ((cViewHolder) holder).effort.setSelection(arrayAdapter.getPosition(arr.get(position).getEffort()));
        ((cViewHolder) holder).risk.setAdapter(arrayAdapter2);
        ((cViewHolder) holder).risk.setSelection(arrayAdapter.getPosition(arr.get(position).getRisk()));
        ((cViewHolder) holder).stability.setAdapter(arrayAdapter2);
        ((cViewHolder) holder).stability.setSelection(arrayAdapter.getPosition(arr.get(position).getStability()));

        ((ProFeatAdp.cViewHolder) holder).delExp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int actualPosition = holder.getAdapterPosition();
                VProductFeature n = arr.get(holder.getAdapterPosition());
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
        ((ProFeatAdp.cViewHolder ) holder).v.setOnLongClickListener(new View.OnLongClickListener() {
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
        Spinner prior;
        Spinner effort;
        Spinner risk;
        Spinner stability;
        ImageView delExp;
        View v;
        public cViewHolder(@NonNull View itemView) {
            super(itemView);
            v = itemView;
            name = (EditText) v.findViewById(R.id.vpfeature);
            prior = (Spinner) v.findViewById(R.id.pspinner);
            effort = (Spinner) v.findViewById(R.id.espinner);
            risk = (Spinner) v.findViewById(R.id.rspinner);
            stability = (Spinner) v.findViewById(R.id.sspinner);
            delExp = (ImageView) itemView.findViewById(R.id.deleteprofeat);
            name.setCustomSelectionActionModeCallback(new algAdapter.AbrCallback(name));

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
