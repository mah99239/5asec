package com.example.a5asec.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public  class Authorization
{

  @Expose
  @SerializedName("user")
  private Users user;
  @Expose
  @SerializedName("authorization")
  private AuthorizationEntity authorization;

public Authorization (String ref)
{
}
  public Users getUser()
  {
    return user;
  }

  public AuthorizationEntity getAuthorization()
  {
    return authorization;
  }

  public static class AuthorizationEntity
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
public AuthorizationEntity (String refresh_token)
{
  this.refresh_token = refresh_token;
}
    public String getScope()
    {
      return scope;
    }

    public int getExpires_in()
    {
      return expires_in;
    }

    public String getRefresh_token()
    {
      return refresh_token;
    }

    public String getToken_type()
    {
      return token_type;
    }

    public String getAccess_token()
    {
      return access_token;
    }

    @Override public String toString()
    {
      return "AuthorizationEntity{" +
          "scope='" + scope + '\'' +
          ", expires_in=" + expires_in +
          ", refresh_token='" + refresh_token + '\'' +
          ", token_type='" + token_type + '\'' +
          ", access_token='" + access_token + '\'' +
          '}';
    }
  }

  @Override public String toString()
  {
    return "Authorization{" +
        "user=" + user +
        ", authorization=" + authorization +
        '}';
  }
}