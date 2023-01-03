package com.example.a5asec.data.remote.api;

import com.example.a5asec.data.model.api.Banners;

import java.util.List;

import io.reactivex.rxjava3.core.Observable;

public class BannersHelper {
private BannersApi mBannersApi;

public BannersHelper(BannersApi bannersApi) {
this.mBannersApi = bannersApi;
}

public Observable<List<Banners>> getBanners() {
return this.mBannersApi.getBanners();
}
}