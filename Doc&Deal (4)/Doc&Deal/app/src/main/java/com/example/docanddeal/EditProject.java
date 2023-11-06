package com.example.docanddeal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditProject extends AppCompatActivity {
    CircleImageView logo;
    EditText name, desc,link;
    Spinner acspintv;
    Button edit;
    static Uri imguri;
    FirebaseFirestore fs;
    DocumentReference dref,ds;
    StorageReference sr;
    Switch makeprivate;
    SponsoredProjects sp;
    //String pId,spId;
    ProjectC p;
    TextView des;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_project);
        logo = findViewById(R.id.editProjLogo);
        name = findViewById(R.id.editProjName1);
        desc = findViewById(R.id.editProjDesc1);
        edit = findViewById(R.id.editProjButton);
        link = findViewById(R.id.editProjLink1);
        acspintv  = (Spinner) findViewById(R.id.editProjAC);
        makeprivate = findViewById(R.id.makepriv);
        des = findViewById(R.id.textView4);

        ArrayList<String> categories = new ArrayList<>();
        categories.add("Bussiness");
        categories.add("E-Commerce");
        categories.add("Educational");
        categories.add("Entertainment");
        categories.add("Lifestyle");
        categories.add("Productivity");
        categories.add("Social Media");
        categories.add("Utility");
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this,R.layout.list_item,categories);
        acspintv.setAdapter(arrayAdapter);
        Bundle b = getIntent().getExtras();
        String type = (String) b.getSerializable("type");
        fs = FirebaseFirestore.getInstance();
        sr = FirebaseStorage.getInstance().getReference();

        if(type.equals("sponsor")){
            sp = (SponsoredProjects) b.getSerializable("sProject");
            String spId = sp.getSpid();
            ds= fs.collection("sponsored_projects").document(spId);
            name.setText(sp.getName());
            link.setText(sp.getLink());
            makeprivate.setChecked(Boolean.parseBoolean(sp.getIsPrivate()));
            desc.setVisibility(View.GONE);
            des.setVisibility(View.GONE);
            acspintv.setSelection(arrayAdapter.getPosition(sp.getType()));
            Picasso.get().load(Uri.parse(sp.getLogo())).resize(50,50).centerCrop().into(logo);
        }
        if(type.equals("creator")){
            p = (ProjectC) b.getSerializable("Project");
            String pId = p.getPid();
            dref = fs.collection("projects").document(pId);
            name.setText(p.getName());
            desc.setText(p.getDescription());
            link.setText(p.getLink());
            acspintv.setSelection(arrayAdapter.getPosition(p.getType()));
            Picasso.get().load(Uri.parse(p.getLogo())).resize(50,50).centerCrop().into(logo);}





        logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CropImage.activity().setAspectRatio(1, 1).start(EditProject.this);
            }
        });

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String slink = link.getText().toString().trim();
                String scateg = acspintv.getSelectedItem().toString();
                String isprivate = makeprivate.isChecked()+"";
                String sname = name.getText().toString().trim();
                if(type.equals("creator")) {
                    String sdesc = desc.getText().toString().trim();
                    if (sname.matches("") || sdesc.matches("") ||scateg.matches("Category"))
                        Toast.makeText(EditProject.this, "Fill all the fields please", Toast.LENGTH_LONG).show();
                    else {
                        if(!sname.equals(p.getName()))
                            dref.update("name",sname);
                        if(!sdesc.equals(p.getDescription()))
                            dref.update("description", sdesc);
                        if(!slink.equals(p.getLink()))
                            dref.update("link",slink);
                        if(!scateg.equals(p.getType()))
                            dref.update("type", scateg);
                        if(!isprivate.equals(p.getIsprivate()))
                            dref.update("isprivate", isprivate);
                        //if(getApplicationContext())
                        startActivity(new Intent(EditProject.this, CreatorMainProjects.class));
                        finish();
                    }
                }
                else{
                    if (sname.matches("")  ||scateg.matches("Category"))
                        Toast.makeText(EditProject.this, "Fill all the fields please", Toast.LENGTH_LONG).show();
                    else {
                        if(!sname.equals(sp.getName()))
                            ds.update("name",sname);
                        if(!slink.equals(sp.getLink()))
                            ds.update("link",slink);
                        if(!scateg.equals(sp.getType()))
                            ds.update("type", scateg);
                        if(!isprivate.equals(sp.getIsPrivate()))
                            ds.update("isprivate", isprivate);
                        //if(getApplicationContext())
                        startActivity(new Intent(EditProject.this, SponsorProfile.class));
                        finish();
                    }

                }
            }
        });
    }
    private String getExtension(Uri uri) {
        String sIOimageurl = "" + imguri;
        return sIOimageurl.substring(sIOimageurl.lastIndexOf(".") + 1);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode==RESULT_OK&& data!=null) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            imguri = result.getUri();
            logo.setImageURI(imguri);
            sr.child(System.currentTimeMillis() + "." + getExtension(imguri)).putFile(imguri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    task.getResult().getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            dref.update("logo", uri.toString());
                        }
                    });
                }
            });
        }
        else {
            Toast.makeText(this,"Error, try again",Toast.LENGTH_SHORT).show();
        }

    }
  /*
    private void photoChanged(String simage,DocumentReference dref) {
        if(!imguri.toString().equals(simage)) {
            StorageTask st;
            System.out.println("////" + getExtension(imguri));
            StorageReference sr1 = sr.child(fAuth.getCurrentUser().getUid() + "." + getExtension(imguri));
            st = sr1.putFile(imguri);
            st.continueWithTask(new Continuation() {
                @Override
                public Object then(@NonNull Task task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }
                    return sr1.getDownloadUrl();

                }
            }) .addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {
                    Uri dd = (Uri) task.getResult();
                    imguri = dd;
                    dref.update("imagepath", dd.toString());

                }

            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(NewProject.this, e.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        }

    }
    private void descChanged(String sDesc,DocumentReference dref) {
        if (!desc.getText().toString().equals(sDesc)) {
            dref.update("phone", desc.getText().toString());
        }
    }
*/
}