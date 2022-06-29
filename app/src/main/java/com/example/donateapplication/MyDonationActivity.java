package com.example.donateapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class MyDonationActivity extends AppCompatActivity {

    RecyclerView recview;
    ArrayList<model> datalist;
    myadepter adepter;
    FirebaseFirestore db;
    String userId;
    FirebaseAuth fAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_donation);

        recview=(RecyclerView)findViewById(R.id.recview);
        recview.setLayoutManager(new LinearLayoutManager(this));
        datalist=new ArrayList<>();


        adepter=new myadepter(datalist,getApplicationContext());
        recview.setAdapter(adepter);

        fAuth=(FirebaseAuth) FirebaseAuth.getInstance();
        userId=fAuth.getCurrentUser().getUid();

        db=FirebaseFirestore.getInstance();
        db.collection("users").document(userId).collection("History").get()
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
                });

    }
}