package com.example.isaksayyed.messenger;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class StatusActivity extends AppCompatActivity{

    Button changeStatusBtn;
    EditText statusEditText;
    String userUid;
    ProgressDialog progressDialog;

    //Fire Base
    private  FirebaseAuth auth;
    private FirebaseUser currentUser;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status);

        //Fire Base
        auth =FirebaseAuth.getInstance();
        currentUser = auth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        userUid =currentUser.getUid();
        databaseReference = firebaseDatabase.getReference().child("Users").child(userUid);


        String stausValue = getIntent().getStringExtra("statusValue");
        progressDialog = new ProgressDialog(this);
        statusEditText = findViewById(R.id.statusUpdate);
        statusEditText.setText(stausValue);
        changeStatusBtn = findViewById(R.id.statusBtn);
        changeStatusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.setTitle("Updating status");
                progressDialog.setMessage("Please wait");
                progressDialog.show();
                String status = statusEditText.getText().toString();
                databaseReference.child("status").setValue(status).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                       if (task.isSuccessful()){
                           progressDialog.dismiss();
                           Intent intent = new Intent(StatusActivity.this,HomeActivity.class);
                           startActivity(intent);
                       }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.hide();
                        Toast.makeText(StatusActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }



}
