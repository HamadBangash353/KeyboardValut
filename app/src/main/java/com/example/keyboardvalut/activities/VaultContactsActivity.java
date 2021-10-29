package com.example.keyboardvalut.activities;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.lifecycle.ProcessLifecycleOwner;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.keyboardvalut.R;
import com.example.keyboardvalut.adapters.HiddenContactsAdapter;
import com.example.keyboardvalut.adapters.HiddenNotesAdapter;
import com.example.keyboardvalut.databinding.ActivityVaultContactsBinding;
import com.example.keyboardvalut.databinding.AddContactsDialogBinding;
import com.example.keyboardvalut.databinding.EditContactsDialogBinding;
import com.example.keyboardvalut.helper.DatabaseHelper;
import com.example.keyboardvalut.interfaces.ClickListener;
import com.example.keyboardvalut.interfaces.ContactEditCallback;
import com.example.keyboardvalut.interfaces.DeleteContactCallback;
import com.example.keyboardvalut.models.ContactsModel;
import com.example.keyboardvalut.models.NotesModel;
import com.example.keyboardvalut.utils.DialogUtils;
import com.example.keyboardvalut.utils.ScreenUtils;

import java.util.ArrayList;
import java.util.List;

public class VaultContactsActivity extends AppCompatActivity implements ClickListener, ContactEditCallback, DeleteContactCallback, LifecycleObserver {

    Context context;
    Intent intent;
    ActivityVaultContactsBinding binding;

    HiddenContactsAdapter hiddenContactsAdapter;
    List<ContactsModel> contactsList;

    Dialog addContactsAddDialog;
    Dialog editContactDialog;
    Dialog deleteContactDialog;

    EditContactsDialogBinding editDialogBinding;

    int contactId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_vault_contacts);
        ScreenUtils.hidingStatusBar(this);

        context = this;

        addContactsAddDialog = new Dialog(context);
        editContactDialog = new Dialog(context);
        deleteContactDialog = new Dialog(context);
        ProcessLifecycleOwner.get().getLifecycle().addObserver(this);
        searchingNotes();
        creatingAddContactsDialog();
        creatingEditContactsDialog();


        binding.setClickHandler(this);
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
            case R.id.btnAddContacts:
                creatingAddContactsDialog();
                addContactsAddDialog.show();
                break;

            case R.id.btnDelete:
                deleteContact();
                deleteContactDialog.dismiss();
                break;

            case R.id.btnCancelDeleteDialog:
            case R.id.ivExitDeleteDialog:
                deleteContactDialog.dismiss();
                break;

            case R.id.btnBack:
                movingToBackActivity();
                break;
        }
    }


    void searchingNotes() {
        binding.etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if (!s.toString().isEmpty()) {
                    binding.btnAddContacts.setVisibility(View.INVISIBLE);
                    binding.emptyLayoutIndicator.setVisibility(View.GONE);
                } else {
                    binding.btnAddContacts.setVisibility(View.VISIBLE);
                    if (contactsList.size() == 0) {
                        binding.emptyLayoutIndicator.setVisibility(View.VISIBLE);
                    }
                }

                filter(s.toString());
            }
        });
    }

    private void addingContacts(String name, String number) {

        ContactsModel model = new ContactsModel();
        model.setName(name);
        model.setNumber(number);
        DatabaseHelper.getDatabase(context).contactsDao().insert(model);


    }

    private void updateContact(int id, String name, String number) {

        DatabaseHelper.getDatabase(context).contactsDao().updateContact(id, name, number);
        refreshAdapter();
    }


    void filter(String text) {
        List<ContactsModel> searchList = new ArrayList();
        for (ContactsModel d : contactsList) {
            //or use .equal(text) with you want equal match
            //use .toLowerCase() for better matches
            if (d.getName().toLowerCase().contains(text.toLowerCase())) {
                searchList.add(d);
            }
        }

        hiddenContactsAdapter.updateAdapter(searchList);
    }


    void settingAdapter() {
        contactsList = DatabaseHelper.getDatabase(context).contactsDao().getAllContacts();
        manipulatingEmptyIndicator();
        hiddenContactsAdapter = new HiddenContactsAdapter(context, contactsList);
        binding.rvContacts.setLayoutManager(new LinearLayoutManager(context));
        binding.rvContacts.setAdapter(hiddenContactsAdapter);

    }


    void manipulatingEmptyIndicator() {
        if (contactsList.size() == 0) {
            binding.emptyLayoutIndicator.setVisibility(View.VISIBLE);
        } else {
            binding.emptyLayoutIndicator.setVisibility(View.GONE);
        }
    }


    void refreshAdapter() {
        contactsList = DatabaseHelper.getDatabase(context).contactsDao().getAllContacts();
        hiddenContactsAdapter.refreshAdapter(contactsList);
        manipulatingEmptyIndicator();
    }

    private void deleteContact() {
        DatabaseHelper.getDatabase(context).contactsDao().delete(contactId);
        refreshAdapter();

    }

    @Override
    protected void onResume() {
        super.onResume();
        settingAdapter();
    }


    private void creatingAddContactsDialog() {
        AddContactsDialogBinding dialogBinding = DataBindingUtil.inflate(LayoutInflater.from(addContactsAddDialog.getContext()), R.layout.add_contacts_dialog, null, false);
        addContactsAddDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        addContactsAddDialog.setContentView(dialogBinding.getRoot());

        dialogBinding.etAddNumber.setText("");
        dialogBinding.etAddName.setText("");

        dialogBinding.btnDoneAddContacts.setOnClickListener(v -> {
            if (dialogBinding.etAddName.getText().toString().isEmpty() || dialogBinding.etAddNumber.getText().toString().isEmpty()) {
                Toast.makeText(context, "Please fill all fields", Toast.LENGTH_SHORT).show();
            } else {
                addingContacts(dialogBinding.etAddName.getText().toString(), dialogBinding.etAddNumber.getText().toString());

                dialogBinding.etAddNumber.setText("");
                dialogBinding.etAddName.setText("");
                refreshAdapter();
                addContactsAddDialog.dismiss();
            }
        });

        dialogBinding.ivExitContactDialog.setOnClickListener(v -> addContactsAddDialog.dismiss());
    }

    private void creatingEditContactsDialog() {
        editDialogBinding = DataBindingUtil.inflate(LayoutInflater.from(editContactDialog.getContext()), R.layout.edit_contacts_dialog, null, false);
        editContactDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        editContactDialog.setContentView(editDialogBinding.getRoot());

        editDialogBinding.etAddNumber.setText("");
        editDialogBinding.etAddName.setText("");

        editDialogBinding.btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editDialogBinding.etAddName.getText().toString().isEmpty() || editDialogBinding.etAddNumber.getText().toString().isEmpty()) {
                    Toast.makeText(context, "Please fill all fields", Toast.LENGTH_SHORT).show();
                } else {
                    updateContact(contactId, editDialogBinding.etAddName.getText().toString(), editDialogBinding.etAddNumber.getText().toString());
                    refreshAdapter();
                    editContactDialog.dismiss();
                }
            }
        });

        editDialogBinding.ivExitEditContactDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editContactDialog.dismiss();
            }
        });
    }

    @Override
    public void onEditContactCallback(String name, String number, int id) {

        contactId = id;
        editDialogBinding.etAddName.setText(name);
        editDialogBinding.etAddNumber.setText(number);
        editContactDialog.show();
    }

    @Override
    public void onDeleteContactCallback(int contactId) {
        this.contactId = contactId;
        DialogUtils.deleteContactDialog(deleteContactDialog, this);
        deleteContactDialog.show();
    }

    private void movingToBackActivity() {
        intent = new Intent(this, VaultSubActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
    }
}