package com.example.a5asec.ui.base;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.a5asec.data.repository.CartRepository;
import com.example.a5asec.ui.view.viewmodel.CartViewModel;

import javax.inject.Inject;

/**
 * Factory for ViewModels
 */
public class CartViewModelFactory implements ViewModelProvider.Factory
{

    private final CartRepository cartRepository;

    @Inject
    public CartViewModelFactory(CartRepository cartRepository)
    {
        this.cartRepository = cartRepository;
    }

    @Override
    @NonNull
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass)
    {
        if (modelClass.isAssignableFrom(CartViewModel.class)) {
            return (T) new CartViewModel(cartRepository);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
