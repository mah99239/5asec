package com.example.a5asec.data.remote.api;

import com.example.a5asec.data.model.api.Category;

import java.util.List;

import javax.inject.Singleton;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;
import retrofit2.http.GET;
import retrofit2.http.Path;

@Singleton

public interface CategoryApi
{

    @GET("api/item-categories")
    Single<List<Category>> getCategory();

    @GET("api/items/{id}")
    Observable<Category.ItemsEntity> getItemCategory(@Path("id") long id);
}
