package com.example.a5asec.ui.view.login;


import static com.google.android.material.snackbar.BaseTransientBottomBar.LENGTH_LONG;

import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import com.example.a5asec.R;
import com.example.a5asec.data.model.api.RegistrationDTO;
import com.example.a5asec.databinding.FragmentSignUpBinding;
import com.example.a5asec.ui.view.viewmodel.SignUpViewModel;
import com.example.a5asec.utility.NetworkConnection;
import com.google.android.material.snackbar.Snackbar;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;
import java.util.Objects;

public class SignUpFragment extends Fragment
    {

    private static final String TAG = "SignUpFragment";
    private FragmentSignUpBinding mBinding;
    private DataPickerFragment mDataPickerFragment;
    private SignUpViewModel mSignUpViewModel;

    private int redColor;
    private int validColor;

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
        {
        // Inflate the layout for this fragment
        mBinding =
                DataBindingUtil.inflate(inflater, R.layout.fragment_sign_up, container, false);

        return mBinding.getRoot();
        }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
        {
        super.onViewCreated(view, savedInstanceState);
        setupUI();
        networkAvailable();
        setupViewModel();

        }

    private void setupUI()
        {
        setupAutoCompleteVIew();
        setupDatePickerFragment();
        validInputUser();
        onClickedTerms();
        onClickedSignUp();
        }


    private void setupAutoCompleteVIew()
        {
        var adapter = new ArrayAdapter<>
                (getContext(), android.R.layout.simple_list_item_single_choice,
                        getGenderArrayList());
        mBinding.includeSignUpContent.actvSignUpGender.setAdapter(adapter);
        }

    /**
     * @return show values gender in UI
     */
    @NonNull
    private ArrayList<String> getGenderArrayList()
        {
        var values = getResources().getStringArray(R.array.sign_up_gender);

        return new ArrayList<>(Arrays.asList(values));
        }

    /**
     * Show datePicker in fragment
     */
    public void setupDatePickerFragment()
        {

        mBinding.includeSignUpContent.etSignUpBirthdate.setOnClickListener(v ->
            {
            mDataPickerFragment =
                    new DataPickerFragment();
            String dateSelected =
                    Objects.requireNonNull(mBinding.includeSignUpContent.etSignUpBirthdate.getText()).toString();


            if (!dateSelected.isEmpty())
                {

                String[] dates = dateSelected.split("/");


                try
                    {

                    int year = Integer.parseInt(dates[2]);
                    int month = Integer.parseInt(dates[0]) - 1;
                    int day = Integer.parseInt(dates[1]);

                    mDataPickerFragment = DataPickerFragment.newInstance(year,
                            month, day);

                    } catch (NumberFormatException | IndexOutOfBoundsException e)
                    {

                    Log.e(TAG, Objects.requireNonNull(e.getMessage()));
                    }
                }


            mDataPickerFragment.show(requireActivity().getSupportFragmentManager(), "dataPicker");


            });

        requireActivity().getSupportFragmentManager().setFragmentResultListener("REQUEST_KEY",
                this, (requestKey, result) ->
                    {
                    if (requestKey.equals("REQUEST_KEY"))
                        {
                        var date = result.getString("SELECTED_DATE");
                        mBinding.includeSignUpContent.etSignUpBirthdate.setText(date);
                        }
                    });

        }

    private void validInputUser()
        {

        redColor = ContextCompat.getColor(requireContext(),
                R.color.md_theme_light_onError);
        validColor = ContextCompat.getColor(requireContext(),
                R.color.md_theme_light_onSurface);
        final int boxStrokeError = (int) (3 * Resources.getSystem().getDisplayMetrics().density);
        final int boxStrokeDefault = (int) (2 * Resources.getSystem().getDisplayMetrics().density);


        validateName(boxStrokeError, boxStrokeDefault);
        validatePhone(boxStrokeError, boxStrokeDefault);
        validatePassword(boxStrokeError, boxStrokeDefault);


        }

    private void validateName(int boxStrokeError, int boxStrokeDefault)
        {
        mBinding.includeSignUpContent.etSignUpName.addTextChangedListener(new TextWatcher()
            {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
                {
                mBinding.includeSignUpContent.tilSignUpName.setBoxStrokeWidthFocused(boxStrokeDefault);
                }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
                {


                if (TextUtils.isEmpty(s))
                    {
                    mBinding.includeSignUpContent.tilSignUpName.setBoxStrokeColor(redColor);
                    mBinding.includeSignUpContent.tilSignUpName.setBoxStrokeWidthFocused(boxStrokeError);
                    } else
                    {
                    mBinding.includeSignUpContent.tilSignUpName.setBoxStrokeColor(validColor);
                    }
                }

            @Override
            public void afterTextChanged(Editable s)
                {
                mBinding.includeSignUpContent.tilSignUpName.setBoxStrokeWidthFocused(boxStrokeDefault);
                }
            });
        }

    private void validatePhone(int boxStrokeError, int boxStrokeDefault)
        {
        mBinding.includeSignUpContent.etSignUpPhone.addTextChangedListener(new TextWatcher()
            {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
                {
                mBinding.includeSignUpContent.tilSignUpPhone.setBoxStrokeWidthFocused(boxStrokeDefault);
                }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
                {

                if (TextUtils.isEmpty(s))
                    {
                    mBinding.includeSignUpContent.tilSignUpPhone.setBoxStrokeColor(redColor);
                    mBinding.includeSignUpContent.tilSignUpPhone.setBoxStrokeWidthFocused(boxStrokeError);
                    } else if (!s.toString().trim().matches(String.valueOf(Patterns.PHONE)))
                    {
                    mBinding.includeSignUpContent.tilSignUpPhone.setError(getString(R.string.sign_up_invalid_mobile));
                    mBinding.includeSignUpContent.tilSignUpPhone.setBoxStrokeColor(redColor);
                    } else
                    {
                    mBinding.includeSignUpContent.tilSignUpPhone.setBoxStrokeColor(validColor);
                    mBinding.includeSignUpContent.tilSignUpPhone.setError(null);
                    }
                }

            @Override
            public void afterTextChanged(Editable s)
                {
                mBinding.includeSignUpContent.tilSignUpPhone.setError(null);
                }
            });
        }

    private void validatePassword(int boxStrokeError, int boxStrokeDefault)
        {
        final String[] password = {null};

        mBinding.includeSignUpContent.etSignUpPass.addTextChangedListener(new TextWatcher()
            {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
                {
                mBinding.includeSignUpContent.tilSignUpPass.setBoxStrokeWidthFocused(boxStrokeDefault);
                }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
                {
                if (TextUtils.isEmpty(s))
                    {
                    mBinding.includeSignUpContent.tilSignUpPass.setBoxStrokeColor(redColor);
                    mBinding.includeSignUpContent.tilSignUpPass.setBoxStrokeWidthFocused(boxStrokeError);
                    } else if (s.length() < 8)
                    {
                    mBinding.includeSignUpContent.tilSignUpPass.setError(getString(R.string.sign_up_error_password));
                    } else
                    {
                    mBinding.includeSignUpContent.tilSignUpPass.setError(null);

                    mBinding.includeSignUpContent.tilSignUpPass.setBoxStrokeColor(validColor);
                    }
                }

            @Override
            public void afterTextChanged(Editable s)
                {
                password[0] = s.toString().trim();
                }
            });
        mBinding.includeSignUpContent.etSignUpConfirmPass.addTextChangedListener(new TextWatcher()
            {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
                {
                mBinding.includeSignUpContent.tilSignUpConfirmPass.setBoxStrokeWidthFocused(boxStrokeDefault);
                }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
                {
                if (TextUtils.isEmpty(s))
                    {
                    mBinding.includeSignUpContent.tilSignUpConfirmPass.setBoxStrokeColor(redColor);
                    mBinding.includeSignUpContent.tilSignUpConfirmPass.setBoxStrokeWidthFocused(boxStrokeError);
                    } else if (!s.toString().trim().matches(String.valueOf(password[0])))
                    {

                    mBinding.includeSignUpContent.tilSignUpConfirmPass.setError(getString(R.string.sign_up_dontmatch_password));
                    } else
                    {
                    mBinding.includeSignUpContent.tilSignUpConfirmPass.setBoxStrokeColor(validColor);
                    }
                if (s.toString().trim().matches(String.valueOf(password[0])))
                    {
                    mBinding.includeSignUpContent.tilSignUpConfirmPass.setError(null);
                    }
                }

            @Override
            public void afterTextChanged(Editable s)
                {
                // add document why this method is empty
                }
            });

        }


    /**
     * called when clicked terms and cond.
     * open  Fragment Terms
     */
    private void onClickedTerms()
        {
        mBinding.includeSignUpContent.tvSignUpPrivacy.setOnClickListener(
                v ->
                    {
                    NavDirections action =
                            SignUpFragmentDirections.actionSignUpFragmentToTermsFragment();

                    Navigation.findNavController(requireView()).navigate(action);
                    });
        }

    private void onClickedSignUp()
        {
        mBinding.btnSignUp.setOnClickListener(v -> validateData());
        }

    private void validateData()
        {
        // optimize: add sign chcek in UI and SDK
      /*   example:
        mobile = +966 502134567
                * birhdate = 2021 - 04 - 27 T19:
        07:05.297 Z
                * Not use arabia ٢٠٢١-٠٥-٠٨T١٩:٥٣:١٦.٤٣٢Z
 */
        var name = Objects.requireNonNull(mBinding.includeSignUpContent.etSignUpName.getText()).toString();
        var email = Objects.requireNonNull(mBinding.includeSignUpContent.etSignUpEmail.getText()).toString();
        var phone = Objects.requireNonNull(mBinding.includeSignUpContent.etSignUpPhone.getText()).toString();
        var password = Objects.requireNonNull(mBinding.includeSignUpContent.etSignUpPass.getText()).toString();
        var gender =
                mBinding.includeSignUpContent.actvSignUpGender.getText().toString();
        var currentLanguage = Locale.getDefault().getLanguage().toUpperCase();
        var lang = currentLanguage.equals("AR") ? currentLanguage : "EN";


        if (isValid())
            {
            var user =
                    new RegistrationDTO(name, email, password, "+966" + phone, gender,
                            DataPickerFragment.getSelectionDate(), lang);
            Log.e(TAG, user.toString());

            addUserSignUpInAPI(user);

            }

        }


    private boolean isValid()
        {
        var name = Objects.requireNonNull(mBinding.includeSignUpContent.etSignUpName.getText()).toString();
        var email = Objects.requireNonNull(mBinding.includeSignUpContent.etSignUpEmail.getText()).toString();
        var phone = Objects.requireNonNull(mBinding.includeSignUpContent.etSignUpPhone.getText()).toString();
        var password = Objects.requireNonNull(mBinding.includeSignUpContent.etSignUpPass.getText()).toString();
        var confirmPassword = Objects.requireNonNull(mBinding.includeSignUpContent.etSignUpConfirmPass.getText()).toString();

        var isCheck =
                mBinding.includeSignUpContent.cbSignUp.isChecked();

        mBinding.includeSignUpContent.tilSignUpName.setError(null);
        mBinding.includeSignUpContent.tilSignUpEmail.setError(null);
        mBinding.includeSignUpContent.tilSignUpPhone.setError(null);
        mBinding.includeSignUpContent.tilSignUpPass.setError(null);
        mBinding.includeSignUpContent.tilSignUpConfirmPass.setError(null);

        if (TextUtils.isEmpty(name)
                || (TextUtils.isEmpty(email) ||
                !email.matches(String.valueOf(Patterns.EMAIL_ADDRESS)))
                || TextUtils.isEmpty(phone)
                || TextUtils.isEmpty(password)
                || password.length() < 8
                || (TextUtils.isEmpty(confirmPassword) || !confirmPassword.matches(password))
                || !isCheck)
            {
            if (TextUtils.isEmpty(name))
                {
                mBinding.includeSignUpContent.tilSignUpName.setError(getString(R.string.sign_up_empty_name));
                }
            if (TextUtils.isEmpty(email))
                {
                mBinding.includeSignUpContent.tilSignUpEmail.setError(getString(R.string.sign_up_empty_email));
                }
            if (TextUtils.isEmpty(phone))
                {
                mBinding.includeSignUpContent.tilSignUpPhone.setError(getString(R.string.sign_up_empty_phone));
                }
            if (password.length() < 8)
                {
                mBinding.includeSignUpContent.tilSignUpPass.setError(getString(R.string.sign_up_error_password));
                }
            if (TextUtils.isEmpty(password))
                {
                mBinding.includeSignUpContent.tilSignUpPass.setError(getString(R.string.sign_up_empty_password));
                }
            if (TextUtils.isEmpty(confirmPassword) || !confirmPassword.matches(password))
                {
                mBinding.includeSignUpContent.tilSignUpConfirmPass.setError(getString(R.string.sign_up_empty_confirmpassword));
                }
            if (!isCheck)
                {
                mBinding.includeSignUpContent.cbSignUp.setButtonTintList(
                        ColorStateList.valueOf(Color.RED));
                }
            return false;
            }

        return true;
        }


    //Complete language: Set english and Arabic
    private void addUserSignUpInAPI(RegistrationDTO user)
        {
        showLoading(); // ProgressIndicator is visible

        mSignUpViewModel.setRegisterUser(user);
        mSignUpViewModel.getStatusCode().observe(getViewLifecycleOwner(), authorizationResource ->
            {

            Log.e(TAG, String.valueOf(authorizationResource.mStatus));

            switch (authorizationResource.mStatus)
                {

                case SUCCESS ->
                    {
                    Log.e(TAG, "SUCCESS");
                    Log.e(TAG, "authorizationResource");


                    new Handler(Looper.getMainLooper()).postDelayed
                            (this::successSignUp, 1000);

                    }


                case ERROR ->
                    {

                    Log.e(TAG, "ERROR:" + authorizationResource.getMMessage());
                    //Handle Error

                    messageSnackbar(authorizationResource.getMMessage()); // SHow message in UI
                    completeLoading();


                    }
                case LOADING ->
                    {

                    Log.e(TAG, "loading:" + authorizationResource.getMMessage());

                    showLoading();
                    }

                }
            });

        }


    private void successSignUp()
        {
        NavDirections action =
                SignUpFragmentDirections.actionNavLoginSignUpFragmentToNavLoginSuccessFragment();

        Navigation.findNavController(requireView()).navigate(action);
        }

    private void messageSnackbar(@NonNull String message)
        {

        //COMPLETE language set english and arabic.

        if (message.contains("Invalid Email format"))
            {
            message = getString(R.string.sign_up_invalid_email);
            } else if (message.contains("Email already used"))
            {
            message = getString(R.string.sign_up_used_email);
            } else if (message.contains("Invalid mobile number"))
            {
            message = getString(R.string.sign_up_invalid_mobile);
            }
        Snackbar.make(mBinding.clSignUpRoot, message, LENGTH_LONG)
                .setAnchorView(mBinding.btnSignUp).show();
        }

    /**
     * ProgressIndicator is show loading
     */
    private void showLoading()
        {
        mBinding.cpiSignUp.setVisibility(View.VISIBLE);
        mBinding.btnSignUp.setEnabled(false);
        }

    /**
     * ProgressIndicator is completed loading
     */
    private void completeLoading()
        {
        mBinding.cpiSignUp.setVisibility(View.INVISIBLE);
        mBinding.btnSignUp.setEnabled(true);
        }


    /**
     * Check network is available
     */
    public void networkAvailable()
        {
        NetworkConnection networkConnection = new NetworkConnection(requireContext());
        networkConnection.observe(getViewLifecycleOwner(), isConnected ->
            {
            if (Boolean.FALSE.equals(isConnected))
                {
                mBinding.btnSignUp.setVisibility(View.INVISIBLE);

                } else
                {
                mBinding.btnSignUp.setVisibility(View.VISIBLE);


                }
            });
        }

    private void setupViewModel()
        {

        mSignUpViewModel = new ViewModelProvider(this).get(SignUpViewModel.class);
        }

    @Override
    public void onDestroy()
        {
        super.onDestroy();
        mBinding = null;
        mDataPickerFragment = null;
        }
    }

