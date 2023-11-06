package com.example.docanddeal;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
public class UserType extends AppCompatActivity {
    TextView creatorlabel;
    ImageView creator;
    TextView sponsorlabel;
    ImageView sponsor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_type);
        creatorlabel = findViewById(R.id.creatorlabel);
        creator = findViewById(R.id.creatoricon);
        creatorlabel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(UserType.this, CreatorSignUp.class);
                startActivity(i);
            }
        });
        creator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(UserType.this, CreatorSignUp.class);
                startActivity(i);
                finish();
            }
        });
        sponsorlabel = findViewById(R.id.sponsorlabel);
        sponsor = findViewById(R.id.sponsoricon);
        sponsorlabel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(UserType.this, SponsorSignUp.class);
                startActivity(i);
            }
        });
        sponsor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(UserType.this, SponsorSignUp.class);
                startActivity(i);
                finish();
            }
        });
    }
}