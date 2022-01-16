package com.iotph.paa.ui.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import android.Manifest;
import android.annotation.SuppressLint;

import androidx.core.app.ActivityCompat;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;

import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.util.Patterns;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.iotph.paa.MainActivity;
import com.iotph.paa.R;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class RegisterActivity extends AppCompatActivity {


    // Initialize
    FirebaseFirestore db;
    FirebaseAuth auth;
    //String currentLatitude = "";
    //String currentLongitude = "";
    //String currentAddress = "";

    //FusedLocationProviderClient fusedLocationProviderClient;

    String name, emailadd, psswd,confirmpsswd;

    EditText fullname, email, passwd, confirmpasswd;
    Button createAccount, loginbutton;
    TextView login, createAccountText ;

    ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        getWindow().setStatusBarColor(Color.TRANSPARENT);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setContentView(R.layout.activity_register);

        getSupportActionBar().hide();


        //LOCATION

        //fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(RegisterActivity.this);

        /*if(ActivityCompat.checkSelfPermission(RegisterActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(RegisterActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED) {

            getLocation();

        } else {

            ActivityCompat.requestPermissions(RegisterActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION}, 100);
        }

*/

        fullname =  findViewById(R.id.fullname);
        email = findViewById(R.id.username);
        passwd = findViewById(R.id.password);
        confirmpasswd = findViewById(R.id.confirmpassword);
        createAccount = findViewById(R.id.signupButton);
        login = findViewById(R.id.login);
        loginbutton = findViewById(R.id.loginButton);
        createAccountText = findViewById(R.id.createAccountText);

/*

        // Typecasting
        RegisterBtn =  findViewById(R.id.register);
        FullName = findViewById(R.id.register_name);
        Address =  findViewById(R.id.register_address);
        Birthday =  findViewById(R.id.register_birthplace);
        MobileNumber = findViewById(R.id.register_mobile_number);
        Email =  findViewById(R.id.register_social);
        Password = findViewById(R.id.register_password);
        backToLogin = findViewById(R.id.back_to_login);

        MaterialDatePicker.Builder builder = MaterialDatePicker.Builder.datePicker();
        builder.setTitleText("Set your Birthdate");
        final MaterialDatePicker materialDatePicker = builder.build();

*/

        // Firebase auth instance
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Creating Account.");
        progressDialog.setCancelable(false);

        createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

              //  String fullName, address, birthday, mobileNumber,  email, password;


                name = fullname.getText().toString().trim();
                //address = Address.getText().toString().trim();
                //birthday = Birthday.getText().toString().trim();
                //mobileNumber = MobileNumber.getText().toString().trim();
                emailadd = email.getText().toString().trim();
                psswd = passwd.getText().toString().trim();
                confirmpsswd = confirmpasswd.getText().toString().trim();

                if(name.isEmpty()) {

                    fullname.setError("Enter valid Full Name");
                    fullname.requestFocus();

                } else if(!Patterns.EMAIL_ADDRESS.matcher(emailadd).matches()) {

                    email.setError("Enter valid Email.");
                    email.requestFocus();

                } else if(psswd.isEmpty()) {

                    passwd.setError("Password is required.");
                    passwd.requestFocus();

                }else if(confirmpsswd.isEmpty()) {

                    confirmpasswd.setError("Password is required.");
                    confirmpasswd.requestFocus();

                }else if(!psswd.equals(confirmpsswd)){

                    confirmpasswd.setError("Password is not the same.");
                    confirmpasswd.requestFocus();
                }

                else {

                    progressDialog.show();
                    auth.createUserWithEmailAndPassword(emailadd, psswd).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull @NotNull Task<AuthResult> task) {

                            if(task.isSuccessful()) {

                                // update account
                                long timestamp = System.currentTimeMillis();

                                // get current user uid
                                String uid = auth.getUid();

                                // Prepare data
                                HashMap<String, Object> hashMap = new HashMap<>();

                                hashMap.put("uid", uid);
                                hashMap.put("email", emailadd);
                                hashMap.put("name", name);
                                hashMap.put("verified", "0");
                                hashMap.put("timestamp", timestamp);

                                db.collection("users_paa").document(uid)
                                            .set(hashMap)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {

                                                    progressDialog.dismiss();
                                                    Toast.makeText(RegisterActivity.this, "Registered Successfully.", Toast.LENGTH_LONG).show();
                                                    startActivity(new Intent(RegisterActivity.this, VerifyActivity.class));
                                                    finish();
                                                }
                                            }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull @NotNull Exception e) {
                                            Toast.makeText(RegisterActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });



                            } else {

                                progressDialog.dismiss();
                                Toast.makeText(RegisterActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull @NotNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(RegisterActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }






            }
        });


        /*Birthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                materialDatePicker.show(getSupportFragmentManager(), "DATE_PICKER");
            }
        });

        materialDatePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener() {
            @Override
            public void onPositiveButtonClick(Object selection) {

                Birthday.setText(materialDatePicker.getHeaderText());
            }
        });
*/
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        confirmpasswd.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || actionId == EditorInfo.IME_ACTION_DONE) {
                    createAccount.performClick();
                }
                return false;
            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull @NotNull String[] permissions, @NonNull @NotNull int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100 && grantResults.length > 0 && (grantResults[0] + grantResults[1]
                == PackageManager.PERMISSION_GRANTED)) {

           // getLocation();
        } else {
            Toast.makeText(getApplicationContext(), "Permission Denied.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();

//        if(auth.getCurrentUser() != null) {
//            startActivity(new Intent(RegisterActivity.this, MainActivity.class));
//            finish();
//        }
    }


   /* @SuppressLint("MissingPermission")
    public  void getLocation() {

        LocationManager locationManager = (LocationManager) getSystemService(
                Context.LOCATION_SERVICE
        );

        if(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {

            fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                @Override
                public void onComplete(@NonNull @NotNull Task<Location> task) {

                    Location location = task.getResult();
                    if(location != null) {

                        double currentLati = location.getLatitude();
                        double currentLongi = location.getLongitude();

                        String addressLine = getCompleteAddress(currentLati, currentLongi);
                        currentAddress = addressLine;
                        currentLatitude = String.valueOf(currentLati);
                        currentLongitude = String.valueOf(currentLongi);
                        Address.setText(String.valueOf(addressLine));


                        Toast.makeText(RegisterActivity.this, addressLine, Toast.LENGTH_SHORT).show();
                    } else {

                        LocationRequest locationRequest = new LocationRequest()
                                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                                .setInterval(10000)
                                .setNumUpdates(1);

                        LocationCallback locationCallback = new LocationCallback() {
                            @Override
                            public void onLocationResult(@NonNull @NotNull LocationResult locationResult) {
                                super.onLocationResult(locationResult);

                                Location locationCallback = locationResult.getLastLocation();
                                String address = getCompleteAddress(locationCallback.getLatitude(), locationCallback.getLongitude());
                                Toast.makeText(RegisterActivity.this, address, Toast.LENGTH_SHORT).show();

                            }


                        };


                        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());
                    }
                }
            });


        } else {

            startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                    .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        }


    }

    private String getCompleteAddress(double LATITUDE, double LONGITUDE) {

        String addressLine = "";
        Geocoder geocoder = new Geocoder(RegisterActivity.this, Locale.getDefault());

        try {
            List<android.location.Address> addresses = geocoder.getFromLocation(LATITUDE,LONGITUDE, 1);
            if(addresses != null) {
                Address returnAddress = addresses.get(0);
                StringBuilder stringBuilder = new StringBuilder("");

                for (int i = 0; i <= returnAddress.getMaxAddressLineIndex(); i++) {



                    stringBuilder.append(returnAddress.getAddressLine(i)).append("\n");
                }

                addressLine = stringBuilder.toString();
            } else  {

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return  addressLine;
    }*/

}