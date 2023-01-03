package com.example.a5asec.data.remote.api;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.a5asec.utility.NetworkConnection;

import java.io.IOException;
import java.net.UnknownHostException;

import okhttp3.Interceptor;
import okhttp3.Response;

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
            Log.e(TAG,"request: " +  chain.request());
            Log.e(TAG,"response: " +  chain.request().tag());
            return chain.proceed(chain.request());
            }
        }
    }
