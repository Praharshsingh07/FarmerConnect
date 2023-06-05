package com.suretrust.farmerconnect;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class loginUsingEmail extends AppCompatActivity {
    Button loginUsingEmail;
    EditText getLoginEmail,getLoginPassword;
    FirebaseFirestore fs;
    FirebaseAuth fauth;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_using_email);

        loginUsingEmail = findViewById(R.id.loginWithEmailButton);
        getLoginEmail = findViewById(R.id.loginEmail);
        getLoginPassword = findViewById(R.id.loginPassword);
        fauth = FirebaseAuth.getInstance();


        loginUsingEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logintheUser();
            }
        });




    }
    public void logintheUser() {

        String gle = getLoginEmail.getText().toString().trim();
        String glp = getLoginPassword.getText().toString().trim();
//        Log.i(String.valueOf(1),geEmail);
        if (gle.equals("") || glp.equals("")) {
            Toast.makeText(this, "Please Enter the Complete Information", Toast.LENGTH_SHORT).show();
        } else {
            fauth.signInWithEmailAndPassword(gle,glp).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(loginUsingEmail.this, "Logged In Successfully", Toast.LENGTH_SHORT).show();
                        //    pg.cancel();
                        Intent go2 = new Intent(loginUsingEmail.this, MainActivity.class);
                        startActivity(go2);
                        finish();
                    }
                    else{
                        Toast.makeText(loginUsingEmail.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                    }
                }
            });

        }
    }
}