package com.example.a5asec.data.remote.api;

import com.example.a5asec.data.model.api.Setting;

import io.reactivex.rxjava3.core.Observable;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PUT;

public interface SettingApi
    {
    @GET("api/application-settings")
    Observable<Setting> getSetting();

    @PUT("api/application-settings")
    Observable<Setting> setSetting(@Body Setting setting);


    }
