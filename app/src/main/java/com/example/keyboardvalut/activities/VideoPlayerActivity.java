package com.example.keyboardvalut.activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.MediaController;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.lifecycle.ProcessLifecycleOwner;

import com.example.keyboardvalut.R;
import com.example.keyboardvalut.databinding.ActivityVideoPlayerBinding;
import com.example.keyboardvalut.interfaces.ClickListener;
import com.example.keyboardvalut.utils.ScreenUtils;

import java.io.File;

public class VideoPlayerActivity extends AppCompatActivity implements ClickListener, LifecycleObserver {

    Context context;
    ActivityVideoPlayerBinding binding;

    String videoPath;

    Intent intent;

    int lifeCycleChecker = 0;

    @Override
    protected void onRestart() {
        super.onRestart();
        if (lifeCycleChecker == 1) {
            startActivity(new Intent(this, VaultPasswordEnteringActivity.class));
            finish();

        }

    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    void onMoveToBackground() {
        lifeCycleChecker = 1;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_video_player);
        ScreenUtils.hidingStatusBar(this);

        context = this;

        videoPath = getIntent().getStringExtra("videoPath");
        ProcessLifecycleOwner.get().getLifecycle().addObserver(this);
        try {
//            MediaController mediaController = new MediaController(VideoPlayerActivity.this);
//            mediaController.setAnchorView(binding.videoView1);

            //specify the location of media file
//            Uri uri = Uri.parse(videoPath);


            //Setting MediaController and URI, then starting the videoView
//            binding.videoView1.setMediaController(mediaController);
//            binding.videoView1.setVideoURI(uri);
//            binding.videoView1.requestFocus();
//            binding.videoView1.start();
            binding.andExoPlayerView.setSource(String.valueOf(Uri.fromFile(new File(videoPath))));

        } catch (Exception e) {
            e.printStackTrace();
        }

        binding.setClickHandler(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        binding.andExoPlayerView.releasePlayer();

    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnBack:
                movingToBackActivity();
                break;

        }
    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    public void onBackPressed() {

        super.onBackPressed();
    }

    private void movingToBackActivity() {
        intent = new Intent(this, VaultVideosActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
    }

}