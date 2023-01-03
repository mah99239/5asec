package com.example.a5asec.data.local.db;


import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.a5asec.data.local.db.dao.OrderDao;
import com.example.a5asec.data.model.db.ItemService;
import com.example.a5asec.data.model.db.LaundryService;

/**
 * Created by Mahmoud on 18/12/2022.
 */

@Database(entities = {ItemService.class, LaundryService.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase
    {
    public static final String DB_NAME = "5asec_db";

    private static volatile AppDatabase INSTANCE;

    public abstract OrderDao orderDao();


    public static AppDatabase getInstance(final Context context)
        {
        if (INSTANCE == null)
            {
            synchronized (AppDatabase.class)
                {
                if (INSTANCE == null)
                    {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    AppDatabase.class, DB_NAME)
                            .fallbackToDestructiveMigration()
                            .build();
                    }
                }
            }
        return INSTANCE;
        }


    }
