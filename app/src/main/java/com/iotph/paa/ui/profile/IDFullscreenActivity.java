package com.iotph.paa.ui.profile;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.iotph.paa.R;
import com.iotph.paa.ui.home.HomeViewModel;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class IDFullscreenActivity extends AppCompatActivity {
    private HomeViewModel homeViewModel;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_idfullscreen);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        getSupportActionBar().hide();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        IDFullscreenActivity.this.setRequestedOrientation( ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        LinearLayout view =  findViewById(R.id.img_id_relative);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        homeViewModel = new ViewModelProvider(this, new ViewModelProvider.NewInstanceFactory()).get(HomeViewModel.class);


        final TextView textView = findViewById(R.id.textName);
        homeViewModel.getText().observe(IDFullscreenActivity.this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });


        final TextView textViewBday = findViewById(R.id.textBday);
        homeViewModel.getTextBday().observe(IDFullscreenActivity.this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textViewBday.setText(s);
            }
        });


        final CircleImageView profilePic = findViewById(R.id.update_profile_pic);
        homeViewModel.getmProfileImg().observe(IDFullscreenActivity.this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                // Log.d("PROFILE",s);
                if(s.length() > 5) Picasso.get().load(s).into(profilePic);

            }
        });

        final ImageView QrPic = findViewById(R.id.img_QR);
        homeViewModel.getmQRImg().observe(IDFullscreenActivity.this, new Observer<Bitmap>() {
            @Override
            public void onChanged(@Nullable Bitmap s) {
                QrPic.setImageBitmap(s);
            }
        });



    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;

    }
}