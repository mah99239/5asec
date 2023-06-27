package com.example.a5asec.data.repository;

import com.example.a5asec.data.local.db.dao.BannerDao;
import com.example.a5asec.data.model.api.Banner;
import com.example.a5asec.data.model.db.BannerEntity;
import com.example.a5asec.data.remote.api.BannersHelper;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;
import timber.log.Timber;

public class BannersRepository
{
    private static final String TAG = "BannersRepository";
    private final BannersHelper mBannersHelper;
    private final BannerDao mBannerDao;
    private boolean isCacheDirty = false;

    @Inject
    public BannersRepository(BannersHelper banners, BannerDao bannerDao)
    {
        this.mBannersHelper = banners;
        this.mBannerDao = bannerDao;
    }

    @NonNull
    public Single<List<Banner>> getBanners()
    {

        return mBannerDao.getAllBanner().flatMap(bannerEntities -> {
                    if (bannerEntities.isEmpty() || isCacheDirty) {
                        return refreshBanners();
                    } else {
                        return Single.just(BannerEntity.asDomainModel(bannerEntities));
                    }
                }).onErrorResumeNext(throwable -> refreshBanners())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }


    private Single<List<Banner>> refreshBanners() {
        return mBannersHelper.getBanners().subscribeOn(Schedulers.io()).flatMap(
                        remoteBanners -> mBannerDao.insertBanners(
                                        Banner.asDatabaseModel(remoteBanners))
                                .andThen(Single.just(remoteBanners)))
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(d -> Timber.tag(TAG).d("subscribe"))
                .doOnSuccess(s -> Timber.tag(TAG).d("onComplete"))
                .doOnError(e -> Timber.tag(TAG).e(e));
    }

    public void refreshCache() {
        isCacheDirty = true;

    }
}
