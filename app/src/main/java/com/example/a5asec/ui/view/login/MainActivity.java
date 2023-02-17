package com.example.a5asec.ui.view.login;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Gravity;
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
import androidx.transition.Slide;
import androidx.transition.Transition;
import androidx.transition.TransitionManager;

import com.example.a5asec.R;
import com.example.a5asec.databinding.ActivityMainBinding;
import com.example.a5asec.utility.NetworkConnection;


public class MainActivity extends AppCompatActivity
    {
    AppBarConfiguration appBarConfiguration;
    ActivityMainBinding mBinding;
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

        setSupportActionBar(mBinding.tbMain);

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
                R.color.md_theme_light_onSurface);
        var textBaseColor = ContextCompat.getColor(getApplication(),
                R.color.md_theme_light_surface);

        var vaildColor = ContextCompat.getColor(getApplication(),
                R.color.md_theme_light_primaryContainer);

        networkConnection.observe(this, isConnected ->
            {
            var errorNetworkMessage = getResources().getString(R.string.all_network_error);
            var backNetworkMessage = getResources().getString(R.string.all_network_back);


            if (Boolean.FALSE.equals(isConnected)) // if internet not connect, Show message try connection internet
                {
                mBinding.tvMainNetwork.setText(errorNetworkMessage);
                mBinding.tvMainNetwork.setBackgroundColor(baseColor);
                mBinding.tvMainNetwork.setTextColor(textBaseColor);
                slideAnimation(false, 250L); // isConnected is false
                } else  // internet is connect,
                {
                slideAnimation(isConnected, 3000L);// isConnected is true
                mBinding.tvMainNetwork.setBackgroundColor(vaildColor);
                mBinding.tvMainNetwork.setTextColor(baseColor);

                mBinding.tvMainNetwork.setText(backNetworkMessage);


                }
            });
        }

    private void slideAnimation(boolean show, long duration) {


    Transition transition = new Slide(Gravity.BOTTOM);
    transition.setDuration(duration);
    transition.addTarget(mBinding.tvMainNetwork);

    TransitionManager.beginDelayedTransition(mBinding.clMain, transition);
    mBinding.tvMainNetwork.setVisibility(!show ? View.VISIBLE : View.GONE);
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