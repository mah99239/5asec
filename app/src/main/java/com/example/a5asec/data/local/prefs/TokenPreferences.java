package com.example.a5asec.data.local.prefs;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import androidx.annotation.NonNull;

import java.util.Date;
import java.util.concurrent.TimeUnit;

public final class TokenPreferences
    {
    public static final String PREF_ACCESS_TOKEN = "access_token";
    public static final String PREF_EXPIRES_IN = "expires_in";
    public static final String PREF_REFRESH_TOKEN = "refresh_token";
    private static final String TAG = TokenPreferences.class.getName();
    private static final String PREF_password = "password";
     static Context sContext;

    static SharedPreferences sSharedPreferences;

    public TokenPreferences(Context context)
        {
        sContext = context;
        sSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        }

    public static void setToken(String accessToken, String refreshToken, long expiresIn)
        {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(sContext).edit();
        editor.putString(PREF_ACCESS_TOKEN, accessToken);
        editor.putString(PREF_REFRESH_TOKEN, refreshToken);
        editor.putLong(PREF_EXPIRES_IN, setExpireTime(expiresIn));
        editor.apply();
        }
    public static void setPassword(String password)
        {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(sContext).edit();
        editor.putString(PREF_password, password);
        editor.apply();
        }
    public static String getPassword()
        {
        return sSharedPreferences.getString(PREF_password, "null").intern();

        }

    public static void restToken()
        {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(sContext).edit();
        editor.putString(PREF_ACCESS_TOKEN, "default");
        editor.putString(PREF_REFRESH_TOKEN, null);
        editor.putLong(PREF_EXPIRES_IN,0L);

        editor.apply();
        }

    @NonNull
    public static String getPrefAccessToken()
        {
        return sSharedPreferences.getString(PREF_ACCESS_TOKEN, "default").intern();
        }

    public static String getPrefRefreshToken()
        {
        return sSharedPreferences.getString(PREF_REFRESH_TOKEN, null);
        }

    public static long getPrefExpireIn()
        {
        return sSharedPreferences.getLong(PREF_EXPIRES_IN, 0);
        }

    private static long setExpireTime(long expire)
        {
        Date date = new Date();
        long currentTime = System.currentTimeMillis();
        long millis = TimeUnit.SECONDS.toMillis(expire);

        date.setTime(currentTime + millis);
        long expireTime = date.getTime();

        Log.e(TAG, "setExpireTime() - expire millis = " + millis);
        Log.e(TAG, "setExpireTime() - expireTime = " + expireTime);
        return expireTime;
        }
    }