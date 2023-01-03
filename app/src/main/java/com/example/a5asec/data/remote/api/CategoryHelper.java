package com.example.a5asec.data.remote.api;

import com.example.a5asec.data.model.api.Category;

import java.util.List;

import io.reactivex.rxjava3.core.Observable;

public class CategoryHelper
    {
    private CategoryApi mCategoryApi;

    public CategoryHelper(CategoryApi categoryApi)
        {
        this.mCategoryApi = categoryApi;
        }

    public Observable<List<Category>> getCategory()
        {
        return this.mCategoryApi.getCategory();
        }

    public Observable<Category.ItemsEntity> getItemCategory(long id)
        {
        return mCategoryApi.getItemCategory(id);
        }
    }