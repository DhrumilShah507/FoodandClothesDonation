package com.example.donateapplication;

import android.app.ActivityOptions;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class SignINActivity extends AppCompatActivity {

    ImageView Logo_img;
    TextView DonateInd,Sign;
    TextInputLayout EmailId,Pwd;
    Button SignIn,New,ForgotPwd;

    FirebaseFirestore fStore;
    FirebaseAuth fAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_i_n);

        //HOOKS

        Logo_img=(ImageView)findViewById(R.id.app_logo);
        DonateInd=(TextView)findViewById(R.id.DonateIndiaTxt);
        Sign=(TextView)findViewById(R.id.SigninTxt);
        EmailId=(TextInputLayout) findViewById(R.id.reg_email);
        Pwd=(TextInputLayout) findViewById(R.id.reg_password);
        SignIn=(Button) findViewById(R.id.reg_btn);
        New=(Button)findViewById(R.id.NewUser);
        ForgotPwd=(Button)findViewById(R.id.forgot);

        fAuth=(FirebaseAuth) FirebaseAuth.getInstance();
        fStore=(FirebaseFirestore) FirebaseFirestore.getInstance();

        //FOR OLD USER
        //SIGN IN BUTTON ON CLICK EVENT
        SignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // progressBar.setVisibility(View.VISIBLE);

                String email = EmailId.getEditText().getText().toString();
                String pass = Pwd.getEditText().getText().toString();
                String user = "user";

                if(TextUtils.isEmpty(email)) {
                    EmailId.setError("Email is required");
                    EmailId.requestFocus();
                    return;
                }else if (TextUtils.isEmpty(pass)) {
                    Pwd.setError("Password is required");
                    Pwd.requestFocus();
                    return;
                } else if (pass.length() <= 6) {
                    Pwd.setError("Pasword must be >= 6 characters");
                    Pwd.requestFocus();
                    return;
                }else{


                    //USER SING IN TO THE FIREBASE
                    fAuth.signInWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                //progressBar.setVisibility(View.INVISIBLE);
                                Toast.makeText(SignINActivity.this, "logged successfully", Toast.LENGTH_SHORT).show();
                                //VIEW ANIMATION
                               // progressBar.setVisibility(View.INVISIBLE);
                                DocumentReference documentReference=fStore.collection("users").document(fAuth.getCurrentUser().getUid());

                                documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                    @Override
                                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                                        Log.d("TAG","ON_SUCESS"+documentSnapshot.getData());

                                        if(documentSnapshot.getString("User")!= null){

                                            Intent intent = new Intent(SignINActivity.this,HomeScreenActivity.class);
                                            startActivity(intent);


                                        }
                                        if(documentSnapshot.getString("NGO")!= null){

                                            Intent intent = new Intent(SignINActivity.this,NGO_HomeActivity.class);
                                            startActivity(intent);


                                        }

                                    }
                                });



                            } else {
                                //progressBar.setVisibility(View.INVISIBLE);
                                Toast.makeText(SignINActivity.this, "Erorr:"+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                }
            }
        });


        //TO RESET PASSWORD//
        //FORGET PASSWORD ON CLICK EVENT
        ForgotPwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText resetMail= new EditText(v.getContext());
                AlertDialog.Builder passwordResetDialog = new AlertDialog.Builder(v.getContext());
                passwordResetDialog.setTitle("Reset Password");
                passwordResetDialog.setMessage("Enter Your Email To Received Reset Link");
                passwordResetDialog.setView(resetMail);

                passwordResetDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //EXTRACT THE EMAIL AND SEND RESET LINK
                        String mail = resetMail.getText().toString();
                        fAuth.sendPasswordResetEmail(mail).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(SignINActivity.this, "Reset Link sent To your Email", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(SignINActivity.this, "Error:Reset Link Is Not Sent"+ e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
                passwordResetDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //CLOSE THE DIALOG
                    }
                });
                passwordResetDialog.create().show();


            }
        });


        New.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(SignINActivity.this,SignUPActivity.class);

                Pair[] pairs=new Pair[7];
                pairs[0] = new Pair<View,String>(Logo_img,"Logo_Img");
                pairs[1] = new Pair<View,String>(DonateInd,"DonateInd");
                pairs[2] = new Pair<View,String>(Sign,"logo_SignIn");
                pairs[3] = new Pair<View,String>(EmailId,"Email");
                pairs[4] = new Pair<View,String>(Pwd,"password_tran");
                pairs[5] = new Pair<View,String>(SignIn,"Signbutton_tran");
                pairs[6] = new Pair<View,String>(New,"NewUser");



                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    ActivityOptions options=ActivityOptions.makeSceneTransitionAnimation(SignINActivity.this,pairs);
                    startActivity(intent,options.toBundle());
                }

            }
        });


    }
}