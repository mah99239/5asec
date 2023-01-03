package com.example.a5asec.ui.view.viewmodel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.a5asec.data.model.api.Terms;
import com.example.a5asec.data.repository.TermsRepository;
import com.example.a5asec.utility.ApiError;
import com.example.a5asec.utility.Resource;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.observers.DisposableObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;

/**
 * Author: Mahmoud98
 */

public class TermsViewModel extends ViewModel
    {
    private static final String TAG = "TermsViewModel";
    public MutableLiveData<Resource<List<Terms>>> terms = new MutableLiveData<>();
    private  CompositeDisposable compositeDisposable = new CompositeDisposable();
    private TermsRepository mTermsRepository;

    public TermsViewModel()
        {
        }

    public TermsViewModel(TermsRepository termsRepository)
        {

        mTermsRepository = termsRepository;
        this.fetchTerms();
        }

    public LiveData<Resource<List<Terms>>> getTerms()
        {
        return terms;
        }

    private void fetchTerms()
        {

        terms.postValue(Resource.loading(null));
        compositeDisposable.add(
                mTermsRepository.getTerms()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())

                        .subscribeWith(new DisposableObserver<List<Terms>>()
                            {
                            @Override
                            public void onNext(@NonNull List<Terms> listResource)
                                {
                                terms.postValue(Resource.success(listResource));
                                dispose();
                                }

                            @Override
                            public void onError(@NonNull Throwable e)
                                {
                                Log.e(TAG, ApiError.handleApiError(e));
                                terms.postValue(Resource.error(ApiError.handleApiError(e), null));

                                }

                            @Override
                            public void onComplete()
                                {
                                // TODO document why this method is empty
                                }
                            }));


        }

    @Override
    protected void onCleared()
        {
        super.onCleared();
        compositeDisposable.clear();
        compositeDisposable.dispose();
        }



    }
