package com.example.a5asec.data.remote.api;

import com.example.a5asec.data.model.api.Authorization;
import com.example.a5asec.data.model.api.RegistrationDTO;
import com.example.a5asec.data.model.api.Users;

import io.reactivex.rxjava3.core.Single;
import okhttp3.RequestBody;


public class UserHelper
    {
    private final UserApi mUserApi;

    public UserHelper(UserApi userApi)
        {
        this.mUserApi = userApi;
        }

    public Single<Users> getAccount()
        {

        return mUserApi.getAccount();
        }


    public Single<ResponseEntity> activateMobile(String key)
        {
        return mUserApi.activateMobile(key);

        }


    public Single<Authorization> authenticateUsers(Users users)
        {
        return mUserApi.authenticateUsers(users);
        }

    public Single<Users> changeLanguage(  String language)
        {

        return mUserApi.changeLanguage(language);
        }

    public Single<Users> changeMobile(String mobile)
        {
        return mUserApi.changeMobile(mobile);

        }

    public Single<Users> changeNotification(boolean notification)
        {
        return mUserApi.changeNotification(notification);
        }

    public Single<ResponseEntity> changePassword(String password)
        {
        return mUserApi.changePassword(password);

        }

    public Single<Authorization.AuthorizationEntity> refreshToken(RequestBody token)
        {
        return mUserApi.refreshToken(token);
        }

    public Single<ResponseEntity> registerUsers(RegistrationDTO user)
        {
        return mUserApi.registerUsers(user);
        }

    public Single<ResponseEntity> finishPassword(String key, String password)
        {
        return mUserApi.finishPassword(key, password);
        }

    public Single<ResponseEntity> restPassword(String email)
        {
        return mUserApi.restPassword(email);
        }
    }