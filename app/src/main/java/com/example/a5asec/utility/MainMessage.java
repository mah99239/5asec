package com.example.a5asec.utility;

import com.google.gson.JsonSyntaxException;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import javax.net.ssl.HttpsURLConnection;
import retrofit2.HttpException;

public class MainMessage implements HandleMessage.Presenter
{
  public static final int API_STATUS_CODE_LOCAL_ERROR = 0;
  private CompositeDisposable mDisposable;
  private HandleMessage.View mView;
  private static final String TAG = "MainMessage";



  @Override public void handleApiError(Throwable error)
  {
    if (error instanceof HttpException)
    {
      switch (((HttpException) error).code())
      {
        case HttpsURLConnection.HTTP_UNAUTHORIZED:
          mView.onError("Unauthorised User ");
          break;
        case HttpsURLConnection.HTTP_FORBIDDEN:
          mView.onError("Forbidden");
          break;
        case HttpsURLConnection.HTTP_INTERNAL_ERROR:
          mView.onError("Internal Server Error");
          break;
        case HttpsURLConnection.HTTP_BAD_REQUEST:
          mView.onError("Bad Request");
          break;
        case API_STATUS_CODE_LOCAL_ERROR:
          mView.onError("No Internet Connection");
          break;
        default:
          mView.onError(error.getLocalizedMessage());
      }
    }
    else if (error instanceof JsonSyntaxException)
    {
      mView.onError("Something Went Wrong API is not responding properly!");
    }
    else
    {
      mView.onError(error.getMessage());
    }
  }

  @Override public void attachView(HandleMessage.View view)
  {
    this.mView =view;
  }
}
