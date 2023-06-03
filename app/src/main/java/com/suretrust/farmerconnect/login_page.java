package com.suretrust.farmerconnect;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class login_page extends AppCompatActivity {
    Button gotoEmailLogin;
    TextView txt2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);
        gotoEmailLogin = findViewById(R.id.usingEmail);
        txt2 = findViewById(R.id.notregistered);

        gotoEmailLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(login_page.this,loginUsingEmail.class);
                startActivity(i);
                finish();
            }
        });


        String s1 = "Not registered yet ? Create new Account";
        SpannableString ss2 = new SpannableString(s1);
        ClickableSpan cs1 = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                Intent i4 = new Intent(login_page.this,registerUser.class);
                startActivity(i4);
            }
        };
        ss2.setSpan(cs1,21,38, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        txt2.setText(ss2);
        txt2.setMovementMethod(LinkMovementMethod.getInstance());

    }
}