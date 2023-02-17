package com.example.a5asec.ui.view.home;

import android.animation.ValueAnimator;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavBackStackEntry;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.bumptech.glide.Glide;
import com.example.a5asec.R;
import com.example.a5asec.data.model.api.Category;
import com.example.a5asec.databinding.FragmentServicesBinding;
import com.example.a5asec.ui.adapters.ServicesAdapter;
import com.example.a5asec.ui.view.viewmodel.CategoryViewModel;
import com.example.a5asec.utility.AdaptiveUtils;
import com.example.a5asec.utility.Resource;
import com.facebook.shimmer.Shimmer;
import com.facebook.shimmer.ShimmerFrameLayout;

import java.util.List;

import lombok.val;

/**
 * Second fragment for the price screen, shows service items of Category price Screen.
 */
public class ServicesFragment extends Fragment
    {
    private static final String TAG = "ServicesItemFragment";
    CategoryViewModel mItemsCategory;
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
                //  .setAutoStart(true)
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
        mBinding.setModel(mItemsCategory);

        mAdapter = new ServicesAdapter(this);

        mBinding.lvServices.setAdapter(mAdapter);

        onCLickItem(mBinding.lvServices);

        }

    private void onCLickItem(@NonNull ListView listView)
        {
        listView.setOnItemClickListener((parent, view, position, id) ->
            {
            Log.e(TAG, "items: " + mAdapter.getItem(position));

            mItemsCategory.setItemService(Resource.success(mAdapter.getItemService(position)));

                LaundryServicesBottomSheet laundryServicesFragment = new LaundryServicesBottomSheet();
                laundryServicesFragment.show(this.getParentFragmentManager(), LaundryServicesBottomSheet.TAG);



            });
        }

    private void setupObserver()
        {
        startAnimation();
        mItemsCategory.getItemServicesOfItemCategory().observe(getViewLifecycleOwner(), itemsEntities ->
            {
            switch (itemsEntities.mStatus)
                {
                case LOADING ->
                    {
                    startAnimation();
                    }
                case SUCCESS ->
                    {

                    Log.i(TAG, "suc:" + itemsEntities.getMData());
                    renderList(itemsEntities.getMData());

                    stopAnimation();
                    }
                case NULL ->
                    {
                    emptyList();

                    }
                case ERROR ->
                    {
                    Log.e(TAG, "ERROR");
                    startAnimation();

                    }

                }
            });

        }
    private void setupItemCategoryObserver()
        {
        mItemsCategory.getItemCategory().observe(getViewLifecycleOwner(), itemsEntities ->
            {
            switch (itemsEntities.mStatus)
                {
                case LOADING ->
                    {
                    Log.e(TAG, "LOADING");
                    startAnimation();
                    }
                case SUCCESS ->
                    {

                    Log.i(TAG, "suc:" + itemsEntities.getMData());

                        val language = AppCompatDelegate.getApplicationLocales().toLanguageTags();
                        var name = itemsEntities.getMData().getName(language);
                        var icon = itemsEntities.getMData().getIconUrl();
                         setupItemServiceWithTwoPane(name, icon);

                    stopAnimation();
                    }
                case NULL ->
                    {
                    Log.e(TAG, "NULL");
                    emptyList();

                    }
                case ERROR ->
                    {
                    Log.e(TAG, "ERROR");
                    startAnimation();

                    }

                }
            });

        }



    private void setupItemServiceWithTwoPane(@NonNull String name, @NonNull String iconUrl)
        {

        try
            {
            String url = "https://" + iconUrl;

            Glide.with(this)

                    .load(url)

                    .fitCenter()
                    .into(mBinding.imvServicesIcon);
            } catch (Exception e)
            {
            e.printStackTrace();
            Log.e(TAG, e.getMessage());
            }

        mBinding.tvServicesName.setText(name);

        }

    private void startAnimation()
        {
        mShimmerFrameLayout.startShimmer();
        mShimmerFrameLayout.setVisibility(View.VISIBLE);
        mBinding.imvServicesEmpty.setVisibility(View.GONE);
        mBinding.tvServicesEmpty.setVisibility(View.GONE);
        mBinding.lvServices.setVisibility(View.GONE);
        hideImageAndName();

        }

    private void stopAnimation()
        {
        mBinding.lvServices.setVisibility(View.VISIBLE);
        mBinding.imvServicesEmpty.setVisibility(View.GONE);
        mBinding.tvServicesEmpty.setVisibility(View.GONE);
        mShimmerFrameLayout.stopShimmer();
        mShimmerFrameLayout.setVisibility(View.GONE);
        showImageAndName();
        }

    private void emptyList()
        {
        mBinding.lvServices.setVisibility(View.GONE);
        mShimmerFrameLayout.setVisibility(View.GONE);
        hideImageAndName();
        mShimmerFrameLayout.stopShimmer();

        mBinding.imvServicesEmpty.setVisibility(View.VISIBLE);
        mBinding.tvServicesEmpty.setVisibility(View.VISIBLE);
        }

    private void showImageAndName()
        {
        if (!AdaptiveUtils.compactScreen(requireActivity()))
            {
            mBinding.imvServicesIcon.setVisibility(View.VISIBLE);
            mBinding.tvServicesName.setVisibility(View.VISIBLE);
            }
        }

    private void hideImageAndName()
        {
        if (!AdaptiveUtils.compactScreen(requireActivity()))
            {
            mBinding.imvServicesIcon.setVisibility(View.GONE);
            mBinding.tvServicesName.setVisibility(View.GONE);
            }
        }

    private void renderList(List<Category.ItemsEntity> category)
        {
        mAdapter.addServices(category);

        mAdapter.notifyDataSetChanged();
        }

    private void setupViewModel()
        {
        NavController navController = NavHostFragment.findNavController(this);
        NavBackStackEntry backStackEntry = navController.getBackStackEntry(R.id.nav_home_priceList);
        Log.e(TAG, "BackStack: " + backStackEntry);
        mItemsCategory = new ViewModelProvider(backStackEntry)
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
        }
    }