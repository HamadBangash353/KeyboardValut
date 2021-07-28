package com.example.keyboardvalut.utils;

import android.app.Activity;
import android.view.WindowManager;

public class ScreenUtils {

    public static void hidingStatusBar(Activity activity) {
        activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

}
