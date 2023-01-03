package com.example.a5asec.ui.view.login;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
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
import com.example.a5asec.databinding.FragmentForgetPasswordBinding;
import com.example.a5asec.ui.view.viewmodel.ChangePasswordViewModel;
import com.example.a5asec.utility.NetworkConnection;

import java.util.Objects;

/**
 * first UI for rest password.
 */
public class ForgetPasswordFragment extends Fragment
    {
    private static final String TAG = "ForgetPasswordFragment";

    private FragmentForgetPasswordBinding mBinding;
    private ChangePasswordViewModel mChangePasswordViewModel;



    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
        {
        // Inflate the layout for this fragment
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_forget_password, container, false);

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
        mBinding.btnForgetPasswordConfirm.setOnClickListener(v -> validateEmail() );

        }

    private void validateEmail()
        {
        var email = Objects.requireNonNull(mBinding.tietForgetPasswordEmail.getText()).toString();

        if(isValidEmail(email))
            {
            addUserSignUpInAPI(email);
            }
        }

    private boolean isValidEmail(String email)
        {
        if((TextUtils.isEmpty(email) ||
                !email.matches(String.valueOf(Patterns.EMAIL_ADDRESS))))
            {

            if (TextUtils.isEmpty(email))
                {
                mBinding.tilForgetPasswordEmail.setError(getString(R.string.sign_up_empty_email));
                }
            else if(!email.matches(String.valueOf(Patterns.EMAIL_ADDRESS)))
                {
                mBinding.tilForgetPasswordEmail.setError(getString(R.string.sign_up_invalid_email));
                }
            return false;
            }
        return true;
        }


    private void addUserSignUpInAPI(String email)
        {


        mChangePasswordViewModel.setRestPassword(email);
        mChangePasswordViewModel.getStatusCode().observe(getViewLifecycleOwner(), authorizationResource ->
            {

            Log.e(TAG, String.valueOf(authorizationResource.mStatus));

            switch (authorizationResource.mStatus)
                {

                case SUCCESS -> {
                Log.e(TAG, "SUCCESS");
                Log.e(TAG, "authorizationResource");


                new Handler(Looper.getMainLooper()).postDelayed
                        (this::openNewPasswordFragment, 1000);

                }


                case ERROR -> {

                Log.e(TAG, "ERROR:" + authorizationResource.getMMessage());
                //Handle Error

                }


                }
            });

        }

    private void openNewPasswordFragment()
        {
        NavDirections action =
                ForgetPasswordFragmentDirections.actionForgetPasswordFragmentToNewPasswordFragment();

        Navigation.findNavController(mBinding.getRoot()).navigate(action);
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
                mBinding.btnForgetPasswordConfirm.setVisibility(View.INVISIBLE);

                } else
                {
                mBinding.btnForgetPasswordConfirm.setVisibility(View.VISIBLE);


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