package com.example.a5asec.ui.base;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.a5asec.data.remote.api.OrderHelper;
import com.example.a5asec.data.repository.OrderRepository;
import com.example.a5asec.ui.view.viewmodel.OrderViewModel;

public class OrderViewModelFactory implements ViewModelProvider.Factory {
private final OrderHelper mOrderHistoryHelper;

public OrderViewModelFactory(OrderHelper orderHistoryHelper) {
        this.mOrderHistoryHelper = orderHistoryHelper;
        }

@NonNull
@Override
public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(OrderViewModel.class)) {
        return (T) new OrderViewModel(new OrderRepository(mOrderHistoryHelper));
        }
        throw new IllegalArgumentException("UnKnown class name");
        }
        }

