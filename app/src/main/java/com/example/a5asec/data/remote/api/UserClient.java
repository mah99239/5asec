package com.example.a5asec.data.remote.api;

import com.example.a5asec.data.model.api.Authorization;
import com.example.a5asec.data.model.api.RegistrationDTO;
import com.example.a5asec.data.model.api.Users;

import io.reactivex.rxjava3.core.Single;
import okhttp3.RequestBody;
import retrofit2.http.Body;

public class UserClient extends Client implements UserApi
    {

    private static UserClient INSTANCE;
    private final UserApi mUserApi;
    private final UserApi mUserApiToken;
    private final UserApi mUserApiConverterScalar;

    public UserClient()
        {
        mUserApi = getRetrofitAdapter().create(UserApi.class);
        mUserApiToken = getRetrofit().create(UserApi.class);
        mUserApiConverterScalar = getRetrofitConverterScalar().create(UserApi.class);

        }

    public static UserClient getInstance()
        {
        return null == INSTANCE ? new UserClient() : INSTANCE;
        }


    @Override
    public Single<Users> getAccount()
        {

        return mUserApiToken.getAccount();
        }

    @Override
    public Single<ResponseEntity> activateMobile(String key)
        {
        return mUserApiConverterScalar.activateMobile(key);

        }


    @Override
    public Single<Authorization> authenticateUsers(Users users)
        {
        return mUserApi.authenticateUsers(users);
        }

    @Override
    public Single<Users> changeLanguage(@Body String language)
        {

        return mUserApiConverterScalar.changeLanguage(language);
        }

    @Override
    public Single<Users> changeMobile(String mobile)
        {
        return mUserApiConverterScalar.changeMobile(mobile);

        }

    @Override
    public Single<Users> changeNotification(boolean notification)
        {
        return mUserApiToken.changeNotification(notification);
        }

    @Override
    public Single<ResponseEntity> changePassword(String password)
        {
        return mUserApiConverterScalar.changePassword(password);

        }

    @Override
    public Single<Authorization.AuthorizationEntity> refreshToken(RequestBody token)
        {
        return mUserApi.refreshToken(token);
        }

    @Override
    public Single<ResponseEntity> registerUsers(RegistrationDTO user)
        {
        return mUserApi.registerUsers(user);
        }

    @Override
    public Single<ResponseEntity> finishPassword(String key, String password)
        {
        return mUserApi.finishPassword(key, password);
        }
    @Override
    public Single<ResponseEntity> restPassword(String email)
        {
        return mUserApi.restPassword(email);
        }


    }

