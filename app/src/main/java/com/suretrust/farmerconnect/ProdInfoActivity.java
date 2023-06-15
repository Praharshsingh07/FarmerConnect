package com.suretrust.farmerconnect;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class ProdInfoActivity extends AppCompatActivity {

    private TextView qualitytv, quantitytv, prod_datetv, prod_nametv, prod_infotv, harv_practv;
    private Button farmers_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_prod_info);
        Window window = this.getWindow();
        window.setStatusBarColor(this.getResources().getColor(R.color.top));

        qualitytv = findViewById(R.id.quality);
        quantitytv = findViewById(R.id.quantity);
        prod_datetv = findViewById(R.id.prod_date);
        prod_nametv = findViewById(R.id.prod_name);
        prod_infotv = findViewById(R.id.prod_info);
        harv_practv = findViewById(R.id.harv_prac);
        farmers_btn = findViewById(R.id.farmers_btn);

        // Get the scanned data from the intent
        String qrID = getIntent().getStringExtra("scanned_info");

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("qrcode").document(qrID);

        docRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    // Extract field values from the document
                    String quality = document.getString("quality");
                    String quantity = document.getString("quantity");
                    String prodDate = document.getString("prod_date");
                    String prodName = document.getString("prod_name");
                    String prodInfo = document.getString("prod_info");
                    String harvPrac = document.getString("harvesting_practices");

                    // Assign values to TextViews
                    qualitytv.setText(quality);
                    quantitytv.setText(quantity);
                    prod_datetv.setText(prodDate);
                    prod_nametv.setText(prodName);
                    prod_infotv.setText(prodInfo);
                    harv_practv.setText(harvPrac);
                } else {
                    // Document doesn't exist
                }
            } else {
                // Error accessing Firestore
            }
        });

        farmers_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProdInfoActivity.this, FarmerInfoActivity.class);
                intent.putExtra("qrID", qrID);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        // Call finish to close the InfoActivity and go back to the previous fragment
        finish();
    }
}
