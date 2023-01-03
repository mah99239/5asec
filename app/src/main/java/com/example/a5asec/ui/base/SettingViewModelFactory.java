package com.example.a5asec.ui.base;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.a5asec.data.remote.api.SettingHelper;
import com.example.a5asec.data.repository.SettingRepository;
import com.example.a5asec.ui.view.viewmodel.SettingViewModel;

public class SettingViewModelFactory implements ViewModelProvider.Factory
    {
    private SettingHelper mSettingHelper;

    public SettingViewModelFactory(SettingHelper settingHelper)
        {
        this.mSettingHelper = settingHelper;
        }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass)
        {
        if (modelClass.isAssignableFrom(SettingViewModel.class))
            {
            return (T) new SettingViewModel(new SettingRepository(mSettingHelper));
            }
        throw new IllegalArgumentException("UnKnown class name");
        }
    }
