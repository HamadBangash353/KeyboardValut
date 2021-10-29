package com.example.keyboardvalut.activities;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.lifecycle.ProcessLifecycleOwner;

import com.example.keyboardvalut.R;
import com.example.keyboardvalut.data.HowToUseData;
import com.example.keyboardvalut.databinding.ActivityHowToUseBinding;
import com.example.keyboardvalut.databinding.ActivityPasswordResetBinding;
import com.example.keyboardvalut.interfaces.ClickListener;
import com.example.keyboardvalut.utils.ScreenUtils;
import com.example.keyboardvalut.utils.SharedPrefUtil;

import java.util.List;

public class HowToUseActivity extends AppCompatActivity implements ClickListener, LifecycleObserver {

    Context context;

    ActivityHowToUseBinding binding;

    SharedPrefUtil prefUtil;

    List<Integer> howToUseScreensList;

    int screenCounter = 1;

    int lifeCycleChecker = 0;

    @Override
    protected void onRestart() {
        super.onRestart();
        if (lifeCycleChecker == 1 && !prefUtil.getPassword().equals("")) {
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
        binding = DataBindingUtil.setContentView(this, R.layout.activity_how_to_use);
        ScreenUtils.hidingStatusBar(this);

        context = this;
        prefUtil = new SharedPrefUtil(context);

        howToUseScreensList = HowToUseData.howToUseScreensData();
        ProcessLifecycleOwner.get().getLifecycle().addObserver(this);

        binding.setClickHandler(this);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnNext:
                movingToNextScreen();
                break;
            case R.id.btnSkip:
                finish();
                break;
        }
    }


    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setMessage("Do you really want to exit?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent intent = new Intent(Intent.ACTION_MAIN);
                        intent.addCategory(Intent.CATEGORY_HOME);
                        startActivity(intent);
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }


    private void movingToNextScreen() {

        if (screenCounter < howToUseScreensList.size()) {
            binding.ivScreens.setImageResource(howToUseScreensList.get(screenCounter));
            screenCounter++;
        } else {
            finish();
        }

    }


}