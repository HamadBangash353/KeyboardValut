package com.example.keyboardvalut.activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.lifecycle.ProcessLifecycleOwner;

import com.example.keyboardvalut.adapters.SpinnerAdapter;
import com.example.keyboardvalut.R;
import com.example.keyboardvalut.data.SpinnerQuestionData;
import com.example.keyboardvalut.databinding.ActivitySecurityQuestionBinding;
import com.example.keyboardvalut.interfaces.ClickListener;
import com.example.keyboardvalut.interfaces.SecurityQuestionCallback;
import com.example.keyboardvalut.utils.ScreenUtils;
import com.example.keyboardvalut.utils.SharedPrefUtil;

public class SecurityQuestionActivity extends AppCompatActivity implements ClickListener, SecurityQuestionCallback, LifecycleObserver {

    Context context;
    Intent intent;

    ActivitySecurityQuestionBinding binding;

    SharedPrefUtil prefUtil;

    String question, answer, email;

    String activityTag;


    int lifeCycleChecker = 0;

    @Override
    protected void onRestart() {
        super.onRestart();
        if (lifeCycleChecker == 1&& !prefUtil.getAnswer().equals("")) {

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


        binding = DataBindingUtil.setContentView(this, R.layout.activity_security_question);
        ScreenUtils.hidingStatusBar(this);
        context = this;

        prefUtil = new SharedPrefUtil(context);

        activityTag = getIntent().getStringExtra("tag");
        ProcessLifecycleOwner.get().getLifecycle().addObserver(this);
        binding.spQuestions.setAdapter(new SpinnerAdapter(context, R.layout.spinner_layout, this, SpinnerQuestionData.questionsData()));

        if (activityTag.equals("passwordActivity")) {
            if (!prefUtil.getAnswer().equals("") && !prefUtil.getEmail().equals("")) {
                passingIntentToPassActivity();
            }
        }

        binding.spQuestions.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                binding.etAnswer.setText("");
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

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
                    restingEmail();
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
        email = binding.etEmail.getText().toString().trim();

        if (answer.isEmpty() || email.isEmpty()) {
            Toast.makeText(context, "Fields Must Be Filled", Toast.LENGTH_SHORT).show();
        } else if (isValidEmail(email)) {
            Toast.makeText(context, "Please enter valid email", Toast.LENGTH_SHORT).show();
        } else {
            prefUtil.setQuestion(question);
            prefUtil.setAnswer(answer);
            prefUtil.setEmail(email);
            passingIntentToPassActivity();
        }
    }


    void restingEmail() {
        answer = binding.etAnswer.getText().toString();
        email = binding.etEmail.getText().toString().trim();

        if (email.isEmpty()) {
            Toast.makeText(context, "Please enter email", Toast.LENGTH_SHORT).show();
        } else if (isValidEmail(email)) {
            Toast.makeText(context, "Please enter valid email", Toast.LENGTH_SHORT).show();
        } else if (!answer.trim().equals("")) {
            prefUtil.setQuestion(question);
            prefUtil.setAnswer(answer);
            prefUtil.setEmail(email);
            Toast.makeText(context, "Email Updated", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            prefUtil.setEmail(email);
            Toast.makeText(context, "Email Updated", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    void passingIntentToPassActivity() {

        intent = new Intent(context, VaultPasswordEnteringActivity.class);

        startActivity(intent);
        finish();
    }

    public static boolean isValidEmail(CharSequence target) {
        return (TextUtils.isEmpty(target) || !Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }

    @Override
    public void onSecurityQuestionCallback(String question) {
        this.question = question;
    }
}