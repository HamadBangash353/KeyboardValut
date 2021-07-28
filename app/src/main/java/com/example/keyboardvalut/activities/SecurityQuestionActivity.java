package com.example.keyboardvalut.activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.example.keyboardvalut.adapters.SpinnerAdapter;
import com.example.keyboardvalut.R;
import com.example.keyboardvalut.data.SpinnerQuestionData;
import com.example.keyboardvalut.databinding.ActivitySecurityQuestionBinding;
import com.example.keyboardvalut.interfaces.ClickListener;
import com.example.keyboardvalut.interfaces.SecurityQuestionCallback;
import com.example.keyboardvalut.utils.ScreenUtils;
import com.example.keyboardvalut.utils.SharedPrefUtil;

public class SecurityQuestionActivity extends AppCompatActivity implements ClickListener, SecurityQuestionCallback {

    Context context;
    Intent intent;

    ActivitySecurityQuestionBinding binding;

    SharedPrefUtil prefUtil;

    String question, answer, email;

    String activityTag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_security_question);
        ScreenUtils.hidingStatusBar(this);
        context = this;

        prefUtil = new SharedPrefUtil(context);

        activityTag = getIntent().getStringExtra("tag");

        binding.spQuestions.setAdapter(new SpinnerAdapter(context, R.layout.spinner_layout, this, SpinnerQuestionData.questionsData()));

        if (activityTag.equals("passwordActivity")) {
            if (!prefUtil.getAnswer().equals("") && !prefUtil.getEmail().equals("")) {
                passingIntent();
            }
        }

        binding.setClickHandler(this);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnProceed:
                if (activityTag.equals("passwordActivity")) {
                    settingData();
                } else {

                }
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    void settingData() {
        answer = binding.etAnswer.getText().toString();
        email = binding.etEmail.getText().toString();

        if (answer.isEmpty() || email.isEmpty()) {
            Toast.makeText(context, "Fields Must Be Filled", Toast.LENGTH_SHORT).show();
        } else if (!isValidEmail(email)) {
            Toast.makeText(context, "Please enter valid email", Toast.LENGTH_SHORT).show();
        } else {
            prefUtil.setQuestion(question);
            prefUtil.setAnswer(answer);
            prefUtil.setEmail(email);
            passingIntent();
        }
    }

    void passingIntent() {

        intent = new Intent(context, VaultPasswordEnteringActivity.class);

        startActivity(intent);
        prefUtil.isAppOpenFirstTime(false);
        finish();
    }

    public static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }

    @Override
    public void onSecurityQuestionCallback(String question) {
        this.question = question;
    }
}