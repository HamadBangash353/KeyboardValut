package com.example.keyboardvalut.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.keyboardvalut.R;
import com.example.keyboardvalut.adapters.DrawerMenuAdapter;
import com.example.keyboardvalut.data.DrawerMenuData;
import com.example.keyboardvalut.databinding.ActivityVaultMainBinding;
import com.example.keyboardvalut.interfaces.ClickListener;
import com.example.keyboardvalut.interfaces.DrawerMenuClickListener;
import com.example.keyboardvalut.utils.DirectoriesUtils;
import com.example.keyboardvalut.utils.GeneralUtils;
import com.example.keyboardvalut.utils.MediaScannerUtils;
import com.example.keyboardvalut.utils.ScreenUtils;
import com.lassi.data.media.MiMedia;
import com.permissionx.guolindev.PermissionX;
import com.permissionx.guolindev.callback.ExplainReasonCallback;
import com.permissionx.guolindev.callback.ForwardToSettingsCallback;
import com.permissionx.guolindev.callback.RequestCallback;
import com.permissionx.guolindev.request.ExplainScope;
import com.permissionx.guolindev.request.ForwardScope;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.List;

import static com.example.keyboardvalut.utils.DirectoriesUtils.restoringHiddenDocumentsDirectory;
import static com.example.keyboardvalut.utils.DirectoriesUtils.settingHiddenDocumentsDirectory;
import static com.example.keyboardvalut.utils.DirectoriesUtils.settingHidingVideoDirectory;
import static com.example.keyboardvalut.utils.DirectoriesUtils.settingRestoreDirectory;

public class VaultMainActivity extends AppCompatActivity implements ClickListener, DrawerMenuClickListener {

    Context context;
    DrawerMenuAdapter adapter;
    ActivityVaultMainBinding binding;
    Intent intent;
    Uri imageUri;
    static int CAMERA_REQUEST_CODE = 5;

    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_vault_main);
        ScreenUtils.hidingStatusBar(this);

        context = this;
        populatingDrawerMenu();
        dialog = new ProgressDialog(context);
        dialog.setMessage("Loading...");
        requestingPermission();
        binding.setClickHandler(this);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnDrawer:
                binding.drawerLayout.openDrawer(GravityCompat.START);
                break;
            case R.id.btnMyVault:
                passingIntentToSubActivity();
                break;
            case R.id.btnNotesValut:
                passingIntentToNotesActivity();
                break;
            case R.id.btnCameraVault:
                cameraIntent();
                break;
            case R.id.btnVaultSettings:
                passingIntentToSettingActivity();
                break;

        }
    }

    void passingIntentToSubActivity() {
        intent = new Intent(context, VaultSubActivity.class);
        startActivity(intent);
    }

    void passingIntentToSettingActivity() {
        intent = new Intent(context, VaultSettingsActivity.class);
        startActivity(intent);
    }

    void cameraIntent() {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "MyPicture");
        values.put(MediaStore.Images.Media.DESCRIPTION, "Photo taken on " + System.currentTimeMillis());
        imageUri = getContentResolver().insert(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(intent, CAMERA_REQUEST_CODE);
    }

    void passingIntentToNotesActivity() {
        intent = new Intent(context, VaultNotesActivity.class);
        startActivity(intent);
    }


    void requestingPermission() {

        PermissionX.init(VaultMainActivity.this)
                .permissions(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA).onExplainRequestReason(new ExplainReasonCallback() {
            @Override
            public void onExplainReason(ExplainScope scope, List<String> deniedList) {
                scope.showRequestReasonDialog(deniedList, "Core fundamental are based on this permission", "OK", "Cancel");
            }
        }).onForwardToSettings(new ForwardToSettingsCallback() {
            @Override
            public void onForwardToSettings(ForwardScope scope, List<String> deniedList) {
                scope.showForwardToSettingsDialog(deniedList, "You need to allow necessary permissions in Settings manually", "OK", "Cancel");
            }
        }).request(new RequestCallback() {
            @Override
            public void onResult(boolean allGranted, List<String> grantedList, List<String> deniedList) {
                if (!allGranted) {
                    requestingPermission();
                } else {
                    creatingDirectories();
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    private void creatingDirectories() {
        DirectoriesUtils.settingMainDirectory();
        DirectoriesUtils.settingHidingImagesDirectory();
        DirectoriesUtils.settingRestoreDirectory();
        DirectoriesUtils.settingHidingVideoDirectory();
        DirectoriesUtils.settingHiddenDocumentsDirectory();
        DirectoriesUtils.restoringHiddenDocumentsDirectory();
        DirectoriesUtils.settingCameraImagesDirectory();
        DirectoriesUtils.settingWrongPassImagesDirectory();
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
                closeDrawer();
                break;
            case 1:
                closeDrawer();
                GeneralUtils.rateUs(context);
                break;
            case 2:
                passingIntentToSubActivity();
                closeDrawer();
                break;
            case 3:
                passingIntentToNotesActivity();
                closeDrawer();
                break;
            case 4:
                cameraIntent();
                closeDrawer();
                break;
            case 5:
                passingIntentToSettingActivity();
                closeDrawer();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == CAMERA_REQUEST_CODE)
            if (resultCode == Activity.RESULT_OK) {
                try {

                    String imagePath = getRealPathFromURI(imageUri);
                    loadingImagesInThread(imagePath);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
    }

    public String getRealPathFromURI(Uri contentUri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = managedQuery(contentUri, proj, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }


    void loadingImagesInThread(String imagePath) {
        dialog.show();

        new Thread(() -> {
            File sdCard = Environment.getExternalStorageDirectory();
            File dir = new File(sdCard.getAbsolutePath() + "/.KeyboardVault/VaultMediaImages");
            try {


                File from = new File(imagePath);
                long currentTime = System.currentTimeMillis();
                File to = new File(dir + "/" + currentTime + "#jpg");
                copyFile(from, to);


            } catch (Exception e) {
                e.printStackTrace();
            }
            runOnUiThread(() -> {

                dialog.dismiss();
                cameraIntent();
            });
        }).start();
    }


    private void copyFile(File sourceFile, File destFile) throws IOException {
        if (!sourceFile.exists()) {
            return;
        }


        FileChannel source;
        FileChannel destination;
        source = new FileInputStream(sourceFile).getChannel();
        destination = new FileOutputStream(destFile).getChannel();
        if (destination != null && source != null) {
            destination.transferFrom(source, 0, source.size());
            sourceFile.delete();
            new MediaScannerUtils(context, destFile);
            new MediaScannerUtils(context, sourceFile);
        }
        if (source != null) {
            source.close();
        }
        if (destination != null) {
            destination.close();
        }
    }

}