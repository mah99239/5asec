package com.example.a5asec.ui.view.viewmodel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.a5asec.data.model.api.Order;
import com.example.a5asec.data.repository.OrderRepository;
import com.example.a5asec.utility.ApiError;
import com.example.a5asec.utility.Resource;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.observers.DisposableSingleObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class OrderViewModel extends ViewModel
    {
    private static final String TAG = "OrderHistoryViewModel";
    private MutableLiveData<Resource<List<Order>>> orderHistory = new MutableLiveData<>();
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();
    private OrderRepository mOrderRepository;

    public OrderViewModel()
        {
        }

    public OrderViewModel(OrderRepository orderRepository)
        {

        mOrderRepository = orderRepository;
        fetchOrderHistories();
        }

    public LiveData<Resource<List<Order>>> getOrderHistory()
        {
        return orderHistory;
        }


    private void fetchOrderHistories()
        {

        orderHistory.postValue(Resource.loading(null));
        compositeDisposable.add(
                mOrderRepository.orderHistories()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())

                        .subscribeWith(new DisposableSingleObserver<List<Order>>()
                            {


                            @Override
                            public void onSuccess(@NonNull List<Order> orderHistories)
                                {
                                orderHistory.postValue(Resource.success(orderHistories));
                                compositeDisposable.remove(this);


                                }

                            @Override
                            public void onError(@NonNull Throwable e)
                                {
                                Log.e(TAG, ApiError.handleApiError(e));
                                orderHistory.postValue(Resource.error(ApiError.handleApiError(e), null));
                                compositeDisposable.remove(this);

                                }


                            }));


        }

    @Override
    protected void onCleared()
        {
        super.onCleared();
        compositeDisposable.clear();
        compositeDisposable.dispose();
        }

    }
