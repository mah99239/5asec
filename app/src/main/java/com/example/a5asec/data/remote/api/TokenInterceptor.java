package com.example.a5asec.data.remote.api;


import android.util.ArrayMap;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.a5asec.data.local.prefs.TokenPreferences;
import com.example.a5asec.utility.NetworkConnection;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.Map;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class TokenInterceptor implements Interceptor
    {
    private static final String TAG = "TokenInterceptor";

    @NotNull
    @Override
    public Response intercept(@NotNull Chain chain) throws IOException
        {

        if (!NetworkConnection.isOnline())
            {
            throw new UnknownHostException("No Internet Connection");
            }
       // okhttp3.RequestBody$Companion$toRequestBody$1@eebc8e4
        Request newRequest = chain.request();
        Response response = chain.proceed(newRequest);
        Log.e(TAG, "intercept ,request : " + newRequest.toString());


     /*    if (responseCount(response) >= 3)
            {
            Log.e(TAG, "response count grater than:" + 3);
            response.close();
            return response; // If we've failed 3 times, give up.
            } */


        // Check code is equal to 401
        if (response.code() == HttpURLConnection.HTTP_UNAUTHORIZED)
            {
          //  synchronized (this)
                {

                try
                    {
                     String newAccessToken = TokenPreferences.getPrefAccessToken();
                    Log.e(TAG, "intercept() Code: " + response.code());
                    response.close();
                    return chain.proceed(newRequestWithAccessToken(newRequest, newAccessToken));

                    } catch (NullPointerException e)
                    {
                    e.getMessage();
                    }
                }
            }
        Log.e(TAG, "intercept: CODE: " + response.code());

        return response;
        }

    private int resultTime()
        {
        // 23.9H to milliSecond = 86399000
        long expireTime = TokenPreferences.getPrefExpireIn();
        Log.e(TAG, "expitre:" + expireTime);

        Date expireDate = new Date();
        expireDate.setTime(expireTime);
        Date nowDate = new Date();



        Log.e(TAG, "intercept() - current time " + nowDate + "expire time" + expireDate);
        return expireDate.compareTo(nowDate);
        }

    private int responseCount(Response response)
        {
        int result = 1;
        while ((response = response.priorResponse()) != null)
            {
            result++;
            }
        return result;
        }

    @NonNull
    private Request newRequestWithAccessToken(@NonNull Request request, @NonNull String accessToken)
        {
        return request.newBuilder()
                .header("Authorization", "bearer " + accessToken)
                .build();
        }


    private void refreshToken()
        {
        // you can use RxJava with Retrofit and add blockingGet
        // it is up to you how to refresh your token
        Map<String, Object> jsonParams = new ArrayMap<>();
        String refreshToken = TokenPreferences.getPrefRefreshToken();

    /*     //put something inside the map, could be null
        String refreshToken = TokenPreferences.getPrefRefreshToken();
        jsonParams.put("refreshToken", refreshToken);
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse
                ("application/json; charset=utf-8"), (new JSONObject(jsonParams)).toString());

        UsersClient.getInstance().refreshToken(body).enqueue(
                new Callback<>()
                    {
                    @Override
                    public void onResponse(@NonNull Call<Authorization.AuthorizationEntity> call,
                                           @NonNull retrofit2.Response<Authorization.AuthorizationEntity> response)
                        {
                        Authorization.AuthorizationEntity body = response.body();
                        if (response.code() == 200 && body != null)
                            {
                            Log.e(TAG,
                                    "refreshToken().onResponse - into condition if code equal to 200"
                                            + response.message());

                            TokenPreferences.setToken(body.getAccess_token(), body.getRefresh_token(),
                                    body.getExpires_in());
                            } else
                            {
                            Log.e(TAG,
                                    "refreshToken().onResponse - into condition code not equal to 200"
                                            + response.message());
                            }
                        call.cancel();
                        }

                    @Override
                    public void onFailure(@NonNull Call<Authorization.AuthorizationEntity> call, @NonNull Throwable t)
                        {
                        Log.e(TAG, "refreshToken().onFailure() - show message " + t.getMessage());
                        call.cancel();
                        }
                    }); */
        }
    }
