package com.example.keyboardvalut.activities;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.lifecycle.ProcessLifecycleOwner;

import com.example.keyboardvalut.R;
import com.example.keyboardvalut.databinding.ActivityAddNoteBinding;
import com.example.keyboardvalut.databinding.ActivityViewNoteBinding;
import com.example.keyboardvalut.helper.DatabaseHelper;
import com.example.keyboardvalut.interfaces.ClickListener;
import com.example.keyboardvalut.models.NotesModel;
import com.example.keyboardvalut.utils.DialogUtils;
import com.example.keyboardvalut.utils.ScreenUtils;

public class NotesViewingActivity extends AppCompatActivity implements ClickListener, LifecycleObserver {

    Context context;

    ActivityViewNoteBinding binding;

    int noteId;

    Dialog deleteNoteDialog;

    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_view_note);
        ScreenUtils.hidingStatusBar(this);

        context = this;
        ProcessLifecycleOwner.get().getLifecycle().addObserver(this);
        noteId = getIntent().getIntExtra("noteId", -2);
        deleteNoteDialog = new Dialog(context);
        gettingNotesDataById();


        binding.setClickHandler(this);

    }

    private void deletingNote() {
        DatabaseHelper.getDatabase(context).notesDao().delete(noteId);
        passingIntentToNotesActivity();

    }

    int lifeCycleChecker = 0;
    @Override
    protected void onRestart() {
        super.onRestart();
        if (lifeCycleChecker==1)
        {
            startActivity(new Intent(this,VaultPasswordEnteringActivity.class));
            finish();

        }

    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    void onMoveToBackground() {
        lifeCycleChecker = 1;
    }


    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnDone:
                passingIntentToNotesActivity();
                break;

            case R.id.btnBack:
                movingToBackActivity();
                break;

            case R.id.ivEdit:
                passingIntentToAddNotesActivity(noteId);
                break;

            case R.id.ivDelete:
                DialogUtils.deleteNoteDialog(deleteNoteDialog, this);
                deleteNoteDialog.show();
                break;

            case R.id.ivCopy:
                copyingText();
                break;

            case R.id.ivExit:
            case R.id.btnCancel:
                deleteNoteDialog.dismiss();
                break;

            case R.id.btnDelete:
                deletingNote();
                deleteNoteDialog.dismiss();
                break;

        }
    }

    void passingIntentToAddNotesActivity(int noteId) {
        intent = new Intent(context, AddNoteActivity.class);
        intent.putExtra("noteId", noteId);
        startActivity(intent);
    }

    private void movingToBackActivity() {
        intent = new Intent(this, VaultNotesActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
    }


    private void copyingText() {
        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("NotesData", binding.etTitle.getText() + "\n" + binding.etDescription.getText());
        clipboard.setPrimaryClip(clip);
        Toast.makeText(context, "Text Copied", Toast.LENGTH_SHORT).show();
    }

    void gettingNotesDataById() {
        NotesModel model = DatabaseHelper.getDatabase(context).notesDao().gettingNoteById(noteId);
        binding.etTitle.setText(model.getTitle());
        binding.etDescription.setText(model.getDescription());
    }

    void passingIntentToNotesActivity() {
        Intent i = new Intent(this, VaultNotesActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(i);
    }

}