package com.example.keyboardvalut.activities;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.davemorrissey.labs.subscaleview.ImageSource;
import com.example.keyboardvalut.R;
import com.example.keyboardvalut.databinding.ActivityImageViewingBinding;
import com.example.keyboardvalut.interfaces.ClickListener;
import com.example.keyboardvalut.utils.ImageRotationUtil;
import com.example.keyboardvalut.utils.ScreenUtils;

public class ImageVeiwingActivity extends AppCompatActivity implements ClickListener {

    Context context;
    ActivityImageViewingBinding binding;

    String imagePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_image_viewing);
        ScreenUtils.hidingStatusBar(this);

        context = this;

        if (getIntent().getStringExtra("activityTag").equals("media")) {
            imagePath = getIntent().getStringExtra("path");
            BitmapFactory.Options Options = new BitmapFactory.Options();
            Options.inSampleSize = 3;
            Options.inJustDecodeBounds = false;
            Bitmap myBitmap = BitmapFactory.decodeFile(imagePath);
            Bitmap rotatedBitMap = ImageRotationUtil.rotatedBitmap(imagePath, myBitmap);
            binding.ivImage.setImage(ImageSource.bitmap(rotatedBitMap));
        } else {
            imagePath = getIntent().getStringExtra("path");
            binding.ivImage.setImage(ImageSource.uri(imagePath));
        }

        binding.setClickHandler(this);
    }


    @Override
    public void onClick(View view) {

    }
}