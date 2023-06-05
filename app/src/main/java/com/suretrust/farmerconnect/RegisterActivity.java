package com.suretrust.farmerconnect;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class RegisterActivity extends AppCompatActivity {
    Button registerUser,performLogin;
    FirebaseAuth fAuth;
    TextView txt1;

    FirebaseFirestore firestore;
    EditText name,email,password,phonenumber;
//    ProgressBar pg;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        name = findViewById(R.id.entername);
        email = findViewById(R.id.enteremail);
        phonenumber = findViewById(R.id.enterphonenumber);
        password = findViewById(R.id.enterpassword);
        performLogin = findViewById(R.id.performLogin);

        fAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        registerUser = findViewById(R.id.registerbutton);
//        pg = findViewById(R.id.progressbar);
        txt1 = findViewById(R.id.txt1);



        registerUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String getname = name.getText().toString().trim();
                String getnumber = phonenumber.getText().toString().trim();
                String getemail = email.getText().toString().trim();
                String getpassword = password.getText().toString().trim();

//                pg = new ProgressDialog(registerUser.this);
//                pg.setTitle("Saving data");
//                pg.setMessage("Wait while Registering you");

                if(getname.equals("")||getnumber.equals("")||getemail.equals("")||getpassword.equals("")){
                    Toast.makeText(RegisterActivity.this, "Please Enter the Complete information", Toast.LENGTH_SHORT).show();
                }

                else{
//                    pg.show();

                    fAuth.createUserWithEmailAndPassword(getemail,getpassword).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {

                            firestore.collection("UserData").
                                    document(fAuth.getInstance().getUid()).
                                    set(new UserData(getname,getnumber,getemail));


                            startActivity(new Intent(RegisterActivity.this,LoginActivity.class));
                            finish();
                            Toast.makeText(RegisterActivity.this, "Account Created Successfully", Toast.LENGTH_SHORT).show();
//                            pg.cancel();

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(RegisterActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
//                            pg.cancel();

                        }
                    });
                }
            }
        });




        performLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i2 = new Intent(RegisterActivity.this,LoginActivity.class);
                startActivity(i2);
            }
        });


        String test1 = "Having an account then Login With Us!";
        SpannableString ss = new SpannableString(test1);
        ClickableSpan cs1 = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                Intent i3 = new Intent(RegisterActivity.this,LoginActivity.class);
                startActivity(i3);
            }
        };
        ss.setSpan(cs1,23,28, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        txt1.setText(ss);
        txt1.setMovementMethod(LinkMovementMethod.getInstance());


    }
}