package com.example.a5asec.ui.view.home;

import android.animation.ValueAnimator;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavBackStackEntry;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.example.a5asec.R;
import com.example.a5asec.data.model.api.Category;
import com.example.a5asec.databinding.FragmentServicesBinding;
import com.example.a5asec.ui.adapters.ServicesAdapter;
import com.example.a5asec.ui.view.viewmodel.CategoryViewModel;
import com.example.a5asec.utility.AdaptiveUtils;
import com.facebook.shimmer.Shimmer;
import com.facebook.shimmer.ShimmerFrameLayout;

import java.util.List;

import timber.log.Timber;

/**
 * Second fragment for the price screen, shows service items of Category price Screen.
 */
public class ServicesFragment extends Fragment
    {
    private static final String TAG = "ServicesItemFragment";
    private CategoryViewModel mCategoryViewModel;
    private FragmentServicesBinding mBinding;
    private ServicesAdapter mAdapter;
    private ShimmerFrameLayout mShimmerFrameLayout;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
        {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_services,
                container, false);
        mBinding.setLifecycleOwner(this);

        return mBinding.getRoot();
        }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
        {
        super.onViewCreated(view, savedInstanceState);
        setupShimmerAnimation();

        setupUi();

        }

    private void setupShimmerAnimation()
        {
        var shimmer = new Shimmer.AlphaHighlightBuilder()
                .setDuration(1000L) // how long the shimmering animation takes to do one full sweep
                .setRepeatMode(ValueAnimator.REVERSE)
                .build();

        mShimmerFrameLayout = mBinding.shService;
        mShimmerFrameLayout.setShimmer(shimmer);

        }

    private void setupUi()
        {
        setupAdapter();
        setupViewModel();
        setupObserver();

        if (!AdaptiveUtils.compactScreen(requireActivity()))
            {
            setupItemCategoryObserver();
            }
        }

    private void setupAdapter()
        {
        mBinding.setModel(mCategoryViewModel);

        mAdapter = new ServicesAdapter();

        mBinding.lvServices.setAdapter(mAdapter);

        onCLickItem();

        }

    private void onCLickItem()
        {
        mBinding.lvServices.setOnItemClickListener((parent, view, position, id) ->
            {
            Timber.tag(TAG).e("onCLickItem() -> position: %s",position);

            mCategoryViewModel.setItemService(position);

            LaundryServicesBottomSheet laundryServicesFragment = new LaundryServicesBottomSheet();
            laundryServicesFragment.show(this.getParentFragmentManager(), LaundryServicesBottomSheet.TAG);

            });
        }

    private void setupObserver()
        {
        mCategoryViewModel.getItemServicesOfItemCategory().observe(getViewLifecycleOwner(), itemsEntities ->
            {
            switch (itemsEntities.mStatus)
                {
                case LOADING -> startAnimation();

                case SUCCESS ->
                    {

                    Timber.tag(TAG).i("suc:%s", itemsEntities.getMData());
                    renderList(itemsEntities.getMData());
                new Handler(Looper.getMainLooper()).postDelayed(this::stopAnimation,500);
                    }
                case NULL -> emptyList();

                case ERROR ->
                    {
                    Timber.tag(TAG).e("ERROR");
                    startAnimation();

                    }

                }
            });

        }

    private void setupItemCategoryObserver()
        {
        mCategoryViewModel.getItemCategory().observe(getViewLifecycleOwner(), itemsEntities ->
            {
            switch (itemsEntities.mStatus)
                {
                case LOADING ->
                    {
                    Timber.tag(TAG).e("LOADING");
                    startAnimation();
                    }
                case SUCCESS ->
                    {

                    Timber.tag(TAG).i("suc:%s", itemsEntities.getMData());
                    var currentLanguage = AppCompatDelegate.getApplicationLocales().toLanguageTags();
                    var name = itemsEntities.getMData().getName(currentLanguage);
                    var icon = itemsEntities.getMData().getIconUrl();
                    setupItemServiceWithTwoPane(name, icon);

                    stopAnimation();
                    }
                case NULL ->
                    {
                    Timber.tag(TAG).e("NULL");
                    emptyList();

                    }
                case ERROR ->
                    {
                    Timber.tag(TAG).e("ERROR");
                    startAnimation();

                    }

                }
            });

        }


    @Override
    public void onStart()
        {
        super.onStart();
        startAnimation();
        Timber.tag(TAG).i("onStart()->");


        }


    private void setupItemServiceWithTwoPane(@NonNull String name, @NonNull String iconUrl)
        {


        mBinding.setUrl(iconUrl);
        mBinding.setItemName(name);

        }

    private void startAnimation()
        {
        mShimmerFrameLayout.startShimmer();
        mShimmerFrameLayout.setVisibility(View.VISIBLE);
        mBinding.gServicesEmpty.setVisibility(View.GONE);
        mBinding.gServicesItems.setVisibility(View.GONE);

        }

    private void stopAnimation()
        {
        mBinding.gServicesEmpty.setVisibility(View.GONE);
        mBinding.gServicesItems.setVisibility(View.VISIBLE);
        mShimmerFrameLayout.setVisibility(View.GONE);
        mShimmerFrameLayout.stopShimmer();
        }

    private void emptyList()
        {
        mBinding.gServicesItems.setVisibility(View.GONE);
        mShimmerFrameLayout.setVisibility(View.GONE);
        mShimmerFrameLayout.stopShimmer();

        mBinding.gServicesEmpty.setVisibility(View.VISIBLE);
        }


    private void renderList(List<Category.ItemsEntity> listItemCategory)
        {
        mAdapter.updateServices(listItemCategory);
        }

    private void setupViewModel()
        {
        NavController navController = NavHostFragment.findNavController(this);
        NavBackStackEntry backStackEntry = navController.getBackStackEntry(R.id.nav_home_priceList);

        mCategoryViewModel = new ViewModelProvider(backStackEntry)
                .get(CategoryViewModel.class);

        }

    @Override
    public void onStop()
        {
        stopAnimation();
        super.onStop();
        }

    @Override
    public void onDestroy()
        {
        super.onDestroy();
        mBinding = null;
        mShimmerFrameLayout = null;
        }
    }