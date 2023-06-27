package com.example.a5asec.data.remote.api;

import com.example.a5asec.data.model.api.Order;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Single;

public class OrderClient implements OrderApi
{
    private final OrderApi mOrderApi;

    @Inject
    public OrderClient(OrderApi orderApi)
    {
        mOrderApi = orderApi;
    }


    @Override
    public Single<List<Order>> orderHistories()
    {
        return mOrderApi.orderHistories();
    }

    @Override
    public Single<List<Order>> createOrder(Order order)
    {
        return mOrderApi.createOrder(order);
    }

    @Override
    public Single<List<Order>> currentOrder()
    {
        return mOrderApi.currentOrder();
    }
}
