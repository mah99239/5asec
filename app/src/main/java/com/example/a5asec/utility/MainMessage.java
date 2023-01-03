package com.example.a5asec.utility;

import com.google.gson.JsonSyntaxException;

import javax.net.ssl.HttpsURLConnection;

import retrofit2.HttpException;

public class MainMessage {
    public static final int API_STATUS_CODE_LOCAL_ERROR = 0;


    public static String handleApiError(Throwable error) {
        if (error instanceof HttpException) {
            switch (((HttpException) error).code()) {
                case HttpsURLConnection.HTTP_UNAUTHORIZED:
                    return ("Unauthorised User");

                case HttpsURLConnection.HTTP_FORBIDDEN:
                    return ("Forbidden");

                case HttpsURLConnection.HTTP_INTERNAL_ERROR:
                    return ("Internal Server Error");

                case HttpsURLConnection.HTTP_BAD_REQUEST:
                    return ("Bad Request");

                case API_STATUS_CODE_LOCAL_ERROR:
                    return ("No Internet Connection");

                default:
                    return (error.getLocalizedMessage());
            }
        } else if (error instanceof JsonSyntaxException) {
            return ("Something Went Wrong API is not responding properly!");
        } else {
            return (error.getMessage());
        }
    }


}
