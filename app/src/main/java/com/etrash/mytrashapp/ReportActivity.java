package com.etrash.mytrashapp;

import static android.content.ContentValues.TAG;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Window;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;

import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.UUID;

public class ReportActivity extends AppCompatActivity {

    private EditText name;
    private EditText mailid;
    private EditText PhoneNo;
    private TextView Address;
    private TextView Place;
    private EditText Description;
    private ImageView image;
    DatabaseReference detailsRef;
    FirebaseStorage storage;
    Button submit;
    Button upload;
    ImageButton locatebtn;
    Uri imageUri;
    Spinner spinner;
    String[] country = {"Select the Panchayat","Kanniyakumari","Karungal","Ponmanai","Thingalnagar","Aralvaimozhi","Kulasekarapuram","Pudukadai","Thiruparappu",
            "Anjugramam", "Azhagappapuram", "Ezhudesam","Killiyur", "Kottaram", "Nallur", "Pazhugal", "Thiruvattaru", "Valvaithankoshtam",
            "Villukuri", "Eraniel", "Kilkulam", "Mulagumudu","Thalakudi","Vellimalai","Kaliyakkavilai","Manavalakurichi","Suchindram",
            "Agastheeswaram","Arumanai","Bhoothapandi","Kadayal", "Kollankodu", "Kumarapuram", "Reethapuram", "Thiruvithankodu", "Verkilambi",
            "Athur", "Ganapathipuram", "Mandaikadu", "Neiyyur", "Thenthamaraikulam", "Alur", "Asaripallam", "Edaikodu", "Kallukuttam", "Kothainallur",
            "Miles", "Palapallam", "Thengampudur", "Unnamalaikadai", "Vilavur", "Azhagiapandiapuram", "Kappiyarai", "Marungur", "Puthalam", "Thesur"};

    FusedLocationProviderClient fusedLocationProviderClient;


    FirebaseDatabase database;


    String namedb;
    String mailiddb;
    String PhoneNodb;
    String Addressdb;
    String Descdb, value;
    private final static int REQUEST_CODE = 100;

    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Images");
    StorageReference storageReference = FirebaseStorage.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_report);
        getSupportActionBar().hide();

        database= FirebaseDatabase.getInstance();




        Objects.requireNonNull(getSupportActionBar()).setSplitBackgroundDrawable(new ColorDrawable(getColor(R.color.darkgreen)));
        image= findViewById(R.id.imageip);
        upload= findViewById(R.id.uploadbtn);
        locatebtn=findViewById(R.id.locatebtn);
        spinner = findViewById(R.id.dropdown);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(ReportActivity.this, android.R.layout.simple_spinner_dropdown_item, country);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                value = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(ReportActivity.this);
        locatebtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                getLastLocation();
            }
        });


        upload.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                ImagePicker.with(ReportActivity.this)
                        .crop()
                        .maxResultSize(1080,1080)
                        .start();
            }
        });


        name = findViewById(R.id.nameip);
        mailid = findViewById(R.id.emailip);
        PhoneNo = findViewById(R.id.phoneip);
        Address = findViewById(R.id.locationip);
        Description = findViewById(R.id.descip);
        image = findViewById(R.id.imageip);
        submit= findViewById(R.id.submit);
        detailsRef= FirebaseDatabase.getInstance().getReference().child("ReportDetails");
        storage= FirebaseStorage.getInstance();

        submit.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){

                process();
            }
        });
    }

    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data){
        super.onActivityResult(requestCode, resultCode, data);

                imageUri= data.getData();
                image.setImageURI(imageUri);
                final StorageReference reference=storageReference.child(System.currentTimeMillis() + "." + getFileExtension(imageUri));
                reference.putFile(imageUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        reference.getDownloadUrl()
                                .addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        imageUri=uri;

                                        database.getReference().child("ReportDetails");
                                        insertReportData(uri.toString(), value);

                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                });


    }

    public void insertReportData(String url, String placeip){
        namedb= name.getText().toString();
        mailiddb= mailid.getText().toString();
        PhoneNodb= PhoneNo.getText().toString();
        Addressdb= Address.getText().toString();
        String place = placeip;
        Descdb= Description.getText().toString();
        String img=url;



        HashMap<String, Object> map= new HashMap<>();

        map.put("name", name.getText().toString());
        map.put("Email", mailid.getText().toString());
        map.put("PhoneNo", PhoneNo.getText().toString());
        map.put("Address", Address.getText().toString());
        map.put("Place", place);
        map.put("Description", Description.getText().toString());
        map.put("Images", img);


        detailsRef.push().setValue(map);
    }

    public void process() {
        if (name.getText().toString().isEmpty()) {
            if (mailid.getText().toString().isEmpty()) {
                if (PhoneNo.getText().toString().isEmpty()) {
                    if (Address.getText().toString().isEmpty()) {
                       if (Place.getText().toString().isEmpty()) {
                            if (Description.getText().toString().isEmpty()) {
                                if (image.getDrawable() == null) {
                                    Toast.makeText(ReportActivity.this, "Fill the above fields.", Toast.LENGTH_SHORT).show();
                                }

                            }
                        }
                    }
                }
            }
        } else {
            Toast.makeText(ReportActivity.this, "Report Submitted Successfully", Toast.LENGTH_SHORT).show();
            finish();
        }
    }
    private void getLastLocation(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){


            fusedLocationProviderClient.getLastLocation()
                    .addOnSuccessListener(new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {

                            if (location != null){



                                try {
                                    Geocoder geocoder = new Geocoder(ReportActivity.this, Locale.getDefault());
                                    List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                                    Address.setText(addresses.get(0).getAddressLine(0));
                                    //Place.setText(addresses.get(0).getLocality());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }


                            }

                        }
                    });


        }else {
            askPermission();
        }


    }
    private void askPermission() {

        ActivityCompat.requestPermissions(ReportActivity.this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_CODE);


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull @org.jetbrains.annotations.NotNull String[] permissions, @NonNull @org.jetbrains.annotations.NotNull int[] grantResults) {

        if (requestCode == REQUEST_CODE){

            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){


                getLastLocation();

            }else {


                Toast.makeText(ReportActivity.this,"Please provide the required permission",Toast.LENGTH_SHORT).show();

            }



        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
    public String getFileExtension(Uri fileuri){
        ContentResolver contentResolver= getContentResolver();
        MimeTypeMap mime= MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(contentResolver.getType(fileuri));
    }

}
