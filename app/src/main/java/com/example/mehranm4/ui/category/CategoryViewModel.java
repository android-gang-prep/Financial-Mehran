package com.example.mehranm4.ui.category;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.mehranm4.database.AppDatabase;
import com.example.mehranm4.database.entity.CategoryEntity;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CategoryViewModel extends AndroidViewModel {

    private AppDatabase appDatabase;
    private LiveData<List<CategoryEntity>> category;

    public CategoryViewModel(@NonNull Application application) {
        super(application);
        appDatabase = AppDatabase.getAppDatabase(application);
    }

    public void init(boolean deposit) {
        category = appDatabase.categoryDao().getCategories(deposit);
    }

    public LiveData<List<CategoryEntity>> getCategory() {
        return category;
    }

    private ExecutorService executorService = Executors.newSingleThreadExecutor();

    public void addCategory(CategoryEntity category) {
        executorService.execute(() -> {
            if (appDatabase.categoryDao().getCategory(category.getName()) == null)
                appDatabase.categoryDao().insert(category);
        });
    }
}
