package com.example.a5asec.data.remote.api;

import com.google.gson.annotations.SerializedName;

import lombok.Data;

@Data
public  class ResponseEntity
    {

    @SerializedName("statusCodeValue")
    private int statusCodeValue;
    @SerializedName("statusCode")
    private String statusCode;
    @SerializedName("body")
    private BodyEntity body;

    public static class BodyEntity
        {
        }

    }
