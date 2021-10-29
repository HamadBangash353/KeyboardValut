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
import com.example.keyboardvalut.databinding.BreakInAlertRowItemBinding;
import com.example.keyboardvalut.databinding.PhotoVaultRowItemBinding;
import com.example.keyboardvalut.interfaces.DeleteFileCallback;
import com.example.keyboardvalut.interfaces.OnImageClickCallback;
import com.example.keyboardvalut.utils.ImageRotationUtil;
import com.example.keyboardvalut.utils.SharedPrefUtil;

import java.io.File;
import java.util.List;

public class BreakInAlertImageAdapter extends RecyclerView.Adapter<BreakInAlertImageAdapter.ViewHolder> {

    private List<File> breakInAlertImages;
    private Context context;;
    SharedPrefUtil prefUtil;
    boolean isLongPressed = false;


    DeleteFileCallback photoDeleteCallback;

    OnImageClickCallback imageClickCallback;



    public BreakInAlertImageAdapter(Context context, List<File> breakInAlertImages) {
        this.breakInAlertImages = breakInAlertImages;
        this.context = context;
        prefUtil = new SharedPrefUtil(context);
        photoDeleteCallback = (DeleteFileCallback) context;
        imageClickCallback = (OnImageClickCallback) context;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        BreakInAlertRowItemBinding binding =
                DataBindingUtil.inflate(LayoutInflater.from(viewGroup.getContext()),
                        R.layout.break_in_alert_row_item, viewGroup, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        BitmapFactory.Options Options = new BitmapFactory.Options();
        Options.inSampleSize = 3;
        Options.inJustDecodeBounds = false;
        Bitmap myBitmap = BitmapFactory.decodeFile(breakInAlertImages.get(position).getAbsolutePath(), Options);
        Bitmap rotatedBitMap = ImageRotationUtil.rotatedBitmap(breakInAlertImages.get(position).getAbsolutePath(), myBitmap);
        viewHolder.binding.ivImage.setImageBitmap(rotatedBitMap);

        viewHolder.binding.ivImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageClickCallback.onImageClickCallback(breakInAlertImages.get(position).getPath());
            }
        });


        viewHolder.binding.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                photoDeleteCallback.onFileDeleteCallback(breakInAlertImages.get(position).getAbsolutePath());
            }
        });

    }

    public void refreshAdapter(List<File> updatedList) {
        breakInAlertImages = updatedList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (breakInAlertImages != null) {
            return breakInAlertImages.size();
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
        private final BreakInAlertRowItemBinding binding;

        public ViewHolder(@NonNull BreakInAlertRowItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
