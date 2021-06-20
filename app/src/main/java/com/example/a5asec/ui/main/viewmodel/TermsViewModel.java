package com.example.a5asec.ui.main.viewmodel;

import android.util.Log;
import android.view.View;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.example.a5asec.data.api.TermsClient;
import com.example.a5asec.data.model.Terms;
import com.example.a5asec.utility.HandleMessage;
import com.example.a5asec.utility.MainMessage;
import com.google.gson.JsonSyntaxException;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;

import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.core.SingleObserver;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import java.util.List;
import java.util.concurrent.TimeUnit;
import javax.net.ssl.HttpsURLConnection;
import retrofit2.HttpException;

/**
 * Author: Mahmoud98
 */

public class TermsViewModel extends ViewModel implements HandleMessage.Presenter
{
  public static final int API_STATUS_CODE_LOCAL_ERROR = 0;

  public MutableLiveData<List<Terms>> termsMutableLiveData = new MutableLiveData<>();
  public boolean isLoading;
  CompositeDisposable compositeDisposable = new CompositeDisposable();
  private static final String TAG = "TermsViewModel";
  private HandleMessage.View mView;

  public void getTerms()
  {


    Single observable = TermsClient.getINSTANCE().getClient()
        // Run on a background thread
        .subscribeOn(Schedulers.io())
        //wait for two second in the emission of success
        .delay(1000 , TimeUnit.MILLISECONDS)
        //Be notified on the main thread
        .observeOn(AndroidSchedulers.mainThread());
    SingleObserver<List<Terms>> observer = new SingleObserver<List<Terms>>()
    {
      @Override public void onSubscribe(@NonNull Disposable d)
      {
        compositeDisposable.add(d);
        isLoading = true;
        mView.showLoading(isLoading);
      }

      @Override public void onSuccess(@NonNull List<Terms> terms)
      {
        termsMutableLiveData.setValue(terms);
        isLoading = false;
        mView.showLoading(isLoading);

      }

      @Override public void onError(@NonNull Throwable e)
      {
        handleApiError(e);
        isLoading = false;
        mView.showLoading(isLoading);

        Log.d(TAG, "onError: "+ e);
      }
    };

    observable.subscribe(observer);

  }

  @Override protected void onCleared()
  {
    super.onCleared();
    compositeDisposable.clear();
  }

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
    Log.d(TAG, "handleApiError: " );
  }

  @Override public void attachView(HandleMessage.View view)
  {
    this.mView = view;
  }


}
