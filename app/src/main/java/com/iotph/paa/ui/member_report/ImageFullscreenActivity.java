package com.iotph.paa.ui.member_report;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.WindowManager;

import com.github.chrisbanes.photoview.PhotoView;
import com.github.chrisbanes.photoview.PhotoViewAttacher;

import com.iotph.paa.R;
import com.squareup.picasso.Picasso;

public class ImageFullscreenActivity extends AppCompatActivity {
    PhotoViewAttacher mAttacher;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_fullscreen);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        getSupportActionBar().hide();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //ImageView imageView = findViewById(R.id.photo_view);
        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {
            String title = bundle.getString("img");
            PhotoView photoView = (PhotoView) findViewById(R.id.photo_view);
            Picasso.get().load(title).into(photoView);
            //Glide.with(this).load(title).into(photoView);

          //  Glide.with(this).load(imageUrl).into(photoView);
          //  Glide.with(this).load(title).into(imageView);
            mAttacher = new PhotoViewAttacher(photoView);
        }
        // If you later call mImageView.setImageDrawable/setImageBitmap/setImageResource/etc then you just need to call
        mAttacher.update();
        //imageView.setOnClickListener(new View.OnClickListener() {
        //    @Override
        //    public void onClick(View v) {
        //        onBackPressed();
        //    }
        //});
    }



        @Override
    public boolean onSupportNavigateUp() {

        onBackPressed();

        return true;
    }

}