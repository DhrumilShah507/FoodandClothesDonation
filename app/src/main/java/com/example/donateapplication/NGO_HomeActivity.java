package com.example.donateapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class NGO_HomeActivity extends AppCompatActivity {

    ImageView pro,ReqList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_n_g_o__home);

        pro=(ImageView)findViewById(R.id.profile_img);
        ReqList=(ImageView)findViewById(R.id.ReqList_img);

        //PROFILE ICON ON CLICK EVENT
        pro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NGO_HomeActivity.this,ProfileActivity.class);
                startActivity(intent);
            }
        });
        //LIST ICON ON CLICK EVENT
        ReqList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NGO_HomeActivity.this,NGO_Interface_Activity.class);
                startActivity(intent);
            }
        });

    }
}