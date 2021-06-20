package com.example.a5asec.ui.main.view;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.example.a5asec.R;
import com.example.a5asec.ui.main.viewmodel.TermsViewModel;
import com.example.a5asec.ui.main.adapters.TermAdapter;
import com.example.a5asec.databinding.ActivityTermsBinding;
import com.example.a5asec.utility.HandleMessage;
import com.google.android.material.snackbar.Snackbar;
import javax.inject.Inject;

public class TermsActivity extends AppCompatActivity implements HandleMessage.View
{
  private static final String TAG = "TermsActivity";
  ActivityTermsBinding mActivityTermsBinding;
  TermAdapter mAdapter;
  TermsViewModel termsViewModel;
  HandleMessage.Presenter presenter;

  @Override
  protected void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);

    mActivityTermsBinding = DataBindingUtil.setContentView(this, R.layout.activity_terms);

    termsViewModel = new ViewModelProvider(this).get(TermsViewModel.class);
    termsViewModel.attachView(this);

    termsViewModel.getTerms();

    mAdapter = new TermAdapter();
    mActivityTermsBinding.lvTerms.setLayoutManager(new LinearLayoutManager(this));
    mActivityTermsBinding.lvTerms.setAdapter(mAdapter);
    //  Log.e("ad", "terms" + termsViewModel.termsMutableLiveData.getValue());

    //  mAdapter.setList(termsViewModel.termsMutableLiveData.getValue());

    termsViewModel.termsMutableLiveData.observe(this, terms1 ->
    {
      mAdapter.setList(terms1);
      Log.e("termsl", terms1 + "");
    });
  }

  @SuppressLint("ShowToast") @Override public void onError(String message)
  {
    Snackbar.make(mActivityTermsBinding.clTermsRoot, message, Snackbar.LENGTH_LONG)
        .setDuration(4000)
        .setAnchorView(mActivityTermsBinding.glTermsBottom).show();
    Log.d(TAG, "onError: " + message);
  }

  @Override public void showLoading(boolean isLoading)
  {
    Log.d(TAG, "showLoading: " + isLoading);

    if (isLoading)
    {
      mActivityTermsBinding.cpiTerms.setVisibility(View.VISIBLE);
    }
    else
    {
      mActivityTermsBinding.cpiTerms.setVisibility(View.INVISIBLE);
    }
  }
}