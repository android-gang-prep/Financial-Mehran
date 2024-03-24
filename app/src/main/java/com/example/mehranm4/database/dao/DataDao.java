package com.example.mehranm4.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.example.mehranm4.database.entity.DataEntity;
import com.example.mehranm4.models.DataModel;

import java.util.List;

@Dao
public interface DataDao {

    @Insert
    long[] insert(DataEntity... data);


    @Update
    int update(DataEntity... data);

    @Delete
    void delete(DataEntity... data);

    @Query("SELECT (b.totalIncome - a.totalCost) FROM(SELECT SUM(cost) as totalCost FROM data WHERE deposit=0) a INNER JOIN(SELECT SUM(cost) as totalIncome FROM data WHERE deposit=1) b")
    long getBalance();

    @Query("SELECT (b.totalIncome - a.totalCost) FROM(SELECT SUM(cost) as totalCost FROM data WHERE deposit=0) a INNER JOIN(SELECT SUM(cost) as totalIncome FROM data WHERE deposit=1) b")
    LiveData<Long> getBalanceLive();

    @Transaction
    @Query("SELECT * FROM data ORDER BY time DESC")
    LiveData<List<DataModel>> getData();
}
