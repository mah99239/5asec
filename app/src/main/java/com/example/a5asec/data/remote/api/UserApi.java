package com.example.a5asec.data.remote.api;

import com.example.a5asec.data.model.api.Authorization;
import com.example.a5asec.data.model.api.RegistrationDTO;
import com.example.a5asec.data.model.api.Users;

import io.reactivex.rxjava3.core.Single;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface UserApi
    {
    @GET("api/client/account")
    Single<Users> getAccount();

    @POST("api/client/account/activate-mobile/finish/{key}")
    Single<ResponseEntity> activateMobile(@Path("key") String key);


    @POST("api/client/account/authenticate")
    Single<Authorization> authenticateUsers(@Body Users users);


    @PUT("api/client/account/change-language")
    Single<Users> changeLanguage(@Body String langKey);

    @PUT("api/client/account/change-mobile")
    Single<Users> changeMobile(@Body String mobile);


    @PUT("api/client/account/change-notification-acceptance")
    Single<Users> changeNotification(@Body boolean acceptNotifications);

    @PUT("api/client/account/change-password")
    Single<ResponseEntity> changePassword(@Body String password);

    @POST("api/client/account/refresh")
    Single<Authorization.AuthorizationEntity> refreshToken(@Body RequestBody refreshToken);

    @POST("api/client/account/register")
    Single<ResponseEntity> registerUsers(@Body RegistrationDTO user);

    @Multipart
    @POST("api/client/account/reset-password/finish")
    Single<ResponseEntity> finishPassword(@Part("key") String key, @Part("newPassword")  String password);

    @POST("api/client/account/reset-password/init")
    Single<ResponseEntity> restPassword(@Body String email);


    }
