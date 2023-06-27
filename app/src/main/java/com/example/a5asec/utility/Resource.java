package com.example.a5asec.utility;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.jetbrains.annotations.Contract;

import lombok.Getter;
import timber.log.Timber;

public class Resource<T>
    {
    public static final String TAG = "Resource";
    @Getter
    public Status mStatus;
    @Getter
    public String mMessage;
    @Getter
    private T mData;




    public Resource(Status mStatus, T mData, String mMessage)
        {
        this.mStatus = mStatus;
        this.mData = mData;
        this.mMessage = mMessage;
        }


    public static <T> Resource<T> success(@Nullable T data)
        {
      //  Timber.tag(TAG).i("Suc:%s", data);
        if (data.toString().equals("[]") || data == null)
            {
         //   Timber.tag(TAG).i("Success:null :%s", data);
            return new Resource<>(Status.NULL, null, null);
            } else return new Resource<>(Status.SUCCESS, data, null);

        }

    @Contract("_ -> null")
    public static <T> Resource<T> empty(T data)
        {
      //  Timber.tag(TAG).i("null%s", data);

        return new Resource<>(Status.NULL, data, null);
        }

    @Contract("null, !null -> null")
    public static <T> Resource<T> error(String msg, T data)
        {
       // Timber.tag(TAG).i(String.valueOf(data));

        return new Resource<>(Status.ERROR, data, msg);
        }

    @Contract("_ -> null")
    public static <T> Resource<T> loading(T data)
        {
       // Timber.tag(TAG).i("loading%s", data);

        return new Resource<>(Status.LOADING, data, null);
        }
    }