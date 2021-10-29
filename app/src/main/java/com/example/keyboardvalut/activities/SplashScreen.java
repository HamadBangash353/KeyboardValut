package com.example.keyboardvalut.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.bumptech.glide.Glide;
import com.example.keyboardvalut.R;
import com.example.keyboardvalut.databinding.ActivitySplashScreenBinding;
import com.example.keyboardvalut.utils.ScreenUtils;


public class SplashScreen extends AppCompatActivity {

    ActivitySplashScreenBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_splash_screen);

        ScreenUtils.hidingStatusBar(this);

        Glide
                .with(this)
                .load(R.drawable.splash_bg)
                .centerCrop()
                .into(binding.ivSplash);

        timeToMoveToMainActivity();
    }


    private void timeToMoveToMainActivity() {
        new Handler().postDelayed(() -> {
            Intent mainIntent = new Intent(SplashScreen.this, PasswordSignUpActivity.class);
            startActivity(mainIntent);
            finish();
        }, 5000);
    }
}