package com.example.a5asec.ui.base;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.a5asec.data.remote.api.BannersHelper;
import com.example.a5asec.data.repository.BannersRepository;
import com.example.a5asec.ui.view.viewmodel.BannersViewModel;

public class BannersViewModelFactory implements ViewModelProvider.Factory
    {
    private BannersHelper mBannersHelper;

    public BannersViewModelFactory(BannersHelper bannersHelper)
        {
        this.mBannersHelper = bannersHelper;
        }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass)
        {
        if (modelClass.isAssignableFrom(BannersViewModel.class))
            {
            return (T) new BannersViewModel(new BannersRepository(mBannersHelper));
            }
        throw new IllegalArgumentException("UnKnown class name");
        }
    }
