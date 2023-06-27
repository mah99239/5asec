package com.example.a5asec.ui.view.viewmodel;

import android.util.ArrayMap;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.a5asec.data.local.prefs.TokenPreferences;
import com.example.a5asec.data.model.api.Authorization;
import com.example.a5asec.data.model.api.Users;
import com.example.a5asec.data.remote.api.ResponseEntity;
import com.example.a5asec.data.repository.UserRepository;
import com.example.a5asec.utility.ApiError;
import com.example.a5asec.utility.Resource;

import org.json.JSONObject;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.exceptions.CompositeException;
import io.reactivex.rxjava3.observers.DisposableSingleObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;
import okhttp3.RequestBody;
import timber.log.Timber;

public class UserViewModel extends ViewModel
{
    private static final String TAG = "UserViewModel";
    private final MutableLiveData<Resource<Users>> mUser = new MutableLiveData<>();
    private final MutableLiveData<Users> mLoginUser = new MutableLiveData<>();
    private final CompositeDisposable mCompositeDisposable = new CompositeDisposable();
    private final MutableLiveData<Resource<Authorization.AuthorizationEntity>> authorization = new MutableLiveData<>();
    @Inject
    TokenPreferences tokenPreferences;
    private MutableLiveData<Resource<ResponseEntity>> mStatusCode;
    private UserRepository mUserRepository;

    public UserViewModel() {
        //IMPROVE: add dagger to handle UI and use singleton
        //https://amitshekhar.me/blog/mvvm-architecture-android
    }

    public UserViewModel(UserRepository userRepository) {

        mUserRepository = userRepository;
        //TODO: test refesh Token
    }

    public LiveData<Resource<Authorization.AuthorizationEntity>> getAuthenticateUsers() {

        authenticateUsers();
        return authorization;
    }

    public void setLogin(Users user) {
        mLoginUser.setValue(user);
    }


    public LiveData<Resource<Users>> getUser() {
        fetchUser();
        return mUser;
    }

    /**
     * called when user click login
     */
    private void authenticateUsers() {
        String login = mLoginUser.getValue().getLogin();
        String password = mLoginUser.getValue().getPassword();

        mCompositeDisposable.add(
                mUserRepository.authenticateUsers(new Users(login, password))
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())

                        .subscribeWith(
                                new DisposableSingleObserver<Authorization>()
                                {
                                    @Override
                                    protected void onStart() {
                                        super.onStart();
                                        authorization.postValue(
                                                Resource.loading(null));

                                    }

                                    @Override
                                    public void onSuccess(@NonNull Authorization authorizationResource) {
                                        authorization.postValue(
                                                Resource.success(
                                                        authorizationResource.getAuthorization()));
                                        mUser.postValue(Resource.success(
                                                authorizationResource.getUser()));
                                        mCompositeDisposable.remove(this);

                                    }

                                    @Override
                                    public void onError(@NonNull Throwable e) {
                                        authorization.postValue(Resource.error(
                                                ApiError.handleApiError(e),
                                                null));
                                        mCompositeDisposable.remove(this);

                                    }
                                }));

    }

    private void fetchUser() {
        mCompositeDisposable.add(
                mUserRepository.getAccount().subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnTerminate(this::checkRefreshToken)
                        .subscribeWith(new DisposableSingleObserver<Users>()
                        {
                            @Override
                            public void onSuccess(@NonNull Users userResource) {
                                mUser.postValue(Resource.success(userResource));

                                Timber.tag(TAG).e("success in fetchUser");

                            }

                            @Override
                            public void onError(@NonNull Throwable e) {
                                if (e instanceof CompositeException compositeException) {
                                    List<Throwable> exceptions = compositeException.getExceptions();
                                    for (Throwable throwable : exceptions) {
                                        Timber.tag(TAG).e(throwable,
                                                "Error in fetchUser with Composite ");

                                        // Handle each exception individually
                                    }
                                } else {
                                    // Handle the error
                                    Timber.tag(TAG).e(e, "Error  fetchUser");
                                    mUser.postValue(Resource.error(
                                            ApiError.handleApiError(e), null));
                                }


                            }
                        }));


    }

    public void checkRefreshToken() {
        Timber.tag(TAG).e("result Time = %s", resultTime());

        Observable.timer(resultTime(), TimeUnit.MILLISECONDS)
                .subscribe(new Observer<>()
                {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        Timber.tag(TAG).e("checkRefreshToken() onSubscribe - ");

                    }

                    @Override
                    public void onNext(@NonNull Long aLong) {
                        Timber.tag(TAG)
                                .e("checkRefreshToken() onNext = " + "o = " + aLong + ", = " + resultTime());
                        refreshTokens();
                    }


                    @Override
                    public void onError(@NonNull Throwable e) {
                        Timber.tag(TAG).e("checkRefreshToken() onError - ");

                    }

                    @Override
                    public void onComplete() {
                        Timber.tag(TAG).e("checkRefreshToken() onComplete - ");

                    }
                });


     /*    if (resultTime() < 0) // resultTime = -1
        {
        //save refreshed token's expire time:
        refreshToken();

        } */
    }

    private void refreshTokens() {


        Timber.tag(TAG).e("refreshToken() - preference  %s", tokenPreferences);

        Map<String, Object> jsonParams = new ArrayMap<>();
        String refreshToken = tokenPreferences.getRefreshToken();
        jsonParams.put("refreshToken", refreshToken);
        RequestBody body = RequestBody.create(
                okhttp3.MediaType.parse("application/json; charset=utf-8"),
                (new JSONObject(jsonParams)).toString());

        mUser.postValue(Resource.loading(null));
        mCompositeDisposable.add(
                mUserRepository.refreshToken(body).subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())

                        .subscribeWith(
                                new DisposableSingleObserver<Authorization.AuthorizationEntity>()
                                {
                                    @Override
                                    public void onSuccess(@NonNull Authorization.AuthorizationEntity tokenResource) {
                                        tokenPreferences.setToken(
                                                tokenResource.getAccess_token(),
                                                tokenResource.getRefresh_token(),
                                                tokenResource.getExpires_in());
                                        Timber.tag(TAG)
                                                .e("refreshToken() - onSuccess ");


                                    }

                                    @Override
                                    public void onError(@NonNull Throwable e) {
                                        Timber.tag(TAG)
                                                .e("refreshToken() - onError :%s",
                                                        e.getMessage());
                                        Timber.tag(TAG)
                                                .e("refreshToken() - onError = %s",
                                                        e);
                                        authorization.postValue(
                                                Resource.error("masse", null));

                                    }
                                }));

    }

    /**
     * result time called when observable is run.
     * test case:
     * current time >= expireTime return 0
     * current time < expireTime  return milliSecond
     *
     * @return milliSecond
     */
    private long resultTime() {

        long expireTime = (tokenPreferences.getExpiresIn() != 0) ? tokenPreferences.getExpiresIn() : 0;//get expireTime with milliSecond

        Timber.tag(TAG).e("resultTime: expireTime = %s", expireTime);
        Date expireDate = new Date();
        expireDate.setTime(expireTime);
        Date nowDate = new Date();


        // 23.9H to milliSecond = 86399000
        long defaultTime = 0L;
        Timber.tag(TAG)
                .e("resultTime: current time =" + nowDate + "expire time = " + expireDate);
        return (expireDate.getTime() <= nowDate.getTime()) ? defaultTime : expireDate.getTime() - nowDate.getTime();

    }


    public void changeLanguage(String language) {
        Timber.tag(TAG).e(" changeLanguage:" + language.toUpperCase());
        mCompositeDisposable.add(
                mUserRepository.changeLanguage(language.toUpperCase())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new DisposableSingleObserver<Users>()
                        {
                            @Override
                            public void onSuccess(@NonNull Users userResource) {
                                mUser.postValue(Resource.success(userResource));
                                Log.e(TAG, "SUCESS changeLanguage");


                            }

                            @Override
                            public void onError(@NonNull Throwable e) {
                                mUser.postValue(Resource.error(
                                        ApiError.handleApiError(e), null));
                                mCompositeDisposable.remove(this);
                                Timber.tag(TAG)
                                        .e("error changeLanguage" + e.getMessage());
                                Timber.tag(TAG).e("error changeLanguage" + e);
                                Timber.tag(TAG)
                                        .e("error changeLanguage" + e.getLocalizedMessage());
                                Timber.tag(TAG)
                                        .e("error changeLanguage" + e.getStackTrace());

                            }
                        }));
    }

    public void changeNotification(boolean notification) {
        mCompositeDisposable.add(
                mUserRepository.changeNotification(notification)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .delay(3, TimeUnit.SECONDS)

                        // .toObservable().debounce(2, TimeUnit.SECONDS)
                        .subscribeWith(new DisposableSingleObserver<Users>()
                        {
                            @Override
                            public void onSuccess(@NonNull Users userResource) {
                                mUser.postValue(Resource.success(userResource));
                                Timber.tag(TAG).e("ChangeNotification:SUCCESS");

                            }

                            @Override
                            public void onError(@NonNull Throwable e) {
                                mUser.postValue(Resource.error(
                                        ApiError.handleApiError(e), null));
                                mCompositeDisposable.remove(this);
                                Timber.tag(TAG)
                                        .e("error,ChangeNotification" + e.getMessage());
                                Timber.tag(TAG)
                                        .e("error ChangeNotification" + e);
                                Timber.tag(TAG)
                                        .e("error ChangeNotification" + e.getLocalizedMessage());
                            }
                        }));

    }

    public LiveData<Resource<ResponseEntity>> getStatusCode(String password) {
        changePassword(password);
        return mStatusCode;
    }

    public void changePassword(String Password) {
        mCompositeDisposable.add(mUserRepository.changePassword(Password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<ResponseEntity>()
                {
                    @Override
                    public void onSuccess(@NonNull ResponseEntity userResource) {
                        mStatusCode.postValue(Resource.success(userResource));
                        Log.e(TAG, "SUCESS changePassword");

                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        mStatusCode.postValue(
                                Resource.error(ApiError.handleApiError(e),
                                        null));
                        mCompositeDisposable.remove(this);
                        Log.e(TAG, "error changePassword" + e.getMessage());


                    }
                }));
    }


    @Override
    protected void onCleared() {
        super.onCleared();
        mCompositeDisposable.clear();
    }


}
