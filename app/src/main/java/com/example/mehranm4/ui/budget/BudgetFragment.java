package com.example.mehranm4.ui.budget;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.mehranm4.PopUpOnSelect;
import com.example.mehranm4.database.entity.BudgetEntity;
import com.example.mehranm4.database.entity.DataEntity;
import com.example.mehranm4.databinding.BudgetFragmentBinding;
import com.example.mehranm4.databinding.CategoryCirBinding;
import com.example.mehranm4.databinding.DialogAddBudgetBinding;
import com.example.mehranm4.databinding.DialogAddItemBinding;
import com.example.mehranm4.databinding.HomeFragmentBinding;
import com.example.mehranm4.models.BudgetModel;
import com.example.mehranm4.models.CategoryModel;
import com.example.mehranm4.models.DataModel;
import com.example.mehranm4.ui.category.CategoryFragment;
import com.example.mehranm4.ui.home.AdapterData;
import com.example.mehranm4.ui.home.HomeViewModel;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class BudgetFragment extends Fragment {

    BudgetFragmentBinding binding;
    BudgetViewModel viewModel;
    List<BudgetModel> list;

    DecimalFormat decimalFormat = new DecimalFormat("#,###");

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(this).get(BudgetViewModel.class);
        binding = BudgetFragmentBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.add.setOnClickListener(v -> addItem(null));
        list = new ArrayList<>();
        AdapterBudget adapterBudget = new AdapterBudget(list, new PopUpOnSelect<BudgetModel>() {
            @Override
            public void onDelete(BudgetModel model) {
                viewModel.deleteData(model.budget);
            }

            @Override
            public void onEdit(BudgetModel model) {
                addItem(model);
            }
        });
        binding.rec.setAdapter(adapterBudget);
        binding.rec.setLayoutManager(new LinearLayoutManager(getContext()));
        viewModel.getBudget().observe(getViewLifecycleOwner(), dataModels -> {
            list.clear();
            list.addAll(dataModels);
            adapterBudget.notifyDataSetChanged();

        });

    }


    DialogAddBudgetBinding dialogBinding;
    long category_id = 0;


    private void addItem(BudgetModel budgetModel) {
        category_id = 0;
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        dialogBinding = DialogAddBudgetBinding.inflate(getLayoutInflater());
        if (budgetModel != null) {
            category_id = budgetModel.budget.getCategory_id();
            dialogBinding.budget.setText(budgetModel.getBudget());
            dialogBinding.icon.setColorFilter(Color.parseColor(budgetModel.category.getColor()));
            dialogBinding.txt.setText(budgetModel.category.getName());
        }

        builder.setView(dialogBinding.getRoot());
        builder.setPositiveButton("Add", (dialog, which) -> {
            if (dialogBinding.budget.getText().toString().trim().replace(",", "").isEmpty() || Long.parseLong(dialogBinding.budget.getText().toString().trim().replace(",", "")) < 1) {
                Toast.makeText(getContext(), "Fill the budget", Toast.LENGTH_SHORT).show();
                return;
            }
            if (category_id == 0) {
                Toast.makeText(getContext(), "Select Category", Toast.LENGTH_SHORT).show();
                return;
            }


            BudgetEntity budgetEntity = new BudgetEntity(category_id, Long.parseLong(dialogBinding.budget.getText().toString().trim().replace(",", "")));
            if (budgetModel != null)
                budgetEntity.setId(budgetModel.budget.getId());
            viewModel.addData(budgetEntity, budgetModel != null);

        });

        dialogBinding.selectCat.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putBoolean("deposit", false);
            try {
                CategoryFragment categoryFragment = new CategoryFragment(category -> {
                    this.category_id = category.getId();
                    dialogBinding.icon.setColorFilter(Color.parseColor(category.getColor()));
                    dialogBinding.txt.setText(category.getName());
                });
                categoryFragment.setArguments(bundle);
                categoryFragment.show(getChildFragmentManager(), "CategoryFragment");
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        dialogBinding.budget.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().trim().isEmpty())
                    return;
                dialogBinding.budget.removeTextChangedListener(this);
                String replace = s.toString().trim().replace(",", "");
                dialogBinding.budget.setText(decimalFormat.format(Long.parseLong(replace)));
                dialogBinding.budget.addTextChangedListener(this);
                dialogBinding.budget.setSelection(decimalFormat.format(Long.parseLong(replace)).length());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        builder.setNegativeButton("Close", null);
        builder.show();

    }


}
