package com.example.docanddeal;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSpinner;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.BackgroundColorSpan;
import android.text.style.CharacterStyle;
import android.text.style.ForegroundColorSpan;
import android.util.DisplayMetrics;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.security.auth.callback.Callback;

public class proposalDoc extends AppCompatActivity implements View.OnClickListener, Callback {


    ImageView done,plusmember,plusneeds,plusoutputs,plusfeatures,plusAPI,plusAlg,plusindi,plusSoc, plusOrg, plusRef,hObjectives,aObjectives, hNeeds,aNeeds, hOutput, aOutput, hFeatures, aFeatures, hInd, aInd, hSoc, aSocc, hOrg, aOrgg, hAPI, aAPII, hALGO, aALGO;
    TextView tObjectives, tNeeds, tOutput,tFeatures, tInd, tSoc, tOrg, tAPI, tALGO;
    LinearLayout teamlist,needsList,outputList,featuresList,APIList,AlgList,IndiList,socList,orgList, refList;
    FirebaseAuth fAuth;
    FirebaseFirestore fs;
    EditText objectives,Otherser,Othersor,name;
    String userID;
    ProposalDocument pd;
    TeamMember tm;
    Document d;
    API api ;
    Algorithm alg;
    CheckBox Un1,Un2,Un3,Un4,Un5,Un6,Un7,Un8,Un9,Un10,Un11,Un12,Un13,Un14,Un15,Un16,Un17;
    CheckBox Sor1,Sor2,Sor3;
    CheckBox Ser1,Ser2,Ser3;
    ArrayList<String>  aneeds, aoutputs, afeatures,  aindi, aSoc,  aOrg, aRef;
    ArrayList<TeamMember> ateam;
    ArrayList<Algorithm> aAlg;
    ArrayList<API> aAPI;
    ArrayList<Abbrev> aexplination;
    Dialog SaveDialog;
    Bundle b;
    Version p1;
    ProjectC pro;
    String pid;
    String ver;
    Context context;
    Button next;
    ArrayList<String> test = new ArrayList<>();
    RelativeLayout prod;
    boolean clicked;


    public void callback(EditText text) {
        this.objectives = text;

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_proposal_doc);
        prod = findViewById(R.id.PROD);
        next = findViewById(R.id.nextToAbbrev);
        Un1= findViewById(R.id.un1);
        Un2= findViewById(R.id.un2);
        Un3= findViewById(R.id.un3);
        Un4= findViewById(R.id.un4);
        Un5= findViewById(R.id.un5);
        Un6= findViewById(R.id.un6);
        Un7= findViewById(R.id.un7);
        Un8= findViewById(R.id.un8);
        Un9= findViewById(R.id.un9);
        Un10= findViewById(R.id.un10);
        Un11= findViewById(R.id.un11);
        Un12= findViewById(R.id.un12);
        Un13= findViewById(R.id.un13);
        Un14= findViewById(R.id.un14);
        Un15= findViewById(R.id.un15);
        Un16= findViewById(R.id.un16);
        Un17= findViewById(R.id.un17);
        Ser1= findViewById(R.id.ser1);
        Ser2= findViewById(R.id.ser2);
        Ser3= findViewById(R.id.ser3);
        Otherser= findViewById(R.id.otherser);
        Sor1= findViewById(R.id.sor1);
        Sor2= findViewById(R.id.sor2);
        Sor3= findViewById(R.id.sor3);
        Othersor= findViewById(R.id.othersor);
        done =  findViewById(R.id.proposalFinish);
        objectives = findViewById(R.id.projectObjectives);

        plusneeds = findViewById(R.id.plusneeds);
        needsList = findViewById(R.id.needsList);
        plusoutputs = findViewById(R.id.plusoutputs);
        outputList = findViewById(R.id.outputList);
        plusfeatures = findViewById(R.id.plusfeatures);
        featuresList = findViewById(R.id.featuresList);
        plusAPI = findViewById(R.id.plusAPI);
        APIList = findViewById(R.id.APIList);
        plusAlg = findViewById(R.id.plusAlg);
        AlgList = findViewById(R.id.AlgList);
        plusmember = findViewById(R.id.plusmember);
        teamlist = findViewById(R.id.teamlist);
        plusindi = findViewById(R.id.plusindi);
        IndiList = findViewById(R.id.IndiList);
        plusSoc = findViewById(R.id.plusSoc);
        socList = findViewById(R.id.socList);
        plusOrg = findViewById(R.id.plusOrg);
        orgList = findViewById(R.id.orgList);
        plusRef = findViewById(R.id.plusRef);
        refList = findViewById(R.id.refList);
        name = findViewById(R.id.docNameProp);
        hObjectives=findViewById(R.id.helpObjectives);
        tObjectives=findViewById(R.id.textObjectives);
        aObjectives=findViewById(R.id.arrowObjectives);
        hNeeds=findViewById(R.id.helpNeeds);
        tNeeds=findViewById(R.id.textNeeds);
        aNeeds=findViewById(R.id.arrowNeeds);
        hOutput=findViewById(R.id.helpOutput);
        tOutput=findViewById(R.id.textOutput);
        aOutput= findViewById(R.id.arrowOutput);
        hFeatures= findViewById(R.id.helpFeatures);
        aFeatures=findViewById(R.id.arrowFeatures);
        tFeatures=findViewById(R.id.textFeatures);
        hInd= findViewById(R.id.helpInd);
        aInd= findViewById(R.id.arrowInd);
        tInd= findViewById(R.id.textInd);
        hSoc= findViewById(R.id.helpSoc);
        tSoc=findViewById(R.id.textSoc);
        aSocc=findViewById(R.id.arrowSoc);
        hOrg= findViewById(R.id.helpOrg);
        tOrg=findViewById(R.id.textOrg);
        aOrgg=findViewById(R.id.arrowOrg);
        hAPI=findViewById(R.id.helpAPI);
        aAPII=findViewById(R.id.arrowAPI);
        tAPI=findViewById(R.id.textAPI);
        hALGO=findViewById(R.id.helpAlgo);
        aALGO=findViewById(R.id.arrowAlgo);
        tALGO=findViewById(R.id.textAlgo);

        clicked=false;
        tObjectives.setVisibility(View.GONE);
        aObjectives.setVisibility(View.GONE);
        tNeeds.setVisibility(View.GONE);
        aNeeds.setVisibility(View.GONE);
        tOutput.setVisibility(View.GONE);
        aOutput.setVisibility(View.GONE);
        tFeatures.setVisibility(View.GONE);
        aFeatures.setVisibility(View.GONE);
        tInd.setVisibility(View.GONE);
        aInd.setVisibility(View.GONE);
        tSoc.setVisibility(View.GONE);
        aSocc.setVisibility(View.GONE);
        tOrg.setVisibility(View.GONE);
        aOrgg.setVisibility(View.GONE);
        tAPI.setVisibility(View.GONE);
        aAPII.setVisibility(View.GONE);
        tALGO.setVisibility(View.GONE);
        aALGO.setVisibility(View.GONE);


        Bundle b = getIntent().getExtras();

        p1 = (Version) b.getSerializable("Version");
        pro = (ProjectC) b.getSerializable("Project");
        pid = pro.getPid();
        ver = p1.getVersion();

        ateam = new ArrayList<>();
        aexplination = new ArrayList<>();
        aneeds = new ArrayList<>();
        aoutputs = new ArrayList<>();
        afeatures = new ArrayList<>();
        aAPI = new ArrayList<>();
        aAlg= new ArrayList<>();
        aindi= new ArrayList<>();
        aSoc= new ArrayList<>();
        aOrg= new ArrayList<>();
        aRef= new ArrayList<>();

        objectives.setCustomSelectionActionModeCallback(new AbrCallback(objectives));

        //objectives.setCus

        next. setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SaveProposolDoc();
                Intent i = new Intent(proposalDoc.this, Proposal2.class);
                i.putExtra("prop",pd);
                i.putExtra("Version",p1);
                i.putExtra("exp",aexplination);
                i.putExtra("Project",pro);
                //i.putExtra("pid",pid);
                proposalDoc.this.startActivity(i);
            }
        });
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(proposalDoc.this)
                        .setTitle("Save")
                        .setMessage("Do you want to save the Document?")
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent i = new Intent(proposalDoc.this, ProjectDetails.class);
                                i.putExtra("Version",p1);
                                i.putExtra("Project",pro);
                                //i.putExtra("pid",pid);
                                startActivity(i);
                                finish();
                            }
                        })
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface arg0, int arg1) {
                                SaveProposolDoc();
                                fAuth =FirebaseAuth.getInstance();
                                fs =  FirebaseFirestore.getInstance();
                                userID = fAuth.getCurrentUser().getUid();
                                /*private void checkName(String name,Uri uri){
                                        dref = fs.collection("users").document(userID);
                                        dref.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                            @Override
                                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                if(documentSnapshot.get("projects")!=null) {
                                                    ArrayList<String> arr = ((ArrayList<String>) documentSnapshot.get("pnames"));
                                                    if(arr.contains(name)){

                                                    }
                                                    else{
                                                        sendToDB(uri);
                                                    }
                                                }

                                            }
                                        });

                                 */
                                fs.collection("documents").whereEqualTo("pid",pid).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                    @Override
                                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                        if(queryDocumentSnapshots.size()==0){
                                            fs.collection("proposalDoc").add(pd).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                @Override
                                                public void onSuccess(DocumentReference documentReference) {
                                                    // public Document(String uid, String type, String name, String docid, String pid, String verid)
                                                    Document d = new Document(userID, "proposal", name.getText().toString(), documentReference.getId(), pid, ver,null);
                                                    fs.collection("documents").document(documentReference.getId()).set(d);
                                                    Intent i = new Intent(proposalDoc.this, ProjectDetails.class);
                                                    i.putExtra("Version", p1);
                                                    i.putExtra("Project", pro);
                                                    // i.putExtra("pid",pid);
                                                    startActivity(i);
                                                    finish();
                                                }
                                            });
                                        }else{
                                            for (DocumentSnapshot ds: queryDocumentSnapshots) {
                                                Document d = ds.toObject(Document.class);
                                                if(d.getName().equals(name.getText().toString()) && d.getVerid().equals(p1.getVersion()))
                                                    Toast.makeText(proposalDoc.this,"Document name already exists",Toast.LENGTH_LONG).show();
                                                else{
                                                    fs.collection("proposalDoc").add(pd).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                        @Override
                                                        public void onSuccess(DocumentReference documentReference) {
                                                            // public Document(String uid, String type, String name, String docid, String pid, String verid)
                                                            Document d = new Document(userID,"proposal",name.getText().toString(),documentReference.getId(),pid,ver,null);
                                                            fs.collection("documents").document(documentReference.getId()).set(d);
                                                            Intent i = new Intent(proposalDoc.this, ProjectDetails.class);
                                                            i.putExtra("Version",p1);
                                                            i.putExtra("Project",pro);
                                                            // i.putExtra("pid",pid);
                                                            startActivity(i);
                                                            finish();
                                                        }
                                                    });
                                                    break;
                                                }

                                            }
                                        }



                                    }
                                });

                            }
                        }).create().show();
            }
        });
        hObjectives.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (clicked==false){
                    tObjectives.setVisibility(View.GONE);
                    aObjectives.setVisibility(View.GONE);
                    clicked = true;
                }
                else {
                    tObjectives.setVisibility(View.VISIBLE);
                    aObjectives.setVisibility(View.VISIBLE);
                    clicked = false;
                }
            }
        });
        hNeeds.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (clicked==false){
                    tNeeds.setVisibility(View.VISIBLE);
                    aNeeds.setVisibility(View.VISIBLE);
                    clicked = true;
                }
                else {
                    tNeeds.setVisibility(View.GONE);
                    aNeeds.setVisibility(View.GONE);
                    clicked = false;
                }
            }
        });
        hOutput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (clicked==false){
                    tOutput.setVisibility(View.GONE);
                    aOutput.setVisibility(View.GONE);
                    clicked = true;
                }
                else {
                    tOutput.setVisibility(View.VISIBLE);
                    aOutput.setVisibility(View.VISIBLE);
                    clicked = false;
                }
            }
        });
        hFeatures.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (clicked==false){
                    tFeatures.setVisibility(View.GONE);
                    aFeatures.setVisibility(View.GONE);
                    clicked = true;
                }
                else {
                    tFeatures.setVisibility(View.VISIBLE);
                    aFeatures.setVisibility(View.VISIBLE);
                    clicked = false;
                }
            }
        });
        hInd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (clicked==false){
                    tInd.setVisibility(View.GONE);
                    aInd.setVisibility(View.GONE);
                    clicked = true;
                }
                else {
                    tInd.setVisibility(View.VISIBLE);
                    aInd.setVisibility(View.VISIBLE);
                    clicked = false;
                }
            }
        });
        hSoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (clicked==false){
                    tSoc.setVisibility(View.GONE);
                    aSocc.setVisibility(View.GONE);
                    clicked = true;
                }
                else {
                    tSoc.setVisibility(View.VISIBLE);
                    aSocc.setVisibility(View.VISIBLE);
                    clicked = false;
                }
            }
        });
        hOrg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (clicked==false){
                    tOrg.setVisibility(View.GONE);
                    aOrgg.setVisibility(View.GONE);
                    clicked = true;
                }
                else {
                    tOrg.setVisibility(View.VISIBLE);
                    aOrgg.setVisibility(View.VISIBLE);
                    clicked = false;
                }
            }
        });
        hAPI.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (clicked==false){
                    tAPI.setVisibility(View.GONE);
                    aAPII.setVisibility(View.GONE);
                    clicked = true;
                }
                else {
                    tAPI.setVisibility(View.VISIBLE);
                    aAPII.setVisibility(View.VISIBLE);
                    clicked = false;
                }
            }
        });
        hALGO.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (clicked==false){
                    tALGO.setVisibility(View.GONE);
                    aALGO.setVisibility(View.GONE);
                    clicked = true;
                }
                else {
                    tALGO.setVisibility(View.VISIBLE);
                    aALGO.setVisibility(View.VISIBLE);
                    clicked = false;
                }
            }
        });

        plusmember.setOnClickListener(this);
        plusAPI.setOnClickListener(this);
        plusAlg.setOnClickListener(this);
        plusfeatures.setOnClickListener(this);
        plusneeds.setOnClickListener(this);
        plusoutputs.setOnClickListener(this);
        plusindi.setOnClickListener(this);
        plusOrg.setOnClickListener(this);
        plusSoc.setOnClickListener(this);
        plusRef.setOnClickListener(this);

    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.plusmember:
                addView();
                break;
            case R.id.plusAPI:
                addViewApi();
                break;
            case R.id.plusAlg:
                addViewAlg();
                break;
            case R.id.plusneeds:
                addViewNeed();
                break;
            case R.id.plusoutputs:
                addViewOutput();
                break;
            case R.id.plusfeatures:
                addViewFeature();
                break;
            case R.id.plusindi:
                addViewIndi();
                break;
            case R.id.plusSoc:
                addViewSoc();
                break;
            case R.id.plusOrg:
                addViewOrg();
                break;
            case R.id.plusRef:
                addViewRef();
                break;
            default:
                break;
        }
    }
    private void addView() {
        View tmview = getLayoutInflater().inflate(R.layout.team_member,null,false);
        EditText tmName = (EditText)  tmview.findViewById(R.id.teamMemberName);
        CheckBox TC1 = (CheckBox) tmview.findViewById(R.id.tc1);
        CheckBox TC2 = (CheckBox) tmview.findViewById(R.id.tc2);
        CheckBox TC3 = (CheckBox) tmview.findViewById(R.id.tc3);
        CheckBox TC4 = (CheckBox) tmview.findViewById(R.id.tc4);
        CheckBox TC5 = (CheckBox) tmview.findViewById(R.id.tc5);
        ImageView delMember = (ImageView) tmview.findViewById(R.id.deleteMember);
        delMember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s1 = tmName.getText().toString();
                removeView(tmview,s1);
            }
        });
        teamlist.addView(tmview);
    }
    private void addView(String sp) {
        View tmview = getLayoutInflater().inflate(R.layout.team_member,null,false);
        EditText tmName = (EditText)  tmview.findViewById(R.id.teamMemberName);
        tmName.setText(sp);
        CheckBox TC1 = (CheckBox) tmview.findViewById(R.id.tc1);
        CheckBox TC2 = (CheckBox) tmview.findViewById(R.id.tc2);
        CheckBox TC3 = (CheckBox) tmview.findViewById(R.id.tc3);
        CheckBox TC4 = (CheckBox) tmview.findViewById(R.id.tc4);
        CheckBox TC5 = (CheckBox) tmview.findViewById(R.id.tc5);
        ImageView delMember = (ImageView) tmview.findViewById(R.id.deleteMember);
        delMember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s1 = tmName.getText().toString();
                removeView(tmview,s1);
            }
        });
        teamlist.addView(tmview);
    }

    private void addViewApi() {
        View apiview = getLayoutInflater().inflate(R.layout.api_proposal,null,false);
        EditText apiName = (EditText) apiview.findViewById(R.id.APIName);
        EditText apiLink = (EditText) apiview.findViewById(R.id.apiLink);
        ImageView delApi = (ImageView) apiview.findViewById(R.id.deleteapi);
        delApi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s1 = apiName.getText().toString();
                String s2 = apiLink.getText().toString();
                removeViewAPI(apiview,s1,s2);
            }
        });
        APIList.addView(apiview);
    }
    private void addViewApi(String sp1, String sp2) {
        View apiview = getLayoutInflater().inflate(R.layout.api_proposal,null,false);
        EditText apiName = (EditText) apiview.findViewById(R.id.APIName);
        apiName.setText(sp1);
        EditText apiLink = (EditText) apiview.findViewById(R.id.apiLink);
        apiLink.setText(sp2);
        ImageView delApi = (ImageView) apiview.findViewById(R.id.deleteapi);
        delApi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s1 = apiName.getText().toString();
                String s2 = apiLink.getText().toString();
                removeViewAPI(apiview,s1,s2);
            }
        });
        APIList.addView(apiview);
    }
    private void addViewAlg() {
        View algview = getLayoutInflater().inflate(R.layout.alg_proposal,null,false);
        EditText ALGName = (EditText) algview.findViewById(R.id.algName);
        EditText algTech = (EditText) algview.findViewById(R.id.algTech);
        ImageView delAlg = (ImageView) algview.findViewById(R.id.deletealg);
        delAlg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s1 = ALGName.getText().toString();
                String s2 = algTech.getText().toString();
                removeViewAlg(algview,s1,s2);
            }
        });
        algTech.setCustomSelectionActionModeCallback(new AbrCallback(algTech));
        AlgList.addView(algview);
    }
    private void addViewAlg(String sp1, String sp2) {
        View algview = getLayoutInflater().inflate(R.layout.alg_proposal,null,false);
        EditText ALGName = (EditText) algview.findViewById(R.id.algName);
        ALGName.setText(sp1);
        EditText algTech = (EditText) algview.findViewById(R.id.algTech);
        algTech.setText(sp2);
        ImageView delAlg = (ImageView) algview.findViewById(R.id.deletealg);
        delAlg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s1 = ALGName.getText().toString();
                String s2 = algTech.getText().toString();
                removeViewAlg(algview,s1,s2);
            }
        });
        algTech.setCustomSelectionActionModeCallback(new AbrCallback(algTech));
        AlgList.addView(algview);
    }

    private void addViewNeed() {
        View needview = getLayoutInflater().inflate(R.layout.needs_proposal,null,false);
        EditText needs = (EditText) needview.findViewById(R.id.needed);
        ImageView delNeed = (ImageView) needview.findViewById(R.id.deleteneed);
        delNeed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s1 = needs.getText().toString();
                removeViewNeed(needview,s1);
            }
        });
        needs.setCustomSelectionActionModeCallback(new AbrCallback(needs));
        needsList.addView(needview);
    }
    private void addViewNeed(String s) {
        View needview = getLayoutInflater().inflate(R.layout.needs_proposal,null,false);
        EditText needs = (EditText) needview.findViewById(R.id.need);
        needs.setText(s);
        ImageView delNeed = (ImageView) needview.findViewById(R.id.deleteneed);
        delNeed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s1 = needs.getText().toString();
                removeViewNeed(needview,s1);
            }
        });
        needs.setCustomSelectionActionModeCallback(new AbrCallback(needs));
        needsList.addView(needview);
    }

    private void addViewOutput() {
        View outview = getLayoutInflater().inflate(R.layout.output_proposal,null,false);
        EditText out = (EditText) outview.findViewById(R.id.out);
        ImageView delOut = (ImageView) outview.findViewById(R.id.deleteoutput);
        delOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s1 = out.getText().toString();
                removeViewOutput(outview,s1);
            }
        });
        out.setCustomSelectionActionModeCallback(new AbrCallback(out));
        outputList.addView(outview);
    }
    private void addViewOutput(String s) {
        View outview = getLayoutInflater().inflate(R.layout.output_proposal,null,false);
        EditText out = (EditText) outview.findViewById(R.id.out);
        out.setText(s);
        ImageView delOut = (ImageView) outview.findViewById(R.id.deleteoutput);
        delOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s1 = out.getText().toString();
                removeViewOutput(outview,s1);
            }
        });
        out.setCustomSelectionActionModeCallback(new AbrCallback(out));
        outputList.addView(outview);
    }
    private void addViewFeature() {
        View fetview = getLayoutInflater().inflate(R.layout.feat_proposal,null,false);
        EditText fet = (EditText) fetview.findViewById(R.id.feture);
        ImageView delFet = (ImageView) fetview.findViewById(R.id.deletefeture);
        delFet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s1 = fet.getText().toString();
                removeViewFeature(fetview,s1);
            }
        });
        fet.setCustomSelectionActionModeCallback(new AbrCallback(fet));
        featuresList.addView(fetview);
    }
    private void addViewFeature(String s) {
        View fetview = getLayoutInflater().inflate(R.layout.feat_proposal,null,false);
        EditText fet = (EditText) fetview.findViewById(R.id.feture);
        fet.setText(s);
        ImageView delFet = (ImageView) fetview.findViewById(R.id.deletefeture);
        delFet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s1 = fet.getText().toString();
                removeViewFeature(fetview,s1);
            }
        });
        fet.setCustomSelectionActionModeCallback(new AbrCallback(fet));
        featuresList.addView(fetview);
    }

    private void addViewIndi() {
        View indiview = getLayoutInflater().inflate(R.layout.indi_proposal,null,false);
        EditText indi = (EditText) indiview.findViewById(R.id.indi);
        ImageView delIndi = (ImageView) indiview.findViewById(R.id.deleteIndi);
        delIndi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s1 = indi.getText().toString();
                removeViewIndi(indiview,s1);
            }
        });
        indi.setCustomSelectionActionModeCallback(new AbrCallback(indi));
        IndiList.addView(indiview);
    }
    private void addViewIndi(String s) {
        View indiview = getLayoutInflater().inflate(R.layout.indi_proposal,null,false);
        EditText indi = (EditText) indiview.findViewById(R.id.indi);
        indi.setText(s);
        ImageView delIndi = (ImageView) indiview.findViewById(R.id.deleteIndi);
        delIndi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s1 = indi.getText().toString();
                removeViewIndi(indiview,s1);
            }
        });
        indi.setCustomSelectionActionModeCallback(new AbrCallback(indi));
        IndiList.addView(indiview);
    }

    private void addViewSoc() {
        View socview = getLayoutInflater().inflate(R.layout.soc_proposal,null,false);
        EditText soc = (EditText) socview.findViewById(R.id.soc);
        ImageView delSoc = (ImageView) socview.findViewById(R.id.deletesoc);
        delSoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s1 = soc.getText().toString();
                removeViewSoc(socview,s1);
            }
        });
        soc.setCustomSelectionActionModeCallback(new AbrCallback(soc));
        socList.addView(socview);
    }
    private void addViewSoc(String sp1) {
        View socview = getLayoutInflater().inflate(R.layout.soc_proposal,null,false);
        EditText soc = (EditText) socview.findViewById(R.id.soc);
        soc.setText(sp1);
        ImageView delSoc = (ImageView) socview.findViewById(R.id.deletesoc);
        delSoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s1 = soc.getText().toString();
                removeViewSoc(socview,s1);
            }
        });
        soc.setCustomSelectionActionModeCallback(new AbrCallback(soc));
        socList.addView(socview);
    }
    private void addViewOrg() {
        View orgview = getLayoutInflater().inflate(R.layout.org_proposal,null,false);
        EditText org = (EditText) orgview.findViewById(R.id.org);
        ImageView delOrg = (ImageView) orgview.findViewById(R.id.deleteorg);
        delOrg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s1 = org.getText().toString();
                removeViewOrg(orgview,s1);
            }
        });
        org.setCustomSelectionActionModeCallback(new AbrCallback(org));
        orgList.addView(orgview);
    }
    private void addViewOrg(String sp1) {
        View orgview = getLayoutInflater().inflate(R.layout.org_proposal,null,false);
        EditText org = (EditText) orgview.findViewById(R.id.org);
        org.setText(sp1);
        ImageView delOrg = (ImageView) orgview.findViewById(R.id.deleteorg);
        delOrg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s1 = org.getText().toString();
                removeViewOrg(orgview,s1);
            }
        });
        org.setCustomSelectionActionModeCallback(new AbrCallback(org));
        orgList.addView(orgview);
    }
    private void addViewRef() {
        View refview = getLayoutInflater().inflate(R.layout.refrences_proposal,null,false);
        EditText ref = (EditText) refview.findViewById(R.id.ref);
        ImageView delRef = (ImageView) refview.findViewById(R.id.deleteref);
        delRef.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s1 = ref.getText().toString();
                removeViewRef(refview,s1);
            }
        });
        ref.setCustomSelectionActionModeCallback(new AbrCallback(ref));
        refList.addView(refview);
    }
    private void addViewRef(String sp1) {
        View refview = getLayoutInflater().inflate(R.layout.refrences_proposal,null,false);
        EditText ref = (EditText) refview.findViewById(R.id.ref);
        ref.setText(sp1);
        ImageView delRef = (ImageView) refview.findViewById(R.id.deleteref);
        delRef.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s1 = ref.getText().toString();
                removeViewRef(refview,s1);
            }
        });
        ref.setCustomSelectionActionModeCallback(new AbrCallback(ref));
        refList.addView(refview);
    }


    private void removeView(View view,String s1){
        teamlist.removeView(view);
        Snackbar.make(prod,"The item is deleted", Snackbar.LENGTH_LONG).setAction("UNDO", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(s1.isEmpty()){
                    addView();
                }
                else {
                    addView(s1);
                }            }
        }).show();
    }
    private void removeViewAPI(View view,String s1,String s2){
        APIList.removeView(view);
        Snackbar.make(prod,"The item is deleted", Snackbar.LENGTH_LONG).setAction("UNDO", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(s1.isEmpty()&&s2.isEmpty()){
                    addViewApi();
                }
                else {
                    addViewApi(s1, s2);
                }            }
        }).show();
    }
    private void removeViewAlg(View view,String s1,String s2){
        AlgList.removeView(view);
        Snackbar.make(prod,"The item is deleted", Snackbar.LENGTH_LONG).setAction("UNDO", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(s1.isEmpty()&&s2.isEmpty()){
                    addViewAlg();
                }
                else {
                    addViewAlg(s1, s2);
                }            }
        }).show();
    }
    private void removeViewNeed(View view,String s1){
        needsList.removeView(view);
        Snackbar.make(prod,"The item is deleted", Snackbar.LENGTH_LONG).setAction("UNDO", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(s1.isEmpty()){
                    addViewNeed();
                }
                else {
                    addViewNeed(s1);
                }            }
        }).show();
    }
    private void removeViewOutput(View view,String s1){
        outputList.removeView(view);
        Snackbar.make(prod,"The item is deleted", Snackbar.LENGTH_LONG).setAction("UNDO", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(s1.isEmpty()){
                    addViewOutput();
                }
                else {
                    addViewOutput(s1);
                }            }
        }).show();
    }
    private void removeViewIndi(View view,String s1){
        IndiList.removeView(view);
        Snackbar.make(prod,"The item is deleted", Snackbar.LENGTH_LONG).setAction("UNDO", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(s1.isEmpty()){
                    addViewIndi();
                }
                else {
                    addViewIndi(s1);
                }            }
        }).show();
    }
    private void removeViewSoc(View view,String s1){
        socList.removeView(view);
        Snackbar.make(prod,"The item is deleted", Snackbar.LENGTH_LONG).setAction("UNDO", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(s1.isEmpty()){
                    addViewSoc();
                }
                else {
                    addViewSoc(s1);
                }            }
        }).show();
    }
    private void removeViewOrg(View view,String s1){
        orgList.removeView(view);
        Snackbar.make(prod,"The item is deleted", Snackbar.LENGTH_LONG).setAction("UNDO", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(s1.isEmpty()){
                    addViewOrg();
                }
                else {
                    addViewOrg(s1);
                }            }
        }).show();
    }
    private void removeViewRef(View view,String s1){
        refList.removeView(view);
        Snackbar.make(prod,"The item is deleted", Snackbar.LENGTH_LONG).setAction("UNDO", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(s1.isEmpty()){
                    addViewRef();
                }
                else {
                    addViewRef(s1);
                }            }
        }).show();
    }
    private void removeViewFeature(View view,String s1){
        featuresList.removeView(view);
        Snackbar.make(prod,"The item is deleted", Snackbar.LENGTH_LONG).setAction("UNDO", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(s1.isEmpty()){
                    addViewFeature();
                }
                else {
                    addViewFeature(s1);
                }            }
        }).show();
    }


    public void SaveProposolDoc (){
        fAuth =FirebaseAuth.getInstance();
        fs =  FirebaseFirestore.getInstance();
        userID = fAuth.getCurrentUser().getUid();
        //DocumentReference dr = fs.collection("proposalDoc");
        String sobjective = objectives.getText().toString().trim();
        String sname = name.getText().toString().trim();
        ArrayList<String> UNs = new ArrayList<>();
        if(Un1.isChecked()){
            UNs.add(Un1.getText().toString());
        }
        if(Un2.isChecked()){
            UNs.add(Un2.getText().toString());
        }
        if(Un3.isChecked()){
            UNs.add(Un3.getText().toString());
        }
        if(Un4.isChecked()){
            UNs.add((String) Un4.getText().toString());
        }
        if(Un5.isChecked()){
            UNs.add(Un5.getText().toString());
        }
        if(Un6.isChecked()){
            UNs.add(Un6.getText().toString());
        }
        if(Un7.isChecked()){
            UNs.add(Un7.getText().toString());
        }
        if(Un8.isChecked()){
            UNs.add(Un8.getText().toString());
        }
        if(Un9.isChecked()){
            UNs.add((String) Un9.getText().toString());
        }
        if(Un10.isChecked()){
            UNs.add(Un10.getText().toString());
        }
        if(Un11.isChecked()){
            UNs.add(Un11.getText().toString());
        }
        if(Un12.isChecked()){
            UNs.add(Un12.getText().toString());
        }
        if(Un13.isChecked()){
            UNs.add(Un13.getText().toString());
        }
        if(Un14.isChecked()){
            UNs.add((String) Un14.getText().toString());
        }
        if(Un15.isChecked()){
            UNs.add(Un15.getText().toString());
        }
        if(Un16.isChecked()){
            UNs.add(Un16.getText().toString());
        }
        if(Un17.isChecked()){
            UNs.add(Un17.getText().toString());
        }
        for (int i = 0; i < teamlist.getChildCount(); i++) {
            View v1 = teamlist.getChildAt(i);
            EditText tmName = (EditText) v1.findViewById(R.id.teamMemberName);
            CheckBox TC1 = (CheckBox) v1.findViewById(R.id.tc1);
            CheckBox TC2 = (CheckBox) v1.findViewById(R.id.tc2);
            CheckBox TC3 = (CheckBox) v1.findViewById(R.id.tc3);
            CheckBox TC4 = (CheckBox) v1.findViewById(R.id.tc4);
            CheckBox TC5 = (CheckBox) v1.findViewById(R.id.tc5);
            ArrayList<String> temCap = new ArrayList<>();
            if(TC1.isChecked()){
                temCap.add(TC1.getText().toString());
            }
            if(TC2.isChecked()){
                temCap.add(TC2.getText().toString());
            }
            if(TC3.isChecked()){
                temCap.add(TC3.getText().toString());
            }
            if(TC4.isChecked()){
                temCap.add((String) TC4.getText().toString());
            }
            if(TC5.isChecked()){
                temCap.add(TC5.getText().toString());
            }

            String stmname = tmName.getText().toString().trim();
            tm = new TeamMember( stmname, temCap);
            ateam.add(tm);
            //fs.collection("teamMember").add(tm);
        }
        for (int i = 0; i < APIList.getChildCount(); i++) {
            View v1 = APIList.getChildAt(i);
            EditText apiName = (EditText) v1.findViewById(R.id.APIName);
            String apiname = apiName.getText().toString().trim();
            EditText apiLink = (EditText) v1.findViewById(R.id.apiLink);
            String apilink = apiLink.getText().toString().trim();
            api = new API(apiname, apilink);
            aAPI.add(api);
            //fs.collection("APIS").add(api);
        }
        if(Ser1.isChecked()){
            aAlg.add(new Algorithm("Searching",Ser1.getText().toString()));
        }
        if(Ser2.isChecked()){
            aAlg.add(new Algorithm("Searching",Ser2.getText().toString()));
        }
        if(Ser3.isChecked()){
            if(Otherser.getText().toString()!="")
                aAlg.add(new Algorithm("Searching",Otherser.getText().toString()));
        }
        if(Sor1.isChecked()){
            aAlg.add(new Algorithm("Sorting",Sor1.getText().toString()));
        }
        if(Sor2.isChecked()){
            aAlg.add(new Algorithm("Sorting",Sor2.getText().toString()));
        }
        if(Sor3.isChecked()){
            if(Othersor.getText().toString()!="")
                aAlg.add(new Algorithm("Sorting",Othersor.getText().toString()));
        }
        for (int i = 0; i < AlgList.getChildCount(); i++) {
            View v1 = AlgList.getChildAt(i);
            EditText ALGName = (EditText) v1.findViewById(R.id.algName);
            String aLGname = ALGName.getText().toString().trim();
            EditText algTech = (EditText) v1.findViewById(R.id.algTech);
            String algT = algTech.getText().toString().trim();
            alg = new Algorithm( aLGname, algT);
            aAlg.add(alg);
        }

        for (int i = 0; i < needsList.getChildCount(); i++) {
            View v1 = needsList.getChildAt(i);
            EditText needs = (EditText) v1.findViewById(R.id.needed);
            String Need = needs.getText().toString().trim();
            aneeds.add(Need);
        }
        for (int i = 0; i < outputList.getChildCount(); i++) {
            View v1 = outputList.getChildAt(i);
            EditText out = (EditText) v1.findViewById(R.id.out);
            String sOut = out.getText().toString().trim();
            aoutputs.add(sOut);
        }
        for (int i = 0; i < featuresList.getChildCount(); i++) {
            View v1 = featuresList.getChildAt(i);
            EditText fet = (EditText) v1.findViewById(R.id.feture);
            String sFet = fet.getText().toString().trim();
            afeatures.add(sFet);
        }
        for (int i = 0; i < IndiList.getChildCount(); i++) {
            View v1 = IndiList.getChildAt(i);
            EditText indi = (EditText) v1.findViewById(R.id.indi);
            String sIndi = indi.getText().toString().trim();
            aindi.add(sIndi);
        }
        for (int i = 0; i < orgList.getChildCount(); i++) {
            View v1 = orgList.getChildAt(i);
            EditText Org = (EditText) v1.findViewById(R.id.org);
            String sOrg = Org.getText().toString().trim();
            aOrg.add(sOrg);
        }
        for (int i = 0; i < socList.getChildCount(); i++) {
            View v1 = socList.getChildAt(i);
            EditText soc = (EditText) v1.findViewById(R.id.soc);
            String sSoc = soc.getText().toString().trim();
            aSoc.add(sSoc);
        }
        for (int i = 0; i < refList.getChildCount(); i++) {
            View v1 = refList.getChildAt(i);
            EditText ref = (EditText) v1.findViewById(R.id.ref);
            String sRef = ref.getText().toString().trim();
            aRef.add(sRef);
        }
        pd = new ProposalDocument(userID, sobjective,sname, aexplination, UNs, aneeds,aoutputs,afeatures,aindi,aSoc,aOrg,aRef,ateam,aAlg,aAPI);

    }
    @Override
    public void onBackPressed() {
        new android.app.AlertDialog.Builder(proposalDoc.this)
                .setTitle("Save")
                .setMessage("Do you want to save the Document?")
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        proposalDoc.super.onBackPressed();
                        Intent i = new Intent(proposalDoc.this, ProjectDetails.class);
                        i.putExtra("Version",p1);
                        i.putExtra("Project",pro);
                        //i.putExtra("pid",pid);
                        startActivity(i);
                        finish();
                    }
                })
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        proposalDoc.super.onBackPressed();
                        SaveProposolDoc();
                        fAuth =FirebaseAuth.getInstance();
                        fs =  FirebaseFirestore.getInstance();
                        userID = fAuth.getCurrentUser().getUid();
                        fs.collection("proposalDoc").add(pd).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                fs.collection("documents").whereEqualTo("pid",pid).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                    @Override
                                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                        if(queryDocumentSnapshots.size()==0){
                                            fs.collection("proposalDoc").add(pd).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                @Override
                                                public void onSuccess(DocumentReference documentReference) {
                                                    // public Document(String uid, String type, String name, String docid, String pid, String verid)
                                                    Document d = new Document(userID, "proposal", name.getText().toString(), documentReference.getId(), pid, ver,null);
                                                    fs.collection("documents").document(documentReference.getId()).set(d);
                                                    Intent i = new Intent(proposalDoc.this, ProjectDetails.class);
                                                    i.putExtra("Version", p1);
                                                    i.putExtra("Project", pro);
                                                    // i.putExtra("pid",pid);
                                                    startActivity(i);
                                                    finish();
                                                }
                                            });
                                        }else{
                                            for (DocumentSnapshot ds: queryDocumentSnapshots) {
                                                Document d = ds.toObject(Document.class);
                                                if(d.getName().equals(name.getText().toString()) && d.getVerid().equals(p1.getVersion()))
                                                    Toast.makeText(proposalDoc.this,"Document name already exists",Toast.LENGTH_LONG).show();
                                                else{
                                                    fs.collection("proposalDoc").add(pd).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                        @Override
                                                        public void onSuccess(DocumentReference documentReference) {
                                                            // public Document(String uid, String type, String name, String docid, String pid, String verid)
                                                            Document d = new Document(userID,"proposal",name.getText().toString(),documentReference.getId(),pid,ver,null);
                                                            fs.collection("documents").document(documentReference.getId()).set(d);
                                                            Intent i = new Intent(proposalDoc.this, ProjectDetails.class);
                                                            i.putExtra("Version",p1);
                                                            i.putExtra("Project",pro);
                                                            // i.putExtra("pid",pid);
                                                            startActivity(i);
                                                            finish();
                                                        }
                                                    });
                                                    break;
                                                }

                                            }
                                        }



                                    }
                                });
                            }
                        });
                    }
                }).create().show();



    }

    class AbrCallback implements ActionMode.Callback {
        EditText et;

        public AbrCallback(EditText et) {
            this.et = et;
        }

        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            MenuInflater inflater = mode.getMenuInflater();
            inflater.inflate(R.menu.text_menu, menu);
            return true;
        }

        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            CharacterStyle cs;
            int start = et.getSelectionStart();
            int end = et.getSelectionEnd();
            String s = et.getText().toString().substring(start, end);
            SpannableStringBuilder ssb = new SpannableStringBuilder(s);
            final ForegroundColorSpan fcs = new ForegroundColorSpan(Color.BLUE);
            ssb.setSpan(fcs, 0, ssb.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
            String currenttext= et.getText().toString();
            String replace= "<span style='background-color:yellow'>"+s+"</span>";
            String newText = currenttext.replaceAll(s,replace);
            et.setText(Html.fromHtml(newText));


            switch (item.getItemId()) {
                case R.id.Abbrv:
                    Abbrev ab = new Abbrev(ssb.toString(), "");
                    boolean flag=true;
                    for(Abbrev ab1 : aexplination){
                        if(ab1.getName().trim().toLowerCase().equals(ab.getName().trim().toLowerCase()))
                            flag = false;
                    }
                    if(flag)
                        aexplination.add(ab);
                    return true;

            }
            return false;
        }

        public void onDestroyActionMode(ActionMode mode) {

        }
    }

}


