package com.example.a5asec.utility;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkRequest;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import javax.inject.Inject;
import javax.inject.Singleton;

import dagger.hilt.android.qualifiers.ApplicationContext;
import timber.log.Timber;

@Singleton
public class NetworkConnection extends LiveData<Boolean>
{

    private final ConnectivityManager connectivityManager;
    private final MutableLiveData<Boolean> networkLiveData = new MutableLiveData<>();
    private final ConnectivityManager.NetworkCallback networkCallback =
            new ConnectivityManager.NetworkCallback()
            {
                @Override
                public void onAvailable(@NonNull Network network)
                {
                    super.onAvailable(network);
                    networkLiveData.postValue(true);
                }

                @Override
                public void onLost(@NonNull Network network)
                {
                    super.onLost(network);
                    networkLiveData.postValue(false);
                }
            };

    @Inject
    public NetworkConnection(@ApplicationContext Context context)
    {
        connectivityManager =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
    }

    @Override
    protected void onActive()
    {
        super.onActive();
        registerNetworkCallback();
    }

    @Override
    protected void onInactive()
    {
        super.onInactive();
        unregisterNetworkCallback();
    }

    public void registerNetworkCallback()
    {
        NetworkRequest request = new NetworkRequest.Builder()
                .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                .build();
        connectivityManager.registerNetworkCallback(request, networkCallback);
    }

    public void unregisterNetworkCallback()
    {
        if (connectivityManager != null) {
            try {
                connectivityManager.unregisterNetworkCallback(networkCallback);

            } catch (Exception e) {
                Timber.tag("unregisterNetworkCallback").d(e);
            }
        }
    }

    public boolean isActive()
    {
        NetworkCapabilities capabilities =
                connectivityManager.getNetworkCapabilities(connectivityManager.getActiveNetwork());
        return capabilities != null &&
                (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                        capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                        capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET));
    }

    public LiveData<Boolean> getNetworkLiveData()
    {
        return networkLiveData;
    }
}
