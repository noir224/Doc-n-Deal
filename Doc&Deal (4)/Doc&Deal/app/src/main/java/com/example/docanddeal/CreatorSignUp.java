package com.example.docanddeal;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CreatorSignUp extends AppCompatActivity {
    String userID;
    EditText username,pass,email,cpass;
    Button signup,login;
    FirebaseAuth fAuth;
    FirebaseFirestore fs;
    StorageReference sr;
    //Uri filepath;
    boolean passwordvis, cpasswordvis;
    String type;
    ImageView img;
    Uri imguri;
    //private static final int PICK_IMAGE_REQUEST=1;
    //private static final int PICK_PDF_REQUEST=1;
    //TextView secretimg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creator_sign_up);
        final String TAG = "TAG";
        username = findViewById(R.id.CreatorUsernameSignUp);
        email = findViewById(R.id.CreatorEmailSignUp);
        pass = findViewById(R.id.CreatorPassSignUp);
        cpass= findViewById(R.id.CreatorcPassSignUp);
        signup = findViewById(R.id.SignUpCreatorbtn);
        login = findViewById(R.id.LoginCreatorbtn);
        img = findViewById(R.id.stuimg);

        sr = FirebaseStorage.getInstance().getReference();

        TextView secret = findViewById(R.id.secstusign);
        ArrayList<String> paths = new ArrayList<>();



        fAuth =FirebaseAuth.getInstance();
        fs =  FirebaseFirestore.getInstance();

        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CropImage.activity().setAspectRatio(1,1).start(CreatorSignUp.this);
            }
        });


        if(fAuth.getCurrentUser() != null){
            userID = fAuth.getCurrentUser().getUid();
            DocumentReference dr = fs.collection("users").document(userID);
            dr.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                    secret.setText(value.getString("type"));
                }
            });
            if((secret.getText().toString().compareTo("creator")==0))
                startActivity(new Intent(CreatorSignUp.this, CreatorMainProjects.class));
            finish();
        }

        pass.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                final int Right =2;
                if (event.getAction()== MotionEvent.ACTION_UP){
                    if(event.getRawX()>=pass.getRight()-pass.getCompoundDrawables()[Right].getBounds().width()){
                        int selection = pass.getSelectionEnd();
                        if(cpasswordvis){
                            //to hide pass
                            pass.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.custom_lock_icon,0,R.drawable.ic_baseline_visibility_off_24,0);
                            pass.setTransformationMethod(PasswordTransformationMethod.getInstance());
                            cpasswordvis = false;
                        }else {
                            //to show pass
                            pass.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.custom_lock_icon,0,R.drawable.ic_baseline_visibility_24,0);
                            pass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                            cpasswordvis = true;
                        }
                        pass.setSelection(selection);
                        return true;
                    }
                }
                return false;
            }
        });


        cpass.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                final int Right =2;
                if (event.getAction()== MotionEvent.ACTION_UP){
                    if(event.getRawX()>=cpass.getRight()-cpass.getCompoundDrawables()[Right].getBounds().width()){
                        int selection = cpass.getSelectionEnd();
                        if(cpasswordvis){
                            //to hide pass
                            cpass.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.custom_lock_icon,0,R.drawable.ic_baseline_visibility_off_24,0);
                            cpass.setTransformationMethod(PasswordTransformationMethod.getInstance());
                            cpasswordvis = false;
                        }else {
                            //to show pass
                            cpass.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.custom_lock_icon,0,R.drawable.ic_baseline_visibility_24,0);
                            cpass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                            cpasswordvis = true;
                        }
                        cpass.setSelection(selection);
                        return true;
                    }
                }
                return false;
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String susername = username.getText().toString().trim();
                String smail = email.getText().toString().trim();
                String spass = pass.getText().toString().trim();
                String spassc = cpass.getText().toString().trim();

                if (imguri==null||susername.matches("") ||smail.matches("") || spass.matches("") || spassc.matches(""))
                    Toast.makeText(CreatorSignUp.this, "Fill all the fields please", Toast.LENGTH_LONG).show();
                else {
                    if (spass.length() < 6)
                        Toast.makeText(CreatorSignUp.this, "Password must be at least 6 characters", Toast.LENGTH_LONG).show();
                    else if (!spass.equals(spassc))
                        Toast.makeText(CreatorSignUp.this, "password and password confrimation do not match", Toast.LENGTH_LONG).show();
                    else{
                        //register user in firebase
                        fAuth.createUserWithEmailAndPassword(smail, spass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // send verification link
                                    FirebaseUser u = fAuth.getCurrentUser();
                                    u.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Toast.makeText(CreatorSignUp.this, "Verification Email hase been sent", Toast.LENGTH_LONG).show();
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.d(TAG,"On Failure: Email not sent "+e.getMessage());
                                        }
                                    });

                                    userID = fAuth.getCurrentUser().getUid();
                                    DocumentReference dref = fs.collection("users").document(userID);
                                    StorageTask st ;
                                    StorageReference sr1 = sr.child(fAuth.getCurrentUser().getUid()+"."+getExtension(imguri));
                                    st = sr1.putFile(imguri);
                                    st.continueWithTask(new Continuation() {
                                        @Override
                                        public Object then(@NonNull Task task) throws Exception {
                                            if (!task.isSuccessful()){
                                                throw task.getException();
                                            }
                                            return sr1.getDownloadUrl();

                                        }
                                    }).addOnCompleteListener(new OnCompleteListener() {
                                        @Override
                                        public void onComplete(@NonNull Task task) {
                                            Uri dd = (Uri) task.getResult();
                                            imguri=dd;
                                            User u = new User(smail,imguri+"","creator",susername);

                                            dref.set(u).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    Log.d(TAG, "OnSuccess: user profile is created for "+userID );
                                                }
                                            });
                                            Toast.makeText(CreatorSignUp.this, "Account has been created successfully", Toast.LENGTH_LONG).show();

                                            startActivity(new Intent(CreatorSignUp.this, Login.class));
                                            finish();
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(CreatorSignUp.this, "Error! "+e.getMessage(), Toast.LENGTH_LONG).show();
                                        }
                                    });

                                } else {
                                    Toast.makeText(CreatorSignUp.this, "Error! " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                }
                            }
                        });

                    }

                }


            }
        });



    }




    private String getExtension(Uri uri){
        String sIOimageurl = ""+imguri;
        return sIOimageurl.substring(sIOimageurl.lastIndexOf(".")+1);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode==RESULT_OK&& data!=null) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                imguri = result.getUri();
                img.setImageURI(imguri);

            } else {
                Toast.makeText(this,"Error, try again",Toast.LENGTH_SHORT).show();
            }
        }
    }

}
