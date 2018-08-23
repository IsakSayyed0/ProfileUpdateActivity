package com.example.isaksayyed.messenger;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;


public class DashBoardFragment extends Fragment implements View.OnClickListener {

    TextView usernameTextView,statusTextView;
    Button logout,edit_photo,edit_status;
    View view;
    String name;
    String status;
    private int PICK_IMAGE_REQUEST = 1;
    CircleImageView profile_image;
    private static final int MAX_LENGTH =10;
    ProgressDialog progressDialog;
    //Fire Base
    FirebaseAuth auth;
    FirebaseUser currentUser;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    StorageReference storageReference;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_dash_board, container, false);


        //FireBase

        storageReference = FirebaseStorage.getInstance().getReference();
        auth = FirebaseAuth.getInstance();


        progressDialog = new ProgressDialog(getContext());
        profile_image = view.findViewById(R.id.listProfileimage);
        usernameTextView = view.findViewById(R.id.usernameTxt);
        statusTextView = view.findViewById(R.id.statusTxt);
        edit_photo = view.findViewById(R.id.editProfile);
        edit_photo.setOnClickListener(this);
        edit_status= view.findViewById(R.id.editSataus);
        edit_status.setOnClickListener(this);
        logout =  view.findViewById(R.id.logOut);
        logout.setOnClickListener(this);

        userinfo();

        return view;

    }

    public void onClick(View v) {
        // Perform action on click
        switch(v.getId()) {
            case R.id.logOut:
                auth.signOut();
                Intent intent = new Intent(getContext(),StartActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK| Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                break;

            case R.id.editProfile:
                Intent imageIntent = new Intent();
                // Show only images, no videos or anything else
                imageIntent.setType("image/*");
                imageIntent.setAction(Intent.ACTION_GET_CONTENT);
                // Always show the chooser (if there are multiple options available)
                startActivityForResult(Intent.createChooser(imageIntent, "Select Picture"), PICK_IMAGE_REQUEST);

                break;

            case R.id.editSataus:
                String stausValue = statusTextView.getText().toString();
                Intent StatusIntent = new Intent(getContext(),StatusActivity.class);
                StatusIntent.putExtra("statusValue",stausValue);
                startActivity(StatusIntent);
                break;


        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data ) {
        super.onActivityResult(requestCode, resultCode, data);


        if (data != null) {
            Uri selectedImage = data.getData();

            progressDialog.setTitle("Uploading Image");
            progressDialog.setMessage("please wait");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();
            String userUid = currentUser.getUid();
            StorageReference filePath = storageReference.child("profile_images").child(userUid+".jpg");
            filePath.putFile(selectedImage).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    if (task.isSuccessful()){
                        String user = currentUser.getUid();
                        Toast.makeText(getContext(), "Done ", Toast.LENGTH_SHORT).show();
                        String imageUri = task.getResult().getDownloadUrl().toString();
                        DatabaseReference imageRef = FirebaseDatabase.getInstance().getReference().child("Users").child(user).child("thumbnail_image");
                        imageRef.setValue(imageUri).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    progressDialog.dismiss();

                                }
                            }
                        });

                    }
                }
            });

        } else {
            progressDialog.dismiss();
            Toast.makeText(getActivity(), "Try Again!!", Toast.LENGTH_SHORT).show();
        }



    }

    public void userinfo(){
        currentUser = auth.getCurrentUser();
        String userUid = currentUser.getUid();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child("Users").child(userUid);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
               Users users = dataSnapshot.getValue(Users.class);
                usernameTextView.setText(users.getName());
                statusTextView.setText(users.getStatus());
                String image = dataSnapshot.child("thumbnail_image").getValue().toString();
                Picasso.get().load(image).into(profile_image);

             //   Toast.makeText(getContext(), dataSnapshot.getValue().toString(), Toast.LENGTH_LONG).show();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


}
