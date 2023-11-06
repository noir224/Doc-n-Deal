package com.example.docanddeal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;

public class Login extends AppCompatActivity {
    EditText email,pass;
    TextView forgot;
    FirebaseAuth fAuth;
    Button login,signup;
    FirebaseFirestore fs;
    boolean passwordvis;
    String userid,type;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        final String TAG = "TAG";
        email = findViewById(R.id.emailLogin);
        pass = findViewById(R.id.passLogin);
        forgot = findViewById(R.id.forgot);
        fAuth = FirebaseAuth.getInstance();
        login = findViewById(R.id.LoginLoginBtn);
        signup = findViewById(R.id.SignUpLoginBtn);
        fs =  FirebaseFirestore.getInstance();

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String smail = email.getText().toString().trim();
                String spass = pass.getText().toString().trim();
                if(smail.matches("") || spass.matches(""))
                    Toast.makeText(Login.this,"Fill all the fields please", Toast.LENGTH_LONG).show();
                else{
                    //authenicate the user
                    fAuth.signInWithEmailAndPassword(smail,spass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                if (fAuth.getCurrentUser().isEmailVerified()){
                                    userid = fAuth.getCurrentUser().getUid();
                                    DocumentReference dref = fs.collection("users").document(userid);
                                    dref.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                        @Override
                                        public void onSuccess(DocumentSnapshot documentSnapshot) {

                                            fs.collection("tokens").document(userid).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                                @Override
                                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                    if (documentSnapshot.exists()) {
                                                        FirebaseMessaging.getInstance().getToken()
                                                                .addOnCompleteListener(new OnCompleteListener<String>() {
                                                                    @Override
                                                                    public void onComplete(@NonNull Task<String> task) {
                                                                        if (!task.isSuccessful()) {
                                                                            System.out.print("no token found");
                                                                            return;
                                                                        } else {

                                                                            // Get new FCM registration token
                                                                            String token = task.getResult();
                                                                            DocumentReference dref = fs.collection("tokens").document(userid);
                                                                            dref.update("token", token);
                                                                            System.out.print("okkkk");
                                                                        }
                                                                    }
                                                                });
                                                    }
                                                    else{
                                                        FirebaseMessaging.getInstance().getToken()
                                                                .addOnCompleteListener(new OnCompleteListener<String>() {
                                                                    @Override
                                                                    public void onComplete(@NonNull Task<String> task) {
                                                                        if (!task.isSuccessful()) {
                                                                            System.out.print("no token found");
                                                                            return;
                                                                        } else {

                                                                            // Get new FCM registration token
                                                                            String token = task.getResult();
                                                                            DocumentReference dref = fs.collection("tokens").document(userid);
                                                                            Token t = new Token(userid,token);
                                                                            dref.set(t).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                @Override
                                                                                public void onSuccess(Void unused) {
                                                                                }
                                                                            });


                                                                        }
                                                                    }
                                                                });
                                                    }
                                                }});



                                            if(documentSnapshot.getString("type").compareTo("creator")==0)
                                                startActivity(new Intent(Login.this, CreatorMainProjects.class));
                                            else
                                                startActivity(new Intent(Login.this, SponsorMain.class));
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(Login.this,"Error!", Toast.LENGTH_LONG).show();
                                            Log.d(TAG,e.toString());
                                        }
                                    });
                                }
                                else{
                                    Toast.makeText(Login.this,"Please Verify your Email!", Toast.LENGTH_LONG).show();
                                }}


                            else{
                                Toast.makeText(Login.this,"Error! "+ task.getException().getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
            }
        });

        forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText resetmail = new EditText(v.getContext());
                AlertDialog.Builder passwordresetdia= new AlertDialog.Builder(v.getContext());
                passwordresetdia.setTitle("Reset Password");
                passwordresetdia.setMessage("Enter your email to recieve reset link");
                passwordresetdia.setView(resetmail);
                passwordresetdia.setPositiveButton("Send", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //extract email and send reset link
                        String mail = resetmail.getText().toString().trim();
                        fAuth.sendPasswordResetEmail(mail).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(Login.this,"Reset link has been sent to your email", Toast.LENGTH_LONG).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(Login.this,"Error! "+ e.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                });
                passwordresetdia.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                passwordresetdia.create().show();
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
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Login.this, UserType.class);
                startActivity(i);
            }
        });
    }
}