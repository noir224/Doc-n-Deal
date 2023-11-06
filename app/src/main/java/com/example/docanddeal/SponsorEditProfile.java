package com.example.docanddeal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SponsorEditProfile extends AppCompatActivity implements AdapterView.OnItemSelectedListener{
    EditText username, email, phone, description, Instagram, Facebook, Twitter;
    Spinner spinner;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String userID;
    Button save, cancel, ADDSP;
    FirebaseUser user;
    ImageView SImage;
    static Uri Simageurl;
    StorageTask upload;
    FirebaseStorage store;
    StorageReference storageReference;
    Map<String, Object> muser;
    DocumentReference doc;

    String sPhone, sEmail,sField , sName, sDescription, SsponsoredProjects, SrejectedProjects, simg, Sinstagram, Sfacebook, Stwitter;
    private static final int PICK_IMAGE_REQUEST = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sponsor_edit_profile);
        SImage = findViewById(R.id.SEditprofilePic);
        username = findViewById(R.id.SEdituserName);
        email = findViewById(R.id.SEditEmail);
        spinner = findViewById(R.id.editspinner);
        phone = findViewById(R.id.SEditphoneNumber);
        save = findViewById(R.id.SponsorSaveEdit);
        description = findViewById(R.id.SDescription);
        Instagram = findViewById(R.id.EditInstagram);
        Facebook = findViewById(R.id.EditFacebook);
        Twitter = findViewById(R.id.EditTwitter);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        userID = fAuth.getCurrentUser().getUid();
        cancel = findViewById(R.id.ScancelEdit);
        user = fAuth.getCurrentUser();
        store = FirebaseStorage.getInstance();
        doc = fStore.collection("users").document(userID);

        spinner.setOnItemSelectedListener((AdapterView.OnItemSelectedListener) this);

        List<String> categories = new ArrayList<String>();
        categories.add("Individual Investor");
        categories.add("eCommerce Company");
        categories.add("Web Development Company");
        categories.add("App Development Company");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);

        doc.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot ds = task.getResult();
                    if (ds.getString("phone") != null) {
                        phone.setText(ds.getString("phone"));
                        sPhone = ds.getString("phone");
                    }
                    if (ds.getString("email") != null) {
                        email.setText(ds.getString("email"));
                        sEmail = ds.getString("email");
                    }
                    if (ds.getString("field") != null) {
                        spinner.setSelection(dataAdapter.getPosition(ds.getString("field")));
                        sField = ds.getString("field");
                    }
                    if (ds.getString("username") != null) {
                        username.setText(ds.getString("username"));
                        sName = ds.getString("username");
                    }
                    if (ds.getString("imagepath") != null) {
                        simg = ds.getString("imagepath");
                        Picasso.get().load(Uri.parse(ds.getString("imagepath"))).resize(50, 50).centerCrop().into(SImage);
                    }
                    if (ds.getString("description") != null) {
                        description.setText(ds.getString("description"));
                        sDescription = ds.getString("description");
                    }
//                    if (ds.getString("SponsoredProjects") != null) {
//                        sponsoredProjects.setText(ds.getString("SponsoredProjects"));
//                        SsponsoredProjects = ds.getString("SponsoredProjects");
//                    }
//                    if (ds.getString("RejectedProjects") != null) {
//                        rejectedProjects.setText(ds.getString("RejectedProjects"));
//                        SrejectedProjects = ds.getString("RejectedProjects");
//                    }
                    if (ds.getString("instagram") != null) {
                        Instagram.setText(ds.getString("instagram"));
                        Sinstagram = ds.getString("instagram");
                    }
                    if (ds.getString("facebook") != null) {
                        Facebook.setText(ds.getString("facebook"));
                        Sfacebook = ds.getString("facebook");
                    }
                    if (ds.getString("twitter") != null) {
                        Twitter.setText(ds.getString("twitter"));
                        Stwitter = ds.getString("twitter");
                    }
                }
            }
        });
        storageReference = store.getReference();
        SImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CropImage.activity().setAspectRatio(1, 1).start(SponsorEditProfile.this);
                //startCropActivity();
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (username.getText().toString().isEmpty() || email.getText().toString().isEmpty() ||  spinner.getSelectedItem().toString().isEmpty() ) {
                    Toast.makeText(SponsorEditProfile.this, "Fill the Empty Fields.", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    if (validCellPhone(phone.getText().toString()) && validEmail(email.getText().toString())) {
                        emailChanged();
                        usernameChanged();
                        photoChanged();
                        fieldChanged();
                        phoneChanged();
                        descriptionChanged();
                        instagramChanged();
                        twitterChanged();
                        facebookChanged();
                        startActivity(new Intent(SponsorEditProfile.this, SponsorProfile.class));
                    } else {
                        Toast.makeText(SponsorEditProfile.this, "Please enter valid phone number and valid email", Toast.LENGTH_SHORT).show();
                        return;
                    }

                }
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SponsorEditProfile.this, SponsorProfile.class));
                finish();
            }
        });
    }

    private String getExtension(Uri uri) {
        /*ContentResolver CR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(CR.getType(uri));
        */
        String simageurl = "" + Simageurl;
        return simageurl.substring(simageurl.lastIndexOf(".")+1);
    }

    private void usernameChanged() {
        if (!username.getText().toString().equals(sName)) {
            doc.update("username", username.getText().toString());
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode==RESULT_OK&& data!=null) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            Simageurl = result.getUri();
            SImage.setImageURI(Simageurl);
            storageReference.child(fAuth.getCurrentUser().getUid() + "." + getExtension(Simageurl)).putFile(Simageurl).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    task.getResult().getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            doc.update("imagepath", uri.toString());
                        }
                    });
                }
            });
        }
        else {
            Toast.makeText(this,"Error, try again",Toast.LENGTH_SHORT).show();
        }

    }

    private void photoChanged() {
        if (Simageurl == null) {

        } else {
            if (!Simageurl.toString().equals(simg)) {
                StorageTask st;
                System.out.println("////" + getExtension(Simageurl));
                StorageReference sr1 = storageReference.child(fAuth.getCurrentUser().getUid() + "." + getExtension(Simageurl));
                st = sr1.putFile(Simageurl);

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
                        Uri dd = (Uri) task.getResult();
                        Simageurl = dd;
                        doc.update("imagepath", dd.toString());
                    }

                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(SponsorEditProfile.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });


            }
        }
    }

    private void phoneChanged() {
        if (!phone.getText().toString().equals(sPhone)) {
            doc.update("phone", phone.getText().toString());
        }
    }

    private void fieldChanged() {
        if (!spinner.getSelectedItem().toString().equals(sField)) {
            doc.update("field", spinner.getSelectedItem().toString());
        }
    }
    private void descriptionChanged() {
        if (!description.getText().toString().equals(sDescription)) {
            doc.update("description", description.getText().toString());
        }
    }

    private void instagramChanged() {
        if (!Instagram.getText().toString().equals(Sinstagram)) {
            doc.update("instagram", Instagram.getText().toString());
        }
    }
    private void twitterChanged() {
        if (!Twitter.getText().toString().equals(Stwitter)) {
            doc.update("twitter", Twitter.getText().toString());
        }
    }
    private void facebookChanged() {
        if (!Facebook.getText().toString().equals(Sfacebook)) {
            doc.update("facebook", Facebook.getText().toString());
        }
    }


    private void emailChanged() {
        if (!email.getText().toString().equals(sEmail)) {
            doc.update("email", email.getText().toString());
            fAuth.getCurrentUser().updateEmail(email.getText().toString()).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    FirebaseUser u = fAuth.getCurrentUser();
                    u.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(SponsorEditProfile.this, "Verification Email hase been sent. PLease verify it in order to be able to login again with the updated email", Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(SponsorEditProfile.this, e.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        }
    }
    public boolean validCellPhone(String number) {
        if (android.util.Patterns.PHONE.matcher(number).matches() && number.length() == 8) {
            return true;
        } else {
            return false;
        }
    }
    public boolean validEmail(String e){
        return android.util.Patterns.EMAIL_ADDRESS.matcher(e).matches();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String item = parent.getItemAtPosition(position).toString();
       // Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();
    }
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
    }
}