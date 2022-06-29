package com.example.donateapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class HomeScreenActivity extends AppCompatActivity {

    Animation topAnim,BottomAnim;
    ImageView pro,Donate,MyDonation;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        //Animation
        topAnim= AnimationUtils.loadAnimation(this,R.anim.top_animation);
        BottomAnim= AnimationUtils.loadAnimation(this,R.anim.botto_animation);

        //HOMEPAGE ICONS
        pro=(ImageView) findViewById(R.id.profile_imgbtn);
        Donate=(ImageView) findViewById(R.id.Donate_imgbtn);
        MyDonation=(ImageView) findViewById(R.id.MyDonation_btn);


        //PROFILE ICON ON CLICK EVENT
        pro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeScreenActivity.this,ProfileActivity.class);
                startActivity(intent);
            }
        });

        Donate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeScreenActivity.this,DonateActivity.class);
                startActivity(intent);
            }
        });

        MyDonation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeScreenActivity.this,MyDonationActivity.class);
                startActivity(intent);
            }
        });





    }
}