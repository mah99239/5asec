package com.example.a5asec.data.api;

import com.example.a5asec.data.model.Terms;
import io.reactivex.rxjava3.core.Single;
import java.util.List;
import retrofit2.http.GET;

public interface TermsInterface
{
  @GET("api/terms-categories")
  Single<List<Terms>> getTerms();
}
