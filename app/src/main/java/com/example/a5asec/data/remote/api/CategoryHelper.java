package com.example.a5asec.data.remote.api;

import com.example.a5asec.data.model.api.Category;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;

public class CategoryHelper
{
    private final CategoryApi mCategoryApi;

    @Inject

    public CategoryHelper(CategoryApi categoryApi)
    {
        this.mCategoryApi = categoryApi;
    }

    public Single<List<Category>> getCategory()
    {
        return this.mCategoryApi.getCategory();
    }

    public Observable<Category.ItemsEntity> getItemCategory(long id)
    {
        return mCategoryApi.getItemCategory(id);
    }
}