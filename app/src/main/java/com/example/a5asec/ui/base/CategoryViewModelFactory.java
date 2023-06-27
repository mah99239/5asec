package com.example.a5asec.ui.base;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.a5asec.data.repository.CategoryRepository;
import com.example.a5asec.ui.view.viewmodel.CategoryViewModel;

import javax.inject.Inject;

public class CategoryViewModelFactory implements ViewModelProvider.Factory
{
    private final CategoryRepository mCategoryRepository;

    @Inject
    public CategoryViewModelFactory(CategoryRepository categoryRepository)
    {
        this.mCategoryRepository = categoryRepository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass)
    {
        if (modelClass.isAssignableFrom(CategoryViewModel.class)) {
            return (T) new CategoryViewModel(mCategoryRepository);
        }
        throw new IllegalArgumentException("UnKnown class name");
    }

}