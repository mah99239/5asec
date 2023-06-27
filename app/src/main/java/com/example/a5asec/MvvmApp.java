package com.example.a5asec;

import android.app.Application;
import android.content.res.Configuration;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;

import com.google.android.material.color.DynamicColors;

import dagger.hilt.android.HiltAndroidApp;
import timber.log.Timber;


/**
 * @author : Mahmoud Abd-Elhafeez
 * @version : 1.0
 */
@HiltAndroidApp
public class MvvmApp extends Application
{


    @Override
    public void onCreate()
    {
        super.onCreate();
        Timber.plant(new Timber.DebugTree());


        //https://github.com/JakeWharton/timber/blob/trunk/timber-sample/src/main/java/com/example/timber/ExampleApp.java
     /*    if (BuildConfig.DEBUG)
            {
            Timber.uprootAll();
            Timber.plant(new Timber.DebugTree());

            } else
            {
            // Timber.plant(new ReleaseTree());
            //  Timber.plant(new CrashReportingTree());

            } */

        //Complete:(1) custom language in app
        //TODO:(2) setting notification in app
        DynamicColors.applyToActivitiesIfAvailable(this);
        // DynamicColors.applyToActivitiesIfAvailable(this, R.style.AppTheme_Overlay)
        // new NetworkConnection(getApplicationContext());


    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig)
    {
        super.onConfigurationChanged(newConfig);
        AppCompatDelegate.getApplicationLocales();
    }

    /**
     * A tree which logs important information for crash reporting.
     */
    private static class CrashReportingTree //extends Timber.Tree
    {
      /*   @Override
        protected void log(int priority, String tag, @NonNull String message, Throwable t)
            {
            if (priority == Log.VERBOSE || priority == Log.DEBUG)
                {
                return;
                }

            FakeCrashLibrary.log(priority, tag, message);

            if (t != null)
                {
                if (priority == Log.ERROR)
                    {
                    FakeCrashLibrary.logError(t);
                    } else if (priority == Log.WARN)
                    {
                    FakeCrashLibrary.logWarning(t);
                    }
                }
            } */
    }

}