package com.example.a5asec.data.remote.api;

import com.example.a5asec.data.model.api.Banner;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Single;


public class BannersClient implements BannersApi
{

    private final BannersApi mBannersApi;

    @Inject
    public BannersClient(BannersApi bannersApi) {
        mBannersApi = bannersApi;
    }


    @Override
    public Single<List<Banner>> getBanners()
    {
        return mBannersApi.getBanners();
    }
}
