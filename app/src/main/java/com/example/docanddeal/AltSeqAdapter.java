package com.example.docanddeal;

import static androidx.core.app.ActivityCompat.startActivityForResult;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.CallSuper;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AltSeqAdapter extends RecyclerView.Adapter implements OnIntentReceived {
    ArrayList<Alternative> arr;
    Context context;
    private static final int REQUEST_CODE=101;
    Uri img;
    int globalpos;
    RecyclerView.ViewHolder golabalholder;
    ProjectC pro;

    public AltSeqAdapter(ArrayList<Alternative> arr, Context context, ProjectC pro) {
        this.arr = arr;
        this.context = context;
        this.pro =pro;

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.upload_seq_item,parent,false);
        return new AltSeqAdapter.AltSeqViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        ((AltSeqAdapter.AltSeqViewHolder ) holder).uploadaltnseq.setText("Upload Alternative's "+arr.get(position).getAltnum() +" Sequence Diagram");
        ((AltSeqAdapter.AltSeqViewHolder ) holder).uploadaltnseq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                globalpos = position;
                System.out.println("/////g "+globalpos);
                golabalholder = holder;
                openFileChooser();

            }
        });
        if(arr.get(position).getAltseq()!=null)
            Picasso.get().load(Uri.parse(arr.get(position).getAltseq().getImagepath())).into(((AltSeqAdapter.AltSeqViewHolder )holder).altseqpic);
    }

    private void openFileChooser() {
        Intent in = new Intent();
        in.setType("image/*");
        in.setAction(Intent.ACTION_GET_CONTENT);
        ((Activity) context).startActivityForResult(in,REQUEST_CODE);

    }

    @Override
    public int getItemCount() {
        return arr.size();
    }

    @Override
    public void onIntent(Intent i, int resultCode) {
        img = i.getData();
        Picasso.get().load(img).into(((AltSeqAdapter.AltSeqViewHolder )golabalholder).altseqpic);
        String imagename = System.currentTimeMillis() + "." + getExtension(img);
        ImagePath img1 = new ImagePath(imagename,img +"");
        arr.get(globalpos).setAltseq(img1);
        sendImage(img,imagename,"AltSeq");
    }
    private String getExtension(Uri uri) {
//        String sIOimageurl = "" + uri;
//        return sIOimageurl.substring(sIOimageurl.lastIndexOf(".") + 1);
        ContentResolver CR = context.getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(CR.getType(uri));
    }

    private void sendImage(Uri uri, String Imagename,String phototype){
        FirebaseAuth fAuth;
        FirebaseFirestore fs;
        String userID;
        StorageReference sr;
        fAuth = FirebaseAuth.getInstance();
        fs = FirebaseFirestore.getInstance();
        sr = FirebaseStorage.getInstance().getReference();
        StorageTask st;
        StorageReference sr1;
        sr1 = sr.child(Imagename);
        st = sr1.putFile(uri);
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
                if (task.isSuccessful()) {
                    Uri dd = (Uri) task.getResult();
                    fs.collection("Images").add(new ImageUri(Imagename,(dd+""), pro.getPid(),phototype)).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        });

    }

    private static class AltSeqViewHolder extends RecyclerView.ViewHolder{
        Button uploadaltnseq;
        ImageView altseqpic;

        View v;
        public AltSeqViewHolder(@NonNull View itemView) {
            super(itemView);
            v = itemView;
            uploadaltnseq = (Button) itemView.findViewById(R.id.uploadaltnseq);
            altseqpic = (ImageView) itemView.findViewById(R.id.altseqpic);
        }
    }


}

