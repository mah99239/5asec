package com.example.a5asec.di;

import android.content.Context;

import com.example.a5asec.data.local.prefs.TokenPreferences;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.qualifiers.ApplicationContext;
import dagger.hilt.components.SingletonComponent;

@Module
@InstallIn(SingletonComponent.class)
public class PreferencesModule {


    @Singleton
    @Provides
    public TokenPreferences provideTokenPreferences(@ApplicationContext Context context) {
        return new TokenPreferences(context);
    }
}
