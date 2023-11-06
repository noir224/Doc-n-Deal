package com.example.docanddeal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.style.CharacterStyle;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
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
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class NewProject extends AppCompatActivity {
    CircleImageView logo;
    EditText name, date, ver, desc, link;
    Switch makeprivate;
    Spinner acspintv;
    Button create;
    DatePickerDialog.OnDateSetListener setListener;
    FirebaseAuth fAuth;
    FirebaseFirestore fs;
    StorageReference sr;
    Uri imguri;
    String userID;
    static String sname, sdate, sver, sdesc, slink, scateg, isprivate;
    DocumentReference dr;
    String s;
    Button addproject;
    TextView datee, version, description;
    ArrayList<String> SPIDs;
    CollectionReference cref,crefS;
    DocumentReference dref;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_project);

        datee = findViewById(R.id.textView1);
        version = findViewById(R.id.textView3);
        description = findViewById(R.id.textView4);
        logo = findViewById(R.id.createProjLogo);
        name = findViewById(R.id.createProjName1);
        date = findViewById(R.id.createProjDate1);
        ver = findViewById(R.id.createProjVersion1);
        desc = findViewById(R.id.createProjDesc1);
        link = findViewById(R.id.createProjLink1);
        acspintv = (Spinner) findViewById(R.id.createProjAC);
        addproject = findViewById(R.id.addProjButton);

        create = findViewById(R.id.createProjButton);
        makeprivate = (Switch) findViewById(R.id.makeprivate);
        ver.setText("1.0");
        SPIDs = new ArrayList<>();

        ArrayList<String> categories = new ArrayList<>();
        categories.add("Category");
        categories.add("Bussiness");
        categories.add("E-Commerce");
        categories.add("Educational");
        categories.add("Entertainment");
        categories.add("Lifestyle");
        categories.add("Productivity");
        categories.add("Social Media");
        categories.add("Utility");
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, R.layout.list_item, categories);
        acspintv.setAdapter(arrayAdapter);

        Calendar c = Calendar.getInstance();
        final int year = c.get(Calendar.YEAR);
        final int month = c.get(Calendar.MONTH) + 1;
        final int day = c.get(Calendar.DAY_OF_MONTH);
        date.setText(day + "/" + month + "/" + year);
        Map<String, Object> SParr = new HashMap<>();
        SParr.put("sponsoredProjects", SPIDs);


        fAuth = FirebaseAuth.getInstance();
        fs = FirebaseFirestore.getInstance();
        sr = FirebaseStorage.getInstance().getReference();

        String userID = fAuth.getCurrentUser().getUid();
        dr = fs.collection("users").document(userID);

        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog dpd = new DatePickerDialog(NewProject.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int day) {
                        month = month + 1;
                        String sdate = day + "/" + month + "/" + year;
                        date.setText(sdate);
                    }
                }, year, month, day);
                dpd.getDatePicker().setMaxDate(System.currentTimeMillis());
                dpd.show();

            }
        });

        logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CropImage.activity().setAspectRatio(1, 1).start(NewProject.this);
            }
        });
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createNewProject();
            }
        });
        dr.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                s = task.getResult().getString("type");
                if ((s.compareTo("creator") == 0))
                    addproject.setVisibility(View.GONE);
                else {
                    create.setVisibility(View.GONE);
                    date.setVisibility(View.GONE);
                    ver.setVisibility(View.GONE);
                    desc.setVisibility(View.GONE);
                    datee.setVisibility(View.GONE);
                    description.setVisibility(View.GONE);
                    version.setVisibility(View.GONE);
                    makeprivate.setVisibility(View.GONE);
                }
            }
        });
        addproject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sname = name.getText().toString().trim();
                sdesc = desc.getText().toString().trim();
                slink = link.getText().toString().trim();
                scateg = acspintv.getSelectedItem().toString();

                if (sname.matches("") || slink.matches("") || scateg.matches("Category"))
                    Toast.makeText(NewProject.this, "Fill all the fields please", Toast.LENGTH_LONG).show();
                else {
                    if (imguri == null) {
                        Uri uri = Uri.parse("https://firebasestorage.googleapis.com/v0/b/docanddeal-41fd8.appspot.com/o/addlogo.png?alt=media&token=a6c20b60-ce60-4d06-b402-481b72bc195f");
                        sendToDB(uri);
                    } else {
                        sendToDB(imguri);
                    }
                }

            }
        });
    }


    private void createNewProject() {
        sname = name.getText().toString().trim();
        sdate = date.getText().toString().trim();
        sver = ver.getText().toString().trim();
        sdesc = desc.getText().toString().trim();
        slink = link.getText().toString().trim();
        scateg = acspintv.getSelectedItem().toString();
        isprivate = makeprivate.isChecked() + "";

        if (sname.matches("") || sdate.matches("") || sver.matches("") || sdesc.matches("") || scateg.matches("Category"))
            Toast.makeText(NewProject.this, "Fill required fields the fields please", Toast.LENGTH_LONG).show();
        else {
            if (Double.parseDouble(sver) > 0) {

                //CollectionReference cref1 = fs.collection("versions");
                if (imguri == null) {
                    Uri uri = Uri.parse("https://firebasestorage.googleapis.com/v0/b/docanddeal-41fd8.appspot.com/o/addlogo.png?alt=media&token=a6c20b60-ce60-4d06-b402-481b72bc195f");
                    sendToDB(uri);
                } else {
                    sendToDB(imguri);
                }

            } else {
                Toast.makeText(NewProject.this, "you cannot have a minus version", Toast.LENGTH_LONG).show();

            }
        }
    }


    private String getExtension(Uri uri) {
        String sIOimageurl = "" + imguri;
        return sIOimageurl.substring(sIOimageurl.lastIndexOf(".") + 1);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                imguri = result.getUri();
                logo.setImageURI(imguri);

            } else {
                Toast.makeText(this, "Error, try again", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void sendToDB(Uri imguri) {
        Uri uri = Uri.parse("https://firebasestorage.googleapis.com/v0/b/docanddeal-41fd8.appspot.com/o/addlogo.png?alt=media&token=a6c20b60-ce60-4d06-b402-481b72bc195f");
        userID = fAuth.getCurrentUser().getUid();
        cref = fs.collection("projects");
        crefS = fs.collection("sponsored_projects");
        dref = fs.collection("users").document(userID);

        StorageTask st;
        StorageReference sr1;
        if (!imguri.equals(uri)) {
            sr1 = sr.child(System.currentTimeMillis() + "." + getExtension(imguri));
            st = sr1.putFile(imguri);
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
                        ArrayList<Version> varr = new ArrayList<>();
                        Version v = new Version(sver, sdate);
                        varr.add(v);
                        dr.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                s = task.getResult().getString("type");
                                sendtoDB2(dd,s,varr);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(NewProject.this, e.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                }
            });
        } else {
            dr.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    s = task.getResult().getString("type");
                    ArrayList<Version> varr = new ArrayList<>();
                    Version v = new Version(sver, sdate);
                    varr.add(v);
                    sendtoDB2(imguri,s,varr);


                }
            });
        }
    }

    private void sendtoDB2(Uri uri,String type,ArrayList<Version> varr) {
        if(type.equals("creator")){
            ProjectC p = new ProjectC(sname, uri + "", sdesc, scateg, isprivate + "", slink, userID, null, varr);
            cref.add(p).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                @Override
                public void onSuccess(DocumentReference documentReference) {
                    dref.update("projects", FieldValue.arrayUnion(documentReference.getId()));
                    documentReference.update("pid", documentReference.getId());
                    startActivity(new Intent(NewProject.this, CreatorMainProjects.class));
                    finish();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(NewProject.this, e.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        }else{
            SponsoredProjects p2 = new SponsoredProjects(sname, uri + "", scateg, slink, userID, null,isprivate);
            crefS.add(p2).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                @Override
                public void onSuccess(DocumentReference documentReference) {
                    String id = documentReference.getId();
                    dr.update("sponsoredProjects",
                            FieldValue.arrayUnion(id));
                    documentReference.update("spid", id);
                    startActivity(new Intent(NewProject.this, SponsorProfile.class));
                    finish();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(NewProject.this, e.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        }
    }
}