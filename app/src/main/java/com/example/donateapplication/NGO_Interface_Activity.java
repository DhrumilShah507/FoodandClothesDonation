package com.example.donateapplication;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

public class NGO_Interface_Activity extends AppCompatActivity {

    RecyclerView recview;
    ArrayList<model> datalist;
    myadepter adepter;
    FirebaseFirestore db;
    String userId;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_n_g_o__interface_);



        recview=(RecyclerView)findViewById(R.id.recview);
        recview.setLayoutManager(new LinearLayoutManager(this));
        datalist=new ArrayList<>();

        adepter=new myadepter(datalist,getApplicationContext());
        recview.setAdapter(adepter);

        fAuth=FirebaseAuth.getInstance();
        fStore=FirebaseFirestore.getInstance();

        //TO GET USER ID FROM DATABASE
        FirebaseUser mFirebaseUser = fAuth.getCurrentUser();
        if (mFirebaseUser != null) {
            userId = fAuth.getCurrentUser().getUid(); //Do what you need to do with the id
        }

        DocumentReference documentReference = fStore.collection("users").document(userId);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {

               String dist=documentSnapshot.getString("District");//get name of current user
               // Toast.makeText(NGO_Interface_Activity.this, dist, Toast.LENGTH_SHORT).show();

                db=FirebaseFirestore.getInstance();
                try {

                    db.collection("NGO").document(dist).collection("Request List").get()
                            .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                @Override
                                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                    List<DocumentSnapshot> list=queryDocumentSnapshots.getDocuments();

                                    for(DocumentSnapshot d:list){
                                        model obj=d.toObject(model.class);
                                        datalist.add(obj);
                                        //Adepter
                                    }

                                        adepter.notifyDataSetChanged();

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(NGO_Interface_Activity.this, "Error:"+e, Toast.LENGTH_SHORT).show();
                        }
                    });

                }catch (Exception E){

                    Toast.makeText(NGO_Interface_Activity.this, "ERROR:"+E, Toast.LENGTH_SHORT).show();

                }



            }
        });

    }
}