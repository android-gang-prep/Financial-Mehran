package com.example.mehranm4.models;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.example.mehranm4.database.entity.BudgetEntity;
import com.example.mehranm4.database.entity.CategoryEntity;
import com.example.mehranm4.database.entity.DataEntity;

import java.text.DecimalFormat;
import java.util.List;

public class BudgetModel {
    @Embedded
    public BudgetEntity budget;


    @Relation(
            parentColumn = "category_id",
            entityColumn = "category_id",
            entity = DataEntity.class
    )
    public List<DataModel> data;


    @Relation(
            parentColumn = "category_id",
            entityColumn = "id",
            entity = CategoryEntity.class
    )
    public CategoryEntity category;

    public long getTotalCost() {
        if (data == null || data.size() < 1)
            return 0;
        long total = 0;
        for (int i = 0; i < data.size(); i++) {
            total += data.get(i).data.getCost();
        }
        return total;
    }

    public long getRemainder() {
        return budget.getBudget() - getTotalCost();
    }

    public String getTotalCostFormat() {
        return new DecimalFormat("#,###").format(getTotalCost());
    }

    public String getBudget() {
        return new DecimalFormat("#,###").format(budget.getBudget());
    }

    public String getBudgetFull() {
        return getTotalCostFormat() + "/" + getBudget();
    }

    public int getPercentFull() {
        return (int) (getTotalCost() * 100 / budget.getBudget());
    }
}
