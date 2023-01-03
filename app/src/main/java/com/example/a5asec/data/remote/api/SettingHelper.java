package com.example.a5asec.data.remote.api;

import com.example.a5asec.data.model.api.Setting;

import io.reactivex.rxjava3.core.Observable;

public class SettingHelper {
private SettingApi mSettingApi;

public SettingHelper(SettingApi settingApi) {
this.mSettingApi = settingApi;
}

public Observable<Setting> getSetting() {
return this.mSettingApi.getSetting();
}
}