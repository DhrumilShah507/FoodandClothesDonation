package com.example.donateapplication;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class SignUPActivity extends AppCompatActivity {
    ImageView Logo_img;
    Animation topAnim,BottomAnim;
    TextInputLayout regName, regEmail, regPhoneNo, regPassword;
    TextView DonateInd,Sign;
    Button regBtn, regToLoginBtn;
    Spinner Dis,Sta;
    String states,district;
    //FirebaseDatabase rootNode;
    // DatabaseReference reference;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String userId;
    CheckBox UserCh,NgoCh;
    ArrayAdapter<CharSequence> FoodType_adapter,District_adapter,States_adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_u_p);

        //Hooks to all xml elements in activity_sign_up.xml
        Logo_img=(ImageView)findViewById(R.id.app_logo);
        DonateInd=(TextView) findViewById(R.id.DonateIndiaTxt);

        //Animation
        topAnim= AnimationUtils.loadAnimation(this,R.anim.top_animation);
        BottomAnim= AnimationUtils.loadAnimation(this,R.anim.botto_animation);

        Sign=(TextView)findViewById(R.id.SigninTxt);

        regName =(TextInputLayout) findViewById(R.id.reg_name);
        regEmail = (TextInputLayout) findViewById(R.id.reg_email);
        regPhoneNo =(TextInputLayout)findViewById(R.id.reg_phoneNo);
        regPassword = (TextInputLayout)findViewById(R.id.reg_password);

        regBtn =(Button) findViewById(R.id.reg_btn);
        regToLoginBtn =(Button) findViewById(R.id.oldUser);
        UserCh=(CheckBox)findViewById(R.id.isUserCheckbox);
        NgoCh=(CheckBox)findViewById(R.id.isAdminCheckbox);

        Dis=(Spinner)findViewById(R.id.District_Spinner);
        Sta=(Spinner)findViewById(R.id.STATES_Spinner);




        //rootNode = (FirebaseDatabase) FirebaseDatabase.getInstance();
        fAuth=(FirebaseAuth) FirebaseAuth.getInstance();
        fStore=(FirebaseFirestore) FirebaseFirestore.getInstance();
        //  reference = (DatabaseReference) rootNode.getReference("users");

        States_adapter = ArrayAdapter.createFromResource(this,
                R.array.STATES, android.R.layout.simple_spinner_item);
        States_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Sta.setAdapter(States_adapter);
        Sta.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
        Dis.setAdapter(District_adapter);
        Dis.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                district = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        UserCh.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(UserCh.isChecked()){
                    NgoCh.setChecked(false);
                }
            }
        });

        NgoCh.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(NgoCh.isChecked()){
                    UserCh.setChecked(false);
                }
            }
        });







        regBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = regName.getEditText().getText().toString();
                String email = regEmail.getEditText().getText().toString();
                String phoneNo = regPhoneNo.getEditText().getText().toString();
                String password = regPassword.getEditText().getText().toString();



                // UserHelperClass helperClass = new UserHelperClass(name, username, email, phoneNo, password);

                if (TextUtils.isEmpty(name)) {
                    regName.setError("Name is required");
                    regName.requestFocus();
                    return;
                }
                else if(TextUtils.isEmpty(email)) {
                    regEmail.setError("Email is required");
                    regEmail.requestFocus();
                    return;
                }else if (TextUtils.isEmpty(password)) {
                    regPassword.setError("Password is required");
                    regPassword.requestFocus();
                    return;
                } else if (password.length() <= 6) {
                    regPassword.setError("Pasword must be >= 6 characters");
                    regPassword.requestFocus();
                    return;

                }else if (TextUtils.isEmpty(phoneNo)) {
                    regPhoneNo.setError("Phone number is required");
                    regPhoneNo.requestFocus();
                    return;
                } else if (phoneNo.length() != 10) {
                    regPhoneNo.setError("Enter valid mobile number");
                    regPhoneNo.requestFocus();
                    return;}
                else if(!(UserCh.isChecked() || NgoCh.isChecked())){
                        Toast.makeText(SignUPActivity.this, "Select account type!!!", Toast.LENGTH_SHORT).show();
                    }
                else {


                    // progressBar.setVisibility(View.VISIBLE);
                    //USER REGISTRATION TO THE FIREBASE
                    fAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {

                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if (task.isSuccessful()) {
                                //  progressBar.setVisibility(View.INVISIBLE);
                                Toast.makeText(SignUPActivity.this, "logged successfully", Toast.LENGTH_SHORT).show();
                                userId=fAuth.getCurrentUser().getUid();
                                DocumentReference documentReference=fStore.collection("users").document(userId);


                                if(UserCh.isChecked()){
                                    Map<String,Object> user= new HashMap<>();
                                    user.put("Name",name);
                                    user.put("Email",email);
                                    user.put("Phone",phoneNo);
                                    user.put("Password",password);
                                    user.put("User","1");
                                    documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {

                                            Log.d("TAG","onSuccess user Profile is create for " + userId);
                                        }
                                    });

                                    Intent intent = new Intent(SignUPActivity.this,HomeScreenActivity.class);
                                    startActivity(intent);


                                }


                                if(NgoCh.isChecked()){
                                    Map<String,Object> user= new HashMap<>();
                                    user.put("Name",name);
                                    user.put("Email",email);
                                    user.put("Phone",phoneNo);
                                    user.put("Password",password);
                                    user.put("District",district);
                                    user.put("States",states);
                                    user.put("NGO","1");
                                    documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {

                                            Log.d("TAG","onSuccess user Profile is create for " + userId);
                                        }
                                    });
                                    Intent intent = new Intent(SignUPActivity.this,NGO_HomeActivity.class);
                                    startActivity(intent);
                                }









                            } else {
                                // progressBar.setVisibility(View.INVISIBLE);
                                Toast.makeText(SignUPActivity.this, "Error:" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                            }
                        }

                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(SignUPActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();


                        }
                    });
                }
            }
        });




        regToLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    Intent intent = new Intent(SignUPActivity.this,SignINActivity.class);
                   startActivity(intent);
               /* Pair[] pairs= new Pair[9];
                pairs[0] = new Pair<View,String>(Logo_img,"Logo_Img");
                pairs[1] = new Pair<View,String>(DonateInd,"DonateInd");
                pairs[2] = new Pair<View,String>(Sign,"logo_SignIn");
                pairs[3] = new Pair<View,String>(regUsername,"username_tran");
                pairs[4] = new Pair<View,String>(regPassword,"password_tran");
                pairs[5] = new Pair<View,String>(regBtn,"Signbutton_tran");
                pairs[6] = new Pair<View,String>(regToLoginBtn,"NewUser");
                pairs[7] = new Pair<View,String>(regName,"FullName");
                pairs[8] = new Pair<View,String>(regPhoneNo,"PhoneNo");

                try{




                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                        ActivityOptions options=ActivityOptions.makeSceneTransitionAnimation(SignUPActivity.this,pairs);
                        startActivity(intent,options.toBundle());
                    }


                }catch (Exception e){
                    Toast.makeText(SignUPActivity.this, "ERROR!!!"+e.getMessage(), Toast.LENGTH_SHORT).show();


            }finally {

                }*/
                }
        });
    }
}