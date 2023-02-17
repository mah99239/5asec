package com.example.a5asec.ui.view.viewmodel;

import android.os.Handler;
import android.os.Looper;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.a5asec.data.model.api.Category;
import com.example.a5asec.data.repository.CategoryRepository;
import com.example.a5asec.ui.view.home.LaundryServicesBottomSheet;
import com.example.a5asec.ui.view.home.ServicesFragment;
import com.example.a5asec.utility.ApiError;
import com.example.a5asec.utility.Resource;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.observers.DisposableObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;
import timber.log.Timber;


/**
 * Exposes the data to be used in the price screen.
 * {@link com.example.a5asec.ui.view.home.PriceListFragment}
 * {@link LaundryServicesBottomSheet}
 * {@link ServicesFragment}
 * This view-model uses  {@link MutableLiveData }s {@link CompositeDisposable}s in this case and
 * {@link Resource<Category>}s to manage data in the screen,
 * {@link CategoryRepository}
 */
public class CategoryViewModel extends ViewModel
    {
    private static final String TAG = "CategoryViewModel";
    private static final int TIME_DELAY = 500;

    private final MutableLiveData<Resource<List<Category>>> mCategory = new MutableLiveData<>();
    private final MutableLiveData<Boolean> mHasData = new MutableLiveData<>(true);
    private final MutableLiveData<Resource<Category>> mItemCategory = new MutableLiveData<>();
    private final MutableLiveData<Resource<List<Category.ItemsEntity>>> mItemServicesOfItemCategory = new MutableLiveData<>();
    private final MutableLiveData<Resource<Category.ItemsEntity>> mItemService = new MutableLiveData<>();
    private final CompositeDisposable mCompositeDisposable = new CompositeDisposable();
    private CategoryRepository mCategoryRepository;
    private final MutableLiveData<List<Integer>> mItemServiceChecked = new MutableLiveData<>();

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
public LiveData<Boolean> hasData()
    {
    return mHasData;
    }


    /**
     * Fetch List of Category when called  fetchCategory.
     */
    private void fetchCategory()
        {

        mCategory.postValue(Resource.loading(null));

        mCompositeDisposable.add(
                mCategoryRepository.getCategory()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .delay(TIME_DELAY, TimeUnit.MILLISECONDS)
                        .subscribeWith(new DisposableObserver<List<Category>>()
                            {
                            @Override
                            public void onNext(@NonNull List<Category> listResource)
                                {
                                mCategory.postValue(Resource.success(listResource));
                                mHasData.postValue(true);
                                }

                            @Override
                            public void onError(@NonNull Throwable e)
                                {
                                Timber.tag(TAG).e(e);
                                mCategory.postValue(Resource.error(ApiError.handleApiError(e), null));
                                mCompositeDisposable.remove(this);
                                mHasData.postValue(false);

                                }

                            @Override
                            public void onComplete()
                                {
                                mCompositeDisposable.remove(this);

                                }
                            }));


        }

    /**
     *  called reloadCategory for reload data or when data is Error.
     */
    public void reloadCategory()
        {
        fetchCategory();
        }

    /**
     * Called GetItemCategory when fetch item category
     * @return item of category
     */
    public LiveData<Resource<Category>> getItemCategory()
        {
        return mItemCategory;
        }

    /**
     * Called setItemCategory when add new item of item category and delay for 0.5 seconds.
     * @param position of list of category.
     */
    public void setItemCategory(int position)
        {
        mItemCategory.postValue(Resource.loading(null));

        if (Objects.requireNonNull(mCategory.getValue()).getMData() != null)
            {
            new Handler(Looper.getMainLooper()).postDelayed(() ->
                {

                var item = Resource.success(mCategory.getValue().getMData().get(position));
                mItemCategory.postValue(item);
                assert item != null;
                setItemServicesOfItemCategory(item.getMData().getItems());
                }, TIME_DELAY);
            }

        }

    /**
     *  Called when fetch new item of category and set list of items service in item category.
     * @param items pass list of items.
     */
    private void setItemServicesOfItemCategory(List<Category.ItemsEntity> items)
        {
        mItemServicesOfItemCategory.postValue(Resource.loading(null));

        mItemServicesOfItemCategory.postValue(Resource.success(items));
        }

    /**
     * Called getItemServicesOfItemCategory to get list of items in category.
     * @return list of item in category item.
     */
    public LiveData<Resource<List<Category.ItemsEntity>>> getItemServicesOfItemCategory()
        {
        return mItemServicesOfItemCategory;
        }

    /**
     * Called when initialized Two pane and get first item in category..
     */
    public void setItemCategoryWithTwoPane()
        {
        setItemCategory(0);

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

    public void setItemService(int id)
        {
        if (mCategory.getValue() != null)
            {
            setItemServiceStream(id);
            } else
            {
            fetchItemCategory(id);
            }
        }

    public void setItemServiceStream(int id)
        {
      /*   mCategory.getValue().getMData().stream().findAny().ifPresent(category ->
            {
            if (category.getId() == id)
                mItemService.postValue(Resource.success(category));
            }
        ); */

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
            Timber.tag(TAG).e("find: = %s", find);

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

    private void fetchItemCategory(int id)
        {

        mItemService.postValue(Resource.loading(null));

        mCompositeDisposable.add(
                mCategoryRepository.getItemCategory(id)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())

                        .subscribeWith(new DisposableObserver<Category.ItemsEntity>()
                            {
                            @Override
                            public void onNext(@NonNull Category.ItemsEntity listResource)
                                {
                                mItemService.postValue(Resource.success(listResource));
                                Timber.tag(TAG).e("onNext =%s", listResource);
                                }

                            @Override
                            public void onError(@NonNull Throwable e)
                                {
                                Timber.tag(TAG).e(e);
                                mItemService.postValue(Resource.error(ApiError.handleApiError(e), null));
                                Timber.tag(TAG).e("onError()");
                                mCompositeDisposable.remove(this);
                                }

                            @Override
                            public void onComplete()
                                {
                                Timber.tag(TAG).e("onComplete()");
                                mCompositeDisposable.remove(this);

                                }
                            }));
        }


    @Override
    protected void onCleared()
        {
        super.onCleared();
        mCompositeDisposable.clear();

        }


    }