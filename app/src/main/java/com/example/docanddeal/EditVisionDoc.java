package com.example.docanddeal;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.CharacterStyle;
import android.text.style.ForegroundColorSpan;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import javax.security.auth.callback.Callback;

public class EditVisionDoc extends AppCompatActivity implements View.OnClickListener, Callback {
    ImageView done,plusProblemDecomp  , plusBusinessOpport, plusStakeholder, plusUser , plusUserNeeds, plusAlternative, plusCapabilities,plusAssump,pluspFeature,  plusref;
    LinearLayout ProblemDecompList, BusinessOpportList,StakeholderList,UserList,UserNeedsList,AlternativeList,CapabilitiesList,assumpList,pFeatureList,refList;
    ImageView hPurpose,hScope,hPS,hPS1,hPS2,hPS3,hPS4,hDecom,hBO,hPPS,hPPS1,hPPS2,hPPS3,hPPS4,hPPS5,hPPS6,hstake,huser,hstakeneed,hAlter,hPerc,hCapa,hAssum,hAppen,hProductFeat,hPrio,hEffo,hRisk,hStab,aPurpose,aScope,aPS,aPS1,aPS2,aPS3,aPS4,aDecom,aBO,aPPS,aPPS1,aPPS2,aPPS3,aPPS4,aPPS5,aPPS6,astake,auser,astakeneed,aAlter,aPerc,aCapa,aAssum,aAppen,aPrio,aEffo,aRisk,aStab;
    TextView tPurpose,tScope,tPS,tPS1,tPS2,tPS3,tPS4,tDecom,tBO,tPPS,tPPS1,tPPS2,tPPS3,tPPS4,tPPS5,tPPS6,tstake,tuser,tstakeneed,tAlter,tPerc,tCapa,tAssum,tAppen,tPrio,tEffo,tRisk,tStab;
    Boolean clicked;
    FirebaseAuth fAuth;
    FirebaseFirestore fs;
    String userID;
    VisionDocument vd;
    Document d;
    String uid, purpose,name,scope,prespictive,conclution;
    ArrayList<String> aBusinessOpportunity,astakeholders,ausers,aAlternatives,aAssumptions,arefrences;
    //ArrayList<String> eBusinessOpportunity,estakeholders,ausers,aAlternatives,aAssumptions,arefrences;
    ArrayList<Problemstatment> aprops;
    ArrayList<Abbrev> explination;
    ArrayList<VPriority> vPriorities;
    ArrayList<VEffort> vEffort;
    ArrayList<VRisk> vRisk;
    ArrayList<VStability> vstability;
    ArrayList<VCapabilities> aCapabilities;
    ProductPosition aprodpos;
    Abbrev exp;
    Problemstatment pros;
    VEffort veffort;
    VPriority vprior;
    VRisk vrisk;
    VStability vstab;
    VCapabilities vcap;
    Bundle b;
    Version p1;
    ProjectC pro;
    String pid;
    String ver;
    Button next;
    RelativeLayout visRL;
    EditText docNameVis,vpurpose,vscope,vProblemOF,vAffects,vImpactOF,vSolution,vFor,vWho,vTheApp,vThat,vUnlike,
            vOurProduct,proOver,con,pcritical,pimportant,puseful,ehigh,emed,elow,rhigh,rmed,rlow,shigh,smed,slow;
    VBusinessOpportAdapter vBusinessAdp;
    RecyclerView vBRV,refrv,stakerv,userrv,altrv,assumprv,probdecrv,keyNeedrv,caprv,pfeatrv;
    RefAdapter refAdp;
    StakeholderAdp stakeAdp;
    UserAdp userAdp;
    VBusinessOpportAdapter busAdp;
    AlterativeeAdp altAdp;
    AssumpAdp assumpAdp;
    PropsAdp proAdp;
    KeyNeedsAdp keynAdp;
    VCapabilitiesAdp vcapAdp;
    ProFeatAdp pfeatAdp;
    String docID;
    DocumentReference dref;
    ArrayList<KeyNeedsStake> vKeyNeed;
    ArrayList<VProductFeature> aprodfeat;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_vision_doc);
        explination = new ArrayList<>();
        Bundle bb = getIntent().getExtras();
        docID = bb.get("docID").toString();
        fAuth = FirebaseAuth.getInstance();
        fs = FirebaseFirestore.getInstance();
        dref = fs.collection("visionDoc").document(docID);
        stakerv = findViewById(R.id.StakeholderRV);
        vBRV = findViewById(R.id.BusinessOpportunityRV);
        visRL = findViewById(R.id.vis);
        done = findViewById(R.id.visionFinish);
        next = findViewById(R.id.nextToAbbrev);
        plusProblemDecomp = findViewById(R.id.plusProblemDecomposition);
        plusBusinessOpport = findViewById(R.id.plusBusinessOpportunity);
        plusStakeholder = findViewById(R.id.plusStakeholder);
        plusUser = findViewById(R.id.plusUser);
        plusUserNeeds = findViewById(R.id.plusUserNeeds);
        plusAlternative = findViewById(R.id.plusAlternative);
        plusCapabilities = findViewById(R.id.plusCapabilities);
        plusAssump = findViewById(R.id.plusAssump);
        pluspFeature = findViewById(R.id.pluspFeature);
        plusref = findViewById(R.id.plusRef);
        ProblemDecompList = findViewById(R.id.ProblemDecompositionList);
        BusinessOpportList = findViewById(R.id.BusinessOpportunityList);
        StakeholderList = findViewById(R.id.StakeholderList);
        UserList = findViewById(R.id.UserList);
        UserNeedsList = findViewById(R.id.UserNeedsList);
        AlternativeList = findViewById(R.id.AlternativeList);
        CapabilitiesList = findViewById(R.id.CapabilitiesList);
        assumpList = findViewById(R.id.assumpList);
        pFeatureList = findViewById(R.id.pFeatureList);
        refList = findViewById(R.id.refList);
        b = getIntent().getExtras();
        p1 = (Version) b.getSerializable("Version");
        pro = (ProjectC) b.getSerializable("Project");
        pid = pro.getPid();
        ver = p1.getVersion();
        aBusinessOpportunity = new ArrayList<>();
        astakeholders = new ArrayList<>();
        ausers = new ArrayList<>();
        aAlternatives = new ArrayList<>();
        aAssumptions = new ArrayList<>();
        aprodfeat = new ArrayList<>();
        arefrences = new ArrayList<>();
        aprops = new ArrayList<>();
        vKeyNeed = new ArrayList<>();
        vPriorities = new ArrayList<>();
        vEffort = new ArrayList<>();
        vRisk = new ArrayList<>();
        vstability = new ArrayList<>();
        aCapabilities = new ArrayList<>();
        docNameVis = findViewById(R.id.docNameVis);
        vpurpose = findViewById(R.id.vpurpose);
        vscope = findViewById(R.id.vscope);
        vProblemOF = findViewById(R.id.vProblemOF);
        vAffects = findViewById(R.id.vAffects);
        vImpactOF = findViewById(R.id.vImpactOF);
        vSolution = findViewById(R.id.vSolution);
        vFor = findViewById(R.id.vFor);
        vWho = findViewById(R.id.vWho);
        vTheApp = findViewById(R.id.vTheApp);
        vThat = findViewById(R.id.vThat);
        vUnlike = findViewById(R.id.vUnlike);
        vOurProduct = findViewById(R.id.vOurProduct);
        proOver = findViewById(R.id.proOver);
        con = findViewById(R.id.con);
        pcritical = findViewById(R.id.pcritical);
        pimportant = findViewById(R.id.pimportant);
        puseful = findViewById(R.id.puseful);
        ehigh = findViewById(R.id.ehigh);
        emed = findViewById(R.id.emed);
        elow = findViewById(R.id.elow);
        rhigh = findViewById(R.id.rhigh);
        rmed = findViewById(R.id.rmed);
        rlow = findViewById(R.id.rlow);
        shigh = findViewById(R.id.shigh);
        smed = findViewById(R.id.smed);
        slow = findViewById(R.id.slow);
        refrv = findViewById(R.id.refRV);
        userrv = findViewById(R.id.UserRV);
        altrv = findViewById(R.id.AlternativeRV);
        assumprv = findViewById(R.id.assumpRV);
        probdecrv = findViewById(R.id.ProblemDecompositionRV);
        keyNeedrv = findViewById(R.id.UserNeedsRV);
        caprv = findViewById(R.id.CapabilitiesRV);
        pfeatrv = findViewById(R.id.pFeatureRV);

        docNameVis.setCustomSelectionActionModeCallback(new EditVisionDoc.AbrCallback(docNameVis));
        vpurpose.setCustomSelectionActionModeCallback(new EditVisionDoc.AbrCallback(vpurpose));
        vscope.setCustomSelectionActionModeCallback(new EditVisionDoc.AbrCallback(vscope));
        vProblemOF.setCustomSelectionActionModeCallback(new EditVisionDoc.AbrCallback(vProblemOF));
        vAffects.setCustomSelectionActionModeCallback(new EditVisionDoc.AbrCallback(vAffects));
        vImpactOF.setCustomSelectionActionModeCallback(new EditVisionDoc.AbrCallback(vImpactOF));
        vSolution.setCustomSelectionActionModeCallback(new EditVisionDoc.AbrCallback(vSolution));
        vFor.setCustomSelectionActionModeCallback(new EditVisionDoc.AbrCallback(vFor));
        vWho.setCustomSelectionActionModeCallback(new EditVisionDoc.AbrCallback(vWho));
        vTheApp.setCustomSelectionActionModeCallback(new EditVisionDoc.AbrCallback(vTheApp));
        vThat.setCustomSelectionActionModeCallback(new EditVisionDoc.AbrCallback(vThat));
        vUnlike.setCustomSelectionActionModeCallback(new EditVisionDoc.AbrCallback(vUnlike));
        vOurProduct.setCustomSelectionActionModeCallback(new EditVisionDoc.AbrCallback(vOurProduct));
        proOver.setCustomSelectionActionModeCallback(new EditVisionDoc.AbrCallback(proOver));
        con.setCustomSelectionActionModeCallback(new EditVisionDoc.AbrCallback(con));
        pcritical.setCustomSelectionActionModeCallback(new EditVisionDoc.AbrCallback(pcritical));
        pcritical.setCustomSelectionActionModeCallback(new EditVisionDoc.AbrCallback(pcritical));
        puseful.setCustomSelectionActionModeCallback(new EditVisionDoc.AbrCallback(puseful));
        ehigh.setCustomSelectionActionModeCallback(new EditVisionDoc.AbrCallback(ehigh));
        emed.setCustomSelectionActionModeCallback(new EditVisionDoc.AbrCallback(emed));
        elow.setCustomSelectionActionModeCallback(new EditVisionDoc.AbrCallback(elow));
        rhigh.setCustomSelectionActionModeCallback(new EditVisionDoc.AbrCallback(rhigh));
        rmed.setCustomSelectionActionModeCallback(new EditVisionDoc.AbrCallback(rmed));
        rlow.setCustomSelectionActionModeCallback(new EditVisionDoc.AbrCallback(rlow));
        shigh.setCustomSelectionActionModeCallback(new EditVisionDoc.AbrCallback(shigh));
        smed.setCustomSelectionActionModeCallback(new EditVisionDoc.AbrCallback(smed));
        slow.setCustomSelectionActionModeCallback(new EditVisionDoc.AbrCallback(slow));

        plusProblemDecomp.setOnClickListener(this);
        plusBusinessOpport.setOnClickListener(this);
        plusStakeholder.setOnClickListener(this);
        plusUser.setOnClickListener(this);
        plusUserNeeds.setOnClickListener(this);
        plusAlternative.setOnClickListener(this);
        plusCapabilities.setOnClickListener(this);
        plusAssump.setOnClickListener(this);
        pluspFeature.setOnClickListener(this);
        plusref.setOnClickListener(this);

        hPurpose=findViewById(R.id.helpPurpose);
        hScope=findViewById(R.id.helpScope);
        hPS=findViewById(R.id.helpPS);
        hPS1=findViewById(R.id.helpPS1);
        hPS2=findViewById(R.id.helpPS2);
        hPS3=findViewById(R.id.helpPS3);
        hPS4=findViewById(R.id.helpPS4);
        hDecom=findViewById(R.id.helpDecom);
        hBO=findViewById(R.id.helpBO);
        hPPS=findViewById(R.id.helpPPS);
        hPPS1=findViewById(R.id.helpPPS1);
        hPPS2=findViewById(R.id.helpPPS2);
        hPPS3=findViewById(R.id.helpPPS3);
        hPPS4=findViewById(R.id.helpPPS4);
        hPPS5=findViewById(R.id.helpPPS5);
        hPPS6=findViewById(R.id.helpPPS6);
        hstake=findViewById(R.id.helpstake);
        huser=findViewById(R.id.helpuser);
        hstakeneed=findViewById(R.id.helpstakeneed);
        hAlter=findViewById(R.id.helpAlter);
        hPerc=findViewById(R.id.helpPerc);
        hCapa=findViewById(R.id.helpCapa);
        hAssum=findViewById(R.id.helpAssum);
        hAppen=findViewById(R.id.helpAppen);
        hProductFeat=findViewById(R.id.helpProductFeat);
        hPrio=findViewById(R.id.helpPrio);
        hEffo=findViewById(R.id.helpEffo);
        hRisk=findViewById(R.id.helpRisk);
        hStab=findViewById(R.id.helpStab);

        aPurpose=findViewById(R.id.arrowPurpose);
        aScope=findViewById(R.id.arrowScope);
        aPS=findViewById(R.id.arrowPS);
        aPS1=findViewById(R.id.arrowPS1);
        aPS2=findViewById(R.id.arrowPS2);
        aPS3=findViewById(R.id.arrowPS3);
        aPS4=findViewById(R.id.arrowPS4);
        aDecom=findViewById(R.id.arrowDecom);
        aBO=findViewById(R.id.arrowBO);
        aPPS=findViewById(R.id.arrowPPS);
        aPPS1=findViewById(R.id.arrowPPS1);
        aPPS2=findViewById(R.id.arrowPPS2);
        aPPS3=findViewById(R.id.arrowPPS3);
        aPPS4=findViewById(R.id.arrowPPS4);
        aPPS5=findViewById(R.id.arrowPPS5);
        aPPS6=findViewById(R.id.arrowPPS6);
        astake=findViewById(R.id.arrowstake);
        auser=findViewById(R.id.arrowuser);
        astakeneed=findViewById(R.id.arrowstakeneed);
        aAlter=findViewById(R.id.arrowAlter);
        aPerc=findViewById(R.id.arrowPerc);
        aCapa=findViewById(R.id.arrowCapa);
        aAssum=findViewById(R.id.arrowAssum);
        aAppen=findViewById(R.id.arrowAppen);
        aPrio=findViewById(R.id.arrowPrio);
        aEffo=findViewById(R.id.arrowEffo);
        aRisk=findViewById(R.id.arrowRisk);
        aStab=findViewById(R.id.arrowStab);

        tPurpose=findViewById(R.id.textPurpose);
        tScope=findViewById(R.id.textScope);
        tPS=findViewById(R.id.textPS);
        tPS1=findViewById(R.id.textPS1);
        tPS2=findViewById(R.id.textPS2);
        tPS3=findViewById(R.id.textPS3);
        tPS4=findViewById(R.id.textPS4);
        tDecom=findViewById(R.id.textDecom);
        tBO=findViewById(R.id.textBO);
        tPPS=findViewById(R.id.textPPS);
        tPPS1=findViewById(R.id.textPPS1);
        tPPS2=findViewById(R.id.textPPS2);
        tPPS3=findViewById(R.id.textPPS3);
        tPPS4=findViewById(R.id.textPPS4);
        tPPS5=findViewById(R.id.textPPS5);
        tPPS6=findViewById(R.id.textPPS6);
        tstake=findViewById(R.id.textstake);
        tuser=findViewById(R.id.textuser);
        tstakeneed=findViewById(R.id.textstakeneed);
        tAlter=findViewById(R.id.textAlter);
        tPerc=findViewById(R.id.textPerc);
        tCapa=findViewById(R.id.textCapa);
        tAssum=findViewById(R.id.textAssum);
        tAppen=findViewById(R.id.textAppen);
        tPrio=findViewById(R.id.textPrio);
        tEffo=findViewById(R.id.textEffo);
        tRisk=findViewById(R.id.textRisk);
        tStab=findViewById(R.id.textStab);

        clicked=false;
        aPurpose.setVisibility(View.GONE);
        aScope.setVisibility(View.GONE);
        aPS.setVisibility(View.GONE);
        aPS1.setVisibility(View.GONE);
        aPS2.setVisibility(View.GONE);
        aPS3.setVisibility(View.GONE);
        aPS4.setVisibility(View.GONE);
        aDecom.setVisibility(View.GONE);
        aBO.setVisibility(View.GONE);
        aPPS.setVisibility(View.GONE);
        aPPS1.setVisibility(View.GONE);
        aPPS2.setVisibility(View.GONE);
        aPPS3.setVisibility(View.GONE);
        aPPS4.setVisibility(View.GONE);
        aPPS5.setVisibility(View.GONE);
        aPPS6.setVisibility(View.GONE);
        astake.setVisibility(View.GONE);
        auser.setVisibility(View.GONE);
        astakeneed.setVisibility(View.GONE);
        aAlter.setVisibility(View.GONE);
        aPerc.setVisibility(View.GONE);
        aCapa.setVisibility(View.GONE);
        aAssum.setVisibility(View.GONE);
        aAppen.setVisibility(View.GONE);
        aPrio.setVisibility(View.GONE);
        aEffo.setVisibility(View.GONE);
        aRisk.setVisibility(View.GONE);
        aStab.setVisibility(View.GONE);
        tPurpose.setVisibility(View.GONE);
        tScope.setVisibility(View.GONE);
        tPS.setVisibility(View.GONE);
        tPS1.setVisibility(View.GONE);
        tPS2.setVisibility(View.GONE);
        tPS3.setVisibility(View.GONE);
        tPS4.setVisibility(View.GONE);
        tDecom.setVisibility(View.GONE);
        tBO.setVisibility(View.GONE);
        tPPS.setVisibility(View.GONE);
        tPPS1.setVisibility(View.GONE);
        tPPS2.setVisibility(View.GONE);
        tPPS3.setVisibility(View.GONE);
        tPPS4.setVisibility(View.GONE);
        tPPS5.setVisibility(View.GONE);
        tPPS6.setVisibility(View.GONE);
        tstake.setVisibility(View.GONE);
        tuser.setVisibility(View.GONE);
        tstakeneed.setVisibility(View.GONE);
        tAlter.setVisibility(View.GONE);
        tPerc.setVisibility(View.GONE);
        tCapa.setVisibility(View.GONE);
        tAssum.setVisibility(View.GONE);
        tAppen.setVisibility(View.GONE);
        tPrio.setVisibility(View.GONE);
        tEffo.setVisibility(View.GONE);
        tRisk.setVisibility(View.GONE);
        tStab.setVisibility(View.GONE);



        vBRV.setHasFixedSize(true);
        RecyclerView.LayoutManager lm = new LinearLayoutManager(this);
        vBRV.setLayoutManager(lm);
        stakerv.setHasFixedSize(true);
        RecyclerView.LayoutManager lm1 = new LinearLayoutManager(this);
        stakerv.setLayoutManager(lm1);
        userrv.setHasFixedSize(true);
        RecyclerView.LayoutManager lm2 = new LinearLayoutManager(this);
        userrv.setLayoutManager(lm2);
        altrv.setHasFixedSize(true);
        RecyclerView.LayoutManager lm3 = new LinearLayoutManager(this);
        altrv.setLayoutManager(lm3);
        assumprv.setHasFixedSize(true);
        RecyclerView.LayoutManager lm4 = new LinearLayoutManager(this);
        assumprv.setLayoutManager(lm4);
        probdecrv.setHasFixedSize(true);
        RecyclerView.LayoutManager lm5 = new LinearLayoutManager(this);
        probdecrv.setLayoutManager(lm5);
        keyNeedrv.setHasFixedSize(true);
        RecyclerView.LayoutManager lm6 = new LinearLayoutManager(this);
        keyNeedrv.setLayoutManager(lm6);
        caprv.setHasFixedSize(true);
        RecyclerView.LayoutManager lm7 = new LinearLayoutManager(this);
        caprv.setLayoutManager(lm7);
        pfeatrv.setHasFixedSize(true);
        RecyclerView.LayoutManager lm8 = new LinearLayoutManager(this);
        pfeatrv.setLayoutManager(lm8);
        refrv.setHasFixedSize(true);
        RecyclerView.LayoutManager lm9 = new LinearLayoutManager(this);
        refrv.setLayoutManager(lm9);

        hPurpose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (clicked==false){
                    tPurpose.setVisibility(View.GONE);
                    aPurpose.setVisibility(View.GONE);
                    clicked = true;
                }
                else {
                    tPurpose.setVisibility(View.VISIBLE);
                    aPurpose.setVisibility(View.VISIBLE);
                    clicked = false;
                }
            }
        });
        hScope.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (clicked==false){
                    tScope.setVisibility(View.GONE);
                    aScope.setVisibility(View.GONE);
                    clicked = true;
                }
                else {
                    tScope.setVisibility(View.VISIBLE);
                    aScope.setVisibility(View.VISIBLE);
                    clicked = false;
                }
            }
        });
        hPS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (clicked==false){
                    tPS.setVisibility(View.GONE);
                    aPS.setVisibility(View.GONE);
                    clicked = true;
                }
                else {
                    tPS.setVisibility(View.VISIBLE);
                    aPS.setVisibility(View.VISIBLE);
                    clicked = false;
                }
            }
        });
        hPS1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (clicked==false){
                    tPS1.setVisibility(View.GONE);
                    aPS1.setVisibility(View.GONE);
                    clicked = true;
                }
                else {
                    tPS1.setVisibility(View.VISIBLE);
                    aPS1.setVisibility(View.VISIBLE);
                    clicked = false;
                }
            }
        });
        hPS2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (clicked==false){
                    tPS2.setVisibility(View.GONE);
                    aPS2.setVisibility(View.GONE);
                    clicked = true;
                }
                else {
                    tPS2.setVisibility(View.VISIBLE);
                    aPS2.setVisibility(View.VISIBLE);
                    clicked = false;
                }
            }
        });
        hPS3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (clicked==false){
                    tPS3.setVisibility(View.GONE);
                    aPS3.setVisibility(View.GONE);
                    clicked = true;
                }
                else {
                    tPS3.setVisibility(View.VISIBLE);
                    aPS3.setVisibility(View.VISIBLE);
                    clicked = false;
                }
            }
        });
        hPS4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (clicked==false){
                    tPS4.setVisibility(View.GONE);
                    aPS4.setVisibility(View.GONE);
                    clicked = true;
                }
                else {
                    tPS4.setVisibility(View.VISIBLE);
                    aPS4.setVisibility(View.VISIBLE);
                    clicked = false;
                }
            }
        });
        hDecom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (clicked==false){
                    tDecom.setVisibility(View.GONE);
                    aDecom.setVisibility(View.GONE);
                    clicked = true;
                }
                else {
                    tDecom.setVisibility(View.VISIBLE);
                    aDecom.setVisibility(View.VISIBLE);
                    clicked = false;
                }
            }
        });
        hBO.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (clicked==false){
                    tBO.setVisibility(View.GONE);
                    aBO.setVisibility(View.GONE);
                    clicked = true;
                }
                else {
                    tBO.setVisibility(View.VISIBLE);
                    aBO.setVisibility(View.VISIBLE);
                    clicked = false;
                }
            }
        });
        hPPS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (clicked==false){
                    tPPS.setVisibility(View.GONE);
                    aPPS.setVisibility(View.GONE);
                    clicked = true;
                }
                else {
                    tPPS.setVisibility(View.VISIBLE);
                    aPPS.setVisibility(View.VISIBLE);
                    clicked = false;
                }
            }
        });
        hPPS1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (clicked==false){
                    tPPS1.setVisibility(View.GONE);
                    aPPS1.setVisibility(View.GONE);
                    clicked = true;
                }
                else {
                    tPPS1.setVisibility(View.VISIBLE);
                    aPPS1.setVisibility(View.VISIBLE);
                    clicked = false;
                }
            }
        });
        hPPS2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (clicked==false){
                    tPPS2.setVisibility(View.GONE);
                    aPPS2.setVisibility(View.GONE);
                    clicked = true;
                }
                else {
                    tPPS2.setVisibility(View.VISIBLE);
                    aPPS2.setVisibility(View.VISIBLE);
                    clicked = false;
                }
            }
        });
        hPPS3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (clicked==false){
                    tPPS3.setVisibility(View.GONE);
                    aPPS3.setVisibility(View.GONE);
                    clicked = true;
                }
                else {
                    tPPS3.setVisibility(View.VISIBLE);
                    aPPS3.setVisibility(View.VISIBLE);
                    clicked = false;
                }
            }
        });
        hPPS4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (clicked==false){
                    tPPS4.setVisibility(View.GONE);
                    aPPS4.setVisibility(View.GONE);
                    clicked = true;
                }
                else {
                    tPPS4.setVisibility(View.VISIBLE);
                    aPPS4.setVisibility(View.VISIBLE);
                    clicked = false;
                }
            }
        });
        hPPS5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (clicked==false){
                    tPPS5.setVisibility(View.GONE);
                    aPPS5.setVisibility(View.GONE);
                    clicked = true;
                }
                else {
                    tPPS5.setVisibility(View.VISIBLE);
                    aPPS5.setVisibility(View.VISIBLE);
                    clicked = false;
                }
            }
        });
        hPPS6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (clicked==false){
                    tPPS6.setVisibility(View.GONE);
                    aPPS6.setVisibility(View.GONE);
                    clicked = true;
                }
                else {
                    tPPS6.setVisibility(View.VISIBLE);
                    aPPS6.setVisibility(View.VISIBLE);
                    clicked = false;
                }
            }
        });
        hstake.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (clicked==false){
                    tstake.setVisibility(View.GONE);
                    astake.setVisibility(View.GONE);
                    clicked = true;
                }
                else {
                    tstake.setVisibility(View.VISIBLE);
                    astake.setVisibility(View.VISIBLE);
                    clicked = false;
                }
            }
        });
        huser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (clicked==false){
                    tuser.setVisibility(View.GONE);
                    auser.setVisibility(View.GONE);
                    clicked = true;
                }
                else {
                    tuser.setVisibility(View.VISIBLE);
                    auser.setVisibility(View.VISIBLE);
                    clicked = false;
                }
            }
        });
        hstakeneed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (clicked==false){
                    tstakeneed.setVisibility(View.GONE);
                    astakeneed.setVisibility(View.GONE);
                    clicked = true;
                }
                else {
                    tstakeneed.setVisibility(View.VISIBLE);
                    astakeneed.setVisibility(View.VISIBLE);
                    clicked = false;
                }
            }
        });
        hAlter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (clicked==false){
                    tAlter.setVisibility(View.GONE);
                    aAlter.setVisibility(View.GONE);
                    clicked = true;
                }
                else {
                    tAlter.setVisibility(View.VISIBLE);
                    aAlter.setVisibility(View.VISIBLE);
                    clicked = false;
                }
            }
        });
        hPerc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (clicked==false){
                    tPerc.setVisibility(View.GONE);
                    aPerc.setVisibility(View.GONE);
                    clicked = true;
                }
                else {
                    tPerc.setVisibility(View.VISIBLE);
                    aPerc.setVisibility(View.VISIBLE);
                    clicked = false;
                }
            }
        });
        hCapa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (clicked==false){
                    tCapa.setVisibility(View.GONE);
                    aCapa.setVisibility(View.GONE);
                    clicked = true;
                }
                else {
                    tCapa.setVisibility(View.VISIBLE);
                    aCapa.setVisibility(View.VISIBLE);
                    clicked = false;
                }
            }
        });
        hAssum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (clicked==false){
                    tAssum.setVisibility(View.GONE);
                    aAssum.setVisibility(View.GONE);
                    clicked = true;
                }
                else {
                    tAssum.setVisibility(View.VISIBLE);
                    aAssum.setVisibility(View.VISIBLE);
                    clicked = false;
                }
            }
        });
        hAppen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (clicked==false){
                    tAppen.setVisibility(View.GONE);
                    aAppen.setVisibility(View.GONE);
                    clicked = true;
                }
                else {
                    tAppen.setVisibility(View.VISIBLE);
                    aAppen.setVisibility(View.VISIBLE);
                    clicked = false;
                }
            }
        });
        hProductFeat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog customdialog= new Dialog(EditVisionDoc.this);
                customdialog.getWindow().setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
                customdialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                customdialog.getWindow().getAttributes().windowAnimations
                        = android.R.style.Animation_Dialog;
                customdialog.setContentView(R.layout.help_product_features);
                Button ok =customdialog.findViewById(R.id.okButton);
                ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        customdialog.cancel();
                    }
                });
                customdialog.show();
            }
        });

        hPrio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (clicked==false){
                    tPrio.setVisibility(View.GONE);
                    aPrio.setVisibility(View.GONE);
                    clicked = true;
                }
                else {
                    tPrio.setVisibility(View.VISIBLE);
                    aPrio.setVisibility(View.VISIBLE);
                    clicked = false;
                }
            }
        });
        hEffo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (clicked==false){
                    tEffo.setVisibility(View.GONE);
                    aEffo.setVisibility(View.GONE);
                    clicked = true;
                }
                else {
                    tEffo.setVisibility(View.VISIBLE);
                    aEffo.setVisibility(View.VISIBLE);
                    clicked = false;
                }
            }
        });
        hRisk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (clicked==false){
                    tRisk.setVisibility(View.GONE);
                    aRisk.setVisibility(View.GONE);
                    clicked = true;
                }
                else {
                    tRisk.setVisibility(View.VISIBLE);
                    aRisk.setVisibility(View.VISIBLE);
                    clicked = false;
                }
            }
        });
        hStab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (clicked==false){
                    tStab.setVisibility(View.GONE);
                    aStab.setVisibility(View.GONE);
                    clicked = true;
                }
                else {
                    tStab.setVisibility(View.VISIBLE);
                    aStab.setVisibility(View.VISIBLE);
                    clicked = false;
                }
            }
        });

        dref.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                vd = documentSnapshot.toObject(VisionDocument.class);
                //fillig adapters with arraylists (filling fields with data)
                if (!vd.getPurpose().isEmpty()) {
                    vpurpose.setText(vd.getPurpose());
                }
                if (!vd.getDname().isEmpty()) {
                    docNameVis.setText(vd.getDname());
                }
                if (!vd.getScope().isEmpty()) {
                    vscope.setText(vd.getScope());
                }
                if (!vd.getPerspective().isEmpty()) {
                    proOver.setText(vd.getPerspective());
                }
                if (!vd.getConclusion().isEmpty()) {
                    con.setText(vd.getConclusion());
                }
                if (vd.getExplination().size() != 0) {
                    explination = vd.getExplination();
                }
                if (vd.getBusinessOpportunity().size() != 0) {
                    aBusinessOpportunity = vd.getBusinessOpportunity();
                    busAdp = new VBusinessOpportAdapter(aBusinessOpportunity, EditVisionDoc.this, visRL,explination);
                    vBRV.setAdapter(busAdp);
                }
                if (vd.getStakeholders().size() != 0) {
                    astakeholders = vd.getStakeholders();
                    stakeAdp = new StakeholderAdp(astakeholders, EditVisionDoc.this, visRL,explination);
                    stakerv.setAdapter(stakeAdp);
                }
                if (vd.getUsers().size() != 0) {
                    ausers = vd.getUsers();
                    userAdp = new UserAdp(ausers, EditVisionDoc.this, visRL,explination);
                    userrv.setAdapter(userAdp);
                }
                if (vd.getAlternatives().size() != 0) {
                    aAlternatives = vd.getAlternatives();
                    altAdp = new AlterativeeAdp(aAlternatives, EditVisionDoc.this, visRL,explination);
                    altrv.setAdapter(altAdp);
                }
                if (vd.getAssumptions().size() != 0) {
                    aAssumptions = vd.getAssumptions();
                    assumpAdp = new AssumpAdp(aAssumptions, EditVisionDoc.this, visRL,explination);
                    assumprv.setAdapter(assumpAdp);
                }
                if (vd.getProps().size() != 0) {
                    aprops = vd.getProps();
                    vProblemOF.setText(aprops.get(0).getProblem());
                    vAffects.setText(aprops.get(0).getAffect());
                    vImpactOF.setText(aprops.get(0).getImpact());
                    vSolution.setText(aprops.get(0).getSolution());
                    aprops.remove(0);
                    proAdp = new PropsAdp(aprops, EditVisionDoc.this, visRL,explination);
                    probdecrv.setAdapter(proAdp);
                }

                if (vd.getRefrences().size() != 0) {
                    arefrences = vd.getRefrences();
                    refAdp = new RefAdapter(arefrences, EditVisionDoc.this, visRL,explination);
                    refrv.setAdapter(refAdp);
                }
                if (!vd.getProdpos().equals(null)) {
                    vFor.setText(vd.getProdpos().getFor());
                    vWho.setText(vd.getProdpos().getWho());
                    vTheApp.setText(vd.getProdpos().getWhatIs());
                    vThat.setText(vd.getProdpos().getThat());
                    vUnlike.setText(vd.getProdpos().getUnlike());
                    vOurProduct.setText(vd.getProdpos().getOurProduct());
                }
                if (vd.getvPriorities().size()!=0) {
                    pcritical.setText(vd.getvPriorities().get(0).getDesc());
                    pimportant.setText(vd.getvPriorities().get(1).getDesc());
                    puseful.setText(vd.getvPriorities().get(2).getDesc());
                }

                if (vd.getvEffort().size()!=0) {
                    ehigh.setText(vd.getvEffort().get(0).getDesc());
                    emed.setText(vd.getvEffort().get(1).getDesc());
                    elow.setText(vd.getvEffort().get(2).getDesc());
                }

                if (vd.getVrisk().size()!=0) {
                    rhigh.setText(vd.getVrisk().get(0).getDesc());
                    rmed.setText(vd.getVrisk().get(1).getDesc());
                    rlow.setText(vd.getVrisk().get(2).getDesc());
                }

                if (vd.getVstability().size()!=0) {
                    shigh.setText(vd.getVstability().get(0).getDesc());
                    smed.setText(vd.getVstability().get(1).getDesc());
                    slow.setText(vd.getVstability().get(2).getDesc());
                }
                if (vd.getvKeyNeed().size()!=0) {
                    vKeyNeed = vd.getvKeyNeed();
                    keynAdp = new KeyNeedsAdp(vKeyNeed, EditVisionDoc.this, visRL,explination);
                    keyNeedrv.setAdapter(keynAdp);
                }

                if (vd.getvCapabilities().size()!=0) {
                    aCapabilities = vd.getvCapabilities();
                    vcapAdp = new VCapabilitiesAdp(aCapabilities, EditVisionDoc.this, visRL,explination);
                    caprv.setAdapter(vcapAdp);
                }

                if (vd.getvProductFeat().size()!=0) {
                    aprodfeat = vd.getvProductFeat();
                    pfeatAdp = new ProFeatAdp(aprodfeat, EditVisionDoc.this, visRL,explination);
                    pfeatrv.setAdapter(pfeatAdp);
                }

            }
        });

        next. setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SaveVision();
                Intent i = new Intent(EditVisionDoc.this, EditVision2.class);
                i.putExtra("vis",vd);
                i.putExtra("Version",p1);
                i.putExtra("exp",explination);
                i.putExtra("Project",pro);
                i.putExtra("docID",docID);
                EditVisionDoc.this.startActivity(i);
            }
        });

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(EditVisionDoc.this)
                        .setTitle("Save")
                        .setMessage("Do you want to save the Document?")
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent i = new Intent(EditVisionDoc.this, ProjectDetails.class);
                                i.putExtra("Version",p1);
                                i.putExtra("Project",pro);
                                startActivity(i);
                                finish();
                            }
                        })
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface arg0, int arg1) {
                                SaveVision();
                                fAuth =FirebaseAuth.getInstance();
                                fs =  FirebaseFirestore.getInstance();
                                userID = fAuth.getCurrentUser().getUid();
                                dref.update("alternatives",aAlternatives);
                                dref.update("assumptions",aAssumptions);
                                dref.update("businessOpportunity",aBusinessOpportunity);
                                dref.update("conclusion",conclution);
                                dref.update("dname",name);
                                //dref.update("explination",explination);
                                dref.update("perspective",prespictive);
                                dref.update("props",aprops);
                                dref.update("purpose",purpose);
                                dref.update("refrences",arefrences);
                                dref.update("scope",scope);
                                dref.update("stakeholders",astakeholders);
                                dref.update("users",ausers);
                                dref.update("vCapabilities",aCapabilities);
                                dref.update("vEffort",vEffort);
                                dref.update("vPriorities",vPriorities);
                                dref.update("vrisk",vRisk);
                                dref.update("vstability",vstability);
                                dref.update("vKeyNeed",vKeyNeed);
                                dref.update("vProductFeat",aprodfeat);
                                Intent i = new Intent(EditVisionDoc.this, ProjectDetails.class);
                                i.putExtra("Version",p1);
                                i.putExtra("Project",pro);
                                startActivity(i);
                                finish();
                            }
                        }).create().show();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.plusProblemDecomposition:
                addViewProbDecomp();
                break;
            case R.id.plusBusinessOpportunity:
                addViewBusinessOpp();
                break;
            case R.id.plusStakeholder:
                addViewStake();
                break;
            case R.id.plusUser:
                addViewUser();
                break;
            case R.id.plusUserNeeds:
                addViewUserNeeds();
                break;
            case R.id.plusAlternative:
                addViewAlternative();
                break;
            case R.id.plusCapabilities:
                addViewCapabilities();
                break;
            case R.id.plusAssump:
                addViewAssump();
                break;
            case R.id.pluspFeature:
                addViewpFeature();
                break;
            case R.id.plusRef:
                addViewRef();
                break;
            default:
                break;
        }
    }

    private void addViewProbDecomp() {
        View probdview = getLayoutInflater().inflate(R.layout.problem_decomposition,null,false);
        EditText probOf = (EditText) probdview.findViewById(R.id.vProblemOF);
        EditText affect = (EditText) probdview.findViewById(R.id.vAffects);
        EditText impact = (EditText) probdview.findViewById(R.id.vImpactOF);
        EditText solution = (EditText) probdview.findViewById(R.id.vSolution);
        ImageView delProb = (ImageView) probdview.findViewById(R.id.deleteprobdec);
        delProb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s1 = probOf.getText().toString();
                String s2 = affect.getText().toString();
                String s3 = impact.getText().toString();
                String s4 = solution.getText().toString();
                removeViewProbDecomp(probdview,s1,s2,s3,s4);
            }
        });
        probOf.setCustomSelectionActionModeCallback(new EditVisionDoc.AbrCallback(probOf));
        affect.setCustomSelectionActionModeCallback(new EditVisionDoc.AbrCallback(affect));
        impact.setCustomSelectionActionModeCallback(new EditVisionDoc.AbrCallback(impact));
        solution.setCustomSelectionActionModeCallback(new EditVisionDoc.AbrCallback(solution));
        ProblemDecompList.addView(probdview);
    }
    private void addViewProbDecomp(String sp1, String sp2, String sp3, String sp4) {
        View probdview = getLayoutInflater().inflate(R.layout.problem_decomposition,null,false);
        EditText probOf = (EditText) probdview.findViewById(R.id.vProblemOF);
        probOf.setText(sp1);
        EditText affect = (EditText) probdview.findViewById(R.id.vAffects);
        affect.setText(sp2);
        EditText impact = (EditText) probdview.findViewById(R.id.vImpactOF);
        impact.setText(sp3);
        EditText solution = (EditText) probdview.findViewById(R.id.vSolution);
        solution.setText(sp4);
        ImageView delProb = (ImageView) probdview.findViewById(R.id.deleteprobdec);
        delProb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s1 = probOf.getText().toString();
                String s2 = affect.getText().toString();
                String s3 = impact.getText().toString();
                String s4 = solution.getText().toString();
                removeViewProbDecomp(probdview,s1,s2,s3,s4);
            }
        });
        probOf.setCustomSelectionActionModeCallback(new EditVisionDoc.AbrCallback(probOf));
        affect.setCustomSelectionActionModeCallback(new EditVisionDoc.AbrCallback(affect));
        impact.setCustomSelectionActionModeCallback(new EditVisionDoc.AbrCallback(impact));
        solution.setCustomSelectionActionModeCallback(new EditVisionDoc.AbrCallback(solution));
        ProblemDecompList.addView(probdview);
    }

    private void removeViewProbDecomp(View probdview, String s1, String s2, String s3, String s4) {
        ProblemDecompList.removeView(probdview);
        Snackbar.make(visRL,"The item is deleted", Snackbar.LENGTH_LONG).setAction("UNDO", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(s1.isEmpty()&&s2.isEmpty()&&s3.isEmpty()&&s4.isEmpty()){
                    addViewProbDecomp();
                }
                else {
                    addViewProbDecomp(s1, s2,s3,s4);
                }            }
        }).show();
    }

    private void addViewBusinessOpp() {
        View BusinessOppview = getLayoutInflater().inflate(R.layout.business_opportunity,null,false);
        EditText buisness = (EditText) BusinessOppview.findViewById(R.id.bopp);
        ImageView delBuisness = (ImageView) BusinessOppview.findViewById(R.id.deletebopp);
        delBuisness.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s1 = buisness.getText().toString();
                removeViewBusinessOpp(BusinessOppview,s1);
            }
        });
        buisness.setCustomSelectionActionModeCallback(new EditVisionDoc.AbrCallback(buisness));
        BusinessOpportList.addView(BusinessOppview);
    }

    private void addViewBusinessOpp(String sp1) {
        View BusinessOppview = getLayoutInflater().inflate(R.layout.business_opportunity,null,false);
        EditText buisness = (EditText) BusinessOppview.findViewById(R.id.bopp);
        buisness.setText(sp1);
        ImageView delBuisness = (ImageView) BusinessOppview.findViewById(R.id.deletebopp);
        delBuisness.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s1 = buisness.getText().toString();
                removeViewBusinessOpp(BusinessOppview,s1);
            }
        });
        buisness.setCustomSelectionActionModeCallback(new EditVisionDoc.AbrCallback(buisness));
        BusinessOpportList.addView(BusinessOppview);
    }

    private void removeViewBusinessOpp(View businessOppview, String s1) {
        BusinessOpportList.removeView(businessOppview);
        Snackbar.make(visRL,"The item is deleted", Snackbar.LENGTH_LONG).setAction("UNDO", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(s1.isEmpty()){
                    addViewBusinessOpp();
                }
                else {
                    addViewBusinessOpp(s1);
                }            }
        }).show();
    }

    private void addViewStake() {
        View Stakeview = getLayoutInflater().inflate(R.layout.stakeholder_summary,null,false);
        EditText stake = (EditText) Stakeview.findViewById(R.id.StakeHolder);
        ImageView delStake = (ImageView) Stakeview.findViewById(R.id.deleteStakeHolder);
        delStake.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s1 = stake.getText().toString();
                removeViewStake(Stakeview,s1);
            }
        });
        stake.setCustomSelectionActionModeCallback(new EditVisionDoc.AbrCallback(stake));
        StakeholderList.addView(Stakeview);
    }
    private void addViewStake(String sp1) {
        View Stakeview = getLayoutInflater().inflate(R.layout.stakeholder_summary,null,false);
        EditText stake = (EditText) Stakeview.findViewById(R.id.StakeHolder);
        stake.setText(sp1);
        ImageView delStake = (ImageView) Stakeview.findViewById(R.id.deleteStakeHolder);
        delStake.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s1 = stake.getText().toString();
                removeViewStake(Stakeview,s1);
            }
        });
        stake.setCustomSelectionActionModeCallback(new EditVisionDoc.AbrCallback(stake));
        StakeholderList.addView(Stakeview);
    }

    private void removeViewStake(View stakeview, String s1) {
        StakeholderList.removeView(stakeview);
        Snackbar.make(visRL,"The item is deleted", Snackbar.LENGTH_LONG).setAction("UNDO", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(s1.isEmpty()){
                    addViewStake();
                }
                else {
                    addViewStake(s1);
                }            }
        }).show();

    }

    private void addViewUser() {
        View Userview = getLayoutInflater().inflate(R.layout.user_summary,null,false);
        EditText user = (EditText) Userview.findViewById(R.id.user);
        ImageView delUser = (ImageView) Userview.findViewById(R.id.deleteUser);
        delUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s1 = user.getText().toString();
                removeViewUser(Userview,s1);
            }
        });
        user.setCustomSelectionActionModeCallback(new EditVisionDoc.AbrCallback(user));
        UserList.addView(Userview);
    }

    private void addViewUser(String sp1) {
        View Userview = getLayoutInflater().inflate(R.layout.user_summary,null,false);
        EditText user = (EditText) Userview.findViewById(R.id.user);
        user.setText(sp1);
        ImageView delUser = (ImageView) Userview.findViewById(R.id.deleteUser);
        delUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s1 = user.getText().toString();
                removeViewUser(Userview,s1);
            }
        });
        user.setCustomSelectionActionModeCallback(new EditVisionDoc.AbrCallback(user));
        UserList.addView(Userview);
    }

    private void removeViewUser(View userview, String s1) {
        UserList.removeView(userview);
        Snackbar.make(visRL,"The item is deleted", Snackbar.LENGTH_LONG).setAction("UNDO", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(s1.isEmpty()){
                    addViewUser();
                }
                else {
                    addViewUser(s1);
                }            }
        }).show();
    }

    private void addViewUserNeeds() {
        View keyview = getLayoutInflater().inflate(R.layout.key_needs,null,false);
        ArrayList<String> pri = new ArrayList<>();
        pri.add("Critical");
        pri.add("Important");
        pri.add("Useful");
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this,R.layout.list_item,pri);
        EditText need = (EditText) keyview.findViewById(R.id.vNeed);
        Spinner Priority = (Spinner) keyview.findViewById(R.id.spinner);
        Priority.setAdapter(arrayAdapter);
        EditText Concerns = (EditText) keyview.findViewById(R.id.vConcerns);
        EditText currsolution = (EditText) keyview.findViewById(R.id.vCurrSolution);
        EditText prsolution = (EditText) keyview.findViewById(R.id.vPropSolution);
        ImageView delProb = (ImageView) keyview.findViewById(R.id.deletekneed);
        delProb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s1 = need.getText().toString();
                String s2 = Priority.getSelectedItem().toString();
                String s3 = Concerns.getText().toString();
                String s4 = currsolution.getText().toString();
                String s5 = prsolution.getText().toString();
                removeViewKey(keyview,s1,s2,s3,s4,s5);
            }
        });
        need.setCustomSelectionActionModeCallback(new EditVisionDoc.AbrCallback(need));
        Concerns.setCustomSelectionActionModeCallback(new EditVisionDoc.AbrCallback(Concerns));
        currsolution.setCustomSelectionActionModeCallback(new EditVisionDoc.AbrCallback(currsolution));
        prsolution.setCustomSelectionActionModeCallback(new EditVisionDoc.AbrCallback(prsolution));
        UserNeedsList.addView(keyview);
    }

    private void addViewUserNeeds(String sp1 , String sp2, String sp3, String sp4, String sp5) {
        View keyview = getLayoutInflater().inflate(R.layout.key_needs,null,false);
        ArrayList<String> pri = new ArrayList<>();
        pri.add("Critical");
        pri.add("Important");
        pri.add("Useful");
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this,R.layout.list_item,pri);
        EditText need = (EditText) keyview.findViewById(R.id.vNeed);
        need.setText(sp1);
        Spinner Priority = (Spinner) keyview.findViewById(R.id.spinner);
        Priority.setAdapter(arrayAdapter);
        Priority.setSelection(arrayAdapter.getPosition(sp2));
        EditText Concerns = (EditText) keyview.findViewById(R.id.vConcerns);
        Concerns.setText(sp3);
        EditText currsolution = (EditText) keyview.findViewById(R.id.vCurrSolution);
        currsolution.setText(sp4);
        EditText prsolution = (EditText) keyview.findViewById(R.id.vPropSolution);
        prsolution.setText(sp5);
        ImageView delProb = (ImageView) keyview.findViewById(R.id.deletekneed);
        delProb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s1 = need.getText().toString();
                String s2 = Priority.getSelectedItem().toString();
                String s3 = Concerns.getText().toString();
                String s4 = currsolution.getText().toString();
                String s5 = prsolution.getText().toString();
                removeViewKey(keyview,s1,s2,s3,s4,s5);
            }
        });
        need.setCustomSelectionActionModeCallback(new EditVisionDoc.AbrCallback(need));
        Concerns.setCustomSelectionActionModeCallback(new EditVisionDoc.AbrCallback(Concerns));
        currsolution.setCustomSelectionActionModeCallback(new EditVisionDoc.AbrCallback(currsolution));
        prsolution.setCustomSelectionActionModeCallback(new EditVisionDoc.AbrCallback(prsolution));
        UserNeedsList.addView(keyview);
    }

    private void removeViewKey(View keyview, String s1, String s2, String s3, String s4, String s5) {
        UserNeedsList.removeView(keyview);
        Snackbar.make(visRL,"The item is deleted", Snackbar.LENGTH_LONG).setAction("UNDO", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(s1.isEmpty()&&s2.isEmpty()&&s3.isEmpty()&&s4.isEmpty()&&s5.isEmpty()){
                    addViewUserNeeds();
                }
                else {
                    addViewUserNeeds(s1,s2,s3,s4,s5);
                }            }
        }).show();
    }

    private void addViewAlternative() {
        View Alterview = getLayoutInflater().inflate(R.layout.valternative,null,false);
        EditText alter = (EditText) Alterview.findViewById(R.id.alter);
        ImageView delAlt = (ImageView) Alterview.findViewById(R.id.deleteAlt);
        delAlt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s1 = alter.getText().toString();
                removeViewAlter(Alterview,s1);
            }
        });
        alter.setCustomSelectionActionModeCallback(new EditVisionDoc.AbrCallback(alter));
        AlternativeList.addView(Alterview);
    }
    private void addViewAlternative(String sp1) {
        View Alterview = getLayoutInflater().inflate(R.layout.valternative,null,false);
        EditText alter = (EditText) Alterview.findViewById(R.id.alter);
        alter.setText(sp1);
        ImageView delAlt = (ImageView) Alterview.findViewById(R.id.deleteAlt);
        delAlt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s1 = alter.getText().toString();
                removeViewAlter(Alterview,s1);
            }
        });
        alter.setCustomSelectionActionModeCallback(new EditVisionDoc.AbrCallback(alter));
        AlternativeList.addView(Alterview);
    }

    private void removeViewAlter(View alterview, String s1) {
        AlternativeList.removeView(alterview);
        Snackbar.make(visRL,"The item is deleted", Snackbar.LENGTH_LONG).setAction("UNDO", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(s1.isEmpty()){
                    addViewAlternative();
                }
                else {
                    addViewAlternative(s1);
                }            }
        }).show();
    }

    private void addViewCapabilities() {
        View Capview = getLayoutInflater().inflate(R.layout.summary_of_capabilities,null,false);
        EditText benefit = (EditText) Capview.findViewById(R.id.vBenefit);
        EditText feature = (EditText) Capview.findViewById(R.id.vFeature);
        ImageView delCap = (ImageView) Capview.findViewById(R.id.deletecab);
        delCap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s1 = benefit.getText().toString();
                String s2 = feature.getText().toString();
                removeViewCapab(Capview,s1,s2);
            }
        });
        benefit.setCustomSelectionActionModeCallback(new EditVisionDoc.AbrCallback(benefit));
        feature.setCustomSelectionActionModeCallback(new EditVisionDoc.AbrCallback(feature));
        CapabilitiesList.addView(Capview);
    }

    private void addViewCapabilities(String s1 , String s2) {
        View Capview = getLayoutInflater().inflate(R.layout.summary_of_capabilities,null,false);
        EditText benefit = (EditText) Capview.findViewById(R.id.vBenefit);
        benefit.setText(s1);
        EditText feature = (EditText) Capview.findViewById(R.id.vFeature);
        feature.setText(s2);
        ImageView delCap = (ImageView) Capview.findViewById(R.id.deletecab);
        delCap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s1 = benefit.getText().toString();
                String s2 = feature.getText().toString();
                removeViewCapab(Capview,s1,s2);
            }
        });
        benefit.setCustomSelectionActionModeCallback(new EditVisionDoc.AbrCallback(benefit));
        feature.setCustomSelectionActionModeCallback(new EditVisionDoc.AbrCallback(feature));
        CapabilitiesList.addView(Capview);
    }

    private void removeViewCapab(View capview, String s1, String s2) {
        CapabilitiesList.removeView(capview);
        Snackbar.make(visRL,"The item is deleted", Snackbar.LENGTH_LONG).setAction("UNDO", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(s1.isEmpty() && s2.isEmpty()){
                    addViewCapabilities();
                }
                else {
                    addViewCapabilities(s1,s2);
                }            }
        }).show();
    }

    private void addViewAssump() {
        View Assumpview = getLayoutInflater().inflate(R.layout.assumptions,null,false);
        EditText assum = (EditText) Assumpview.findViewById(R.id.assump);
        ImageView delAssum = (ImageView) Assumpview.findViewById(R.id.deleteAssump);
        delAssum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s1 = assum.getText().toString();
                removeViewAssump(Assumpview,s1);
            }
        });
        assum.setCustomSelectionActionModeCallback(new EditVisionDoc.AbrCallback(assum));
        assumpList.addView(Assumpview);

    }

    private void addViewAssump(String sp1) {
        View Assumpview = getLayoutInflater().inflate(R.layout.assumptions,null,false);
        EditText assum = (EditText) Assumpview.findViewById(R.id.assump);
        assum.setText(sp1);
        ImageView delAssum = (ImageView) Assumpview.findViewById(R.id.deleteAssump);
        delAssum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s1 = assum.getText().toString();
                removeViewAssump(Assumpview,s1);
            }
        });
        assum.setCustomSelectionActionModeCallback(new EditVisionDoc.AbrCallback(assum));
        assumpList.addView(Assumpview);

    }

    private void removeViewAssump(View assumpview, String s1) {
        assumpList.removeView(assumpview);
        Snackbar.make(visRL,"The item is deleted", Snackbar.LENGTH_LONG).setAction("UNDO", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(s1.isEmpty()){
                    addViewAssump();
                }
                else {
                    addViewAssump(s1);
                }            }
        }).show();
    }

    private void addViewpFeature() {
        View featview = getLayoutInflater().inflate(R.layout.product_features,null,false);
        ArrayList<String> pri = new ArrayList<>();
        pri.add("Critical");
        pri.add("Important");
        pri.add("Useful");
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this,R.layout.list_item,pri);
        ArrayList<String> pri2 = new ArrayList<>();
        pri2.add("High");
        pri2.add("Medium");
        pri2.add("Low");
        ArrayAdapter<String> arrayAdapter2 = new ArrayAdapter<>(this,R.layout.list_item,pri2);
        EditText feature = (EditText) featview.findViewById(R.id.vpfeature);
        Spinner Priority = (Spinner) featview.findViewById(R.id.pspinner);
        Priority.setAdapter(arrayAdapter);
        Spinner Effort = (Spinner) featview.findViewById(R.id.espinner);
        Effort.setAdapter(arrayAdapter2);
        Spinner Risk = (Spinner) featview.findViewById(R.id.rspinner);
        Risk.setAdapter(arrayAdapter2);
        Spinner Stability = (Spinner) featview.findViewById(R.id.sspinner);
        Stability.setAdapter(arrayAdapter2);
        ImageView delProb = (ImageView) featview.findViewById(R.id.deleteprofeat);
        delProb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s1 = feature.getText().toString();
                String s2 = Priority.getSelectedItem().toString();
                String s3 = Effort.getSelectedItem().toString();
                String s4 = Risk.getSelectedItem().toString();
                String s5 = Stability.getSelectedItem().toString();
                removeViewpFeature(featview,s1,s2,s3,s4,s5);
            }
        });
        feature.setCustomSelectionActionModeCallback(new EditVisionDoc.AbrCallback(feature));
        pFeatureList.addView(featview);
    }

    private void addViewpFeature(String s1, String s2, String s3, String s4,String s5) {
        View featview = getLayoutInflater().inflate(R.layout.product_features,null,false);
        ArrayList<String> pri = new ArrayList<>();
        pri.add("Critical");
        pri.add("Important");
        pri.add("Useful");
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this,R.layout.list_item,pri);
        ArrayList<String> pri2 = new ArrayList<>();
        pri.add("High");
        pri.add("Medium");
        pri.add("Low");
        ArrayAdapter<String> arrayAdapter2 = new ArrayAdapter<>(this,R.layout.list_item,pri);
        EditText feature = (EditText) featview.findViewById(R.id.vpfeature);
        feature.setText(s1);
        Spinner Priority = (Spinner) featview.findViewById(R.id.pspinner);
        Priority.setAdapter(arrayAdapter);
        Priority.setSelection(arrayAdapter.getPosition(s2));
        Spinner Effort = (Spinner) featview.findViewById(R.id.espinner);
        Effort.setAdapter(arrayAdapter2);
        Priority.setSelection(arrayAdapter.getPosition(s3));
        Spinner Risk = (Spinner) featview.findViewById(R.id.rspinner);
        Risk.setAdapter(arrayAdapter2);
        Priority.setSelection(arrayAdapter.getPosition(s4));
        Spinner Stability = (Spinner) featview.findViewById(R.id.sspinner);
        Stability.setAdapter(arrayAdapter2);
        Priority.setSelection(arrayAdapter.getPosition(s5));
        ImageView delProb = (ImageView) featview.findViewById(R.id.deleteprofeat);
        delProb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s1 = feature.getText().toString();
                String s2 = Priority.getSelectedItem().toString();
                String s3 = Effort.getSelectedItem().toString();
                String s4 = Risk.getSelectedItem().toString();
                String s5 = Stability.getSelectedItem().toString();
                removeViewpFeature(featview,s1,s2,s3,s4,s5);
            }
        });
        feature.setCustomSelectionActionModeCallback(new EditVisionDoc.AbrCallback(feature));
        pFeatureList.addView(featview);
    }

    private void removeViewpFeature(View featview, String s1, String s2, String s3, String s4, String s5) {
        pFeatureList.removeView(featview);
        Snackbar.make(visRL,"The item is deleted", Snackbar.LENGTH_LONG).setAction("UNDO", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(s1.isEmpty() && s2.isEmpty() && s3.isEmpty() && s4.isEmpty() && s5.isEmpty()){
                    addViewpFeature();
                }
                else {
                    addViewpFeature(s1,s2,s3,s4,s5);
                }
            }
        }).show();
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
        ref.setCustomSelectionActionModeCallback(new EditVisionDoc.AbrCallback(ref));
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
        ref.setCustomSelectionActionModeCallback(new EditVisionDoc.AbrCallback(ref));
        refList.addView(refview);
    }
    private void removeViewRef(View view,String s1){
        refList.removeView(view);
        Snackbar.make(visRL,"The item is deleted", Snackbar.LENGTH_LONG).setAction("UNDO", new View.OnClickListener() {
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
                    for(Abbrev ab1 : explination){
                        if(ab1.getName().trim().toLowerCase().equals(ab.getName().trim().toLowerCase()))
                            flag = false;
                    }
                    if(flag)
                        explination.add(ab);
                    return true;

            }
            return false;
        }

        public void onDestroyActionMode(ActionMode mode) {

        }
    }

    public void SaveVision () {
        aBusinessOpportunity = new ArrayList<>();
        astakeholders = new ArrayList<>();
        ausers = new ArrayList<>();
        aAlternatives = new ArrayList<>();
        aAssumptions = new ArrayList<>();
        arefrences = new ArrayList<>();
        aprops = new ArrayList<>();
        vKeyNeed = new ArrayList<>();
        vPriorities = new ArrayList<>();
        vEffort = new ArrayList<>();
        vRisk = new ArrayList<>();
        vstability = new ArrayList<>();
        aCapabilities = new ArrayList<>();
        aprodfeat = new ArrayList<>();
        fAuth =FirebaseAuth.getInstance();
        fs =  FirebaseFirestore.getInstance();
        userID = fAuth.getCurrentUser().getUid();
        purpose = vpurpose.getText().toString();
        name = docNameVis.getText().toString();
        scope = vscope.getText().toString();
        prespictive = proOver.getText().toString();
        conclution = con.getText().toString();
        for (int i = 0; i < BusinessOpportList.getChildCount(); i++) {
            View v1 = BusinessOpportList.getChildAt(i);
            EditText buisness = (EditText) v1.findViewById(R.id.bopp);
            String buisopp = buisness.getText().toString().trim();
            aBusinessOpportunity.add(buisopp);
        }
        for (int i = 0; i < vBRV.getChildCount(); i++) {
            View v1 = vBRV.getChildAt(i);
            EditText buisness = (EditText) v1.findViewById(R.id.bopp);
            String buisopp = buisness.getText().toString().trim();
            aBusinessOpportunity.add(buisopp);
        }
        for (int i = 0; i < StakeholderList.getChildCount(); i++) {
            View v1 = StakeholderList.getChildAt(i);
            EditText stake = (EditText) v1.findViewById(R.id.StakeHolder);
            String buisopp = stake.getText().toString().trim();
            astakeholders.add(buisopp);
        }
        for (int i = 0; i < stakerv.getChildCount(); i++) {
            View v1 = stakerv.getChildAt(i);
            EditText stake = (EditText) v1.findViewById(R.id.StakeHolder);
            String buisopp = stake.getText().toString().trim();
            astakeholders.add(buisopp);
        }
        for (int i = 0; i < UserList.getChildCount(); i++) {
            View v1 = UserList.getChildAt(i);
            EditText user = (EditText) v1.findViewById(R.id.user);
            String users = user.getText().toString().trim();
            ausers.add(users);
        }
        for (int i = 0; i < userrv.getChildCount(); i++) {
            View v1 = userrv.getChildAt(i);
            EditText user = (EditText) v1.findViewById(R.id.user);
            String users = user.getText().toString().trim();
            ausers.add(users);
        }
        for (int i = 0; i < AlternativeList.getChildCount(); i++) {
            View v1 = AlternativeList.getChildAt(i);
            EditText alter = (EditText) v1.findViewById(R.id.alter);
            String alt = alter.getText().toString().trim();
            aAlternatives.add(alt);
        }
        for (int i = 0; i < altrv.getChildCount(); i++) {
            View v1 = altrv.getChildAt(i);
            EditText alter = (EditText) v1.findViewById(R.id.alter);
            String alt = alter.getText().toString().trim();
            aAlternatives.add(alt);
        }
        for (int i = 0; i < assumpList.getChildCount(); i++) {
            View v1 = assumpList.getChildAt(i);
            EditText assum = (EditText) v1.findViewById(R.id.assump);
            String assu = assum.getText().toString().trim();
            aAssumptions.add(assu);
        }
        for (int i = 0; i < assumprv.getChildCount(); i++) {
            View v1 = assumprv.getChildAt(i);
            EditText assum = (EditText) v1.findViewById(R.id.assump);
            String assu = assum.getText().toString().trim();
            aAssumptions.add(assu);
        }
        for (int i = 0; i < refList.getChildCount(); i++) {
            View v1 = refList.getChildAt(i);
            EditText ref = (EditText) v1.findViewById(R.id.ref);
            String sRef = ref.getText().toString().trim();
            arefrences.add(sRef);
        }
        for (int i = 0; i < refrv.getChildCount(); i++) {
            View v1 = refrv.getChildAt(i);
            EditText ref = (EditText) v1.findViewById(R.id.ref);
            String sRef = ref.getText().toString().trim();
            arefrences.add(sRef);
        }
        String pro = vProblemOF.getText().toString();
        String aff = vAffects.getText().toString();
        String imp = vImpactOF.getText().toString();
        String solu = vSolution.getText().toString();
        Problemstatment p = new Problemstatment(pro,aff,imp,solu);
        aprops.add(p);
        for (int i = 0; i < ProblemDecompList.getChildCount(); i++) {
            View v1 = ProblemDecompList.getChildAt(i);
            EditText probOf = (EditText) v1.findViewById(R.id.vProblemOF);
            String pf = probOf.getText().toString();
            EditText affect = (EditText) v1.findViewById(R.id.vAffects);
            String af = affect.getText().toString();
            EditText impact = (EditText) v1.findViewById(R.id.vImpactOF);
            String im = impact.getText().toString();
            EditText solution = (EditText) v1.findViewById(R.id.vSolution);
            String sol = solution.getText().toString();
            Problemstatment ps = new Problemstatment(pf,af,im,sol);
            aprops.add(ps);
        }
        for (int i = 0; i < probdecrv.getChildCount(); i++) {
            View v1 = probdecrv.getChildAt(i);
            EditText probOf = (EditText) v1.findViewById(R.id.vProblemOF);
            String pf = probOf.getText().toString();
            EditText affect = (EditText) v1.findViewById(R.id.vAffects);
            String af = affect.getText().toString();
            EditText impact = (EditText) v1.findViewById(R.id.vImpactOF);
            String im = impact.getText().toString();
            EditText solution = (EditText) v1.findViewById(R.id.vSolution);
            String sol = solution.getText().toString();
            Problemstatment ps = new Problemstatment(pf,af,im,sol);
            aprops.add(ps);
        }
        String f =  vFor.getText().toString();
        String w =  vWho.getText().toString();
        String ta = vTheApp.getText().toString();
        String t = vThat.getText().toString();
        String un = vUnlike.getText().toString();
        String opro = vOurProduct.getText().toString();
        aprodpos = new ProductPosition(f,w,ta,t,un,opro);

        String pc = pcritical.getText().toString();
        String pi =pimportant.getText().toString();
        String pu =puseful.getText().toString();
        VPriority vp1 = new VPriority("Critical",pc);
        VPriority vp2 = new VPriority("Important",pi);
        VPriority vp3 = new VPriority("Useful",pu);
        vPriorities.add(vp1);
        vPriorities.add(vp2);
        vPriorities.add(vp3);

        String eh =ehigh.getText().toString();
        String em =emed.getText().toString();
        String el =elow.getText().toString();
        VEffort ep1 = new VEffort("High",eh);
        VEffort ep2 = new VEffort("Medium",em);
        VEffort ep3 = new VEffort("Low",el);
        vEffort.add(ep1);
        vEffort.add(ep2);
        vEffort.add(ep3);

        String rh =rhigh.getText().toString();
        String rm =rmed.getText().toString();
        String rl =rlow.getText().toString();
        VRisk rp1 = new VRisk("High",rh);
        VRisk rp2 = new VRisk("Medium",rm);
        VRisk rp3 = new VRisk("Low",rl);
        vRisk.add(rp1);
        vRisk.add(rp2);
        vRisk.add(rp3);

        String sh =shigh.getText().toString();
        String sm =smed.getText().toString();
        String sl =slow.getText().toString();
        VStability sp1 = new VStability("High",sh);
        VStability sp2 = new VStability("Medium",sm);
        VStability sp3 = new VStability("Low",sl);
        vstability.add(sp1);
        vstability.add(sp2);
        vstability.add(sp3);

        for (int i = 0; i < UserNeedsList.getChildCount(); i++) {
            View v1 = UserNeedsList.getChildAt(i);
            EditText need = (EditText) v1.findViewById(R.id.vNeed);
            String n = need.getText().toString();
            Spinner Priority = (Spinner) v1.findViewById(R.id.spinner);
            String pr = Priority.getSelectedItem().toString();
            EditText Concerns = (EditText) v1.findViewById(R.id.vConcerns);
            String c = Concerns.getText().toString();
            EditText currsolution = (EditText) v1.findViewById(R.id.vCurrSolution);
            String cs = currsolution.getText().toString();
            EditText prsolution = (EditText) v1.findViewById(R.id.vPropSolution);
            String ps = prsolution.getText().toString();
            KeyNeedsStake vK = new KeyNeedsStake(n,pr,c,cs,ps);
            vKeyNeed.add(vK);
        }

        for (int i = 0; i < keyNeedrv.getChildCount(); i++) {
            View v1 = keyNeedrv.getChildAt(i);
            EditText need = (EditText) v1.findViewById(R.id.vNeed);
            String n = need.getText().toString();
            Spinner Priority = (Spinner) v1.findViewById(R.id.spinner);
            String pr = Priority.getSelectedItem().toString();
            EditText Concerns = (EditText) v1.findViewById(R.id.vConcerns);
            String c = Concerns.getText().toString();
            EditText currsolution = (EditText) v1.findViewById(R.id.vCurrSolution);
            String cs = currsolution.getText().toString();
            EditText prsolution = (EditText) v1.findViewById(R.id.vPropSolution);
            String ps = prsolution.getText().toString();
            KeyNeedsStake vK = new KeyNeedsStake(n,pr,c,cs,ps);
            vKeyNeed.add(vK);
        }

        for (int i = 0; i < pFeatureList.getChildCount(); i++) {
            View v1 = pFeatureList.getChildAt(i);
            EditText namef = (EditText) v1.findViewById(R.id.vpfeature);
            String pf = namef.getText().toString();
            Spinner prior = (Spinner) v1.findViewById(R.id.pspinner);
            String pr = prior.getSelectedItem().toString();
            Spinner effort = (Spinner) v1.findViewById(R.id.espinner);
            String pe = effort.getSelectedItem().toString();
            Spinner risk = (Spinner) v1.findViewById(R.id.rspinner);
            String prr = risk.getSelectedItem().toString();
            Spinner stability = (Spinner) v1.findViewById(R.id.sspinner);
            String ps = stability.getSelectedItem().toString();
            VProductFeature vK = new VProductFeature(pf,pr,pe,prr,ps);
            aprodfeat.add(vK);
        }

        for (int i = 0; i < pfeatrv.getChildCount(); i++) {
            View v1 = pfeatrv.getChildAt(i);
            EditText namef = (EditText) v1.findViewById(R.id.vpfeature);
            String pf = namef.getText().toString();
            Spinner prior = (Spinner) v1.findViewById(R.id.pspinner);
            String pr = prior.getSelectedItem().toString();
            Spinner effort = (Spinner) v1.findViewById(R.id.espinner);
            String pe = effort.getSelectedItem().toString();
            Spinner risk = (Spinner) v1.findViewById(R.id.rspinner);
            String prr = risk.getSelectedItem().toString();
            Spinner stability = (Spinner) v1.findViewById(R.id.sspinner);
            String ps = stability.getSelectedItem().toString();
            VProductFeature vK = new VProductFeature(pf,pr,pe,prr,ps);
            aprodfeat.add(vK);
        }

        for (int i = 0; i < CapabilitiesList.getChildCount(); i++) {
            View v1 = CapabilitiesList.getChildAt(i);
            EditText benefit = (EditText) v1.findViewById(R.id.vBenefit);
            String b = benefit.getText().toString();
            EditText feature = (EditText) v1.findViewById(R.id.vFeature);
            String fe = feature.getText().toString();
            VCapabilities vc = new VCapabilities(b,fe);
            aCapabilities.add(vc);
        }

        for (int i = 0; i < caprv.getChildCount(); i++) {
            View v1 = caprv.getChildAt(i);
            EditText benefit = (EditText) v1.findViewById(R.id.vBenefit);
            String b = benefit.getText().toString();
            EditText feature = (EditText) v1.findViewById(R.id.vFeature);
            String fe = feature.getText().toString();
            VCapabilities vc = new VCapabilities(b,fe);
            aCapabilities.add(vc);
        }

        vd = new VisionDocument(userID,purpose,name,scope,prespictive,conclution,aBusinessOpportunity,astakeholders,ausers,aAlternatives,aAssumptions,arefrences,aprops,explination,aprodpos,vPriorities,vEffort,vRisk,vstability,aCapabilities,vKeyNeed,aprodfeat);

    }
}