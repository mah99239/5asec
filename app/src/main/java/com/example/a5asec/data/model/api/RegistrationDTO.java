package com.example.a5asec.data.model.api;

import com.google.gson.annotations.SerializedName;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

@Data
public class RegistrationDTO
    {

    @SerializedName("password")
    private String password;

    @SerializedName("mobile")
    private String mobile;

    @SerializedName("langKey")
    private String langKey;

    @Setter(AccessLevel.NONE)
    @SerializedName("gender")
    private String gender;

    @SerializedName("fullName")
    private String fullName;

    @SerializedName("email")
    private String email;

    @SerializedName("birthDate")
    private String birthDate;

    public RegistrationDTO(String fullName, String email, String password, String mobile, String gender,
                           String birthDate, String langKey)
        {

        this.fullName = fullName;
        this.email = email;
        this.password = password;
        this.mobile = mobile;
        setGender(gender);
        this.birthDate = birthDate;
        this.langKey = langKey;
        }

    public void setGender(String gender)
        {
        if (gender.equals("MALE") || gender.equals("ذكر"))
            {
            gender = Users.Gender.MALE.toString();
            } else if (gender.equals("FEMALE") || gender.equals("انثى"))
            {
            gender = Users.Gender.FEMALE.toString();
            } else
            {
            gender = Users.Gender.NOT_SPECIFIED.toString();
            }

        this.gender = gender;
        }

    }
