package com.example.a5asec.data.model.api;


import com.google.gson.annotations.SerializedName;

import java.util.List;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;

@Data
public class City
    {

    @SerializedName("areas")
    private List<AreasDTO> areas;
    @SerializedName("deliveryCost")
    private float deliveryCost;
    @SerializedName("expectedPickup")
    private Integer expectedPickup;
    @SerializedName("id")
    private Integer id;
    @SerializedName("isCovered")
    private Boolean isCovered;
    @SerializedName("lang")
    private String lang;
    @SerializedName("lat")
    private String lat;
    @SerializedName("nameAr")
    private String nameAr;
    @SerializedName("nameEn")
    private String nameEn;

    @Getter(AccessLevel.NONE)
    private String name;

    public String getName(String language)
        {
        return language.equals("en") ? nameEn : nameAr;
        }

    @Data
    public static class AreasDTO
        {
        @SerializedName("id")
        private Integer id;
        @SerializedName("isCovered")
        private Boolean isCovered;
        @SerializedName("lang")
        private String lang;
        @SerializedName("lat")
        private String lat;
        @SerializedName("nameAr")
        private String nameAr;
        @SerializedName("nameEn")
        private String nameEn;

        @Getter(AccessLevel.NONE)
        private String name;

        public String getName(String language)
            {
            return language.equals("en") ? nameEn : nameAr;
            }

        }
    }
