package com.etrash.mytrashapp;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;


import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    ImageButton elecImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();



        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigation);
        bottomNavigationView.setSelectedItemId(R.id.bottom_home);

        elecImage = findViewById(R.id.elecImage);
        elecImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, MapsActivity.class);
                startActivity(intent);
            }
        });


        bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.bottom_home:
                    return true;
                case R.id.bottom_AboutUs:
                    startActivity(new Intent(getApplicationContext(), AboutUsActivity.class));
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    finish();
                    return true;
                case R.id.bottom_profile:
                    startActivity(new Intent(getApplicationContext(), ProfileGameActivity.class));
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    finish();
                    return true;
            }
            return false;
        });
    }
    public void addOperation(View view){
        startActivity(new Intent(getApplicationContext(), ReportActivity.class));
    }
    public void feed(View view){
        startActivity(new Intent(getApplicationContext(), FeedBackActivity.class));
    }

    public void queryatn(View view){
        startActivity(new Intent(getApplicationContext(), QueryActivity.class));
    }
    public void his(View view){
        startActivity(new Intent(getApplicationContext(), HistoryActivity.class));
    }
    public void reward(View view){
        startActivity(new Intent(getApplicationContext(), RewardActivity.class));
    }
}