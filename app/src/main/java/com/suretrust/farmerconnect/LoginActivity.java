package com.suretrust.farmerconnect;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class LoginActivity extends AppCompatActivity {


    private static final String TAG = "LoginActivity";
    private static final int RC_SIGN_IN = 123;

    // variable for FirebaseAuth class
    private FirebaseAuth mAuth;

    // variable for our text input field for phone number
    private EditText edtPhone;

    // button for generating OTP
    private Button generateOTPBtn;

    private TextView gotoEmail,gotoPhone,tv5,tv6;
    private RelativeLayout relPhone,relEmail,signInButton;
    Button loginUsingEmail;
    EditText getLoginEmail,getLoginPassword;

    // string for storing the verification ID
    private String verificationId;
    String phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_login);
        Window window = this.getWindow();
        window.setStatusBarColor(this.getResources ().getColor(R.color.top));

        // initializing instance of FirebaseAuth
        mAuth = FirebaseAuth.getInstance();

        // initializing variables for EditText and Button
        edtPhone = findViewById(R.id.idEdtPhoneNumber);
        generateOTPBtn = findViewById(R.id.idBtnGetOtp);
        gotoEmail=findViewById(R.id.gotoEmail);
        gotoPhone=findViewById(R.id.gotoPhone);
        relEmail=findViewById(R.id.relEmail);
        relPhone=findViewById(R.id.relPhone);
        loginUsingEmail = findViewById(R.id.loginWithEmailButton);
        getLoginEmail = findViewById(R.id.loginEmail);
        getLoginPassword = findViewById(R.id.loginPassword);
        signInButton = findViewById(R.id.googleSignInButton);
        tv5 = findViewById(R.id.tv5);
        tv6 = findViewById(R.id.tv6);

        signInButton.setOnClickListener(view -> signInWithGoogle());

        tv6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(i);
            }
        });


        loginUsingEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logintheUser();
            }
        });

        gotoEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                relEmail.setVisibility(View.VISIBLE);
                relPhone.setVisibility(View.INVISIBLE);
                tv5.setVisibility(View.VISIBLE);
                tv6.setVisibility(View.VISIBLE);


            }
        });

        gotoPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                relEmail.setVisibility(View.INVISIBLE);
                relPhone.setVisibility(View.VISIBLE);
                tv5.setVisibility(View.INVISIBLE);
                tv6.setVisibility(View.INVISIBLE);

            }
        });

        // setting onClick listener for generate OTP button
        generateOTPBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // check if the user has entered a phone number
                if (TextUtils.isEmpty(edtPhone.getText().toString())) {
                    // display a toast message if phone number is empty
                    Toast.makeText(LoginActivity.this, "Please enter a valid phone number.", Toast.LENGTH_SHORT).show();
                } else {
                    // get the phone number and send OTP
                    phone = "+91" + edtPhone.getText().toString();
                    sendVerificationCode(phone);
                }
            }
        });
    }

    private void sendVerificationCode(String number) {
        // send OTP to the provided phone number
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(number)		 // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)				 // Activity (for callback binding)
                        .setCallbacks(mCallBack)		 // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    // callback method is called on PhoneAuthProvider
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks

            // initialize callbacks for verification
            mCallBack = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        // method called when OTP is sent from Firebase
        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            // store the verification ID received from Firebase
            verificationId = s;

            // start the VerifyOTPActivity and pass the verification ID
            Intent intent = new Intent(LoginActivity.this, VerifyOTPActivity.class);
            intent.putExtra("verificationId", verificationId);
            intent.putExtra("mobno", phone);
            startActivity(intent);
        }

        // method called when user receives OTP from Firebase
        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
            // This method can be empty as we are handling verification in VerifyOTPActivity.
        }

        // method called when Firebase fails to send the OTP
        @Override
        public void onVerificationFailed(FirebaseException e) {
            // display an error message
            Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
            Log.d("myapp",""+e.getMessage());
        }
    };

    public void logintheUser() {

        String gle = getLoginEmail.getText().toString().trim();
        String glp = getLoginPassword.getText().toString().trim();
//        Log.i(String.valueOf(1),geEmail);
        if (gle.equals("") || glp.equals("")) {
            Toast.makeText(this, "Please Enter the Complete Information", Toast.LENGTH_SHORT).show();
        } else {
            mAuth.signInWithEmailAndPassword(gle,glp).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(LoginActivity.this, "Logged In Successfully", Toast.LENGTH_SHORT).show();
                        //    pg.cancel();
                        Intent go2 = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(go2);
                    }
                    else{
                        Toast.makeText(LoginActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                    }
                }
            });

        }
    }











    private void signInWithGoogle() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .requestId() // force account picker to be shown
                .build();
        GoogleSignInClient googleSignInClient = GoogleSignIn.getClient(this, gso);
        Intent signInIntent = googleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }




    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                Toast.makeText(this, "Sign in failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e(TAG, "signInResult:failed code=" + e.getStatusCode());
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "signInWithCredential:success");

//                        FirebaseFirestore db = FirebaseFirestore.getInstance();
//                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//                        String email = user.getEmail();

                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();

//                        db.collection("user-students").document(email).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
//                            @Override
//                            public void onSuccess(DocumentSnapshot documentSnapshot) {
//                                if (documentSnapshot.exists()) {
//                                    Intent intent = new Intent(LoginActivity.this, StudentDashBoardActivity.class);
//                                    startActivity(intent);
//                                    finish();
//                                } else {
//                                    db.collection("user-teachers").document(email).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
//                                        @Override
//                                        public void onSuccess(DocumentSnapshot documentSnapshot) {
//                                            if (documentSnapshot.exists()) {
//                                                Intent intent = new Intent(LoginActivity.this, TeacherDashBoardActivity.class);
//                                                startActivity(intent);
//                                                finish();
//                                            } else {
//                                                db.collection("user-admins").document(email).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
//                                                    @Override
//                                                    public void onSuccess(DocumentSnapshot documentSnapshot) {
//                                                        if (documentSnapshot.exists()) {
//                                                            Intent intent = new Intent(LoginActivity.this, AdminDashBoardActivity.class);
//                                                            startActivity(intent);
//                                                            finish();
//                                                        } else {
//                                                            Intent intent = new Intent(LoginActivity.this, StudentProfileFillActivity.class);
//                                                            startActivity(intent);
//                                                            finish();
//                                                        }
//                                                    }
//                                                });
//                                            }
//                                        }
//                                    });
//                                }
//                            }
//                        });



                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "signInWithCredential:failure", task.getException());
                        Toast.makeText(LoginActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

}
