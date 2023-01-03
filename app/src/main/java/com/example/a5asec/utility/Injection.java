package com.example.a5asec.utility;

import android.content.Context;

import com.example.a5asec.data.local.db.AppDatabase;
import com.example.a5asec.data.local.db.dao.LocalOrderDataSource;
import com.example.a5asec.data.local.db.dao.OrderDataSource;
import com.example.a5asec.ui.base.CartViewModelFactory;

public class Injection
    {
    public static OrderDataSource provideUserDataSource(Context context) {
    AppDatabase database = AppDatabase.getInstance(context);
    return new LocalOrderDataSource(database.orderDao());
    }

    public static CartViewModelFactory provideViewModelFactory(Context context) {
    OrderDataSource dataSource = provideUserDataSource(context);
    return new CartViewModelFactory(dataSource);
    }
    }
