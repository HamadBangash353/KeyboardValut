package com.example.keyboardvalut.utils;

import android.os.Environment;

import java.io.File;

public class DirectoriesUtils {


    public static void settingHidingImagesDirectory() {
        File dir = new File(Environment.getExternalStorageDirectory() + "/.KeyboardVault/MyVaultAppImages");
        try {
            if (dir.mkdir()) {
                System.out.println("Directory created");
            } else {
                System.out.println("Directory is not created");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void settingMainDirectory() {
        File dir = new File(Environment.getExternalStorageDirectory() + "/.KeyboardVault");
        try {
            if (dir.mkdir()) {
                System.out.println("Directory created");
            } else {
                System.out.println("Directory is not created");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void settingHidingVideoDirectory() {
        File dir = new File(Environment.getExternalStorageDirectory() + "/.KeyboardVault/MyVaultVideos");
        try {
            if (dir.mkdir()) {
                System.out.println("Directory created");
            } else {
                System.out.println("Directory is not created");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void settingRestoreDirectory() {
        File dir = new File(Environment.getExternalStorageDirectory() + "/.KeyboardVault/RestoredData");
        try {
            if (dir.mkdir()) {
                System.out.println("Directory created");
            } else {
                System.out.println("Directory is not created");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void settingHiddenDocumentsDirectory() {
        File dir = new File(Environment.getExternalStorageDirectory() + "/.KeyboardVault/MyVaultDocuments");
        try {
            if (dir.mkdir()) {
                System.out.println("Directory created");
            } else {
                System.out.println("Directory is not created");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void restoringHiddenDocumentsDirectory() {
        File dir = new File(Environment.getExternalStorageDirectory() + "/.KeyboardVault/RestoredDocuments");
        try {
            if (dir.mkdir()) {
                System.out.println("Directory created");
            } else {
                System.out.println("Directory is not created");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void settingCameraImagesDirectory() {
        File dir = new File(Environment.getExternalStorageDirectory() + "/.KeyboardVault/VaultMediaImages");
        try {
            if (dir.mkdir()) {
                System.out.println("Directory created");
            } else {
                System.out.println("Directory is not created");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void settingWrongPassImagesDirectory() {
        File dir = new File(Environment.getExternalStorageDirectory() + "/.KeyboardVault/wrongPassImages");
        try {
            if (dir.mkdir()) {
                System.out.println("Directory created");
            } else {
                System.out.println("Directory is not created");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
