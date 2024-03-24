package com.example.mehranm4;

import android.app.Application;

import com.example.mehranm4.database.AppDatabase;
import com.example.mehranm4.database.entity.CategoryEntity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        File dexOutputDir = getCodeCacheDir();
        dexOutputDir.setReadOnly();

        if (AppDatabase.getAppDatabase(getApplicationContext()).categoryDao().count() == 0) {
            CategoryEntity[] categoryEntities = new CategoryEntity[6];
            categoryEntities[0] = new CategoryEntity("Salary", "#1E88E5", true);
            categoryEntities[1] = new CategoryEntity("Gift", "#8B5BE0", true);
            categoryEntities[2] = new CategoryEntity("Other", "#E4D6A1", true);

            categoryEntities[3] = new CategoryEntity("Food", "#FF5722", false);
            categoryEntities[4] = new CategoryEntity("Recreation", "#FFEB3B", false);
            categoryEntities[5] = new CategoryEntity("Other", "#E4D6A1", false);

            AppDatabase.getAppDatabase(getApplicationContext()).categoryDao().insert(categoryEntities);
        }
    }
}
