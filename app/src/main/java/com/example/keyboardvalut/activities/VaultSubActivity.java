package com.example.keyboardvalut.activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.keyboardvalut.R;
import com.example.keyboardvalut.adapters.DrawerMenuAdapter;
import com.example.keyboardvalut.data.DrawerMenuData;
import com.example.keyboardvalut.databinding.ActivityVaultSubBinding;
import com.example.keyboardvalut.interfaces.ClickListener;
import com.example.keyboardvalut.interfaces.DrawerMenuClickListener;
import com.example.keyboardvalut.utils.ScreenUtils;

public class VaultSubActivity extends AppCompatActivity implements ClickListener, DrawerMenuClickListener {

    Context context;
    DrawerMenuAdapter adapter;
    Intent intent;
    ActivityVaultSubBinding binding;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_vault_sub);
        ScreenUtils.hidingStatusBar(this);



        context = this;
        populatingDrawerMenu();
        binding.setClickHandler(this);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnPhotosVault:
                passingIntentPhotoVaultActivity();
                break;
            case R.id.btnVideosVault:
                passingIntentVideoVaultActivity();
                break;
            case R.id.btnDocsVault:
                passingIntentDocsVaultActivity();
                break;
            case R.id.btnMediaVault:
               passingIntentMediaVaultActivity();
                break;
            case R.id.btnContactsVault:
                passingIntentContactsVaultActivity();
                break;

        }
    }


    void passingIntentPhotoVaultActivity() {
        intent = new Intent(context, VaultPhotosActivity.class);
        startActivity(intent);
    }

    void passingIntentVideoVaultActivity() {
        intent = new Intent(context, VaultVideosActivity.class);
        startActivity(intent);
    }

    void passingIntentDocsVaultActivity() {
        intent = new Intent(context, VaultDocsActivity.class);
        startActivity(intent);
    }

    void passingIntentMediaVaultActivity() {
        intent = new Intent(context, VaultMediaActivity.class);
        startActivity(intent);
    }

    void passingIntentContactsVaultActivity() {
        intent = new Intent(context, VaultContactsActivity.class);
        startActivity(intent);
    }

    private void populatingDrawerMenu() {
        adapter = new DrawerMenuAdapter(context, new DrawerMenuData().getMenuList());
        binding.rvDrawerMenu.setLayoutManager(new LinearLayoutManager(context));
        binding.rvDrawerMenu.setAdapter(adapter);
    }

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