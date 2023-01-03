package com.example.a5asec.ui.view.login;

import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.content.ContextCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.a5asec.R;
import com.example.a5asec.databinding.ActivityMainBinding;
import com.example.a5asec.utility.NetworkConnection;
import com.google.android.material.appbar.MaterialToolbar;


public class MainActivity extends AppCompatActivity
    {
    private static final String TAG = "MainActivity";
    AppBarConfiguration appBarConfiguration;
    ActivityMainBinding mBinding;
    MaterialToolbar mToolbar;
    NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState)
        {
        super.onCreate(savedInstanceState);
        mBinding = ActivityMainBinding.inflate(getLayoutInflater());

        setContentView(mBinding.getRoot());

        setupUI();
        }

    private void setupUI()
        {

        mToolbar = this.mBinding.tbMain;
        setSupportActionBar(mToolbar);

        setupNavigationUi();
        checkConnections();
        }

    private void setupNavigationUi()
        {
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(mBinding.fcMainContainer.getId());
        navController = navHostFragment != null ? navHostFragment.getNavController() : null;
        assert navController != null;

        AppBarConfiguration build = new AppBarConfiguration.Builder(navController.getGraph()).build();
        appBarConfiguration = build;
        NavigationUI.setupActionBarWithNavController(this, navController, build);
        }

    @Override
    public boolean onSupportNavigateUp()
        {
        NavController findNavController = Navigation.findNavController(this, R.id.fc_main_container);
        this.navController = findNavController;
        return findNavController.navigateUp() || super.onSupportNavigateUp();
        }

    private void checkConnections()
        {
        NetworkConnection networkConnection = new NetworkConnection(getApplication());
        var baseColor = ContextCompat.getColor(getApplication(),
                R.color.md_theme_light_shadow);
        var textBaseColor = ContextCompat.getColor(getApplication(),
                R.color.md_theme_light_outlineVariant);

        var vaildColor = ContextCompat.getColor(getApplication(),
                R.color.md_theme_light_inversePrimary);

        networkConnection.observe(this, isConnected ->
            {
            var errorNetworkMessage = getResources().getString(R.string.all_network_error);
            var backNetworkMessage = getResources().getString(R.string.all_network_back);


            if (!isConnected) // if internet not connect, Show message try connection internet
                {
                mBinding.tvMainNetwork.setText(errorNetworkMessage);
                mBinding.tvMainNetwork.setBackgroundColor(baseColor);
                mBinding.tvMainNetwork.setTextColor(textBaseColor);
                mBinding.tvMainNetwork.setVisibility(View.VISIBLE);
                } else  // internet is connect,
                {

                mBinding.tvMainNetwork.setBackgroundColor(vaildColor);
                mBinding.tvMainNetwork.setTextColor(baseColor);

                mBinding.tvMainNetwork.setText(backNetworkMessage);
                new Handler(Looper.getMainLooper()).postDelayed(() ->
                        mBinding.tvMainNetwork.setVisibility(View.GONE), 1000);

                }
            });
        }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig)
        {
        super.onConfigurationChanged(newConfig);
        AppCompatDelegate.getApplicationLocales();
        }

    @Override
    public void onDestroy()
        {
        super.onDestroy();
        this.mBinding = null;
        }
    }