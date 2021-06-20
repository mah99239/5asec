 package com.example.a5asec.data.api;

import android.content.Context;
import com.example.a5asec.data.model.Authorization;

import okhttp3.OkHttpClient;

import org.jetbrains.annotations.NotNull;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public abstract class Client
{
  private static final String BASE_URL = "https://stg.5asec-ksa.com/";


  protected static Retrofit getRetrofit()
  {
    //TokenAuthenticator tokenAuthenticator = new TokenAuthenticator();
    TokenInterceptor tokenInterceptor = new TokenInterceptor();
    OkHttpClient okHttpClient = UnsafeOkHttpClient.getUnsafeOkHttpClient();
    OkHttpClient client = okHttpClient.newBuilder()
           .addInterceptor(tokenInterceptor)
        //  .authenticator(tokenAuthenticator)
        .build();

    return new Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build();
  }

  @NotNull public static Retrofit getRetrofitAdapter()
  {
    OkHttpClient okHttpClient = UnsafeOkHttpClient.getUnsafeOkHttpClient();
    OkHttpClient client = okHttpClient.newBuilder()
        .build();

    return new Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
        .build();
  }
}


