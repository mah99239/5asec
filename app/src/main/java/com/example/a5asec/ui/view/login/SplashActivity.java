package com.example.a5asec.ui.view.login;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.splashscreen.SplashScreen;

import com.example.a5asec.data.local.prefs.TokenPreferences;
import com.example.a5asec.ui.view.home.HomeActivity;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import timber.log.Timber;

@AndroidEntryPoint
public class SplashActivity extends AppCompatActivity
{
    private static final String TAG = "SplashActivity";
    @Inject
    TokenPreferences tokenPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        //   setTheme(R.style.SplashTheme);

        SplashScreen splashScreen = SplashScreen.installSplashScreen(this);
        super.onCreate(savedInstanceState);
//        ((MvvmApp)this.getApplication()).getPreferencesComponent().inject(this);


        //DaggerAppComponent.builder().build().inject(this);

        splashScreen.setKeepOnScreenCondition(() -> true);
        startActivity();
        finish();


    }

    private void startActivity()
    {
        try {

            if (tokenPreferences.getAccessToken() != null) {
                Timber.tag(TAG).e("startActivity HomeActivity %s",
                        tokenPreferences.getAccessToken());
                startActivity(
                        new Intent(SplashActivity.this, HomeActivity.class));
            } else {

                startActivity(
                        new Intent(SplashActivity.this, MainActivity.class));
                Timber.tag(TAG).e("startActivity, MainActivity %s",
                        tokenPreferences.getAccessToken());

            }
        } catch (NullPointerException e) {
            Timber.tag(TAG).e(e);
        }
    }
}