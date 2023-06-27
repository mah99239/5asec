package com.example.a5asec.data.local.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.a5asec.data.local.db.dao.BannerDao;
import com.example.a5asec.data.local.db.dao.CategoryDao;
import com.example.a5asec.data.local.db.dao.OrderDao;
import com.example.a5asec.data.model.db.BannerEntity;
import com.example.a5asec.data.model.db.CategoryEntity;
import com.example.a5asec.data.model.db.ItemService;
import com.example.a5asec.data.model.db.LaundryService;

/**
 * Created by Mahmoud on 18/12/2022.
 */
@Database(entities = {CategoryEntity.class, CategoryEntity.ItemsEntity.class, CategoryEntity.ItemServicesEntity.class, CategoryEntity.LaundryServicesEntity.class, ItemService.class, LaundryService.class, BannerEntity.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {


    public static final String DB_NAME = "5asec_db";


    public AppDatabase() {
    }


    public abstract OrderDao orderDao();

    public abstract CategoryDao categoryDao();

    public abstract BannerDao bannerDao();


}
