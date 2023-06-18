package com.suretrust.farmerconnect;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class FarmerInfoActivity extends AppCompatActivity {

    private TextView f_nametv, agetv, locationtv, farmer_nametv, farmer_infotv;
    private Button donate_btn;
    private ImageView farmer_photo;

    private String upi,f_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_farmer_info);
        Window window = this.getWindow();
        window.setStatusBarColor(this.getResources().getColor(R.color.top));


        f_nametv = findViewById(R.id.f_name);
        agetv = findViewById(R.id.age);
        locationtv = findViewById(R.id.location);
        farmer_nametv = findViewById(R.id.farmer_name);
        farmer_infotv = findViewById(R.id.farmer_info);
        farmer_photo= findViewById(R.id.farmer_photo);
        donate_btn = findViewById(R.id.donate_btn);


        String qrID = getIntent().getStringExtra("qrID");


        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("qrcode").document(qrID);

        docRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    // Extract field values from the document
                    f_name = document.getString("farmer_name");
                    String age = document.getString("age");
                    String location = document.getString("location");
                    String farmer_info = document.getString("farmer_info");
                    upi=document.getString("farmer_upi");

                    // Assign values to TextViews
                    f_nametv.setText(f_name);
                    agetv.setText(age);
                    locationtv.setText(location);
                    farmer_nametv.setText(f_name);
                    farmer_infotv.setText(farmer_info);
                } else {
                    // Document doesn't exist
                }
            } else {
                // Error accessing Firestore
            }
        });

        donate_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FarmerInfoActivity.this, PaymentActivity.class);
                intent.putExtra("upiID", upi);
                intent.putExtra("name", f_name);
                startActivity(intent);
            }
        });

    }
}