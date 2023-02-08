package com.example.a5asec.ui.view.login;

import static com.google.android.material.snackbar.BaseTransientBottomBar.LENGTH_LONG;

import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
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
    private int redColor;
    private int validColor;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
        {

        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_new_password, container, false);

        return mBinding.getRoot();
        }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
        {
        super.onViewCreated(view, savedInstanceState);
        validInputUser();
        setupViewModel();
        setupUi();
        networkAvailable();

        }
    private void validInputUser()
        {

        redColor = ContextCompat.getColor(requireContext(),
                R.color.md_theme_light_onError);
        validColor = ContextCompat.getColor(requireContext(),
                R.color.md_theme_light_onSurface);
        final int boxStrokeError = (int) (3 * Resources.getSystem().getDisplayMetrics().density);
        final int boxStrokeDefault = (int) (2 * Resources.getSystem().getDisplayMetrics().density);

        validatePassword(boxStrokeError, boxStrokeDefault);


        }
    private void validatePassword(int boxStrokeError, int boxStrokeDefault)
        {
        final String[] password = {null};

        mBinding.etNewPassPass.addTextChangedListener(new TextWatcher()
            {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
                {
                mBinding.tilNewPassNewPass.setBoxStrokeWidthFocused(boxStrokeDefault);
                }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
                {
                if (TextUtils.isEmpty(s))
                    {
                    mBinding.tilNewPassNewPass.setBoxStrokeColor(redColor);
                    mBinding.tilNewPassNewPass.setBoxStrokeWidthFocused(boxStrokeError);
                    } else if (s.length() < 8)
                    {
                    mBinding.tilNewPassNewPass.setError(getString(R.string.sign_up_error_password));
                    } else
                    {
                    mBinding.tilNewPassNewPass.setError(null);

                    mBinding.tilNewPassNewPass.setBoxStrokeColor(validColor);
                    }
                }

            @Override
            public void afterTextChanged(Editable s)
                {
                password[0] = s.toString().trim();
                }
            });
        mBinding.etNewPassConfirmPass.addTextChangedListener(new TextWatcher()
            {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
                {
                mBinding.tilNewPassConfirmPass.setBoxStrokeWidthFocused(boxStrokeDefault);
                }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
                {
                if (TextUtils.isEmpty(s))
                    {
                    mBinding.tilNewPassConfirmPass.setBoxStrokeColor(redColor);
                    mBinding.tilNewPassConfirmPass.setBoxStrokeWidthFocused(boxStrokeError);
                    } else if (!s.toString().trim().matches(String.valueOf(password[0])))
                    {

                    mBinding.tilNewPassConfirmPass.setError(getString(R.string.sign_up_dontmatch_password));
                    } else
                    {
                    mBinding.tilNewPassConfirmPass.setBoxStrokeColor(validColor);
                    }
                if (s.toString().trim().matches(String.valueOf(password[0])))
                    {
                    mBinding.tilNewPassConfirmPass.setError(null);
                    }
                }

            @Override
            public void afterTextChanged(Editable s)
                {
                // add document why this method is empty
                }
            });

        }
    private void setupUi()
        {
        mBinding.btnNewPass.setOnClickListener(v ->
                validateEmail()
        );

        mBinding.etNewPassConfirmPass.setOnEditorActionListener((v, actionId, event) ->
            {
            validateEmail();
            return false;
            });
        }

    private void validateEmail()
        {
        var code = Objects.requireNonNull(mBinding.etNewPassCode.getText()).toString();
        var password = Objects.requireNonNull(mBinding.etNewPassPass.getText()).toString();
        var confirmPassword = Objects.requireNonNull(mBinding.etNewPassConfirmPass.getText()).toString();

        if (isValidEmail(code, password, confirmPassword))
            {
            addUserSignUpInAPI(code, password);
            }
        }

    private boolean isValidEmail(String code, String password, String confirmPassword)
        {
        if ((TextUtils.isEmpty(code) ||
                TextUtils.isEmpty(password))
                || password.length() < 8 || !confirmPassword.equals(password))
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
        Snackbar.make(mBinding.btnNewPass, message, LENGTH_LONG)
                .setAnchorView(mBinding.btnNewPass).show();
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
                mBinding.btnNewPass.setVisibility(View.INVISIBLE);

                } else
                {
                mBinding.btnNewPass.setVisibility(View.VISIBLE);


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
