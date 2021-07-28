package com.example.keyboardvalut.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;


import com.example.keyboardvalut.R;
import com.example.keyboardvalut.databinding.DrawerMenuItemBinding;
import com.example.keyboardvalut.interfaces.DrawerMenuClickListener;
import com.example.keyboardvalut.models.DrawerMenuModel;

import java.util.List;

public class DrawerMenuAdapter extends RecyclerView.Adapter<DrawerMenuAdapter.MyViewHolder> {

    List<DrawerMenuModel> drawerDataList;
    private Context context;


    DrawerMenuClickListener listener;

    public DrawerMenuAdapter(Context context, List<DrawerMenuModel> drawerDataList) {
        this.drawerDataList = drawerDataList;
        this.context = context;
        listener = (DrawerMenuClickListener) context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        DrawerMenuItemBinding binding =
                DataBindingUtil.inflate(LayoutInflater.from(viewGroup.getContext()),
                        R.layout.drawer_menu_item, viewGroup, false);
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder viewHolder, int position) {


        viewHolder.binding.tvMenuTitle.setText(drawerDataList.get(position).getTitle());
        viewHolder.binding.ivDrawerIcon.setImageResource(drawerDataList.get(position).getResourceId());

        viewHolder.binding.drawerMenuLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onDrawerMenuClickListener(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (drawerDataList != null) {
            return drawerDataList.size();
        } else {
            return 0;
        }
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        private final DrawerMenuItemBinding binding;

        public MyViewHolder(@NonNull DrawerMenuItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;


        }
    }
}
