package com.example.a5asec.ui.view.viewmodel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.a5asec.data.remote.api.ResponseEntity;
import com.example.a5asec.data.remote.api.UserClient;
import com.example.a5asec.data.remote.api.UserHelper;
import com.example.a5asec.utility.Resource;

import java.io.IOException;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.observers.DisposableSingleObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;
import retrofit2.HttpException;

public class ChangePasswordViewModel extends ViewModel
    {
    private static final String TAG = "ChangePasswordViewModel";
    private MutableLiveData<Resource<ResponseEntity>> mStatusCode ;
    private final CompositeDisposable mCompositeDisposable = new CompositeDisposable();
    private final UserHelper mUserHelper = new UserHelper(new UserClient());


    public ChangePasswordViewModel()
        {

        }

    public void setRestPassword(String  email)
        {
        restPassword(email);
        }
    public void setFinishPassword(String code, String password)
        {
        finishPassword(code, password);
        }

    public LiveData<Resource<ResponseEntity>> getStatusCode()
        {
        return mStatusCode;
        }

    public void restPassword(String email)
        {
        mStatusCode = new MutableLiveData<>();
        mCompositeDisposable.add(
                mUserHelper.restPassword(email)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())

                        .subscribeWith(new DisposableSingleObserver<ResponseEntity>()
                            {
                            @Override
                            public void onSuccess(@NonNull ResponseEntity userResource)
                                {
                                mStatusCode.setValue(Resource.success(userResource));
                                Log.e(TAG, "registerUsers:SUCCESS");

                                }

                            @Override
                            public void onError(@NonNull Throwable e)
                                {
                                mCompositeDisposable.remove(this);
                                try
                                    {

                                    var message =
                                            ((HttpException) e).response().errorBody().string();

                                    mStatusCode.setValue(Resource.error(message, null));

                                    Log.e(TAG, "error body: "
                                            + ((HttpException) e).response().errorBody().string());
                                    } catch (IOException ex)
                                    {
                                    ex.printStackTrace();
                                    }


                                }

                            }));

        }

    public void finishPassword(String key, String passsword)
        {
        mStatusCode = new MutableLiveData<>();
        mCompositeDisposable.add(
                mUserHelper.finishPassword(key, passsword)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())

                        .subscribeWith(new DisposableSingleObserver<ResponseEntity>()
                            {
                            @Override
                            public void onSuccess(@NonNull ResponseEntity userResource)
                                {
                                mStatusCode.setValue(Resource.success(userResource));
                                Log.e(TAG, "finishPassword:SUCCESS");

                                }

                            @Override
                            public void onError(@NonNull Throwable e)
                                {
                                mCompositeDisposable.remove(this);
                                try
                                    {

                                    var message =
                                            ((HttpException) e).response().errorBody().string();

                                    mStatusCode.setValue(Resource.error(message, null));

                                    Log.e(TAG, "finishPassword: "
                                            + ((HttpException) e).response().errorBody().string());
                                    } catch (IOException ex)
                                    {
                                    ex.printStackTrace();
                                    }


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
