package com.example.keyboardvalut.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefUtil {
    SharedPreferences prefs;
    SharedPreferences.Editor editor;

    @SuppressLint("CommitPrefEdits")
    public SharedPrefUtil(Context context) {
        if (context != null) {
            prefs = context.getSharedPreferences("LogoCreaterPref", Context.MODE_PRIVATE);
            editor = prefs.edit();
        }
    }

    public void setPassword(String password) {
        editor.putString("password", password);
        editor.commit();
    }

    public String getPassword() {
        return prefs.getString("password", "");
    }


    public void setQuestion(String question) {
        editor.putString("question", question);
        editor.commit();
    }

    public String getQuestion() {
        return prefs.getString("question", "");
    }


    public void setAnswer(String answer) {
        editor.putString("answer", answer);
        editor.commit();
    }

    public String getAnswer() {
        return prefs.getString("answer", "");
    }


    public void setEmail(String email) {
        editor.putString("email", email);
        editor.commit();
    }

    public String getEmail() {
        return prefs.getString("email", "");
    }


    public void setClickedPos(int position) {
        editor.putInt("position", position);
        editor.commit();
    }

    public int getClickedPos() {
        return prefs.getInt("position", -1);
    }


    public void isAppOpenFirstTime(boolean status) {
        editor.putBoolean("status", status);
        editor.commit();
    }

    public boolean getAppOpenFirstTime() {
        return prefs.getBoolean("status", true);
    }

    public void isBreakInAlertEnabled(boolean status) {
        editor.putBoolean("bStatus", status);
        editor.commit();
    }

    public boolean getBreakInAlert() {
        return prefs.getBoolean("bStatus", true);
    }



    public void setIconChangedStatus(boolean status) {
        editor.putBoolean("iconStatus", status);
        editor.commit();
    }

    public boolean getIconChangedStatus() {
        return prefs.getBoolean("iconStatus", false);
    }
    public void clearPreferences() {
        editor.clear();
        editor.commit();
    }

}