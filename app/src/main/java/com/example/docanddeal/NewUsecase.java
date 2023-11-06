package com.example.docanddeal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;

public class NewUsecase extends AppCompatActivity {
    RelativeLayout rledituc;
    EditText ucnum, ucdate, ucauthor, precond, postcond,ucname;
    Spinner priority;
    ImageView helpprecond, arrowprecond, helppostcond, arrowpostcond, helpmainflow, arrowmainflow, plusmain, helpalt,
            plusalt, arrowalt, deleteuc, stdpic, mainseqpic, plusactor, plusrelated, helpstd,helpsequence;
    TextView textpostcond, textprecond, textmainflow, textalt;
    boolean precondclicked,postcondclicked,mainflowclicked,altclicked;
    LinearLayout mainflowlist, altlist, actorslist, relatedlist;
    RecyclerView mainflowlistrv, altlistrv, altuploadsrv, actors, realted;
    Button saveuc, uploadstd, uploadmainseq;
    Bundle b;
    Version ver;
    ProjectC pro;
    UseCase uc;
    FirebaseAuth fAuth;
    FirebaseFirestore fs;
    String userID;
    StorageReference sr;
    ArrayList<Abbrev> aexplination;
    ArrayList<UseCase> ucsarr;
    ArrayList<Actor> actorsarr;
    ArrayList<Relationship> relarr;
    ArrayList<Row> mainflowarr;
    ArrayList<Alternative> altarr;
    ArrayList<Row> altrowsarr;
    boolean actorIsEmpty = false;
    boolean RelIsEmpty = false;
    final static int STD_CODE = 99;
    final static int MAIN_SEQ_CODE = 100;
    final static int REQUEST_CODE = 101;
    Uri stduri, mainsequri;
    ActorAdapter actorAdapter;
    RelatedAdapter relatedAdapter;
    RowAdapter mainflowAdapter;
    AltAdapter altAdapter;
    AltSeqAdapter altSeqAdapter;
    private OnIntentReceived mIntentListener;
    ArrayList<Relationship> relarrcopy;
    SRSDocument srsd;
    boolean goToPage = true;
    String cameFrom,docID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_usecase);
        rledituc = findViewById(R.id.rledituc);
        ucname = findViewById(R.id.ucname);
        ucnum = findViewById(R.id.ucnum);
        ucdate = findViewById(R.id.ucdate);
        ucauthor = findViewById(R.id.ucauthor);
        precond = findViewById(R.id.precond);
        helpprecond = findViewById(R.id.helpprecond);
        arrowprecond = findViewById(R.id.arrowprecond);
        textprecond = findViewById(R.id.textprecond);
        helppostcond = findViewById(R.id.helppostcond);
        arrowpostcond = findViewById(R.id.arrowpostcond);
        textpostcond = findViewById(R.id.textpostcond);
        postcond = findViewById(R.id.postcond);
        actors = findViewById(R.id.actors);
        plusactor = findViewById(R.id.plusactor);
        actorslist = findViewById(R.id.actorslist);
        priority = findViewById(R.id.priority);
        realted = findViewById(R.id.realted);
        plusrelated = findViewById(R.id.plusrelated);
        relatedlist = findViewById(R.id.relatedlist);
        helpmainflow = findViewById(R.id.helpmainflow);
        arrowmainflow = findViewById(R.id.arrowmainflow);
        textmainflow = findViewById(R.id.textmainflow);
        mainflowlist = findViewById(R.id.mainflowlist);
        plusmain = findViewById(R.id.plusmain);
        helpalt = findViewById(R.id.helpalt);
        plusalt = findViewById(R.id.plusalt);
        arrowalt = findViewById(R.id.arrowalt);
        textalt = findViewById(R.id.textalt);
        altlist = findViewById(R.id.altlist);
        deleteuc = findViewById(R.id.deleteuc);
        saveuc = findViewById(R.id.saveuc);
        mainflowlistrv = findViewById(R.id.mainflowlistrv);
        altlistrv = findViewById(R.id.altlistrv);
        altuploadsrv = findViewById(R.id.altuploadsrv);
        uploadstd = findViewById(R.id.uploadstd);
        uploadmainseq = findViewById(R.id.uploadmainseq);
        stdpic = findViewById(R.id.stdpic);
        mainseqpic = findViewById(R.id.mainseqpic);
        helpstd = findViewById(R.id.helpstatetransition);
        helpsequence = findViewById(R.id.helpsequencedigram);
        fAuth = FirebaseAuth.getInstance();
        fs = FirebaseFirestore.getInstance();
        sr = FirebaseStorage.getInstance().getReference();
        ArrayList<String> priorities = new ArrayList<>();
        priorities.add("Priority");
        priorities.add("High");
        priorities.add("Medium");
        priorities.add("Low");
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, R.layout.list_item, priorities);
        System.out.println(priority);
        priority.setAdapter(arrayAdapter);
        Calendar c = Calendar.getInstance();
        final int year = c.get(Calendar.YEAR);
        final int month = c.get(Calendar.MONTH) + 1;
        final int day = c.get(Calendar.DAY_OF_MONTH);
        String sdate = day + "/" + month + "/" + year;
        ucdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog dpd = new DatePickerDialog(NewUsecase.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int day) {
                        month = month + 1;
                        String sdate = day + "/" + month + "/" + year;
                        ucdate.setText(sdate);
                    }
                }, year, month, day);
                dpd.getDatePicker().setMaxDate(System.currentTimeMillis());
                dpd.show();
            }
        });

        b = getIntent().getExtras();
        ver = (Version) b.getSerializable("Version");
        pro = (ProjectC) b.getSerializable("Project");
        cameFrom = b.get("cameFrom").toString();
        if(cameFrom.equals("Edit") || cameFrom.equals("Adapter")) {
            docID = b.getString("docID");
            System.out.println("////docID "+docID);
        }
        srsd = (SRSDocument) b.getSerializable("sd");
        aexplination = srsd.getExplination();
        ucsarr = srsd.getUcs();
        int size =1;
        if(ucsarr==null)
            ucnum.setText(size+"");
        else if(ucsarr.size()==0)
            ucnum.setText(size+"");
        else {
            size = ucsarr.size()+1;
            ucnum.setText( size+ "");
        }

        if (getIntent().hasExtra("uc")) {
            uc = (UseCase) b.getSerializable("uc");
            actorsarr = uc.getActors();
            relarr = uc.getRelationships();
            relarrcopy = new ArrayList<>(relarr);
            mainflowarr = uc.getMainflowrows();
            altarr = uc.getAlternatives();
            // ArrayList<Row> altrowsarr;
            ucname.setText(uc.getName());
            ucnum.setText(uc.getUcnum() + "");
            ucdate.setText(uc.getDate());
            ucauthor.setText(uc.getAuthor());
            precond.setText(uc.getPrecond());
            postcond.setText(uc.getPostcond());
            postcondclicked = false;
            precondclicked=false;
            mainflowclicked = false;
            altclicked = false;

            textprecond.setVisibility(View.GONE);
            arrowprecond.setVisibility(View.GONE);

            helpprecond.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (precondclicked==false){
                        textprecond.setVisibility(View.VISIBLE);
                        arrowprecond.setVisibility(View.VISIBLE);
                        precondclicked = true;
                    }
                    else {
                        textprecond.setVisibility(View.GONE);
                        arrowprecond.setVisibility(View.GONE);
                        precondclicked = false;
                    }
                }
            });

            helppostcond.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (postcondclicked==false){
                        textpostcond.setVisibility(View.VISIBLE);
                        arrowpostcond.setVisibility(View.VISIBLE);
                        postcondclicked = true;
                    }
                    else {
                        textpostcond.setVisibility(View.GONE);
                        arrowpostcond.setVisibility(View.GONE);
                        postcondclicked = false;
                    }
                }
            });
            helpmainflow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mainflowclicked==false){
                        textmainflow.setVisibility(View.VISIBLE);
                        arrowmainflow.setVisibility(View.VISIBLE);
                        mainflowclicked = true;
                    }
                    else {
                        textmainflow.setVisibility(View.GONE);
                        arrowmainflow.setVisibility(View.GONE);
                        mainflowclicked = false;
                    }
                }
            });
            helpalt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (altclicked==false){
                        textalt.setVisibility(View.VISIBLE);
                        arrowalt.setVisibility(View.VISIBLE);
                        altclicked = true;
                    }
                    else {
                        textalt.setVisibility(View.GONE);
                        arrowalt.setVisibility(View.GONE);
                        altclicked = false;
                    }
                }
            });

            helpstd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(NewUsecase.this, StateTransitionHelp.class);
                    startActivity(i);
                }
            });
            helpsequence.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(NewUsecase.this, SequenceDiagramHelp.class);
                    startActivity(i);
                }
            });



            priority.setSelection(arrayAdapter.getPosition(uc.getPriority()));
            for (int i = 0; i < relarrcopy.size(); i++) {
                Relationship r = relarrcopy.get(i);
                if (r.getType().equals("association")) {
                    relarrcopy.remove(i);
                    i--;
                }
            }
        } else {
            uc = new UseCase(size, sdate);
            actorsarr = uc.getActors();
            relarr = uc.getRelationships();
            relarrcopy = new ArrayList<>(relarr);
            mainflowarr = uc.getMainflowrows();
            altarr = uc.getAlternatives();
        }
        actorAdapter = new ActorAdapter(actorsarr, NewUsecase.this, rledituc);
        relatedAdapter = new RelatedAdapter(relarrcopy, NewUsecase.this, rledituc, uc, ucsarr);
        mainflowAdapter = new RowAdapter(mainflowarr, NewUsecase.this, rledituc, ver, uc, pro, aexplination, srsd,"New",cameFrom,docID);
        altSeqAdapter = new AltSeqAdapter(altarr, NewUsecase.this,pro);
        altAdapter = new AltAdapter(altarr, NewUsecase.this, rledituc, ver, uc, pro, aexplination, srsd, altSeqAdapter,"New",cameFrom,docID);

        actors.setHasFixedSize(true);
        RecyclerView.LayoutManager lm = new GridLayoutManager(this, 4);
        actors.setLayoutManager(lm);

        realted.setHasFixedSize(true);
        RecyclerView.LayoutManager lm1 = new GridLayoutManager(this, 2);
        realted.setLayoutManager(lm1);

        mainflowlistrv.setHasFixedSize(true);
        RecyclerView.LayoutManager lm2 = new LinearLayoutManager(this);
        mainflowlistrv.setLayoutManager(lm2);

        altlistrv.setHasFixedSize(true);
        RecyclerView.LayoutManager lm3 = new LinearLayoutManager(this);
        altlistrv.setLayoutManager(lm3);

        altuploadsrv.setHasFixedSize(true);
        RecyclerView.LayoutManager lm4 = new LinearLayoutManager(this);
        altuploadsrv.setLayoutManager(lm4);


        //actors Recycler view
        if (actorsarr != null) {
            if (!actorsarr.isEmpty()) {
                actors.setVisibility(View.VISIBLE);
                actors.setAdapter(actorAdapter);
            }
        } else {
            actorIsEmpty = true;
        }
        //related uc recycler view
        if (relarrcopy != null) {
            if (!relarrcopy.isEmpty()) {
                realted.setVisibility(View.VISIBLE);
                realted.setAdapter(relatedAdapter);
            }
        } else {
            RelIsEmpty = true;
        }
        //Mainflow rows recycler view
        if (mainflowarr != null) {
            if (!mainflowarr.isEmpty()) {
                mainflowlistrv.setVisibility(View.VISIBLE);
                mainflowlistrv.setAdapter(mainflowAdapter);
            }
        }
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(mainflowlistrv);
        //Alternatives recycler view & Alternative uploads
        if (altarr != null) {
            if (!altarr.isEmpty()) {
                altlistrv.setVisibility(View.VISIBLE);
                altlistrv.setAdapter(altAdapter);

                altuploadsrv.setVisibility(View.VISIBLE);
                altuploadsrv.setAdapter(altSeqAdapter);
                mIntentListener = altSeqAdapter;
            }
        }
        ItemTouchHelper itemTouchHelperAlt = new ItemTouchHelper(simpleCallbackAlt);
        itemTouchHelperAlt.attachToRecyclerView(altlistrv);
        uploadstd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser(STD_CODE);
            }
        });
        uploadmainseq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser(MAIN_SEQ_CODE);
            }
        });
        //add actor
        plusactor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addViewActor();
            }
        });
        //add related uc
        plusrelated.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addViewRelated();
            }
        });
        //add row to main flow
        plusmain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveUC();
                Intent i = new Intent(NewUsecase.this, Row_Screen.class);
                i.putExtra("uc", uc);
                i.putExtra("from","New");
                i.putExtra("sd", srsd);
                i.putExtra("rowtype", "Main");
                i.putExtra("Version", ver);
                i.putExtra("exp", aexplination);
                i.putExtra("Project", pro);
                i.putExtra("cameFrom",cameFrom);
                i.putExtra("docID",docID);
                startActivity(i);
            }
        });
        // add new alternative (will also add new sequence diagram button
        plusalt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int altnum;
                if (altarr == null)
                    altnum = 1;
                else
                    altnum = altarr.size() + 1;
                altarr.add(new Alternative(altnum, uc.getUcnum()));
                altlistrv.setVisibility(View.VISIBLE);
                altuploadsrv.setVisibility(View.VISIBLE);
                altlistrv.setAdapter(altAdapter);
                altuploadsrv.setAdapter(altSeqAdapter);
                mIntentListener = altSeqAdapter;
            }
        });
        saveuc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backToSrs();
            }
        });

        ucnum.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.toString().isEmpty()){

                }else{
                    uc.setUcnum(Integer.parseInt(s.toString().trim()));
                }

            }
        });
        ucdate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                uc.setDate(s.toString());
            }
        });
        ucauthor.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                uc.setAuthor(s.toString());
            }
        });
        precond.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                uc.setPrecond(s.toString());
            }
        });
        postcond.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                uc.setPostcond(s.toString());
            }
        });
        priority.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                uc.setPriority(priorities.get(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

    @Override
    public void onBackPressed() {
        saveUC();
        if(goToPage){
            srsd.getUcs().add(uc);
            srsd.setUcs(srsd.getUcs());
            Intent i;
            if(cameFrom.equals("srs"))
                i = new Intent(NewUsecase.this,srsdoc1.class);
            else {
                i = new Intent(NewUsecase.this, EditSRSdoc1.class);
                i.putExtra("docID",docID);
            }
            i.putExtra("sd", srsd);
            i.putExtra("Version", ver);
            i.putExtra("exp", aexplination);
            i.putExtra("Project", pro);
            startActivity(i);
            super.onBackPressed();
        }


    }

    private void saveUC() {
        String sucnum = ucnum.getText().toString().trim();
        String sucname = ucname.getText().toString().trim();
        String sucdate = ucdate.getText().toString().trim();
        String sucauthor = ucauthor.getText().toString().trim();
        String sprecond = precond.getText().toString().trim();
        String spostcond = postcond.getText().toString().trim();
        String spriority = priority.getSelectedItem().toString();
        if (sucnum.isEmpty() || sucnum.equals("null") || sucnum==null) {
            Toast.makeText(NewUsecase.this, "Please fill usecase number", Toast.LENGTH_LONG).show();
            goToPage = false;
        }else if(sucname.isEmpty() || sucname.equals("null") || sucname==null){
            Toast.makeText(NewUsecase.this, "Please fill usecase name", Toast.LENGTH_LONG).show();
            goToPage = false;
        } else{
            boolean uniquename = true;
            for (int i=0;i<srsd.getUcs().size();i++) {
                if (srsd.getUcs().get(i).getName().toLowerCase().equals(sucname)) {
                    uniquename = false;
                    break;
                }
            }
            if (uniquename) {
                if (!spriority.equals("Priority"))
                    uc.setPriority(spriority);
                uc.setName(sucname);
                uc.setUcnum(Integer.parseInt(sucnum));
                uc.setDate(sucdate);
                uc.setAuthor(sucauthor);
                uc.setPrecond(sprecond);
                uc.setPostcond(spostcond);
                uc.setActors(actorsarr);
                uc.setAlternatives(altarr);
                ArrayList<Relationship> rel = new ArrayList<>(relarr);
                // rel now have the common elements between relarr and relarrcopy
                //array inside bracket stays the same
                rel.retainAll(relarrcopy);
                for (int j = 0; j < relarrcopy.size(); j++) {
                    //if element was common then skip
                    if (rel.contains(relarrcopy.get(j)))
                        continue;
                    else {
                        //if element wasn't common then add
                        rel.add(relarrcopy.get(j));
                    }
                }
                uc.setRelationships(rel);
                uc.setMainflowrows(mainflowarr);
                goToPage = true;

            } else {
                Toast.makeText(NewUsecase.this, "Usecase name must be unique", Toast.LENGTH_LONG).show();
                goToPage = false;
            }
        }


    }

    private void backToSrs() {
        saveUC();
        if(goToPage){
            srsd.getUcs().add(uc);
            srsd.setUcs(srsd.getUcs());
            Intent i;
            if(cameFrom.equals("srs"))
                i = new Intent(NewUsecase.this,srsdoc1.class);
            else {
                i = new Intent(NewUsecase.this, EditSRSdoc1.class);
                i.putExtra("docID",docID);
            }
            i.putExtra("sd", srsd);
            i.putExtra("Version", ver);
            i.putExtra("exp", aexplination);
            i.putExtra("Project", pro);
            startActivity(i);
        }

    }

    private void openFileChooser(int code) {
        Intent in = new Intent();
        in.setType("image/*");
        in.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(in, code);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Uri uri = data.getData();
        if (requestCode == REQUEST_CODE) {
            if (mIntentListener != null) {
                mIntentListener.onIntent(data, resultCode);
            }
        }
        if (requestCode == STD_CODE) {
            stduri = uri;
            Picasso.get().load(stduri).into(stdpic);
            String imagename = System.currentTimeMillis() + "." + getExtension(stduri);
            ImagePath imgpath = new ImagePath(imagename,stduri+"");
            uc.setStdscreen(imgpath);
            sendImage(stduri,imagename,"std");
        }
        if (requestCode == MAIN_SEQ_CODE) {
            mainsequri = uri;
            Picasso.get().load(mainsequri).into(mainseqpic);
            String imagename = System.currentTimeMillis() + "." + getExtension(mainsequri);
            ImagePath imgpath = new ImagePath(imagename,mainsequri+"");
            uc.setMainflowseq(imgpath);
            sendImage(mainsequri,imagename,"MainSeq");

        }
    }
    private String getExtension(Uri uri) {
//        String sIOimageurl = "" + uri;
//        return sIOimageurl.substring(sIOimageurl.lastIndexOf(".") + 1);
        ContentResolver CR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(CR.getType(uri));
    }

    private void sendImage(Uri uri, String Imagename,String photocopy){
        StorageTask st;
        StorageReference sr1;
        sr1 = sr.child(Imagename);
        st = sr1.putFile(uri);
        st.continueWithTask(new Continuation() {
            @Override
            public Object then(@NonNull Task task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }

                return sr1.getDownloadUrl();

            }
        }).addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {
                if (task.isSuccessful()) {
                    Uri dd = (Uri) task.getResult();

                    fs.collection("Images").add(new ImageUri(Imagename,(dd+""),pro.getPid(),photocopy)).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(NewUsecase.this, e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        });

    }

    private void addViewActor() {
        View actorview = getLayoutInflater().inflate(R.layout.actor_spinner, null, false);
        Spinner actor = (Spinner) actorview.findViewById(R.id.actorspin);
        Button addToActors = (Button) actorview.findViewById(R.id.addToActors);
        ArrayList<String> acs = new ArrayList<>();
        acs.add("Actor");
        for (Actor a : srsd.getAllactors()) {
            boolean actorexists = false;
            for (Actor ucact : actorsarr) {
                if (a.getName().equals(ucact.getName())) {
                    actorexists = true;
                    break;
                }
            }
            if (actorexists)
                continue;
            else
                acs.add(a.getName());
        }
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, R.layout.list_item, acs);
        actor.setAdapter(arrayAdapter);
        addToActors.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sactor = actor.getSelectedItem().toString();
                if (sactor.equals("Actor uc")) {
                    Toast.makeText(NewUsecase.this, "Please choose an actor", Toast.LENGTH_LONG).show();

                } else {
                    for (Actor a : srsd.getAllactors()) {
                        if (sactor.equals(a.getName())) {
                            actorsarr.add(a);
                            if (actorIsEmpty == false) {
                                actors.setVisibility(View.VISIBLE);
                                actors.setAdapter(actorAdapter);
                            } else {
                                actors.getAdapter().notifyDataSetChanged();
                            }
                            actorslist.removeView(actorview);
                            actorslist.setVisibility(View.GONE);
                        }
                    }
                }

            }
        });
        if (acs.size() == 1 && actorsarr !=null) {
            Toast.makeText(NewUsecase.this, "This usecase is already associated with all actors", Toast.LENGTH_LONG).show();
        } else if(acs.size() == 1 && actorsarr ==null) {
            Toast.makeText(NewUsecase.this, "No actors available", Toast.LENGTH_LONG).show();
        } else {
            actorslist.setVisibility(View.VISIBLE);
            actorslist.addView(actorview);
        }

    }


    private void addViewRelated() {
        View relview = getLayoutInflater().inflate(R.layout.relateduc_dynamic_item, null, false);
        Spinner source = (Spinner) relview.findViewById(R.id.source);
        Spinner type = (Spinner) relview.findViewById(R.id.type);
        Spinner target = (Spinner) relview.findViewById(R.id.target);
        Button addToRel = (Button) relview.findViewById(R.id.addToRel);
        ArrayList<String> sources = new ArrayList<>();
        sources.add("source uc");
        if(ucsarr != null){
            for (UseCase uc2 : ucsarr)
                sources.add(uc2.getName());
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, R.layout.list_item, sources);
            source.setAdapter(arrayAdapter);
            ArrayList<String> types = new ArrayList<>();
            types.add("type");
            types.add("extend");
            types.add("include");
            ArrayAdapter<String> arrayAdapter1 = new ArrayAdapter<>(this, R.layout.list_item, types);
            type.setAdapter(arrayAdapter1);
            ArrayList<String> targets = new ArrayList<>();
            targets.add("target uc");
            for (UseCase uc2 : ucsarr)
                targets.add(uc2.getName());
            ArrayAdapter<String> arrayAdapter2 = new ArrayAdapter<>(this, R.layout.list_item, targets);
            target.setAdapter(arrayAdapter2);
        }

        addToRel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ssource = source.getSelectedItem().toString();
                String stype = type.getSelectedItem().toString();
                String starget = target.getSelectedItem().toString();
                if (ssource.equals("source uc")) {
                    Toast.makeText(NewUsecase.this, "Please choose a source usecase", Toast.LENGTH_LONG).show();
                } else if (stype.equals("type")) {
                    Toast.makeText(NewUsecase.this, "Please choose relationship type", Toast.LENGTH_LONG).show();
                } else if (starget.equals("target uc")) {
                    Toast.makeText(NewUsecase.this, "Please choose a target usecase", Toast.LENGTH_LONG).show();
                } else {
                    if (ssource.equals(starget)) {
                        Toast.makeText(NewUsecase.this, "A usecase cannot have a relationship with itself", Toast.LENGTH_LONG).show();
                    } else if (!ssource.equals(uc.getName()) && !starget.equals(uc.getName()))
                        Toast.makeText(NewUsecase.this, "Current usecase must be either the source or the target", Toast.LENGTH_LONG).show();
                    else {
                        String sourceid = getUCid(ssource);
                        String targetid = getUCid(starget);
                        Relationship r = new Relationship(sourceid, targetid, stype, (relarr.size() + 5000) + "");
                        relarrcopy.add(r);
                        //relarr.add(r);
                        if (RelIsEmpty == false) {
                            realted.setVisibility(View.VISIBLE);
                            realted.setAdapter(relatedAdapter);
                        } else {
                            realted.getAdapter().notifyDataSetChanged();
                        }
                        relatedlist.removeView(relview);
                        relatedlist.setVisibility(View.GONE);
                    }
                }

            }
        });
        if (ucname.getText().toString().trim().isEmpty()) {
            Toast.makeText(NewUsecase.this, "Please type usecase name first", Toast.LENGTH_LONG).show();
        }else if(ucsarr == null){
            Toast.makeText(NewUsecase.this, "No available usecases", Toast.LENGTH_LONG).show();
        }
        else {
            relatedlist.setVisibility(View.VISIBLE);
            relatedlist.addView(relview);
        }

    }

    private String getUCid(String name) {
        String id = "";
        for (UseCase uc : ucsarr) {
            if (name.equals(uc.getName())) {
                id = uc.getId();
                break;
            }
        }
        return id;
    }

    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.START | ItemTouchHelper.END, 0) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {

            int fromPosition = viewHolder.getAdapterPosition();
            int toPosition = target.getAdapterPosition();
            Collections.swap(mainflowarr, fromPosition, toPosition);
            recyclerView.getAdapter().notifyItemMoved(fromPosition, toPosition);
            for (int i = 0; i < mainflowarr.size(); i++) {
                mainflowarr.get(i).setNum((i + 1));
            }
            recyclerView.getAdapter().notifyDataSetChanged();
            //recyclerView.getAdapter().onBindViewHolder(viewHolder,toPosition);
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

        }
    };
    ItemTouchHelper.SimpleCallback simpleCallbackAlt = new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.START | ItemTouchHelper.END, 0) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {

            int fromPosition = viewHolder.getAdapterPosition();
            int toPosition = target.getAdapterPosition();
            Collections.swap(altarr, fromPosition, toPosition);
            recyclerView.getAdapter().notifyItemMoved(fromPosition, toPosition);
            altuploadsrv.getAdapter().notifyItemMoved(fromPosition, toPosition);
            for (int i = 0; i < altarr.size(); i++) {
                altarr.get(i).setAltnum((i + 1));
            }
            recyclerView.getAdapter().notifyDataSetChanged();
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

        }
    };
}




