package com.example.mehranm4.ui.alarms;

import static android.app.PendingIntent.FLAG_MUTABLE;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
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
import com.example.mehranm4.database.entity.AlarmEntity;
import com.example.mehranm4.database.entity.BudgetEntity;
import com.example.mehranm4.database.entity.DataEntity;
import com.example.mehranm4.databinding.BudgetFragmentBinding;
import com.example.mehranm4.databinding.DialogAddAlarmBinding;
import com.example.mehranm4.databinding.DialogAddBudgetBinding;
import com.example.mehranm4.ui.budget.AdapterBudget;
import com.example.mehranm4.ui.budget.BudgetViewModel;
import com.example.mehranm4.ui.category.CategoryFragment;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class AlarmFragment extends Fragment {

    BudgetFragmentBinding binding;
    AlarmViewModel viewModel;
    List<AlarmEntity> list;

    DecimalFormat decimalFormat = new DecimalFormat("#,###");


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(this).get(AlarmViewModel.class);
        binding = BudgetFragmentBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.add.setOnClickListener(v -> addItem(null));
        list = new ArrayList<>();
        AdapterAlarm adapterAlarm = new AdapterAlarm(list, new PopUpOnSelect<AlarmEntity>() {
            @Override
            public void onDelete(AlarmEntity model) {
                viewModel.deleteData(model);
            }

            @Override
            public void onEdit(AlarmEntity model) {
                addItem(model);
            }
        }, alarmEntity -> {
            viewModel.addData(new DataEntity(6,System.currentTimeMillis(),alarmEntity.getTitle(),false,alarmEntity.getCost()),false);
            AlarmEntity alarmEntity1 = alarmEntity;
            alarmEntity1.setPay(true);
            try {
                viewModel.addData(alarmEntity1, true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        binding.rec.setAdapter(adapterAlarm);
        binding.rec.setLayoutManager(new LinearLayoutManager(getContext()));
        viewModel.getAlarms().observe(getViewLifecycleOwner(), dataModels -> {
            list.clear();
            list.addAll(dataModels);
            adapterAlarm.notifyDataSetChanged();
        });

    }


    DialogAddAlarmBinding dialogBinding;


    private void addItem(AlarmEntity budgetModel) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        dialogBinding = DialogAddAlarmBinding.inflate(getLayoutInflater());
        Calendar c = new GregorianCalendar();

        if (budgetModel != null) {
            dialogBinding.cost.setText(budgetModel.getCostFormat());
            dialogBinding.title.setText(budgetModel.getTitle());
            c.setTimeInMillis(budgetModel.getTime());
            dialogBinding.day.setText(String.valueOf(c.get(Calendar.DAY_OF_MONTH)));
        }

        builder.setView(dialogBinding.getRoot());
        builder.setPositiveButton("Add", (dialog, which) -> {
            if (dialogBinding.cost.getText().toString().trim().replace(",", "").isEmpty() || Long.parseLong(dialogBinding.cost.getText().toString().trim().replace(",", "")) < 1) {
                Toast.makeText(getContext(), "Fill the budget", Toast.LENGTH_SHORT).show();
                return;
            }

            if (dialogBinding.day.getText().toString().trim().isEmpty() || (Integer.parseInt(dialogBinding.day.getText().toString().trim()) < 1 && Integer.parseInt(dialogBinding.day.getText().toString().trim()) > 31)) {
                Toast.makeText(getContext(), "Fill the day", Toast.LENGTH_SHORT).show();
                return;
            }

            if (dialogBinding.title.getText().toString().trim().isEmpty()) {
                Toast.makeText(getContext(), "Fill the title", Toast.LENGTH_SHORT).show();
                return;
            }

            Calendar calendar = new GregorianCalendar();

            if (calendar.get(Calendar.DAY_OF_MONTH) == Integer.parseInt(dialogBinding.day.getText().toString().trim())) {
                calendar.add(Calendar.MINUTE, 5);
            }else if (calendar.get(Calendar.DAY_OF_MONTH) > Integer.parseInt(dialogBinding.day.getText().toString().trim())) {
                calendar.add(Calendar.MONTH, 1);
            }

            calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(dialogBinding.day.getText().toString().trim()));

            long id = 0;

            AlarmEntity alarmEntity = new AlarmEntity(dialogBinding.title.getText().toString().trim(), calendar.getTimeInMillis(), Long.parseLong(dialogBinding.cost.getText().toString().trim().replace(",", "")), true);
            try {
                if (budgetModel != null) {
                    alarmEntity.setId(budgetModel.getId());
                    id = budgetModel.getId();
                    viewModel.addData(alarmEntity, true);
                } else {
                    id = viewModel.addData(alarmEntity, false);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            AlarmManager alarmMgr = (AlarmManager) getContext().getSystemService(Context.ALARM_SERVICE);
            Intent intent = new Intent(getContext(), AlarmReceiver.class);
            intent.putExtra("id", id);
            PendingIntent alarmIntent = PendingIntent.getBroadcast(getContext(), 0, intent, PendingIntent.FLAG_IMMUTABLE);

            alarmMgr.setExact(AlarmManager.RTC_WAKEUP,
                    calendar.getTimeInMillis(), alarmIntent);


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
