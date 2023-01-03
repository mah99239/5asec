package com.example.a5asec.ui.view.viewmodel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.a5asec.data.model.api.Setting;
import com.example.a5asec.data.repository.SettingRepository;
import com.example.a5asec.utility.ApiError;
import com.example.a5asec.utility.Resource;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.observers.DisposableObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class SettingViewModel  extends ViewModel
    {
    private static final String TAG = "SettingViewModel";
    private MutableLiveData<Resource<Setting>> mSetting = new MutableLiveData<>();
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private SettingRepository mSettingRepository;

    public SettingViewModel()
        {
        }

    public SettingViewModel(SettingRepository settingRepository)
        {

        mSettingRepository = settingRepository;
        this.fetchSetting();
        }

    public LiveData<Resource<Setting>> getSetting()
        {
        return mSetting;
        }

    private void fetchSetting()
        {

        mSetting.postValue(Resource.loading(null));
        compositeDisposable.add(
                mSettingRepository.getSetting()
                        .subscribeOn(Schedulers.single())
                        .observeOn(AndroidSchedulers.mainThread())

                        .subscribeWith(new DisposableObserver<Setting>()
                            {
                            @Override
                            public void onNext(@NonNull Setting settingResource)
                                {
                               mSetting.postValue(Resource.success(settingResource));

                                }

                            @Override
                            public void onError(@NonNull Throwable e)
                                {
                                Log.e(TAG, ApiError.handleApiError(e));
                                mSetting.postValue(Resource.error(ApiError.handleApiError(e), null));

                                }

                            @Override
                            public void onComplete()
                                {
                                onCleared();
                                // TODO document why this method is empty
                                }
                            }));


        }

    @Override
    protected void onCleared()
        {
        super.onCleared();
        compositeDisposable.clear();
        compositeDisposable.dispose();
        }
    public void reload()
        {
        fetchSetting();
        }

    }
