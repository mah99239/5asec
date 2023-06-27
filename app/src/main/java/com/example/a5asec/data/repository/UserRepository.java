package com.example.a5asec.data.repository;

import com.example.a5asec.data.remote.api.ResponseEntity;
import com.example.a5asec.data.remote.api.UserHelper;
import com.example.a5asec.data.model.api.Authorization;
import com.example.a5asec.data.model.api.Users;

import io.reactivex.rxjava3.core.Single;
import okhttp3.RequestBody;

public class UserRepository
    {
    private static final String TAG = "UserRepository";
    private final UserHelper mUserHelper;

    public UserRepository(UserHelper userHelper)
        {
        this.mUserHelper = userHelper;
        }

    public Single<Users> getAccount()
        {

        return mUserHelper.getAccount();
        }


    public Single<ResponseEntity> activateMobile(String key)
        {
        return mUserHelper.activateMobile(key);

        }


    public Single<Authorization> authenticateUsers(Users users)
        {
        return mUserHelper.authenticateUsers(users);
        }

    public Single<Users> changeLanguage(String language)
        {
        return mUserHelper.changeLanguage(language);
        }

    public Single<Users> changeMobile(String mobile)
        {
        return mUserHelper.changeMobile(mobile);

        }

    public Single<Users> changeNotification(boolean notification)
        {
        return mUserHelper.changeNotification(notification);
        }

    public Single<ResponseEntity> changePassword(String password)
        {
        return mUserHelper.changePassword(password);

        }

    public Single<Authorization.AuthorizationEntity> refreshToken(RequestBody token)
        {
        return mUserHelper.refreshToken(token);
        }



    }