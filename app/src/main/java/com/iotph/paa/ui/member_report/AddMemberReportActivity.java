package com.iotph.paa.ui.member_report;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.iotph.paa.R;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class AddMemberReportActivity extends AppCompatActivity {

    ImageView selectImageView;
    ImageView imageContainer;
    ProgressDialog progressDialog;

    private StorageReference storageReference;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth auth;
    //private GoogleApi googleApi;
    private Uri imageUri;
    private String myUri = "";
    private StorageTask uploadTask;

    private DocumentReference documentReference;

    EditText Title, Description, Address;
    Button submitReport;

    //private static final int REQUEST_LOCATION = 1;
    //private LocationManager locationManager;
    private RequestQueue requestQueue;
    private String notificationUrl = "https://fcm.googleapis.com/fcm/send";

    String currentLatitude = "";
    String currentLongitude = "";
    String currentAddress = "";

    FusedLocationProviderClient fusedLocationProviderClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_member_report);
        getSupportActionBar().setSubtitle(R.string.action_add_report);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        requestQueue = Volley.newRequestQueue(this);

        FirebaseMessaging.getInstance().subscribeToTopic("reports");

        auth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference().child("public/reports");

        progressDialog = new ProgressDialog(this);

        selectImageView = findViewById(R.id.IVPreviewImage);
        imageContainer = findViewById(R.id.selectedImage);

        Title = findViewById(R.id.report_title);
        Description = findViewById(R.id.report_description);
        Address = findViewById(R.id.report_location);

        submitReport = findViewById(R.id.submitReport);





        selectImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ImagePicker.Companion.with(AddMemberReportActivity.this)
                        .crop()
                        .compress(1024)
                        .maxResultSize(1080, 1080)
                        .start(10);
            }
        });


        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(AddMemberReportActivity.this);

        if(ActivityCompat.checkSelfPermission(AddMemberReportActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
        && ActivityCompat.checkSelfPermission(AddMemberReportActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) ==
        PackageManager.PERMISSION_GRANTED) {

            getLocation();

        } else {

            ActivityCompat.requestPermissions(AddMemberReportActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION}, 100);
        }


//        Address.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
//
//                if(!locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
//                    startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
//                } else {
//                    getLocation();
//                }
//            }
//        });





        submitReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(Title.getText().toString().isEmpty() || Description.getText().toString().isEmpty() || imageUri == null){
                    Toast.makeText(AddMemberReportActivity.this, "Please fill all fields.", Toast.LENGTH_SHORT).show();
                }
                else {
                    submitReport();
                }
            }
        });
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull @NotNull String[] permissions, @NonNull @NotNull int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100 && grantResults.length > 0 && (grantResults[0] + grantResults[1]
                == PackageManager.PERMISSION_GRANTED)) {

            getLocation();
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
    protected void onActivityResult(int requestCode, int resultCode, @Nullable  Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 10 && resultCode == RESULT_OK && data != null) {
            imageUri = data.getData();

            imageContainer.setImageURI(imageUri);

        } else {
            Toast.makeText(this, "Something went wrong, Please try again.", Toast.LENGTH_SHORT).show();
        }


    }

    @SuppressLint("MissingPermission")
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


                       Toast.makeText(AddMemberReportActivity.this, addressLine, Toast.LENGTH_SHORT).show();
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
                               Toast.makeText(AddMemberReportActivity.this, address, Toast.LENGTH_SHORT).show();

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
        Geocoder geocoder = new Geocoder(AddMemberReportActivity.this, Locale.getDefault());

        try {
            List<Address> addresses = geocoder.getFromLocation(LATITUDE,LONGITUDE, 1);
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
    }


    private void submitReport() {

        progressDialog.setTitle("Submit Report");
        progressDialog.setMessage("Report is being submitted.");
        progressDialog.setCancelable(false);
        progressDialog.show();

        String title, description, address;

        title = Title.getText().toString().trim();
        description = Description.getText().toString().trim();
        address = Address.getText().toString().trim();
        long timestamp = System.currentTimeMillis();

        if(imageUri != null) {

            final StorageReference fileReference = storageReference.child(auth.getCurrentUser().getUid()+ ".jpg");

            uploadTask = fileReference.putFile(imageUri);

            uploadTask.continueWithTask(new Continuation() {
                @Override
                public Object then(@NonNull @NotNull Task task) throws Exception {

                    if(!task.isSuccessful()) {
                        throw task.getException();
                    }
                    return fileReference.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener() {
                @SuppressLint("UseCompatLoadingForDrawables")
                @Override
                public void onComplete(@NonNull @NotNull Task task) {

                    if(task.isSuccessful()) {

                        Uri downloadUri = (Uri) task.getResult();
                        myUri = downloadUri.toString();
                        HashMap<String, Object> hashMap = new HashMap<>();
                        hashMap.put("DESCRIPTION", description);
                        hashMap.put("address", address);
                        hashMap.put("latitude", currentLatitude);
                        hashMap.put("longitude", currentLongitude);
                        hashMap.put("datetime", timestamp);
                        hashMap.put("title", title);
                        hashMap.put("filename", myUri);
                        hashMap.put("uid", auth.getCurrentUser().getUid());
                        hashMap.put("type", "Single Picture");


                        HashMap<String, Object> notificationMap = new HashMap<>();
                        notificationMap.put("title", title);
                        notificationMap.put("timestamp", timestamp);
                        notificationMap.put("type", "reports");

                        firebaseFirestore.collection("reports").document()
                                .set(hashMap);

                        firebaseFirestore.collection("notification_logs").document()
                                .set(notificationMap);

                        progressDialog.dismiss();



                        sendNotification();

                        Toast.makeText(AddMemberReportActivity.this, "Report Submitted Successfully.", Toast.LENGTH_SHORT ).show();

                        Title.getText().clear();
                        Description.getText().clear();
                        Address.getText().clear();
                        imageUri = null;
                        imageContainer.setImageResource(R.drawable.ic_baseline_photo_size_select_actual_24);
                        onBackPressed();
                    }
                }
            });
        }


    }

    private  void sendNotification() {

        // Json Object
        JSONObject jsonObject = new JSONObject();

        try {

            jsonObject.put("to", "/topics/"+"reports");
            JSONObject notificationObject = new JSONObject();
            notificationObject.put("title","New Report Added:"+ Title.getText().toString());
            notificationObject.put("body", Description.getText().toString());
            jsonObject.put("notification", notificationObject);


            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, notificationUrl,
                    jsonObject,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            }){

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                   Map<String, String> header = new HashMap<>();
                   header.put("content-type", "application/json");
                   header.put("authorization", "key=AAAAFPXvdxE:APA91bGE9LoYKBGxhVQkVeCHcQ29-1jXPcNymiiyUZBuuDy1xLjNDXyVRR6ed59uxui9VnKmJaWWh6ltj3M1SSPsLtSzCV4kbBXyC7RyFSbhf2MxTRkJ2PttDaLs9FkyeO_9Wdi5SaS1");

                   return header;
                }
            };

            requestQueue.add(jsonObjectRequest);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}