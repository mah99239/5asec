package com.example.a5asec.data.remote.api;

import com.example.a5asec.data.model.api.Category;

import java.util.List;

import io.reactivex.rxjava3.core.Observable;

public class CategoryClient extends Client implements CategoryApi
    {
    private static CategoryClient INSTANCE;
    private final CategoryApi mCategoryApi;
    private final CategoryApi mItemCategoryTokenApi;

    public CategoryClient()
        {

        mCategoryApi = getRetrofitAdapter().create(CategoryApi.class);
        mItemCategoryTokenApi = getRetrofit().create(CategoryApi.class);
        }

    public static CategoryClient getINSTANCE()
        {
        return null == INSTANCE ? new CategoryClient() : INSTANCE;
        }

    @Override
    public Observable<List<Category>> getCategory()
        {
        return mCategoryApi.getCategory();
        }

    @Override
    public Observable<Category.ItemsEntity> getItemCategory(long id)
        {
        return mItemCategoryTokenApi.getItemCategory(id);
        }
    }
