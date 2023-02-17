package com.example.a5asec.utility;

import android.content.Context;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Registry;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.integration.okhttp3.OkHttpUrlLoader;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.module.LibraryGlideModule;
import com.example.a5asec.data.remote.api.UnsafeOkHttpClient;

import java.io.InputStream;

import okhttp3.Call;
import okhttp3.OkHttpClient;

@GlideModule
public class OkHttpLibraryGlideModule  extends LibraryGlideModule
    {
    @Override
    public void registerComponents(
            @NonNull Context context, @NonNull Glide glide, @NonNull Registry registry) {
/*     OkHttpClient client = UnsafeOkHttpClient.getUnsafeOkHttpClient();
    registry.replace(GlideUrl.class, InputStream.class,
            new OkHttpUrlLoader.Factory((Call.Factory) client)); */
    registry.replace(GlideUrl.class, InputStream.class, new OkHttpUrlLoader.Factory());

    }
}
