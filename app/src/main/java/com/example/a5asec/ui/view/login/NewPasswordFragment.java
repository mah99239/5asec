package com.example.a5asec.ui.view.login;

import static com.google.android.material.snackbar.BaseTransientBottomBar.LENGTH_LONG;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import com.example.a5asec.R;
import com.example.a5asec.databinding.FragmentNewPasswordBinding;
import com.example.a5asec.ui.view.viewmodel.ChangePasswordViewModel;
import com.example.a5asec.utility.NetworkConnection;
import com.google.android.material.snackbar.Snackbar;

import java.util.Objects;

/**
 * second UI for rest password.
 */
public class NewPasswordFragment extends Fragment
    {
    private static final String TAG = "NewPasswordFragment";
    private FragmentNewPasswordBinding mBinding;
    private ChangePasswordViewModel mChangePasswordViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
        {

        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_new_password, container, false);

        return mBinding.getRoot();
        }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
        {
        super.onViewCreated(view, savedInstanceState);
        setupViewModel();
        setupUi();
        networkAvailable();

        }

    private void setupUi()
        {
        mBinding.btnNewPassConfirm.setOnClickListener(v ->
                validateEmail()
        );
        }

    private void validateEmail()
        {
        var code = Objects.requireNonNull(mBinding.etNewPassCode.getText()).toString();
        var password = Objects.requireNonNull(mBinding.etNewPassPass.getText()).toString();

        if (isValidEmail(code, password))
            {
            addUserSignUpInAPI(code, password);
            }
        }

    private boolean isValidEmail(String code, String password)
        {
        if ((TextUtils.isEmpty(code) ||
                TextUtils.isEmpty(password))
                || password.length() < 8)
            {

            if (TextUtils.isEmpty(code))
                {
                mBinding.tilNewPassCode.setError(getString(R.string.newPassword_empty_code));
                } else if (TextUtils.isEmpty(password))
                {
                mBinding.tilNewPassNewPass.setError(getString(R.string.sign_up_empty_password));
                } else if (password.length() < 8)
                {
                mBinding.tilNewPassNewPass.setError(getString(R.string.sign_up_error_password));

                }

            return false;
            }

        return true;
        }


    private void addUserSignUpInAPI(String code, String password)
        {


        mChangePasswordViewModel.setFinishPassword(code, password);
        mChangePasswordViewModel.getStatusCode().observe(getViewLifecycleOwner(), authorizationResource ->
            {

            Log.e(TAG, String.valueOf(authorizationResource.mStatus));

            switch (authorizationResource.mStatus)
                {

                case SUCCESS -> {
                Log.e(TAG, "SUCCESS");
                Log.e(TAG, "authorizationResource");


                new Handler(Looper.getMainLooper()).postDelayed
                        (this::successFinishResetPassword, 1000);

                }


                case ERROR -> {
                messageSnackbar();
                Log.e(TAG, "ERROR:" + authorizationResource.getMMessage());
                //Handle Error

                }


                }
            });

        }

    private void messageSnackbar()
        {

        var message = getString(R.string.newPassword_error_code);
        Snackbar.make(mBinding.btnNewPassConfirm, message, LENGTH_LONG)
                .setAnchorView(mBinding.btnNewPassConfirm).show();
        }

    private void successFinishResetPassword()
        {
        NavDirections action =
                NewPasswordFragmentDirections.actionNewPasswordFragmentToNavLogin();

        Navigation.findNavController(requireView()).navigate(action);
        }

    private void setupViewModel()
        {
        mChangePasswordViewModel = new ViewModelProvider(this).get(ChangePasswordViewModel.class);
        }

    /**
     * Check network is available
     */
    public void networkAvailable()
        {
        NetworkConnection networkConnection = new NetworkConnection(requireContext());
        networkConnection.observe(getViewLifecycleOwner(), isConnected ->
            {
            if (!isConnected)
                {
                mBinding.btnNewPassConfirm.setVisibility(View.INVISIBLE);

                } else
                {
                mBinding.btnNewPassConfirm.setVisibility(View.VISIBLE);


                }
            });
        }


    @Override
    public void onDestroy()
        {
        super.onDestroy();
        mBinding = null;
        }
    }
