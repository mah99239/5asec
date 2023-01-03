package com.example.a5asec.data.remote.api;

import com.example.a5asec.data.model.api.Order;

import java.util.List;

import io.reactivex.rxjava3.core.Single;

public class OrderHelper
    {
    private OrderApi mOrderApi;

    public OrderHelper(OrderApi orderApi)
        {
        this.mOrderApi = orderApi;
        }


    public Single<List<Order>> orderHistories()
        {
        return mOrderApi.orderHistories();
        }


    public   Single<List<Order>> createOrder( Order order)
        {
        return mOrderApi.createOrder(order);
        }


    public Single<List<Order>> currentOrder()
        {
        return mOrderApi.currentOrder();
        }

    }
