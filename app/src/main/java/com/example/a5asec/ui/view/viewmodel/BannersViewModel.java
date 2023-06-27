package com.example.a5asec.ui.view.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.a5asec.data.model.api.Banner;
import com.example.a5asec.data.repository.BannersRepository;
import com.example.a5asec.ui.adapters.BannersAdapter;
import com.example.a5asec.utility.ApiError;
import com.example.a5asec.utility.Resource;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.observers.DisposableSingleObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;
import timber.log.Timber;

@HiltViewModel
public class BannersViewModel extends ViewModel
{
    private static final String TAG = "BannersViewModel";
    private final MutableLiveData<Resource<List<Banner>>> mBanners = new MutableLiveData<>();
    private final CompositeDisposable mCompositeDisposable = new CompositeDisposable();
    private final MutableLiveData<Boolean> mHasData = new MutableLiveData<>(
            true);
    private BannersAdapter adapter;
    private BannersRepository mBannersRepository;

    public BannersViewModel()
    {
    }

    @Inject
    public BannersViewModel(BannersRepository bannersRepository)
    {
        mBannersRepository = bannersRepository;
        adapter = new BannersAdapter();
        fetchBanners();
    }

    public BannersAdapter getAdapter()
    {
        return this.adapter;
    }

    public List<Banner> getData()
    {
        return mBanners.getValue().getMData();
    }

    public LiveData<Resource<List<Banner>>> getBanners()
    {
        return mBanners;
    }

    public LiveData<Boolean> hasData()
    {
        return mHasData;
    }

    private void fetchBanners()
    {

        mBanners.postValue(Resource.loading(null));
        mCompositeDisposable.add(
                mBannersRepository.getBanners().subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())

                        .subscribeWith(
                                new DisposableSingleObserver<List<Banner>>()
                                {

                                    @Override
                                    public void onSuccess(@NonNull List<Banner> banners) {
                                        mBanners.postValue(
                                                Resource.success(banners));
                                        mHasData.setValue(true);
                                        Timber.tag(TAG)
                                                .e("fetchBanners() -> onNext()");
                                        mCompositeDisposable.remove(this);

                                    }

                                    @Override
                                    public void onError(@NonNull Throwable e)
                                    {
                                        Timber.tag(TAG).e(e);
                                        mBanners.postValue(Resource.error(
                                                ApiError.handleApiError(e),
                                                null));
                                        mHasData.postValue(false);
                                        mCompositeDisposable.remove(this);


                                    }


                                }));


    }

    /**
     * called to reload data
     */
    public void reload()
    {
        fetchBanners();
    }

    @Override
    protected void onCleared()
    {
        super.onCleared();
        mCompositeDisposable.clear();
        mCompositeDisposable.dispose();
    }


}
