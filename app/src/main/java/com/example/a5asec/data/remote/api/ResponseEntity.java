package com.example.a5asec.data.remote.api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.Data;

@Data
public  class ResponseEntity
    {

    @Expose
    @SerializedName("statusCodeValue")
    private int statusCodeValue;
    @Expose
    @SerializedName("statusCode")
    private String statusCode;
    @Expose
    @SerializedName("body")
    private BodyEntity body;

    public static class BodyEntity
        {
        }

    }
