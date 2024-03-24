package com.example.mehranm4.ui.alarms;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.PopupMenu;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mehranm4.OnClickCustom;
import com.example.mehranm4.PopUpOnSelect;
import com.example.mehranm4.databinding.ItemAlarmBinding;
import com.example.mehranm4.databinding.ItemBudgetBinding;
import com.example.mehranm4.database.entity.AlarmEntity;

import java.util.List;

public class AdapterAlarm extends RecyclerView.Adapter<AdapterAlarm.ViewHolder> {
    private List<AlarmEntity> list;
    private PopUpOnSelect<AlarmEntity> popUpOnSelect;
    private OnClickCustom<AlarmEntity> alarmEntityOnClickCustom;

    public AdapterAlarm(List<AlarmEntity> list, PopUpOnSelect<AlarmEntity> popUpOnSelect, OnClickCustom<AlarmEntity> alarmEntityOnClickCustom) {
        this.list = list;
        this.popUpOnSelect = popUpOnSelect;
        this.alarmEntityOnClickCustom = alarmEntityOnClickCustom;
    }

    @NonNull
    @Override
    public AdapterAlarm.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemAlarmBinding binding = ItemAlarmBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterAlarm.ViewHolder holder, int position) {
        holder.binding.setModel(list.get(position));
        holder.binding.more.setOnClickListener(v -> {
            PopupMenu popupMenu = new PopupMenu(v.getContext(), v);
            popupMenu.getMenu().add("Edit");
            popupMenu.getMenu().add("Delete");
            popupMenu.show();
            popupMenu.setOnMenuItemClickListener(item -> {
                if (item.getTitle().equals("Edit")) {
                    popUpOnSelect.onEdit(list.get(position));
                } else if (item.getTitle().equals("Delete")) {
                    popUpOnSelect.onDelete(list.get(position));
                }
                return true;
            });
        });
        holder.binding.pay.setOnClickListener(v -> alarmEntityOnClickCustom.onClick(list.get(position)));
        holder.binding.progress.setPercent(list.get(position).getPercent());
        holder.binding.percent.setText(list.get(position).getPercent()+"%");
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ItemAlarmBinding binding;

        public ViewHolder(@NonNull ItemAlarmBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
