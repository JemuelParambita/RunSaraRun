package com.iotph.paa;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;

import com.bumptech.glide.Glide;
import com.github.chrisbanes.photoview.PhotoView;
import com.google.firebase.auth.FirebaseAuth;
import com.iotph.paa.ui.login.LoginActivity;
import com.squareup.picasso.Picasso;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class SplashActivity extends AppCompatActivity {
    Handler handler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        getWindow().setStatusBarColor(Color.TRANSPARENT);
        getSupportActionBar().hide();

        handler=new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {


                    if(FirebaseAuth.getInstance().getCurrentUser() != null) {
                        startActivity(new Intent(SplashActivity.this, MainActivity.class));

                    }else {
                        startActivity(new Intent(SplashActivity.this, HomeActivity.class));
                    }
                finish();
            }
        },3000);

    }
}