package com.example.keyboardvalut.interfaces;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.keyboardvalut.models.ContactsModel;
import com.example.keyboardvalut.models.NotesModel;

import java.util.List;

@Dao
public interface ContactsDao {

    @Query("SELECT * FROM ContactsModel")
    List<ContactsModel> getAllContacts();

    @Query("SELECT * FROM ContactsModel WHERE id=:contactId")
    ContactsModel gettingContactsById(int contactId);

    @Insert
    void insert(ContactsModel contactsModel);

    @Query("Delete FROM ContactsModel WHERE id=:id")
    void delete(int id);

    @Query("UPDATE ContactsModel SET name = :name , number=:number WHERE id = :id")
    int updateContact(int id, String name, String number);
}
