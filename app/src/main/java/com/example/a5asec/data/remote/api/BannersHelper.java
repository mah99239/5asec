package com.example.a5asec.data.remote.api;

import com.example.a5asec.data.model.api.Banner;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Single;

public class BannersHelper
{
    private final BannersApi mBannersApi;

    @Inject
    public BannersHelper(BannersApi bannersApi) {
        this.mBannersApi = bannersApi;
    }

    public Single<List<Banner>> getBanners() {
        return this.mBannersApi.getBanners();
    }
}