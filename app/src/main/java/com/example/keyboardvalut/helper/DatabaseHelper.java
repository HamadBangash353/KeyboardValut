package com.example.keyboardvalut.helper;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.keyboardvalut.interfaces.ContactsDao;
import com.example.keyboardvalut.interfaces.NotesDao;
import com.example.keyboardvalut.models.ContactsModel;
import com.example.keyboardvalut.models.NotesModel;

@Database(entities = {NotesModel.class, ContactsModel.class}, version = 1)
public abstract class DatabaseHelper extends RoomDatabase {
    public abstract NotesDao notesDao();
    public abstract ContactsDao contactsDao();

    private static DatabaseHelper INSTANCE;

   public static DatabaseHelper getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (DatabaseHelper.class) {
                if (INSTANCE == null) {
                    INSTANCE =
                            Room.databaseBuilder(context.getApplicationContext(),
                                    DatabaseHelper.class,
                                    "GeneralDatabase").allowMainThreadQueries().build();
                }
            }
        }
        return INSTANCE;
    }
}
