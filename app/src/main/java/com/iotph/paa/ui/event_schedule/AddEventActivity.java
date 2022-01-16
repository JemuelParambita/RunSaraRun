package com.iotph.paa.ui.event_schedule;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;
import com.iotph.paa.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class AddEventActivity extends AppCompatActivity {

    EditText title, date;
    TimePicker timePicker;
    Button submitEvent;

    FirebaseFirestore firebaseFirestore;
    FirebaseAuth auth;

    ProgressDialog progressDialog;
    private RequestQueue requestQueue;
    private String notificationUrl = "https://fcm.googleapis.com/fcm/send";




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);

        getSupportActionBar().setSubtitle(R.string.menu_events_sched);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        auth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        progressDialog = new ProgressDialog(this);

        long timestamp = System.currentTimeMillis();
        title = findViewById(R.id.event_title);
        date = findViewById(R.id.event_date);
        timePicker = findViewById(R.id.event_time);
        timePicker.setIs24HourView(false);
        submitEvent = findViewById(R.id.event_submit);

        requestQueue = Volley.newRequestQueue(this);

        FirebaseMessaging.getInstance().subscribeToTopic("events");

        MaterialDatePicker.Builder builder = MaterialDatePicker.Builder.datePicker();
        builder.setTitleText("Event Date and Time");
        final MaterialDatePicker materialDatePicker = builder.build();

        submitEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                progressDialog.setTitle("Create Event");
                progressDialog.setMessage("Adding event to calendar.");
                progressDialog.setCancelable(false);
                progressDialog.show();


                String eventTitle, eventDate, eventTime;


                eventTitle = title.getText().toString().trim();
                eventDate = date.getText().toString().trim();







                int hour, minute;
                String am_pm;
                if(Build.VERSION.SDK_INT >= 23) {

                    hour = timePicker.getHour();
                    minute = timePicker.getMinute();
                } else  {

                    hour = timePicker.getCurrentHour();
                    minute = timePicker.getCurrentMinute();
                }

                if(hour > 12) {
                    am_pm = "PM";
                    hour = hour - 12;
                } else  {
                    am_pm = "AM";
                    hour = hour +12;
                }

                eventTime = hour + ":" + minute + " " + am_pm;

                HashMap<String, Object> hashMap = new HashMap<>();

                try {
                    SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy hh:mm a");
                    Date date = dateFormat.parse(eventDate+" "+eventTime);
                    long millis = date.getTime();
                    Log.d("TIMESTAMP", eventDate+" "+eventTime + "===" +millis);


                    hashMap.put("event_title", eventTitle);
                    hashMap.put("event_date", eventDate);
                    hashMap.put("event_time", eventTime);
                    hashMap.put("timestamp", millis);
                    hashMap.put("uid", auth.getCurrentUser().getUid());

                } catch (ParseException e) {
                    Log.d("TIMESTAMP", eventDate+" "+eventTime +"===="+e);
                    e.printStackTrace();
                }



                HashMap<String, Object> notificationMap = new HashMap<>();
                notificationMap.put("title", eventTitle);
                notificationMap.put("timestamp", timestamp);
                notificationMap.put("type", "events");


                firebaseFirestore.collection("events").document()
                            .set(hashMap);

                firebaseFirestore.collection("notification_logs").document()
                            .set(notificationMap);


                progressDialog.dismiss();

                sendNotification();



                Toast.makeText(AddEventActivity.this, "Event created successfully.", Toast.LENGTH_LONG).show();
                onBackPressed();
            }
        });

        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                materialDatePicker.show(getSupportFragmentManager(), "DATE_PICKER");


            }
        });

        materialDatePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener() {
            @Override
            public void onPositiveButtonClick(Object selection) {

                date.setText(materialDatePicker.getHeaderText());
            }
        });

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;

    }




    private  void sendNotification() {

        // Json Object
        JSONObject jsonObject = new JSONObject();

        try {

            jsonObject.put("to", "/topics/"+"events");
            JSONObject notificationObject = new JSONObject();
            notificationObject.put("title", "New Event Added:" +title.getText().toString());
            notificationObject.put("body","The event will be on:"+ date.getText().toString());
//            notificationObject.put("click_action", ".ui.event_schedule.AddEventActivity");
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