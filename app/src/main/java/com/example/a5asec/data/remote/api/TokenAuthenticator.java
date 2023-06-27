package com.example.a5asec.data.remote.api;


import android.util.ArrayMap;
import android.util.Log;

import com.example.a5asec.data.local.prefs.TokenPreferences;
import com.example.a5asec.data.model.api.Authorization;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.Map;

import javax.inject.Inject;

import okhttp3.Authenticator;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.Route;
import timber.log.Timber;

public class TokenAuthenticator implements Authenticator
{
    private static final String TAG = "TokenAuthenticator";
    boolean isRefresh;
    retrofit2.Response<Authorization.AuthorizationEntity> sCall;
    String newAccessToken;
    @Inject
    TokenPreferences tokenPreferences;


    @Nullable
    @Override
    public synchronized Request authenticate(@Nullable Route route, @NotNull Response response)
            throws IOException
    {

        Timber.tag(TAG).e("token:" + newAccessToken);

        if (responseCount(response) >= 3) {
            Timber.tag(TAG).e("response count grater than:" + 3);
            return null; // If we've failed 3 times, give up.
        }
        boolean refresh = refreshToken();


        if (response.request().header("Authorization") != null) {
            return null; // Give up, we've already attempted to authenticate.
        }
        if (response.code() == HttpURLConnection.HTTP_UNAUTHORIZED) {
            if (refresh) {
                Timber.tag(TAG).e("refresh");
                return response.request().newBuilder().header("Authorization",
                        "bearer" + " " + newAccessToken).build();

                // refresh token is successful, we saved new token to storage.
                // Get your token from storage and set header
                //execute failed request again with new access token
            } else {
                Log.e(TAG, "failed");
                // Refresh token failed, you can logout user or retry couple of times
                // Returning null is critical here, it will stop the current request
                // If you do not return null, you will end up in a loop calling refresh
                return null;
            }
        }
        Timber.tag(TAG).e("refresh:" + newAccessToken);

        newAccessToken = tokenPreferences.getAccessToken();

        Request request = response.request().newBuilder()
                .header("Authorization", "bearer" + " " + newAccessToken)
                .build();
        Timber.tag(TAG).e("request" + response.code());
        return request;
    }

    public synchronized boolean refreshToken() throws IOException
    {
        // you can use RxJava with Retrofit and add blockingGet
        // it is up to you how to refresh your token
        Map<String, Object> jsonParams = new ArrayMap<>();
        //put something inside the map, could be null
 /*        String refreshToken = TokenPreferences.getPrefRefreshToken();
        Log.e("refreshToken", refreshToken + "s");
        jsonParams.put("refreshToken", refreshToken);
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse
                ("application/json; charset=utf-8"), (new JSONObject(jsonParams)).toString());

        UsersClient.getInstance().refreshToken(body).enqueue(
                new Callback<Authorization.AuthorizationEntity>() {
                    @Override
                    public void onResponse(Call<Authorization.AuthorizationEntity> call,
                                           retrofit2.Response<Authorization.AuthorizationEntity> response) {
                        Authorization.AuthorizationEntity body = response.body();
                        if (response.code() == 200 && body != null) {
                            Log.e(TAG + ".resp", "refresh.ok:" + response.message());

                        } else {

                            Log.e(TAG + ".resp", "refresh.error:" + response.message());

                        }
                    }

                    @Override
                    public void onFailure(Call<Authorization.AuthorizationEntity> call, Throwable t) {
                        Log.e(TAG, "refresh.FAILURE/" + t.getMessage());
                    }
                });
        sCall = UsersClient.getInstance().refreshToken(body).execute();

        int responseCode = sCall.code();
        Log.e(TAG, responseCode + "code");
        assert sCall.body() != null;
        Log.e(TAG, "responseCOde.token" + sCall.body().getAccess_token());
        newAccessToken = sCall.body().getAccess_token();
        if (responseCode == 200) {
            // save new token to sharedpreferences, storage etc.
            Log.e(TAG, "/refresh.OK/" + "new token");
            TokenPreferences.setToken(sCall.body().getAccess_token(), sCall.body().getRefresh_token()
                    , sCall.body().getExpires_in());
            return true;
        } else {        //cannot refresh
            Log.e(TAG, "/refresh.ERROR/" + "cannot refresh");

            return false;
        } */
        return false;

    }


    retrofit2.Response<Authorization.AuthorizationEntity> refresh()
    {
        // Map<String> jsonParams = new ArrayMap<>();
   /*      Map<String, Object> jsonParams = new ArrayMap<>();
        //put something inside the map, could be null
        String refreshToken = TokenPreferences.getPrefRefreshToken();
        jsonParams.put("refreshToken", refreshToken);
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse
                ("application/json; charset=utf-8"), (new JSONObject(jsonParams)).toString());

        //   Call<Authorization.AuthorizationEntity> s = null;
        UsersClient.getInstance().refreshToken(body).enqueue(
                new Callback<Authorization.AuthorizationEntity>() {
                    @Override
                    public void onResponse(Call<Authorization.AuthorizationEntity> call,
                                           retrofit2.Response<Authorization.AuthorizationEntity> response) {
                        sCall = response;

                    }

                    @Override
                    public void onFailure(Call<Authorization.AuthorizationEntity> call, Throwable t) {


                    }


                }); */

        return sCall;

    }

    private int responseCount(Response response)
    {
        int result = 1;
        while ((response = response.priorResponse()) != null) {
            result++;
        }
        return result;
    }
}