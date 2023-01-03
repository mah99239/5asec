package com.example.a5asec.ui.base;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.a5asec.data.remote.api.AddressHelper;
import com.example.a5asec.data.repository.AddressRepository;
import com.example.a5asec.ui.view.viewmodel.AddressViewModel;

public class AddressViewModelFactory implements ViewModelProvider.Factory
    {
    private AddressHelper mAddressHelper;

    public AddressViewModelFactory(AddressHelper addressHelper)
        {
        this.mAddressHelper = addressHelper;
        }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass)
        {
        if (modelClass.isAssignableFrom(AddressViewModel.class))
            {
            return (T) new AddressViewModel(new AddressRepository(mAddressHelper));
            }
        throw new IllegalArgumentException("UnKnown class name with AddressViewModel");
        }
    }
