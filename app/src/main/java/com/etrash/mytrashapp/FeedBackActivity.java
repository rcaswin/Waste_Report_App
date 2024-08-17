package com.etrash.mytrashapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FeedBackActivity extends AppCompatActivity {

    Button feedback;
    DatabaseReference detailsRef;
    EditText comments;
    RatingBar ratingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_feed_back);
        getSupportActionBar().hide();
        detailsRef= FirebaseDatabase.getInstance().getReference().child("Feedback");
        feedback=findViewById(R.id.feedbtn);
        comments=findViewById(R.id.reviewtext);
        ratingBar=findViewById(R.id.ratingBar);

        feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Rating();
            }
        });


    }

    public void Rating(){
        String commentsdb=comments.getText().toString();
        float rating= ratingBar.getRating();
        FeedBackDB DB= new FeedBackDB(commentsdb);
        detailsRef.push().setValue(DB);
        detailsRef.child("FeedBack").setValue(rating);
        Toast.makeText(FeedBackActivity.this, "FeedBack Submitted Successfully", Toast.LENGTH_SHORT).show();
        finish();
    }
}