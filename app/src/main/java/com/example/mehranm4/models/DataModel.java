package com.example.mehranm4.models;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.example.mehranm4.database.entity.CategoryEntity;
import com.example.mehranm4.database.entity.DataEntity;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DataModel {

    @Embedded
    public DataEntity data;

    @Relation(
            parentColumn = "category_id",
            entityColumn = "id",
            entity = CategoryEntity.class
    )
    public CategoryModel category;


    public String getTime() {
        return new SimpleDateFormat("yyyy/MM/dd").format(new Date(data.getTime()));
    }


    public String getCost() {
        return new DecimalFormat("#,###").format(data.getCost());
    }

}
