package com.example.a5asec.ui.view.viewmodel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.a5asec.data.model.api.Category;
import com.example.a5asec.data.repository.CategoryRepository;
import com.example.a5asec.ui.view.home.ServicesFragment;
import com.example.a5asec.utility.ApiError;
import com.example.a5asec.utility.Resource;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.observers.DisposableObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;


/**
 * Exposes the data to be used in the price screen.
 * {@link com.example.a5asec.ui.view.home.PriceListFragment}
 * {@link com.example.a5asec.ui.view.home.LaundryServicesFragment}
 * {@link ServicesFragment}
 * This view-model uses  {@link MutableLiveData }s {@link CompositeDisposable}s in this case and
 * {@link Resource<Category>}s to manage data in the screen,
 * {@link CategoryRepository}
 */
public class CategoryViewModel extends ViewModel
    {
    private static final String TAG = "CategoryViewModel";

    private  MutableLiveData<Resource<List<Category>>> mCategory = new MutableLiveData<>();
    private MutableLiveData<Resource<List<Category.ItemsEntity>>> mItemsCategory = new MutableLiveData<>();
    private  MutableLiveData<Resource<Category.ItemsEntity>> mItemService = new MutableLiveData<>();
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    private CategoryRepository mCategoryRepository;
    private MutableLiveData<List<Integer>> mItemServiceChecked = new MutableLiveData<>();

    public CategoryViewModel()
        {
        }

    public CategoryViewModel(CategoryRepository categoryRepository)
        {
        mCategoryRepository = categoryRepository;
        fetchCategory();
        }

    public LiveData<Resource<List<Category>>> getCategory()
        {
        return mCategory;
        }


    /**
     * FetchCategory used for fetch data.
     */
    private void fetchCategory()
        {

        mCategory.postValue(Resource.loading(null));

        compositeDisposable.add(
                mCategoryRepository.getCategory()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())

                        .subscribeWith(new DisposableObserver<List<Category>>()
                            {
                            @Override
                            public void onNext(@NonNull List<Category> listResource)
                                {
                                mCategory.postValue(Resource.success(listResource));

                                }

                            @Override
                            public void onError(@NonNull Throwable e)
                                {
                                Log.e(TAG, e.getMessage());
                                mCategory.postValue(Resource.error(ApiError.handleApiError(e), null));
                                compositeDisposable.remove(this);

                                }

                            @Override
                            public void onComplete()
                                {
                                compositeDisposable.remove(this);

                                }
                            }));


        }

    /**
     * reloadCategory called for reload data and when data is Error.
     */
    public void reloadCategory()
        {
        fetchCategory();
        }

    public LiveData<Resource<Category.ItemsEntity>> getItemService()
        {
        return mItemService;
        }

    public void setItemService(Resource<Category.ItemsEntity> items)
        {
        mItemService.postValue(Resource.loading(null));
        mItemService.postValue(items);

        }

    public void setItemService(long id)
        {
        if (mCategory.getValue() != null)
            {
            setItemServiceStream(id);
            } else
            {
            fetchItemCategory(id);
            }
        }

    public void setItemServiceStream(long id)
        {
        for (Category c : mCategory.getValue().getMData())
            {
            var find = c.getItems().stream()
                    .filter(itemsEntity ->
                        {
                        if (itemsEntity.getId() == id)
                            {

                            mItemService.postValue(Resource.success(itemsEntity));

                            return true;
                            }
                        return false;
                        }).findAny();
            // .filter(category -> category.getId() == id).findAny();
            Log.e(TAG, "find: = " + find);

            }


        }

    public LiveData<List<Integer>> getItemLaundryServiceChecked()
        {
        return mItemServiceChecked;
        }

    public void setItemLaundryServiceChecked(List<Integer> id)
        {
        mItemServiceChecked.postValue(id);
        }

    private void fetchItemCategory(long id)
        {

        mItemService.postValue(Resource.loading(null));

        compositeDisposable.add(
                mCategoryRepository.getItemCategory(id)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())

                        .subscribeWith(new DisposableObserver<Category.ItemsEntity>()
                            {
                            @Override
                            public void onNext(@NonNull Category.ItemsEntity listResource)
                                {
                                mItemService.postValue(Resource.success(listResource));
                                Log.e(TAG, "onNext =" + listResource);
                                }

                            @Override
                            public void onError(@NonNull Throwable e)
                                {
                                Log.e(TAG, e.getMessage());
                                mItemService.postValue(Resource.error(ApiError.handleApiError(e), null));
                                Log.e(TAG, "onError()");
                                compositeDisposable.remove(this);
                                }

                            @Override
                            public void onComplete()
                                {
                                Log.e(TAG, "onComplete()");
                                compositeDisposable.remove(this);

                                }
                            }));
        }

    public LiveData<Resource<List<Category.ItemsEntity>>> getItemCategory()
        {
        return mItemsCategory;
        }

    public void setItemCategory(Resource<List<Category.ItemsEntity>> items)
        {
        mItemsCategory.postValue(Resource.loading(null));
        mItemsCategory.postValue(items);

        }
    public void setItemCategoryWithTwoPane()
        {
        mItemsCategory.postValue(Resource.loading(null));
        var item =  Resource.success(  mCategory.getValue().getMData().get(0).getItems());
        mItemsCategory.postValue(item);

        }

    @Override
    protected void onCleared()
        {
        super.onCleared();
        compositeDisposable.clear();
        compositeDisposable.dispose();

        }


    }