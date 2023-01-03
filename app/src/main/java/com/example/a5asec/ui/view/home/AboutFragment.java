package com.example.a5asec.ui.view.home;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.a5asec.R;
import com.example.a5asec.data.model.api.Setting;
import com.example.a5asec.databinding.FragmentAboutBinding;
import com.example.a5asec.ui.view.viewmodel.SettingViewModel;
/**
 *  Fragment for the about us  screen, show wep page from url
 */
public class AboutFragment extends Fragment
    {

    private static final String TAG = "AboutFragment";
    private static final String LANGUAGE_TAG_EN = "en";
    FragmentAboutBinding mBinding;
    WebView webView;
    private SettingViewModel mSettingViewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
        {

        // Inflate the layout for this fragment
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_about, container, false);

        return mBinding.getRoot();
        }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
        {
        super.onViewCreated(view, savedInstanceState);
        webView = mBinding.wvAbout;
        setupViewModel();
        setupObserver();
        }

    @SuppressLint("SetJavaScriptEnabled")
    private void setupWebView(String url)
        {

        webView.getSettings().setLoadsImagesAutomatically(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.callOnClick();
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        Log.e(TAG, "URL: " + url);
        webView.loadUrl(url);


        webView.setWebViewClient(new WebViewClient()
            {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request)
                {
                webView.loadUrl(request.getUrl().toString());
                return false; //Returns:true to cancel the current load, otherwise return false.
                }

            });
        }

    private void setupObserver()
        {
        mSettingViewModel.getSetting().observe(getViewLifecycleOwner(), settingResource ->
            {
            switch (settingResource.mStatus)
                {

                case SUCCESS -> {
                Log.e(TAG, "SUCCESS");
                Log.e(TAG, "suc:" + settingResource.getMData());
                String url = getUrl(settingResource.getMData());
                setupWebView(url);

                }

                case LOADING -> {
                Log.e(TAG, "LOADING");


                }
                case ERROR -> {

                Log.e(TAG, "ERROR");

                }
                }
            });
        }

    private String getUrl(Setting setting)
        {
        String language = AppCompatDelegate.getApplicationLocales().toLanguageTags();

        if (language.equals(LANGUAGE_TAG_EN))
            {
            return setting.getAboutWebsiteURLEn();
            } else
            {
            return setting.getAboutWebsiteURLAr();
            }
        }

    private void setupViewModel()
        {
        mSettingViewModel = new ViewModelProvider(requireActivity())
                .get(SettingViewModel.class);
        }
    }