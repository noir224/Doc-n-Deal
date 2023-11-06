package com.example.docanddeal;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.SearchManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipDrawable;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class SearchSponsors extends AppCompatActivity implements AdapterView.OnItemSelectedListener{
    TextView logout, home, empty,notifications;
    CircularProgressIndicator progressBar;
    CircleImageView profilepic;
    FirebaseFirestore fs;
    FirebaseAuth fAuth;
    DocumentReference dref;
    String userID;
    ArrayList<SponsorUser> sponsors, sponsorsAZ, sponsorsZA,sponsorsnow;
    SponsorsAdapter sa;
    RecyclerView RV;
    ArrayAdapter<String> adapterItems;
    String[] categorylist ={"Individual Investor","eCommerce Company","Web Development Company","App Development Company"};
    AutoCompleteTextView filterdropdown;
    Chip azchip, zachip;
    ChipGroup chipgroup;
    boolean azchipselected, zachipselected;
    //spinner
    Spinner field;
    Spinner spinner;
    ArrayList<String> f2;
    ArrayAdapter<String> fadapter;
    ImageView notired;
    boolean notiopen;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_sponsors);
        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        home=findViewById(R.id.homeSS);
        profilepic=findViewById(R.id.profilePicSS);
        empty= findViewById(R.id.ifnullSponsors);
        logout=findViewById(R.id.LogoutBtnSS);
        progressBar =findViewById(R.id.progBarSponsors);
        notifications = findViewById(R.id.notiSS);
        fAuth = FirebaseAuth.getInstance();
        userID = fAuth.getCurrentUser().getUid();
        fs = FirebaseFirestore.getInstance();
        dref = fs.collection("users").document(userID);
        sponsors=new ArrayList<>();
        azchip = findViewById(R.id.azchip);
        zachip = findViewById(R.id.zachip);
        chipgroup = findViewById(R.id.cgOptions);
        spinner = (Spinner)findViewById(R.id.filterdrop);
        sponsorsnow = new ArrayList<>(sponsors);
        notired = findViewById(R.id.newnotiCsponsors);



        dref.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot ds = task.getResult();
                    Picasso.get().load(Uri.parse(ds.getString("imagepath"))).resize(50,50).centerCrop().into(profilepic);
                }
            }
        });


        spinner.setOnItemSelectedListener((AdapterView.OnItemSelectedListener) this);

        List<String> categories = new ArrayList<String>();
        categories.add("All");
        categories.add("Your Previous Sponsors");
        categories.add("Individual Investor");
        categories.add("eCommerce Company");
        categories.add("Web Development Company");
        categories.add("App Development Company");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);

        //String sfield = spinner.getSelectedItem().toString();



        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String type = spinner.getSelectedItem().toString();
                empty.setVisibility(View.GONE);
                if(type.equalsIgnoreCase("Individual Investor")){
                    ArrayList<SponsorUser> sponsorsindv = new ArrayList<>();
                    for(int m=0;m< sponsors.size();m++){
                        if(sponsors.get(m).getField().equalsIgnoreCase(type)){
                            sponsorsindv.add(sponsors.get(m));
                        };
                    }
                    sa = new SponsorsAdapter(sponsorsindv, SearchSponsors.this,empty);
                    RV.setAdapter(sa);
                    sponsorsnow = sa.getList();
                }
                if(type.equalsIgnoreCase("eCommerce Company")){
                    ArrayList<SponsorUser> sponsorsindv = new ArrayList<>();
                    for(int m=0;m< sponsors.size();m++){
                        if(sponsors.get(m).getField().equalsIgnoreCase(type)){
                            sponsorsindv.add(sponsors.get(m));
                        };
                    }
                    sa = new SponsorsAdapter(sponsorsindv, SearchSponsors.this,empty);
                    RV.setAdapter(sa);
                    sponsorsnow = sa.getList();
                }
                if(type.equalsIgnoreCase("Web Development Company")){
                    ArrayList<SponsorUser> sponsorsindv = new ArrayList<>();
                    for(int m=0;m< sponsors.size();m++){
                        if(sponsors.get(m).getField().equalsIgnoreCase(type)){
                            sponsorsindv.add(sponsors.get(m));
                        };
                    }
                    sa = new SponsorsAdapter(sponsorsindv, SearchSponsors.this,empty);
                    RV.setAdapter(sa);
                    sponsorsnow = sa.getList();
                }
                if(type.equalsIgnoreCase("App Development Company")){
                    ArrayList<SponsorUser> sponsorsindv = new ArrayList<>();
                    for(int m=0;m< sponsors.size();m++){
                        if(sponsors.get(m).getField().equalsIgnoreCase(type)){
                            sponsorsindv.add(sponsors.get(m));
                        };
                    }
                    sa = new SponsorsAdapter(sponsorsindv, SearchSponsors.this,empty);
                    RV.setAdapter(sa);
                    sponsorsnow = sa.getList();
                }
                if(type.equalsIgnoreCase("All")){
                    sa = new SponsorsAdapter(sponsors, SearchSponsors.this,empty);
                    RV.setAdapter(sa);
                    sponsorsnow = sa.getList();
                }
                if(type.equalsIgnoreCase("Your Previous Sponsors")){
                    ArrayList<SponsorUser> sponsorsindv = new ArrayList<>();


                    fs.collection("shared_projects")
                            .whereEqualTo("creatorID", userID).whereEqualTo("stage1","accepted")
                            .get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful()) {
                                        for (QueryDocumentSnapshot document : task.getResult()) {
                                            SharedProject d = document.toObject(SharedProject.class);
                                            /*fs.collection("users").whereEqualTo("id",d.getSharedWith()).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                                @Override
                                                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                                    if (task.isSuccessful()){
                                                        System.out.println("another task successful");
                                                        for (QueryDocumentSnapshot document : task.getResult()){
                                                            System.out.println("got the sponsor");
                                                            SponsorUser s = document.toObject(SponsorUser.class);
                                                            boolean thereissomeone = false;
                                                            for (SponsorUser l : sponsorsindv){
                                                                if (s.getId().equalsIgnoreCase(l.getId())){
                                                                    thereissomeone = true;
                                                                    System.out.println("there is someone");}}
                                                            if (thereissomeone==false){
                                                                sponsorsindv.add(s);
                                                                System.out.println("it is added");
                                                            }
                                                        }

                                                    }


                                                }});*/
                                            String neededid = d.getSharedWith();
                                            SponsorUser neededsponsor;
                                            boolean thereissomeone = false;
                                            for (SponsorUser u: sponsors){
                                                if(u.getId().equalsIgnoreCase(neededid)){
                                                    for (SponsorUser l : sponsorsindv){
                                                        if (u.getId().equalsIgnoreCase(l.getId())){
                                                            thereissomeone = true; }}
                                                    if (thereissomeone==false){
                                                        sponsorsindv.add(u);
                                                    }
                                                }
                                            }

                                        }


                                    }

                                    if(sponsorsindv.isEmpty()){
                                        empty.setVisibility(View.VISIBLE);
                                        sa = new SponsorsAdapter(sponsorsindv, SearchSponsors.this,empty);
                                        RV.setAdapter(sa);
                                        sponsorsnow = sa.getList();
                                    }
                                    else{
                                        sa = new SponsorsAdapter(sponsorsindv, SearchSponsors.this,empty);
                                        RV.setAdapter(sa);
                                        sponsorsnow = sa.getList();}



                                }


                            });




                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {


            }
        });



        ArrayList<SharedProject> list = new ArrayList<>();
        notiopen = true;
        notired.setVisibility(View.GONE);
        fs.collection("shared_projects")
                .whereEqualTo("creatorID", userID)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                SharedProject d = document.toObject(SharedProject.class);
                                if (d.getStage1().equalsIgnoreCase("accepted")||d.getStage1().equalsIgnoreCase("accept_with_revision")||
                                        d.getStage1().equalsIgnoreCase("rejected")){
                                    list.add(d);
                                }
                                if (d.getStage2().equalsIgnoreCase("accepted")||d.getStage2().equalsIgnoreCase("accept_with_revision")){
                                    list.add(d);
                                }
                                if (d.getStage3().equalsIgnoreCase("accepted")||d.getStage3().equalsIgnoreCase("accept_with_revision")){
                                    list.add(d);
                                }


                            }
                            if(list!=null) {
                                for (SharedProject m: list){
                                    if (m.isOpenedcreator()==false){
                                        notiopen = false;
                                    }
                                }
                                if (notiopen==false){
                                    notired.setVisibility(View.VISIBLE);
                                }
                                else {
                                    notired.setVisibility(View.GONE);
                                }
                            }
                        }
                    }
                });



        RV =findViewById(R.id.SearchSponsorsRV);
        RV.setHasFixedSize(true);
        RecyclerView.LayoutManager lm = new GridLayoutManager(this,4);
        RV.setLayoutManager(lm);
        empty.setVisibility(View.GONE);
        fs.collection("users").whereEqualTo("type","sponsor").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    if(!task.getResult().isEmpty()) {
                        empty.setVisibility(View.GONE);
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String id = document.getId();
                            fs.collection("users").document(id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                    SponsorUser s = documentSnapshot.toObject(SponsorUser.class);
                                    s.setId(id);
                                    sponsors.add(s);
                                    if (sponsors != null) {
                                        sa = new SponsorsAdapter(sponsors, SearchSponsors.this, empty);
                                        RV.setAdapter(sa);
                                        empty.setVisibility(View.GONE);
                                    } else {
                                        empty.setVisibility(View.VISIBLE);
                                    }
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {

                                }
                            });
                        }
                        progressBar.setVisibility(View.GONE);
                    }
                    else{
                        empty.setVisibility(View.VISIBLE);
                    }
                }
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userid = fAuth.getCurrentUser().getUid();
                fs.collection("tokens").document(userid).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            DocumentReference dref = fs.collection("tokens").document(userid);
                            dref.update("token", null);
                        }
                    }
                });
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(SearchSponsors.this, Login.class));
                finish();

            }
        });
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SearchSponsors.this, CreatorMainProjects.class));
            }
        });
        profilepic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SearchSponsors.this, IdeaOwnerProfilePage.class);
                startActivity(i);
            }
        });

        notifications.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SearchSponsors.this, CreatorNotifications.class));

            }
        });

        chipgroup.setOnCheckedChangeListener(new ChipGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(ChipGroup group, int checkedId) {

                if(Integer.compare(checkedId,chipgroup.getChildAt(0).getId())==0){
                    sponsorsAZ = new ArrayList<>(sponsorsnow);
                    Collections.sort(sponsorsAZ, new Comparator<SponsorUser>() {
                        @Override
                        public int compare(SponsorUser s1, SponsorUser s2) {
                            return s1.getUsername().compareTo(s2.getUsername());}

                        @Override
                        public boolean equals(Object o) {
                            return false;
                        }
                    });
                    sa = new SponsorsAdapter(sponsorsAZ, SearchSponsors.this,empty);
                    RV.setAdapter(sa);



                }

                if(Integer.compare(checkedId,chipgroup.getChildAt(1).getId())==0){
                    sponsorsZA = new ArrayList<>(sponsorsnow);
                    Collections.sort(sponsorsZA, new Comparator<SponsorUser>() {
                        @Override
                        public int compare(SponsorUser s1, SponsorUser s2) {
                            return s1.getUsername().compareTo(s2.getUsername());}

                        @Override
                        public boolean equals(Object o) {
                            return false;
                        }
                    });
                    Collections.reverse(sponsorsZA);
                    sa = new SponsorsAdapter(sponsorsZA, SearchSponsors.this,empty);
                    RV.setAdapter(sa);

                }
                if(checkedId == View.NO_ID) {
                    sa = new SponsorsAdapter(sponsorsnow, SearchSponsors.this,empty);
                    RV.setAdapter(sa);
                }
            }
        });




    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search,menu);
        MenuItem item = menu.findItem(R.id.actionSearch);
        SearchView searchview = (SearchView) item.getActionView();
        searchview.setQueryHint("Search for a sponsor");
        searchview.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                sa.getFilter().filter(newText);
                return false;
            }
        });
        return true;
    }

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