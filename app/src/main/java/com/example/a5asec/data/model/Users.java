package com.example.a5asec.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.Locale;

public class Users
{
  @Expose
  @SerializedName("mobileVerified")
  private boolean mobileVerified;
  @Expose
  @SerializedName("emailVerified")
  private boolean emailVerified;
  @Expose
  @SerializedName("acceptNotifications")
  private boolean acceptNotifications;
  @Expose
  @SerializedName("authorities")
  private List<String> authorities;
  @Expose
  @SerializedName("lastModifiedDate")
  private String lastModifiedDate;
  @Expose
  @SerializedName("lastModifiedBy")
  private String lastModifiedBy;
  @Expose
  @SerializedName("createdDate")
  private String createdDate;
  @Expose
  @SerializedName("createdBy")
  private String createdBy;
  @Expose
  @SerializedName("langKey")
  private String langKey;
  @Expose
  @SerializedName("activated")
  private boolean activated;
  @Expose
  @SerializedName("gender")
  private String gender;
  @Expose
  @SerializedName("birthDate")
  private String birthDate;
  @Expose
  @SerializedName("mobile")
  private String mobile;
  @Expose
  @SerializedName("email")
  private String email;
  @Expose
  @SerializedName("fullName")
  private String fullName;
  @Expose
  @SerializedName("deviceId")
  private String deviceId;
  @Expose
  @SerializedName("type")
  private String type;
  @Expose
  @SerializedName("login")
  private String login;
  @Expose
  @SerializedName("id")
  private int id;
  @Expose
  @SerializedName("password")
  private String password;

  public enum LanKey
  {EN, AR}

  public enum Gender
  {MALE, FEMALE, NOT_SPECIFIED}
public  Users (){}
  public Users(String fullName, String email, String password, String mobile, String gender,
      String birthDate)

  {
    this.fullName = fullName;
    this.email = email;
    this.password = password;
    this.mobile = mobile;
    setGender(gender);
    this.birthDate = birthDate;
    this.langKey = setLangKey();
  }

  public Users(String login, String password)
  {
    this.login = login;
    this.password = password;
  }

  public String getPassword()
  {
    return password;
  }

  public void setPassword(String password)
  {
    this.password = password;
  }

  public String getMobile()
  {
    return mobile;
  }

  public void setMobile(String mobile)
  {
    this.mobile = mobile;
  }

  public String getLangKey()
  {
    return langKey;
  }

  public String setLangKey()
  {
    String lan = Locale.getDefault().getLanguage().trim();
    return lan.equals(LanKey.AR.toString()) ? LanKey.AR.toString() : LanKey.EN.toString();
  }

  public String getGender()
  {

    return gender;
  }

  public void setGender(String gender)
  {
    if (gender.equals("MALE"))
    {
      gender = Gender.MALE.toString();
    }
    else if (gender.equals("FEMALE"))
    {
      gender = Gender.FEMALE.toString();
    }
    else
    {
      gender = Gender.NOT_SPECIFIED.toString();
    }

    this.gender = gender;
  }

  @Override public String toString()
  {
    return "Users{" +
        "mobileVerified=" + mobileVerified +
        ", emailVerified=" + emailVerified +
        ", acceptNotifications=" + acceptNotifications +
        ", authorities=" + authorities +
        ", lastModifiedDate='" + lastModifiedDate + '\'' +
        ", lastModifiedBy='" + lastModifiedBy + '\'' +
        ", createdDate='" + createdDate + '\'' +
        ", createdBy='" + createdBy + '\'' +
        ", langKey='" + langKey + '\'' +
        ", activated=" + activated +
        ", gender='" + gender + '\'' +
        ", birthDate='" + birthDate + '\'' +
        ", mobile='" + mobile + '\'' +
        ", email='" + email + '\'' +
        ", fullName='" + fullName + '\'' +
        ", deviceId='" + deviceId + '\'' +
        ", type='" + type + '\'' +
        ", login='" + login + '\'' +
        ", id=" + id +
        ", password='" + password + '\'' +
        '}';
  }
}
