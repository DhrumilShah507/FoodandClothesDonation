package com.example.donateapplication;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import de.hdodenhof.circleimageview.CircleImageView;

public class DataDisplayActivity extends AppCompatActivity {

    CircleImageView imge;
    Button FatchBtn;
    TextView name, title, email, add, foodname, foodtype, pickupdate, pickuptime, quantity,state,district, description;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_display);
        FatchBtn=(Button)findViewById(R.id.fatchLocationBtn);


        imge=(CircleImageView)findViewById(R.id.circleImage);
        name=(TextView)findViewById(R.id.Name_);
        email=(TextView)findViewById(R.id.Email_);
        title=(TextView)findViewById(R.id.Title_);
        add=(TextView)findViewById(R.id.Add_);
        foodname=(TextView)findViewById(R.id.Fname_);
        foodtype=(TextView)findViewById(R.id.Ftype_);
        pickupdate=(TextView)findViewById(R.id.Date_);
        pickuptime=(TextView)findViewById(R.id.Time_);
        quantity=(TextView)findViewById(R.id.Que_);
        description=(TextView)findViewById(R.id.Desc_);

        state=(TextView)findViewById(R.id.State_);
        district=(TextView)findViewById(R.id.Dist_);


        //  imge.setImageResource(getIntent().getIntExtra("imgname",0));
        name.setText(getIntent().getStringExtra("Name"));
        email.setText(getIntent().getStringExtra("Email"));
        title.setText(getIntent().getStringExtra("Title"));
        add.setText(getIntent().getStringExtra("ADD"));
        foodtype.setText(getIntent().getStringExtra("FoodType"));
        foodname.setText(getIntent().getStringExtra("FoodName"));
        pickupdate.setText(getIntent().getStringExtra("PickUpDate"));
        pickuptime.setText(getIntent().getStringExtra("PickUpTime"));
        quantity.setText(getIntent().getStringExtra("Quantity"));
        state.setText(getIntent().getStringExtra("States"));
        district.setText(getIntent().getStringExtra("District"));


        description.setText(getIntent().getStringExtra("Description"));

        FatchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder ConfomationDialog = new AlertDialog.Builder(v.getContext());
                ConfomationDialog.setTitle("Are you ready to collect the donation?");


                ConfomationDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Toast.makeText(DataDisplayActivity.this, "Fetching Address:"+getIntent().getStringExtra("ADD"), Toast.LENGTH_SHORT).show();

                        Intent intent=new Intent(Intent.ACTION_VIEW,
                                Uri.parse("google.navigation:q="+ getIntent().getStringExtra("ADD")));
                        intent.setPackage("com.google.android.apps.maps");
                        //   intent.putExtra(Intent.EXTRA_SARCH,"Ahemdabad");
                        //  Intent chooser=Intent.createChooser(intent,"Launch Maps");
                        if(intent.resolveActivity(getPackageManager())!=null){
                            startActivity(intent);
                        }



                    }
                });
                ConfomationDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //CLOSE THE DIALOG
                    }
                });
                ConfomationDialog.create().show();




            }
        });


    }
}