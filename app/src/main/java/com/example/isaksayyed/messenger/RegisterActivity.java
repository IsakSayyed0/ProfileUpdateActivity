package com.example.isaksayyed.messenger;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    Button regsiterBtn;
    private static final int pick_code = 102;
    EditText emailTxt, username, passwordTxt;
    ProgressDialog progressBar;

    //Firebase
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;

    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        regsiterBtn = findViewById(R.id.registerButton);
        emailTxt = findViewById(R.id.emailTxt);
        passwordTxt = findViewById(R.id.passwordTxt);
        username = findViewById(R.id.userTxt);
        progressBar = new ProgressDialog(this);

        //Firebasw Auth
        mAuth = FirebaseAuth.getInstance();//firebase instance



        regsiterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String name = username.getText().toString();
                String email = emailTxt.getText().toString();
                String password = passwordTxt.getText().toString();
                  if (!TextUtils.isEmpty(name) || !TextUtils.isEmpty(email) || !TextUtils.isEmpty(password) ){
                      progressBar.setTitle("Registering");
                      progressBar.setCanceledOnTouchOutside(false);
                      progressBar.show();
                      registerUser(name, email, password);
                  }

            }
        });
    }


    public void registerUser(final String usernames, String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    currentUser=mAuth.getCurrentUser();
                    String userUid = currentUser.getUid();
                    databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(userUid);

                        HashMap<String,String> hashMap = new HashMap<>();
                        hashMap.put("name",usernames);
                        hashMap.put("status","Hi I am using Messenger2 ");
                        hashMap.put("thumbnail_image","thumbnail image");


                        databaseReference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                if (task.isSuccessful()){
                                    progressBar.dismiss();
                                    Intent intent = new Intent(RegisterActivity.this, HomeActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK| Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);
                                    finish();
                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(RegisterActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                }

        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressBar.hide();
                Toast.makeText(RegisterActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}