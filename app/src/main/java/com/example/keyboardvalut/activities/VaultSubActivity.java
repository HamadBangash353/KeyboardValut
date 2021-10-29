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
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.keyboardvalut.R;
import com.example.keyboardvalut.adapters.DrawerMenuAdapter;
import com.example.keyboardvalut.data.DrawerMenuData;
import com.example.keyboardvalut.databinding.ActivityVaultSubBinding;
import com.example.keyboardvalut.interfaces.ClickListener;
import com.example.keyboardvalut.interfaces.DrawerMenuClickListener;
import com.example.keyboardvalut.utils.ScreenUtils;

public class VaultSubActivity extends AppCompatActivity implements ClickListener, LifecycleObserver {

    Context context;
    Intent intent;
    ActivityVaultSubBinding binding;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_vault_sub);
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
        switch (view.getId()) {
            case R.id.btnPhotosVault:
                passingIntentPhotoVaultActivity();
                break;
            case R.id.btnVideosVault:
                passingIntentVideoVaultActivity();
                break;
            case R.id.btnDocsVault:
                passingIntentDocsVaultActivity();
                break;
            case R.id.btnMediaVault:
                passingIntentMediaVaultActivity();
                break;
            case R.id.btnContactsVault:
                passingIntentContactsVaultActivity();
                break;
            case R.id.btnBack:
                movingToBackActivity();
                break;

        }
    }


    void passingIntentPhotoVaultActivity() {
        intent = new Intent(context, VaultPhotosActivity.class);
        startActivity(intent);
    }

    void passingIntentVideoVaultActivity() {
        intent = new Intent(context, VaultVideosActivity.class);
        startActivity(intent);
    }

    void passingIntentDocsVaultActivity() {
        intent = new Intent(context, VaultDocsActivity.class);
        startActivity(intent);
    }

    void passingIntentMediaVaultActivity() {
        intent = new Intent(context, VaultMediaActivity.class);
        startActivity(intent);
    }

    void passingIntentContactsVaultActivity() {
        intent = new Intent(context, VaultContactsActivity.class);
        startActivity(intent);
    }

    private void movingToBackActivity() {
        intent = new Intent(this, VaultMainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
    }

}