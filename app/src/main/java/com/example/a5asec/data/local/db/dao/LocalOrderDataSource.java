package com.example.a5asec.data.local.db.dao;

import com.example.a5asec.data.model.db.ItemService;
import com.example.a5asec.data.model.db.LaundryService;
import com.example.a5asec.data.model.db.ServiceAndLaundryService;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Single;

public class LocalOrderDataSource implements OrderDataSource {
    private final OrderDao mOrderDao;

    public LocalOrderDataSource(OrderDao orderDao) {
        this.mOrderDao = orderDao;
    }


    @Override
    public Flowable<List<ServiceAndLaundryService>> getAllOrder() {
        return mOrderDao.getAllOrder();
    }

    @Override
    public Single<Long> insertOrUpdateService(ItemService service) {
        return mOrderDao.insertService(service);
    }

    @Override
    public Completable insertOrUpdateLaundryService(List<LaundryService> laundryService) {
        return mOrderDao.insertLaundryService(laundryService);
    }

    @Override
    public Single<Integer> updateService(ItemService service) {
        return mOrderDao.updateService(service);
    }

    @Override
    public Completable updateLaundryService(List<LaundryService> order) {
        return mOrderDao.updateLaundryServices(order);
    }


    @Override
    public Single<Integer> delete(ItemService... names) {
        return mOrderDao.deleteService(names);
    }

    @Override
    public Single<Integer> deleteLaundryService(int id) {
        return mOrderDao.deleteLaundryService(id);
    }

    @Override
    public Single<Integer> deleteAllOrder() {
        return mOrderDao.deleteAllService();
    }

    @Override
    public Flowable<Integer> getCountOrder() {
        return mOrderDao.getCountOrder();
    }


    public Completable restIdService() {
        return mOrderDao.restIdService();
    }

    public Completable restIdLaundryService() {
        return mOrderDao.restIdLaundryService();
    }
}
