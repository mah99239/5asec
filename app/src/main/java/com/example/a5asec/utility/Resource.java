package com.example.a5asec.utility;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.jetbrains.annotations.Contract;

import lombok.Getter;

public class Resource<T>
    {
    public static final String TAG = "Resource";
    @Getter
    public Status mStatus;
    @Getter
    public String mMessage;
    @Getter
    private T mData;


    public Resource()
        {
        }

    public Resource(Status mStatus, T mData, String mMessage)
        {
        this.mStatus = mStatus;
        this.mData = mData;
        this.mMessage = mMessage;
        }

    @Nullable
    public static <T> Resource<T> success(@Nullable T data)
        {
        Log.e(TAG, "Suc:" + data);
        if (data.toString().equals("[]") || data == null)
            {
            Log.i(TAG, "Success:null :" + data);
            return new Resource<>(Status.NULL, null, null);
            } else return new Resource<>(Status.SUCCESS, data, null);

        }

    @NonNull
    @Contract("_ -> null")
    public static <T> Resource<T> empty(T data)
        {
        Log.i(TAG, "null" + data);

        return new Resource<>(Status.NULL, data, null);
        }

    @NonNull
    @Contract("null, !null -> null")
    public static <T> Resource<T> error(String msg, T data)
        {
        Log.i(TAG, String.valueOf(data));

        return new Resource<>(Status.ERROR, data, msg);
        }

    @NonNull
    @Contract("_ -> null")
    public static <T> Resource<T> loading(T data)
        {
        Log.i(TAG, "loading" + data);

        return new Resource<>(Status.LOADING, data, null);
        }
    }