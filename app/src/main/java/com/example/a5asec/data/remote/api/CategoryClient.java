package com.example.a5asec.data.remote.api;

import com.example.a5asec.data.model.api.Category;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;

public class CategoryClient implements CategoryApi
{
    private final CategoryApi mCategoryApi;

    @Inject
    public CategoryClient(CategoryApi categoryApi)
    {
        mCategoryApi = categoryApi;
    }


    @Override
    public Single<List<Category>> getCategory()
    {
        return mCategoryApi.getCategory();
    }

    @Override
    public Observable<Category.ItemsEntity> getItemCategory(long id)
    {
        return mCategoryApi.getItemCategory(id);
    }
}
