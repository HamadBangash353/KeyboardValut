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

import com.example.keyboardvalut.R;
import com.example.keyboardvalut.adapters.DrawerMenuAdapter;
import com.example.keyboardvalut.data.DrawerMenuData;
import com.example.keyboardvalut.databinding.ActivityPlayVideosBinding;
import com.example.keyboardvalut.databinding.ActivityVaultVideosBinding;
import com.example.keyboardvalut.interfaces.ClickListener;
import com.example.keyboardvalut.interfaces.DrawerMenuClickListener;
import com.example.keyboardvalut.utils.ScreenUtils;

public class PlayVideoActivity extends AppCompatActivity implements ClickListener, DrawerMenuClickListener {

    Context context;
    DrawerMenuAdapter adapter;
    Intent intent;
    ActivityPlayVideosBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_play_videos);
        ScreenUtils.hidingStatusBar(this);

        context = this;
       // populatingDrawerMenu();
        binding.setClickHandler(this);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnPhotosVault:
                intent = new Intent(context, VaultSettingsActivity.class);
                startActivity(intent);
                break;

        }
    }


    private void populatingDrawerMenu() {
        adapter = new DrawerMenuAdapter(context, new DrawerMenuData().getMenuList());
//        binding.rvDrawerMenu.setLayoutManager(new LinearLayoutManager(context));
//        binding.rvDrawerMenu.setAdapter(adapter);
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