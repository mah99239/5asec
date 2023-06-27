package com.example.a5asec.data.local.db.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import com.example.a5asec.data.model.db.BannerEntity;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;

@Dao

public interface BannerDao
{
    @Transaction
    @Query("SELECT * FROM banner")
    Single<List<BannerEntity>> getAllBanner();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insertBanners(List<BannerEntity> categories);
}
