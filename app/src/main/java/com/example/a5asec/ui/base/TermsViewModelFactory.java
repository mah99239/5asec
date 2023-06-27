package com.example.a5asec.ui.base;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.a5asec.data.remote.api.TermsHelper;
import com.example.a5asec.data.repository.TermsRepository;
import com.example.a5asec.ui.view.viewmodel.TermsViewModel;

public class TermsViewModelFactory implements ViewModelProvider.Factory {
    private final TermsHelper mTermsHelper;

    public TermsViewModelFactory(TermsHelper termsHelper) {
        this.mTermsHelper = termsHelper;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(TermsViewModel.class)) {
            return (T) new TermsViewModel(new TermsRepository(mTermsHelper));
        }
        throw new IllegalArgumentException("UnKnown class name");
    }
}
