package com.example.a5asec.data.model.api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.Data;

@Data
public  class Order
    {

    @Expose
    @SerializedName("type")
    private String type;
    @Expose
    @SerializedName("transportationType")
    private String transportationType;
    @Expose
    @SerializedName("totalReattemptCost")
    private int totalReattemptCost;
    @Expose
    @SerializedName("totalDiscount")
    private int totalDiscount;
    @Expose
    @SerializedName("totalDeliveryCost")
    private int totalDeliveryCost;
    @Expose
    @SerializedName("totalCost")
    private int totalCost;
    @Expose
    @SerializedName("taxPercentage")
    private int taxPercentage;
    @Expose
    @SerializedName("tax")
    private int tax;
    @Expose
    @SerializedName("systemNote")
    private String systemNote;
    @Expose
    @SerializedName("street")
    private String street;
    @Expose
    @SerializedName("status")
    private String status;
    @Expose
    @SerializedName("referenceNumber")
    private String referenceNumber;
    @Expose
    @SerializedName("reattemptCount")
    private int reattemptCount;
    @Expose
    @SerializedName("reattemptCost")
    private int reattemptCost;
    @Expose
    @SerializedName("promoCode")
    private PromoCodeEntity promoCode;
    @Expose
    @SerializedName("pickupTo")
    private PickupToEntity pickupTo;
    @Expose
    @SerializedName("pickupFrom")
    private PickupFromEntity pickupFrom;
    @Expose
    @SerializedName("pickupDate")
    private String pickupDate;
    @Expose
    @SerializedName("paymentProcess")
    private PaymentProcessEntity paymentProcess;
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
    @SerializedName("itemsCount")
    private int itemsCount;
    @Expose
    @SerializedName("itemsCost")
    private int itemsCost;
    @Expose
    @SerializedName("initialCost")
    private int initialCost;
    @Expose
    @SerializedName("id")
    private int id;
    @Expose
    @SerializedName("floor")
    private String floor;
    @Expose
    @SerializedName("deliveryTo")
    private DeliveryToEntity deliveryTo;
    @Expose
    @SerializedName("deliveryFrom")
    private DeliveryFromEntity deliveryFrom;
    @Expose
    @SerializedName("deliveryDate")
    private String deliveryDate;
    @Expose
    @SerializedName("createdDate")
    private String createdDate;
    @Expose
    @SerializedName("courierName")
    private String courierName;
    @Expose
    @SerializedName("courierMobile")
    private String courierMobile;
    @Expose
    @SerializedName("courierId")
    private int courierId;
    @Expose
    @SerializedName("clientName")
    private String clientName;
    @Expose
    @SerializedName("clientMobile")
    private String clientMobile;
    @Expose
    @SerializedName("clientId")
    private int clientId;
    @Expose
    @SerializedName("cityNameEn")
    private String cityNameEn;
    @Expose
    @SerializedName("cityNameAr")
    private String cityNameAr;
    @Expose
    @SerializedName("cityDeliveryCost")
    private int cityDeliveryCost;
    @Expose
    @SerializedName("cashOnDeliveryFees")
    private int cashOnDeliveryFees;
    @Expose
    @SerializedName("cancellationCost")
    private int cancellationCost;
    @Expose
    @SerializedName("building")
    private String building;
    @Expose
    @SerializedName("areaNameEn")
    private String areaNameEn;
    @Expose
    @SerializedName("areaNameAr")
    private String areaNameAr;
    @Expose
    @SerializedName("applicationDiscountValue")
    private int applicationDiscountValue;
    @Expose
    @SerializedName("applicationDiscountType")
    private String applicationDiscountType;
    @Expose
    @SerializedName("applicationDiscount")
    private int applicationDiscount;
    @Expose
    @SerializedName("apartment")
    private String apartment;

    @Data
    public static class PromoCodeEntity
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

    @Data
    public static class PickupToEntity
        {
        @Expose
        @SerializedName("year")
        private int year;
        @Expose
        @SerializedName("timezoneOffset")
        private int timezoneOffset;
        @Expose
        @SerializedName("time")
        private int time;
        @Expose
        @SerializedName("seconds")
        private int seconds;
        @Expose
        @SerializedName("month")
        private int month;
        @Expose
        @SerializedName("minutes")
        private int minutes;
        @Expose
        @SerializedName("hours")
        private int hours;
        @Expose
        @SerializedName("day")
        private int day;
        @Expose
        @SerializedName("date")
        private int date;
        }

    @Data
    public static class PickupFromEntity
        {
        @Expose
        @SerializedName("year")
        private int year;
        @Expose
        @SerializedName("timezoneOffset")
        private int timezoneOffset;
        @Expose
        @SerializedName("time")
        private int time;
        @Expose
        @SerializedName("seconds")
        private int seconds;
        @Expose
        @SerializedName("month")
        private int month;
        @Expose
        @SerializedName("minutes")
        private int minutes;
        @Expose
        @SerializedName("hours")
        private int hours;
        @Expose
        @SerializedName("day")
        private int day;
        @Expose
        @SerializedName("date")
        private int date;
        }

    @Data
    public static class PaymentProcessEntity
        {
        @Expose
        @SerializedName("type")
        private String type;
        @Expose
        @SerializedName("total")
        private int total;
        @Expose
        @SerializedName("receivedById")
        private int receivedById;
        @Expose
        @SerializedName("receivedBy")
        private String receivedBy;
        @Expose
        @SerializedName("paymentOrderId")
        private String paymentOrderId;
        @Expose
        @SerializedName("note")
        private String note;
        @Expose
        @SerializedName("id")
        private int id;
        @Expose
        @SerializedName("done")
        private boolean done;
        @Expose
        @SerializedName("date")
        private String date;
        }

    @Data
    public static class DeliveryToEntity
        {
        @Expose
        @SerializedName("year")
        private int year;
        @Expose
        @SerializedName("timezoneOffset")
        private int timezoneOffset;
        @Expose
        @SerializedName("time")
        private int time;
        @Expose
        @SerializedName("seconds")
        private int seconds;
        @Expose
        @SerializedName("month")
        private int month;
        @Expose
        @SerializedName("minutes")
        private int minutes;
        @Expose
        @SerializedName("hours")
        private int hours;
        @Expose
        @SerializedName("day")
        private int day;
        @Expose
        @SerializedName("date")
        private int date;
        }

    @Data
    public static class DeliveryFromEntity
        {
        @Expose
        @SerializedName("year")
        private int year;
        @Expose
        @SerializedName("timezoneOffset")
        private int timezoneOffset;
        @Expose
        @SerializedName("time")
        private int time;
        @Expose
        @SerializedName("seconds")
        private int seconds;
        @Expose
        @SerializedName("month")
        private int month;
        @Expose
        @SerializedName("minutes")
        private int minutes;
        @Expose
        @SerializedName("hours")
        private int hours;
        @Expose
        @SerializedName("day")
        private int day;
        @Expose
        @SerializedName("date")
        private int date;
        }
    }
