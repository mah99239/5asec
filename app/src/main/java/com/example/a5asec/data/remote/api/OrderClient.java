package com.example.a5asec.data.remote.api;

import com.example.a5asec.data.model.api.Order;

import java.util.List;

import io.reactivex.rxjava3.core.Single;

public class OrderClient extends Client implements OrderApi
    {
    private static OrderClient INSTANCE;
    private OrderApi mOrderApi;

    public OrderClient()
        {
        mOrderApi = getRetrofit().create(OrderApi.class);
        }

    public static OrderClient getINSTANCE()
        {
        return null == INSTANCE ? new OrderClient() : INSTANCE;
        }

    @Override
    public Single<List<Order>> orderHistories()
        {
        return mOrderApi.orderHistories();
        }

    @Override
    public   Single<List<Order>> createOrder( Order order)
        {
        return mOrderApi.createOrder(order);
        }

    @Override
   public Single<List<Order>> currentOrder()
        {
        return mOrderApi.currentOrder();
        }
    }
