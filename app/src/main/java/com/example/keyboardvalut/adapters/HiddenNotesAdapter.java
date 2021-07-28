package com.example.keyboardvalut.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.keyboardvalut.R;
import com.example.keyboardvalut.databinding.NotesRowItemBinding;
import com.example.keyboardvalut.interfaces.DeleteNoteCallback;
import com.example.keyboardvalut.interfaces.EditNoteCallback;
import com.example.keyboardvalut.interfaces.OnNoteClick;
import com.example.keyboardvalut.models.NotesModel;

import java.io.File;
import java.util.List;

public class HiddenNotesAdapter extends RecyclerView.Adapter<HiddenNotesAdapter.ViewHolder> {

    private List<NotesModel> notesList;
    private Context context;
    EditNoteCallback editNoteCallback;
    DeleteNoteCallback deleteNoteCallback;
    OnNoteClick onNoteClick;


    public HiddenNotesAdapter(Context context, List<NotesModel> notesList) {
        this.notesList = notesList;
        this.context = context;
        editNoteCallback = (EditNoteCallback) context;
        deleteNoteCallback = (DeleteNoteCallback) context;
        onNoteClick = (OnNoteClick) context;


    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        NotesRowItemBinding binding =
                DataBindingUtil.inflate(LayoutInflater.from(viewGroup.getContext()),
                        R.layout.notes_row_item, viewGroup, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {

        viewHolder.binding.tvHeader.setText(notesList.get(position).getTitle());
        viewHolder.binding.tvDescription.setText(notesList.get(position).getDescription());

        viewHolder.binding.ivEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editNoteCallback.onEditNoteCallback(notesList.get(position).getId());
            }
        });
        viewHolder.binding.ivDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteNoteCallback.onDeleteNoteCallback(notesList.get(position).getId());
            }
        });

        viewHolder.binding.cvNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onNoteClick.onNoteClickCallback(notesList.get(position).getId());
            }
        });


    }

    @Override
    public int getItemCount() {
        if (notesList != null) {
            return notesList.size();
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
        private final NotesRowItemBinding binding;

        public ViewHolder(@NonNull NotesRowItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    public void refreshAdapter(List<NotesModel> updatedList) {
        notesList = updatedList;
        notifyDataSetChanged();
    }

    public void updateAdapter(List<NotesModel> updatedList) {
        notesList = updatedList;
        notifyDataSetChanged();

    }
}
