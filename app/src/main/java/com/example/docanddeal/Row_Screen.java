package com.example.docanddeal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.CharacterStyle;
import android.text.style.ForegroundColorSpan;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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

public class Row_Screen extends AppCompatActivity {
    TextView uatext,srtext;
    EditText ua,sr;
    Button uploaduascreen,uploadsrscreen;
    ImageView uascreen,srscreen;
    Bundle b;
    ProjectC pro;
    Version ver;
    ArrayList<Abbrev> aexplination;
    Row r;
    UseCase uc;
    SRSDocument srsd;
    FirebaseAuth fAuth;
    FirebaseFirestore fs;
    String userID;
    StorageReference sr2;
    final static int UA_CODE=103;
    final static int SR_CODE=104;
    Uri uauri,sruri;
    Alternative alt;
    String type;
    String from,cameFrom,docID;
    int num;
    boolean newrow = true;
    ImagePath uaimg,srimg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_row_screen);
        uatext= findViewById(R.id.uatext);
        srtext= findViewById(R.id.srtext);
        ua= findViewById(R.id.ua);
        sr= findViewById(R.id.sr);
        uploaduascreen= findViewById(R.id.uploaduascreen);
        uploadsrscreen= findViewById(R.id.uploadsrscreen);
        uascreen= findViewById(R.id.uascreen);
        srscreen= findViewById(R.id.srscreen);
        fAuth = FirebaseAuth.getInstance();
        fs = FirebaseFirestore.getInstance();
        sr2 = FirebaseStorage.getInstance().getReference();
        b = getIntent().getExtras();
        ver = (Version) b.getSerializable("Version");
        pro = (ProjectC) b.getSerializable("Project");
        uc = (UseCase) b.getSerializable("uc");
        srsd = (SRSDocument) b.getSerializable("sd");
        from = (String) b.getSerializable("from");
        cameFrom = b.get("cameFrom").toString();
        if(cameFrom.equals("Edit"))
            docID = b.get("docID").toString();
        aexplination = srsd.getExplination();
        ua.setCustomSelectionActionModeCallback(new Row_Screen.AbrCallback(ua));
        sr.setCustomSelectionActionModeCallback(new Row_Screen.AbrCallback(sr));
        if(getIntent().hasExtra("alt"))
            alt = (Alternative) b.getSerializable("alt");
        //if we are pressing on something already filled
        if(getIntent().hasExtra("row")){
            r = (Row) b.getSerializable("row");
            newrow = false;
            type = r.getType();
            num = r.getNum();
            if(type.equals("Main")){
                uatext.setText("UA"+num);
                srtext.setText("SR"+num);
            }else{
                uatext.setText("A"+alt.getAltnum()+".UA"+num);
                srtext.setText("A"+alt.getAltnum()+".SR"+num);
            }
            ua.setText(r.getUa());
            sr.setText(r.getSr());
            if(r.getUascreen() != null)
                Picasso.get().load(Uri.parse(r.getUascreen().getImagepath())).into(uascreen);
            if(r.getSrscreen() != null)
                Picasso.get().load(Uri.parse(r.getSrscreen().getImagepath())).into(srscreen);
        }
        //if we are creating new row
        if(getIntent().hasExtra("rowtype")){
            type = (String) b.get("rowtype");

            if(type.equals("Main")){
                int size = uc.getMainflowrows().size();
                num= getRowNum(size);
                r = new Row(type,num,uc.getUcnum());
                uatext.setText("UA"+num);
                srtext.setText("SR"+num);
            }else{
                int size = alt.getAltrows().size();
                num= getRowNum(size);
                r = new Row(type,num,uc.getUcnum());
                uatext.setText("A"+alt.getAltnum()+".UA"+num);
                srtext.setText("A"+alt.getAltnum()+".SR"+num);
            }
        }

        uploaduascreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser(UA_CODE);
            }
        });
        uploadsrscreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser(SR_CODE);
            }
        });
    }
    private int getRowNum(int size){
        int num;
        if(size == 0)
            num=1;
        else
            num=size+1;

        return num;
    }
    private void openFileChooser(int CODE){
        Intent in = new Intent();
        in.setType("image/*");
        in.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(in,CODE);

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Uri uri = data.getData();
        if (requestCode == UA_CODE) {
            uauri = uri;
            Picasso.get().load(uauri).into(uascreen);
            String imagename = System.currentTimeMillis() + "." + getExtension(uauri);
            uaimg = new ImagePath(imagename,uauri+"");
            r.setUascreen(uaimg);
            sendImage(uauri,imagename,"ua"+type);
        }
        if (requestCode == SR_CODE) {
            sruri = uri;
            Picasso.get().load(sruri).into(srscreen);
            String imagename = System.currentTimeMillis() + "." + getExtension(sruri);
            srimg = new ImagePath(imagename,sruri+"");
            r.setSrscreen(srimg);
            sendImage(sruri,imagename,"sr"+type);
        }
    }
    private String getExtension(Uri uri) {
//        String sIOimageurl = "" + uri;
//        return sIOimageurl.substring(sIOimageurl.lastIndexOf(".") + 1);
        ContentResolver CR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(CR.getType(uri));
    }

    private void sendImage(Uri uri, String Imagename, String photocopy){
        StorageTask st;
        StorageReference sr1;
        sr1 = sr2.child(Imagename);
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
                            Toast.makeText(Row_Screen.this, e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        });

    }

    @Override
    public void onBackPressed() {
        String sua = ua.getText().toString().trim();
        String ssr = sr.getText().toString().trim();
        //if we are creating new row
        if(newrow){
            Row rw = new Row(sua,ssr,type,uaimg,srimg,num,uc.getUcnum());
            if(type.equals("Main"))
                uc.getMainflowrows().add(rw);
            else
                uc.getAlternatives().get(alt.getAltnum()-1).getAltrows().add(rw);
        }//if we are editing a row
        else{
            if(type.equals("Main")) {
                uc.getMainflowrows().get(num - 1).setSr(ssr);
                uc.getMainflowrows().get(num - 1).setUa(sua);
                uc.getMainflowrows().get(num - 1).getSrscreen().setImagepath(sruri+"");
                uc.getMainflowrows().get(num - 1).getUascreen().setImagepath(uauri+"");

            }
            else{
                uc.getAlternatives().get(alt.getAltnum()-1).getAltrows().get(num-1).setSr(ssr);
                uc.getAlternatives().get(alt.getAltnum()-1).getAltrows().get(num-1).setUa(sua);
                uc.getAlternatives().get(alt.getAltnum()-1).getAltrows().get(num-1).getSrscreen().setImagepath(sruri+"");
                uc.getAlternatives().get(alt.getAltnum()-1).getAltrows().get(num-1).getUascreen().setImagepath(uauri+"");
            }

        }
        Intent i;
        if(from.equals("New"))
            i = new Intent(Row_Screen.this,NewUsecase.class);
        else
            i = new Intent(Row_Screen.this,EditUsecase.class);
        i.putExtra("Version",ver);
        i.putExtra("Project",pro);
        i.putExtra("uc",uc);
        i.putExtra("exp",aexplination);
        i.putExtra("sd",srsd);
        i.putExtra("cameFrom",cameFrom);
        i.putExtra("docID",docID);
        startActivity(i);
        super.onBackPressed();
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

}
