package com.etrash.mytrashapp;

import static android.graphics.Color.TRANSPARENT;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class RedeemActivity extends AppCompatActivity {

    Button TransactionHistory, amazonRedeem, gplayredeem, paypalRedeem, paytmRedeem, btnRedeem, btnCancel;
    TextView tv1, tv2, paytmneed, amazonneed, gplayneed, paypalneed, paytmcoins, amazoncoins, gplaycoins, paypalcoins, withMethods;
    ProgressBar thPB, paytmPB, gplayPB, paypalPB, amazonPB;
    Dialog dialog;
    ImageView withLogo;
    EditText edtPaymentsDetails, edtCoins;


    FirebaseAuth auth;
    FirebaseUser user;
    DatabaseReference reference;
    int currentCoins;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_redeem);

        getSupportActionBar().hide();
        dialog = new Dialog(RedeemActivity.this);
        dialog.setContentView(R.layout.payment_dialog);

        amazonRedeem= findViewById(R.id.btnAmazonRedeem);
        gplayredeem= findViewById(R.id.btnGoogleRedeem);
        paypalRedeem= findViewById(R.id.button5);
        paytmRedeem= findViewById(R.id.btnPaytmRedeem);
        thPB= findViewById(R.id.progressBar);
        paytmPB= findViewById(R.id.progressBar2);
        gplayPB=findViewById(R.id.progressBar4);
        paypalPB=findViewById(R.id.progressBar5);
        amazonPB=findViewById(R.id.progressBar3);
        tv1= findViewById(R.id.textView9);
        tv2= findViewById(R.id.textView12);
        paytmneed= findViewById(R.id.textView16);
        amazonneed= findViewById(R.id.textView20);
        gplayneed= findViewById(R.id.textView22);
        paypalneed= findViewById(R.id.textView25);
        paytmcoins= findViewById(R.id.textView26);
        amazoncoins= findViewById(R.id.textView27);
        gplaycoins= findViewById(R.id.textView29);
        paypalcoins= findViewById(R.id.textView30);
        edtPaymentsDetails= dialog.findViewById(R.id.editTextTextPersonName);
        edtCoins= dialog.findViewById(R.id.editTextPhone);
        btnCancel=dialog.findViewById(R.id.button3);
        btnRedeem= dialog.findViewById(R.id.button4);
        withLogo= dialog.findViewById(R.id.imageView11);
        withMethods= dialog.findViewById(R.id.textView19);



        if(dialog.getWindow() != null){
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(TRANSPARENT));
            dialog.setCancelable(false);
        }

        paytmRedeem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                withLogo.setImageResource(R.drawable.paytm);
                withMethods.setText("Paytm");
                dialog.show();
            }
        });

        amazonRedeem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                withLogo.setImageResource(R.drawable.amazon);
                withMethods.setText("Amazon");
                dialog.show();
            }
        });

        gplayredeem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                withLogo.setImageResource(R.drawable.gplay);
                withMethods.setText("Google Play");
                dialog.show();
            }
        });

        paypalRedeem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                withLogo.setImageResource(R.drawable.paypal);
                withMethods.setText("Paypal");
                dialog.show();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        btnRedeem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String paymentmethods= withMethods.getText().toString();
                redeem(paymentmethods);
            }
        });

        auth= FirebaseAuth.getInstance();
        user= auth.getCurrentUser();
        reference= FirebaseDatabase.getInstance().getReference();
        reference.child("Users").child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ProfileDB profileDB= snapshot.getValue(ProfileDB.class);

                thPB.setProgress(profileDB.getCoins());
                tv1.setText(String.valueOf(profileDB.getCoins()));
                tv2.setText(String.valueOf(profileDB.getCoins()));

                currentCoins= Integer.parseInt(String.valueOf(profileDB.getCoins()));
                int requiredCoin= 5000-currentCoins;

                paytmPB.setProgress(profileDB.getCoins());
                amazonPB.setProgress(profileDB.getCoins());
                gplayPB.setProgress(profileDB.getCoins());
                paypalPB.setProgress(profileDB.getCoins());

                if(currentCoins>=5000){
                    paytmneed.setText("Completed");
                    amazonneed.setText("Completed");
                    gplayneed.setText("Completed");
                    paypalneed.setText("Completed");

                    paytmcoins.setVisibility(View.GONE);
                    amazoncoins.setVisibility(View.GONE);
                    gplaycoins.setVisibility(View.GONE);
                    paypalcoins.setVisibility(View.GONE);

                    amazonRedeem.setEnabled(true);
                    paytmRedeem.setEnabled(true);
                    gplayredeem.setEnabled(true);
                    paypalRedeem.setEnabled(true);
                }
                else{
                    amazonRedeem.setEnabled(false);
                    paytmRedeem.setEnabled(false);
                    gplayredeem.setEnabled(false);
                    paypalRedeem.setEnabled(false);

                    paytmcoins.setText(requiredCoin+"");
                    amazoncoins.setText(requiredCoin+"");
                    gplaycoins.setText(requiredCoin+"");
                    paypalcoins.setText(requiredCoin+"");
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void redeem(String paymentmethods) {

        String withCoins= edtCoins.getText().toString();
        String paymentdetails= edtPaymentsDetails.getText().toString();

        Calendar calendar= Calendar.getInstance();

        SimpleDateFormat currentDate= new SimpleDateFormat("dd-MM-yyyy");
        String date= currentDate.format(calendar.getTime());

        HashMap<String , Object> map= new HashMap<>();
        map.put("Paymentdetails", paymentdetails);
        map.put("Coin", withCoins);
        map.put("Payment_Methods", paymentmethods);
        map.put("Status", "false");
        map.put("Date", date);

        reference.child("Redeem").child(user.getUid())
                .push()
                .setValue(map)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        updateCoin();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void updateCoin() {
        int withdrawalcoin = Integer.parseInt(edtCoins.getText().toString());
        int updatedcoin = currentCoins-withdrawalcoin;

        HashMap<String, Object> map = new HashMap<>();
        map.put("coins", updatedcoin);

        reference.child("Users").child(user.getUid())
                .updateChildren(map)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(getApplicationContext(), "Congratulations", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }else{
                            Toast.makeText(getApplicationContext(), "error", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();;
                        }
                    }
                });
    }
}