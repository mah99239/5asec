package com.example.a5asec;

import android.app.Application;
import android.content.res.Configuration;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;

import com.example.a5asec.utility.NetworkConnection;
import com.google.android.material.color.DynamicColors;


/**
 * @author : Mahmoud Abd Elhafeez
 * @version : 1.0
 */

public class MvvmApp extends Application
    {
    private static final String TAG = "MvvmApp";

    @Override
    public void onCreate()
        {
        super.onCreate();


        //Complete:(1) custom language in app
        //TODO:(2) setting notification in app
        DynamicColors.applyToActivitiesIfAvailable(this);
        // DynamicColors.applyToActivitiesIfAvailable(this, R.style.AppTheme_Overlay)
        new NetworkConnection(getApplicationContext());


        }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig)
        {
        super.onConfigurationChanged(newConfig);
        AppCompatDelegate.getApplicationLocales();
        }
    }