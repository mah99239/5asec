package com.example.a5asec.data.api;

import android.util.Log;
import com.example.a5asec.data.model.Authorization;
import com.example.a5asec.data.model.Users;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
public class UsersClient extends Client
{

private static UsersClient INSTANCE;
private static UsersInterface usersInterface = null;

public UsersClient()
{
  usersInterface = getRetrofit().create(UsersInterface.class);
}

public static UsersClient getInstance()
{
  return null == INSTANCE ? new UsersClient() : INSTANCE;
}

  /**
   *
   * @param users
   * @return users
   */
  public Call<Users> registerUser(Users users)
{
  return usersInterface.registerUsers(users);
}

  public void authenticateUsers(Users users , Callback<Authorization> callback)
  {
    Call<Authorization> usersCall = usersInterface.authenticateUsers(users);
    usersCall.enqueue(callback);
  }

public void getAccount(  Callback<Users>callback)
{
  Call<Users> usersCall = usersInterface.getAccount();
  usersCall.enqueue(callback);
}
public Call<Authorization.AuthorizationEntity> refreshToken( RequestBody token)
{
  Call<Authorization.AuthorizationEntity> authorizationCall = usersInterface.refreshToken(token);
  return authorizationCall;
}
  }

