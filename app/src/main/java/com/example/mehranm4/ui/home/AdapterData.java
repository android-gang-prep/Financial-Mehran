package com.example.mehranm4.ui.home;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mehranm4.PopUpOnSelect;
import com.example.mehranm4.databinding.ItemDataBinding;
import com.example.mehranm4.models.DataModel;

import java.util.List;

public class AdapterData extends RecyclerView.Adapter<AdapterData.ViewHolder> {
    private List<DataModel> list;
    private PopUpOnSelect<DataModel> popUpOnSelect;



    public AdapterData(List<DataModel> list, PopUpOnSelect<DataModel> popUpOnSelect) {
        this.list = list;
        this.popUpOnSelect = popUpOnSelect;
    }

    @NonNull
    @Override
    public AdapterData.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemDataBinding binding = ItemDataBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterData.ViewHolder holder, int position) {
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
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ItemDataBinding binding;

        public ViewHolder(@NonNull ItemDataBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
