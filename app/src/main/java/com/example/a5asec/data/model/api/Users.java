package com.example.a5asec.data.model.api;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;


@Data
public class Users
    {

    @Expose
    @SerializedName("type")
    private String type;
    @Expose
    @SerializedName("mobileVerified")
    private boolean mobileVerified;
    @Expose
    @SerializedName("mobile")
    private String mobile;
    @Expose
    @SerializedName("login")
    private String login;
    @Expose
    @SerializedName("lastModifiedDate")
    private String lastModifiedDate;
    @Expose
    @SerializedName("lastModifiedBy")
    private String lastModifiedBy;
    @Getter
    @Expose
    @SerializedName("langKey")
    private String langKey;
    @Expose
    @SerializedName("id")
    private int id;
    @Expose
    @SerializedName("gender")
    private String gender;
    @Expose
    @SerializedName("fullName")
    private String fullName;
    @Expose
    @SerializedName("emailVerified")
    private boolean emailVerified;
    @Expose
    @SerializedName("email")
    private String email;
    @Expose
    @SerializedName("deviceId")
    private String deviceId;
    @Expose
    @SerializedName("createdDate")
    private String createdDate;
    @Expose
    @SerializedName("createdBy")
    private String createdBy;
    @Expose
    @SerializedName("cityEn")
    private String cityEn;
    @Expose
    @SerializedName("cityAr")
    private String cityAr;
    @Expose
    @SerializedName("birthDate")
    private String birthDate;
    @Expose
    @SerializedName("authorities")
    private List<String> authorities;
    @Expose
    @SerializedName("areaEn")
    private String areaEn;
    @Expose
    @SerializedName("areaAr")
    private String areaAr;
    @Expose
    @SerializedName("activated")
    private boolean activated;
    @Expose
    @SerializedName("acceptNotifications")
    private boolean acceptNotifications;

    @Expose
    @SerializedName("password")
    private String password;

    @Getter(AccessLevel.NONE)
    private String city;

    @Getter(AccessLevel.NONE)
    private String area;

    public Users(String fullName, String email, String password, String mobile, String gender,
                 String birthDate)

        {
        this.fullName = fullName;
        this.email = email;
        this.password = password;
        this.mobile = mobile;
        setGender(gender);
        this.birthDate = birthDate;
        this.langKey = getLangKey();


        }

    public Users(String login, String password)
        {
        this.login = login;
        this.password = password;
        }

    public String getCity(@NonNull String language)
        {
        return language.equals("en") ? cityEn : cityAr;
        }

    public String getArea(@NonNull String language)
        {
        return language.equals("en") ? areaEn : areaAr;
        }



    public String getPassword()
        {
        return password;
        }

    public String setLangKey()
        {
        String lan = AppCompatDelegate.getApplicationLocales().toLanguageTags();
        return lan;
        }

    public void setGender(String gender)
        {
        if (gender.equals("MALE"))
            {
            gender = Gender.MALE.toString();
            } else if (gender.equals("FEMALE"))
            {
            gender = Gender.FEMALE.toString();
            } else
            {
            gender = Gender.NOT_SPECIFIED.toString();
            }

        this.gender = gender;
        }

    public enum LanKey
        {EN, AR}


    public enum Gender
        {MALE, FEMALE, NOT_SPECIFIED}


    }
