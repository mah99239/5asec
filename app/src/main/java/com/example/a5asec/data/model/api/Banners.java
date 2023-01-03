package com.example.a5asec.data.model.api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.Data;


/**
 * Banners
 * used lombok to
 */
@Data
public class Banners {


    @Expose
    @SerializedName("id")
    private Integer id;

    @Expose
    @SerializedName("url")
    private String url;

    @Expose
    @SerializedName("active")
    private Boolean active;
    @Expose
    @SerializedName("createdById")
    private Integer createdById;
    @Expose
    @SerializedName("createdByName")
    private String createdByName;
    @Expose
    @SerializedName("createdAt")
    private String createdAt;
    @Expose
    @SerializedName("description")
    private String description;


}
