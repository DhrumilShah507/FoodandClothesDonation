package com.example.donateapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import javax.annotation.Nullable;

public class ProfileActivity extends AppCompatActivity {


    Button logout,ProChange;
    TextView fname,email,phone;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    StorageReference storageReference;
    String userId;
    ImageView pro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        pro=(ImageView)findViewById(R.id.profile);

        //PROFILE DISPLAY TEXTVIEW
        fname=(TextView)findViewById(R.id.profileName);
        email=(TextView)findViewById(R.id.profileEmail);
        phone=(TextView)findViewById(R.id.profilePhone);

        //LOG OUT BUTTON
        logout=(Button)findViewById(R.id.logOut);

        //
        ProChange=(Button)findViewById(R.id.changeProfileImg);

        //FIREBASE AUTHENTICATION INSTANCE
        fAuth= FirebaseAuth.getInstance();
        fStore= FirebaseFirestore.getInstance();
        storageReference= FirebaseStorage.getInstance().getReference();
        StorageReference profileRef=storageReference.child("users/"+fAuth.getCurrentUser().getUid()+"/profile.jpg");
        profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(pro);
            }
        });

        //TO GET USER ID FROM DATABASE
        FirebaseUser mFirebaseUser = fAuth.getCurrentUser();
        if(mFirebaseUser != null) {
            userId=fAuth.getCurrentUser().getUid(); //Do what you need to do with the id
        }


        //TO GET DATA PROVIDED BY USER FROM DATABASE
        final DocumentReference documentReference= fStore.collection("users").document(userId);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(DocumentSnapshot documentSnapshot,  FirebaseFirestoreException e) {
                try{

                    phone.setText(documentSnapshot.getString("Phone"));
                    fname.setText(documentSnapshot.getString("Name"));
                    email.setText(documentSnapshot.getString("Email"));



                }catch (Exception E){

                    Toast.makeText(ProfileActivity.this, "Error:"+ e+ "\nError:" + E, Toast.LENGTH_SHORT).show();

                }finally {

                }

                // pro.setImageURI(documentSnapshot.toObject());
            }
        });

        ProChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //
                Intent openGalleryIntent=new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(openGalleryIntent,1000);
            }
        });

        //


        //LOGOUT BUTTON

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {

                    FirebaseAuth.getInstance().signOut();
                   Intent i= new Intent(ProfileActivity.this,SignINActivity.class);
                   startActivity(i);
                }catch (Exception e){

                    Toast.makeText(ProfileActivity.this, "Error:"+ e, Toast.LENGTH_SHORT).show();

                }finally {

                }

            }
        });


    }

    //

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @androidx.annotation.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1000){
            if(resultCode== Activity.RESULT_OK){
                Uri imageUri=data.getData();
                // pro.setImageURI(imageUri);

                uploadImageToFirebase(imageUri);
            }
        }
    }

    private void uploadImageToFirebase(Uri imageUri) {
        //upload Image To Firebase Storage
        final StorageReference fileRef= storageReference.child("users/"+fAuth.getCurrentUser().getUid()+"/profile.jpg");
        fileRef.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Picasso.get().load(uri).into(pro);
                    }
                });
            }
        });

    }
}