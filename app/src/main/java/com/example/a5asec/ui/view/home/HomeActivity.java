package com.example.a5asec.ui.view.home;


import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.OptIn;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.os.LocaleListCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.window.layout.WindowMetrics;
import androidx.window.layout.WindowMetricsCalculator;

import com.example.a5asec.R;
import com.example.a5asec.data.local.prefs.TokenPreferences;
import com.example.a5asec.data.remote.api.SettingClient;
import com.example.a5asec.data.remote.api.SettingHelper;
import com.example.a5asec.data.remote.api.UserClient;
import com.example.a5asec.data.remote.api.UserHelper;
import com.example.a5asec.databinding.ActivityHomeBinding;
import com.example.a5asec.ui.base.CartViewModelFactory;
import com.example.a5asec.ui.base.SettingViewModelFactory;
import com.example.a5asec.ui.base.UserViewModelFactory;
import com.example.a5asec.ui.view.viewmodel.CartViewModel;
import com.example.a5asec.ui.view.viewmodel.SettingViewModel;
import com.example.a5asec.ui.view.viewmodel.UserViewModel;
import com.example.a5asec.utility.GlideApp;
import com.example.a5asec.utility.Injection;
import com.example.a5asec.utility.NetworkConnection;
import com.example.a5asec.utility.Status;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.badge.BadgeUtils;
import com.google.android.material.badge.ExperimentalBadgeUtils;
import com.google.android.material.snackbar.Snackbar;

import java.util.Objects;


@OptIn(markerClass = ExperimentalBadgeUtils.class)
public class HomeActivity extends AppCompatActivity
    {

    public enum WindowSizeClass
        {COMPACT, MEDIUM, EXPANDED}

    private static final String TAG = "HomeActivity";
    NavController navController;
    private AppBarConfiguration mAppBarConfiguration;
    private ActivityHomeBinding mBinding;

    private SettingViewModel mSettingViewModel;
    private BadgeDrawable badgeDrawable;

    private UserViewModel mUserViewModel;
    private CartViewModel mCartViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState)
        {
        super.onCreate(savedInstanceState);

        mBinding = ActivityHomeBinding.inflate(getLayoutInflater());

        setContentView(mBinding.getRoot());
        ViewGroup container = mBinding.clHomeContainer;
        container.addView(new View(this)
            {
            @Override
            protected void onConfigurationChanged(Configuration newConfig)
                {
                super.onConfigurationChanged(newConfig);
                computeWindowSizeClasses();
                }
            });
        computeWindowSizeClasses();

        checkConnections();

        new TokenPreferences(this);
        setupViewModel();
        setupObservers();

        setupCartBadge();
        }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig)
        {
        super.onConfigurationChanged(newConfig);
        AppCompatDelegate.getApplicationLocales();


        }

    private void computeWindowSizeClasses()
        {
        WindowMetrics metrics = WindowMetricsCalculator.getOrCreate()
                .computeCurrentWindowMetrics(this);

        float widthDp = metrics.getBounds().width() /
                getResources().getDisplayMetrics().density;
        WindowSizeClass widthWindowSizeClass;

        if (widthDp < 600f)
            {
            widthWindowSizeClass = WindowSizeClass.COMPACT;
            } else if (widthDp < 840f)
            {
            widthWindowSizeClass = WindowSizeClass.MEDIUM;
            } else
            {
            widthWindowSizeClass = WindowSizeClass.EXPANDED;
            }


        if (widthWindowSizeClass == WindowSizeClass.COMPACT)
            {
            setupNavigationForCompact();
            } else
            {
            setupNavigationForRail();
            }

  /*   float heightDp = metrics.getBounds().height() /
            getResources().getDisplayMetrics().density;
    WindowSizeClass heightWindowSizeClass;

    if (heightDp < 480f) {
    heightWindowSizeClass = WindowSizeClass.COMPACT;
    } else if (heightDp < 900f) {
    heightWindowSizeClass = WindowSizeClass.MEDIUM;
    } else {
    heightWindowSizeClass = WindowSizeClass.EXPANDED;
    } */

        // Use widthWindowSizeClass and heightWindowSizeClass.
        }

    private void setupNavigationForCompact()
        {
        setSupportActionBar(mBinding.toolbarHome);
        setupToolBar();
        setupNavigationBottom();


        }

    private void setupNavigationBottom()
        {
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home_priceList, R.id.nav_home_orders, R.id.nav_home_setting)
                .build();
        var navHostFragment =(NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment_home);

        navController = navHostFragment.getNavController();
        NavigationUI.setupActionBarWithNavController( this,navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(mBinding.navViewHome, navController);
        }
    private void setupNavigationForRail()
        {
        setupNavigationRil();
        setupHeaderNavigationRail();
        }
    private void setupNavigationRil()
        {
        setSupportActionBar(mBinding.toolbarHome);
         getSupportActionBar().hide();
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home_priceList, R.id.nav_home_orders, R.id.nav_home_setting)
                .build();
        var navHostFragment =(NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment_home);

         navController = navHostFragment.getNavController();
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);

            NavigationUI.setupWithNavController(mBinding.navRailHome, navController);

        }

    private void setupNavigationDrawer()
        {
/*
        BottomNavigationView drawer = mBinding.drawerHome;
        NavigationView navigationView = mBinding.ngvHome;


        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home_profile,
                R.id.nav_home_orders,
                R.id.nav_home_priceList,
                R.id.nav_home_discount,
                R.id.nav_home_terms,
                R.id.nav_home_contact,
                R.id.nav_home_setting,
                R.id.nav_home_about,
                R.id.nav_logOut
        )
                .setOpenableLayout(drawer)

                .build();

        navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_home);

        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController); */
/*
        NavigationUI.setupWithNavController(mToolbar, navController, mAppBarConfiguration);
*/


        }


    @Override
    public boolean onSupportNavigateUp()
        {
        navController = Navigation.findNavController(this, R.id.nav_host_fragment_home);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
        }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
        {
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;

        }

    private void setupToolBar()
        {

        if (mBinding.toolbarHome != null)
            {
            mBinding.toolbarHome.setOnMenuItemClickListener(item ->
                {
                switch (item.getItemId())
                    {
                    case R.id.action_home_notification:
                        // User chose the "notification" item, show the app settings UI...
                        Snackbar.make(getParent(), mBinding.getRoot(), "not", 300).show();
                        navController.navigateUp();

                        navController.navigate(R.id.nav_home_notification);
                        return true;

                    case R.id.action_home_cart:
                        // User chose the "cart" action, mark the current item
                        // as a favorite...
                        navController.navigateUp();

                        navController.navigate(R.id.nav_home_cart);

                        return true;

                    default:
                        // If we got here, the user's action was not recognized.
                        // Invoke the superclass to handle it.
                        return HomeActivity.super.onOptionsItemSelected(item);
                    }
                });
            }

        }

    private void setupHeaderNavigationRail()
        {
        mBinding.navHeaderHome.btnRailHeaderCart.setOnClickListener(
                v ->
                    {
                    navController.navigateUp();

                    navController.navigate(R.id.nav_home_cart);
                    });
        mBinding.navHeaderHome.btnRailHeaderNotifaction.setOnClickListener(v ->
            {
            Snackbar.make(getParent(), mBinding.getRoot(), "btnRailHeader Notifaction", 300).show();
            navController.navigateUp();

            navController.navigate(R.id.nav_home_notification);
            });
        }

    private void setupCartBadge()
        {

        mCartViewModel.getCount().observe(this, observeCount ->
            {
            switch (observeCount.getMStatus())
                {
                case SUCCESS ->
                    {
                    var count = observeCount.getMData();
                    Log.e(TAG, "count = " + count);
                    if (count > 0)
                        {
                        badgeDrawable = BadgeDrawable.create(this);

                        badgeDrawable.setNumber(count);
                        }
                    BadgeUtils.attachBadgeDrawable(badgeDrawable, mBinding.toolbarHome, R.id.action_home_cart);

                    }

                }
            });
        }


    private void setupObservers()
        {
        setupSettingObserver();
        setupUserObserver();
        }

    private void setupSettingObserver()
        {
        mSettingViewModel.getSetting().observe(this, settingResource ->
            {
            switch (settingResource.mStatus)
                {

                case SUCCESS -> Log.e(TAG + ", setupSettingObserver", "SUCCESS");

                case LOADING -> Log.e(TAG + "setupSettingObserver", "LOADING");
                case ERROR -> Log.e(TAG + ", setupSettingObserver", "ERROR");
                }
            });

        }


    private void setupUserObserver()
        {
        mUserViewModel.getUser().observe(this, userResource ->
            {
            switch (userResource.mStatus)
                {

                case SUCCESS ->
                    {
                    String keyLanguage =
                            userResource.getMData().getLangKey();
                    LocaleListCompat appLocale = LocaleListCompat.forLanguageTags(keyLanguage);
                    AppCompatDelegate.setApplicationLocales(appLocale);

                    Log.e(TAG + ", setupUserObserver", "suc:" + userResource.getMData());


                    }

                case LOADING -> Log.e(TAG + ":user", "LOADING");
                case ERROR -> Log.e(TAG + ":user", "ERROR");
                }
            });

        }

    private void setupViewModel()
        {
        setupSettingViewModel();
        setupUserViewModel();
        setupCartViewModel();
        }

    private void setupSettingViewModel()
        {

        mSettingViewModel = new ViewModelProvider(this,
                new SettingViewModelFactory(new SettingHelper(new SettingClient()))).get(SettingViewModel.class);
        }

    private void setupUserViewModel()
        {
        mUserViewModel = new ViewModelProvider(this,
                new UserViewModelFactory(new UserHelper(new UserClient()))).get(UserViewModel.class);
        }

    private void setupCartViewModel()
        {

        CartViewModelFactory viewModelFactory = Injection.provideViewModelFactory(this);
        mCartViewModel = new ViewModelProvider(this, viewModelFactory)
                .get(CartViewModel.class);
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


            if (!isConnected)
                {
                mBinding.tvHomeNetwork.setText(errorNetworkMessage);
                mBinding.tvHomeNetwork.setBackgroundColor(baseColor);
                mBinding.tvHomeNetwork.setTextColor(textBaseColor);
                mBinding.tvHomeNetwork.setVisibility(View.VISIBLE);
                } else
                {
                try
                    {
                    if (mSettingViewModel.getSetting().getValue().getMStatus().equals(Status.ERROR))
                        {
                        mSettingViewModel.reload();

                        }
                    } catch (NullPointerException e)
                    {
                    Log.e(TAG, e.toString());
                    }
                mBinding.tvHomeNetwork.setBackgroundColor(vaildColor);
                mBinding.tvHomeNetwork.setTextColor(baseColor);

                mBinding.tvHomeNetwork.setText(backNetworkMessage);
                new Handler(Looper.getMainLooper()).postDelayed(() ->
                        mBinding.tvHomeNetwork.setVisibility(View.GONE), 1000);

                }
            });
        }

    @Override
    public void onLowMemory()
        {
        super.onLowMemory();
        GlideApp.get(this).clearMemory();
        GlideApp.with(this).onLowMemory();
        }

    @Override
    public void onTrimMemory(int level)
        {
        super.onTrimMemory(level);
        GlideApp.get(this).onTrimMemory(level);
        GlideApp.with(this).onTrimMemory(level);
        }

    @Override
    protected void onDestroy()
        {
        super.onDestroy();
        GlideApp.get(this).clearMemory();
        }
    }