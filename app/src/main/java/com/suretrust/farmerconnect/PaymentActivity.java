package com.suretrust.farmerconnect;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Arrays;
import java.util.List;

public class PaymentActivity extends AppCompatActivity {
    private final int REQUEST_CODE = 123;
    private EditText upiIdEditText;
    private EditText amountEditText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        /*1.1 Defining the variables with sample UPI Apps package names. The merchant can add more UPI apps with their package names according to their requirement */
        String BHIM_UPI = "in.org.npci.upiapp";
        String GOOGLE_PAY = "com.google.android.apps.nbu.paisa.user";
        String PHONE_PE = "com.phonepe.app";
        String PAYTM = "net.one97.paytm";

        /*1.2 Combining the UPI app package name variables in a list */
        List<String> upiApps = Arrays.asList(PAYTM, GOOGLE_PAY, PHONE_PE, BHIM_UPI);

        /*2.1 Defining button elements for generic UPI OS intent and specific UPI Apps */
        Button upiButton = findViewById(R.id.upi);
        Button paytmButton = findViewById(R.id.paytm);
        Button gpayButton = findViewById(R.id.gpay);
        Button phonepeButton = findViewById(R.id.phonepe);
        Button bhimButton = findViewById(R.id.bhim);
        upiIdEditText=findViewById(R.id.upi_id_edittext);
        amountEditText=findViewById(R.id.amount_edittext);

        /*2.2 Combining button elements of specific UPI Apps in a list in the same order as the above upiApps list of UPI app package names */
        List<Button> upiAppButtons = Arrays.asList(paytmButton, gpayButton, phonepeButton, bhimButton);

        /*3. Defining a UPI intent with a Paytm merchant UPI spec deeplink */
        String uri = "upi://pay?pa=paytmqr2810050501011ooqggb29a01@paytm&pn=Paytm%20Merchant&mc=5499&mode=02&orgid=000000&paytmqr=2810050501011OOQGGB29A01&am=11&sign=MEYCIQDq96qhUnqvyLsdgxtfdZ11SQP//6F7f7VGJ0qr//lF/gIhAPgTMsopbn4Y9DiE7AwkQEPPnb2Obx5Fcr0HJghd4gzo";
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
        intent.setData(Uri.parse(uri));

        /*4.1 Defining an on click action for the UPI generic OS intent chooser. This is just for reference, not needed in case of UPI Smart Intent.
            - This will display a list of all apps available to respond to the UPI intent
            in a chooser tray by the Android OS */
        upiButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String upiId = upiIdEditText.getText().toString().trim();
                String amount = amountEditText.getText().toString().trim();

                // Check if the UPI ID and amount are not empty
                if (upiId.isEmpty() || amount.isEmpty()) {
                    Toast.makeText(PaymentActivity.this, "Please enter UPI ID and amount", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Create the UPI intent URI using the user input values
                String uri = "upi://pay?pa=" + upiId + "&pn=Payee%20Name&mc=MerchantCode&mode=02&orgid=000000&paytmqr=PaymentReference&am=" + amount + "&sign=MEYCIQDq96qhUnqvyLsdgxtfdZ11SQP//6F7f7VGJ0qr//lF/gIhAPgTMsopbn4Y9DiE7AwkQEPPnb2Obx5Fcr0HJghd4gzo";

                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                intent.setData(Uri.parse(uri));
                Intent chooser = Intent.createChooser(intent, "Pay with...");
                startActivityForResult(chooser, REQUEST_CODE);
            }
        });

        /*4.2 Defining an on click action for the UPI intent to be carried out by specific apps
            - Clicking on the respective buttons will invoke those specific UPI apps (whenever available)
            - The buttons for specific UPI apps will be displayed when following conditions are met:
                1. App is installed
                 2. App is in the list of apps ready to respond to a UPI intent
                    -> This is how the SMART INTENT will work
                    -> The button will only be visible when the app has a UPI ready user

            This will also log the results of the above two check in debug logs */
        for (int i = 0; i < upiApps.size(); i++) {
            final Button b = upiAppButtons.get(i);
            final String p = upiApps.get(i);
            Log.d("UpiAppVisibility", p + " | " + isAppInstalled(p) + " | " + isAppUpiReady(p));
            if (isAppInstalled(p) && isAppUpiReady(p)) {
                b.setVisibility(View.VISIBLE);
                b.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                        intent.setData(Uri.parse(uri));
                        intent.setPackage(p);
                        startActivityForResult(intent, REQUEST_CODE);
                    }
                });
            } else {
                b.setVisibility(View.INVISIBLE);
            }
        }
    }

    /*This function is to log the returned results of the transaction.
        - One can replace this with the standard UPI intent result handler code. */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE) {
            // Process based on the data in response.
            Log.d("result", data != null ? data.toString() : "null");
            if (data != null) {
                String status = data.getStringExtra("Status");
                Log.d("result", status);
                if (status != null) {
                    Toast.makeText(getApplicationContext(), status, Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    /*
        This function checks if the app with this package name is installed on the phone
    */
    public boolean isAppInstalled(String packageName) {
        PackageManager pm = getPackageManager();
        try {
            pm.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    /*
        This function checks if the app with this package name is responding to UPI intent
        - i.e. the app has a ready UPI user (as per the NPCI recommended implementation)
        - Circular: https://www.npci.org.in/sites/default/files/circular/Circular-73-Payer_App_behaviour_for_Intent_based_transaction_on_UPI.pdf
    */
    public boolean isAppUpiReady(String packageName) {
        boolean appUpiReady = false;
        Intent upiIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("upi://pay"));
        PackageManager pm = getPackageManager();
        List<ResolveInfo> upiActivities = pm.queryIntentActivities(upiIntent, 0);
        for (ResolveInfo a : upiActivities) {
            if (a.activityInfo.packageName.equals(packageName)) {
                appUpiReady = true;
                break;
            }
        }
        return appUpiReady;
    }
}
