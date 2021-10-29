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
import com.example.keyboardvalut.databinding.ActivityConfirmResetPasswordBinding;
import com.example.keyboardvalut.interfaces.ClickListener;
import com.example.keyboardvalut.utils.ScreenUtils;
import com.example.keyboardvalut.utils.SharedPrefUtil;

public class ConfirmResetPasswordActivity extends AppCompatActivity implements ClickListener {

    Context context;
    Intent intent;

    ActivityConfirmResetPasswordBinding binding;

    SharedPrefUtil prefUtil;

    String getPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_confirm_reset_password);
        ScreenUtils.hidingStatusBar(this);

        context = this;
        prefUtil = new SharedPrefUtil(context);

        getPassword=getIntent().getStringExtra("password");


        binding.setClickHandler(this);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnProceed:
                confirmingPassword();
                break;
        }
    }

    void confirmingPassword() {
        String password = binding.etPassword.getText().toString();
        if (password.isEmpty()) {
            Toast.makeText(context, "Fields must be filled", Toast.LENGTH_SHORT).show();
        } else {
            if (password.equals(getPassword)) {
                prefUtil.setPassword(getPassword);
                Toast.makeText(context, "Password Reset Successful", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(context, "Password is not matching", Toast.LENGTH_SHORT).show();
            }
        }
    }

}