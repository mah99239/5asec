package com.example.a5asec.ui.base;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.a5asec.data.repository.BannersRepository;
import com.example.a5asec.ui.view.viewmodel.BannersViewModel;

import javax.inject.Inject;

public class BannersViewModelFactory implements ViewModelProvider.Factory
{
    private final BannersRepository mBannersRepository;

    @Inject
    public BannersViewModelFactory(BannersRepository bannersRepository)
    {
        this.mBannersRepository = bannersRepository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass)
    {
        if (modelClass.isAssignableFrom(BannersViewModel.class)) {
            return (T) new BannersViewModel(mBannersRepository);
        }
        throw new IllegalArgumentException("UnKnown class name");
    }
}
