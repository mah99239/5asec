package com.example.a5asec.data.repository;

import com.example.a5asec.data.remote.api.CategoryHelper;
import com.example.a5asec.data.model.api.Category;

import java.util.List;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;

public class CategoryRepository
    {
    public static final String TAG = "CategoryRepository";
    private CategoryHelper mCategoryHelper;


    public CategoryRepository(CategoryHelper categoryHelper) {
    this.mCategoryHelper = categoryHelper;
    }
    @NonNull
    public Observable<List<Category>> getCategory() {
    return mCategoryHelper.getCategory();
    }

   @NonNull
    public Observable<Category.ItemsEntity> getItemCategory(long id) {
    return mCategoryHelper.getItemCategory(id);
    }
    }
