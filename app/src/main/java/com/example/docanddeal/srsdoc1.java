package com.example.docanddeal;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.PrecomputedText;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextWatcher;
import android.text.style.CharacterStyle;
import android.text.style.ForegroundColorSpan;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.Result;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Locale;

import javax.security.auth.callback.Callback;


public class srsdoc1 extends AppCompatActivity implements View.OnClickListener, Callback {
    RelativeLayout PROD;
    ImageView srsFinish, plusuc, plusnfr, plusRef, ucdimage;
    EditText docNameSRS;
    Button uploaducd, uploaducdxml, nextToAbbrev;
    LinearLayout ucsList, nfrsList, refList;
    RecyclerView ucsrv,refrv,nfrsrv;
    FirebaseAuth fAuth;
    FirebaseFirestore fs;
    String userID;
    StorageReference sr;
    UseCase uc;
    Document d;
    SRSDocument srsd;
    ArrayList<Reference> aRef;
    ArrayList<NonFunc> anfrs;
    ArrayList<UseCase> aucs;
    ArrayList<Abbrev> aexplination;
    Bundle b;
    Version p1;
    ProjectC pro;
    String pid;
    String ver;
    boolean clicked;
    private static final int PICK_IMAGE_REQUEST = 1;
    Uri ucduri;
    private static final int PICK_XML_REQUEST = 12;
    private static final int PICK_NOT_EMPTY_REQUEST = 13;
    static Uri filepath;
    ArrayList<UseCase> ucs;
    ArrayList<UseCase> ucs1;
    NonFuncAdapter nfrAdp;
    RefAdapterSRS refAdp;
    ArrayList<Actor> allactors;
    ImageView helpuc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_srsdoc1);
        PROD = findViewById(R.id.PROD);
        srsFinish = findViewById(R.id.srsFinish);
        docNameSRS = findViewById(R.id.docNameSRS);
        uploaducd = findViewById(R.id.uploaducd);
        uploaducdxml = findViewById(R.id.uploaducdxml);
        ucdimage = findViewById(R.id.ucdimage);
        ucsList = findViewById(R.id.ucs);
        plusuc = findViewById(R.id.plusuc);
        nfrsList = findViewById(R.id.nfrs);
        plusnfr = findViewById(R.id.plusnfr);
        refList = findViewById(R.id.refs);
        plusRef = findViewById(R.id.plusrefs);
        ucsrv = findViewById(R.id.ucsrv);
        nfrsrv= findViewById(R.id.nfrsrv);
        refrv = findViewById(R.id.refrv);
        nextToAbbrev = findViewById(R.id.nextToAbbrev);
        fAuth = FirebaseAuth.getInstance();
        fs = FirebaseFirestore.getInstance();
        sr = FirebaseStorage.getInstance().getReference();
        userID = fAuth.getCurrentUser().getUid();
        clicked = false;
        anfrs = new ArrayList<>();
        aexplination = new ArrayList<>();
        aucs = new ArrayList<>();
        aRef = new ArrayList<>();
        srsd = new SRSDocument(userID);
        b = getIntent().getExtras();
        p1 = (Version) b.getSerializable("Version");
        pro = (ProjectC) b.getSerializable("Project");
        pid = pro.getPid();
        ver = p1.getVersion();
        helpuc = findViewById(R.id.helpusecasediagram);
        allactors = new ArrayList<>();
        //nfr and ref only shows when coming from mathcing with number, otherwise if it is coming from edit uc they won't be saved
        //must pass the nfrs/refs through uc adapter


        helpuc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(srsdoc1.this, UsecasesHelp.class);
                startActivity(i);
            }
        });


        if (getIntent().hasExtra("sd")) {
            srsd = (SRSDocument) b.getSerializable("sd");
            docNameSRS.setText(srsd.getDname());
            ucduri = Uri.parse(srsd.getUcdimg());
            filepath = Uri.parse(srsd.getUcdaml());
            Picasso.get().load(ucduri).into(ucdimage);
            anfrs = srsd.getNfrs();
            aexplination = srsd.getExplination();
            nfrAdp = new NonFuncAdapter(anfrs, srsdoc1.this, PROD,aexplination);
            nfrsrv.setVisibility(View.VISIBLE);
            nfrsrv.setHasFixedSize(true);
            RecyclerView.LayoutManager lm8 = new LinearLayoutManager(this);
            nfrsrv.setLayoutManager(lm8);
            nfrsrv.setAdapter(nfrAdp);
            if (srsd.getNfrs().size() != 0) {

            }
            refrv.setVisibility(View.VISIBLE);
            refrv.setHasFixedSize(true);
            RecyclerView.LayoutManager lm9 = new LinearLayoutManager(this);
            refrv.setLayoutManager(lm9);
            aRef = srsd.getRefs();
            refAdp = new RefAdapterSRS(aRef, srsdoc1.this, PROD,aexplination);
            refrv.setAdapter(refAdp);
            if (srsd.getRefs().size() != 0) {

            }
            ucs = srsd.getUcs();
            Collections.sort(ucs);
            ucsrv.setVisibility(View.VISIBLE);
            ucsrv.setHasFixedSize(true);
            RecyclerView.LayoutManager lm = new GridLayoutManager(this,2);
            ucsrv.setLayoutManager(lm);
            UseCaseAdapter da = new UseCaseAdapter(ucs, srsdoc1.this, aexplination, p1, pro,srsd,"srs",null);
            ucsrv.setAdapter(da);
        }else{
            nfrsrv.setVisibility(View.VISIBLE);
            nfrsrv.setHasFixedSize(true);
            RecyclerView.LayoutManager lm8 = new LinearLayoutManager(this);
            nfrsrv.setLayoutManager(lm8);
            nfrAdp = new NonFuncAdapter(anfrs, srsdoc1.this, PROD,aexplination);
            nfrsrv.setAdapter(nfrAdp);
            refrv.setVisibility(View.VISIBLE);
            refrv.setHasFixedSize(true);
            RecyclerView.LayoutManager lm9 = new LinearLayoutManager(this);
            refrv.setLayoutManager(lm9);
            refAdp = new RefAdapterSRS(aRef, srsdoc1.this, PROD,aexplination);
            refrv.setAdapter(refAdp);

        }


        docNameSRS.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String s1 = s.toString().trim();
                if(!s1.isEmpty()){
                    srsd.setDname(s1);
                }
            }
        });



        uploaducd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });
        uploaducdxml.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ucs != null){
                    if(ucs.size() != 0)
                        select(PICK_NOT_EMPTY_REQUEST);
                }
                else
                    select(PICK_XML_REQUEST);
            }
        });

        nextToAbbrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SaveSRSDoc();
                Intent i = new Intent(srsdoc1.this, srsdoc2.class);
                i.putExtra("sd", srsd);
                i.putExtra("Version", p1);
                i.putExtra("exp", aexplination);
                i.putExtra("Project", pro);
                //i.putExtra("pid",pid);
                srsdoc1.this.startActivity(i);
            }
        });
        srsFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(srsdoc1.this)
                        .setTitle("Save")
                        .setMessage("Do you want to save the Document?")
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent i = new Intent(srsdoc1.this, ProjectDetails.class);
                                i.putExtra("Version", p1);
                                i.putExtra("Project", pro);
                                //i.putExtra("pid",pid);
                                startActivity(i);
                                finish();
                            }
                        })
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface arg0, int arg1) {
                                SaveSRSDoc();
                                fAuth = FirebaseAuth.getInstance();
                                fs = FirebaseFirestore.getInstance();
                                userID = fAuth.getCurrentUser().getUid();
                                /*private void checkName(String name,Uri uri){
                                        dref = fs.collection("users").document(userID);
                                        dref.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                            @Override
                                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                if(documentSnapshot.get("projects")!=null) {
                                                    ArrayList<String> arr = ((ArrayList<String>) documentSnapshot.get("pnames"));
                                                    if(arr.contains(name)){

                                                    }
                                                    else{
                                                        sendToDB(uri);
                                                    }
                                                }

                                            }
                                        });

                                 */
                                fs.collection("documents").whereEqualTo("pid", pid).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                    @Override
                                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                        if (queryDocumentSnapshots.size() == 0) {
                                            sendToDB();
                                        } else {
                                            for (DocumentSnapshot ds : queryDocumentSnapshots) {
                                                Document d = ds.toObject(Document.class);
                                                if (d.getName().equals(docNameSRS.getText().toString()) && d.getVerid().equals(p1.getVersion()))
                                                    Toast.makeText(srsdoc1.this, "Document name already exists", Toast.LENGTH_LONG).show();
                                                else {
                                                    sendToDB();
                                                    break;
                                                }

                                            }
                                        }


                                    }
                                });

                            }
                        }).create().show();
            }
        });

        plusuc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SaveSRSDoc();
                Intent i = new Intent(srsdoc1.this, NewUsecase.class);
                i.putExtra("sd", srsd);
                i.putExtra("Version", p1);
                i.putExtra("exp", aexplination);
                i.putExtra("Project", pro);
                i.putExtra("cameFrom","srs");
                startActivity(i);
            }
        });
        plusnfr.setOnClickListener(this);
        plusRef.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.plusnfr:
                anfrs.add(new NonFunc(""));
                nfrsrv.getAdapter().notifyDataSetChanged();
                break;
            case R.id.plusrefs:
                aRef.add(new Reference(""));
                refrv.getAdapter().notifyDataSetChanged();
                break;
            default:
                break;
        }
    }

    private void select(int CODE) {
        Intent in = new Intent();
        in.setType("text/xml");
        in.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(in, CODE);

    }
    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(srsdoc1.this)
                .setTitle("Save")
                .setMessage("Do you want to save the Document?")
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent i = new Intent(srsdoc1.this, ProjectDetails.class);
                        i.putExtra("Version", p1);
                        i.putExtra("Project", pro);
                        //i.putExtra("pid",pid);
                        startActivity(i);
                        finish();
                    }
                })
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        SaveSRSDoc();
                        fAuth = FirebaseAuth.getInstance();
                        fs = FirebaseFirestore.getInstance();
                        userID = fAuth.getCurrentUser().getUid();
                                /*private void checkName(String name,Uri uri){
                                        dref = fs.collection("users").document(userID);
                                        dref.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                            @Override
                                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                if(documentSnapshot.get("projects")!=null) {
                                                    ArrayList<String> arr = ((ArrayList<String>) documentSnapshot.get("pnames"));
                                                    if(arr.contains(name)){

                                                    }
                                                    else{
                                                        sendToDB(uri);
                                                    }
                                                }

                                            }
                                        });

                                 */
                        fs.collection("documents").whereEqualTo("pid", pid).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                if (queryDocumentSnapshots.size() == 0) {
                                    sendToDB();
                                } else {
                                    for (DocumentSnapshot ds : queryDocumentSnapshots) {
                                        Document d = ds.toObject(Document.class);
                                        if (d.getName().equals(docNameSRS.getText().toString()) && d.getVerid().equals(p1.getVersion()))
                                            Toast.makeText(srsdoc1.this, "Document name already exists", Toast.LENGTH_LONG).show();
                                        else {
                                            sendToDB();
                                            break;
                                        }

                                    }
                                }


                            }
                        });

                    }
                }).create().show();
        super.onBackPressed();

    }

    private void SaveSRSDoc() {
        fAuth = FirebaseAuth.getInstance();
        fs = FirebaseFirestore.getInstance();
        userID = fAuth.getCurrentUser().getUid();
        String sname = docNameSRS.getText().toString().trim();

        if(ucs==null){
            ucs = new ArrayList<>();
            ArrayList<Actor> all = new ArrayList<>();
            srsd = new SRSDocument(userID, sname, ucduri+"", filepath+"", ucs, anfrs, aRef, aexplination,all);
        }else{
            srsd = new SRSDocument(userID, sname, ucduri+"", filepath+"", ucs, anfrs, aRef, aexplination,allactors);
        }

    }

    private void sendToDB() {
        StorageTask st;
        StorageReference sr1;
        if (srsd.getUcdimg() != null) {
            if(!srsd.getUcdimg().isEmpty()){
                sr1 = sr.child(System.currentTimeMillis() + "." + getExtension(Uri.parse(srsd.getUcdimg())));
                System.out.println("lllllll "+Uri.fromFile(new File(srsd.getUcdimg())));
                st = sr1.putFile(myUri(ucduri,srsd.getUcdimg()));
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
                            StorageTask st2;
                            StorageReference sr2;
                            Uri dd = (Uri) task.getResult();
                            if(srsd.getUcdaml()!= null && filepath != null){
                                if(!srsd.getUcdaml().isEmpty()){
                                    sr2 = sr.child(System.currentTimeMillis() + "." + getExtension(Uri.parse(srsd.getUcdaml())));
                                    st2 = sr2.putFile(myUri(filepath,srsd.getUcdaml()));
                                    st2.continueWithTask(new Continuation() {
                                        @Override
                                        public Object then(@NonNull Task task) throws Exception {
                                            if (!task.isSuccessful()) {
                                                throw task.getException();
                                            }
                                            return sr2.getDownloadUrl();

                                        }
                                    }).addOnCompleteListener(new OnCompleteListener() {
                                        @Override
                                        public void onComplete(@NonNull Task task) {
                                            if (task.isSuccessful()) {
                                                Uri dd2 = (Uri) task.getResult();
                                                srsd.setUcdimg(dd + "");
                                                srsd.setUcdaml(dd2 + "");
                                                finalSend();
                                            }
                                        }
                                    });
                                }else {
                                    srsd.setUcdimg(dd + "");
                                    finalSend();
                                }
                            }else {
                                srsd.setUcdimg(dd + "");
                                finalSend();
                            }
                        }
                    }
                });
            }else{
                finalSend();
            }
        }else{
            finalSend();
        }


    }
    public Uri myUri(Uri originalUri,String path){
        Uri returnedUri = null;
        if (originalUri.getScheme() == null){
            returnedUri = Uri.fromFile(new File(path));
            // or you can just do -->
            // returnedUri = Uri.parse("file://"+camUri.getPath());
        }else{
            returnedUri = originalUri;
        }
        return returnedUri;
    }
    private void finalSend(){
        fs.collection("Images").whereEqualTo("pid",pro.getPid()).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for(DocumentSnapshot ds : queryDocumentSnapshots){
                    int size = srsd.getUcs().size();
                    ImageUri iu = ds.toObject(ImageUri.class);
                    if(iu.getPhototype().equals("MainSeq")){
                        for(int i=0;i<size;i++){
                            if(srsd.getUcs().get(i).getMainflowseq() != null){
                                if(srsd.getUcs().get(i).getMainflowseq().getImagename().equals(iu.getImagename())) {
                                    srsd.getUcs().get(i).getMainflowseq().setImagepath(iu.getUri());
                                    break;
                                }
                            }

                        }
                    }else if(iu.getPhototype().equals("AltSeq")){
                        outer: for(int i=0;i<size;i++){
                            UseCase u = srsd.getUcs().get(i);
                            inner:for(int j=0;j<u.getAlternatives().size();j++){
                                Alternative a = u.getAlternatives().get(j);
                                if(a.getAltseq() != null) {
                                    if(a.getAltseq().getImagename().equals(iu.getImagename())) {
                                        srsd.getUcs().get(i).getAlternatives().get(j).getAltseq().setImagepath(iu.getUri());
                                        break outer;
                                    }
                                }
                            }
                        }

                    }else if(iu.getPhototype().equals("std")){
                        for(int i=0;i<size;i++){
                            if(srsd.getUcs().get(i).getStdscreen()!=null){
                                if(srsd.getUcs().get(i).getStdscreen().getImagename().equals(iu.getImagename())) {
                                    srsd.getUcs().get(i).getStdscreen().setImagepath(iu.getUri());
                                    break;
                                }
                            }

                        }

                    }else if(iu.getPhototype().equals("uaMain")){
                        outer: for(int i=0;i<size;i++){
                            UseCase u = srsd.getUcs().get(i);
                            inner:for(int j=0;j<u.getMainflowrows().size();j++){
                                Row r = u.getMainflowrows().get(j);
                                if(r.getUascreen()!=null){
                                    if(r.getUascreen().getImagename().equals(iu.getImagename())) {
                                        srsd.getUcs().get(i).getMainflowrows().get(j).getUascreen().setImagepath(iu.getUri());
                                        break outer;
                                    }
                                }

                            }
                        }
                    }else if(iu.getPhototype().equals("srMain")){
                        outer: for(int i=0;i<size;i++){
                            UseCase u = srsd.getUcs().get(i);
                            inner:for(int j=0;j<u.getMainflowrows().size();j++){
                                Row r = u.getMainflowrows().get(j);
                                if(r.getSrscreen()!=null) {
                                    if (r.getSrscreen().getImagename().equals(iu.getImagename())) {
                                        srsd.getUcs().get(i).getMainflowrows().get(j).getSrscreen().setImagepath(iu.getUri());
                                        break outer;
                                    }
                                }
                            }
                        }

                    }else if(iu.getPhototype().equals("uaAlt")){
                        outer: for(int i=0;i<size;i++){
                            UseCase u = srsd.getUcs().get(i);
                            inner:for(int j=0;j<u.getAlternatives().size();j++){
                                Alternative a = u.getAlternatives().get(j);
                                for(int k=0;k<a.getAltrows().size();k++){
                                    Row r = a.getAltrows().get(k);
                                    if(r.getUascreen()!=null) {
                                        if(r.getUascreen().getImagename().equals(iu.getImagename())) {
                                            srsd.getUcs().get(i).getAlternatives().get(j).getAltrows().get(k).getUascreen().setImagepath(iu.getUri());
                                            break outer;
                                        }
                                    }
                                }

                            }
                        }

                    }else if(iu.getPhototype().equals("srAlt")){
                        outer: for(int i=0;i<size;i++){
                            UseCase u = srsd.getUcs().get(i);
                            inner:for(int j=0;j<u.getAlternatives().size();j++){
                                Alternative a = u.getAlternatives().get(j);
                                for(int k=0;k<a.getAltrows().size();k++){
                                    Row r = a.getAltrows().get(k);
                                    if(r.getSrscreen()!=null) {
                                        if(r.getSrscreen().getImagename().equals(iu.getImagename())) {
                                            srsd.getUcs().get(i).getAlternatives().get(j).getAltrows().get(k).getSrscreen().setImagepath(iu.getUri());
                                            break outer;
                                        }
                                    }

                                }

                            }
                        }
                    }
                }
                fs.collection("srsDoc").add(srsd).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        // public Document(String uid, String type, String name, String docid, String pid, String verid)
                        Document d = new Document(userID, "srs", docNameSRS.getText().toString(), documentReference.getId(), pid, ver, null);
                        fs.collection("documents").document(documentReference.getId()).set(d);
                        Intent i = new Intent(srsdoc1.this, ProjectDetails.class);
                        i.putExtra("Version", p1);
                        i.putExtra("Project", pro);
                        // i.putExtra("pid",pid);
                        startActivity(i);
                        finish();
                    }
                });
            }
        });
    }

    class AbrCallback implements ActionMode.Callback {
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
            String currenttext = et.getText().toString();
            String replace = "<span style='background-color:yellow'>" + s + "</span>";
            String newText = currenttext.replaceAll(s, replace);
            et.setText(Html.fromHtml(newText));


            switch (item.getItemId()) {
                case R.id.Abbrv:
                    Abbrev ab = new Abbrev(ssb.toString(), "");
                    boolean flag = true;
                    for (Abbrev ab1 : aexplination) {
                        if (ab1.getName().trim().toLowerCase().equals(ab.getName().trim().toLowerCase()))
                            flag = false;
                    }
                    if (flag)
                        aexplination.add(ab);
                    return true;

            }
            return false;
        }

        public void onDestroyActionMode(ActionMode mode) {

        }
    }

    private void openFileChooser() {
        Intent in = new Intent();
        in.setType("image/*");
        in.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(in, PICK_IMAGE_REQUEST);


    }

    private String getExtension(Uri uri) {
//        String sIOimageurl = "" + uri;
//        return sIOimageurl.substring(sIOimageurl.lastIndexOf(".") + 1);
        ContentResolver CR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(CR.getType(uri));
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri imguri = data.getData();
            ucduri = imguri;
            Picasso.get().load(imguri).into(ucdimage);
        }
        if (requestCode == PICK_XML_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            //upload.setEnabled(true);
            filepath = data.getData();
            parseXML();
        }
        if (requestCode == PICK_NOT_EMPTY_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            //upload.setEnabled(true);
            filepath = data.getData();
            parseXML(ucs);
        }

    }

    private void parseXML() {
        XmlPullParserFactory parserFactory;
        try {
            parserFactory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = parserFactory.newPullParser();
            InputStream is = getContentResolver().openInputStream(filepath);
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(is, null);
            ucs = processParsing(parser);
            SaveSRSDoc();
            Intent i = new Intent(srsdoc1.this, MatchWithNumber.class);
            i.putExtra("sd", srsd);
            i.putExtra("ucs", ucs);
            i.putExtra("notempty", false);
            i.putExtra("Version", p1);
            i.putExtra("cameFrom","srs");
            i.putExtra("Project", pro);
            //i.putExtra("pid",pid);
            startActivity(i);


        } catch (XmlPullParserException e) {

        } catch (IOException e) {

        }
    }
    private void parseXML(ArrayList<UseCase> ucs) {
        XmlPullParserFactory parserFactory;
        try {
            parserFactory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = parserFactory.newPullParser();
            InputStream is = getContentResolver().openInputStream(filepath);
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(is, null);
            ArrayList<UseCase> ucs1 = processParsing(parser);
            //for evey new use case in the new arr, if the new use case exists in the original (set velues of actors & related uc), else add it (what if uc was removed ?)
            //if uc was removed in ucs1 it will never compare to ucs
            for(int i=0;i<ucs1.size();i++){
                UseCase uc1 = ucs1.get(i);
                boolean exists = false;
                for(int j=0;j<ucs.size();j++){
                    if(uc1.getName().toLowerCase().equals(ucs.get(j).getName().toLowerCase())){
                        //ucs.get(j).setUcnum(uc1.getUcnum());
                        ucs.get(j).setActors(uc1.getActors());
                        ucs.get(j).setRelationships(uc1.getRelationships());
                        exists = true;
                    }
                }
                if(exists)
                    continue;
                else {
                    ucs.add(uc1);
                }
            }
            //for removed
            //for all original usecases, if they exist in ucs1 them continue, else remove the uc
            for(int i=0;i<ucs.size();i++){
                UseCase uco = ucs.get(i);

                boolean exists = false;
                for(int j=0;j<ucs1.size();j++){
                    if(uco.getName().toLowerCase().equals(ucs1.get(j).getName().toLowerCase())){
                        //ucs.get(j).setUcnum(uc1.getUcnum());
                        exists = true;
                        break;
                    }
                }
                if(exists)
                    continue;
                else {
                    ucs.remove(i);
                }
            }
            SaveSRSDoc();
            Intent i = new Intent(srsdoc1.this, MatchWithNumber.class);
            i.putExtra("sd", srsd);
            i.putExtra("ucs", ucs);
            i.putExtra("notempty", true);
            i.putExtra("Version", p1);
            i.putExtra("Project", pro);
            i.putExtra("cameFrom","srs");
            //i.putExtra("pid",pid);
            startActivity(i);


        } catch (XmlPullParserException e) {

        } catch (IOException e) {

        }
    }

    private ArrayList<UseCase> processParsing(XmlPullParser parser) throws XmlPullParserException, IOException {
        Calendar c = Calendar.getInstance();
        final int year = c.get(Calendar.YEAR);
        final int month = c.get(Calendar.MONTH) + 1;
        final int day = c.get(Calendar.DAY_OF_MONTH);
        String date = day + "/" + month + "/" + year;
        ArrayList<UseCase> ucs = new ArrayList<>();
        ArrayList<Actor> actors = new ArrayList<>();
        ArrayList<Relationship> relationships = new ArrayList<>();
        int eventType = parser.getEventType();
        UseCase usecase = null;
        while (eventType != XmlPullParser.END_DOCUMENT) {
            String eltName = null;
            switch (eventType) {
                case XmlPullParser.START_DOCUMENT:
                    ucs = new ArrayList<>();
                    break;
                case XmlPullParser.START_TAG:
                    eltName = parser.getName();
                    if (eltName.equals("mxCell")) {
                        System.out.println("reached1");
                        String id = parser.getAttributeValue(parser.getNamespace(), "id");
                        if (id.equals("0") || id.equals("1")) {
                            System.out.println("reached2");
                        } else {
                            System.out.println("reached3");
                            String style = parser.getAttributeValue(parser.getNamespace(), "style");
                            if (!style.isEmpty() || style != null) {
                                System.out.println("reached4");
                                if (style.contains("shape=umlActor")) {
//                                    int index = 0;
//                                    if (actors != null)
//                                        index = actors.size();
                                    Actor a = new Actor(id, parser.getAttributeValue(parser.getNamespace(), "value"));
                                    actors.add(a);
                                    allactors.add(a);
                                } else if (style.contains("ellipse")) {
                                    System.out.println("reached6");
//                                    int index = 0;
//                                    if (ucs != null)
//                                        index = ucs.size();
                                    String name = parser.getAttributeValue(parser.getNamespace(), "value");
                                    UseCase uc = new UseCase(name, id);
                                    uc.setDate(date);
                                    ucs.add(uc);
                                } else if (style.contains("endArrow")) {
                                    System.out.println("reached7");
//                                    int index = 0;
//                                    if (relationships != null)
//                                        index = relationships.size();
                                    String type = parser.getAttributeValue(parser.getNamespace(), "value");
                                    String source = parser.getAttributeValue(parser.getNamespace(), "source");
                                    String target = parser.getAttributeValue(parser.getNamespace(), "target");
                                    if (type != null) {
                                        if (type.contains("extend") || type.contains("extends"))
                                            type = "extend";
                                        else if (type.contains("include") || type.contains("includes"))
                                            type = "include";
                                        else
                                            type = "association";
                                    } else {
                                        type = "association";
                                    }

                                    Relationship r = new Relationship(source, target, type, id);
                                    relationships.add(r);
                                }
                            }
                        }

                    }
                    break;
            }
            eventType = parser.next();
        }

        for (UseCase uc:ucs) {
            String ucid = uc.getId();
            for(Relationship r: relationships){
                String source = r.getSource();
                String target = r.getTarget();
                if(source.equals(ucid) || target.equals(ucid)){
                    for (Actor a: actors) {
                        if(source.equals(a.getId()) || target.equals(a.getId()))
                            uc.getActors().add(a);
                    }
                    uc.getRelationships().add(r);
                }
            }
        }
        ArrayList<GetActor> ga = new ArrayList<>();
        for (Actor a : actors) {
            ga.add(new GetActor(a));
        }
        for(UseCase uc : ucs){
            for(int i=0;i<actors.size();i++){
                GetActor g = ga.get(i);
                if(uc.getActors().contains(g.getA()) && !ga.get(i).getUcs().contains(uc) ){
                    ga.get(i).getUcs().add(uc);
                    ga.get(i).getUcsids().add(uc.getId());
                }
            }

        }

        for( GetActor get : ga){
            for(int i=0;i<get.getUcsids().size();i++){
                String gaids = get.getUcsids().get(i);

                System.out.println("ids size "+get.getUcsids().size());
                System.out.println("ucs size "+get.getUcs().size());
                for(Relationship r : get.getUcs().get(i).getRelationships()){
                    String source = r.getSource();
                    String target = r.getTarget();
                    //if source is not the uc itself & source is not an actor & ids doesn't contain source id
                    //what if source was another actor?
                    if(!source.equals(gaids) && !get.getUcsids().contains(source) && !isActor(source, actors) ){

                        get.getUcsids().add(source);
                        for(UseCase uc : ucs){
                            if(uc.getId().equals(source)){
                                get.getUcs().add(uc);
                                break;
                            }
                        }

                    }
                    if(!target.equals(gaids) && !isActor(target, actors) && !get.getUcsids().contains(target) ){
                        get.getUcsids().add(target);
                        for(UseCase uc : ucs){
                            if(uc.getId().equals(target)){
                                get.getUcs().add(uc);
                                break;
                            }
                        }
                    }
                }
            }
        }
        for(UseCase uc : ucs){
            for(GetActor get : ga){
                if(get.getUcs().contains(uc) && !uc.getActors().contains(get.getA()))
                    uc.getActors().add(get.getA());
            }
        }

        return ucs;

    }

    public boolean isActor(String id,ArrayList<Actor> actors){
        boolean flag=false;
        for(Actor a : actors){
            if(a.getId().equals(id)){
                flag = true;
                break;
            }

        }
        return flag;
    }

}