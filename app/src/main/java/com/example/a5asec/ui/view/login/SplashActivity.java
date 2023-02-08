package com.example.a5asec.ui.view.login;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.splashscreen.SplashScreen;

import com.example.a5asec.data.local.prefs.TokenPreferences;
import com.example.a5asec.ui.view.home.HomeActivity;


public class SplashActivity extends AppCompatActivity
    {
    private static final String TAG = "SplashActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState)
        {

        //   setTheme(R.style.SplashTheme);
        SplashScreen splashScreen = SplashScreen.installSplashScreen(this);

        super.onCreate(savedInstanceState);


        splashScreen.setKeepOnScreenCondition(() -> true);
        startActivity();
        finish();


        }

    private void startActivity()
        {
        new TokenPreferences(this);
        try
            {
            if (!TokenPreferences.getPrefAccessToken().equals("default"))
                {
                Log.e(TAG, "pref" + TokenPreferences.getPrefAccessToken());
                startActivity(new Intent(SplashActivity.this, HomeActivity.class));
                } else
                {

                startActivity(new Intent(SplashActivity.this, MainActivity.class));

                }
            } catch (NullPointerException e)
            {
            Log.e(TAG, e.getMessage());
            }
        }
    }