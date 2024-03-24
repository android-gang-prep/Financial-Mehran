package com.example.mehranm4.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.mehranm4.database.dao.AlarmDao;
import com.example.mehranm4.database.dao.BudgetDao;
import com.example.mehranm4.database.dao.CategoryDao;
import com.example.mehranm4.database.dao.DataDao;
import com.example.mehranm4.database.entity.AlarmEntity;
import com.example.mehranm4.database.entity.BudgetEntity;
import com.example.mehranm4.database.entity.CategoryEntity;
import com.example.mehranm4.database.entity.DataEntity;

@Database(entities = {DataEntity.class, CategoryEntity.class, BudgetEntity.class, AlarmEntity.class}, version = 3, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase appDatabase;

    public abstract CategoryDao categoryDao();

    public abstract BudgetDao budgetDao();

    public abstract DataDao dataDao();
    public abstract AlarmDao alarmDao();

    public static AppDatabase getAppDatabase(Context context) {
        if (appDatabase == null)
            appDatabase = Room.databaseBuilder(context, AppDatabase.class, "mehran4").allowMainThreadQueries().build();

        return appDatabase;
    }
}
