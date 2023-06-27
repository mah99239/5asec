package com.example.a5asec.data.local.db.dao;


import com.example.a5asec.data.model.db.CategoryEntity;
import com.example.a5asec.data.model.db.CategoryWithItems;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;

public interface CategoryDataSource {
    Single<List<CategoryWithItems>> getCategoriesWithItemsAndServices();


    Completable insertItems(List<CategoryEntity.ItemsEntity> categories);

    Completable insertItemService(List<CategoryEntity.ItemServicesEntity> categories);

    Completable insertLaundryService(List<CategoryEntity.LaundryServicesEntity> categories);

    Completable insertCategoriesWithItemsAndServices(List<CategoryEntity> categoriesWithItems);

}
