package com.example.a5asec.data.api;

import android.annotation.SuppressLint;
import android.os.Build;
import android.util.ArrayMap;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import com.example.a5asec.data.local.TokenPreferences;
import com.example.a5asec.data.model.Authorization;

import java.io.IOException;

import java.net.HttpURLConnection;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.Map;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;
import retrofit2.Call;
import retrofit2.Callback;

public class TokenInterceptor implements Interceptor
{
  private static final String TAG = "TokenInterceptor";

  @RequiresApi(api = Build.VERSION_CODES.O) @SuppressLint("CheckResult") @NotNull @Override
  public Response intercept(@NotNull Chain chain) throws IOException
  {
    Request newRequest = chain.request();
    Response response = chain.proceed(newRequest);

    if (responseCount(response) >= 3)
    {
      Log.e(TAG, "response count grater than:" + 3);
      response.close();
      return response; // If we've failed 3 times, give up.
    }

    //Java 8 - toEpochMilli() method of ZonedDateTime
    Log.e(TAG, "intercept() - ZonedDateTime " + ZonedDateTime.now().toInstant().toEpochMilli());

    if (response.code() == HttpURLConnection.HTTP_UNAUTHORIZED)
    {
      synchronized (this)
      {
        Log.e(TAG, ".intercept()  - get number of result " + resultTime());

        if (resultTime() < 0)
        {
          //result time = -1
          //save refreshed token's expire time:
          refreshToken();
          response.close();


         /* newRequest = chain.request().newBuilder()
              // .header("Authorization", "bearer" + " " + TokenPreferences.getPrefAccessToken())
              .header("Authorization", "bearer" + " " + newAccessToken)
              .build();
          Log.e(TAG, "refresh:" + newRequest.body());*/
          final String newAccessToken = TokenPreferences.getPrefAccessToken();

          Log.e(TAG, "intercept() - into condition if  result equal to -1" + response.code());
          //   response.close();
          return chain.proceed(newRequestWithAccessToken(newRequest, newAccessToken));
        }

        final String newAccessToken = TokenPreferences.getPrefAccessToken();
        Log.e(TAG, "intercept() - into sync " + response.code());
        response.close();
        return chain.proceed(newRequestWithAccessToken(newRequest, newAccessToken));
      }
    }
    Log.e(TAG, "intercept() - into sync " + response.code());

    return response;
  }

  private int resultTime()
  {
    long expireTime = TokenPreferences.getPrefExpireIn();
    Date expireDate = new Date();
    expireDate.setTime(expireTime);
    Date nowDate = new Date();

    // 23.9H to milliSecond = 86399000

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

  @RequiresApi(api = Build.VERSION_CODES.KITKAT)
  private void refreshToken()
  {
    // you can use RxJava with Retrofit and add blockingGet
    // it is up to you how to refresh your token
    Map<String, Object> jsonParams = new ArrayMap<>();

    //put something inside the map, could be null
    String refreshToken = TokenPreferences.getPrefRefreshToken();
    jsonParams.put("refreshToken", refreshToken);
    RequestBody body = RequestBody.create(okhttp3.MediaType.parse
        ("application/json; charset=utf-8"), (new JSONObject(jsonParams)).toString());

    UsersClient.getInstance().refreshToken(body).enqueue(
        new Callback<Authorization.AuthorizationEntity>()
        {
          @Override public void onResponse(Call<Authorization.AuthorizationEntity> call,
              retrofit2.Response<Authorization.AuthorizationEntity> response)
          {
            Authorization.AuthorizationEntity body = response.body();
            if (response.code() == 200 && body != null)
            {
              Log.e(TAG,
                  "refreshToken().onResponse - into condition if code equal to 200"
                      + response.message());

              TokenPreferences.setToken(body.getAccess_token(), body.getRefresh_token(),
                  body.getExpires_in());
            }
            else
            {
              Log.e(TAG,
                  "refreshToken().onResponse - into condition code not equal to 200"
                      + response.message());
            }
            call.cancel();
          }

          @Override public void onFailure(Call<Authorization.AuthorizationEntity> call, Throwable t)
          {
            Log.e(TAG, "refreshToken().onFailure() - show message " + t.getMessage());
            call.cancel();
          }
        });
  }
}
