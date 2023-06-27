package com.example.a5asec.data.local.db.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import com.example.a5asec.data.model.db.CategoryEntity;
import com.example.a5asec.data.model.db.CategoryWithItems;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;

@Dao
public interface CategoryDao
{
    @Transaction
    @Query("SELECT * FROM categories")
    Single<List<CategoryWithItems>> getCategoriesWithItemsAndServices();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insertCategory(CategoryEntity categories);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insertItems(List<CategoryEntity.ItemsEntity> categories);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insertItemService(List<CategoryEntity.ItemServicesEntity> categories);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insertLaundryService(List<CategoryEntity.LaundryServicesEntity> categories);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    @Transaction
    Completable insertCategoriesWithItemsAndServices(List<CategoryEntity> categoriesWithItems);


}
