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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    //Fire Base
    FirebaseAuth mAuth;
    FirebaseUser currentUser;

    Button loginBtn;
    EditText emailTxt,passwordTxt;
    ProgressDialog progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Fire Base
        mAuth= FirebaseAuth.getInstance();
        currentUser= mAuth.getCurrentUser();

        loginBtn= findViewById(R.id.registerButton);
        emailTxt= findViewById(R.id.emailTxt);
        passwordTxt= findViewById(R.id.passwordTxt);
        progressBar= new ProgressDialog(this);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email= emailTxt.getText().toString();
                String password = passwordTxt.getText().toString();

                if (!TextUtils.isEmpty(email )|| !TextUtils.isEmpty(password))
                     progressBar.setTitle("Loging In ");
                     progressBar.setMessage("Please Wait");
                     progressBar.setCanceledOnTouchOutside(false);
                     progressBar.show();
                    login(email,password);
            }
        });
    }

    public void login(String email,String password){
        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    progressBar.dismiss();
                    Intent intent = new Intent(LoginActivity.this,HomeActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK| Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressBar.hide();
                Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
