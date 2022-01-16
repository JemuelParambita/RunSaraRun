package com.iotph.paa.ui.profile;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.iotph.paa.R;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class UpdateProfileActivity extends AppCompatActivity {

    private StorageReference storageReference;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth auth;
    private  DocumentReference documentReference;

    private Uri imageUri;
    private String myUri = "";
    private StorageTask uploadTask;


    EditText fullName, address, birthday, mobileNumber, email, age, employer, organization;
    CircleImageView profilePic;
    TextView tvFullname, tvEmail;
    FloatingActionButton selectImage;
    Button profileUpdateButton;
    ProgressDialog progressDialog;
    String profilePicturePath;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);


        auth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference().child("Profile Pic");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setSubtitle(R.string.profile_title);
        getSupportActionBar().setElevation(0);
        getSupportActionBar().setBackgroundDrawable(
                new ColorDrawable(Color.parseColor("#004E3A")));

        fullName = findViewById(R.id.profile_name);
        address = findViewById(R.id.profile_address);
        birthday = findViewById(R.id.profile_bday);
        mobileNumber = findViewById(R.id.profile_mobile);
        email = findViewById(R.id.profile_email);
        profilePic = findViewById(R.id.profile_dp);
        tvFullname = findViewById(R.id.display_fullname);
        tvEmail = findViewById(R.id.display_email);
        selectImage = findViewById(R.id.profile_select_image);
        age = findViewById(R.id.profile_age);
        employer = findViewById(R.id.profile_employer);
        organization = findViewById(R.id.profile_organization);
        profileUpdateButton = findViewById(R.id.profile_update_button);

        progressDialog = new ProgressDialog(this);


        MaterialDatePicker.Builder builder = MaterialDatePicker.Builder.datePicker();
        builder.setTitleText("Set your Birthdate");
        final MaterialDatePicker materialDatePicker = builder.build();

        birthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                materialDatePicker.show(getSupportFragmentManager(), "DATE_PICKER");
            }
        });

        materialDatePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener() {
            @Override
            public void onPositiveButtonClick(Object selection) {

                birthday.setText(materialDatePicker.getHeaderText());
            }
        });


        selectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ImagePicker.Companion.with(UpdateProfileActivity.this)
                        .crop()
                        .compress(1024)
                        .maxResultSize(1080, 1080)
                        .start(10);
            }
        });


        // Get user info
        getUserInfo();

        profileUpdateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                updateProfile();

            }
        });



    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 10 && resultCode == RESULT_OK && data != null) {
            imageUri = data.getData();

            profilePic.setImageURI(imageUri);

        } else {
            Toast.makeText(this, "Something went wrong, Please try again.", Toast.LENGTH_SHORT).show();
        }
    }

    private void getUserInfo() {

        SharedPreferences sharedPreferences = getSharedPreferences("user_info", getApplicationContext().MODE_PRIVATE);
//
        DocumentReference documentReference = firebaseFirestore.collection("users").document(auth.getUid());

        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<DocumentSnapshot> task) {

                if(task.isSuccessful()) {

                    DocumentSnapshot documentSnapshot = task.getResult();

                    fullName.setText(documentSnapshot.getString("fullName"));
                    address.setText(documentSnapshot.getString("address"));
                    birthday.setText(documentSnapshot.getString("birthday"));
                    mobileNumber.setText(documentSnapshot.getString("mobileNumber"));
                    email.setText(documentSnapshot.getString("email"));
                    age.setText(documentSnapshot.getString("age"));
                    employer.setText(documentSnapshot.getString("employer"));
                    organization.setText(documentSnapshot.getString("organization"));

                    tvFullname.setText(documentSnapshot.getString("fullName"));
                    tvEmail.setText(documentSnapshot.getString("email"));
                    if(!documentSnapshot.getString("profilePic").isEmpty()) Picasso.get().load(documentSnapshot.getString("profilePic")).into(profilePic);
                }
            }
        });



    }


    private  void updateProfile() {

        progressDialog.setTitle("Update Profile");
        progressDialog.setMessage("Your profile is being updated.");
        progressDialog.setCancelable(false);
        progressDialog.show();



        String profile_name, profile_bday, profile_address, profile_age,
        profile_mobile, profile_email, profile_employer, profile_org;

        profile_name = fullName.getText().toString().trim();
        profile_bday = birthday.getText().toString().trim();
        profile_address = address.getText().toString().trim();
        profile_age = age.getText().toString().trim();
        profile_email = email.getText().toString().trim();
        profile_employer = employer.getText().toString().trim();
        profile_org = organization.getText().toString().trim();
        profile_mobile = mobileNumber.getText().toString().trim();



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
                @Override
                public void onComplete(@NonNull @NotNull Task task) {

                    if(task.isSuccessful()) {


                        Uri downloadUri = (Uri) task.getResult();
                        myUri = downloadUri.toString();



                        HashMap<String, Object> hashMap = new HashMap<>();
                        hashMap.put("profilePic", myUri);
                        hashMap.put("fullName", profile_name);
                        hashMap.put("email", profile_email);
                        hashMap.put("address", profile_address);
                        hashMap.put("birthday", profile_bday);
                        hashMap.put("age", profile_age);
                        hashMap.put("employer", profile_employer);
                        hashMap.put("organization", profile_org);
                        hashMap.put("mobileNumber", profile_mobile);

                        firebaseFirestore.collection("users").document(auth.getCurrentUser().getUid())
                                .update(hashMap);

                        progressDialog.dismiss();

                        Toast.makeText(UpdateProfileActivity.this, "Profile has been updated.", Toast.LENGTH_SHORT).show();
                        documentReference = firebaseFirestore.collection("users").document(auth.getCurrentUser().getUid());

                        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                            @Override
                            public void onEvent(@Nullable @org.jetbrains.annotations.Nullable DocumentSnapshot value, @Nullable @org.jetbrains.annotations.Nullable FirebaseFirestoreException error) {
                                if(value.exists()) {

                                    profilePicturePath = value.getString("profilePic");
                                    Picasso.get().load(profilePicturePath).into(selectImage);
                                }
                            }
                        });
                    }

                }
            });
        } else {

            progressDialog.setTitle("Update Profile");
            progressDialog.setMessage("Your profile is being updated.");
            progressDialog.setCancelable(false);
            progressDialog.show();

            HashMap<String, Object> hashMap = new HashMap<>();

            hashMap.put("fullName", profile_name);
            hashMap.put("email", profile_email);
            hashMap.put("address", profile_address);
            hashMap.put("birthday", profile_bday);
            hashMap.put("age", profile_age);
            hashMap.put("employer", profile_employer);
            hashMap.put("organization", profile_org);
            hashMap.put("mobileNumber", profile_mobile);

            firebaseFirestore.collection("users").document(auth.getCurrentUser().getUid())
                    .update(hashMap);

            getUserInfo();

            progressDialog.dismiss();

            Toast.makeText(UpdateProfileActivity.this, "Profile has been updated.", Toast.LENGTH_SHORT).show();

        }

    }

}