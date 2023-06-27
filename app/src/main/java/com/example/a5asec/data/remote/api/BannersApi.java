package com.example.a5asec.data.remote.api;

import com.example.a5asec.data.model.api.Banner;

import java.util.List;

import javax.inject.Singleton;

import io.reactivex.rxjava3.core.Single;
import retrofit2.http.GET;

@Singleton
public interface BannersApi
{
    @GET("api/banners")
    Single<List<Banner>> getBanners();
}
