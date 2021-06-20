package com.example.a5asec.utility;

import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkRequest;
import android.os.Build;
import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import static android.content.Context.CONNECTIVITY_SERVICE;

public class NetworkConnection extends LiveData<Boolean>
{

  private final Context context;
  ConnectivityManager connectivityManager;

  public NetworkConnection(Context context)
  {
    this.context = context;
    connectivityManager =
        (ConnectivityManager) context.getSystemService(CONNECTIVITY_SERVICE);
  }

  @Override
  protected void onActive()
  {
    super.onActive();
    updateConnection();
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
    {
      connectivityManager.registerDefaultNetworkCallback(connectivityManagerCallback());
    }
    else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
    {
      lollipopNetworkRequest();
    }
    else
    {
      context.registerReceiver(networkReceiver,
                               new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }
  }

  @Override
  protected void onInactive()
  {
    super.onInactive();
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
    {

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
    else
    {
      try {
        context.unregisterReceiver(networkReceiver);
      } catch (IllegalArgumentException e) {
        e.printStackTrace();
      }
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
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
    {
      ConnectivityManager.NetworkCallback networkCallback =
          new ConnectivityManager.NetworkCallback()
          {
            @Override public void onLost(@NonNull Network network)
            {
              super.onLost(network);
              postValue(false);
            }

            @Override public void onAvailable(@NonNull Network network)
            {
              super.onAvailable(network);
              postValue(true);
            }
          };
      return networkCallback;
    } else
    {
      throw new IllegalAccessError("Error");
    }
  }

  private  BroadcastReceiver networkReceiver = new BroadcastReceiver()
  {

    @Override public void onReceive(Context context, Intent intent)
    {
      updateConnection();

    }
  };

  private void updateConnection()
  {
    NetworkCapabilities capabilities =
        connectivityManager.getNetworkCapabilities(connectivityManager.getActiveNetwork());
    boolean isConnect =
        capabilities != null && capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI
                                                              | NetworkCapabilities.TRANSPORT_CELLULAR);

    postValue(isConnect);
  }
}
