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
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.ProcessLifecycleOwner;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import com.example.a5asec.R;
import com.example.a5asec.data.model.api.RegistrationDTO;
import com.example.a5asec.databinding.FragmentSignUpBinding;
import com.example.a5asec.ui.view.viewmodel.SignUpViewModel;
import com.example.a5asec.utility.NetworkConnection;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class SignUpFragment extends Fragment implements LifecycleObserver
    {

    private static final String TAG = "SignUpFragment";
   private FragmentSignUpBinding mBinding;
    DataPickerFragment mDataPickerFragment;
    private SignUpViewModel mSignUpViewModel;
    private TextInputLayout mTextInputLayoutName;
    private TextInputLayout mTextInputLayoutEmail;
    private TextInputLayout mTextInputLayoutPhone;
    private TextInputLayout mTextInputLayoutPassword;
    private TextInputLayout mTextInputLayoutConfirmPassword;


    private TextInputEditText mTextInputEditTextName;
    private TextInputEditText mTextInputEditTextEmail;
    private TextInputEditText mTextInputEditTextPhone;
    private TextInputEditText mTextInputEditTextPassword;
    private TextInputEditText mTextInputEditTextConfirmPassword;
    private AutoCompleteTextView mAutoCompleteTextViewGender;
    private TextInputEditText mTextInputEditTextBirthDate;
    private TextView mTextViewPrivacy;
    private CheckBox mCheckBoxPrivacy;
    private Button mButtonSignUp;

    private int redColor ;
    private  int validColor ;

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
        {
        // Inflate the layout for this fragment
        mBinding =
                DataBindingUtil.inflate(inflater, R.layout.fragment_sign_up, container, false);
        View view = mBinding.getRoot();

        ProcessLifecycleOwner.get().getLifecycle().addObserver(this);

        return view;
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
        initView();
        setupDatePickerFragment();
        validInputUser();
        onClickedSignUp();
        onClickedTerms();
        }


    private void initView()
        {
        mTextInputLayoutName = mBinding.includeSignUpContant.tilSignUpName;
        mTextInputLayoutEmail = mBinding.includeSignUpContant.tilSignUpEmail;
        mTextInputLayoutPhone = mBinding.includeSignUpContant.tilSignUpPhone;
        mTextInputLayoutPassword = mBinding.includeSignUpContant.tilSignUpPass;
        mTextInputLayoutConfirmPassword = mBinding.includeSignUpContant.tilSignUpConfirmPass;
 /*        mTextInputLayoutGender = mBinding.includeSignUpContant.tilSignUpGender;
        mTextInputLayoutBirthDate = mBinding.includeSignUpContant.tilSignUpBirthdate; */
/*     private TextInputLayout mTextInputLayoutGender;
    private TextInputLayout mTextInputLayoutBirthDate; */
        mTextInputEditTextName = mBinding.includeSignUpContant.tietSignUpName;
        mTextInputEditTextEmail = mBinding.includeSignUpContant.tietSignUpEmail;
        mTextInputEditTextPhone = mBinding.includeSignUpContant.tietSignUpPhone;
        mTextInputEditTextPassword = mBinding.includeSignUpContant.tietSignUpPass;
        mTextInputEditTextConfirmPassword = mBinding.includeSignUpContant.tietSignUpConfirmPass;
        mAutoCompleteTextViewGender = mBinding.includeSignUpContant.actvSignUpGender;
        mTextInputEditTextBirthDate = mBinding.includeSignUpContant.tietSignUpBirthdate;

        mTextViewPrivacy = mBinding.includeSignUpContant.tvSignUpPrivacy;
        mCheckBoxPrivacy = mBinding.includeSignUpContant.cbSignUp;

        mButtonSignUp = mBinding.btnSignUp;

        setupAutoCompleteVIew();
        }

    private void setupAutoCompleteVIew()
        {
        var adapter = new ArrayAdapter<>
                (getContext(), android.R.layout.simple_list_item_single_choice,
                        getGenderArrayList());
        mAutoCompleteTextViewGender.setAdapter(adapter);
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

        mTextInputEditTextBirthDate.setOnClickListener(v ->
            {
            mDataPickerFragment =
                    new DataPickerFragment();
            String dateSelected =
                    Objects.requireNonNull(mTextInputEditTextBirthDate.getText()).toString();


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
                        mBinding.includeSignUpContant.tietSignUpBirthdate.setText(date);
                        }
                    });

        }

    /**
     * called when clicked terms and cond.
     * open  Fragment Terms
     */
    private void onClickedTerms()
        {
        mTextViewPrivacy.setOnClickListener(
                v ->
                    {
                    NavDirections action =
                            SignUpFragmentDirections.actionSignUpFragmentToTermsFragment();

                    Navigation.findNavController(requireView()).navigate(action);
                    });
        }

    private void onClickedSignUp()
        {
        mButtonSignUp.setOnClickListener(v -> validateData());
        }

    private void validInputUser()
        {

        redColor=  ContextCompat.getColor(requireContext(),
                R.color.md_theme_dark_onError);
        validColor = ContextCompat.getColor(requireContext(),
                R.color.valid);
        final int boxStrokeError = (int) (3 * Resources.getSystem().getDisplayMetrics().density);
        final int boxStrokeDefault = (int) (2 * Resources.getSystem().getDisplayMetrics().density);


        validateName(boxStrokeError, boxStrokeDefault);
        validatePhone(boxStrokeError, boxStrokeDefault);
        validatePassword(boxStrokeError, boxStrokeDefault);


        }

    private void validateName(int boxStrokeError, int boxStrokeDefault)
        {
        mTextInputEditTextName.addTextChangedListener(new TextWatcher()
            {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
                {
                mTextInputLayoutName.setBoxStrokeWidthFocused(boxStrokeDefault);
                }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
                {


                if (TextUtils.isEmpty(s))
                    {
                    mTextInputLayoutName.setBoxStrokeColor(redColor);
                    mTextInputLayoutName.setBoxStrokeWidthFocused(boxStrokeError);
                    } else
                    {
                    mTextInputLayoutName.setBoxStrokeColor(validColor);
                    }
                }

            @Override
            public void afterTextChanged(Editable s)
                {
                mTextInputLayoutName.setBoxStrokeWidthFocused(boxStrokeDefault);
                }
            });
        }

    private void validatePhone(int boxStrokeError, int boxStrokeDefault)
        {
        mTextInputEditTextPhone.addTextChangedListener(new TextWatcher()
            {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
                {
                mTextInputLayoutPhone.setBoxStrokeWidthFocused(boxStrokeDefault);
                }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
                {

                if (TextUtils.isEmpty(s))
                    {
                    mTextInputLayoutPhone.setBoxStrokeColor(redColor);
                    mTextInputLayoutPhone.setBoxStrokeWidthFocused(boxStrokeError);
                    } else if (!s.toString().trim().matches(String.valueOf(Patterns.PHONE)))
                    {
                    mTextInputLayoutPhone.setError(getString(R.string.sign_up_invalid_mobile));
                    mTextInputLayoutPhone.setBoxStrokeColor(redColor);
                    } else
                    {
                    mTextInputLayoutPhone.setBoxStrokeColor(validColor);
                    mTextInputLayoutPhone.setError(null);
                    }
                }

            @Override
            public void afterTextChanged(Editable s)
                {
                mTextInputLayoutPhone.setError(null);
                }
            });
        }

    private void validatePassword(int boxStrokeError, int boxStrokeDefault)
        {
        final String[] password = {null};

        mTextInputEditTextPassword.addTextChangedListener(new TextWatcher()
            {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
                {
                mTextInputLayoutPassword.setBoxStrokeWidthFocused(boxStrokeDefault);
                }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
                {
                if (TextUtils.isEmpty(s))
                    {
                    mTextInputLayoutPassword.setBoxStrokeColor(redColor);
                    mTextInputLayoutPassword.setBoxStrokeWidthFocused(boxStrokeError);
                    } else if (s.length() < 8)
                    {
                    mTextInputLayoutPassword.setError(getString(R.string.sign_up_error_password));
                    } else
                    {
                    mTextInputLayoutPassword.setError(null);

                    mTextInputLayoutPassword.setBoxStrokeColor(validColor);
                    }
                }

            @Override
            public void afterTextChanged(Editable s)
                {
                password[0] = s.toString().trim();
                }
            });
        mTextInputEditTextConfirmPassword.addTextChangedListener(new TextWatcher()
            {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
                {
                mTextInputLayoutConfirmPassword.setBoxStrokeWidthFocused(boxStrokeDefault);
                }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
                {
                if (TextUtils.isEmpty(s))
                    {
                    mTextInputLayoutConfirmPassword.setBoxStrokeColor(redColor);
                    mTextInputLayoutConfirmPassword.setBoxStrokeWidthFocused(boxStrokeError);
                    } else if (!s.toString().trim().matches(String.valueOf(password[0])))
                    {

                    mTextInputLayoutConfirmPassword.setError(getString(R.string.sign_up_dontmatch_password));
                    } else
                    {
                    mTextInputLayoutConfirmPassword.setBoxStrokeColor(validColor);
                    }
                if (s.toString().trim().matches(String.valueOf(password[0])))
                    {
                    mTextInputLayoutConfirmPassword.setError(null);
                    }
                }

            @Override
            public void afterTextChanged(Editable s)
                {
                // add document why this method is empty
                }
            });

        }

    private boolean isValid()
        {
        var name = Objects.requireNonNull(mTextInputEditTextName.getText()).toString();
        var email = Objects.requireNonNull(mTextInputEditTextEmail.getText()).toString();
        var phone = Objects.requireNonNull(mTextInputEditTextPhone.getText()).toString();
        var password = Objects.requireNonNull(mTextInputEditTextPassword.getText()).toString();
        var confirmPassword = Objects.requireNonNull(mTextInputEditTextConfirmPassword.getText()).toString();

        var isCheck =
                mCheckBoxPrivacy.isChecked();

        mTextInputLayoutName.setError(null);
        mTextInputLayoutEmail.setError(null);
        mTextInputLayoutPhone.setError(null);
        mTextInputLayoutPassword.setError(null);
        mTextInputLayoutConfirmPassword.setError(null);

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
                mTextInputLayoutName.setError(getString(R.string.sign_up_empty_name));
                }
            if (TextUtils.isEmpty(email))
                {
                mTextInputLayoutEmail.setError(getString(R.string.sign_up_empty_email));
                }
            if (TextUtils.isEmpty(phone))
                {
                mTextInputLayoutPhone.setError(getString(R.string.sign_up_empty_phone));
                }
            if(password.length() < 8 )
                {
                mTextInputLayoutPassword.setError(getString(R.string.sign_up_error_password));
                }
            if (TextUtils.isEmpty(password))
                {
                mTextInputLayoutPassword.setError(getString(R.string.sign_up_empty_password));
                }
            if (TextUtils.isEmpty(confirmPassword) || !confirmPassword.matches(password))
                {
                mTextInputLayoutConfirmPassword.setError(getString(R.string.sign_up_empty_confirmpassword));
                }
            if (!isCheck)
                {
                mCheckBoxPrivacy.setButtonTintList(
                        ColorStateList.valueOf(Color.RED));
                }
            return false;
            }

        return true;
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
        var name = Objects.requireNonNull(mTextInputEditTextName.getText()).toString();
        var email = Objects.requireNonNull(mTextInputEditTextEmail.getText()).toString();
        var phone = Objects.requireNonNull(mTextInputEditTextPhone.getText()).toString();
        var password = Objects.requireNonNull(mTextInputEditTextPassword.getText()).toString();
        var gender =
                mAutoCompleteTextViewGender.getText().toString();


        if (isValid())
            {
            var user =
                    new RegistrationDTO(name, email, password, "+966" + phone, gender,
                            DataPickerFragment.getSelectionDate());
            Log.e(TAG, user.toString());

            addUserSignUpInAPI(user);

            }

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

                case SUCCESS -> {
                Log.e(TAG, "SUCCESS");
                Log.e(TAG, "authorizationResource");


                 new Handler(Looper.getMainLooper()).postDelayed
                        (this::successSignUp, 1000);

                }


                case ERROR -> {

                Log.e(TAG, "ERROR:" + authorizationResource.getMMessage());
                //Handle Error

                messageSnackbar(authorizationResource.getMMessage()); // SHow message in UI
                completeLoading();


                }
                case LOADING -> {

                Log.e(TAG, "loading:" + authorizationResource.getMMessage());

                showLoading();
                }

                }
            });

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
                mButtonSignUp.setVisibility(View.INVISIBLE);

                } else
                {
                mButtonSignUp.setVisibility(View.VISIBLE);


                }
            });
        }

    private void successSignUp()
        {
        NavDirections action =
                SignUpFragmentDirections.actionNavLoginSignUpFragmentToNavLoginSuccessFragment();

        Navigation.findNavController(requireView()).navigate(action);
        }


    private void setupViewModel()
        {

        mSignUpViewModel =  new ViewModelProvider(this).get(SignUpViewModel.class);
        }

    /**
     *     ProgressIndicator is show loading
      */
    private void showLoading()
        {
        mBinding.cpiSignUp.setVisibility(View.VISIBLE);
        mButtonSignUp.setEnabled(false);
        }

    /**
     *     ProgressIndicator is completed loading
      */
    private void completeLoading()
        {
        mBinding.cpiSignUp.setVisibility(View.INVISIBLE);
        mButtonSignUp.setEnabled(true);
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
                .setAnchorView(mButtonSignUp).show();
        }








    }

