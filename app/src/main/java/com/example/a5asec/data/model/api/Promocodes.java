package com.example.a5asec.data.model.api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.Data;

@Data
public abstract class Promocodes
    {

    @Expose
    @SerializedName("value")
    private int value;
    @Expose
    @SerializedName("used")
    private boolean used;
    @Expose
    @SerializedName("start")
    private String start;
    @Expose
    @SerializedName("id")
    private int id;
    @Expose
    @SerializedName("expirationDate")
    private String expirationDate;
    @Expose
    @SerializedName("discountType")
    private String discountType;
    @Expose
    @SerializedName("days")
    private int days;
    @Expose
    @SerializedName("createdByName")
    private String createdByName;
    @Expose
    @SerializedName("createdById")
    private int createdById;
    @Expose
    @SerializedName("createdAt")
    private String createdAt;
    @Expose
    @SerializedName("code")
    private String code;
    @Expose
    @SerializedName("clientName")
    private String clientName;
    @Expose
    @SerializedName("clientId")
    private int clientId;
    }
