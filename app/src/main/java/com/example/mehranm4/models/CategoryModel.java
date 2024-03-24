package com.example.mehranm4.models;


import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.Relation;

import com.example.mehranm4.database.entity.CategoryEntity;
import com.example.mehranm4.database.entity.DataEntity;

import java.util.List;

public class CategoryModel {
    @Embedded
    public CategoryEntity category;

    @Relation(parentColumn = "id",
            entityColumn = "category_id")
    public List<DataEntity> data;


    public long getTotal() {
        long total = 0;
        for (int i = 0; i < data.size(); i++) {
            total += data.get(i).getCost();
        }
        return total;
    }



}
