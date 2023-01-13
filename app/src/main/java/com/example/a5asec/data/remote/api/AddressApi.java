package com.example.a5asec.data.remote.api;

import com.example.a5asec.data.model.api.Address;
import com.example.a5asec.data.model.api.City;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface AddressApi
    {
    @GET("api/addresses/mine")
    Observable<List<Address>> getAddress();

    @GET("api/cities")
    Observable<List<City>> getCities();

    @DELETE("/api/addresses/{id}")
    Completable deleteAddress(@Path("id") long id);

    @POST("api/addresses")
    Single<Address> addAddress(@Body Address.CreateAddress address);

    @PUT("api/addresses")
    Single<Address> updateAddress(@Body Address.UpdateAddress address);

    @PUT("api/addresses/primary/{id}")
    Single<List<Address>> primaryAddress(@Path("id") long id);
    }
