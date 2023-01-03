package com.example.a5asec.data.remote.api;

import com.example.a5asec.data.model.api.Terms;

import java.util.List;

import io.reactivex.rxjava3.core.Observable;


public class TermsClient extends Client implements TermsApi
    {
    private static TermsClient INSTANCE;

    private final TermsApi mTermsApi;

    public TermsClient() {
        mTermsApi = getRetrofitAdapter().create(TermsApi.class);
    }

    public static TermsClient getINSTANCE() {
        return null == INSTANCE ? new TermsClient() : INSTANCE;
    }

    @Override
    public Observable<List<Terms>> getTerms() {
        return mTermsApi.getTerms();
    }
}