package com.example.a5asec.ui.main.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.View;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;

import androidx.lifecycle.ProcessLifecycleOwner;
import com.example.a5asec.R;
import com.example.a5asec.data.api.UsersClient;
import com.example.a5asec.data.local.TokenPreferences;
import com.example.a5asec.databinding.ActivityMainBinding;
import com.example.a5asec.data.model.Authorization;
import com.example.a5asec.data.model.Users;
import com.example.a5asec.utility.HandleMessage;
import com.example.a5asec.utility.NetworkConnection;
import com.google.android.material.snackbar.Snackbar;

import org.jetbrains.annotations.NotNull;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created on : June 3, 2021
 * Author     : MahmoudZ98
 * Email      : mahmoud99239@gmail.com
 */
public class MainActivity extends AppCompatActivity implements LifecycleObserver
{
  private static final String TAG = "MainActivity";

  ActivityMainBinding mBinding;
  TokenPreferences tokenPreferences;
  HandleMessage.Presenter presenter;

  @RequiresApi(api = Build.VERSION_CODES.KITKAT) @Override
  protected void onCreate(Bundle savedInstanceState)
  {
    tokenPreferences = new TokenPreferences(getApplicationContext());
    super.onCreate(savedInstanceState);
    //this initialize view with DataBindingUtil
    mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
    mBinding.btnLoginSignup.setOnClickListener(v ->
    {
      Intent weatherDetailIntent = new Intent(MainActivity.this, SignUpActivity.class);
      startActivity(weatherDetailIntent);
    });
    mBinding.btnLoginLogin.setOnClickListener(v -> isValidate());
    mBinding.btnLoginSkip.setOnClickListener(v -> getLogin());

    ProcessLifecycleOwner.get().getLifecycle().addObserver(this);
  }

  @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
  public void networkAvailable()
  {
    NetworkConnection networkConnection = new NetworkConnection(this);
    networkConnection.observe(this, isConnceted ->
    {
      if (!isConnceted)
      {
        mBinding.btnLoginLogin.setVisibility(View.INVISIBLE);
        mBinding.btnLoginSignup.setVisibility(View.INVISIBLE);
        mBinding.btnLoginSkip.setVisibility(View.INVISIBLE);
      }
      else
      {
        mBinding.btnLoginLogin.setVisibility(View.VISIBLE);
        mBinding.btnLoginSignup.setVisibility(View.VISIBLE);
        mBinding.btnLoginSkip.setVisibility(View.VISIBLE);
      }
    });
  }

  @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
  private void checkUsers()
  {
    final int redColor;
    final int validColor;

    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M)
    {
      validColor = getResources().getColor(R.color.valid, getTheme());
      redColor = getResources().getColor(R.color.error, getTheme());
    }
    else
    {
      validColor = getResources().getColor(R.color.valid);
      redColor = getResources().getColor(R.color.error);
    }

    mBinding.etLoginEmail.addTextChangedListener(new TextWatcher()
    {
      @Override
      public void beforeTextChanged(CharSequence s, int start, int count, int after)
      {
        // Do nothing because not change before text
      }

      @Override
      public void onTextChanged(CharSequence s, int start, int before, int count)
      {
        if (TextUtils.isEmpty(s))
        {
          mBinding.tilLoginEmail.setBoxStrokeColor(redColor);
          mBinding.tilLoginEmail.setError(null);
        }
        else if (!s.toString().trim().matches(String.valueOf(Patterns.EMAIL_ADDRESS)))
        {
          mBinding.tilLoginEmail.setError("Enter valid email");
          mBinding.tilLoginEmail.setBoxStrokeColor(redColor);
        }
        else
        {
          mBinding.tilLoginEmail.setBoxStrokeColor(validColor);
          mBinding.tilLoginEmail.setError(null);
        }
      }

      @Override
      public void afterTextChanged(Editable s)
      {

      }
    });
    mBinding.etPassword.addTextChangedListener(new TextWatcher()
    {
      @Override
      public void beforeTextChanged(CharSequence s, int start, int count, int after)
      {

      }

      @Override
      public void onTextChanged(CharSequence s, int start, int before, int count)
      {
        if (TextUtils.isEmpty(s))
        {
          mBinding.tilLoginPassword.setBoxStrokeColor(redColor);
        }

        else
        {
          mBinding.tilLoginPassword.setError(null);

          mBinding.tilLoginPassword.setBoxStrokeColor(validColor);
        }
      }

      @Override
      public void afterTextChanged(Editable s)
      {

      }
    });
  }

  private static String token;
  String refreshToken;

  public void login(Users login)
  {

    loading();

    UsersClient.getInstance().authenticateUsers(login, new Callback<Authorization>()
    {
      @Override public void onResponse(@NotNull Call<Authorization> call,
          @NotNull Response<Authorization> response)
      {
        mBinding.piLogin.setVisibility(View.INVISIBLE);
        Authorization responseAuthorization = response.body();

        if (response.code() == 200 && responseAuthorization != null)
        {
          messageSnackbar(response.message());
          token = responseAuthorization.getAuthorization().getAccess_token();
          refreshToken = responseAuthorization.getAuthorization().getRefresh_token();
          long expireIn = responseAuthorization.getAuthorization().getExpires_in();

          TokenPreferences.setToken(token, refreshToken, expireIn);
          Log.e(TAG, "login().onResponse() - message" + response.message());
          call.cancel();
        }
        else
        {
          Log.e(TAG, "login().onResponse() - message" + response.message());
          call.cancel();
        }
      }

      @Override public void onFailure(Call<Authorization> call, Throwable t)
      {
        mBinding.piLogin.setVisibility(View.INVISIBLE);
        Log.e(TAG, "login().onFailure() - message" + t.getMessage());
        messageSnackbar(t.getMessage());
        call.cancel();
      }
    });
  }

  public void getLogin()
  {
    String a = tokenPreferences.getPrefRefreshToken();

    Log.e(TAG + "getLogin", a + "a");

    UsersClient.getInstance().getAccount(new Callback<Users>()
    {
      @Override public void onResponse(Call<Users> call, Response<Users> response)
      {

        Users users = response.body();
        if (response.code() == 200 && users != null)
        {

          call.cancel();
          Log.e(TAG, "getLogin().onResponse() - condition is code  equal to 200 message"
              + response.message());
        }
        else
        {
          Log.e(TAG, "getLogin().onResponse() - condition is code not equal to 200 message"
              + response.message());
          call.cancel();
        }
      }

      @Override public void onFailure(Call<Users> call, Throwable t)
      {
        Log.e(TAG, "getLogin().onFailure() -  message" + t.getMessage());
        messageSnackbar(t.getMessage());
        call.cancel();
      }
    });
  }

  void messageSnackbar(String message)
  {

    //COMPLETE language set english and arabic.

    if (message.contains("size=104"))
    {
      message = "Inactive account";
    }
    else if (message.contains("size=80"))
    {
      message = "Invalid username or password";
    }

    Snackbar.make(mBinding.clLoginRoot, message, Snackbar.LENGTH_LONG).setDuration(3000)
        .setBackgroundTint(getResources().getColor(R.color.error))
        .setAnchorView(mBinding.btnLoginLogin).show();
  }

  private boolean isValidate()
  {
    String login = mBinding.etLoginEmail.getText().toString();
    String password = mBinding.etPassword.getText().toString();
    mBinding.etLoginEmail.setError(null);
    mBinding.etPassword.setError(null);
    if ((TextUtils.isEmpty(login) || !login.toString()
        .trim()
        .matches(String.valueOf(Patterns.EMAIL_ADDRESS)))
        || TextUtils.isEmpty(password))
    {
      if (TextUtils.isEmpty(login))
      {
        mBinding.etLoginEmail.setError("email is empty");
      }
      if (TextUtils.isEmpty(password))
      {
        mBinding.etPassword.setError("Password is empty");
      }
      // return false;
    }
    //  Login users = new Login(login, password);
    Users users = new Users("mahmoud99239@gmail.com", "12345678");
    Log.e("login user", "ok");
    login(users);
    return true;
  }

  private void loading()
  {
    mBinding.piLogin.setVisibility(View.VISIBLE);
  }

  @Override
  public void onBackPressed()
  {
    this.finishAffinity();
  }

}