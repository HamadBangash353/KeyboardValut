package com.example.keyboardvalut.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.keyboardvalut.R;
import com.example.keyboardvalut.adapters.DrawerMenuAdapter;
import com.example.keyboardvalut.adapters.HiddenDocumentsAdapter;
import com.example.keyboardvalut.data.DrawerMenuData;
import com.example.keyboardvalut.databinding.ActivityVaultDocsBinding;
import com.example.keyboardvalut.interfaces.ClickListener;
import com.example.keyboardvalut.interfaces.DocumentClickCallBack;
import com.example.keyboardvalut.interfaces.DrawerMenuClickListener;
import com.example.keyboardvalut.interfaces.SelectedPathListCallback;
import com.example.keyboardvalut.utils.DialogUtils;
import com.example.keyboardvalut.utils.MediaScannerUtils;
import com.example.keyboardvalut.utils.ScreenUtils;
import com.example.keyboardvalut.utils.SharedPrefUtil;
import com.lassi.data.media.MiMedia;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import droidninja.filepicker.FilePickerBuilder;
import droidninja.filepicker.FilePickerConst;
import droidninja.filepicker.utils.ContentUriUtils;


public class VaultDocsActivity extends AppCompatActivity implements ClickListener, DrawerMenuClickListener, SelectedPathListCallback, DocumentClickCallBack {

    Context context;
    DrawerMenuAdapter adapter;
    Intent intent;
    boolean isViewSelected = false;
    ActivityVaultDocsBinding binding;
    List<String> photoPaths;
    ProgressDialog dialog;

    HiddenDocumentsAdapter hiddenDocumentsAdapter;

    List<String> selectedPathsList;

    String selectedPath;


    Dialog restoreAllImagesDialog;
    Dialog deleteAllImagesDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_vault_docs);
        ScreenUtils.hidingStatusBar(this);

        context = this;

        photoPaths = new ArrayList<>();
        selectedPathsList = new ArrayList<>();

        restoreAllImagesDialog = new Dialog(context);
        deleteAllImagesDialog = new Dialog(context);

        dialog = new ProgressDialog(context);
        dialog.setMessage("Loading...");

        searchingDocuments();

        settingSharedPrefs();
        binding.setClickHandler(this);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnAddPhotos:
                openingFilePicker();
                break;
            case R.id.btnRestoreAllImages:
                DialogUtils.restoreAllFileDialog(restoreAllImagesDialog, this);
                restoreAllImagesDialog.show();
                break;
            case R.id.btnCancelDelteAllImageDialog:
            case R.id.ivExitDeleteAllImagesDialog:
                DialogUtils.deleteAllFileDialog(deleteAllImagesDialog, VaultDocsActivity.this);
                dismissDialog(deleteAllImagesDialog);
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
            if (gettingHiddenDocuments().size() == 0) {
                binding.emptyLayoutIndicator.setVisibility(View.VISIBLE);
            } else {
                binding.emptyLayoutIndicator.setVisibility(View.GONE);
                selectedPathsList.clear();
                binding.bottonSheetLayout.setVisibility(View.GONE);
                binding.btnAddPhotos.setVisibility(View.VISIBLE);
            }
            binding.bottonSheetLayout.setVisibility(View.GONE);

            settingAdapter();
        }, 200);


    }

    void openingFilePicker() {
        FilePickerBuilder.getInstance()
                .setMaxCount(10) //optional
                .setActivityTheme(R.style.LibAppTheme) //optional
                .pickFile(this, 3);

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


    void loadingDocsInThread(List<Uri> documents) {
        dialog.show();

        new Thread(() -> {
            File sdCard = Environment.getExternalStorageDirectory();
            File dir = new File(sdCard.getAbsolutePath() + "/.KeyboardVault/MyVaultDocuments");
            try {
                int i = 0;
                while (i < documents.size()) {
                    String path = ContentUriUtils.INSTANCE.getFilePath(context, documents.get(i));
                    Log.d("MyDatatata", path);
                    String filename = path.substring(path.lastIndexOf("/") + 1);
                    if (filename.indexOf(".") > 0) {
                        filename = filename.substring(0, filename.lastIndexOf("."));
                        Log.d("Datatata", "dadfada");
                    }
                    String extension = path.substring(path.lastIndexOf("."));
                    File from = new File(path);
                    File to = new File(dir + "/" + filename + "#" + extension);
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


    void renamingFile(String fileNameWithHash, String fileNameWithoutHash, String extension) {
        dialog.show();
        Log.d("MyData", fileNameWithHash);
        Log.d("MyData", fileNameWithoutHash);
        Log.d("MyData", extension);
        new Thread(() -> {

            try {
//                openDocument(new File(fileName));
                File fileDirectory = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/MyVaultDocuments/");
                File from = new File(fileDirectory, fileNameWithHash);
                File to = new File(fileDirectory, fileNameWithoutHash + extension);
                from.renameTo(to);
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
        if (resultCode == Activity.RESULT_OK && data != null) {
            if (requestCode == 3) {
                if (resultCode == Activity.RESULT_OK && data != null) {
                    ArrayList<Uri> docPaths = new ArrayList<>(data.getParcelableArrayListExtra(FilePickerConst.KEY_SELECTED_DOCS));
                    loadingDocsInThread(docPaths);
                    Log.d("MyDocuments", docPaths.toString() + "");

                }

            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    void searchingDocuments() {
        binding.etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                // TODO Auto-generated method stub
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable s) {

                // filter your list from your input
                filter(s.toString());
                //you can use runnable postDelayed like 500 ms to delay search text
            }
        });
    }


    void filter(String text) {
        List<File> searchList = new ArrayList();
        for (File d : gettingHiddenDocuments()) {
            //or use .equal(text) with you want equal match
            //use .toLowerCase() for better matches
            if (d.getAbsolutePath().contains(text)) {
                searchList.add(d);
            }
        }
        //update recyclerview
        hiddenDocumentsAdapter.updateAdapter(searchList);
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
//            sourceFile.delete();
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


    List<File> gettingHiddenDocuments() {
        File fileDirectory = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/KeyboardVault/MyVaultDocuments/");
        File[] dirFiles = fileDirectory.listFiles();
        return Arrays.asList(dirFiles);

    }

    void settingAdapter() {
        hiddenDocumentsAdapter = new HiddenDocumentsAdapter(context, gettingHiddenDocuments());
        binding.rvDocuments.setLayoutManager(new LinearLayoutManager(context));
        binding.rvDocuments.setAdapter(hiddenDocumentsAdapter);

    }


    void restoringFiles(String path) {

        File sdCard = Environment.getExternalStorageDirectory();
        File dir = new File(sdCard.getAbsolutePath() + "/.KeyboardVault/RestoredDocuments");
        File from = new File(path);
        String extension = path.substring(path.lastIndexOf("#")).replace("#", "");
        String filename = path.substring(path.lastIndexOf("/") + 1);
        if (filename.indexOf("#") > 0)
            filename = filename.substring(0, filename.lastIndexOf("#"));
        File to = new File(dir + "/" + filename + "." + extension);
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

    public void openDocument(File path) {
        Uri pdf = FileProvider.getUriForFile(this, this.getApplicationContext().getPackageName() + ".provider", path);

        Intent pdfOpenintent = new Intent(Intent.ACTION_VIEW);
        pdfOpenintent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        pdfOpenintent.setDataAndType(pdf, "application/pdf");

        try {
            startActivity(pdfOpenintent);
        } catch (ActivityNotFoundException e) {
            // handle no application here....
        }
    }

    @Override
    public void onDocumentClickCallBack(String filePath) {


        openDocument(new File(filePath));

    }

}