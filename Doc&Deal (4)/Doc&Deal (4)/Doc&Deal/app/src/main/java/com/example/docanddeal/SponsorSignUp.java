package com.example.docanddeal;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
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
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SponsorSignUp extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    String userID;
    EditText username,pass,email,cpass;
    Button signup,login;
    FirebaseAuth fAuth;
    ImageView img;
    String type = "sponsor";
    FirebaseFirestore fs;
    StorageReference sr,sr2;
    boolean passwordvis, cpasswordvis;
    //get the spinner from the xml.
    Map<String,Object> muser;
    Uri imguri;
    Spinner field;
    Spinner spinner;
    ArrayList<String> f2;
    ArrayAdapter<String> fadapter;
    //String[] fields = new String[]{"Individual Investor", "eCommerce Company", "Web Development Company","App Development Company"};
    private static final int PICK_IMAGE_REQUEST=1;
    private static final int PICK_PDF_REQUEST=1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sponsor_sign_up);
        spinner = (Spinner)findViewById(R.id.spinner2);
        final String TAG = "TAG";
        username = findViewById(R.id.SponsorUsernameSignUp);
        email = findViewById(R.id.SponsorEmailSignUp);
        pass = findViewById(R.id.SponsorPassSignUp);
        cpass= findViewById(R.id.SponsorcPassSignUp);
        signup = findViewById(R.id.SignUpSponsorbtn);
        login = findViewById(R.id.LoginSponsorbtn);
        img = findViewById(R.id.stuimg);

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



        sr = FirebaseStorage.getInstance().getReference();
        TextView secret = findViewById(R.id.secstusign);
        fAuth =FirebaseAuth.getInstance();
        fs =  FirebaseFirestore.getInstance();

        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CropImage.activity().setAspectRatio(1,1).start(SponsorSignUp.this);
            }
        });
        pass.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                final int Right =2;
                if (event.getAction()== MotionEvent.ACTION_UP){
                    if(event.getRawX()>=pass.getRight()-pass.getCompoundDrawables()[Right].getBounds().width()){
                        int selection = pass.getSelectionEnd();
                        if(passwordvis){
                            //to hide pass
                            pass.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.custom_lock_icon,0,R.drawable.ic_baseline_visibility_off_24,0);
                            pass.setTransformationMethod(PasswordTransformationMethod.getInstance());
                            passwordvis = false;
                        }else {
                            //to show pass
                            pass.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.custom_lock_icon,0,R.drawable.ic_baseline_visibility_24,0);
                            pass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                            passwordvis = true;
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

        if(fAuth.getCurrentUser() != null){
            userID = fAuth.getCurrentUser().getUid();
            DocumentReference dr = fs.collection("users").document(userID);
            dr.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                    secret.setText(value.getString("type"));
                }
            });
            if((secret.getText().toString().compareTo("sponsor")==0))
                startActivity(new Intent(SponsorSignUp.this, Spinner.class));
            finish();
        }

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String susername = username.getText().toString().trim();
                String smail = email.getText().toString().trim();
                String spass = pass.getText().toString().trim();
                String spassc = cpass.getText().toString().trim();
                String sfield = spinner.getSelectedItem().toString();

                if (imguri==null||susername.matches("") ||smail.matches("") || sfield.matches("") || spass.matches("") || spassc.matches(""))
                    Toast.makeText(SponsorSignUp.this, "Fill all the fields please", Toast.LENGTH_LONG).show();
                else {
                    if (spass.length() < 6)
                        Toast.makeText(SponsorSignUp.this, "Password must be at least 6 characters", Toast.LENGTH_LONG).show();
                    else if (!spass.equals(spassc))
                        Toast.makeText(SponsorSignUp.this, "password and password confrimation do not match", Toast.LENGTH_LONG).show();
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
                                            Toast.makeText(SponsorSignUp.this, "Verification Email hase been sent", Toast.LENGTH_LONG).show();
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
                                            SponsorUser u = new SponsorUser(smail,imguri+"","sponsor",susername,userID,sfield);

                                            dref.set(u).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    Log.d(TAG, "OnSuccess: user profile is created for "+userID );
                                                }
                                            });
                                            Toast.makeText(SponsorSignUp.this, "Account has been created successfully", Toast.LENGTH_LONG).show();

                                            startActivity(new Intent(SponsorSignUp.this, Login.class));
                                            finish();
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(SponsorSignUp.this, "Error! "+e.getMessage(), Toast.LENGTH_LONG).show();
                                        }
                                    });

                                } else {
                                    Toast.makeText(SponsorSignUp.this, "Error! " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
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

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // On selecting a spinner item
        String item = parent.getItemAtPosition(position).toString();

        // Showing selected spinner item
       // Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();
    }
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
    }

}
