package com.example.mehranm4.ui.category;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.mehranm4.OnClickCustom;
import com.example.mehranm4.database.entity.CategoryEntity;
import com.example.mehranm4.databinding.CategoryFragmentBinding;
import com.example.mehranm4.databinding.DialogEdittextBinding;
import com.example.mehranm4.ui.home.HomeFragment;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CategoryFragment extends BottomSheetDialogFragment {

    CategoryFragmentBinding binding;
    CategoryViewModel viewModel;
    OnClickCustom<CategoryEntity> categoryEntityOnClickCustom;

    public CategoryFragment(OnClickCustom<CategoryEntity> categoryEntityOnClickCustom) {
        this.categoryEntityOnClickCustom = categoryEntityOnClickCustom;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(this).get(CategoryViewModel.class);
        binding = CategoryFragmentBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        List<CategoryEntity> list = new ArrayList<>();
        CategoryAdapter adapter = new CategoryAdapter(list, category -> {
            if (category.getColor().equals("#009688")) {
                addCategory();
                return;
            }
            categoryEntityOnClickCustom.onClick(category);
            dismiss();
        });
        binding.rec.setAdapter(adapter);
        binding.rec.setLayoutManager(new LinearLayoutManager(getContext()));
        viewModel.init(getArguments().getBoolean("deposit"));
        viewModel.getCategory().observe(getViewLifecycleOwner(), categoryEntities -> {
            list.clear();
            list.addAll(categoryEntities);
            list.add(new CategoryEntity("Add Category", "#009688", getArguments().getBoolean("deposit")));
            adapter.notifyDataSetChanged();
        });

    }

    private void addCategory() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        DialogEdittextBinding binding = DialogEdittextBinding.inflate(getLayoutInflater());
        builder.setView(binding.getRoot());
        builder.setNegativeButton("Close", null);
        builder.setPositiveButton("Create", (dialog, which) -> {
            if (binding.name.getText().toString().trim().isEmpty()) {
                Toast.makeText(getContext(), "Fill the category name", Toast.LENGTH_SHORT).show();
                return;
            }
            Random rnd = new Random();
            viewModel.addCategory(new CategoryEntity(binding.name.getText().toString().trim(), "#" + Integer.toHexString(Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256))).substring(2), getArguments().getBoolean("deposit")));
        });
        builder.show();

    }
}
