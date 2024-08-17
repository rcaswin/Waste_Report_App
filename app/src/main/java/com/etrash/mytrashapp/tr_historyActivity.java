package com.etrash.mytrashapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class tr_historyActivity extends AppCompatActivity {

    ArrayList<trModel>list;
    FirebaseDatabase database;
    FirebaseUser user;
    FirebaseAuth auth;
    trAdaptor tradaptor;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_tr_history);

        getSupportActionBar().hide();

        auth= FirebaseAuth.getInstance();
        database= FirebaseDatabase.getInstance();
        user= auth.getCurrentUser();

        recyclerView= findViewById(R.id.redeemRecycle);

        LinearLayoutManager manager= new LinearLayoutManager(this);


        database.getReference().child("Redeem").child(user.getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        list = new ArrayList<>();

                        if(snapshot.exists()){
                            for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                                trModel model = dataSnapshot.getValue(trModel.class);
                                list.add(model);
                            }
                            tradaptor = new trAdaptor(tr_historyActivity.this, list);
                            recyclerView.setAdapter(tradaptor);
                            recyclerView.setLayoutManager(manager);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(tr_historyActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });

    }
}