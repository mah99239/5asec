package com.example.a5asec.data.repository;

import com.example.a5asec.data.remote.api.TermsHelper;
import com.example.a5asec.data.model.api.Terms;

import java.util.List;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;

public class TermsRepository {
private static final String TAG = "TermsRepository";
    private TermsHelper mTermsHelper;


    public TermsRepository(TermsHelper apiHelper) {
        this.mTermsHelper = apiHelper;
    }
    @NonNull
    public Observable<List<Terms>> getTerms() {
  //  Log.d(TAG,"getTerms:" + String.valueOf(apiHelper.getTerms()));
        return mTermsHelper.getTerms();
    }
}
