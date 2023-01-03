package com.example.a5asec.utility;

import static android.content.Context.CONNECTIVITY_SERVICE;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkRequest;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

public class NetworkConnection extends LiveData<Boolean>
    {

    static ConnectivityManager connectivityManager;
    private BroadcastReceiver networkReceiver = new BroadcastReceiver()
        {

        @Override
        public void onReceive(Context context, Intent intent)
            {
            updateConnection();

            }
        };

    public NetworkConnection(Context context)
        {
        connectivityManager =
                (ConnectivityManager) context.getSystemService(CONNECTIVITY_SERVICE);
        }

    public static boolean isOnline()
        {
        var capabilities = connectivityManager.getNetworkCapabilities(
                connectivityManager.getActiveNetwork());
        if (capabilities != null)
            {
            if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) return true;
            else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) return true;
            else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET))
                return true;

            }
        return false;
        }

    @SuppressLint("MissingPermission")
    @Override
    protected void onActive()
        {
        super.onActive();
        updateConnection();
        connectivityManager.registerDefaultNetworkCallback(connectivityManagerCallback());
        }

    @Override
    protected void onInactive()
        {
        super.onInactive();

        try
            {
            //FIXME:error in connectivityManagerCallback  is null
            //    NetworkCallback was not registered
            connectivityManager.unregisterNetworkCallback(connectivityManagerCallback());
            } catch (Exception e)
            {
            e.printStackTrace();
            }
        }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void lollipopNetworkRequest()
        {
        NetworkRequest request = new NetworkRequest.Builder()
                .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
                .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
                .addTransportType(NetworkCapabilities.TRANSPORT_ETHERNET).build();
        connectivityManager.registerNetworkCallback(request, connectivityManagerCallback());
        }

    public ConnectivityManager.NetworkCallback connectivityManagerCallback()
        {
        return new ConnectivityManager.NetworkCallback()
            {
            @Override
            public void onLost(@NonNull Network network)
                {
                super.onLost(network);
                postValue(false);
                }

            @Override
            public void onAvailable(@NonNull Network network)
                {
                super.onAvailable(network);
                postValue(true);
                }
            };
        }

    private void updateConnection()
        {
        NetworkCapabilities capabilities =
                connectivityManager.getNetworkCapabilities(connectivityManager.getActiveNetwork());
        @SuppressLint("WrongConstant") boolean isConnect =
                capabilities != null && capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI
                        | NetworkCapabilities.TRANSPORT_CELLULAR);

        postValue(isConnect);
        }
    }
