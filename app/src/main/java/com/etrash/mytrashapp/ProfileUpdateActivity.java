package com.etrash.mytrashapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.etrash.mytrashapp.databinding.ActivitySignUpBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class ProfileUpdateActivity extends AppCompatActivity {

    ActivitySignUpBinding binding;
    FirebaseAuth auth;
    DatabaseReference database;
    FirebaseUser user;
    ProgressDialog dialog;

    EditText name, phoneno, mailid, place, pincode;

    String Name, Phoneno, MailId, Place, Pincode;

    Button submit;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        binding= ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(R.layout.activity_profile_update);
        getSupportActionBar().hide();

        submit= findViewById(R.id.btnSubmit);
        name= findViewById(R.id.editName);
        phoneno= findViewById(R.id.editPhoneNo);
        mailid= findViewById(R.id.editMail);
        place= findViewById(R.id.editPlace);
        pincode= findViewById(R.id.editPinCode);

        getSupportActionBar().hide();
        auth= FirebaseAuth.getInstance();
        database= FirebaseDatabase.getInstance().getReference().child("Users");
        user= auth.getCurrentUser();

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InsertProfiledata();
                profileprocess();
            }
        });
    }
    public void InsertProfiledata(){

        Name= name.getText().toString();
        Phoneno= phoneno.getText().toString();
        MailId= mailid.getText().toString();
        Place= place.getText().toString();
        Pincode= pincode.getText().toString();
        String refer= MailId.substring(0,MailId.lastIndexOf("@"));
        String referCode= refer.replace(".", "");

        HashMap<String, Object> map= new HashMap<>();

        map.put("name",name.getText().toString());
        map.put("Mobile Number",phoneno.getText().toString());
        map.put("Email",mailid.getText().toString());
        map.put("Place",place.getText().toString());
        map.put("Pincode",pincode.getText().toString());
        map.put("profile", "https://firebasestorage.googleapis.com/v0/b/mytrashapp-9e17f.appspot.com/o/images.png?alt=media&token=5e83054b-5194-49e2-a78a-f95d35ee1baa");
        map.put("referCode", referCode);
        map.put("coins",20);
        database.child(user.getUid()).setValue(map);
    }
    public void profileprocess(){
        if(name==null){
            if(phoneno==null){
                if(mailid==null){
                    if(place==null){
                        if(pincode==null){
                            Toast.makeText(ProfileUpdateActivity.this, "Fill the above fields.", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        }else{
            Toast.makeText(ProfileUpdateActivity.this, "Profile updated Successfully", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
        }
    }
}