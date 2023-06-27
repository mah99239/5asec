package com.example.a5asec.di;

import android.content.Context;

import androidx.room.Room;

import com.example.a5asec.data.local.db.AppDatabase;
import com.example.a5asec.data.local.db.dao.BannerDao;
import com.example.a5asec.data.local.db.dao.CategoryDao;
import com.example.a5asec.data.local.db.dao.CategoryDataSource;
import com.example.a5asec.data.local.db.dao.LocalCategoryDataSource;
import com.example.a5asec.data.local.db.dao.OrderDao;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.qualifiers.ApplicationContext;
import dagger.hilt.components.SingletonComponent;

@Module
@InstallIn(SingletonComponent.class)
public class AppDatabaseModule {
    @Provides
    @Singleton
    public static AppDatabase provideAppDatabase(@ApplicationContext Context context) {
        return Room.databaseBuilder(context.getApplicationContext(),
                        AppDatabase.class, AppDatabase.DB_NAME)
                .fallbackToDestructiveMigration().allowMainThreadQueries()
                .build();
    }


    @Provides
    @Singleton
    public static OrderDao provideOrderDao(AppDatabase appDatabase) {
        return appDatabase.orderDao();
    }

    @Provides
    @Singleton
    public static CategoryDataSource provideCategoryDataSource(CategoryDao categoryDao) {
        return new LocalCategoryDataSource(categoryDao);
    }

    @Provides
    @Singleton
    public static CategoryDao provideCategoryDao(AppDatabase appDatabase) {
        return appDatabase.categoryDao();
    }

    @Provides
    @Singleton
    public static BannerDao provideBannerDao(AppDatabase appDatabase) {
        return appDatabase.bannerDao();
    }
}
