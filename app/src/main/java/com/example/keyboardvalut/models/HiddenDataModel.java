package com.example.keyboardvalut.models;

import java.io.File;

public class HiddenDataModel {

    File file;
    boolean isSelected=false;

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
