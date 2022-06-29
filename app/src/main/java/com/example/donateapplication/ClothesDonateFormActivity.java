package com.example.donateapplication;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.DialogFragment;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import javax.annotation.Nullable;

public class ClothesDonateFormActivity extends AppCompatActivity
        implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener{

    EditText D_Name, sub, Add, F_Name, D, T, Que, Des;
    Button NextBtn;
    Spinner C_Type,Dist,States;
    String userId, email,clothtype,dist,states;
    ImageView DatePicker,TimePicker;
    private FirebaseAuth fAuth = FirebaseAuth.getInstance();
    private FirebaseFirestore fStore = FirebaseFirestore.getInstance();
    private CollectionReference UserRef = fStore.collection("users");
    private CollectionReference NGORef = fStore.collection("NGO");
    FusedLocationProviderClient fusedLocationProviderClient;
    ArrayAdapter<CharSequence> ClothType_adapter,District_adapter,States_adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clothes_donate_form);


        //hooks
        Dist=(Spinner) findViewById(R.id.District);
        States=(Spinner)findViewById(R.id.STATES);
        D_Name = (EditText) findViewById(R.id.DonorName);
        sub = (EditText) findViewById(R.id.Title);
        Add = (EditText) findViewById(R.id.Address);
        C_Type = (Spinner) findViewById(R.id.ClothTypeSpinner);
        F_Name = (EditText) findViewById(R.id.FoodName);
        D = (EditText) findViewById(R.id.Date);
        T = (EditText) findViewById(R.id.Time);
        Que = (EditText) findViewById(R.id.Quantity);
        Des = (EditText) findViewById(R.id.Description);
        NextBtn = (Button) findViewById(R.id.NextBtn);

        DatePicker=(ImageView)findViewById(R.id.Date_picker);
        TimePicker=(ImageView)findViewById(R.id.Time_picker);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);


        TimePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DialogFragment timePicker = new TimePickerFragment();
                timePicker.show(getSupportFragmentManager(), "time picker");

            }
        });

        DatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DialogFragment datePicker = new DatePickerFragment();
                datePicker.show(getSupportFragmentManager(), "date picker");

            }
        });


        if (ActivityCompat.checkSelfPermission(ClothesDonateFormActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            showLocation();

        } else {
            ActivityCompat.requestPermissions(ClothesDonateFormActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 0);
        }


        //TO GET USER ID FROM DATABASE
        FirebaseUser mFirebaseUser = fAuth.getCurrentUser();
        if (mFirebaseUser != null) {
            userId = fAuth.getCurrentUser().getUid(); //Do what you need to do with the id
        }


        //TO GET DATA PROVIDED BY USER FROM DATABASE
        final DocumentReference documentReference = fStore.collection("users").document(userId);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {

                D_Name.setText(documentSnapshot.getString("Name"));//get name of current user
                email = documentSnapshot.getString("Email");
                // pass= documentSnapshot.getString("Password");


            }
        });

        ClothType_adapter = ArrayAdapter.createFromResource(this,
                R.array.ClothType, android.R.layout.simple_spinner_item);
        ClothType_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        C_Type.setAdapter(ClothType_adapter);
        C_Type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                clothtype = parent.getItemAtPosition(position).toString();


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        States_adapter = ArrayAdapter.createFromResource(this,
                R.array.STATES, android.R.layout.simple_spinner_item);
        States_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        States.setAdapter(States_adapter);
        States.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                states = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        District_adapter = ArrayAdapter.createFromResource(this,
                R.array.District, android.R.layout.simple_spinner_item);
        District_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Dist.setAdapter(District_adapter);
        Dist.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                dist = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



        NextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String name = D_Name.getText().toString().trim();
                String title = sub.getText().toString().trim();
                String add = Add.getText().toString().trim();
                String foodname = F_Name.getText().toString().trim();
                String pickupdate = D.getText().toString().trim();
                String pickuptime = T.getText().toString().trim();
                String quantity = Que.getText().toString().trim();
                String description = Des.getText().toString().trim();

                if (TextUtils.isEmpty(title)) {
                    sub.setError("Please Enter Any Title!!!");
                    sub.requestFocus();
                    return;
                }
                else if(TextUtils.isEmpty(name)) {
                    D_Name.setError("Please Enter Your Name!!!");
                    D_Name.requestFocus();
                    return;
                }else if (TextUtils.isEmpty(add)) {
                    Add.setError("Please Enter proper pickup address!!!");
                    Add.requestFocus();
                    return;
                } else if (TextUtils.isEmpty(foodname)) {
                    F_Name.setError("Please Enter Food Name!!!");
                    F_Name.requestFocus();
                    return;
                }
                else if (TextUtils.isEmpty(pickupdate)) {
                    D.setError("Please enter pickup date!!!");
                    D.requestFocus();
                    return;
                }
                else if (TextUtils.isEmpty(pickuptime)) {
                    T.setError("Please enter pickup time!!!");
                    T.requestFocus();
                    return;
                }
                else if (TextUtils.isEmpty(description)) {
                    Des.setError("Enter any description OR write NA");
                    Des.requestFocus();
                    return;
                }
                else if (TextUtils.isEmpty(description)) {
                    Des.setError("Enter any description OR write NA");
                    Des.requestFocus();
                    return;
                }else if (TextUtils.isEmpty(quantity)) {
                    Que.setError("");
                    Que.requestFocus();
                    return;
                }
                else {

                    AlertDialog.Builder ConfomationDialog = new AlertDialog.Builder(v.getContext());
                    ConfomationDialog.setTitle("Are you surely wanna to donate?");

                    ConfomationDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            model mod = new model(name, title, email, add, foodname, clothtype, pickupdate, pickuptime, quantity, description,dist,states);


                            //add a new collection into firebase
                            UserRef.document(userId).collection("History").add(mod);
                            NGORef.document(dist).collection("Request List").add(mod);

                            //*    Intent intent = new Intent(ClothesDonateFormActivity.this,PicActivity.class);
                            //  startActivity(intent);


                            //send mail to the ngo
                            sendMail();

                        }
                    });
                    ConfomationDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //CLOSE THE DIALOG
                        }
                    });
                    ConfomationDialog.create().show();


                    //temparary variabale for store data



                }

            }

            private void sendMail() {
                String recipientList = "19it137@charusat.edu.in";
                String[] recipients = recipientList.split(",");
                String subject = sub.getText().toString();
                String message = "Doner name: " + D_Name.getText().toString() + "\n\n" + "Address: "
                        + Add.getText().toString() + "\n\n" + "Food Type: " + clothtype + "\n" + "Name oF Food: " + F_Name.getText().toString() + "\n" + "Food Quantity: "
                        + Que.getText().toString() + "\n\n" + "Pick Details: " + "\n" + "Date: " + D.getText().toString() + "\n" + "Time: " + T.getText().toString() + "\n\n"+"\nState: "+states+"\nDistrict: "+dist+ "\n\nDescription: " + "\n" + Des.getText().toString();
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.putExtra(Intent.EXTRA_EMAIL, recipients);
                intent.putExtra(Intent.EXTRA_SUBJECT, subject);
                intent.putExtra(Intent.EXTRA_TEXT, message);
                intent.setType("message/rfc822");
                startActivity(Intent.createChooser(intent, "Choose an email client"));
            }

        });
        
    }

    private void showLocation() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                Location location=task.getResult();
                if(location!=null){

                    Geocoder geocoder=new Geocoder(ClothesDonateFormActivity.this, Locale.getDefault());

                    try{
                        List<Address> addressesList=geocoder.getFromLocation(location.getLatitude(),
                                location.getLongitude(),1);
                        Add.setText(addressesList.get(0).getAddressLine(0));

                        // Add.setText(addressesList.get(0).);


                    }catch (Exception e){

                        Toast.makeText(ClothesDonateFormActivity.this, "Error:" +e, Toast.LENGTH_SHORT).show();

                    }

                }
                else{

                    Toast.makeText(ClothesDonateFormActivity.this, "Location null error", Toast.LENGTH_SHORT).show();

                }



            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Toast.makeText(ClothesDonateFormActivity.this, "Error:"+e, Toast.LENGTH_SHORT).show();

            }
        });



    }

    @Override
    public void onDateSet(android.widget.DatePicker view, int year, int month, int date) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DATE, date);
        String currentDateString = DateFormat.getDateInstance(DateFormat.FULL).format(c.getTime());

        D.setText(currentDateString);
    }

    @Override
    public void onTimeSet(android.widget.TimePicker view, int hourOfDay, int minute) {

        T.setText( "Hour: "+hourOfDay + " Minute: " + minute);
    }
}