package com.example.a5asec.data.remote.api;

import com.example.a5asec.data.model.api.Terms;

import java.util.List;

import io.reactivex.rxjava3.core.Observable;
import retrofit2.http.GET;

public interface TermsApi
    {

    @GET("api/terms-categories")
    Observable<List<Terms>> getTerms();
    }
