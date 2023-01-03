package com.example.a5asec.data.repository;

import com.example.a5asec.data.remote.api.SettingHelper;
import com.example.a5asec.data.model.api.Setting;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;

public class SettingRepository
    {
    private static final String TAG = "SettingRepository";
    private SettingHelper mSettingHelper;


    public SettingRepository(SettingHelper settingHelper) {
    this.mSettingHelper = settingHelper;
    }
    @NonNull
    public Observable<Setting> getSetting() {
    return mSettingHelper.getSetting();
    }
    }
