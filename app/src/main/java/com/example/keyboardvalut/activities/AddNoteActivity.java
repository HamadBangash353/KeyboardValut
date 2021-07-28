package com.example.keyboardvalut.activities;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.icu.text.UnicodeSetSpanner;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.example.keyboardvalut.R;
import com.example.keyboardvalut.adapters.DrawerMenuAdapter;
import com.example.keyboardvalut.adapters.HiddenDocumentsAdapter;
import com.example.keyboardvalut.databinding.ActivityAddNoteBinding;
import com.example.keyboardvalut.databinding.ActivityVaultNotesBinding;
import com.example.keyboardvalut.helper.DatabaseHelper;
import com.example.keyboardvalut.interfaces.ClickListener;
import com.example.keyboardvalut.models.NotesModel;
import com.example.keyboardvalut.utils.DialogUtils;
import com.example.keyboardvalut.utils.ScreenUtils;

import java.util.ArrayList;
import java.util.List;


public class AddNoteActivity extends AppCompatActivity implements ClickListener {

    Context context;

    ActivityAddNoteBinding binding;

    int noteId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_note);
        ScreenUtils.hidingStatusBar(this);

        context = this;

        noteId = getIntent().getIntExtra("noteId", -2);

        if (noteId == -1) {
            binding.tvAddNotes.setText("Done");
        } else {
            binding.tvAddNotes.setText("Update Note");
            gettingNotesDataById();
        }

        binding.setClickHandler(this);

    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnAddPhotos:
                break;
            case R.id.btnDone:
                if (noteId == -1) {
                    addingNotesToDatabase();
                } else {
                    updatingNote();
                }
                break;

        }
    }

    void gettingNotesDataById() {
        NotesModel model = DatabaseHelper.getDatabase(context).notesDao().gettingNoteById(noteId);
        binding.etTitle.setText(model.getTitle());
        binding.etDescription.setText(model.getDescription());
    }


    private void addingNotesToDatabase() {
        String title = binding.etTitle.getText().toString();
        String description = binding.etDescription.getText().toString();

        if (title.isEmpty() || description.isEmpty()) {
            Toast.makeText(context, "Please fill all fields", Toast.LENGTH_SHORT).show();
        } else {
            NotesModel model = new NotesModel();
            model.setTitle(title);
            model.setDescription(description);
            DatabaseHelper.getDatabase(context).notesDao().insert(model);

            passingIntentToNotesActivity();

        }
    }

    private void updatingNote() {
      int i=  DatabaseHelper.getDatabase(context).notesDao().updateNote(noteId, binding.etTitle.getText().toString(), binding.etDescription.getText().toString());
        Log.d("MyData",i+"");
        passingIntentToNotesActivity();
    }


    void passingIntentToNotesActivity() {
        Intent i = new Intent(this, VaultNotesActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(i);
    }


}