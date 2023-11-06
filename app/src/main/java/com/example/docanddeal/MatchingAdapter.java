package com.example.docanddeal;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MatchingAdapter extends RecyclerView.Adapter {
    ArrayList<UseCase> arr;
    ArrayList<String> checknames;
    Context context;
    boolean notempty;
    ArrayList<String> names;
    ArrayList<UsecaseChosen> namesb;

    public MatchingAdapter(ArrayList<UseCase> arr, Context context, boolean notempty,ArrayList<String> names,
                           ArrayList<UsecaseChosen> namesb) {
        this.arr = arr;
        this.context = context;
        this.notempty = notempty;
        checknames = new ArrayList<>();
        this.names = names;
        this.namesb = namesb;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.matching_item,parent,false);
        return new MatchingAdapter.MatchingViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        ((MatchingAdapter.MatchingViewHolder) holder).ucnum.setText("Usecase "+(position+1));

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(context, R.layout.list_item, names);
        ((MatchingAdapter.MatchingViewHolder) holder).ucname.setAdapter(arrayAdapter);
        if(notempty)
            ((MatchingAdapter.MatchingViewHolder) holder).ucname.setSelection(arrayAdapter.getPosition(arr.get(position).getName()));

        //if(arr.get(position).getUcnum()!=-1){
        //selectSpinnerItemByValue(((MatchingAdapter.MatchingViewHolder) holder).ucname,arr.get(position).getName(),names);
        //((MatchingAdapter.MatchingViewHolder) holder).ucname.setSelection(arrayAdapter.getPosition(arr.get(position).getName()));
        //}

        ((MatchingAdapter.MatchingViewHolder) holder).ucname.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position1, long id) {
                if(position1!=0){
                    namesb.get(position1-1).setChosen(true);
                    if(checknames.contains(names.get(position1))){
                        ((MatchingViewHolder) holder).warning.setVisibility(View.VISIBLE);
                    }else{
                        checknames.add(names.get(position1));
                        ((MatchingViewHolder) holder).warning.setVisibility(View.GONE);

                        for(int i=0;i<arr.size();i++){
                            if(arr.get(i).getName().equals(names.get(position1)))
                                arr.get(i).setUcnum((position+1));
                        }

                    }
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

    @Override
    public int getItemCount() {
        return arr.size();
    }
    private static class MatchingViewHolder extends RecyclerView.ViewHolder{
        TextView ucnum;
        TextView warning;
        Spinner ucname;
        View v;
        public MatchingViewHolder(@NonNull View itemView) {
            super(itemView);
            v = itemView;
            ucnum= (TextView) v.findViewById(R.id.ucnum);
            warning= (TextView) v.findViewById(R.id.warning);
            ucname= (Spinner) v.findViewById(R.id.ucname);

        }
    }
    public static void selectSpinnerItemByValue(Spinner spnr, String value, ArrayList<String> names) {
        for(int i=0;i< names.size();i++)
            if(value.equals(names.get(i)))
                spnr.setSelection(i);

    }
}

