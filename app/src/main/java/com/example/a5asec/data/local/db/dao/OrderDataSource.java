package com.example.a5asec.data.local.db.dao;

import com.example.a5asec.data.model.db.ItemService;
import com.example.a5asec.data.model.db.LaundryService;
import com.example.a5asec.data.model.db.ServiceAndLaundryService;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;

public interface OrderDataSource
    {
    Observable<List<ServiceAndLaundryService>> getAllOrder();


    Single<Long>  insertOrUpdateService(ItemService service);

    Completable insertOrUpdateLaundryService(List<LaundryService> laundryService);

    Single<Integer> updateService(ItemService service);

    Completable updateLaundryService(List<LaundryService>  order);

    Single<Integer> delete(ItemService... names);
    Single<Integer> deleteLaundryService(int id);

    Single<Integer> deleteAllOrder();

    Single<Integer> getCountOrder();




    Completable restIdService ();
    Completable restIdLaundryService ();


    }
