package com.example.keyboardvalut.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.keyboardvalut.R;
import com.example.keyboardvalut.databinding.PhotoVaultRowItemBinding;
import com.example.keyboardvalut.interfaces.DeleteFileCallback;
import com.example.keyboardvalut.interfaces.OnImageClickCallback;
import com.example.keyboardvalut.interfaces.OnVideoClickCallback;
import com.example.keyboardvalut.interfaces.SelectedPathListCallback;
import com.example.keyboardvalut.interfaces.OnFileRestoreCallback;
import com.example.keyboardvalut.utils.SharedPrefUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class HiddenImagesAdapter extends RecyclerView.Adapter<HiddenImagesAdapter.ViewHolder> {

    private List<File> hiddenImagesList;
    private Context context;
    private OnFileRestoreCallback onFileRestoreCallback;
    SharedPrefUtil prefUtil;
    boolean isLongPressed = false;

    List<String> selectedPathList;

    SelectedPathListCallback selectedPathListCallback;
    DeleteFileCallback photoDeleteCallback;
    OnImageClickCallback imageClickCallback;
    private int visibilityCounter = 0;


    public HiddenImagesAdapter(Context context, List<File> effectsList) {
        this.hiddenImagesList = effectsList;
        this.context = context;
        prefUtil = new SharedPrefUtil(context);
        onFileRestoreCallback = (OnFileRestoreCallback) context;
        photoDeleteCallback = (DeleteFileCallback) context;
        selectedPathListCallback = (SelectedPathListCallback) context;
        imageClickCallback = (OnImageClickCallback) context;
        selectedPathList = new ArrayList<>();

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        PhotoVaultRowItemBinding binding =
                DataBindingUtil.inflate(LayoutInflater.from(viewGroup.getContext()),
                        R.layout.photo_vault_row_item, viewGroup, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        BitmapFactory.Options Options = new BitmapFactory.Options();
        Options.inSampleSize = 3;
        Options.inJustDecodeBounds = false;
        Bitmap myBitmap = BitmapFactory.decodeFile(hiddenImagesList.get(position).getAbsolutePath(), Options);
        viewHolder.binding.ivImage.setImageBitmap(myBitmap);


        if (prefUtil.getClickedPos() == -1) {
            viewHolder.binding.ivShade.setVisibility(View.GONE);
            viewHolder.binding.ivTick.setVisibility(View.GONE);
        }

        viewHolder.binding.ivImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (visibilityCounter == 0 && viewHolder.binding.ivTick.getVisibility() == View.GONE) {
                    imageClickCallback.onImageClickCallback(hiddenImagesList.get(position).getPath());
                    isLongPressed = false;
                }

                if (isLongPressed) {
                    prefUtil.setClickedPos(position);

                    Log.d("MyPosition", position + "");

                    if (viewHolder.binding.ivShade.getVisibility() == View.VISIBLE) {
                        viewHolder.binding.ivShade.setVisibility(View.GONE);
                        viewHolder.binding.ivTick.setVisibility(View.GONE);
                        visibilityCounter--;
                        selectedPathList.remove(hiddenImagesList.get(position).getAbsolutePath());

                    } else {
                        viewHolder.binding.ivShade.setVisibility(View.VISIBLE);
                        viewHolder.binding.ivTick.setVisibility(View.VISIBLE);
                        visibilityCounter++;
                        selectedPathList.add(hiddenImagesList.get(position).getAbsolutePath());

                    }

                    Log.d("Counter", visibilityCounter + "");
                    selectedPathListCallback.onPathSelected(selectedPathList, visibilityCounter);
                }
            }
        });

        viewHolder.binding.ivImage.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                prefUtil.setClickedPos(position);
                viewHolder.binding.ivShade.setVisibility(View.VISIBLE);
                viewHolder.binding.ivTick.setVisibility(View.VISIBLE);
                selectedPathList.add(hiddenImagesList.get(position).getAbsolutePath());
                visibilityCounter++;

                selectedPathListCallback.onPathSelected(selectedPathList, visibilityCounter);
                isLongPressed = true;
                notifyDataSetChanged();


                return true;
            }
        });


        viewHolder.binding.btnRestore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onFileRestoreCallback.onFileRestoreCallback(hiddenImagesList.get(position).getAbsolutePath());
            }
        });


        viewHolder.binding.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                photoDeleteCallback.onFileDeleteCallback(hiddenImagesList.get(position).getAbsolutePath());
            }
        });
    }

    public void refreshAdapter(List<File> updatedList)
    {
        hiddenImagesList=updatedList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (hiddenImagesList != null) {
            return hiddenImagesList.size();
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
        private final PhotoVaultRowItemBinding binding;

        public ViewHolder(@NonNull PhotoVaultRowItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
