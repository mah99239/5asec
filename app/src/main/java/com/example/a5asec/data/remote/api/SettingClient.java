package com.example.a5asec.data.remote.api;

import com.example.a5asec.data.model.api.Setting;

import io.reactivex.rxjava3.core.Observable;

public class SettingClient extends Client implements SettingApi
    {
    private static SettingClient INSTANCE;
    private final SettingApi mSettingApi;

    public SettingClient()
        {
        mSettingApi = getRetrofitAdapter().create(SettingApi.class);
        }

    public static SettingClient getINSTANCE()
        {
        return null == INSTANCE ? new SettingClient() : INSTANCE;
        }

    @Override
    public Observable<Setting> getSetting()
        {
        return mSettingApi.getSetting();
        }

    @Override
    public Observable<Setting> setSetting(Setting setting)
        {
        return null;
        }
    }
