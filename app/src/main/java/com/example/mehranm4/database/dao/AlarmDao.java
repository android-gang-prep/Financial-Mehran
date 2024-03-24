package com.example.mehranm4.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.mehranm4.database.entity.AlarmEntity;
import com.example.mehranm4.database.entity.BudgetEntity;
import com.example.mehranm4.models.BudgetModel;

import java.util.List;

@Dao
public interface AlarmDao {

    @Insert
    long insert(AlarmEntity data);


    @Update
    int update(AlarmEntity data);

    @Delete
    void delete(AlarmEntity... data);


    @Query("SELECT * FROM alarms where id=:id")
    AlarmEntity getAlarm(long id);

    @Query("SELECT * FROM alarms ORDER BY time DESC")
    LiveData<List<AlarmEntity>> getAlarms();
}
