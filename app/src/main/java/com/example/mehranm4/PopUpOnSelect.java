package com.example.mehranm4;

import com.example.mehranm4.models.DataModel;

public interface PopUpOnSelect<T> {
    void onDelete(T model);

    void onEdit(T model);
}