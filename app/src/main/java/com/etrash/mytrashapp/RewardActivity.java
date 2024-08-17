package com.etrash.mytrashapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.etrash.mytrashapp.databinding.ActivityMainBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;


public class RewardActivity extends AppCompatActivity {
    ActivityMainBinding binding;

    FirebaseUser user;
    FirebaseDatabase database;
    FirebaseAuth auth;

    TextView textView,textView2;
    ImageView imageView;
    Button button;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_reward);
        getSupportActionBar().hide();

        auth= FirebaseAuth.getInstance();
        database= FirebaseDatabase.getInstance();
        user=auth.getCurrentUser();

        textView= findViewById(R.id.rewardname);
        imageView= findViewById(R.id.profilephoto);
        button= findViewById(R.id.Redeembtn);
        textView2= findViewById(R.id.points);

        database.getReference().child("Users").child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                ProfileDB profileDB= snapshot.getValue(ProfileDB.class);

                if(snapshot.exists()){
                    textView.setText(profileDB.getName());
                    textView2.setText(profileDB.getCoins()+"");

                    Picasso.get()
                            .load(profileDB.getProfile())
                            .placeholder(R.drawable.placeholder)
                            .into(imageView);
                }
                else{
                    Toast.makeText(getApplicationContext(), "data not exists", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }
    public void refer(View view){
        startActivity(new Intent(getApplicationContext(), ReferActivity.class));
    }
    public void redeem(View view){
        startActivity(new Intent(getApplicationContext(), RedeemActivity.class));
    }
    public void trhistory(View view){
        startActivity(new Intent(getApplicationContext(), tr_historyActivity.class));
    }
}