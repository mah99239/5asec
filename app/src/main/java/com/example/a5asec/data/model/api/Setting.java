package com.example.a5asec.data.model.api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.Data;

@Data
public  class Setting
    {

    @Expose
    @SerializedName("websiteLastUpdateDate")
    private int websiteLastUpdateDate;
    @Expose
    @SerializedName("bannerSlideInterval")
    private int bannerSlideInterval;
    @Expose
    @SerializedName("cashOnDeliveryFees")
    private int cashOnDeliveryFees;
    @Expose
    @SerializedName("taxesPercentage")
    private int taxesPercentage;
    @Expose
    @SerializedName("cancellationCost")
    private int cancellationCost;
    @Expose
    @SerializedName("reattemptCost")
    private int reattemptCost;
    @Expose
    @SerializedName("appDiscount")
    private int appDiscount;
    @Expose
    @SerializedName("appDiscountType")
    private String appDiscountType;
    @Expose
    @SerializedName("appDiscountActive")
    private boolean appDiscountActive;
    @Expose
    @SerializedName("courierVisitIntervalsCount")
    private int courierVisitIntervalsCount;
    @Expose
    @SerializedName("courierVisitInterval")
    private int courierVisitInterval;
    @Expose
    @SerializedName("courierVisitStartTime")
    private String courierVisitStartTime;
    @Expose
    @SerializedName("defaultPageSize")
    private int defaultPageSize;
    @Expose
    @SerializedName("defaultLang")
    private String defaultLang;
    @Expose
    @SerializedName("defaultLat")
    private String defaultLat;
    @Expose
    @SerializedName("aboutWebsiteURLAr")
    private String aboutWebsiteURLAr;
    @Expose
    @SerializedName("aboutWebsiteURLEn")
    private String aboutWebsiteURLEn;
    @Expose
    @SerializedName("changeMobileServiceActive")
    private boolean changeMobileServiceActive;
    @Expose
    @SerializedName("mailServiceActive")
    private boolean mailServiceActive;
    @Expose
    @SerializedName("smsServiceActive")
    private boolean smsServiceActive;
    @Expose
    @SerializedName("defaultProcessingDays")
    private int defaultProcessingDays;
    @Expose
    @SerializedName("bankAccountNumber")
    private String bankAccountNumber;
    @Expose
    @SerializedName("onlinePaymentActive")
    private boolean onlinePaymentActive;
    @Expose
    @SerializedName("cashOnDeliveryActive")
    private boolean cashOnDeliveryActive;
    @Expose
    @SerializedName("dashboardMinRelease")
    private String dashboardMinRelease;
    @Expose
    @SerializedName("instagramLink")
    private String instagramLink;
    @Expose
    @SerializedName("twitterLink")
    private String twitterLink;
    @Expose
    @SerializedName("facebookLink")
    private String facebookLink;
    @Expose
    @SerializedName("contactEmail")
    private String contactEmail;
    @Expose
    @SerializedName("contactPhone")
    private String contactPhone;
    @Expose
    @SerializedName("id")
    private int id;
    }
