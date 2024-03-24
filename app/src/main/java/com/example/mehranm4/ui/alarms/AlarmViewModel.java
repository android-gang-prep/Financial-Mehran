package com.example.mehranm4.ui.alarms;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.mehranm4.database.AppDatabase;
import com.example.mehranm4.database.entity.AlarmEntity;
import com.example.mehranm4.database.entity.BudgetEntity;
import com.example.mehranm4.database.entity.DataEntity;
import com.example.mehranm4.models.BudgetModel;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class AlarmViewModel extends AndroidViewModel {

    private AppDatabase appDatabase;

    private LiveData<List<AlarmEntity>> alarms;

    public AlarmViewModel(@NonNull Application application) {
        super(application);
        appDatabase = AppDatabase.getAppDatabase(application);
        alarms = appDatabase.alarmDao().getAlarms();
    }

    private ExecutorService executorService = Executors.newSingleThreadExecutor();

    public long addData(AlarmEntity entity, boolean update) throws ExecutionException, InterruptedException {
        Callable<Long> callable;

        if (update)
            callable = () -> (long) appDatabase.alarmDao().update(entity);
        else
            callable = () -> appDatabase.alarmDao().insert(entity);

        Future<Long> longFuture = executorService.submit(callable);

        return longFuture.get();
    }
    public void addData(DataEntity entity, boolean update) {
        if (update)
            executorService.execute(() -> appDatabase.dataDao().update(entity));
        else
            executorService.execute(() -> appDatabase.dataDao().insert(entity));
    }
    public void deleteData(AlarmEntity entity) {
        executorService.execute(() -> appDatabase.alarmDao().delete(entity));
    }

    public LiveData<List<AlarmEntity>> getAlarms() {
        return alarms;
    }
}
