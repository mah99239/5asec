package com.example.a5asec.ui.base;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.a5asec.data.remote.api.CategoryHelper;
import com.example.a5asec.data.repository.CategoryRepository;
import com.example.a5asec.ui.view.viewmodel.CategoryViewModel;

public class CategoryViewModelFactory implements ViewModelProvider.Factory
    {
    private CategoryHelper mCategoryHelper;

    public CategoryViewModelFactory(CategoryHelper categoryHelper)
        {
        this.mCategoryHelper = categoryHelper;
        }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass)
        {
        if (modelClass.isAssignableFrom(CategoryViewModel.class))
            {
            return (T) new CategoryViewModel(new CategoryRepository(mCategoryHelper));
            }
        throw new IllegalArgumentException("UnKnown class name");
        }

    }