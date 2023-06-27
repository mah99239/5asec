package com.example.a5asec.data.local.prefs;

import android.content.Context;
import android.content.SharedPreferences;

import javax.inject.Inject;



public class TokenPreferences
    {

    private static final String PREFS_NAME = "token_prefs";
    private static final String ACCESS_TOKEN_KEY = "access_token";
    private static final String REFRESH_TOKEN_KEY = "refresh_token";
    private static final String EXPIRES_IN_KEY = "expires_in";
    private static final String PASSWORD_KEY = "password";

    private final SharedPreferences sharedPreferences;
    @Inject
    public TokenPreferences(Context context)
        {
        sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        }

    public void setToken(String accessToken, String refreshToken, long expiresIn)
        {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(ACCESS_TOKEN_KEY, accessToken);
        editor.putString(REFRESH_TOKEN_KEY, refreshToken);
        editor.putLong(EXPIRES_IN_KEY, expiresIn);
        editor.apply();
        }

    public String getAccessToken()
        {
        return sharedPreferences.getString(ACCESS_TOKEN_KEY, null);
        }

    public String getRefreshToken()
        {
        return sharedPreferences.getString(REFRESH_TOKEN_KEY, null);
        }

    public long getExpiresIn()
        {
        return sharedPreferences.getLong(EXPIRES_IN_KEY, 0);
        }

    public String getPassword()
        {
        return sharedPreferences.getString(PASSWORD_KEY, null);
        }

    public void setPassword(String password)
        {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(PASSWORD_KEY, password);
        editor.apply();
        }

    public void resetToken()
        {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(ACCESS_TOKEN_KEY);
        editor.remove(REFRESH_TOKEN_KEY);
        editor.remove(EXPIRES_IN_KEY);
        editor.apply();
        }
    }