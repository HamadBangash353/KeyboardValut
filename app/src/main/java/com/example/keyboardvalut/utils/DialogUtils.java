package com.example.keyboardvalut.utils;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;

import androidx.databinding.DataBindingUtil;

import com.example.keyboardvalut.R;
import com.example.keyboardvalut.databinding.AddContactsDialogBinding;
import com.example.keyboardvalut.databinding.BreakInAlertConfirmDialogBinding;
import com.example.keyboardvalut.databinding.ChangeIconConfirmDialogBinding;
import com.example.keyboardvalut.databinding.DeleteAllDialogBinding;
import com.example.keyboardvalut.databinding.DeleteConfirmDialogBinding;
import com.example.keyboardvalut.databinding.DeleteContactDialogBinding;
import com.example.keyboardvalut.databinding.DeleteNotesConfirmDialogBinding;
import com.example.keyboardvalut.databinding.RestoreAllDialogBinding;
import com.example.keyboardvalut.databinding.RestoreConfirmDialogBinding;
import com.example.keyboardvalut.interfaces.ClickListener;

public class DialogUtils {

    public static void restoreDialog(Dialog dialog, ClickListener clickListener) {
        RestoreConfirmDialogBinding binding = DataBindingUtil.inflate(LayoutInflater.from(dialog.getContext()), R.layout.restore_confirm_dialog, null, false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(binding.getRoot());
        binding.setClickListener(clickListener);
    }


    public static void deleteFileDialog(Dialog dialog, ClickListener clickListener) {
        DeleteConfirmDialogBinding binding = DataBindingUtil.inflate(LayoutInflater.from(dialog.getContext()), R.layout.delete_confirm_dialog, null, false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(binding.getRoot());
        binding.setClickListener(clickListener);
    }

    public static void restoreAllFileDialog(Dialog dialog, ClickListener clickListener) {
        RestoreAllDialogBinding binding = DataBindingUtil.inflate(LayoutInflater.from(dialog.getContext()), R.layout.restore_all_dialog, null, false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(binding.getRoot());
        binding.setClickListener(clickListener);
    }

    public static void deleteAllFileDialog(Dialog dialog, ClickListener clickListener) {
        DeleteAllDialogBinding binding = DataBindingUtil.inflate(LayoutInflater.from(dialog.getContext()), R.layout.delete_all_dialog, null, false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(binding.getRoot());
        binding.setClickListener(clickListener);
    }

    public static void deleteNoteDialog(Dialog dialog, ClickListener clickListener) {
        DeleteNotesConfirmDialogBinding binding = DataBindingUtil.inflate(LayoutInflater.from(dialog.getContext()), R.layout.delete_notes_confirm_dialog, null, false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(binding.getRoot());
        binding.setClickListener(clickListener);
    }


    public static void deleteContactDialog(Dialog dialog, ClickListener clickListener) {
        DeleteContactDialogBinding binding = DataBindingUtil.inflate(LayoutInflater.from(dialog.getContext()), R.layout.delete_contact_dialog, null, false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(binding.getRoot());
        binding.setClickListener(clickListener);
    }

    public static void replaceIconDialog(Dialog dialog, ClickListener clickListener) {
        ChangeIconConfirmDialogBinding binding = DataBindingUtil.inflate(LayoutInflater.from(dialog.getContext()), R.layout.change_icon_confirm_dialog, null, false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(binding.getRoot());
        binding.setClickListener(clickListener);
    }

    public static void breakInAlertDialog(Dialog dialog, ClickListener clickListener) {
        BreakInAlertConfirmDialogBinding binding = DataBindingUtil.inflate(LayoutInflater.from(dialog.getContext()), R.layout.break_in_alert_confirm_dialog, null, false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(binding.getRoot());
        binding.setClickListener(clickListener);
    }





}
