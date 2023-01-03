package com.example.a5asec.data.remote.api;

import com.example.a5asec.data.model.api.Banners;

import java.util.List;

import io.reactivex.rxjava3.core.Observable;
import retrofit2.http.GET;

public interface BannersApi
    {
    @GET("api/banners")
    Observable<List<Banners>> getBanners();
    }
