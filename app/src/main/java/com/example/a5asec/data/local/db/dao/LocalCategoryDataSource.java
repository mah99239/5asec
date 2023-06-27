package com.example.a5asec.data.local.db.dao;

import com.example.a5asec.data.model.db.CategoryEntity;
import com.example.a5asec.data.model.db.CategoryWithItems;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.CompletableObserver;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import timber.log.Timber;

@Singleton
public class LocalCategoryDataSource implements CategoryDataSource {
    private final CategoryDao mCategoryDao;

    @Inject
    public LocalCategoryDataSource(CategoryDao categoryDao) {
        this.mCategoryDao = categoryDao;
    }


    @Override
    public Single<List<CategoryWithItems>> getCategoriesWithItemsAndServices() {
        return mCategoryDao.getCategoriesWithItemsAndServices();
    }


    @Override
    public Completable insertItems(List<CategoryEntity.ItemsEntity> items) {
        return mCategoryDao.insertItems(items);
    }

    @Override
    public Completable insertItemService(List<CategoryEntity.ItemServicesEntity> categories) {
        return mCategoryDao.insertItemService(categories);
    }

    @Override
    public Completable insertLaundryService(List<CategoryEntity.LaundryServicesEntity> categories) {
        return mCategoryDao.insertLaundryService(categories);
    }

    public Completable insertCategoriesWithItemsAndServices(List<CategoryEntity> categoryEntities) {
        return Completable.fromAction(() -> {
            for (CategoryEntity categoryEntity : categoryEntities) {
                List<CategoryEntity.ItemsEntity> items = categoryEntity.items;
                List<CategoryEntity.ItemServicesEntity> itemsServices = new ArrayList<>();
                List<CategoryEntity.LaundryServicesEntity> laundryServices = new ArrayList<>();

                for (CategoryEntity.ItemsEntity item : items) {
                    if (item.itemServices != null) {
                        for (CategoryEntity.ItemServicesEntity service : item.itemServices) {
                            if (service.laundryService != null) {
                                laundryServices.add(service.laundryService);

                            }
                            itemsServices.add(service);
                        }

                    }
                }

                insertItems(items).subscribeOn(Schedulers.io()).andThen(insertLaundryService(laundryServices)).andThen(insertItemService(itemsServices)).subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        Timber.tag("LocalCategoryDataSource").d("Insertion started");
                    }

                    @Override
                    public void onComplete() {
                        Timber.tag("LocalCategoryDataSource").d("Insertion completed");
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Timber.tag("LocalCategoryDataSource").e(e, "Insertion failed");
                    }
                });
            }
        }).andThen(mCategoryDao.insertCategoriesWithItemsAndServices(categoryEntities));
    }
}



