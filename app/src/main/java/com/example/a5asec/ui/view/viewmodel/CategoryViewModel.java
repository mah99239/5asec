package com.example.a5asec.ui.view.viewmodel;

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

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.observers.DisposableObserver;
import io.reactivex.rxjava3.observers.DisposableSingleObserver;
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
@HiltViewModel
public class CategoryViewModel extends ViewModel {
    private static final String TAG = "CategoryViewModel";
    private static final int TIME_DELAY = 500;

    private final MutableLiveData<Resource<List<Category>>> mCategoriesLiveData =
            new MutableLiveData<>();
    private final MutableLiveData<Boolean> mHasData = new MutableLiveData<>(
            true);
    private final MutableLiveData<Resource<Category>> mItemCategory = new MutableLiveData<>();
    private final MutableLiveData<Resource<List<Category.ItemsEntity>>>
            mItemServicesOfItemCategory = new MutableLiveData<>();
    private final MutableLiveData<Resource<Category.ItemsEntity>> mItemService =
            new MutableLiveData<>();
    private final CompositeDisposable mCompositeDisposable = new CompositeDisposable();
    private final MutableLiveData<List<Integer>> mItemServiceChecked = new MutableLiveData<>();
    private CategoryRepository mCategoryRepository;

    public CategoryViewModel() {
    }

    @Inject
    public CategoryViewModel(CategoryRepository categoryRepository) {
        mCategoryRepository = categoryRepository;
        fetchCategories();
    }

    public LiveData<Resource<List<Category>>> getCategory() {
        return mCategoriesLiveData;
    }

    public LiveData<Boolean> hasData() {
        return mHasData;
    }


    /**
     * Fetch List of Category when called  fetchCategory.
     */
    private void fetchCategories() {

        mCategoriesLiveData.postValue(Resource.loading(null));

        mCompositeDisposable.add(mCategoryRepository.getCategories()
                .delay(TIME_DELAY, TimeUnit.MILLISECONDS)
                .subscribeWith(new DisposableSingleObserver<List<Category>>() {


                    @Override
                    public void onSuccess(@NonNull List<Category> categories) {
                        mCategoriesLiveData.postValue(
                                Resource.success(categories));
                        mHasData.postValue(true);
                        mCompositeDisposable.remove(this);

                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Timber.tag(TAG).e(e);
                        mCategoriesLiveData.postValue(
                                Resource.error(ApiError.handleApiError(e),
                                        null));
                        mCompositeDisposable.remove(this);
                        mHasData.postValue(false);
                        mCompositeDisposable.remove(this);

                    }


                }));


    }

    /**
     * called reloadCategory for reload data or when data is Error.
     */
    public void refreshCategories() {

        mCategoryRepository.refreshCache();
        fetchCategories();
    }

    /**
     * Called GetItemCategory when fetch item category
     *
     * @return item of category
     */
    public LiveData<Resource<Category>> getItemCategory() {
        return mItemCategory;
    }

    /**
     * Called setItemCategory when add new item of item category and delay for 0.5 seconds.
     *
     * @param position of list of category.
     */
    public void setItemCategory(int position) {
        mItemCategory.postValue(Resource.loading(null));

        if (Objects.requireNonNull(mCategoriesLiveData.getValue())
                .getMData() != null) {
            var item = Resource.success(
                    mCategoriesLiveData.getValue().getMData().get(position));
            mItemCategory.postValue(item);
            setItemServicesOfItemCategory(item.getMData().getItems());
        }
    }

    /**
     * Called getItemServicesOfItemCategory to get list of items in category.
     *
     * @return list of item in category item.
     */
    public LiveData<Resource<List<Category.ItemsEntity>>> getItemServicesOfItemCategory() {
        return mItemServicesOfItemCategory;


    }

    /**
     * Called when fetch new item of category and set list of items service in item category.
     *
     * @param items pass list of items.
     */
    private void setItemServicesOfItemCategory(List<Category.ItemsEntity> items) {
        mItemServicesOfItemCategory.postValue(Resource.loading(null));

        mItemServicesOfItemCategory.postValue(Resource.success(items));
    }

    /**
     * Called when initialized Two pane and get first item in category..
     */
    public void setItemCategoryWithTwoPane() {
        setItemCategory(0);

    }

    public LiveData<Resource<Category.ItemsEntity>> getItem() {
        return mItemService;
    }

    public Category.ItemServicesEntity getItemServiceById(int id) {

        return Objects.requireNonNull(mItemService.getValue()).getMData()
                .getItemServices().stream()
                .filter(itemServicesEntity -> itemServicesEntity.getId() == id)
                .findFirst().get();
    }

    public List<Category.ItemServicesEntity> getItemService() {
        return mItemService.getValue().getMData().getItemServices();
    }

    public void setItemService(int position) {
        mItemService.postValue(Resource.loading(null));

        var item = Objects.requireNonNull(
                        mItemServicesOfItemCategory.getValue()).getMData()
                .get(position);
        mItemService.postValue(Resource.success(item));

    }

    //TODO: called this method in laundry Services bottom sheet.
    public void setItemServiceWithId(int id) {
        Timber.tag(TAG).e("mCategory item service = %s",
                mCategoriesLiveData.getValue());

        if (mCategoriesLiveData.getValue() != null) {
            setItemServiceStream(id);
        }
        else {
            fetchItemCategory(id);
        }
    }

    public void setItemServiceStream(int id) {
      /*   mCategory.getValue().getMData().stream().findAny().ifPresent(category ->
            {
            if (category.getId() == id)
                mItemService.postValue(Resource.success(category));
            }
        ); */
        mItemService.postValue(Resource.loading(null));

        categoryLoop:
        for (Category c : mCategoriesLiveData.getValue().getMData()) {
            for (Category.ItemsEntity item : c.getItems()) {
                if (item.getId() == id) {

                    mItemService.postValue(Resource.success(item));
                    Timber.tag(TAG).e("find: = %s", item);

                    break categoryLoop;
                }
            }

          /*   var find = c.getItems().stream()
                    .filter(itemsEntity ->
                        {

                        return false;
                        }).findFirst();
            break categoryLoop; */


        }


    }

    public LiveData<List<Integer>> getItemLaundryServiceChecked() {
        return mItemServiceChecked;
    }

    public void setItemLaundryServiceChecked(List<Integer> id) {
        mItemServiceChecked.postValue(id);
    }

    private void fetchItemCategory(int id) {

        mItemService.postValue(Resource.loading(null));

        mCompositeDisposable.add(mCategoryRepository.getItemCategory(id)
                .subscribeWith(new DisposableObserver<Category.ItemsEntity>() {
                    @Override
                    public void onNext(@NonNull Category.ItemsEntity listResource) {
                        mItemService.postValue(Resource.success(listResource));
                        Timber.tag(TAG).e("onNext =%s", listResource);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Timber.tag(TAG).e(e);
                        mItemService.postValue(
                                Resource.error(ApiError.handleApiError(e),
                                        null));
                        Timber.tag(TAG).e("onError()");
                        mCompositeDisposable.remove(this);
                    }

                    @Override
                    public void onComplete() {
                        Timber.tag(TAG).e("onComplete()");
                        mCompositeDisposable.remove(this);

                    }
                }));
    }


    @Override
    protected void onCleared() {
        super.onCleared();
        mCompositeDisposable.clear();

    }


}