package com.example.a5asec.data.remote.api;


import androidx.annotation.NonNull;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public abstract class Client
    {
    private static final String TAG = "Client";
    private static final String BASE_URL = "https://stg.5asec-ksa.com/";

    Client()
        {
        //cache url
     /*    File httpCacheDirectory = new File(MyApp.getContext().getCacheDir(), "responses");
        int cacheSize = 5 * 1024 * 1024; // 5 MiB
        Cache cache = new Cache(httpCacheDirectory, cacheSize); */
        }

    @NonNull
    protected static Retrofit getRetrofit()
        {
        var tokenInterceptor = new TokenInterceptor();
        var okHttpClient = UnsafeOkHttpClient.getUnsafeOkHttpClient();

        var client = okHttpClient.newBuilder()
                .addInterceptor(tokenInterceptor)
                //   .cache(cache)
                .connectTimeout(1, TimeUnit.MINUTES)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(15, TimeUnit.SECONDS)
                .build();

        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)

                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())

                .build();
        }

    @NonNull
    protected static Retrofit getRetrofitConverterScalar()
        {
        var tokenInterceptor = new TokenInterceptor();
        var okHttpClient = UnsafeOkHttpClient.getUnsafeOkHttpClient();

        var client = okHttpClient.newBuilder()
                .addInterceptor(tokenInterceptor)
                .connectTimeout(1, TimeUnit.MINUTES)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(15, TimeUnit.SECONDS)
                //   .cache(cache)
                .build();

        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)

                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())

                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())

                .build();


        }

    @NotNull
    protected static Retrofit getRetrofitAdapter()
        {
        var okHttpClient = UnsafeOkHttpClient.getUnsafeOkHttpClient();
        var client = okHttpClient.newBuilder()
                .addInterceptor(new ConnectivityInterceptor())
                .connectTimeout(1, TimeUnit.MINUTES)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(15, TimeUnit.SECONDS)
                //   .cache(cache)
                .build();

        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .build();


        }


    }


