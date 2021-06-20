package com.example.a5asec.data.local;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public final class TokenPreferences
{
  static Context mContext;
  static SharedPreferences sp;
  public static final String PREF_ACCESS_TOKEN = "access_token";
  public static final String PREF_REFRESH_TOKEN = "refresh_token";
  public static final String PREF_EXPIRES_IN = "expires_in";
  private static final String TAG  = TokenPreferences.class.getName();

  public static void setToken(String accessToken, String refreshToken,
      long expiresIn)
  {

    SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(mContext);
    SharedPreferences.Editor editor = sp.edit();
    editor.putString(PREF_ACCESS_TOKEN, accessToken);
    editor.putString(PREF_REFRESH_TOKEN, refreshToken);
    editor.putLong(PREF_EXPIRES_IN, setExpireTime(expiresIn));
    editor.apply();

  }

  public TokenPreferences(Context context)
  {
    mContext = context;
    sp = PreferenceManager.getDefaultSharedPreferences(mContext);
  }

  public static String getPrefAccessToken()
  {
    return sp.getString(PREF_ACCESS_TOKEN, null).intern();
  }

  public static String getPrefRefreshToken()
  {
    return sp.getString(PREF_REFRESH_TOKEN, null);
  }
  public static  long getPrefExpireIn()
  {

    return sp.getLong(PREF_EXPIRES_IN, 0);
  }
  private static long setExpireTime (long expire)
  {
    Date date = new Date();
    long currentTime = System.currentTimeMillis();

  long millis = TimeUnit.SECONDS.toMillis(expire);
    date.setTime( currentTime + millis);
    long expireTime = date.getTime();
    Log.e(TAG, "setExpireTime() - expire millis = "+ millis);

    Log.e(TAG, "setExpireTime() - expiretime = "+ expireTime);

    return expireTime;
  }
}
