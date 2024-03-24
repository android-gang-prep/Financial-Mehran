package com.example.mehranm4.ui.budget;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.mehranm4.database.AppDatabase;
import com.example.mehranm4.database.entity.BudgetEntity;
import com.example.mehranm4.database.entity.DataEntity;
import com.example.mehranm4.models.BudgetModel;
import com.example.mehranm4.models.CategoryModel;
import com.example.mehranm4.models.DataModel;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BudgetViewModel extends AndroidViewModel {

    private AppDatabase appDatabase;

    private LiveData<List<BudgetModel>> budget;

    public BudgetViewModel(@NonNull Application application) {
        super(application);
        appDatabase = AppDatabase.getAppDatabase(application);
        budget = appDatabase.budgetDao().getBudgets();
    }

    private ExecutorService executorService = Executors.newSingleThreadExecutor();

    public void addData(BudgetEntity entity, boolean update) {
        executorService.execute(() -> {
            BudgetModel budgetModel = appDatabase.budgetDao().getBudgetByCategory(entity.getCategory_id());
            if (update || budgetModel != null) {
                if (budgetModel != null)
                    entity.setId(budgetModel.budget.getId());
                appDatabase.budgetDao().update(entity);

            } else
                appDatabase.budgetDao().insert(entity);
        });

    }

    public void deleteData(BudgetEntity entity) {
        executorService.execute(() -> appDatabase.budgetDao().delete(entity));
    }


    public LiveData<List<BudgetModel>> getBudget() {
        return budget;
    }
}
