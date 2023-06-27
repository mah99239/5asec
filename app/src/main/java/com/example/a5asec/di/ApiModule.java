package com.example.a5asec.di;

import com.example.a5asec.data.remote.api.BannersApi;
import com.example.a5asec.data.remote.api.CategoryApi;
import com.example.a5asec.data.remote.api.ConnectivityInterceptor;
import com.example.a5asec.data.remote.api.UnsafeOkHttpClient;
import com.example.a5asec.utility.NetworkConnection;

import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

@Module
@InstallIn(SingletonComponent.class)
public abstract class ApiModule
{
    private static final String BASE_URL = "https://stg.5asec-ksa.com/";

    @Provides
    @Singleton
    public static ConnectivityInterceptor provideConnectivityInterceptor(
            NetworkConnection networkConnection)
    {
        return new ConnectivityInterceptor(networkConnection);
    }

    @Provides
    @Singleton
    public static OkHttpClient provideOkHttpClient(ConnectivityInterceptor connectivityInterceptor)
    {
        var okHttpClient = UnsafeOkHttpClient.getUnsafeOkHttpClient();
        return okHttpClient.newBuilder()
                .addInterceptor(connectivityInterceptor)
                .connectTimeout(1, TimeUnit.MINUTES)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(15, TimeUnit.SECONDS)
                .build();
    }

    @Provides
    @Singleton
    public static Retrofit provideRetrofit(OkHttpClient okHttpClient)
    {


        return new Retrofit.Builder().baseUrl(BASE_URL).client(okHttpClient)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .build();
    }

    @Provides
    @Singleton
    public static BannersApi provideBannersApi(Retrofit retrofit)
    {
        return retrofit.create(BannersApi.class);
    }

    @Provides
    @Singleton
    public static CategoryApi provideCategoryApi(Retrofit retrofit)
    {
        return retrofit.create(CategoryApi.class);
    }


}
