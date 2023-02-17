package com.example.a5asec.data.model.api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.Data;

@Data
public  class Authorization
    {

    @Expose
    @SerializedName("user")
    private Users user;
    @Expose
    @SerializedName("authorization")
    private AuthorizationEntity authorization;


    @Data
    public  class AuthorizationEntity
        {

        @Expose
        @SerializedName("scope")
        private String scope;
        @Expose
        @SerializedName("expires_in")
        private int expires_in;
        @Expose
        @SerializedName("refresh_token")
        private String refresh_token;
        @Expose
        @SerializedName("token_type")
        private String token_type;
        @Expose
        @SerializedName("access_token")
        private String access_token;
        }

    }