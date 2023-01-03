package com.example.a5asec.ui.view.login;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.example.a5asec.R;
import com.example.a5asec.data.local.prefs.TokenPreferences;
import com.example.a5asec.ui.view.home.HomeActivity;

public class SplashActivity extends AppCompatActivity
    {
    private static final String TAG = "SplashActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState)
        {
        setTheme(R.style.SplashTheme);

        super.onCreate(savedInstanceState);

    new TokenPreferences(this);
        try
            {
            if(!TokenPreferences.getPrefAccessToken().equals("default"))
                {
                Log.e(TAG, "pref"  +TokenPreferences.getPrefAccessToken() );
                startActivity(new Intent(SplashActivity.this, HomeActivity.class));
                }
            else
                {

                startActivity(new Intent(SplashActivity.this, MainActivity.class));

                }
            }catch (NullPointerException e)
            {
            Log.e(TAG, e.getMessage());
            }

        finish();


        }
    }