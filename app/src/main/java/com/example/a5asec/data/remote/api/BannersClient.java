package com.example.a5asec.data.remote.api;

import com.example.a5asec.data.model.api.Banners;

import java.util.List;

import io.reactivex.rxjava3.core.Observable;

public class BannersClient extends Client implements BannersApi
    {
    private static BannersClient INSTANCE;

    private final BannersApi mBannersApi;

    public BannersClient() {
    mBannersApi = getRetrofitAdapter().create(BannersApi.class);
    }

    public static BannersClient getINSTANCE() {
    return null == INSTANCE ? new BannersClient() : INSTANCE;
    }


    @Override
    public Observable<List<Banners>> getBanners()
        {
        return mBannersApi.getBanners();
        }
    }
