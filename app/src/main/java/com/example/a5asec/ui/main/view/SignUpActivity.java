package com.example.a5asec.ui.main.view;

import android.app.Activity;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;

import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.ArrayAdapter;


import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.lifecycle.ProcessLifecycleOwner;

import com.example.a5asec.R;
import com.example.a5asec.data.api.UsersClient;
import com.example.a5asec.databinding.ActivitySignupBinding;
import com.example.a5asec.data.model.Users;
import com.example.a5asec.utility.HandleMessage;
import com.example.a5asec.utility.NetworkConnection;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Arrays;
import org.jetbrains.annotations.NotNull;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpActivity extends AppCompatActivity implements LifecycleObserver, HandleMessage.View
{
  ActivitySignupBinding mBinding;
  String mMessageResponse = "";
  DataPickerFragment mDataPickerFragment;

  @Override
  protected void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);

    //initialize ActivityMainBinding
    mBinding = DataBindingUtil.setContentView(this, R.layout.activity_signup);
    mDataPickerFragment =
        new DataPickerFragment(mBinding.includeSignUpDetail.etDetailBirthdate);
    ProcessLifecycleOwner.get().getLifecycle().addObserver(this);
    mBinding.buttonSignUpSign.setOnClickListener(v ->
        //        isValid()
        isValid()

    );
  }

  @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
  public void checkUserSignUp()
  {
    //c: set language in TextView

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

    final int boxStrokeError = (int) (4 * Resources.getSystem().getDisplayMetrics().density);
    final int boxStrokeDefault = (int) (2 * Resources.getSystem().getDisplayMetrics().density);

    final String[] password = { null };

    mBinding.etSignUpName.addTextChangedListener(new TextWatcher()
    {

      @Override
      public void beforeTextChanged(CharSequence s, int start, int count, int after)
      {
        mBinding.tlSignUpName.setBoxStrokeWidthFocused(boxStrokeDefault);
      }

      @Override
      public void onTextChanged(CharSequence s, int start, int before, int count)
      {
                /*IF name  isEmpty
                     add color red and edit boxStrokeWidth to 4 in editText
                 */
        if (TextUtils.isEmpty(s))
        {
          mBinding.tlSignUpName.setBoxStrokeColor(redColor);
          mBinding.tlSignUpName.setBoxStrokeWidthFocused(boxStrokeError);
        }
        else
        {
          mBinding.tlSignUpName.setBoxStrokeColor(validColor);
        }
      }

      @Override
      public void afterTextChanged(Editable s)
      {
        mBinding.tlSignUpName.setBoxStrokeWidthFocused(boxStrokeDefault);
      }
    });
    mBinding.etSignUpEmail.addTextChangedListener(new TextWatcher()
    {
      @Override
      public void beforeTextChanged(CharSequence s, int start, int count, int after)
      {
        mBinding.tlSignUpEmail.setBoxStrokeWidthFocused(boxStrokeDefault);
      }

      @Override
      public void onTextChanged(CharSequence s, int start, int before, int count)
      {
        if (TextUtils.isEmpty(s))
        {
          mBinding.tlSignUpEmail.setBoxStrokeColor(redColor);
          mBinding.tlSignUpEmail.setBoxStrokeWidthFocused(boxStrokeError);
          mBinding.tlSignUpEmail.setError(null);
        }
        else if (!s.toString().trim().matches(String.valueOf(Patterns.EMAIL_ADDRESS)))
        {
          mBinding.tlSignUpEmail.setError("Enter vaild email");

          mBinding.tlSignUpEmail.setBoxStrokeColor(redColor);
        }
        else
        {
          mBinding.tlSignUpEmail.setBoxStrokeColor(validColor);
          mBinding.tlSignUpEmail.setError(null);
        }
      }

      @Override
      public void afterTextChanged(Editable s)
      {

      }
    });
    mBinding.etSignUpPhone.addTextChangedListener(new TextWatcher()
    {
      @Override
      public void beforeTextChanged(CharSequence s, int start, int count, int after)
      {
        mBinding.tlSignUpPhone.setBoxStrokeWidthFocused(boxStrokeDefault);
      }

      @Override
      public void onTextChanged(CharSequence s, int start, int before, int count)
      {

        if (TextUtils.isEmpty(s))
        {
          mBinding.tlSignUpPhone.setBoxStrokeColor(redColor);
          mBinding.tlSignUpPhone.setBoxStrokeWidthFocused(boxStrokeError);
        }
        else if (!s.toString().trim().matches(String.valueOf(Patterns.PHONE)))
        {
          mBinding.tlSignUpPhone.setError("check");
          mBinding.tlSignUpPhone.setBoxStrokeColor(redColor);
        }
        else
        {
          mBinding.tlSignUpPhone.setBoxStrokeColor(validColor);
          mBinding.tlSignUpPhone.setError(null);
        }
      }

      @Override
      public void afterTextChanged(Editable s)
      {
        mBinding.tlSignUpPhone.setError(null);
      }
    });
    mBinding.etSignUpPass.addTextChangedListener(new TextWatcher()
    {
      @Override
      public void beforeTextChanged(CharSequence s, int start, int count, int after)
      {
        mBinding.tlSignUpPass.setBoxStrokeWidthFocused(boxStrokeDefault);
      }

      @Override
      public void onTextChanged(CharSequence s, int start, int before, int count)
      {
        if (TextUtils.isEmpty(s))
        {
          mBinding.tlSignUpPass.setBoxStrokeColor(redColor);
          mBinding.tlSignUpPass.setBoxStrokeWidthFocused(boxStrokeError);
        }
        else if (s.length() < 8)
        {
          mBinding.tlSignUpPass.setError("Password must be at least 8 characters.");
        }
        else
        {
          mBinding.tlSignUpPass.setError(null);

          mBinding.tlSignUpPass.setBoxStrokeColor(validColor);
        }
      }

      @Override
      public void afterTextChanged(Editable s)
      {
        password[0] = s.toString().trim();
      }
    });
    mBinding.etSignUpConfirmPass.addTextChangedListener(new TextWatcher()
    {
      @Override
      public void beforeTextChanged(CharSequence s, int start, int count, int after)
      {
        mBinding.tlSignUpConfirmPass.setBoxStrokeWidthFocused(boxStrokeDefault);
      }

      @Override
      public void onTextChanged(CharSequence s, int start, int before, int count)
      {
        if (TextUtils.isEmpty(s))
        {
          mBinding.tlSignUpConfirmPass.setBoxStrokeColor(redColor);
          mBinding.tlSignUpConfirmPass.setBoxStrokeWidthFocused(boxStrokeError);
        }
        else if (!s.toString().trim().matches(String.valueOf(password[0])))
        {

          mBinding.tlSignUpConfirmPass.setError("Don't match");
        }
        else
        {
          mBinding.tlSignUpConfirmPass.setBoxStrokeColor(validColor);
        }
        if (s.toString().trim().matches(String.valueOf(password[0])))
        {
          mBinding.tlSignUpConfirmPass.setError(null);
        }
      }

      @Override
      public void afterTextChanged(Editable s)
      {

      }
    });
  }

  private boolean isValid()
  {

    String name = mBinding.etSignUpName.getText().toString();
    String email = mBinding.etSignUpEmail.getText().toString();
    String phone = mBinding.etSignUpPhone.getText().toString();
    String password = mBinding.etSignUpPass.getText().toString();
    String confirmPassword = mBinding.etSignUpConfirmPass.getText().toString();
    String gender = mBinding.includeSignUpDetail.autoDetailGender.getText().toString();
    boolean isCheck = mBinding.includeSignUpDetail.includeDetailPrivacy.cbPrivacy.isChecked();

    mBinding.tlSignUpName.setError(null);
    mBinding.tlSignUpEmail.setError(null);
    mBinding.tlSignUpPhone.setError(null);
    mBinding.tlSignUpPass.setError(null);
    mBinding.tlSignUpConfirmPass.setError(null);

    if (TextUtils.isEmpty(name)
        || (TextUtils.isEmpty(email) ||
        !email.matches(String.valueOf(Patterns.EMAIL_ADDRESS)))
        || TextUtils.isEmpty(phone)
        || TextUtils.isEmpty(password)
        ||
        (TextUtils.isEmpty(confirmPassword) || !confirmPassword.matches(password))
        ||
        !isCheck)
    {
      if (TextUtils.isEmpty(name))
      {
        mBinding.tlSignUpName.setError("name is null");
      }
      if (TextUtils.isEmpty(email) || !email.matches(String.valueOf(Patterns.EMAIL_ADDRESS)))
      {
        mBinding.tlSignUpEmail.setError("check email");
      }
      if (TextUtils.isEmpty(phone))
      {
        mBinding.tlSignUpPhone.setError("check phone");
      }
      if (TextUtils.isEmpty(password))
      {
        mBinding.tlSignUpPass.setError("check password");
      }
      if (TextUtils.isEmpty(confirmPassword) || !confirmPassword.matches(password))
      {
        mBinding.tlSignUpConfirmPass.setError("check confirm password");
      }
      if (!isCheck)
      {
        mBinding.includeSignUpDetail.includeDetailPrivacy.cbPrivacy.setButtonTintList(
            ColorStateList.valueOf(Color.RED));
      }
      return false;
    }

    // optimize: add sign chcek in UI and SDK
    Users users;
    /* example: mobile = +966 502134567
     *  birhdate = 2021-04-27T19:07:05.297Z
     *  Not use arabia ٢٠٢١-٠٥-٠٨T١٩:٥٣:١٦.٤٣٢Z
     */
    users = new Users(name, email, password, "+966" + phone, gender, mDataPickerFragment.getDate());
    // users = new Users("name", "sdf@ff.c", "123456789", "+966" + 506412345, gender,

    Log.e("user", users.toString());
    addSign(users);

    return true;
  }

  //Complete language: Set english and Arabic
  private void addSign(Users user)
  {
    loading();
    UsersClient.getInstance().registerUser(user).enqueue(new Callback<Users>()
    {
      @Override
      public void onResponse(Call<Users> call, Response<Users> response)
      {
        mBinding.cpiSignUp.setVisibility(View.INVISIBLE);

        // if successful
        //    go to another activity
        // If not successful
        //    show error message
        if (response.isSuccessful())
        {
          mMessageResponse = response.message();
          messageSnackbar(mMessageResponse);
          success();
        }

        if (response.code() == 400)
        {

          mMessageResponse = (response.errorBody() != null) ?
              String.valueOf(response.errorBody().source()) : "error dom't know";
          Log.e("responseError", String.valueOf(response.errorBody().source()));
          Log.e("responseError", String.valueOf(response.code()));

          messageSnackbar(mMessageResponse);
        }
      }

      @Override
      public void onFailure(@NotNull Call<Users> call, @NotNull Throwable t)
      {
        mBinding.cpiSignUp.setVisibility(View.INVISIBLE);
        Log.e("failure", t.getMessage());
        messageSnackbar(t.getMessage());
        call.cancel();
      }
    });
  }

  /**
   * if user click on button terms
   * open activity Terms
   */
  @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
  void onClickTerms()
  {
    mBinding.includeSignUpDetail.includeDetailPrivacy.tvPrivacyPrivacy.setOnClickListener(
        v ->
        {
          Log.e("MainActivity", "click");
          Intent weatherDetailIntent = new Intent(SignUpActivity.this, TermsActivity.class);
          startActivity(weatherDetailIntent);
        });
  }

  /**
   * start Activity {@link SuccessActivity
   * }
   */
  private void success()
  {
    Intent weatherDetailIntent = new Intent(SignUpActivity.this, SuccessActivity.class);
    startActivity(weatherDetailIntent);
  }

  @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
  private void setupAutoCompleteVIew()
  {
    ArrayAdapter<String> adapter = new ArrayAdapter<String>
        (this, android.R.layout.simple_list_item_single_choice, getGenderArrayList());
    mBinding.includeSignUpDetail.autoDetailGender.setAdapter(adapter);
  }

  public ArrayList getGenderArrayList()
  {
    //optimize ArrayList and add language arabia
    Users.Gender[] values = Users.Gender.values();
    ArrayList genderArrayList = new ArrayList<String>();
    genderArrayList.addAll(Arrays.asList(values));
    return genderArrayList;
  }

  @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
  public void setupDatePickerFragment()
  {

    mBinding.includeSignUpDetail.etDetailBirthdate.setOnClickListener(v ->
    {
      String dateStr = mBinding.includeSignUpDetail.etDetailBirthdate.getText().toString();
      String[] dates = dateStr.split("/");

      for (int i = 0; i < dates.length; i++)
      {
        Log.w("TAG", "stringLen: " + dates[i]);
      }

      try
      {

        int year = Integer.valueOf(dates[2]);
        int month = Integer.valueOf(dates[0]) - 1;
        int day = Integer.valueOf(dates[1]);

        mDataPickerFragment = DataPickerFragment.newInstance(year,
            month, day);
      } catch (NumberFormatException | IndexOutOfBoundsException e)
      {
        messageSnackbar(e.getMessage());
      }
      if (mDataPickerFragment.getArguments() != null)
      {
        Log.w("TAG", "onClick: " + mDataPickerFragment.getArguments().toString());
        messageSnackbar(mDataPickerFragment.getArguments().toString());
      }
      mDataPickerFragment.show(getSupportFragmentManager(), "dataPicker");
    });
  }

  void loading()
  {
    mBinding.cpiSignUp.setVisibility(View.VISIBLE);
  }

  /**
   * Shows messages
   * if message error equal to number 70
   * show this message : This Email already existed
   * If message error equal to number 73
   * show message : invalid mobile number
   *
   * @param message add text to Snackbar
   */
  void messageSnackbar(String message)
  {

    //COMPLETE language set english and arabic.

    if (message.contains("size=70"))
    {
      message = getResources().getString(R.string.signUp_error_email);
    }
    else if (message.contains("size=73"))
    {
      message = getResources().getString(R.string.signUp_error_mobile);
    }
    Snackbar.make(mBinding.clSignUpRoot, message, Snackbar.LENGTH_LONG)
        .setBackgroundTint(getResources().getColor(R.color.error))
        .setAnchorView(mBinding.buttonSignUpSign).show();
  }

  /**
   * If Network is connected
   * Show button in activity and return
   * If network not connected
   * Show text not connected
   */
  @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
  public void networkAvailable()
  {
    NetworkConnection networkConnection = new NetworkConnection(this);
    networkConnection.observe(this, isConnected ->
    {
      if (Boolean.TRUE.equals(isConnected))
      {
        mBinding.buttonSignUpSign.setVisibility(View.VISIBLE);
        mBinding.tvSignUpConnect.setVisibility(View.INVISIBLE);
        return;
      }

      mBinding.buttonSignUpSign.setVisibility(View.INVISIBLE);
      mBinding.tvSignUpConnect.setVisibility(View.VISIBLE);
    });
  }



  @Override public void onError(String message)
  {

  }

  @Override public void showLoading(boolean isLoading)
  {

  }
}
