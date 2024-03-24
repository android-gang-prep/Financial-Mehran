package com.example.mehranm4.ui.home;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.mehranm4.database.AppDatabase;
import com.example.mehranm4.database.entity.DataEntity;
import com.example.mehranm4.models.CategoryModel;
import com.example.mehranm4.models.DataModel;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HomeViewModel extends AndroidViewModel {

    private AppDatabase appDatabase;

    private LiveData<List<CategoryModel>> category;
    private LiveData<List<DataModel>> data;
    private LiveData<Long> balance;

    public HomeViewModel(@NonNull Application application) {
        super(application);
        appDatabase = AppDatabase.getAppDatabase(application);
        category = appDatabase.categoryDao().getCategoriesModel();
        balance = appDatabase.dataDao().getBalanceLive();
    }

    private ExecutorService executorService = Executors.newSingleThreadExecutor();

    public void addData(DataEntity entity, boolean update) {
        if (update)
            executorService.execute(() -> appDatabase.dataDao().update(entity));
        else
            executorService.execute(() -> appDatabase.dataDao().insert(entity));
    }

    public void deleteData(DataEntity entity) {
        executorService.execute(() -> appDatabase.dataDao().delete(entity));
    }

    public void initData() {
        data = appDatabase.dataDao().getData();
    }

    public LiveData<List<CategoryModel>> getCategory() {
        return category;
    }

    public LiveData<List<DataModel>> getData() {
        return data;
    }

    public LiveData<Long> getBalance() {
        return balance;
    }
}
