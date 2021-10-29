package com.example.keyboardvalut.activities;

import android.annotation.SuppressLint;
import android.content.Context;
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
import com.example.keyboardvalut.databinding.ActivityPasswordResetBinding;
import com.example.keyboardvalut.databinding.ActivityPasswordSignUpBinding;
import com.example.keyboardvalut.interfaces.ClickListener;
import com.example.keyboardvalut.utils.ScreenUtils;
import com.example.keyboardvalut.utils.SharedPrefUtil;

public class ResetPasswordActivity extends AppCompatActivity implements ClickListener, LifecycleObserver {

    Context context;
    Intent intent;

    ActivityPasswordResetBinding binding;

    SharedPrefUtil prefUtil;

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
        binding = DataBindingUtil.setContentView(this, R.layout.activity_password_reset);
        ScreenUtils.hidingStatusBar(this);

        context = this;
        prefUtil = new SharedPrefUtil(context);
        ProcessLifecycleOwner.get().getLifecycle().addObserver(this);

        binding.setClickHandler(this);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnProceed:
                settingPassword();
                break;
        }
    }

    void settingPassword() {
        String password = binding.etPassword.getText().toString();
        String confirmPassword = binding.etConfirmPassword.getText().toString();
        if (password.isEmpty() || confirmPassword.isEmpty()) {
            Toast.makeText(context, "Fields must be filled", Toast.LENGTH_SHORT).show();
        } else if (prefUtil.getPassword().equals(password)) {
            Toast.makeText(context, "Password is same", Toast.LENGTH_SHORT).show();
        } else {
            if (password.equals(confirmPassword)) {
                if (password.length() != 4) {
                    Toast.makeText(context, "Password must be 4 numbers", Toast.LENGTH_SHORT).show();
                } else {

                    passingIntent(password);
                }
            } else {
                Toast.makeText(context, "Password is not matching", Toast.LENGTH_SHORT).show();
            }
        }
    }

    void passingIntent(String password) {
        intent = new Intent(context, ConfirmResetPasswordActivity.class);
        intent.putExtra("password",password);
        startActivity(intent);
        if (true) {
            finish();
        }
    }

}