package com.suretrust.farmerconnect;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class InfoActivity extends AppCompatActivity {

    private TextView textViewData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        textViewData = findViewById(R.id.textViewScannedData);

        // Get the scanned data from the intent
        String scannedData = getIntent().getStringExtra("scanned_info");

        // Display the scanned data
        textViewData.setText(scannedData);
    }

    @Override
    public void onBackPressed() {
        // Call finish to close the InfoActivity and go back to the previous fragment
        finish();
    }
}
