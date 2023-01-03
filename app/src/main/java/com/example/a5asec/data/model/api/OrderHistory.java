package com.example.a5asec.data.model.api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.Data;

@Data
public  class OrderHistory
    {

    @Expose
    @SerializedName("updatedById")
    private int updatedById;
    @Expose
    @SerializedName("updatedBy")
    private String updatedBy;
    @Expose
    @SerializedName("systemNote")
    private String systemNote;
    @Expose
    @SerializedName("status")
    private String status;
    @Expose
    @SerializedName("id")
    private int id;
    @Expose
    @SerializedName("forCourier")
    private boolean forCourier;
    @Expose
    @SerializedName("forClient")
    private boolean forClient;
    @Expose
    @SerializedName("date")
    private String date;


    }
