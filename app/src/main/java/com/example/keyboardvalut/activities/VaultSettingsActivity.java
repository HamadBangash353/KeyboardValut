package com.example.keyboardvalut.activities;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
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

import com.example.keyboardvalut.BuildConfig;
import com.example.keyboardvalut.R;
import com.example.keyboardvalut.adapters.DrawerMenuAdapter;
import com.example.keyboardvalut.data.DrawerMenuData;
import com.example.keyboardvalut.databinding.ActivityVaultSettingsBinding;
import com.example.keyboardvalut.databinding.ActivityVaultSubBinding;
import com.example.keyboardvalut.interfaces.ClickListener;
import com.example.keyboardvalut.interfaces.DrawerMenuClickListener;
import com.example.keyboardvalut.utils.DialogUtils;
import com.example.keyboardvalut.utils.ScreenUtils;
import com.example.keyboardvalut.utils.SharedPrefUtil;

import java.security.Security;

public class VaultSettingsActivity extends AppCompatActivity implements ClickListener, LifecycleObserver {

    Context context;

    ActivityVaultSettingsBinding binding;
    SharedPrefUtil prefUtil;

    Dialog replaceIconDialog;
    Dialog breakInAlertDialog;

    Intent intent;

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_vault_settings);
        ScreenUtils.hidingStatusBar(this);
        ProcessLifecycleOwner.get().getLifecycle().addObserver(this);
        context = this;
        prefUtil = new SharedPrefUtil(context);

        replaceIconDialog = new Dialog(context);
        breakInAlertDialog = new Dialog(context);


        binding.setClickHandler(this);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.firstRow:
                DialogUtils.replaceIconDialog(replaceIconDialog, VaultSettingsActivity.this);
                replaceIconDialog.show();
                break;
            case R.id.fourthRow:
                passingIntentToResetPassActivity();
                break;
            case R.id.fifthRow:
                passingIntentToSecurityQuestionActivity();
                break;
            case R.id.thirdRow:
                passingIntentToBreakInAlertActivity();
                break;
            case R.id.btnReplaceIcon:
                changeIcon();
                replaceIconDialog.dismiss();
                break;
            case R.id.btnBack:
                movingToBackActivity();
                break;
            case R.id.ivExitDialog:
                replaceIconDialog.dismiss();
                break;
        }

    }


    void passingIntentToResetPassActivity() {
        intent = new Intent(context, ResetPasswordActivity.class);
        startActivity(intent);
    }


    void passingIntentToSecurityQuestionActivity() {
        intent = new Intent(context, SecurityQuestionActivity.class);
        intent.putExtra("tag", "settingActivity");
        startActivity(intent);
    }

    void passingIntentToBreakInAlertActivity() {
        intent = new Intent(context, BreakInAlertImagesActivity.class);
        startActivity(intent);
    }

    void changeIcon() {

        if (!prefUtil.getIconChangedStatus()) {
            getPackageManager().setComponentEnabledSetting(
                    new ComponentName(BuildConfig.APPLICATION_ID, "com.example.keyboardvalut.SplashScreen"),
                    PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);

            getPackageManager().setComponentEnabledSetting(
                    new ComponentName(BuildConfig.APPLICATION_ID, "com.example.keyboardvalut.SplashScreenAlias2"),
                    PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);

            prefUtil.setIconChangedStatus(true);
        } else {

            getPackageManager().setComponentEnabledSetting(
                    new ComponentName(BuildConfig.APPLICATION_ID, "com.example.keyboardvalut.SplashScreenAlias2"),
                    PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);

            getPackageManager().setComponentEnabledSetting(
                    new ComponentName(BuildConfig.APPLICATION_ID, "com.example.keyboardvalut.SplashScreen"),
                    PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);

            prefUtil.setIconChangedStatus(false);
        }
    }

    private void movingToBackActivity() {
        intent = new Intent(this, VaultMainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
    }
}
