package com.example.a5asec.data.repository;

import com.example.a5asec.data.local.db.dao.OrderDao;
import com.example.a5asec.data.model.db.ItemService;
import com.example.a5asec.data.model.db.LaundryService;
import com.example.a5asec.data.model.db.ServiceAndLaundryService;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class CartRepository {

    private final OrderDao orderDao;

    @Inject
    public CartRepository(OrderDao orderDao) {
        this.orderDao = orderDao;
    }

    @NonNull
    public Flowable<List<ServiceAndLaundryService>> getOrder() {
        return orderDao.getOrder().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Single<Long> insertService(ItemService service) {
        return orderDao.insertService(service).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Completable insertLaundryService(List<LaundryService> laundryServices) {
        return orderDao.insertLaundryService(laundryServices)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Single<Integer> updateService(ItemService order) {
        return orderDao.updateService(order).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Completable updateLaundryService(List<LaundryService> order) {
        return orderDao.updateLaundryServices(order).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Completable updateLaundryService(LaundryService laundryService) {
        return orderDao.updateLaundryService(laundryService).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Single<Integer> deleteService(ItemService names) {
        return orderDao.deleteService(names).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Single<Integer> deleteLaundryService(int id) {
        return orderDao.deleteLaundryService(id).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Single<Integer> deleteAllService() {
        return orderDao.deleteAllService().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Flowable<List<ServiceAndLaundryService>> getAllOrder() {
        return orderDao.getAllOrder().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Flowable<Integer> getCountOrder() {
        return orderDao.getCountOrder().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Completable restIdService() {
        return orderDao.restIdService().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Completable restIdLaundryService() {
        return orderDao.restIdLaundryService().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
