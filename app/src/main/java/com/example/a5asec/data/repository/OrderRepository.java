package com.example.a5asec.data.repository;

import com.example.a5asec.data.model.api.Order;
import com.example.a5asec.data.remote.api.OrderHelper;

import java.util.List;

import io.reactivex.rxjava3.core.Single;

public class OrderRepository
    {
    private static final String TAG = "OrderRepository";
    private OrderHelper mOrderHelper;


    public OrderRepository(OrderHelper orderHelper)
        {
        this.mOrderHelper = orderHelper;
        }

    public Single<List<Order>> orderHistories()
        {
        return mOrderHelper.orderHistories();
        }


    public   Single<List<Order>> createOrder( Order order)
        {
        return mOrderHelper.createOrder(order);
        }


    public Single<List<Order>> currentOrder()
        {
        return mOrderHelper.currentOrder();
        }

    }
