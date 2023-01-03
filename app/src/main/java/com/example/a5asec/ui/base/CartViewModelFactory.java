package com.example.a5asec.ui.base;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.a5asec.data.local.db.dao.OrderDataSource;
import com.example.a5asec.ui.view.viewmodel.CartViewModel;

/**
 * Factory for ViewModels
 */
public class CartViewModelFactory implements ViewModelProvider.Factory
    {

    private final OrderDataSource mOrderDataSource;

    public CartViewModelFactory(OrderDataSource dataSource)
        {
        mOrderDataSource = dataSource;
        }

    @Override
    @NonNull
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass)
        {
        if (modelClass.isAssignableFrom(CartViewModel.class))
            {
            return (T) new CartViewModel(mOrderDataSource);
            }
//noinspection unchecked
        throw new IllegalArgumentException("Unknown ViewModel class");
        }
    }
