package com.example.keyboardvalut.activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Matrix;
import android.os.Bundle;
import android.service.media.CameraPrewarmService;
import android.view.Surface;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.Camera;

import androidx.camera.core.ImageCapture;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;

import com.example.keyboardvalut.R;
import com.example.keyboardvalut.adapters.DrawerMenuAdapter;
import com.example.keyboardvalut.databinding.ActivityEnterVaultPasswordBinding;
import com.example.keyboardvalut.interfaces.ClickListener;
import com.example.keyboardvalut.interfaces.DrawerMenuClickListener;
import com.example.keyboardvalut.utils.ScreenUtils;
import com.google.common.util.concurrent.ListenableFuture;
import com.lassi.presentation.cameraview.controls.CameraView;

import java.util.concurrent.ExecutionException;

public class EnterVaultPasswordActivity extends AppCompatActivity implements ClickListener, DrawerMenuClickListener {

    Context context;
    DrawerMenuAdapter adapter;

    ActivityEnterVaultPasswordBinding binding;

    Camera camera;
     Preview preview;
    ImageCapture imageCapture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_enter_vault_password);
        ScreenUtils.hidingStatusBar(this);

        context = this;

        binding.setClickHandler(this);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnDrawer:
                binding.drawerLayout.openDrawer(GravityCompat.START);
                break;

        }
    }


//    private void populatingDrawerMenu() {
//        adapter = new DrawerMenuAdapter(context, new DrawerMenuData().getMenuList());
//        binding.rvDrawerMenu.setLayoutManager(new LinearLayoutManager(context));
//        binding.rvDrawerMenu.setAdapter(adapter);
//    }

    private void closeDrawer() {
        binding.drawerLayout.closeDrawer(GravityCompat.START);
    }

    @Override
    public void onDrawerMenuClickListener(int position) {
        switch (position) {
            case 0:
                Toast.makeText(context, "Home", Toast.LENGTH_SHORT).show();
                closeDrawer();
                break;
            case 1:
                closeDrawer();
                break;
            case 2:
                closeDrawer();
                break;
            case 3:

                closeDrawer();
                break;
        }
    }






}