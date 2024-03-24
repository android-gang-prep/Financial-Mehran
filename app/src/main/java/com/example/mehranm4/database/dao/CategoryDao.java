package com.example.mehranm4.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.example.mehranm4.database.entity.CategoryEntity;
import com.example.mehranm4.models.CategoryModel;

import java.util.List;

@Dao
public interface CategoryDao {
    @Insert
    long[] insert(CategoryEntity... category);


    @Query("SELECT COUNT(*) from category")
    int count();


    @Query("SELECT * from category where deposit=:deposit")
    LiveData<List<CategoryEntity>> getCategories(boolean deposit);

    @Query("SELECT * from category")
   List<CategoryEntity> getCategories();

    @Query("SELECT * from category where id=:id")
    CategoryEntity getCategory(long id);
    @Query("SELECT * from category where name=:name")
    CategoryEntity getCategory(String name);



    @Delete
    void delete(CategoryEntity category);

    @Update
    void update(CategoryEntity category);

    @Transaction
    @Query("SELECT * from category")
    LiveData<List<CategoryModel>> getCategoriesModel();
}
