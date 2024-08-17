package com.etrash.mytrashapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.etrash.mytrashapp.databinding.ActivityReferBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class ReferActivity extends AppCompatActivity {
    ActivityReferBinding binding;
    FirebaseAuth auth;
    FirebaseUser user;
    DatabaseReference reference;
    TextView textView, textView2;
    Button button, button2;
    String OppId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        binding= ActivityReferBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().hide();

        auth= FirebaseAuth.getInstance();
        user= auth.getCurrentUser();
        reference= FirebaseDatabase.getInstance().getReference().child("Users");

        textView= findViewById(R.id.referCode);
        button= findViewById(R.id.btnRedeemCode);
        textView2= findViewById(R.id.copyReferCode);
        button2= findViewById(R.id.btnreferCode);

        loadReferCode();

        checkRedeemAvailable();

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String referCode= textView.getText().toString();
                String shareBody= "Hey, I am using a best waste reporting app. Join using my invite code and get 100"+"coins.MY INVITE code is"+referCode;
                Intent intent=new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT, shareBody);
                startActivity(intent);
            }
        });

        textView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager clipboardManager=(ClipboardManager) getSystemService(ReferActivity.this.CLIPBOARD_SERVICE);
                ClipData clipData= ClipData.newPlainText("Data", textView.getText().toString());
                clipboardManager.setPrimaryClip(clipData);
                Toast.makeText(ReferActivity.this, "Referral code copied", Toast.LENGTH_SHORT).show();
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText editText= new EditText(ReferActivity.this);
                editText.setHint("Code12f");

                LinearLayout.LayoutParams layoutParams= new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT);

                editText.setLayoutParams(layoutParams);

                AlertDialog.Builder alertdialog= new AlertDialog.Builder(ReferActivity.this);
                alertdialog.setTitle("Redeem code");
                alertdialog.setView(editText);

                alertdialog.setPositiveButton("Redeem", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String inputCode= editText.getText().toString();

                        if(TextUtils.isEmpty(inputCode)){
                            Toast.makeText(ReferActivity.this, "Input valid code", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if(inputCode.equals(textView.getText().toString())){
                            Toast.makeText(ReferActivity.this, "You can not input your own code", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        redeemQuery(inputCode, dialog);
                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                alertdialog.show();
            }
        });
    }

    private  void  redeemQuery(String inputCode, DialogInterface dialogInterface){
        Query query= reference.orderByChild("referCode").equalTo(inputCode);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                    OppId= dataSnapshot.getKey();
                    reference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            ProfileDB profileDB= snapshot.child(OppId).getValue(ProfileDB.class);
                            ProfileDB profileDB1= snapshot.child(user.getUid()).getValue(ProfileDB.class);

                            int coins= profileDB.getCoins();
                            int updatedCoins= coins+100;
                            int myCoins= profileDB1.getCoins();
                            int myUpdatedCoins= myCoins+100;
                            HashMap<String , Object> map= new HashMap<>();
                            map.put("coins", myUpdatedCoins);
                            map.put("redeemed", true);

                            reference.child(OppId).updateChildren(map);
                            reference.child(user.getUid()).updateChildren(map)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            dialogInterface.dismiss();
                                            Toast.makeText(ReferActivity.this, "Congrats", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(ReferActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void checkRedeemAvailable(){
        reference.child(user.getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists() && snapshot.hasChild("redeemed")){
                            boolean isAvailable = snapshot.child("redeemed").getValue(Boolean.class);
                            if(isAvailable){
                                button.setVisibility(View.GONE);
                            }else{
                                button.setVisibility(View.VISIBLE);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(ReferActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void loadReferCode(){
        reference.child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String referCode= snapshot.child("referCode").getValue(String.class);
                textView.setText(referCode);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ReferActivity.this,error.getMessage(), Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }
}