package com.suretrust.farmerconnect;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

public class SplashScreen extends AppCompatActivity {
    private static final long SPLASH_TIMEOUT = 1000; // 1 second

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_splash_screen);
        Window window = this.getWindow();
        window.setStatusBarColor(this.getResources ().getColor(R.color.white));

        // Using a Handler to delay the transition to the next activity
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Create an Intent to start the next activity
                Intent intent = new Intent(SplashScreen.this, registerUser.class);
                startActivity(intent);

                // Close the splash screen activity
                finish();
            }
        }, 2000);
    }
}
