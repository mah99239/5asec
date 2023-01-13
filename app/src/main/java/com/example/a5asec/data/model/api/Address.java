package com.example.a5asec.data.model.api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
@Data
public  class Address
    {


    @Expose
    @SerializedName("area")
    private AreaEntity area;
    @Expose
    @SerializedName("lang")
    private String lang;
    @Expose
    @SerializedName("lat")
    private String lat;
    @Expose
    @SerializedName("note")
    private String note;
    @Expose
    @SerializedName("isPrimary")
    private boolean isPrimary;
    @Expose
    @SerializedName("nearLandmark")
    private String nearLandmark;
    @Expose
    @SerializedName("apartment")
    private String apartment;
    @Expose
    @SerializedName("floor")
    private String floor;
    @Expose
    @SerializedName("building")
    private String building;
    @Expose
    @SerializedName("street")
    private String street;
    @Expose
    @SerializedName("id")
    private int id;




    @Data
    public static   class CreateAddress
        {

        @Expose
        @SerializedName("street")
        private transient  String street;
        @Expose
        @SerializedName("note")
        private String note;
        @Expose
        @SerializedName("nearLandmark")
        private String nearLandmark;
        @Expose
        @SerializedName("lat")
        private String lat;
        @Expose
        @SerializedName("lang")
        private String lang;
        @Expose
        @SerializedName("floor")
        private String floor;
        @Expose
        @SerializedName("building")
        private String building;
        @Expose
        @SerializedName("areaId")
        private int areaId;
        @Expose
        @SerializedName("apartment")
        private String apartment;
        public CreateAddress(String apartment, String street, int areaId, String building, String floor,
                       String lang, String lat, String nearLandmark, String notes)
            {
            this.apartment = apartment;
            this.street = street;
            this.areaId = areaId;
            this.building = building;
            this.floor = floor;
            this.lang = lang;
            this.lat= lat;
            this.nearLandmark = nearLandmark;
            this.note = notes;
            }
        }

    @Data
    public static  class UpdateAddress
        {

        @Expose
        @SerializedName("street")
        private String street;
        @Expose
        @SerializedName("note")
        private String note;
        @Expose
        @SerializedName("nearLandmark")
        private String nearLandmark;
        @Expose
        @SerializedName("lat")
        private String lat;
        @Expose
        @SerializedName("lang")
        private String lang;
        @Expose
        @SerializedName("id")
        private int id;
        @Expose
        @SerializedName("floor")
        private String floor;
        @Expose
        @SerializedName("building")
        private String building;
        @Expose
        @SerializedName("areaId")
        private int areaId;
        @Expose
        @SerializedName("apartment")
        private String apartment;

        public UpdateAddress(String apartment, String street, int areaId, String building, String floor,
                             String lang, String lat, String nearLandmark, String notes)
            {
            this.apartment = apartment;
            this.street = street;
            this.areaId = areaId;
            this.building = building;
            this.floor = floor;
            this.lang = lang;
            this.lat= lat;
            this.nearLandmark = nearLandmark;
            this.note = notes;
            }
        }

    @Data
    public static class AreaEntity
        {
        @Expose
        @SerializedName("city")
        private CityEntity city;
        @Expose
        @SerializedName("lat")
        private String lat;
        @Expose
        @SerializedName("lang")
        private String lang;
        @Expose
        @SerializedName("isCovered")
        private boolean isCovered;
        @Expose
        @SerializedName("nameAr")
        private String nameAr;
        @Expose
        @SerializedName("nameEn")
        private String nameEn;
        @Expose
        @SerializedName("id")
        private int id;

        @Getter(AccessLevel.NONE)
        private String name;

        public String getName(String language)
            {
            return language.equals("en") ? this.getNameEn() : this.getNameAr();
            }
        }

    @Data
    public static class CityEntity
        {
        @Expose
        @SerializedName("lat")
        private String lat;
        @Expose
        @SerializedName("lang")
        private String lang;
        @Expose
        @SerializedName("expectedPickup")
        private int expectedPickup;
        @Expose
        @SerializedName("deliveryCost")
        private float deliveryCost;
        @Expose
        @SerializedName("isCovered")
        private boolean isCovered;
        @Expose
        @SerializedName("nameAr")
        private String nameAr;
        @Expose
        @SerializedName("nameEn")
        private String nameEn;
        @Expose
        @SerializedName("id")
        private int id;

        @Getter(AccessLevel.NONE)
        private String name;

        public String getName(String language)
            {
            return language.equals("en") ? getNameEn() : getNameAr();
            }

        }


    }
