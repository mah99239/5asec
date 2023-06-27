package com.example.a5asec.data.repository;

import com.example.a5asec.data.local.db.dao.CategoryDataSource;
import com.example.a5asec.data.model.api.Category;
import com.example.a5asec.data.model.db.CategoryEntity;
import com.example.a5asec.data.remote.api.CategoryHelper;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;
import timber.log.Timber;

public class CategoryRepository
{
    public static final String TAG = "CategoryRepository";
    private final CategoryHelper categoryHelper;
    private final CategoryDataSource localCategoryDataSource;
    private boolean isCacheDirty = false;

    @Inject
    public CategoryRepository(CategoryHelper categoryHelper, CategoryDataSource localCategoryDataSource) {
        this.categoryHelper = categoryHelper;
        this.localCategoryDataSource = localCategoryDataSource;
    }

    /**
     * Gets the list of categories from the local database, or fetches it from the remote API if the
     * local database is empty or the cache is dirty.
     */
    public Single<List<Category>> getCategories() {
        return localCategoryDataSource.getCategoriesWithItemsAndServices()
                .flatMap(categoryWithItems -> {
                    if (categoryWithItems.isEmpty() || isCacheDirty) {
                        Timber.tag(TAG).e("from network");
                        return refreshCategories();
                    } else {
                        Timber.tag(TAG).e("from database");
                        return Single.just(CategoryEntity.asDomainModel(
                                categoryWithItems));
                    }
                }).onErrorResumeNext(throwable -> refreshCategories())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }


    /**
     * Fetches the list of categories from the remote API and updates the local database.
     */

    public Single<List<Category>> refreshCategories() {
        return categoryHelper.getCategory().subscribeOn(Schedulers.io())
                .flatMap(
                        remoteCategories -> localCategoryDataSource.insertCategoriesWithItemsAndServices(
                                        Category.asDatabaseModel(remoteCategories))
                                .andThen(Single.just(remoteCategories)))
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(d -> Timber.tag(TAG).d("subscribe"))
                .doOnSuccess(s -> {
                    Timber.tag(TAG).d("onComplete");
                    isCacheDirty = false;
                }).doOnError(e -> Timber.tag(TAG).e(e));
    }

    /**
     * Fetches a single item from the remote API
     */
    @NonNull
    public Observable<Category.ItemsEntity> getItemCategory(long id) {
        return categoryHelper.getItemCategory(id).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }


    /**
     * Refreshes the list of categories by fetching the categories from the remote API and updating
     * the local database.
     */
    public void refreshCache() {
        isCacheDirty = true;

    }
}
