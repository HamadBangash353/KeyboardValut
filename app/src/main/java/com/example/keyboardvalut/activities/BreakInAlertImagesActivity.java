package com.example.keyboardvalut.activities;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.lifecycle.ProcessLifecycleOwner;
import androidx.recyclerview.widget.GridLayoutManager;

import com.example.keyboardvalut.R;
import com.example.keyboardvalut.adapters.BreakInAlertImageAdapter;
import com.example.keyboardvalut.adapters.HiddenMediaAdapter;
import com.example.keyboardvalut.databinding.ActivityBreakInAlertImagesBinding;
import com.example.keyboardvalut.interfaces.ClickListener;
import com.example.keyboardvalut.interfaces.DeleteFileCallback;
import com.example.keyboardvalut.interfaces.OnImageClickCallback;
import com.example.keyboardvalut.utils.DialogUtils;
import com.example.keyboardvalut.utils.ScreenUtils;
import com.example.keyboardvalut.utils.SharedPrefUtil;

import java.io.File;
import java.util.Arrays;
import java.util.List;

public class BreakInAlertImagesActivity extends AppCompatActivity implements ClickListener, OnImageClickCallback, DeleteFileCallback, LifecycleObserver {

    Context context;
    Intent intent;
    ActivityBreakInAlertImagesBinding binding;
    BreakInAlertImageAdapter breakInAlertAdapter;
    SharedPrefUtil prefUtil;

    Dialog breakInAlertDialog;
    Dialog deleteImageDialog;

    String selectedPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_break_in_alert_images);
        ScreenUtils.hidingStatusBar(this);

        context = this;

        prefUtil = new SharedPrefUtil(context);
        breakInAlertDialog = new Dialog(context);

        deleteImageDialog = new Dialog(context);
        ProcessLifecycleOwner.get().getLifecycle().addObserver(this);
        if (prefUtil.getBreakInAlert()) {
            binding.ivBreakInAlert.setImageResource(R.drawable.ic_toggle_on);
        } else {
            binding.ivBreakInAlert.setImageResource(R.drawable.ic_toggle_off);
        }

        binding.setClickHandler(this);
    }

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


    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnEnableAlert:
                binding.ivBreakInAlert.setImageResource(R.drawable.ic_toggle_on);
                prefUtil.isBreakInAlertEnabled(true);
                breakInAlertDialog.dismiss();
                break;

            case R.id.ivBreakInAlert:
                settingBreakInAlertToggle();
                break;

            case R.id.btnCancelDeleteDialog:
            case R.id.ivExitDeleteDialog:
                DialogUtils.deleteFileDialog(deleteImageDialog, BreakInAlertImagesActivity.this);
                dismissDialog(deleteImageDialog);
                break;

            case R.id.btnDeleteAll:
                new File(selectedPath).delete();
                dismissDialog(deleteImageDialog);
                refreshAdapter();
                break;
            case R.id.ivExitDialog:
                breakInAlertDialog.dismiss();
                break;

            case R.id.btnBack:
                movingToBackActivity();
                break;

        }
    }


    void refreshAdapter() {

        breakInAlertAdapter.refreshAdapter(gettingHiddenImages());
    }

    private void dismissDialog(Dialog dialog) {
        if (dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        new Handler().postDelayed(() -> {
            if (gettingHiddenImages().size() == 0) {
                binding.emptyLayoutIndicator.setVisibility(View.VISIBLE);
            } else {
                binding.emptyLayoutIndicator.setVisibility(View.GONE);
                binding.bottonSheetLayout.setVisibility(View.GONE);
            }
            binding.bottonSheetLayout.setVisibility(View.GONE);

            settingAdapter();
        }, 30);


    }

    List<File> gettingHiddenImages() {
        File fileDirectory = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/.KeyboardVault/wrongPassImages/");
        File[] dirFiles = fileDirectory.listFiles();
        return Arrays.asList(dirFiles);

    }

    void settingAdapter() {
        breakInAlertAdapter = new BreakInAlertImageAdapter(context, gettingHiddenImages());
        binding.rvPhotos.setLayoutManager(new GridLayoutManager(context, 2));
        binding.rvPhotos.setAdapter(breakInAlertAdapter);

    }

    @Override
    public void onImageClickCallback(String path) {
        intent = new Intent(context, ImageVeiwingActivity.class);
        intent.putExtra("path", path);
        intent.putExtra("activityTag", "breakInAlert");
        startActivity(intent);
    }

    private void movingToBackActivity() {
        intent = new Intent(this, VaultSettingsActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
    }

    private void settingBreakInAlertToggle() {
        if (!prefUtil.getBreakInAlert()) {

            DialogUtils.breakInAlertDialog(breakInAlertDialog, BreakInAlertImagesActivity.this);
            breakInAlertDialog.show();

        } else {
            binding.ivBreakInAlert.setImageResource(R.drawable.ic_toggle_off);
            prefUtil.isBreakInAlertEnabled(false);
        }
    }

    @Override
    public void onFileDeleteCallback(String path) {
        selectedPath = path;
        DialogUtils.deleteFileDialog(deleteImageDialog, this);
        deleteImageDialog.show();
    }
}