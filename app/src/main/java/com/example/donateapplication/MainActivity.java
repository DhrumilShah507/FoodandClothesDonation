package com.example.donateapplication;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Pair;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity {

    private static int SPLASH_SCREEN = 3000;
    Animation topAnim,BottomAnim;
    ImageView Logo;
    TextView Wel,Donate;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Animation
        topAnim= AnimationUtils.loadAnimation(this,R.anim.top_animation);
        BottomAnim= AnimationUtils.loadAnimation(this,R.anim.botto_animation);

        //hooks
        Logo=findViewById(R.id.app_logo);
        Wel=findViewById(R.id.WelcomeTxt);
        Donate=findViewById(R.id.DonateIndiaTxt);
       fAuth=(FirebaseAuth) FirebaseAuth.getInstance();
        fStore=(FirebaseFirestore) FirebaseFirestore.getInstance();

        Logo.setAnimation(topAnim);
        Wel.setAnimation(topAnim);
        Donate.setAnimation(BottomAnim);



            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

                    //IF USER ALREADY SIGNED IN
                    if(fAuth.getCurrentUser() != null) {

                        DocumentReference documentReference=fStore.collection("users").document(fAuth.getCurrentUser().getUid());
                        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {

                                if(documentSnapshot.getString("User")!= null){

                                    Intent intent = new Intent(MainActivity.this,HomeScreenActivity.class);
                                    startActivity(intent);
                                    finish();

                                }
                                else if(documentSnapshot.getString("NGO")!= null) {

                                    Intent intent = new Intent(MainActivity.this, NGO_HomeActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                                else{
                                    Intent intent = new Intent(MainActivity.this,SignINActivity.class);

                                    Pair[] pairs=new Pair[2];
                                    pairs[0] = new Pair<View,String>(Logo,"Logo_Img");
                                    pairs[1] = new Pair<View,String>(Logo,"DonateInd");

                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                        ActivityOptions options=ActivityOptions.makeSceneTransitionAnimation(MainActivity.this,pairs);
                                        startActivity(intent,options.toBundle());
                                        finish();
                                    }

                                }


                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                                Intent intent = new Intent(MainActivity.this,SignINActivity.class);

                                Pair[] pairs=new Pair[2];
                                pairs[0] = new Pair<View,String>(Logo,"Logo_Img");
                                pairs[1] = new Pair<View,String>(Logo,"DonateInd");

                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                    ActivityOptions options=ActivityOptions.makeSceneTransitionAnimation(MainActivity.this,pairs);
                                    startActivity(intent,options.toBundle());
                                    finish();
                                }



                            }
                        });




                    }
                    else
                    {

                        Intent intent = new Intent(MainActivity.this,SignINActivity.class);

                        Pair[] pairs=new Pair[2];
                        pairs[0] = new Pair<View,String>(Logo,"Logo_Img");
                        pairs[1] = new Pair<View,String>(Logo,"DonateInd");

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            ActivityOptions options=ActivityOptions.makeSceneTransitionAnimation(MainActivity.this,pairs);
                            startActivity(intent,options.toBundle());
                            finish();
                        }

                    }





                }
            },SPLASH_SCREEN);







    }
}