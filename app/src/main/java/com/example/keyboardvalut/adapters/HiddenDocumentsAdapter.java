package com.example.keyboardvalut.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.keyboardvalut.R;
import com.example.keyboardvalut.databinding.DocumentsVaultRowItemBinding;
import com.example.keyboardvalut.interfaces.DeleteFileCallback;
import com.example.keyboardvalut.interfaces.DocumentClickCallBack;
import com.example.keyboardvalut.interfaces.OnFileRestoreCallback;
import com.example.keyboardvalut.interfaces.SelectedPathListCallback;
import com.example.keyboardvalut.utils.SharedPrefUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class HiddenDocumentsAdapter extends RecyclerView.Adapter<HiddenDocumentsAdapter.ViewHolder> {

    private List<File> hiddenDocuments;
    private Context context;

    SharedPrefUtil prefUtil;
    boolean isLongPressed = false;

    List<String> selectedPathList;

    SelectedPathListCallback selectedPathListCallback;
    DocumentClickCallBack documentClickCallBack;

    private int visibilityCounter = 0;


    public HiddenDocumentsAdapter(Context context, List<File> hiddenDocuments) {
        this.hiddenDocuments = hiddenDocuments;
        this.context = context;
        prefUtil = new SharedPrefUtil(context);
        selectedPathListCallback = (SelectedPathListCallback) context;
        documentClickCallBack = (DocumentClickCallBack) context;
        selectedPathList = new ArrayList<>();

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        DocumentsVaultRowItemBinding binding =
                DataBindingUtil.inflate(LayoutInflater.from(viewGroup.getContext()),
                        R.layout.documents_vault_row_item, viewGroup, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {


        String filename = hiddenDocuments.get(position).getAbsolutePath().substring(hiddenDocuments.get(position).getAbsolutePath().lastIndexOf("/") + 1);
        viewHolder.binding.tvName.setText(filename.replace("#", "."));

        String extension = "";
        if (hiddenDocuments.get(position).getAbsolutePath().contains("#")) {
            extension = hiddenDocuments.get(position).getAbsolutePath().substring(hiddenDocuments.get(position).getAbsolutePath().lastIndexOf("#"));
        } else {
            extension = hiddenDocuments.get(position).getAbsolutePath().substring(hiddenDocuments.get(position).getAbsolutePath().lastIndexOf("."));

        }
        Log.d("MyExtension", extension);
        switch (extension) {
            case "#pdf":
            case ".pdf":
                viewHolder.binding.ivType.setImageResource(R.drawable.ic_pdf);
                break;
            case "#doc":
                viewHolder.binding.ivType.setImageResource(R.drawable.ic_doc);
                break;
            case "#excel":
                viewHolder.binding.ivType.setImageResource(R.drawable.ic_excel);
                break;
            case "#ppt":
                viewHolder.binding.ivType.setImageResource(R.drawable.ic_ppt);
                break;
        }

        viewHolder.binding.layoutCheckBox.setOnClickListener(v -> {
            if (viewHolder.binding.ivUnchecked.getVisibility() == View.INVISIBLE) {
                viewHolder.binding.ivUnchecked.setVisibility(View.VISIBLE);
                viewHolder.binding.ivChecked.setVisibility(View.INVISIBLE);

                visibilityCounter++;
                selectedPathList.add(hiddenDocuments.get(position).getAbsolutePath());

            } else {
                viewHolder.binding.ivChecked.setVisibility(View.VISIBLE);
                viewHolder.binding.ivUnchecked.setVisibility(View.INVISIBLE);

                visibilityCounter--;
                selectedPathList.remove(hiddenDocuments.get(position).getAbsolutePath());

            }

            selectedPathListCallback.onPathSelected(selectedPathList, visibilityCounter);

        });

        viewHolder.binding.cvMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                documentClickCallBack.onDocumentClickCallBack(hiddenDocuments.get(position).getAbsolutePath());
            }
        });


    }

    public void refreshAdapter(List<File> updatedList) {
        hiddenDocuments = updatedList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (hiddenDocuments != null) {
            return hiddenDocuments.size();
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
        private final DocumentsVaultRowItemBinding binding;

        public ViewHolder(@NonNull DocumentsVaultRowItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    public void updateAdapter(List<File> updatedList) {
        hiddenDocuments = updatedList;
        notifyDataSetChanged();

    }
}
