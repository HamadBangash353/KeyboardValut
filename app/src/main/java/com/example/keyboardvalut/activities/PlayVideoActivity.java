package com.example.keyboardvalut.activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.lifecycle.ProcessLifecycleOwner;

import com.example.keyboardvalut.R;
import com.example.keyboardvalut.adapters.DrawerMenuAdapter;
import com.example.keyboardvalut.data.DrawerMenuData;
import com.example.keyboardvalut.databinding.ActivityPlayVideosBinding;
import com.example.keyboardvalut.databinding.ActivityVaultVideosBinding;
import com.example.keyboardvalut.interfaces.ClickListener;
import com.example.keyboardvalut.interfaces.DrawerMenuClickListener;
import com.example.keyboardvalut.utils.ScreenUtils;

public class PlayVideoActivity extends AppCompatActivity implements ClickListener, LifecycleObserver {

    Context context;
    Intent intent;
    ActivityPlayVideosBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_play_videos);
        ScreenUtils.hidingStatusBar(this);
        ProcessLifecycleOwner.get().getLifecycle().addObserver(this);
        context = this;
        binding.setClickHandler(this);
    }

    int lifeCycleChecker = 0;
    @Override
    protected void onRestart() {
        super.onRestart();
        if (lifeCycleChecker==1)
        {
            startActivity(new Intent(this,VaultPasswordEnteringActivity.class));
            finish();

        }

    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    void onMoveToBackground() {
        lifeCycleChecker = 1;
    }


    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btnPhotosVault) {
            intent = new Intent(context, VaultSettingsActivity.class);
            startActivity(intent);
        }
    }



}