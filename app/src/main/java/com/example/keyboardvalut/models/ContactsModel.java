package com.example.keyboardvalut.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class ContactsModel {

    @PrimaryKey(autoGenerate = true)
    public int id;
    String name;
    String number;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }
}
