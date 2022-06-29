package com.example.donateapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class DonateActivity extends AppCompatActivity {

    ImageView FoodDonate,ClothesDonation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donate);


        FoodDonate=(ImageView)findViewById(R.id.food_donation);
        ClothesDonation=(ImageView)findViewById(R.id.clothes_donation);

        FoodDonate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DonateActivity.this,FormActivity.class);
                startActivity(intent);
            }
        });
        ClothesDonation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DonateActivity.this,ClothesDonateFormActivity.class);
                startActivity(intent);
            }
        });

    }
}