package com.etrash.mytrashapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

public class MappingActivity extends AppCompatActivity {

    CardView clothingCard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        setContentView(R.layout.activity_mapping);
        getSupportActionBar().hide();

        clothingCard = findViewById(R.id.clothingCard);

        clothingCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MappingActivity.this, MappingActivity.class);
                startActivity(intent);
            }
        });
    }
}