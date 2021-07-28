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

public class VaultSettingsActivity extends AppCompatActivity implements ClickListener {

    Context context;

    ActivityVaultSettingsBinding binding;
    SharedPrefUtil prefUtil;

    Dialog replaceIconDialog;
    Dialog breakInAlertDialog;

    Intent intent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_vault_settings);
        ScreenUtils.hidingStatusBar(this);

        context = this;
        prefUtil = new SharedPrefUtil(context);

        replaceIconDialog = new Dialog(context);
        breakInAlertDialog = new Dialog(context);


        if (prefUtil.getBreakInAlert()) {
            binding.ivBreakInAlert.setImageResource(R.drawable.ic_toggle_on);
        } else {
            binding.ivBreakInAlert.setImageResource(R.drawable.ic_toggle_off);
        }

        binding.setClickHandler(this);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ivBreakInAlert:
                settingBreakInAlertToggle();
                break;

            case R.id.firstRow:
                DialogUtils.replaceIconDialog(replaceIconDialog, VaultSettingsActivity.this);
                replaceIconDialog.show();
                break;

            case R.id.fourthRow:
                passingIntentToResetPassActivity();
                break;
            case R.id.fifthRow:
                passingIntentToResetPassActivity();
                break;

            case R.id.btnReplaceIcon:
                changeIcon();
                replaceIconDialog.dismiss();
                break;
            case R.id.ivExitDialog:
                breakInAlertDialog.dismiss();
                break;

            case R.id.btnEnableAlert:
                binding.ivBreakInAlert.setImageResource(R.drawable.ic_toggle_on);
                prefUtil.isBreakInAlertEnabled(true);
                breakInAlertDialog.dismiss();
                break;

        }

    }

    private void settingBreakInAlertToggle() {
        if (!prefUtil.getBreakInAlert()) {

            DialogUtils.breakInAlertDialog(breakInAlertDialog, VaultSettingsActivity.this);
            breakInAlertDialog.show();

        } else {
            binding.ivBreakInAlert.setImageResource(R.drawable.ic_toggle_off);
            prefUtil.isBreakInAlertEnabled(false);
        }
    }


    void passingIntentToResetPassActivity() {
        intent = new Intent(context, ResetPasswordActivity.class);
        startActivity(intent);
    }


    void passingIntentToSecurityQuestionActivity() {
        intent = new Intent(context, SecurityQuestionActivity.class);
        startActivity(intent);
    }

    void changeIcon() {

        if (!prefUtil.getIconChangedStatus()) {
            getPackageManager().setComponentEnabledSetting(
                    new ComponentName(BuildConfig.APPLICATION_ID, "com.example.keyboardvalut.PasswordSignUpActivity"),
                    PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);

            getPackageManager().setComponentEnabledSetting(
                    new ComponentName(BuildConfig.APPLICATION_ID, "com.example.keyboardvalut.PasswordActivityAlias2"),
                    PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);

            prefUtil.setIconChangedStatus(true);
        } else {

            getPackageManager().setComponentEnabledSetting(
                    new ComponentName(BuildConfig.APPLICATION_ID, "com.example.keyboardvalut.PasswordActivityAlias2"),
                    PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);

            getPackageManager().setComponentEnabledSetting(
                    new ComponentName(BuildConfig.APPLICATION_ID, "com.example.keyboardvalut.PasswordSignUpActivity"),
                    PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);

            prefUtil.setIconChangedStatus(false);
        }
    }
}
