package com.example.keyboardvalut.activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.example.keyboardvalut.R;
import com.example.keyboardvalut.databinding.ActivityPasswordResetBinding;
import com.example.keyboardvalut.databinding.ActivityPasswordSignUpBinding;
import com.example.keyboardvalut.interfaces.ClickListener;
import com.example.keyboardvalut.utils.ScreenUtils;
import com.example.keyboardvalut.utils.SharedPrefUtil;

public class ResetPasswordActivity extends AppCompatActivity implements ClickListener {

    Context context;
    Intent intent;

    ActivityPasswordResetBinding binding;

    SharedPrefUtil prefUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_password_reset);
        ScreenUtils.hidingStatusBar(this);

        context = this;
        prefUtil = new SharedPrefUtil(context);


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
        } else {
            if (password.equals(confirmPassword)) {
                if (password.length() != 4) {
                    Toast.makeText(context, "Password must be 4 numbers", Toast.LENGTH_SHORT).show();
                } else {
                    prefUtil.setPassword(password);
                    passingIntent();
                }
            } else {
                Toast.makeText(context, "Password is not matching", Toast.LENGTH_SHORT).show();
            }
        }
    }

    void passingIntent() {
        intent = new Intent(context, ConfirmResetPasswordActivity.class);
        startActivity(intent);
        if (true) {
            finish();
        }
    }

}