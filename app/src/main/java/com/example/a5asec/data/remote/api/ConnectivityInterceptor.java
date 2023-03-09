package com.example.a5asec.data.remote.api;


import androidx.annotation.NonNull;

import com.example.a5asec.utility.NetworkConnection;

import java.io.IOException;
import java.net.UnknownHostException;

import okhttp3.Interceptor;
import okhttp3.Response;
import timber.log.Timber;

public class ConnectivityInterceptor implements Interceptor
    {
    private static final String TAG = "ConnectivityInterceptor";

    @NonNull
    @Override
    public Response intercept(@NonNull Chain chain) throws IOException
        {
        if (!NetworkConnection.isOnline())
            {
            throw new UnknownHostException("No Internet Connection");
            } else
            {
            Timber.tag(TAG).e("request: %s", chain.request());
            Timber.tag(TAG).e("response: %s", chain.request().tag());
            return chain.proceed(chain.request());
            }
        }
    }
