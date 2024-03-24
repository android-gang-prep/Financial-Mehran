package com.example.mehranm4;


import static com.google.common.truth.Truth.assertThat;

import android.os.Handler;
import android.os.Looper;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner;

import com.example.mehranm4.database.AppDatabase;
import com.example.mehranm4.database.dao.BudgetDao;
import com.example.mehranm4.database.dao.CategoryDao;
import com.example.mehranm4.database.dao.DataDao;
import com.example.mehranm4.database.entity.BudgetEntity;
import com.example.mehranm4.database.entity.CategoryEntity;
import com.example.mehranm4.database.entity.DataEntity;
import com.example.mehranm4.models.BudgetModel;
import com.example.mehranm4.models.DataModel;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;
import java.util.concurrent.CountDownLatch;


@RunWith(AndroidJUnit4ClassRunner.class)
public class DBTest {

    private DataDao dataDao;
    private CategoryDao categoryDao;
    private BudgetDao budgetDao;
    private AppDatabase db;

    @Before
    public void createDB() {
        db = Room.inMemoryDatabaseBuilder(ApplicationProvider.getApplicationContext(), AppDatabase.class).allowMainThreadQueries().build();
        dataDao = db.dataDao();
        categoryDao = db.categoryDao();
        budgetDao = db.budgetDao();
    }

    @Test
    public void addCategory() {
        CategoryEntity category = new CategoryEntity("TEST", "#FFFFFF", true);
        long id = categoryDao.insert(category)[0];
        category.setId(id);

        List<CategoryEntity> categorise = categoryDao.getCategories();
        assertThat(categorise.size()).isEqualTo(1);

    }

    @Test
    public void deleteCategory() {
        CategoryEntity category = new CategoryEntity("TEST", "#FFFFFF", true);
        long id = categoryDao.insert(category)[0];
        category.setId(id);

        List<CategoryEntity> categorise = categoryDao.getCategories();
        assertThat(categorise.size()).isEqualTo(1);

        categoryDao.delete(category);

        categorise = categoryDao.getCategories();
        assertThat(categorise.size()).isEqualTo(0);


    }

    @Test
    public void updateCategory() {
        CategoryEntity category = new CategoryEntity("TEST", "#FFFFFF", true);
        long id = categoryDao.insert(category)[0];
        category.setId(id);

        CategoryEntity updateCategory = new CategoryEntity("TEST Updated", "#FFFFFF", true);
        updateCategory.setId(id);

        categoryDao.update(updateCategory);

        CategoryEntity result = categoryDao.getCategory(id);

        assertThat(result.getName()).isEqualTo(updateCategory.getName());

    }


    @Test
    public void addData() throws InterruptedException {
        CategoryEntity category = new CategoryEntity("TEST", "#FFFFFF", true);
        long category_id = categoryDao.insert(category)[0];
        category.setId(category_id);


        DataEntity data = new DataEntity(category_id, System.currentTimeMillis(), "Testing", true, 10000);

        long id = dataDao.insert(data)[0];
        data.setId(id);
        CountDownLatch signal = new CountDownLatch(1);
        LiveData<List<DataModel>> liveData = dataDao.getData();
        Observer<List<DataModel>> observer = dataModels -> {
            assertThat(dataModels.size()).isEqualTo(1);
            assertThat(category_id).isEqualTo(dataModels.get(0).category.category.getId());
            assertThat(id).isEqualTo(dataModels.get(0).category.data.get(0).getId());

            signal.countDown();

        };
        new Handler(Looper.getMainLooper()).post(() -> liveData.observeForever(observer));


        signal.await();
        new Handler(Looper.getMainLooper()).post(() -> liveData.removeObserver(observer));
    }

    @Test
    public void deleteData() throws InterruptedException {
        CategoryEntity category = new CategoryEntity("TEST", "#FFFFFF", true);
        long category_id = categoryDao.insert(category)[0];
        category.setId(category_id);


        DataEntity data = new DataEntity(category_id, System.currentTimeMillis(), "Testing", true, 10000);
        long id = dataDao.insert(data)[0];
        data.setId(id);


        dataDao.delete(data);

        LiveData<List<DataModel>> liveData = dataDao.getData();


        CountDownLatch signal = new CountDownLatch(1);
        Observer<List<DataModel>> observerRemove = dataModels -> {
            assertThat(dataModels.size()).isEqualTo(0);
            signal.countDown();
        };
        new Handler(Looper.getMainLooper()).post(() -> liveData.observeForever(observerRemove));
        signal.await();
        new Handler(Looper.getMainLooper()).post(() -> liveData.removeObserver(observerRemove));


    }

    @Test
    public void updateData() throws InterruptedException {
        CategoryEntity category = new CategoryEntity("TEST", "#FFFFFF", true);
        long category_id = categoryDao.insert(category)[0];
        category.setId(category_id);


        DataEntity data = new DataEntity(category_id, System.currentTimeMillis(), "Testing", true, 10000);
        long id = dataDao.insert(data)[0];
        data.setId(id);

        DataEntity dataUpdate = new DataEntity(category_id, System.currentTimeMillis(), "Testing", true, 10001);
        dataUpdate.setId(id);
        dataDao.update(dataUpdate);

        LiveData<List<DataModel>> liveData = dataDao.getData();


        CountDownLatch signal = new CountDownLatch(1);
        Observer<List<DataModel>> observerRemove = dataModels -> {
            assertThat(dataModels.get(0).data.getCost()).isEqualTo(dataUpdate.getCost());
            signal.countDown();
        };
        new Handler(Looper.getMainLooper()).post(() -> liveData.observeForever(observerRemove));
        signal.await();
        new Handler(Looper.getMainLooper()).post(() -> liveData.removeObserver(observerRemove));

    }

    @Test
    public void testData(){
        CategoryEntity daramadCategory = new CategoryEntity("Daramad", "#FFFFFF", true);
        long daramad_category_id = categoryDao.insert(daramadCategory)[0];
        daramadCategory.setId(daramad_category_id);


        DataEntity daramadData = new DataEntity(daramad_category_id, System.currentTimeMillis(), "Daramad", true, 9000000);
        daramadData.setId(dataDao.insert(daramadData)[0]);


        CategoryEntity hazineCategory = new CategoryEntity("Hazine", "#FFFFFF", false);
        long hazine_category_id = categoryDao.insert(hazineCategory)[0];
        hazineCategory.setId(hazine_category_id);

        BudgetEntity budget = new BudgetEntity(hazine_category_id, 700000);
        budget.setId(budgetDao.insert(budget)[0]);

        DataEntity hazineData = new DataEntity(hazine_category_id, System.currentTimeMillis(), "Hazine", false, 100000);
        hazineData.setId(dataDao.insert(hazineData)[0]);


        BudgetModel budgetModel = budgetDao.getBudget(budget.getId());

        assertThat(budgetModel.getRemainder()).isEqualTo(budget.getBudget() - hazineData.getCost());

        long balance = dataDao.getBalance();

        assertThat(balance).isEqualTo(daramadData.getCost() - hazineData.getCost());



    }


    @After
    public void closeDB() {
        db.close();
    }
}
