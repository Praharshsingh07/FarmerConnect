package com.suretrust.farmerconnect;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class DonationActivity extends AppCompatActivity {

    Button pay_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donation);

        pay_btn=findViewById(R.id.pay_btn);

        pay_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(DonationActivity.this,PaymentActivity.class);
                startActivity(i);
            }
        });
    }
}