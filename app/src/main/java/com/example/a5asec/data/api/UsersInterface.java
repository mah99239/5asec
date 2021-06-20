package com.example.a5asec.data.api;

import com.example.a5asec.data.model.Authorization;
import com.example.a5asec.data.model.Users;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface UsersInterface
{
  //  Call<Users> getUsers(@Header("Authorization")String authToken @Path("email") String email,
  //  @Path("login") String login);

  // @POST("/api/client/account/authenticate")  Call<Users> getUsers(@Body Users users);

  @POST("api/client/account/authenticate")
  Call<Authorization> authenticateUsers(@Body Users users);

  @POST("api/client/account/register")
  Call<Users> registerUsers(@Body Users users);

  @GET("/api/client/account")
  Call<Users> getAccount();

  @POST("/api/client/account/refresh")
  Call<Authorization.AuthorizationEntity> refreshToken(@Body RequestBody refreshToken);

}
