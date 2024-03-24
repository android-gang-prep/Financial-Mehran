package com.example.mehranm4.ui.budget;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.PopupMenu;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mehranm4.PopUpOnSelect;
import com.example.mehranm4.databinding.ItemBudgetBinding;
import com.example.mehranm4.databinding.ItemDataBinding;
import com.example.mehranm4.models.BudgetModel;

import java.util.List;

public class AdapterBudget extends RecyclerView.Adapter<AdapterBudget.ViewHolder> {
    private List<BudgetModel> list;
    private PopUpOnSelect<BudgetModel> popUpOnSelect;


    public AdapterBudget(List<BudgetModel> list, PopUpOnSelect<BudgetModel> popUpOnSelect) {
        this.list = list;
        this.popUpOnSelect = popUpOnSelect;
    }

    @NonNull
    @Override
    public AdapterBudget.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemBudgetBinding binding = ItemBudgetBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterBudget.ViewHolder holder, int position) {
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
        holder.binding.progress.setPercent(list.get(position).getPercentFull());
        holder.binding.percent.setText(list.get(position).getPercentFull()+"%");
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ItemBudgetBinding binding;

        public ViewHolder(@NonNull ItemBudgetBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
