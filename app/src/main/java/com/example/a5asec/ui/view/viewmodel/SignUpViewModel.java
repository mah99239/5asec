package com.example.a5asec.ui.view.viewmodel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.a5asec.data.remote.api.ResponseEntity;
import com.example.a5asec.data.remote.api.UserClient;
import com.example.a5asec.data.remote.api.UserHelper;
import com.example.a5asec.data.model.api.RegistrationDTO;
import com.example.a5asec.utility.Resource;

import java.io.IOException;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.observers.DisposableSingleObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;
import retrofit2.HttpException;
import timber.log.Timber;

public class SignUpViewModel extends ViewModel
    {
    private static final String TAG = "SignUpViewModel";

    private  MutableLiveData<Resource<ResponseEntity>> mStatusCode ;
    private final CompositeDisposable mCompositeDisposable = new CompositeDisposable();
    private UserHelper mUserHelper = new UserHelper(new UserClient());


    public SignUpViewModel()
        {

        }
    public void setRegisterUser(RegistrationDTO registerUser)
        {
        registerUsers(registerUser);
        }

    public LiveData<Resource<ResponseEntity>> getStatusCode()
        {
        return mStatusCode;
        }
    public void registerUsers(RegistrationDTO user)
        {
        mStatusCode = new MutableLiveData<>();
        mCompositeDisposable.add(
                mUserHelper.registerUsers(user)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())

                        .subscribeWith(new DisposableSingleObserver<ResponseEntity>()
                            {
                            @Override
                            public void onSuccess(@NonNull ResponseEntity userResource)
                                {
                                mStatusCode.setValue(Resource.success(userResource));
                                Timber.tag(TAG).e("registerUsers:SUCCESS");
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

    @Override
    protected void onCleared()
        {
        super.onCleared();
        mCompositeDisposable.clear();
        }
    }
