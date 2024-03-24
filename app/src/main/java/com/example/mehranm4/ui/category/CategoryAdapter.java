package com.example.mehranm4.ui.category;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mehranm4.OnClickCustom;
import com.example.mehranm4.database.entity.CategoryEntity;
import com.example.mehranm4.databinding.CategoryItemBinding;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {
    private List<CategoryEntity> list;
    private OnClickCustom<CategoryEntity> onClickCustom;

    public CategoryAdapter(List<CategoryEntity> list, OnClickCustom<CategoryEntity> onClickCustom) {
        this.list = list;
        this.onClickCustom = onClickCustom;
    }

    @NonNull
    @Override
    public CategoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CategoryItemBinding binding = CategoryItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryAdapter.ViewHolder holder, int position) {
        holder.binding.icon.setColorFilter(Color.parseColor(list.get(position).getColor()));
        holder.binding.txt.setText(list.get(position).getName());
        holder.binding.getRoot().setOnClickListener(v -> onClickCustom.onClick(list.get(position)));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CategoryItemBinding binding;

        public ViewHolder(@NonNull CategoryItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
