package com.example.keyboardvalut.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.keyboardvalut.R;
import com.example.keyboardvalut.databinding.ContactsVaultRowItemBinding;
import com.example.keyboardvalut.interfaces.ContactEditCallback;
import com.example.keyboardvalut.interfaces.DeleteContactCallback;
import com.example.keyboardvalut.models.ContactsModel;

import java.util.Collections;
import java.util.List;

public class HiddenContactsAdapter extends RecyclerView.Adapter<HiddenContactsAdapter.ViewHolder> {

    private List<ContactsModel> contactList;
    private Context context;

    ContactEditCallback contactEditCallback;
    DeleteContactCallback deleteContactCallback;


    public HiddenContactsAdapter(Context context, List<ContactsModel> contactList) {
        this.contactList = contactList;
        this.context = context;

        contactEditCallback = (ContactEditCallback) context;
        deleteContactCallback = (DeleteContactCallback) context;

        Collections.reverse(contactList);


    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        ContactsVaultRowItemBinding binding =
                DataBindingUtil.inflate(LayoutInflater.from(viewGroup.getContext()),
                        R.layout.contacts_vault_row_item, viewGroup, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {

        viewHolder.binding.tvName.setText(contactList.get(position).getName());

        String contactFirstLetter = contactList.get(position).getName().charAt(0) + "";

        viewHolder.binding.tvFirstLetter.setText(contactFirstLetter.toUpperCase());

        viewHolder.binding.ivEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                contactEditCallback.onEditContactCallback(contactList.get(position).getName(), contactList.get(position).getNumber(), contactList.get(position).getId());
            }
        });

        viewHolder.binding.ivDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteContactCallback.onDeleteContactCallback(contactList.get(position).getId());
            }
        });


        viewHolder.binding.ivCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + contactList.get(position).getNumber() + ""));
                context.startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        if (contactList != null) {
            return contactList.size();
        } else {
            return 0;
        }
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private final ContactsVaultRowItemBinding binding;

        public ViewHolder(@NonNull ContactsVaultRowItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    public void refreshAdapter(List<ContactsModel> updatedList) {
        contactList = updatedList;
        notifyDataSetChanged();
    }

    public void updateAdapter(List<ContactsModel> updatedList) {
        contactList = updatedList;
        notifyDataSetChanged();

    }
}
