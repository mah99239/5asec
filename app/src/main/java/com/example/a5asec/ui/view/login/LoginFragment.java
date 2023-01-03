package com.example.a5asec.ui.view.login;

import android.content.Intent;
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
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.ProcessLifecycleOwner;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavBackStackEntry;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import com.example.a5asec.R;
import com.example.a5asec.data.remote.api.UserClient;
import com.example.a5asec.data.remote.api.UserHelper;
import com.example.a5asec.data.model.api.Users;
import com.example.a5asec.data.local.prefs.TokenPreferences;
import com.example.a5asec.databinding.FragmentLoginBinding;
import com.example.a5asec.ui.base.UserViewModelFactory;
import com.example.a5asec.ui.view.home.HomeActivity;
import com.example.a5asec.ui.view.viewmodel.UserViewModel;
import com.example.a5asec.utility.NetworkConnection;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * main UI for login screen
 */
public class LoginFragment extends Fragment implements LifecycleObserver
    {
    private static final String TAG = "LoginFragment";

    private FragmentLoginBinding mBinding;
    private TextInputLayout mTextInputLayoutEmail;
    private TextInputLayout mTextInputLayoutPassword;
    private TextInputEditText mTextInputEditTextEmail;
    private TextInputEditText mTextInputEditTextPassword;
    private Button mButtonLogin;
    private Button mButtonForgotPassword;
    private Button mButtonSignUp;
    private Button mButtonSkip;
    private UserViewModel mUserViewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
        {
        // Inflate the layout for this fragment
        TokenPreferences tokenPreferences = new TokenPreferences(getActivity());

        //this initialize view with DataBindingUtil
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false);

        return mBinding.getRoot();
        }

    @Override
    public void onViewCreated(@NonNull @NotNull View view,
                              @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState)
        {
        super.onViewCreated(view, savedInstanceState);
        setupUI();
        ProcessLifecycleOwner.get().getLifecycle().addObserver(this);

        }

    private void setupUI()
        {
        initView();
        setupViewModel();
        setupClickedButtons();
        networkAvailable();
        checkDateValidateWithUser();
        }

    private void initView()
        {
        mTextInputLayoutEmail = mBinding.tilLoginEmail;
        mTextInputLayoutPassword = mBinding.tilLoginPassword;
        mTextInputEditTextEmail = mBinding.tietLoginEmail;
        mTextInputEditTextPassword = mBinding.tietLoginPassword;
        mButtonLogin = mBinding.btnLoginLogin;
        mButtonForgotPassword = mBinding.btnLoginForgotPassword;
        mButtonSignUp = mBinding.btnLoginSignup;
        mButtonSkip = mBinding.btnLoginSkip;
        }

    /**
     * Handle  all click buttons
     */
    private void setupClickedButtons()
        {

        mButtonLogin.setOnClickListener(v ->
                checkLoginValidAPI());
        mButtonForgotPassword.setOnClickListener(v ->
            {
            NavDirections action =
                    LoginFragmentDirections.actionLoginFragmentToForgetPasswordFragment();

            Navigation.findNavController(mBinding.flLoginRoot).navigate(action);

            });

        mButtonSignUp.setOnClickListener(v ->
            {
            NavDirections action =
                    LoginFragmentDirections.actionLoginFragmentToSignUpFragment();

            Navigation.findNavController(mBinding.flLoginRoot).navigate(action);
            });

        mButtonSkip.setOnClickListener(v ->
                getLogin());


        }


    private void checkDateValidateWithUser()
        {

        mTextInputEditTextEmail.addTextChangedListener(new TextWatcher()
            {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
                {
                // Do nothing because not change before text
                }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
                {
                if (TextUtils.isEmpty(s))
                    {
                    mTextInputLayoutEmail.setBoxStrokeColor(getColorNotValid());
                    mTextInputLayoutEmail.setError(null);
                    } else if (!s.toString().trim().matches(String.valueOf(Patterns.EMAIL_ADDRESS)))
                    {
                    mTextInputLayoutEmail.setError(getString(R.string.login_email_invaild));
                    mTextInputLayoutEmail.setBoxStrokeColor(getColorNotValid());
                    } else
                    {
                    mTextInputLayoutEmail.setBoxStrokeColor(getColorValid());
                    mTextInputLayoutEmail.setError(null);
                    }
                }

            @Override
            public void afterTextChanged(Editable s)
                {

                }
            });
        mTextInputEditTextPassword.addTextChangedListener(new TextWatcher()
            {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
                {

                }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
                {
                if (TextUtils.isEmpty(s))
                    {
                    mTextInputLayoutPassword.setBoxStrokeColor(getColorNotValid());
                    } else
                    {
                    mTextInputLayoutPassword.setError(null);

                    mTextInputLayoutPassword.setBoxStrokeColor(getColorValid());
                    }
                }

            @Override
            public void afterTextChanged(Editable s)
                {

                }
            });
        }

    /**
     * @param login    check login is not match and empty
     * @param password check is  empty
     * @return If user and password are valid return true
     */
    private boolean isCheckValidate(String login, String password)
        {
        mTextInputEditTextPassword.setError(null);
        mTextInputEditTextPassword.setError(null);
        // Check login and pass are empty
        if (TextUtils.isEmpty(login) && TextUtils.isEmpty(password))
            {
            messageSnackbar(getString(R.string.login_empty));
            return false;
            }
        // Check login is empty return false
        if (TextUtils.isEmpty(login))
            {
            mTextInputEditTextEmail.setError(getString(R.string.login_email_empty));
            messageSnackbar(getString(R.string.login_email_empty));
            return false;
            }
        // Check password is empty return false
        if (TextUtils.isEmpty(password))
            {
            mTextInputEditTextPassword.setError(getString(R.string.login_password_empty));
            messageSnackbar(getString(R.string.login_password_empty));
            return false;
            }
        // Check login is not match return false
        if (!login.trim().matches(String.valueOf(Patterns.EMAIL_ADDRESS)))
            {
            messageSnackbar(getString(R.string.login_email_match));
            return false;
            }
        return true;
        }

    private void checkLoginValidAPI()
        {

        String login = Objects.requireNonNull(mTextInputEditTextEmail.getText()).toString();
        String password = Objects.requireNonNull(mTextInputEditTextPassword.getText()).toString();

        if (isCheckValidate(login, password))
            {

            Users users = new Users(login, password);
            loginAPI(users); // Check login in api
            }


        }

    private void loginAPI(Users login)
        {

        showLoading();  // ProgressIndicator is show and visible
        mUserViewModel.setLogin(login);
        mUserViewModel.getAuthenticateUsers().observe(getViewLifecycleOwner(), authorizationResource ->
            {

            Log.e(TAG, String.valueOf(authorizationResource.mStatus));

            switch (authorizationResource.mStatus)
                {

                case SUCCESS -> {
                Log.e(TAG, "SUCCESS");
                Log.e(TAG, "SUCCESS = " + authorizationResource.getMData());

                String token = authorizationResource.getMData().getAccess_token();
                String refreshToken = authorizationResource.getMData().getRefresh_token();
                long expireIn = authorizationResource.getMData().getExpires_in();

                TokenPreferences.setToken(token, refreshToken, expireIn);
                TokenPreferences.setPassword(mTextInputEditTextPassword.getText().toString());
                new Handler(Looper.getMainLooper()).postDelayed
                        (this::openHomeActivity, 1000);

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
                case NULL -> {
                Log.e(TAG, "NULL:" + authorizationResource.getMMessage());

                }

                }
            });


        }

    public void getLogin()
        {

        }

    private void openHomeActivity()
        {
        Intent intent = new Intent(getContext(), HomeActivity.class);
        startActivity(intent);
        getActivity().finish();
        }


    private void messageSnackbar(@NonNull String message)
        {


        if (message.contains("size=104"))
            {
            message = getString(R.string.login_inactive);
            } else if (message.contains("size=80"))
            {
            message = getString(R.string.login_invaild);
            } else if (message.equals("Unauthorised User"))
            {
            message = getString(R.string.login_invaild);
            }

        Snackbar.make(mBinding.getRoot(), message, BaseTransientBottomBar.LENGTH_LONG)
                .setAnchorView(mBinding.btnLoginLogin).show();
        }

    /**
     * ProgressIndicator is show loading
     */
    private void showLoading()
        {
        mBinding.llLoginContainerProgress.setVisibility(View.VISIBLE);
        mButtonLogin.setEnabled(false);
        }

    /**
     * ProgressIndicator is completed loading
     */
    private void completeLoading()
        {
        mBinding.flLoginRoot.setBackground(null);
        mBinding.llLoginContainerProgress.setVisibility(View.GONE);
        mBinding.btnLoginLogin.setEnabled(true);
        }


    private int getColorValid()
        {
        return getResources().getColor(R.color.valid, getActivity().getTheme());

        }

    private int getColorNotValid()
        {
        return getResources().getColor(R.color.md_theme_dark_onError, getActivity().getTheme());

        }


    /**
     * Check network is available
     */
    public void networkAvailable()
        {
        NetworkConnection networkConnection = new NetworkConnection(getContext());
        networkConnection.observe(getViewLifecycleOwner(), isConnected ->
            {
            // if internet not connect, Don't show button
            if (!isConnected)
                {
                mBinding.btnLoginLogin.setVisibility(View.INVISIBLE);
                mBinding.btnLoginSignup.setVisibility(View.INVISIBLE);
                mBinding.btnLoginSkip.setVisibility(View.INVISIBLE);
                mBinding.btnLoginForgotPassword.setVisibility(View.INVISIBLE);
                }
            // internet is connect, Show button
            else
                {
                mBinding.btnLoginLogin.setVisibility(View.VISIBLE);
                mBinding.btnLoginSignup.setVisibility(View.VISIBLE);
                mBinding.btnLoginSkip.setVisibility(View.VISIBLE);
                mBinding.btnLoginForgotPassword.setVisibility(View.VISIBLE);

                }
            });
        }
    private void setupViewModel()
        {
        NavController navController = NavHostFragment.findNavController(this);
        NavBackStackEntry backStackEntry = navController.getBackStackEntry(R.id.nav_login);

        mUserViewModel = new ViewModelProvider(backStackEntry,
                new UserViewModelFactory(new UserHelper(new UserClient()))).get(UserViewModel.class);
        }
    @Override
    public void onDestroy()
        {
        super.onDestroy();
        mBinding = null;
        }


    }