package com.example.isaksayyed.messenger;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class HomeActivity extends AppCompatActivity {

    private TextView mTextMessage;

    //FireBase
    FirebaseAuth mAuth;
    FirebaseUser currentUser;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment=null;
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    fragment= new HomeFragment();
                    break;
                case R.id.navigation_notifications:
                    fragment= new NotificationFragment();
                    break;
                case R.id.navigation_dashboard:
                   fragment= new DashBoardFragment();
                    break;
            }
            getSupportFragmentManager().beginTransaction().replace(R.id.fragmentcontainer,fragment).commit();
            return true;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //FireBase
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentcontainer,new HomeFragment()).commit();
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (currentUser == null){
            Intent intent = new Intent(HomeActivity.this,StartActivity.class);
            startActivity(intent);
            finish();
        }
    }
}
