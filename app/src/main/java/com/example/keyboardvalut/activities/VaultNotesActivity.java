package com.example.keyboardvalut.activities;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.keyboardvalut.R;
import com.example.keyboardvalut.adapters.HiddenNotesAdapter;
import com.example.keyboardvalut.databinding.ActivityVaultNotesBinding;
import com.example.keyboardvalut.helper.DatabaseHelper;
import com.example.keyboardvalut.interfaces.ClickListener;
import com.example.keyboardvalut.interfaces.DeleteNoteCallback;
import com.example.keyboardvalut.interfaces.EditNoteCallback;
import com.example.keyboardvalut.interfaces.OnNoteClick;
import com.example.keyboardvalut.models.NotesModel;
import com.example.keyboardvalut.utils.DialogUtils;
import com.example.keyboardvalut.utils.ScreenUtils;

import java.util.ArrayList;
import java.util.List;

public class VaultNotesActivity extends AppCompatActivity implements ClickListener, EditNoteCallback, DeleteNoteCallback, OnNoteClick {

    Context context;
    Intent intent;
    ActivityVaultNotesBinding binding;

    HiddenNotesAdapter hiddenNotesAdapter;
    List<NotesModel> notesList;

    Dialog deleteNoteDialog;

    int noteId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_vault_notes);
        ScreenUtils.hidingStatusBar(this);

        context = this;

        deleteNoteDialog = new Dialog(context);
        searchingNotes();


        binding.setClickHandler(this);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnAddNotes:
                passingIntentToAddNotesActivity(-1);
                break;
            case R.id.btnDelete:
                deletingNote();
                deleteNoteDialog.dismiss();
                break;
        }
    }




    void searchingNotes() {
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
        List<NotesModel> searchList = new ArrayList();
        for (NotesModel d : notesList) {
            //or use .equal(text) with you want equal match
            //use .toLowerCase() for better matches
            if (d.getTitle().toLowerCase().contains(text)) {
                searchList.add(d);
            }
        }

        hiddenNotesAdapter.updateAdapter(searchList);
    }


    void passingIntentToAddNotesActivity(int noteId) {
        intent = new Intent(context, AddNoteActivity.class);
        intent.putExtra("noteId", noteId);
        startActivity(intent);
    }

    void passingIntentToViewNotesActivity(int noteId) {
        intent = new Intent(context, NotesViewingActivity.class);
        intent.putExtra("noteId", noteId);
        startActivity(intent);
    }

    void settingAdapter() {
        notesList = DatabaseHelper.getDatabase(context).notesDao().getAllNotes();


        manipulatingEmptyIndicator();
        hiddenNotesAdapter = new HiddenNotesAdapter(context, notesList);
        binding.rvNotes.setLayoutManager(new LinearLayoutManager(context));
        binding.rvNotes.setAdapter(hiddenNotesAdapter);

    }


    void manipulatingEmptyIndicator() {
        if (notesList.size() == 0) {
            binding.emptyLayoutIndicator.setVisibility(View.VISIBLE);
        } else {
            binding.emptyLayoutIndicator.setVisibility(View.GONE);
        }
    }


    void refreshAdapter() {
        notesList = DatabaseHelper.getDatabase(context).notesDao().getAllNotes();
        hiddenNotesAdapter.refreshAdapter(notesList);
        manipulatingEmptyIndicator();
    }

    private void deletingNote() {
        DatabaseHelper.getDatabase(context).notesDao().delete(noteId);
        refreshAdapter();

    }

    @Override
    protected void onResume() {
        super.onResume();
        settingAdapter();
    }

    @Override
    public void onEditNoteCallback(int noteId) {
        passingIntentToAddNotesActivity(noteId);

    }

    @Override
    public void onDeleteNoteCallback(int noteId) {
        this.noteId = noteId;
        DialogUtils.deleteNoteDialog(deleteNoteDialog, this);
        deleteNoteDialog.show();
    }

    @Override
    public void onNoteClickCallback(int noteId) {
        passingIntentToViewNotesActivity(noteId);
    }
}