package com.example.keyboardvalut.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class NotesModel {

    @PrimaryKey(autoGenerate = true)
    public int id;
    String title;
    String description;

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
