package com.example.a5asec.data.model.api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.Data;

@Data
public  class Order
    {

    @Expose
    @SerializedName("itemsCount")
    private int itemsCount;
    @Expose
    @SerializedName("courierMobile")
    private String courierMobile;
    @Expose
    @SerializedName("courierName")
    private String courierName;
    @Expose
    @SerializedName("courierId")
    private int courierId;
    @Expose
    @SerializedName("clientMobile")
    private String clientMobile;
    @Expose
    @SerializedName("clientName")
    private String clientName;
    @Expose
    @SerializedName("clientId")
    private int clientId;
    @Expose
    @SerializedName("promoCode")
    private PromoCodeEntity promoCode;
    @Expose
    @SerializedName("paymentProcess")
    private PaymentProcessEntity paymentProcess;
    @Expose
    @SerializedName("lang")
    private String lang;
    @Expose
    @SerializedName("lat")
    private String lat;
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
    @SerializedName("areaNameAr")
    private String areaNameAr;
    @Expose
    @SerializedName("areaNameEn")
    private String areaNameEn;
    @Expose
    @SerializedName("cityNameAr")
    private String cityNameAr;
    @Expose
    @SerializedName("cityNameEn")
    private String cityNameEn;
    @Expose
    @SerializedName("totalCost")
    private double totalCost;
    @Expose
    @SerializedName("totalDiscount")
    private double totalDiscount;
    @Expose
    @SerializedName("applicationDiscountValue")
    private double applicationDiscountValue;
    @Expose
    @SerializedName("initialCost")
    private int initialCost;
    @Expose
    @SerializedName("itemsCost")
    private int itemsCost;
    @Expose
    @SerializedName("cashOnDeliveryFees")
    private int cashOnDeliveryFees;
    @Expose
    @SerializedName("totalDeliveryCost")
    private int totalDeliveryCost;
    @Expose
    @SerializedName("cityDeliveryCost")
    private int cityDeliveryCost;
    @Expose
    @SerializedName("tax")
    private double tax;
    @Expose
    @SerializedName("taxPercentage")
    private int taxPercentage;
    @Expose
    @SerializedName("cancellationCost")
    private int cancellationCost;
    @Expose
    @SerializedName("totalReattemptCost")
    private int totalReattemptCost;
    @Expose
    @SerializedName("reattemptCount")
    private int reattemptCount;
    @Expose
    @SerializedName("reattemptCost")
    private int reattemptCost;
    @Expose
    @SerializedName("applicationDiscount")
    private int applicationDiscount;
    @Expose
    @SerializedName("applicationDiscountType")
    private String applicationDiscountType;
    @Expose
    @SerializedName("deliveryTo")
    private String deliveryTo;
    @Expose
    @SerializedName("deliveryFrom")
    private String deliveryFrom;
    @Expose
    @SerializedName("deliveryDate")
    private String deliveryDate;
    @Expose
    @SerializedName("pickupTo")
    private String pickupTo;
    @Expose
    @SerializedName("pickupFrom")
    private String pickupFrom;
    @Expose
    @SerializedName("pickupDate")
    private String pickupDate;
    @Expose
    @SerializedName("note")
    private String note;
    @Expose
    @SerializedName("status")
    private String status;
    @Expose
    @SerializedName("transportationType")
    private String transportationType;
    @Expose
    @SerializedName("type")
    private String type;
    @Expose
    @SerializedName("createdDate")
    private String createdDate;
    @Expose
    @SerializedName("referenceNumber")
    private String referenceNumber;
    @Expose
    @SerializedName("id")
    private int id;

    @Data
    public static class PromoCodeEntity
        {
        @Expose
        @SerializedName("clientName")
        private String clientName;
        @Expose
        @SerializedName("clientId")
        private int clientId;
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
        @SerializedName("used")
        private boolean used;
        @Expose
        @SerializedName("value")
        private int value;
        @Expose
        @SerializedName("discountType")
        private String discountType;
        @Expose
        @SerializedName("expirationDate")
        private String expirationDate;
        @Expose
        @SerializedName("days")
        private int days;
        @Expose
        @SerializedName("start")
        private String start;
        @Expose
        @SerializedName("code")
        private String code;
        @Expose
        @SerializedName("id")
        private int id;
        }

    @Data
    public static class PaymentProcessEntity
        {
        @Expose
        @SerializedName("receivedBy")
        private String receivedBy;
        @Expose
        @SerializedName("receivedById")
        private int receivedById;
        @Expose
        @SerializedName("done")
        private boolean done;
        @Expose
        @SerializedName("total")
        private double total;
        @Expose
        @SerializedName("type")
        private String type;
        @Expose
        @SerializedName("date")
        private String date;
        @Expose
        @SerializedName("id")
        private int id;
        }
    }