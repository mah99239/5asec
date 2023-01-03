package com.example.a5asec.utility;

import static java.net.HttpURLConnection.HTTP_BAD_REQUEST;
import static java.net.HttpURLConnection.HTTP_CLIENT_TIMEOUT;
import static java.net.HttpURLConnection.HTTP_FORBIDDEN;
import static java.net.HttpURLConnection.HTTP_GATEWAY_TIMEOUT;
import static java.net.HttpURLConnection.HTTP_INTERNAL_ERROR;
import static java.net.HttpURLConnection.HTTP_UNAUTHORIZED;

import com.google.gson.JsonSyntaxException;

import java.net.UnknownHostException;

import retrofit2.HttpException;

public final class ApiError
    {
    public static final int API_STATUS_CODE_LOCAL_ERROR = 0;

    public static String handleApiError(Throwable error)
        {

        if (error instanceof HttpException)
            {
            //TODO: add message with timeout
            return switch (((HttpException) error).code())
                    {
                    case HTTP_UNAUTHORIZED -> ("Unauthorised User");
                    case HTTP_FORBIDDEN -> ("Forbidden");
                    case HTTP_INTERNAL_ERROR -> ("Internal Server Error");
                    case HTTP_BAD_REQUEST -> ("Bad Request");
                    case HTTP_CLIENT_TIMEOUT -> ("Bad HTTP_CLIENT_TIMEOUT");
                    case HTTP_GATEWAY_TIMEOUT -> ("Check your internet data");
                    case API_STATUS_CODE_LOCAL_ERROR -> ("No Internet Connection");
                    default -> ("default: " + error.getLocalizedMessage() + error.getLocalizedMessage());
                    };
            } else if (error instanceof JsonSyntaxException)
            {
            return "Something Went Wrong API is not responding properly!";
            } else if (error instanceof UnknownHostException)
            {
            return "Check your data internet";

            } else
            {

            return ((HttpException)error).response().errorBody().toString();
            }

        }

/*
* String getErrorMessage(Throwable e) {
    RetrofitError retrofitError;
    if (e instanceof RetrofitError) {
        retrofitError = ((RetrofitError) e);
        if (retrofitError.getKind() == RetrofitError.Kind.NETWORK) {
            return "Network is down!";
        }
    }
}
*
*  */
    }