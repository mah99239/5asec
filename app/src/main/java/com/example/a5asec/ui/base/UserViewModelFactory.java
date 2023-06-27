package com.example.a5asec.ui.base;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.a5asec.data.remote.api.UserHelper;
import com.example.a5asec.data.repository.UserRepository;
import com.example.a5asec.ui.view.viewmodel.UserViewModel;

public class UserViewModelFactory implements ViewModelProvider.Factory {
private final UserHelper mUserHelper;

public UserViewModelFactory(UserHelper userHelper) {
        this.mUserHelper = userHelper;
        }

@NonNull
@Override
public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(UserViewModel.class)) {
        return (T) new UserViewModel(new UserRepository(mUserHelper));
        }
        throw new IllegalArgumentException("UnKnown class name");
        }
    }
