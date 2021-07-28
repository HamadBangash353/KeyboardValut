package com.example.keyboardvalut.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.keyboardvalut.R;
import com.example.keyboardvalut.databinding.PhotoVaultRowItemBinding;
import com.example.keyboardvalut.interfaces.DeleteFileCallback;
import com.example.keyboardvalut.interfaces.OnVideoClickCallback;
import com.example.keyboardvalut.interfaces.SelectedPathListCallback;
import com.example.keyboardvalut.interfaces.OnFileRestoreCallback;
import com.example.keyboardvalut.utils.SharedPrefUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class HiddenVideosAdapter extends RecyclerView.Adapter<HiddenVideosAdapter.ViewHolder> {

    private List<File> videoMediaList;
    private final Context context;
    private final OnFileRestoreCallback onFileRestoreCallback;
    SharedPrefUtil prefUtil;
    boolean isLongPressed = false;

    List<String> selectedPathList;

    SelectedPathListCallback selectedPathListCallback;
    DeleteFileCallback photoDeleteCallback;
    OnVideoClickCallback videoClickCallback;
    private int visibilityCounter = 0;


    public HiddenVideosAdapter(Context context, List<File> effectsList) {
        this.videoMediaList = effectsList;
        this.context = context;
        prefUtil = new SharedPrefUtil(context);
        onFileRestoreCallback = (OnFileRestoreCallback) context;
        photoDeleteCallback = (DeleteFileCallback) context;
        selectedPathListCallback = (SelectedPathListCallback) context;
        videoClickCallback = (OnVideoClickCallback) context;
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
        RequestOptions requestOptions = new RequestOptions();
        Glide.with(context)
                .load("video_url")
                .apply(requestOptions)
                .thumbnail(Glide.with(context).load(videoMediaList.get(position).getAbsolutePath()))
                .into(viewHolder.binding.ivImage);


        if (prefUtil.getClickedPos() == -1) {
            viewHolder.binding.ivShade.setVisibility(View.GONE);
            viewHolder.binding.ivTick.setVisibility(View.GONE);
        }

        viewHolder.binding.ivImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (visibilityCounter == 0 && viewHolder.binding.ivTick.getVisibility() == View.GONE) {
                    videoClickCallback.onVideoClickCallback(videoMediaList.get(position).getPath());
                    isLongPressed = false;
                }
                if (isLongPressed) {
                    prefUtil.setClickedPos(position);

                    if (viewHolder.binding.ivShade.getVisibility() == View.VISIBLE) {
                        viewHolder.binding.ivShade.setVisibility(View.GONE);
                        viewHolder.binding.ivTick.setVisibility(View.GONE);
                        visibilityCounter--;
                        selectedPathList.remove(videoMediaList.get(position).getPath());

                    } else {
                        viewHolder.binding.ivShade.setVisibility(View.VISIBLE);
                        viewHolder.binding.ivTick.setVisibility(View.VISIBLE);
                        visibilityCounter++;
                        selectedPathList.add(videoMediaList.get(position).getPath());

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
                selectedPathList.add(videoMediaList.get(position).getPath());
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
                onFileRestoreCallback.onFileRestoreCallback(videoMediaList.get(position).getPath());
            }
        });


        viewHolder.binding.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                photoDeleteCallback.onFileDeleteCallback(videoMediaList.get(position).getPath());
            }
        });
    }

    public void refreshAdapter(List<File> updatedList) {
        videoMediaList = updatedList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (videoMediaList != null) {
            return videoMediaList.size();
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
