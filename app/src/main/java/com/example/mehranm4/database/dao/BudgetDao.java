package com.example.mehranm4.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.example.mehranm4.database.entity.BudgetEntity;
import com.example.mehranm4.database.entity.DataEntity;
import com.example.mehranm4.models.BudgetModel;
import com.example.mehranm4.models.DataModel;

import java.util.List;

@Dao
public interface BudgetDao {

    @Insert
    long[] insert(BudgetEntity... data);


    @Update
    int  update(BudgetEntity... data);

    @Delete
    void delete(BudgetEntity... data);

    @Query("SELECT * FROM budgets where category_id=:id")
    BudgetModel getBudgetByCategory(long id);
    @Query("SELECT * FROM budgets where id=:id")
    BudgetModel getBudget(long id);
    @Transaction
    @Query("SELECT * FROM budgets ORDER BY id DESC")
    LiveData<List<BudgetModel>> getBudgets();
}
