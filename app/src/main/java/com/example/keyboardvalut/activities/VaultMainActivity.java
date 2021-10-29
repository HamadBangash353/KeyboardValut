package com.example.keyboardvalut.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.lifecycle.ProcessLifecycleOwner;
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

import static android.os.Build.VERSION.SDK_INT;

public class VaultMainActivity extends AppCompatActivity implements ClickListener, DrawerMenuClickListener, LifecycleObserver {

    Context context;
    DrawerMenuAdapter adapter;
    ActivityVaultMainBinding binding;
    Intent intent;
    Uri imageUri;
    static int CAMERA_REQUEST_CODE = 5;

    int lifeCycleChecker=0;
    int cameraIntentChecker=0;

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
        requestingAndroid11Permission();
        ProcessLifecycleOwner.get().getLifecycle().addObserver(this);


        binding.setClickHandler(this);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.btnBack:
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

//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        lifeCycleChecker=1;
//        cameraIntentChecker=0;
//        Toast.makeText(context, "dest", Toast.LENGTH_SHORT).show();
//    }

    void passingIntentToSubActivity() {
        cameraIntentChecker=1;
        intent = new Intent(context, VaultSubActivity.class);
        startActivity(intent);
    }

    void passingIntentToSettingActivity() {
        cameraIntentChecker=1;
        intent = new Intent(context, VaultSettingsActivity.class);
        startActivity(intent);
    }

    void passingIntentToHowToUseActivity() {
        cameraIntentChecker=1;
        intent = new Intent(context, HowToUseActivity.class);
        startActivity(intent);
    }

//    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
//    void appCreated() {
//        cameraIntentChecker=0;
//    }


    @Override
    protected void onRestart() {
        super.onRestart();
        if (lifeCycleChecker==1&cameraIntentChecker==0)
        {
            startActivity(new Intent(VaultMainActivity.this,VaultPasswordEnteringActivity.class));
            finish();

        }
        cameraIntentChecker=0;
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    void onMoveToBackground() {
        lifeCycleChecker = 1;
    }



    void cameraIntent() {

        cameraIntentChecker = 1;
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
        cameraIntentChecker=1;
        intent = new Intent(context, VaultNotesActivity.class);
        startActivity(intent);
    }


    private void requestingAndroid11Permission() {

        if (SDK_INT >= Build.VERSION_CODES.R && !Environment.isExternalStorageManager()) {
            Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
            intent.addCategory("android.intent.category.DEFAULT");
            intent.setData(Uri.parse(String.format("package:%s", getApplicationContext().getPackageName())));
            startActivityForResult(intent, 7);
        }
    }


    void requestingPermission() {

        PermissionX.init(VaultMainActivity.this)
                .permissions(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA).onExplainRequestReason((scope, deniedList) -> scope.showRequestReasonDialog(deniedList, "Core fundamental are based on this permission", "OK", "Cancel")).onForwardToSettings((scope, deniedList) -> scope.showForwardToSettingsDialog(deniedList, "You need to allow necessary permissions in Settings manually", "OK", "Cancel")).request((allGranted, grantedList, deniedList) -> {
            if (!allGranted) {
                requestingPermission();
            } else {
                creatingDirectories();
            }
        });
    }

//    @Override
//    protected void onResume() {
//        super.onResume();
//        if (lifeCycleChecker == 1 && cameraIntentChecker == 0) {
//            startActivity(new Intent(VaultMainActivity.this, VaultPasswordEnteringActivity.class));
//            finish();
//        }
//    }

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
            case 6:
                passingIntentToHowToUseActivity();
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

        if (requestCode == 7) {
            if (SDK_INT >= Build.VERSION_CODES.R) {
                if (Environment.isExternalStorageManager()) {
                    creatingDirectories();
                } else {
                    Toast.makeText(this, "Allow permission for storage access!", Toast.LENGTH_SHORT).show();
                }
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
//                cameraIntent();
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

    @Override
    public void onBackPressed() {
        showingExitDialog();
    }

    private void showingExitDialog() {
        new AlertDialog.Builder(this)
                .setMessage("Do you really want to exit?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
//                        Intent homeIntent = new Intent(Intent.ACTION_MAIN);
//                        homeIntent.addCategory(Intent.CATEGORY_HOME);
//                        homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                        startActivity(homeIntent);
//                        System.exit(1);
                        Intent intent = new Intent(Intent.ACTION_MAIN);
                        intent.addCategory(Intent.CATEGORY_HOME);
                        startActivity(intent);
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }

}