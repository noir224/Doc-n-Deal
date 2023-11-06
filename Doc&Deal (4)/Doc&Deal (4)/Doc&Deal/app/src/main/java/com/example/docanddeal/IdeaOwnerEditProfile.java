package com.example.docanddeal;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
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
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class IdeaOwnerEditProfile extends AppCompatActivity {
    EditText username,field,email,phone;;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String userID;
    Button save,cancel;
    FirebaseUser user;
    ImageView IOImage;
    static Uri IOimageurl;
    StorageTask upload;
    FirebaseStorage store;
    StorageReference storageReference;
    Map<String,Object> muser;
    DocumentReference doc;

    String sPhone,sEmail,sField,sName, simg ;
    private static final int PICK_IMAGE_REQUEST=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_idea_owner_edit_profile);
        IOImage = findViewById(R.id.IOEditprofilePic);
        username = findViewById(R.id.IOEdituserName);
        email = findViewById(R.id.IOEditemail);
        field = findViewById(R.id.IOEditfield);
        phone = findViewById(R.id.IOEditphoneNumber);
        save = findViewById(R.id.creatorSaveEdit);
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        userID = fAuth.getCurrentUser().getUid();
        cancel = findViewById(R.id.cancelEdit);
        user = fAuth.getCurrentUser();
        store = FirebaseStorage.getInstance();
        doc = fStore.collection("users").document(userID);

        doc.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot ds = task.getResult();
                    if (ds.getString("phone")!=null){
                        phone.setText(ds.getString("phone"));
                        sPhone= ds.getString("phone");}
                    if (ds.getString("email")!=null){
                        email.setText(ds.getString("email"));
                        sEmail=ds.getString("email");}
                    if (ds.getString("field")!=null){
                        field.setText(ds.getString("field"));
                        sField=ds.getString("field");}
                    if (ds.getString("username")!=null){
                        username.setText(ds.getString("username"));
                        sName = ds.getString("username");}
                    if (ds.getString("imagepath")!=null){
                        simg = ds.getString("imagepath");
                        Picasso.get().load(Uri.parse(ds.getString("imagepath"))).resize(50,50).centerCrop().into(IOImage);
                    }}
            }
        });
        storageReference = store.getReference();
        IOImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CropImage.activity().setAspectRatio(1,1).start(IdeaOwnerEditProfile.this);
                //startCropActivity();
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(username.getText().toString().isEmpty()  || email.getText().toString().isEmpty() || phone.getText().toString().isEmpty() || field.getText().toString().isEmpty() ){
                    Toast.makeText(IdeaOwnerEditProfile.this, "Fill the empty fields.", Toast.LENGTH_SHORT).show();
                    return;
                }
                else{
                    if(validCellPhone(phone.getText().toString()) && validEmail(email.getText().toString())) {
                        usernameChanged();
                        photoChanged();
                        emailChanged();
                        fieldChanged();
                        phoneChanged();
                        startActivity(new Intent(IdeaOwnerEditProfile.this, IdeaOwnerProfilePage.class));
                    }
                    else{
                        Toast.makeText(IdeaOwnerEditProfile.this, "Please enter valid phone number and email", Toast.LENGTH_SHORT).show();
                        return;
                    }

                }}


        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(IdeaOwnerEditProfile.this, IdeaOwnerProfilePage.class));
                finish();
            }
        });


    }

    private String getExtension(Uri uri){
        /*ContentResolver CR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(CR.getType(uri));
        */
        String sIOimageurl = ""+IOimageurl;
        return sIOimageurl.substring(sIOimageurl.lastIndexOf(".")+1);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode==RESULT_OK&& data!=null) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            IOimageurl = result.getUri();
            IOImage.setImageURI(IOimageurl);
            storageReference.child(fAuth.getCurrentUser().getUid() + "." + getExtension(IOimageurl)).putFile(IOimageurl).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
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

    private void usernameChanged() {
        if (!username.getText().toString().equals(sName)) {
            doc.update("username", username.getText().toString());
        }
    }
    private void photoChanged() {
        if(IOimageurl==null){

        }else {
            if (!IOimageurl.toString().equals(simg)) {
                StorageTask st;
                System.out.println("////"+getExtension(IOimageurl));
                StorageReference sr1 = storageReference.child(fAuth.getCurrentUser().getUid() + "." + getExtension(IOimageurl));
                st = sr1.putFile(IOimageurl);

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
                        IOimageurl = dd;
                        doc.update("imagepath", dd.toString());

                    }

                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(IdeaOwnerEditProfile.this, e.getMessage(), Toast.LENGTH_LONG).show();
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
        if (!field.getText().toString().equals(sField)) {
            doc.update("field", field.getText().toString());
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
                            Toast.makeText(IdeaOwnerEditProfile.this, "Verification Email has been sent. PLease verify it in order to be able to login again with the updated email", Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(IdeaOwnerEditProfile.this,e.getMessage(),Toast.LENGTH_LONG).show();
                }
            });


        }
    }
    public boolean validCellPhone(String number) {
        if(android.util.Patterns.PHONE.matcher(number).matches() && number.length()==8) {
            return true;
        }
        else {
            return false;

        }
    }
    public boolean validEmail(String e){
        return android.util.Patterns.EMAIL_ADDRESS.matcher(e).matches();
    }


}
