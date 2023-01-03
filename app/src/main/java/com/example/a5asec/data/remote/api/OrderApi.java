package com.example.a5asec.data.remote.api;

import com.example.a5asec.data.model.api.Order;

import java.util.List;

import io.reactivex.rxjava3.core.Single;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface OrderApi
    {

    @POST("api/orders")
    Single<List<Order>> createOrder(@Body Order order);

    @GET("/api/orders/mine/old")
    Single<List<Order>> orderHistories();

    @GET("api/orders/mine/current")
    Single<List<Order>> currentOrder();

    }
