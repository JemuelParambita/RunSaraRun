package com.iotph.paa;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.MutableLiveData;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.iotph.paa.databinding.ActivityMainBinding;
//import com.iotph.rsr.ui.form_survey.FormSurveyData;

import com.iotph.paa.ui.home.HomeFragment;
import com.iotph.paa.ui.info_board.InfoBoardFragment;
import com.iotph.paa.ui.login.LoginActivity;
import com.iotph.paa.ui.chat.ChatActivity;
import com.iotph.paa.ui.member_profile.MemberProfileFragment;
import com.iotph.paa.ui.notif.InboxActivity;
import com.iotph.paa.ui.profile.IDFullscreenActivity;
import com.iotph.paa.ui.profile.UpdateProfileActivity;

import org.jetbrains.annotations.NotNull;

import de.hdodenhof.circleimageview.CircleImageView;


public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;
    private MutableLiveData<String> userFullName;
    FirebaseAuth auth;
    FirebaseFirestore db;

    ImageView qrCodeImage;
    CircleImageView imageView;
    ProgressDialog progressDialog;

    ImageView selectReportImage;

    private Uri imageUri;
    private String myUri = "";
    private StorageTask uploadTask;
    private StorageReference storageReference;
    private  DocumentReference documentReference;

    String profilePicturePath;

    private Toast toast;
    private long lastBackPressTime = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        /*// FCM
        FirebaseMessaging.getInstance().subscribeToTopic("events")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<Void> task) {

                        String message = "Done";

                        if(!task.isSuccessful()) {
                            message = "Failed";
                        }
                    }
                });
*/
        // Initialize firebase auth
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference().child("Profile Pic");




        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Logging out");
        progressDialog.setCancelable(false);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarMain.toolbar);


        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_profile)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        BottomNavigationView navViewBottom = findViewById(R.id.nav_view_bottom);
        navViewBottom.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {


                switch (item.getItemId()) {
                    //case R.id.navigation_chat:
                        //Toast.makeText(getApplicationContext(), " CHAT Coming Soon", Toast.LENGTH_SHORT).show();
                    //    Intent intent=new Intent(MainActivity.this, ChatActivity.class);
                   //     startActivity(intent);

                   // break;
                    case R.id.navigation_home:
                        InfoBoardFragment infoBoardFragment = new InfoBoardFragment();
                        FragmentManager fragmentManagerinfoB = getSupportFragmentManager();
                        fragmentManagerinfoB.popBackStackImmediate();
                        FragmentTransaction fragmentTransactioninfoB = fragmentManagerinfoB.beginTransaction();
                        fragmentTransactioninfoB.replace(R.id.nav_host_fragment_content_main, infoBoardFragment).commit();
                        break;
                    case R.id.navigation_memberprofile:
                        MemberProfileFragment memberProfileFragment = new MemberProfileFragment();
                        FragmentManager fragmentManagermemberP = getSupportFragmentManager();
                        fragmentManagermemberP.popBackStackImmediate();
                        FragmentTransaction fragmentTransactionmemberP = fragmentManagermemberP.beginTransaction();
                        fragmentTransactionmemberP.replace(R.id.nav_host_fragment_content_main, memberProfileFragment).commit();
                        break;

                        //getSupportActionBar().setSubtitle("");
                       // HomeFragment homeFragment = new HomeFragment();
                       // FragmentManager fragmentManager = getSupportFragmentManager();
                       // fragmentManager.popBackStackImmediate();
                      //  FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                      //  fragmentTransaction.replace(R.id.nav_host_fragment_content_main, homeFragment).commit();
                     //   break;

                   // case R.id.navigation_infoBoard:
                  //      InfoBoardFragment infoBoardFragment = new InfoBoardFragment();
                   //     FragmentManager fragmentManagerinfoB = getSupportFragmentManager();
                    //    fragmentManagerinfoB.popBackStackImmediate();
                   //     FragmentTransaction fragmentTransactioninfoB = fragmentManagerinfoB.beginTransaction();
                   //     fragmentTransactioninfoB.replace(R.id.nav_host_fragment_content_main, infoBoardFragment).commit();
                   //     break;
                }
                return true;
            }
        });
        // Navigation drawer
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull @NotNull MenuItem menuItem) {

                int id = menuItem.getItemId();
                progressDialog.show();
                if(id == R.id.navigation_logout) {
                    auth.signOut();
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                    finish();
                }
                if(id == R.id.nav_home) {
                    getSupportActionBar().setSubtitle("");
                    HomeFragment homeFragment = new HomeFragment();
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    fragmentManager.popBackStackImmediate();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.nav_host_fragment_content_main, homeFragment).commit();
                    drawer.closeDrawers();
                }
                if(id == R.id.nav_profile) {
                    Intent intent=new Intent(MainActivity.this, UpdateProfileActivity.class);
                    startActivity(intent);
                }

                progressDialog.dismiss();

                return true;
            }
        });

        //RelativeLayout view =  findViewById(R.id.img_id_relative);
        //view.setOnClickListener(new View.OnClickListener() {
        //    @Override
        //    public void onClick(View v) {
               // Intent intent=new Intent(MainActivity.this, IDFullscreenActivity.class);
               // startActivity(intent);
         //   }
        //});


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        /*if(requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK && data != null) {

            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            imageUri = result.getUri();

           // imageView.setImageURI(imageUri);
        } else {
            Toast.makeText(this, "Something went wrong, Please try again.", Toast.LENGTH_SHORT).show();
        }*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {

            case R.id.notif:
                //Toast.makeText(getApplicationContext(), " INBOX Coming Soon", Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(MainActivity.this, InboxActivity.class);
                startActivity(intent);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }


    @Override
    public void onBackPressed() {

        if(this.lastBackPressTime < System.currentTimeMillis() - 4000) {

            toast = Toast.makeText(this, "Press Back Again to Exit App", 4000);
            toast.show();

            this.lastBackPressTime = System.currentTimeMillis();
        } else {

            if(toast != null) {
                toast.cancel();
            }

            super.onBackPressed();
        }


//        Fragment fragment = (EventScheduleFragment) getFragmentManager().findFragmentByTag(".EventScheduleFragment");

    }
}