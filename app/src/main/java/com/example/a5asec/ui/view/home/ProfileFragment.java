package com.example.a5asec.ui.view.home;

import static com.google.android.material.snackbar.BaseTransientBottomBar.LENGTH_LONG;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.a5asec.R;
import com.example.a5asec.data.model.api.Users;
import com.example.a5asec.data.local.prefs.TokenPreferences;
import com.example.a5asec.databinding.FragmentProfileBinding;
import com.example.a5asec.ui.view.viewmodel.UserViewModel;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;

import java.util.Objects;


public class ProfileFragment extends Fragment
    {
    private static final String TAG = "ProfileFragment";
    FragmentProfileBinding mBinding;
    private UserViewModel mUserViewModel;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
        {

        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container, false);

        return mBinding.getRoot();
        }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
        {
        super.onViewCreated(view, savedInstanceState);
        setupUi();

        }

    private void setupUi()
        {
        initView();
        setupUserViewModel();
        setupUserObserver();
        }

    private void initView()
        {
        initClickedButton();
        }

    private void initClickedButton()
        {
        mBinding.btnProfileAddress.setOnClickListener(v ->
            {
            Log.e(TAG, "addressButton");
            var action
                    = ProfileFragmentDirections.actionNavHomeProfileToManageAddressFragment2();
            Navigation.findNavController(requireView()).navigate(action);
            });
        mBinding.btnProfileMobile.setOnClickListener(v ->
            {

            Log.e(TAG, "mobileButton");

            });
        mBinding.btnProfilePassword.setOnClickListener(v ->
            {
            Log.e(TAG, "passwordButton");
            dialogChangePassword();
            });
        }

    private void setupUserObserver()
        {
        mUserViewModel.getUser().observe(getViewLifecycleOwner(), userResource ->
            {
            switch (userResource.mStatus)
                {

                case SUCCESS -> {

                Log.e(TAG + ":user", "SUCCESS");
                Log.e(TAG + ":user", "suc:" + userResource.getMData());
                setDataUser(userResource.getMData());

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

    private void setDataUser(@NonNull Users user)
        {
        String name = user.getFullName();
        String email = user.getEmail();
        String phone = user.getMobile();
        String address = user.getAreaEn();
        boolean verify = user.isMobileVerified();

        checkedAddress(address);
        checkedVerifyPassword(verify);
        mBinding.tvProfileName.setText(name);
        mBinding.tvProfileEmail.setText(email);
        mBinding.tvProfilePhone.setText(phone);
        // addressTextView.setText();
        }

    private void checkedAddress(String address)
        {
        if (address == null)
            {
            Log.e(TAG, "null");
            mBinding.tvProfileAddress.setVisibility(View.VISIBLE);
            } else
            {
            Log.e(TAG, address);

            mBinding.tvProfileAddress.setVisibility(View.GONE);

            }
        }

    private void checkedVerifyPassword(boolean verify)
        {
        Log.e(TAG, "checkedVerifyPassword:" + verify);

        if (verify) mBinding.btnProfileMobile.setVisibility(View.GONE);
        else mBinding.btnProfileMobile.setVisibility(View.VISIBLE);
        }

    private void setupUserViewModel()
        {
        mUserViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);
        }


    private void dialogChangePassword()
        {
        String positiveText = getResources().getString(R.string.change_password_positive);
        String negativeText = getResources().getString(R.string.change_password_negative);
        String title = getResources().getString(R.string.profile_button_change_password);

        new MaterialAlertDialogBuilder(requireContext())
                .setTitle(title)
                .setView(R.layout.dialog_change_password)
                .setPositiveButton(
                        positiveText,
                        (dialog, which) ->
                            {

                            EditText olderPasswordEditText =
                                    ((AlertDialog) dialog).findViewById(R.id.et_change_password_older);
                            EditText newPasswordEditText =
                                    ((AlertDialog) dialog).findViewById(R.id.et_change_password_new);

                            var oldPassword =
                                    Objects.requireNonNull(olderPasswordEditText.getText()).toString();
                            var newPassword =
                                    Objects.requireNonNull(newPasswordEditText.getText()).toString();

                            if (checkValidPassword(oldPassword, newPassword))
                                {
                                setupChangePasswordObserver(newPassword);
                                }
                            })
                .setNegativeButton(negativeText, null)
                .show();

        }

    private void setupChangePasswordObserver(String password)
        {
        mUserViewModel.getStatusCode(password).observe(getViewLifecycleOwner(), userResource ->
            {
            switch (userResource.mStatus)
                {

                case SUCCESS -> {

                Log.e(TAG + ":setupChangePasswordObserver", "SUCCESS");
                Log.e(TAG + ":setupChangePasswordObserver", "suc:" + userResource.getMData());
                String message = getString(R.string.change_password_success);
                messageSnackbar(message);
                }

                case LOADING -> {
                Log.e(TAG + ":user", "LOADING");


                }
                case ERROR -> {

                Log.e(TAG + ":user", "ERROR");
                messageSnackbar(userResource.getMMessage());

                }
                }
            });

        }

    private boolean checkValidPassword(String old, String newPassword)
        {

        var oldPass = TokenPreferences.getPassword();
        if (TextUtils.isEmpty(old) || TextUtils.isEmpty(newPassword) || newPassword.length() < 8
                || !oldPass.equals(old))
            {
            String message = null;

            if (TextUtils.isEmpty(old))
                {
                message = getString(R.string.change_password_empty_old);
                } else if (TextUtils.isEmpty(newPassword))
                {
                message = getString(R.string.change_password_empty_new);

                } else if (newPassword.length() < 8)
                {
                message = getString(R.string.change_password_error_length);

                } else if (!oldPass.equals(old))
                {
                message = getString(R.string.change_password_error_old);

                }
            messageSnackbar(message);
            return false;
            }
        return true;
        }

    private void messageSnackbar(@NonNull String message)
        {

        Snackbar.make(mBinding.btnProfilePassword, message, LENGTH_LONG)
                .setAnchorView(mBinding.btnProfilePassword).show();
        }


    @Override
    public void onDestroy()
        {
        super.onDestroy();
        mBinding = null;
        }
    }