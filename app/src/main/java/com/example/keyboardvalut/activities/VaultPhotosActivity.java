package com.example.keyboardvalut.activities;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;

import com.esafirm.imagepicker.features.ImagePicker;
import com.esafirm.imagepicker.model.Image;
import com.example.keyboardvalut.R;
import com.example.keyboardvalut.adapters.DrawerMenuAdapter;
import com.example.keyboardvalut.adapters.HiddenImagesAdapter;
import com.example.keyboardvalut.data.DrawerMenuData;
import com.example.keyboardvalut.databinding.ActivityVaultPhotosBinding;
import com.example.keyboardvalut.interfaces.ClickListener;
import com.example.keyboardvalut.interfaces.DeleteFileCallback;
import com.example.keyboardvalut.interfaces.DrawerMenuClickListener;
import com.example.keyboardvalut.interfaces.OnImageClickCallback;
import com.example.keyboardvalut.interfaces.SelectedPathListCallback;
import com.example.keyboardvalut.interfaces.OnFileRestoreCallback;
import com.example.keyboardvalut.utils.DialogUtils;
import com.example.keyboardvalut.utils.MediaScannerUtils;
import com.example.keyboardvalut.utils.ScreenUtils;
import com.example.keyboardvalut.utils.SharedPrefUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class VaultPhotosActivity extends AppCompatActivity implements ClickListener, DrawerMenuClickListener, OnFileRestoreCallback, SelectedPathListCallback, DeleteFileCallback, OnImageClickCallback {

    Context context;
    DrawerMenuAdapter adapter;
    Intent intent;
    boolean isViewSelected = false;
    ActivityVaultPhotosBinding binding;
    List<String> photoPaths;
    ProgressDialog dialog;

    HiddenImagesAdapter hiddenImagesAdapter;

    List<String> selectedPathsList;

    String selectedPath;

    Dialog restoreSingleImageDialog;
    Dialog deleteImageDialog;
    Dialog restoreAllImagesDialog;
    Dialog deleteAllImagesDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_vault_photos);
        ScreenUtils.hidingStatusBar(this);

        context = this;

        photoPaths = new ArrayList<>();
        selectedPathsList = new ArrayList<>();

        restoreSingleImageDialog = new Dialog(context);
        deleteImageDialog = new Dialog(context);
        restoreAllImagesDialog = new Dialog(context);
        deleteAllImagesDialog = new Dialog(context);

        dialog = new ProgressDialog(context);
        dialog.setMessage("Loading...");

        settingSharedPrefs();
        binding.setClickHandler(this);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tvAddPhotos:
                openingGallery();
                break;
            case R.id.btnRestoreAllImages:
                DialogUtils.restoreAllFileDialog(restoreAllImagesDialog, this);
                restoreAllImagesDialog.show();
                break;
            case R.id.btnDoneAddContacts:
                restoringFiles(selectedPath);
                dismissDialog(restoreSingleImageDialog);
                refreshAdapter();
                break;
            case R.id.btnCancelResotoreDilog:
            case R.id.ivExitContactDialog:
                DialogUtils.restoreDialog(restoreSingleImageDialog, VaultPhotosActivity.this);
                dismissDialog(restoreSingleImageDialog);
                break;
            case R.id.btnCancelDeleteDialog:
            case R.id.ivExitDeleteDialog:
                DialogUtils.deleteFileDialog(deleteImageDialog, VaultPhotosActivity.this);
                dismissDialog(deleteImageDialog);
                break;
            case R.id.btnCancelDelteAllImageDialog:
            case R.id.ivExitDeleteAllImagesDialog:
                DialogUtils.deleteAllFileDialog(deleteAllImagesDialog, VaultPhotosActivity.this);
                dismissDialog(deleteAllImagesDialog);
                break;
            case R.id.btnDeleteAll:
                new File(selectedPath).delete();
                dismissDialog(deleteImageDialog);
                refreshAdapter();
                break;
            case R.id.btnRestoreAll:
                restoreAllImages(selectedPathsList);
                dismissDialog(restoreAllImagesDialog);
                reloadingAdapter();
                break;

            case R.id.btnDeleteAllImage:
                DialogUtils.deleteAllFileDialog(deleteAllImagesDialog, this);
                deleteAllImagesDialog.show();
                break;

            case R.id.btnDeleteAllDialog:
                deletingAllFile(selectedPathsList);
                dismissDialog(deleteAllImagesDialog);
                break;

        }
    }

    private void dismissDialog(Dialog dialog) {
        if (dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    void refreshAdapter() {

        hiddenImagesAdapter.refreshAdapter(gettingHiddenImages());
    }


    void reloadingAdapter() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                settingAdapter();
                selectedPathsList.clear();
            }
        }, 700);

    }

    @Override
    protected void onResume() {
        super.onResume();

        new Handler().postDelayed(() -> {
            if (gettingHiddenImages().size() == 0) {
                binding.emptyLayoutIndicator.setVisibility(View.VISIBLE);
            } else {
                binding.emptyLayoutIndicator.setVisibility(View.GONE);
                selectedPathsList.clear();
                binding.bottonSheetLayout.setVisibility(View.GONE);
                binding.btnAddPhotos.setVisibility(View.VISIBLE);
            }
            binding.bottonSheetLayout.setVisibility(View.GONE);

            settingAdapter();
        }, 500);


    }

    void openingGallery() {
        ImagePicker.create(this)
                .multi()
                .limit(15)
                .showCamera(false)
                .theme(R.style.PickerTheme)
                .toolbarImageTitle("Tap to select image")
                .start();
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

    private void deletingAllFile(List<String> selectedPathsList) {
        dialog.show();

        new Thread(() -> {
            try {
                int i = 0;
                while (i < selectedPathsList.size()) {
                    new File(selectedPathsList.get(i)).delete();
                    i++;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            runOnUiThread(() -> {
                reloadingAdapter();
                dialog.dismiss();
            });
        }).start();
    }


    private void settingSharedPrefs() {
        new SharedPrefUtil(context).setClickedPos(-1);
    }


    void loadingImagesInThread(List<Image> images) {
        dialog.show();

        new Thread(() -> {
            File sdCard = Environment.getExternalStorageDirectory();
            File dir = new File(sdCard.getAbsolutePath() + "/.KeyboardVault/MyVaultAppImages");
            try {
                int i = 0;
                while (i < images.size()) {

                    File from = new File(images.get(i).getPath());
                    long currentTime = System.currentTimeMillis();
                    File to = new File(dir + "/" + currentTime + "#jpg");
                    copyFile(from, to);
                    i++;
                }


            } catch (Exception e) {
                e.printStackTrace();
            }
            runOnUiThread(() -> {

                dialog.dismiss();
            });
        }).start();
    }


    void restoreAllImages(List<String> imagesPaths) {
        dialog.show();
        Toast.makeText(context, imagesPaths.size() + "", Toast.LENGTH_SHORT).show();
        new Thread(() -> {

            try {
                int i = 0;
                while (i < imagesPaths.size()) {
                    restoringFiles(imagesPaths.get(i));

                    i++;
                }


            } catch (Exception e) {
                e.printStackTrace();
            }
            runOnUiThread(() -> {

                dialog.dismiss();
            });
        }).start();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (ImagePicker.shouldHandle(requestCode, resultCode, data)) {

            List<Image> images = ImagePicker.getImages(data);

            loadingImagesInThread(images);


        }
        super.onActivityResult(requestCode, resultCode, data);
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


    List<File> gettingHiddenImages() {
        File fileDirectory = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/.KeyboardVault/MyVaultAppImages/");
        File[] dirFiles = fileDirectory.listFiles();
        return Arrays.asList(dirFiles);


    }

    void settingAdapter() {
        hiddenImagesAdapter = new HiddenImagesAdapter(context, gettingHiddenImages());
        binding.rvPhotos.setLayoutManager(new GridLayoutManager(context, 2));
        binding.rvPhotos.setAdapter(hiddenImagesAdapter);

    }

    @Override
    public void onFileRestoreCallback(String path) {
        selectedPath = path;
        DialogUtils.restoreDialog(restoreSingleImageDialog, this);
        restoreSingleImageDialog.show();

    }

    void restoringFiles(String path) {

        File sdCard = Environment.getExternalStorageDirectory();
        File dir = new File(sdCard.getAbsolutePath() + "/.KeyboardVault/RestoredData");
        File from = new File(path);
        long currentTime = System.currentTimeMillis();
        File to = new File(dir + "/" + currentTime + ".jpg");
        try {
            copyFile(from, to);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        if (isViewSelected) {
            settingAdapter();
            binding.bottonSheetLayout.setVisibility(View.GONE);
            binding.btnAddPhotos.setVisibility(View.VISIBLE);
            isViewSelected = false;
        } else {
            passingIntentToBackActivity();
        }
    }


    void passingIntentToBackActivity() {
        intent = new Intent(this, VaultSubActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
    }

    @Override
    public void onPathSelected(List<String> selectedPathsList, int visibilityCounter) {


        if (visibilityCounter > 0) {
            isViewSelected = true;
            binding.bottonSheetLayout.setVisibility(View.VISIBLE);
            binding.btnAddPhotos.setVisibility(View.GONE);
            this.selectedPathsList = selectedPathsList;
        } else {
            isViewSelected = false;
            binding.btnAddPhotos.setVisibility(View.VISIBLE);
            binding.bottonSheetLayout.setVisibility(View.GONE);
        }
    }

    @Override
    public void onFileDeleteCallback(String path) {
        selectedPath = path;
        DialogUtils.deleteFileDialog(deleteImageDialog, this);
        deleteImageDialog.show();
    }

    @Override
    public void onImageClickCallback(String path) {
        intent =new Intent(context,ImageVeiwingActivity.class);
        intent.putExtra("path",path);
        intent.putExtra("activityTag","images");
        startActivity(intent);
    }
}