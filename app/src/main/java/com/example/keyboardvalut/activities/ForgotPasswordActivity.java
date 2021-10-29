package com.example.keyboardvalut.activities;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.example.keyboardvalut.R;
import com.example.keyboardvalut.adapters.SpinnerAdapter;
import com.example.keyboardvalut.data.SpinnerQuestionData;
import com.example.keyboardvalut.databinding.ActivityForgotPasswordBinding;
import com.example.keyboardvalut.databinding.ActivitySecurityQuestionBinding;
import com.example.keyboardvalut.interfaces.ClickListener;
import com.example.keyboardvalut.interfaces.SecurityQuestionCallback;
import com.example.keyboardvalut.utils.GmailSender;
import com.example.keyboardvalut.utils.ScreenUtils;
import com.example.keyboardvalut.utils.SharedPrefUtil;

import java.io.File;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class ForgotPasswordActivity extends AppCompatActivity implements ClickListener, SecurityQuestionCallback {

    Context context;
    Intent intent;

    ActivityForgotPasswordBinding binding;

    SharedPrefUtil prefUtil;

    String question;

    String activityTag;
    String user = "vaultkeyboard@gmail.com";
    String password = "Wolfiz123!@#";
    GmailSender sender;
    Message message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_forgot_password);
        ScreenUtils.hidingStatusBar(this);
        context = this;

        prefUtil = new SharedPrefUtil(context);

        activityTag = getIntent().getStringExtra("tag");

        sender = new GmailSender(user, password);

        binding.spQuestions.setAdapter(new SpinnerAdapter(context, R.layout.spinner_layout, this, SpinnerQuestionData.questionsData()));

        binding.spQuestions.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                binding.etAnswer.setText("");
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        settingData();
        settingProperties();

        binding.setClickHandler(this);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.cvSendEmail:
                sendingEmail();
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    void settingData() {
        binding.etAnswer.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (s.toString().length() == prefUtil.getAnswer().length()) {
                    if (question.equals(prefUtil.getQuestion()) && s.toString().equals(prefUtil.getAnswer())) {
                        intent = new Intent(context, VaultMainActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(context, "Answer is incorrect", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    void settingProperties() {
        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");

        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(user, password);
            }
        });


        try {
            message = new MimeMessage(session);
            message.setFrom(new InternetAddress(user));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(prefUtil.getEmail()));
            message.setSubject("Password Recovered");
            message.setText("Your Recovered Password For Vault keyboard is " + prefUtil.getPassword());
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onSecurityQuestionCallback(String question) {
        this.question = question;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void sendingEmail() {
        ProgressDialog dialog;
        dialog = new ProgressDialog(context);
        dialog.setMessage("Please wait...");
        dialog.show();

        new Thread(() -> {
            try {

                Transport.send(message);

            } catch (Exception e) {
                e.printStackTrace();
                Log.d("MyError", e.getMessage());
            }
            runOnUiThread(() -> {

                Toast.makeText(context, "Check your email for password", Toast.LENGTH_SHORT).show();
                showingEmailAlert();
                dialog.dismiss();
            });
        }).start();
    }

    private void showingEmailAlert() {
        AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setTitle("Alert");
        alertDialog.setMessage("Password has been sent to your Mail box. If you are unable to recover from inbox , Please check Spam Folder!");
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                (dialog, which) -> dialog.dismiss());


        alertDialog.show();
    }

}