package com.example.a5asec.utility;

import android.app.Activity;

public class HandleMessage
{
  public interface View
  {
    void onError(String message);
    void showLoading(boolean isLoading);

  }

  public interface Presenter
  {
    void handleApiError(Throwable error);
    void attachView(View view);

  }
}
