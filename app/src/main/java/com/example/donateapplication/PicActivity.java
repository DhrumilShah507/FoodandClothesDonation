 package com.example.donateapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

 public class PicActivity extends AppCompatActivity {

    ImageView img;
    Button browse,upload;
    Uri filepath;
    List<String> files;
    Bitmap bitmap;
     FirebaseStorage storage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pic);

        img=(ImageView)findViewById(R.id.demoimage);
        browse=(Button)findViewById(R.id.browsebtn);
        upload=(Button)findViewById(R.id.uploadebtn);
        storage=FirebaseStorage.getInstance();

        files=new ArrayList<>();

        browse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dexter.withActivity(PicActivity.this)
                        .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                        .withListener(new PermissionListener() {
                            @Override
                            public void onPermissionGranted(PermissionGrantedResponse response) {

                                Intent intent = new Intent(Intent.ACTION_PICK);
                                intent.setType("image/*");
                                startActivityForResult(Intent.createChooser(intent,"Please select image"),1);


                            }

                            @Override
                            public void onPermissionDenied(PermissionDeniedResponse response) {

                            }

                            @Override
                            public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                                token.continuePermissionRequest();
                            }
                        }).check();
            }

        });




    }



     @Override
     protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
         super.onActivityResult(requestCode, resultCode, data);

         if(requestCode==1){
             Toast.makeText(this, "Here", Toast.LENGTH_SHORT).show();

            filepath=data.getData();
            InputStream inputStream= null;
            try {
                inputStream = getContentResolver().openInputStream(filepath);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            bitmap= BitmapFactory.decodeStream(inputStream);
             Toast.makeText(this, bitmap.toString(), Toast.LENGTH_SHORT).show();

             img.setImageBitmap(bitmap);
            uploadtofirebase();

            //  try {


           // }catch (Exception e){

          //      Toast.makeText(this, "Error: "+ e, Toast.LENGTH_SHORT).show();

       //     }
        }
     }


     private void uploadtofirebase()
     {
        final ProgressDialog dialog=new ProgressDialog(this);
        dialog.setTitle("File Uploader");
         dialog.show();


         StorageReference uploder=storage.getReference().child("users");
         uploder.putFile(filepath)
                 .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                     @Override
                     public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                         String uri = storage.getReference().child("users").getDownloadUrl().toString();

                         dialog.dismiss();
                         Toast.makeText(PicActivity.this, "Uri"+ uri, Toast.LENGTH_SHORT).show();//File uploaded

                     }
                 }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
             @Override
             public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {


                 float percent=(100*snapshot.getBytesTransferred())/snapshot.getTotalByteCount();
                dialog.setMessage("Uploded:"+(int)percent+"%");

             }
         }).addOnFailureListener(new OnFailureListener() {
             @Override
             public void onFailure(@NonNull Exception e) {
                 Toast.makeText(PicActivity.this, "Error: "+ e, Toast.LENGTH_SHORT).show();
             }
         });

     }

 }