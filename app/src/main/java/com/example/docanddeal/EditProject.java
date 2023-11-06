package com.example.docanddeal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
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

import de.hdodenhof.circleimageview.CircleImageView;

public class EditProject extends AppCompatActivity implements View.OnClickListener{
    CircleImageView logo;
    EditText name, desc,link;
    Spinner acspintv;
    Button edit;
    static Uri imguri;
    FirebaseFirestore fs;
    DocumentReference dref,ds;
    StorageReference sr;
    FirebaseAuth fAuth;
    Switch makeprivate;
    SRProjects sp;
    String userID;
    ProjectC p;
    TextView des,privateTV;
    LinearLayout TeamList;
    RelativeLayout newp;
    ArrayList<String> teamMemer;
    RecyclerView teamrv;
    TeamMemberProjAdp teamAdp;
    ImageView plusTeam;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_project);
        logo = findViewById(R.id.editProjLogo);
        name = findViewById(R.id.editProjName1);
        desc = findViewById(R.id.editProjDesc1);
        edit = findViewById(R.id.editProjButton);
        link = findViewById(R.id.editProjLink1);
        teamrv = findViewById(R.id.teamRV);
        acspintv  = (Spinner) findViewById(R.id.editProjAC);
        makeprivate = findViewById(R.id.makepriv);
        des = findViewById(R.id.textView4);
        privateTV = findViewById(R.id.privateTV);
        plusTeam = findViewById(R.id.plusTeam);
        teamMemer = new ArrayList<>();
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
        fAuth = FirebaseAuth.getInstance();
        userID = fAuth.getCurrentUser().getUid();
        plusTeam.setOnClickListener(this);
        teamrv.setHasFixedSize(true);
        RecyclerView.LayoutManager lm = new LinearLayoutManager(this);
        teamrv.setLayoutManager(lm);
        TeamList = findViewById(R.id.teamlist);

        if(type.equals("sponsor")){
            sp = (SRProjects) b.getSerializable("sProject");
            String spId = sp.getSpid();
            ds= fs.collection("sponsored_projects").document(spId);
            name.setText(sp.getName());
            link.setText(sp.getLink());
            desc.setVisibility(View.GONE);
            des.setVisibility(View.GONE);
            privateTV.setVisibility(View.GONE);
            acspintv.setSelection(arrayAdapter.getPosition(sp.getType()));
            makeprivate.setChecked(Boolean.parseBoolean(sp.getIsPrivate()));
            Picasso.get().load(Uri.parse(sp.getLogo())).resize(50,50).centerCrop().into(logo);
        }
        if(type.equals("creator")){
            p = (ProjectC) b.getSerializable("Project");
            String pId = p.getPid();
            dref = fs.collection("projects").document(pId);
            name.setText(p.getName());
            desc.setText(p.getDescription());
            link.setText(p.getLink());
            teamMemer = p.getTeammebers();
            teamAdp = new TeamMemberProjAdp(teamMemer, EditProject.this, newp);
            teamrv.setAdapter(teamAdp);

            makeprivate.setChecked(Boolean.parseBoolean(p.getIsprivate()));
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
                teamMemer = new ArrayList<>();
                for (int i = 0; i < TeamList.getChildCount(); i++) {
                    View v1 = TeamList.getChildAt(i);
                    EditText team = (EditText) v1.findViewById(R.id.team);
                    String tea = team.getText().toString().trim();
                    teamMemer.add(tea);
                }
                for (int i = 0; i < teamrv.getChildCount(); i++) {
                    View v1 = teamrv.getChildAt(i);
                    EditText team = (EditText) v1.findViewById(R.id.team);
                    String tea = team.getText().toString().trim();
                    teamMemer.add(tea);
                }
                if(type.equals("creator")) {
                    String sdesc = desc.getText().toString().trim();
                    if (sname.matches("") || sdesc.matches("") ||scateg.matches("Category"))
                        Toast.makeText(EditProject.this, "Fill all the fields please", Toast.LENGTH_LONG).show();
                    else {
                        if(!sname.equals(p.getName())){
                            fs.collection("users").document(userID).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                    CreatorUser cu = documentSnapshot.toObject(CreatorUser.class);
                                    if(cu.getProjects()== null){
                                        dref.update("name",sname);
                                        goToCreatorMain(sdesc,slink,scateg,isprivate,teamMemer);
                                    }else{
                                        if(cu.getProjects().isEmpty()){
                                            dref.update("name",sname);
                                            goToCreatorMain(sdesc,slink,scateg,isprivate,teamMemer);
                                        }
                                        else{
                                            if(cu.getPnames().contains(sname))
                                                Toast.makeText(EditProject.this,"Project name already exists",Toast.LENGTH_LONG).show();
                                            else {
                                                dref.update("name", sname);
                                                goToCreatorMain(sdesc, slink, scateg, isprivate,teamMemer);
                                            }
                                        }
                                    }

                                }
                            });
                        }else{
                            goToCreatorMain(sdesc,slink,scateg,isprivate,teamMemer);
                        }

                        //if(getApplicationContext())

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
    private void goToCreatorMain(String sdesc,String slink,String scateg,String isprivate, ArrayList<String> teamMemer){
        if(!sdesc.equals(p.getDescription()))
            dref.update("description", sdesc);
        if(!slink.equals(p.getLink()))
            dref.update("link",slink);
        if(!scateg.equals(p.getType()))
            dref.update("type", scateg);
        if(!isprivate.equals(p.getIsprivate()))
            dref.update("isprivate", isprivate);
        if(!teamMemer.equals(p.getTeammebers()))
            dref.update("teammebers", teamMemer);

        startActivity(new Intent(EditProject.this, CreatorMainProjects.class));
        finish();
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

    private void addViewTeam() {
        View teamview = getLayoutInflater().inflate(R.layout.team_member_proj,null,false);
        EditText team = (EditText) teamview.findViewById(R.id.team);
        ImageView delExp = (ImageView) teamview.findViewById(R.id.deleteTeam);
        delExp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s1 = team.getText().toString();
                removeViewTeam(teamview,s1);
            }
        });
        TeamList.addView(teamview);
    }

    private void addViewTeam(String sp1) {
        View teamview = getLayoutInflater().inflate(R.layout.team_member_proj,null,false);
        EditText team = (EditText) teamview.findViewById(R.id.team);
        team.setText(sp1);
        ImageView delExp = (ImageView) teamview.findViewById(R.id.deleteexp);
        delExp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s1 = team.getText().toString();
                removeViewTeam(teamview,s1);
            }
        });
        TeamList.addView(teamview);
    }

    private void removeViewTeam(View view, String s1){
        TeamList.removeView(view);
        Snackbar.make(newp,"The item is deleted", Snackbar.LENGTH_LONG).setAction("UNDO", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (s1.isEmpty()){
                    addViewTeam();
                }
                else {
                    addViewTeam(s1);
                }            }
        }).show();

    }

    @Override
    public void onClick(View v) {
        addViewTeam();
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