package com.example.docanddeal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;

public class MainActivity extends AppCompatActivity {
    TextView secret;
    DocumentReference dr;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FirebaseAuth fAuth = FirebaseAuth.getInstance();
        FirebaseFirestore fs = FirebaseFirestore.getInstance();




        Handler h = new Handler(getMainLooper());
        h.postDelayed(new Runnable(){
            @Override
            public void run() {
                if(fAuth.getCurrentUser() != null){
                    String userID = fAuth.getCurrentUser().getUid();
                    dr = fs.collection("users").document(userID);
                    dr.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                          /*  secret.setText(task.getResult().getString("type"));
                            if((secret.getText().toString().compareTo("creator")==0))
                                startActivity(new Intent(MainActivity.this, CreatorMainProjects.class));
                            else
                                startActivity(new Intent(MainActivity.this, SponsorMain.class));
                            finish();
                            */
                            fs.collection("tokens").document(userID).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
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
                                                            DocumentReference dref = fs.collection("tokens").document(userID);
                                                            dref.update("token", token);
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
                                                            DocumentReference dref = fs.collection("tokens").document(userID);
                                                            Token t = new Token(userID,token);
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
                            String type = task.getResult().get("type").toString();
                           if(type.equals("creator"))
                               startActivity(new Intent(MainActivity.this, CreatorMainProjects.class));
                           else
                               startActivity(new Intent(MainActivity.this, SponsorMain.class));
                            finish();
                        }
                    });

                }
                else{
                    Intent i = new Intent(MainActivity.this, Login.class);
                    startActivity(i);
                    finish();}
            }
        }, 2000);


    }
}