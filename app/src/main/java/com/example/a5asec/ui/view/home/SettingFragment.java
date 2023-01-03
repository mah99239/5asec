package com.example.a5asec.ui.view.home;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.os.LocaleListCompat;
import androidx.databinding.ObservableArrayMap;
import androidx.databinding.ObservableField;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.a5asec.R;
import com.example.a5asec.databinding.FragmentSettingBinding;
import com.example.a5asec.ui.view.viewmodel.UserViewModel;


public class SettingFragment extends Fragment
    {
    private static final String TAG = "SettingFragment";
    private FragmentSettingBinding mBinding;
    private UserViewModel mUserViewModel;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
        {
        mBinding = FragmentSettingBinding.inflate(inflater, container, false);
        return mBinding.getRoot();
        }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState)
        {
        super.onViewCreated(view, savedInstanceState);
        setupUi();
        setupUserViewModel();
        setupUserObserver();

        }

    private void setupUi()
        {

        }

    /**
     * called when choose language in UI.
     *   If user's clicked current language
     *     show hint and helper text in TextInputLayout Setting Language.
     *   else
     *     get key from clicked language and pass to Application Locales.
     * @param language
     */
    private void setupLanguage(String language)
        {
        String[] stringArray = getResources().getStringArray(R.array.setting_language);

        ObservableArrayMap<String, String> languageMap = new ObservableArrayMap<>();
        ObservableField<String> currentLanguage = new ObservableField<>();


        languageMap.put("EN", stringArray[0]);
        languageMap.put("AR", stringArray[1]);
        currentLanguage.set(languageMap.get(language));

        mBinding.setCurrentLanguage(currentLanguage.get());


        mBinding.actvSettingLanguage.setOnItemClickListener((parent, view, position, id) ->
            {
            String selectedLanguage = mBinding.actvSettingLanguage.getText().toString();


            if (selectedLanguage.equals(currentLanguage.get()))
                {
                String helperText = getResources().getString(R.string.setting_text_language_helper)
                        + " (" + currentLanguage.get() + ")";
                mBinding.tilSettingLanguage.setHelperText(helperText);
                } else
                {

                String keyLanguage =
                        languageMap.keySet()
                                .stream()
                                .filter(key -> selectedLanguage.equals(languageMap.get(key)))
                                .findFirst().get();

                mUserViewModel.changeLanguage(keyLanguage);
                LocaleListCompat appLocale = LocaleListCompat.forLanguageTags(keyLanguage);
                // Call this on the main thread as it may require Activity.restart()
                AppCompatDelegate.setApplicationLocales(appLocale);

                restartFragment();
                }
            });


        }


    private void restartFragment()
        {

        var fragmentId = Navigation.findNavController(getView()).getCurrentDestination().getId();
        Navigation.findNavController(getView()).popBackStack(fragmentId, true);
        Navigation.findNavController(getView()).navigate(fragmentId);


        }

    private void setupUserObserver()
        {
        mUserViewModel.getUser().observe(getViewLifecycleOwner(), userResource ->
            {
            switch (userResource.mStatus)
                {

                case SUCCESS -> {
                setupLanguage(userResource.getMData().getLangKey());
                setupSwitchNotification(userResource.getMData().isAcceptNotifications());
                Log.e(TAG + ":user", "SUCCESS");
                Log.e(TAG + ":user", "suc:" + userResource.getMData());

                }

                case LOADING -> {
                Log.e(TAG + ":user", "LOADING");


                }
                case ERROR -> {

                Log.e(TAG + ":user", "ERROR");

                }
                }
            });

        }

    private void setupSwitchNotification(boolean notification)
        {
        mBinding.swSettingNotification.setChecked(notification);

        mBinding.swSettingNotification.setOnClickListener(v ->
            {
         boolean isChecked = mBinding.swSettingNotification.isChecked();
            mUserViewModel.changeNotification(isChecked);

            });

        }

    private void setupUserViewModel()
        {
        mUserViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);
        }

    @Override
    public void onDestroy()
        {
        super.onDestroy();
        mBinding = null;
        }
    }