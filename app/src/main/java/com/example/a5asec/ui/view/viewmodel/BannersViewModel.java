package com.example.a5asec.ui.view.viewmodel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.a5asec.data.model.api.Banners;
import com.example.a5asec.data.repository.BannersRepository;
import com.example.a5asec.utility.ApiError;
import com.example.a5asec.utility.Resource;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.observers.DisposableObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class BannersViewModel extends ViewModel
    {
    private static final String TAG = "BannersViewModel";
    private MutableLiveData<Resource<List<Banners>>> mBanners = new MutableLiveData<>();
    private CompositeDisposable mCompositeDisposable = new CompositeDisposable();
    private BannersRepository mBannersRepository;

    public BannersViewModel()
        {
        }

    public BannersViewModel(BannersRepository bannersRepository)
        {
        mBannersRepository = bannersRepository;
        fetchBanners();
        }

    public LiveData<Resource<List<Banners>>> getBanners()
        {
        return mBanners;
        }

    private void fetchBanners()
        {

        mBanners.postValue(Resource.loading(null));
        mCompositeDisposable.add(
                mBannersRepository.getBanners()
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())

                        .subscribeWith(new DisposableObserver<List<Banners>>()
                            {
                            @Override
                            public void onNext(@NonNull List<Banners> listResource)
                                {
                                mBanners.postValue(Resource.success(listResource));

                                }

                            @Override
                            public void onError(@NonNull Throwable e)
                                {
                                Log.e(TAG, e.getMessage());
                                mBanners.postValue(Resource.error(ApiError.handleApiError(e), null));

                                }

                            @Override
                            public void onComplete()
                                {
                                onCleared();
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
