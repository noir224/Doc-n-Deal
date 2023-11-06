package com.example.docanddeal;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;

public class MatchWithNumber extends AppCompatActivity {
    ImageView ucd;
    RecyclerView matchingrv;
    Button savematching;
    Bundle b;
    Version p1;
    ProjectC pro;
    SRSDocument srsd;
    String camefrom;
    boolean notempty;
    boolean wronginput;
    ArrayList<String> names;
    ArrayList<UsecaseChosen> namesb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match_with_number);
        ucd=findViewById(R.id.ucd);
        matchingrv= findViewById(R.id.matchingrv);
        savematching=findViewById(R.id.savematching);
        b = getIntent().getExtras();
        p1 = (Version) b.getSerializable("Version");
        pro = (ProjectC) b.getSerializable("Project");
        srsd = (SRSDocument) b.getSerializable("sd");
        camefrom = (String) b.getSerializable("cameFrom");
        names= new ArrayList<>();
        namesb = new ArrayList<>();
        notempty = (boolean) b.get("notempty");
        namesb = new ArrayList<>();
        names.add("UC Name");
        for (UseCase uc : srsd.getUcs()) {
            names.add(uc.getName());
            namesb.add(new UsecaseChosen(uc.getName()));
        }
        matchingrv.setHasFixedSize(true);
        RecyclerView.LayoutManager lm = new GridLayoutManager(this,2);
        matchingrv.setLayoutManager(lm);
        MatchingAdapter da = new MatchingAdapter(srsd.getUcs(), MatchWithNumber.this,notempty,names,namesb);
        matchingrv.setAdapter(da);

        savematching.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ArrayList<String> names1 = new ArrayList<>();
                wronginput =false;
                for(UsecaseChosen s : namesb){
                    System.out.println("kkkkk "+s.getUcname());
                    if(!s.isChosen()){
                        Toast.makeText(MatchWithNumber.this,"Please match all usescases with their numbers and make sure that each one is unique",Toast.LENGTH_LONG).show();
                        wronginput = true;
                        break;
                    }else{
                        if(names1.contains(s.getUcname())){
                            Toast.makeText(MatchWithNumber.this,"You cannot have 2 use cases with same names",Toast.LENGTH_LONG).show();
                            wronginput = true;
                            break;
                        }else{
                            names1.add(s.getUcname());
                        }
                    }
                }
//                for(UsecaseChosen s : namesb){
//                    System.out.println("kkkkk "+s);
//                    if(s.getUcname().equals("UC Name")){
//                        Toast.makeText(MatchWithNumber.this,"Please match all usescases with their numbers",Toast.LENGTH_LONG).show();
//                        wronginput = true;
//                        break;
//                    }else{
//                        if(names1.contains(s.getUcname())){
//                            Toast.makeText(MatchWithNumber.this,"You cannot have 2 use cases with same names",Toast.LENGTH_LONG).show();
//                            wronginput = true;
//                            break;
//                        }else{
//                            names1.add(s.getUcname());
//                        }
//                    }
//                }
                if(wronginput==false){
                    Intent i;
                    if(camefrom.equals("srs"))
                        i = new Intent(MatchWithNumber.this,srsdoc1.class);
                    else {
                        i = new Intent(MatchWithNumber.this, EditSRSdoc1.class);
                        String docID = b.get("docID").toString();
                        i.putExtra("docID",docID);
                    }
                    i.putExtra("sd",srsd);
                    i.putExtra("Version",p1);
                    i.putExtra("Project",pro);
                    startActivity(i);
                    finish();
                }

            }
        });

    }
}