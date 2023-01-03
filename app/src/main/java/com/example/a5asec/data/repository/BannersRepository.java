package com.example.a5asec.data.repository;

import com.example.a5asec.data.remote.api.BannersHelper;
import com.example.a5asec.data.model.api.Banners;

import java.util.List;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;

public class BannersRepository
    {
    private static final String TAG = "BannersRepository";
    private BannersHelper mBannersHelper;


    public BannersRepository(BannersHelper banners)
        {
        this.mBannersHelper = banners;
        }

    @NonNull
    public Observable<List<Banners>> getBanners()
        {
        return mBannersHelper.getBanners();
        }
    }
