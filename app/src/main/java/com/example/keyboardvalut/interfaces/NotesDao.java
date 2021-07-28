package com.example.keyboardvalut.interfaces;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.keyboardvalut.models.NotesModel;
import com.lassi.presentation.cameraview.utils.Task;

import java.util.List;

@Dao
public interface NotesDao {

    @Query("SELECT * FROM NotesModel")
    List<NotesModel> getAllNotes();

    @Query("SELECT * FROM NotesModel WHERE id=:noteId")
    NotesModel gettingNoteById(int noteId);

    @Insert
    void insert(NotesModel notesModel);

    @Query("Delete FROM NotesModel WHERE id=:id")
    void delete(int id);

    @Query("UPDATE NotesModel SET title = :title , description=:description WHERE id = :id")
    int updateNote(int id, String title, String description);
}
