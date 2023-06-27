package com.example.a5asec.data.model;

import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.a5asec.data.local.db.AppDatabase;
import com.example.a5asec.data.model.db.ItemService;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class OrderDaoTest
    {
    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();
    ItemService item = new ItemService("fas", "fas", 12, 21, 2);
    private AppDatabase mDatabase;

    @Before
    public void initDb() {
    // using an in-memory database because the information stored here disappears when the
    // process is killed
    mDatabase = Room.inMemoryDatabaseBuilder(ApplicationProvider.getApplicationContext(),
                    AppDatabase.class)
            // allowing main thread queries, just for testing
            .allowMainThreadQueries()
            .build();

    }

    @After
    public void closeDb() {
    mDatabase.close();
    }

    @Test
    public void getUsersWhenNoUserInserted() {
    mDatabase.orderDao().getAllOrder()
            .test()
            .assertNoValues();
    }

    @Test
    public void insertAndGetUser() {
    // When inserting a new user in the data source
    mDatabase.orderDao().insertService(item).blockingSubscribe();

    // When subscribing to the emissions of the user
    mDatabase.orderDao().getAllOrder()
            .test()
            // assertValue asserts that there was only one emission of the user
            .assertValue(i -> {
            // The emitted user is the expected one
            var data = i.get(0);

            return data != null && data.getService().getNameEn().equals(item.getNameEn()) &&
                    data.getService().getNameAr().equals(item.getNameAr());
            });
    }
    }
