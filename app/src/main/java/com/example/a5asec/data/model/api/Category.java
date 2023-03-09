package com.example.a5asec.data.model.api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;

@Data
public class Category
    {


    @Expose
    @SerializedName("id")
    private int id;


    @Expose
    @SerializedName("nameEn")
    private String nameEn;
    @Expose
    @SerializedName("nameAr")
    private String nameAr;

    //This means @Data will not produce a getter for this field so have to explicitly define it
    @Getter(AccessLevel.NONE)
    private String name;

    @Expose
    @SerializedName("iconUrl")
    private String iconUrl;
    @Expose
    @SerializedName("sortIndex")
    private int sortIndex;
    @Expose
    @SerializedName("items")
    private List<ItemsEntity> items;

    public String getName(String language)
        {
        return language.equals("en") ? nameEn : nameAr;
        }

    @Data
    public static class ItemServicesEntity
        {


        @Expose
        @SerializedName("laundryService")
        private LaundryServiceEntity laundryService;
        @Expose
        @SerializedName("id")
        private int id;

        @Expose
        @SerializedName("cost")
        private int cost;

        public ItemServicesEntity(int id, int cost, LaundryServiceEntity entity)
            {
            this.id = id;
            this.cost = cost;
            this.laundryService = entity;
            }
        }

    @Data
    public static class LaundryServiceEntity
        {
        @Expose
        @SerializedName("id")
        private int id;
        @Expose
        @SerializedName("nameEn")
        private String nameEn;
        @Expose
        @SerializedName("nameAr")
        private String nameAr;

        @Getter(AccessLevel.NONE)
        private String name;

        @Expose
        @SerializedName("iconUrl")
        @Getter(AccessLevel.NONE)
        private String iconUrl;

        public String getIconUrl()
            {
            String url;
            if (!iconUrl.contains("5asec-ksa.com/icons/laundry-service/"))
                {
                url = "https://5asec-ksa.com/icons/laundry-service/" + iconUrl;
                } else url = "https://" + iconUrl;
            return url;
            }

        public String getName(String language)
            {
            return language.equals("en") ? nameEn : nameAr;
            }

        }

    @Data
    public class ItemsEntity
        {
        @Expose
        @SerializedName("id")
        private int id;
        @Expose
        @SerializedName("nameEn")
        private String nameEn;
        @Expose
        @SerializedName("nameAr")
        private String nameAr;

        @Getter(AccessLevel.NONE)
        private String name;
        @Expose
        @SerializedName("itemServices")
        private List<ItemServicesEntity> itemServices;

        @Expose
        @SerializedName("cost")
        private int cost;

        public String getName(String language)
            {
            return language.equals("en") ? nameEn : nameAr;
            }

        }
    }
