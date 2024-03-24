package com.example.mehranm4.ui.home;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.graphics.Paint;
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
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.mehranm4.PopUpOnSelect;
import com.example.mehranm4.R;
import com.example.mehranm4.database.entity.CategoryEntity;
import com.example.mehranm4.database.entity.DataEntity;
import com.example.mehranm4.databinding.CategoryCirBinding;
import com.example.mehranm4.databinding.DialogAddItemBinding;
import com.example.mehranm4.databinding.HomeFragmentBinding;
import com.example.mehranm4.models.CategoryModel;
import com.example.mehranm4.models.DataModel;
import com.example.mehranm4.ui.category.CategoryFragment;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class HomeFragment extends Fragment {

    HomeFragmentBinding binding;
    HomeViewModel viewModel;
    List<CategoryModel> costs;
    List<CategoryModel> incomes;
    List<DataModel> costsData;
    List<DataModel> incomesData;
    DecimalFormat decimalFormat = new DecimalFormat("#,###");

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        binding = HomeFragmentBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.add.setOnClickListener(v -> addItem(null));
        costs = new ArrayList<>();
        incomes = new ArrayList<>();
        costsData = new ArrayList<>();
        incomesData = new ArrayList<>();
        viewModel.getCategory().observe(getViewLifecycleOwner(), categoryModels -> {
            long cost = 0;
            long income = 0;
            costs.clear();
            incomes.clear();
            for (CategoryModel categoryModel : categoryModels) {

                if (categoryModel.getTotal() > 0) {
                    if (categoryModel.category.isDeposit()) {
                        incomes.add(categoryModel);
                    } else costs.add(categoryModel);
                }

                for (DataEntity data : categoryModel.data) {
                    if (data.isDeposit()) {
                        income += data.getCost();
                    } else {
                        cost += data.getCost();
                    }
                }
            }
            binding.incomes.setText(decimalFormat.format(income));
            binding.costs.setText(decimalFormat.format(cost));

            if (binding.incomesB.isChecked()) {

                binding.circleChatView.addData(incomes);
                categoryChart(incomes);

            } else {
                binding.circleChatView.addData(costs);
                categoryChart(costs);

            }
        });
        viewModel.initData();
        viewModel.getData().observe(getViewLifecycleOwner(), dataModels -> {
            costsData.clear();
            incomesData.clear();

            for (DataModel data : dataModels) {
                if (data.data.isDeposit()) incomesData.add(data);
                else costsData.add(data);
            }

            if (binding.incomesB.isChecked()) setData(incomesData);
            else setData(costsData);

        });
        binding.radioG.setOnCheckedChangeListener((group, checkedId) -> {
            if (binding.incomesB.isChecked()) {
                setData(incomesData);
                categoryChart(incomes);
                binding.circleChatView.addData(incomes);
            } else {
                setData(costsData);
                binding.circleChatView.addData(costs);
                categoryChart(costs);
            }


        });

        viewModel.getBalance().observe(getViewLifecycleOwner(), aLong -> {
            if (aLong!=null)
            binding.balance.setText(String.format("Balance: %s", decimalFormat.format(aLong)));
            else binding.balance.setText("Balance: 0");
        });
    }

    private void categoryChart(List<CategoryModel> categories) {
        long total = 0;
        for (int i = 0; i < categories.size(); i++) {
            total += categories.get(i).getTotal();
        }
        binding.category.removeAllViews();
        CategoryCirBinding bindingChart = null;
        int l = 0;
        for (int i = 0; i < categories.size(); i++) {
            if (i % 3 == 0) {
                bindingChart = CategoryCirBinding.inflate(getLayoutInflater());
                binding.category.addView(bindingChart.getRoot());
                l = 0;
            }
            if (l == 0) {
                bindingChart.li1.setVisibility(View.VISIBLE);
                bindingChart.icon1.setColorFilter(Color.parseColor(categories.get(i).category.getColor()));
                bindingChart.txt1.setText(categories.get(i).category.getName() + " " + (100 * categories.get(i).getTotal() / total) + "%");
            } else if (l == 1) {
                bindingChart.li2.setVisibility(View.VISIBLE);

                bindingChart.icon2.setColorFilter(Color.parseColor(categories.get(i).category.getColor()));
                bindingChart.txt2.setText(categories.get(i).category.getName() + " " + (100 * categories.get(i).getTotal() / total) + "%");
            } else if (l == 2) {
                bindingChart.li3.setVisibility(View.VISIBLE);

                bindingChart.icon3.setColorFilter(Color.parseColor(categories.get(i).category.getColor()));
                bindingChart.txt3.setText(categories.get(i).category.getName() + " " + (100 * categories.get(i).getTotal() / total) + "%");

            }
            l++;
        }
    }

    private void setData(List<DataModel> data) {
        AdapterData adapterData = new AdapterData(data, new PopUpOnSelect<DataModel>() {
            @Override
            public void onDelete(DataModel dataModel) {
                viewModel.deleteData(dataModel.data);
            }

            @Override
            public void onEdit(DataModel dataModel) {
                addItem(dataModel);
            }
        });
        binding.rec.setAdapter(adapterData);
        binding.rec.setLayoutManager(new LinearLayoutManager(getContext()));
    }


    DialogAddItemBinding dialogBinding;
    long category_id = 0;
    long time;


    private void addItem(DataModel dataModel) {
        category_id = 0;
        time = System.currentTimeMillis();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        dialogBinding = DialogAddItemBinding.inflate(getLayoutInflater());

        if (dataModel != null) {
            time = dataModel.data.getTime();
            category_id = dataModel.data.getCategory_id();
            dialogBinding.cost.setText(dataModel.getCost());
            dialogBinding.description.setText(dataModel.data.getDescription());
            dialogBinding.icon.setColorFilter(Color.parseColor(dataModel.category.category.getColor()));
            dialogBinding.txt.setText(dataModel.category.category.getName());
            if (dataModel.data.isDeposit()) {
                dialogBinding.incomesB.setChecked(true);
                dialogBinding.costsB.setChecked(false);
            } else {
                dialogBinding.incomesB.setChecked(false);
                dialogBinding.costsB.setChecked(true);

            }
        }

        dialogBinding.date.setText(new SimpleDateFormat("yyyy/MM/dd").format(new Date(time)));
        builder.setView(dialogBinding.getRoot());
        builder.setPositiveButton("Add", (dialog, which) -> {
            if (dialogBinding.cost.getText().toString().trim().replace(",", "").isEmpty() || Long.parseLong(dialogBinding.cost.getText().toString().trim().replace(",", "")) < 1) {
                Toast.makeText(getContext(), "Fill the cost", Toast.LENGTH_SHORT).show();
                return;
            }
            if (category_id == 0) {
                Toast.makeText(getContext(), "Select Category", Toast.LENGTH_SHORT).show();
                return;
            }

            String dsc = "";
            if (!dialogBinding.description.getText().toString().trim().isEmpty())
                dsc = dialogBinding.description.getText().toString().trim();
            DataEntity dataEntity = new DataEntity(category_id, time, dsc, dialogBinding.incomesB.isChecked(), Long.parseLong(dialogBinding.cost.getText().toString().trim().replace(",", "")));
            if (dataModel != null)
                dataEntity.setId(dataModel.data.getId());
            viewModel.addData(dataEntity, dataModel != null);

        });
        Calendar d = new GregorianCalendar();
        dialogBinding.selectDate.setOnClickListener(v -> new DatePickerDialog(getContext(), (view, year, month, dayOfMonth) -> {
            Calendar calendar = new GregorianCalendar(year, month, dayOfMonth);
            time = calendar.getTimeInMillis();
            dialogBinding.date.setText(new SimpleDateFormat("yyyy/MM/dd").format(new Date(time)));
        }, d.get(Calendar.YEAR), d.get(Calendar.MONTH), d.get(Calendar.DAY_OF_MONTH)).show());

        dialogBinding.selectCat.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putBoolean("deposit", dialogBinding.incomesB.isChecked());
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

        dialogBinding.cost.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().trim().isEmpty())
                    return;
                dialogBinding.cost.removeTextChangedListener(this);
                String replace = s.toString().trim().replace(",", "");
                dialogBinding.cost.setText(decimalFormat.format(Long.parseLong(replace)));
                dialogBinding.cost.addTextChangedListener(this);
                dialogBinding.cost.setSelection(decimalFormat.format(Long.parseLong(replace)).length());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        builder.setNegativeButton("Close", null);
        builder.show();

    }


}
