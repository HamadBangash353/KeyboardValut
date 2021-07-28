package com.example.keyboardvalut.interfaces;

import android.opengl.Visibility;
import android.view.View;

import java.util.List;
import java.util.Map;

public interface SelectedPathListCallback {

    void onPathSelected(List<String> selectedPathList, int visibility);
}
