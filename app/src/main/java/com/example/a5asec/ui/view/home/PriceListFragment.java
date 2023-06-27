package com.example.a5asec.ui.view.home;

import android.animation.ValueAnimator;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavBackStackEntry;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.slidingpanelayout.widget.SlidingPaneLayout;

import com.example.a5asec.R;
import com.example.a5asec.data.model.api.Category;
import com.example.a5asec.databinding.FragmentPriceListBinding;
import com.example.a5asec.ui.adapters.BannersAdapter;
import com.example.a5asec.ui.adapters.PriceAdapter;
import com.example.a5asec.ui.base.BannersViewModelFactory;
import com.example.a5asec.ui.base.CategoryViewModelFactory;
import com.example.a5asec.ui.view.viewmodel.BannersViewModel;
import com.example.a5asec.ui.view.viewmodel.CategoryViewModel;
import com.example.a5asec.utility.AdaptiveUtils;
import com.example.a5asec.utility.NetworkConnection;
import com.facebook.shimmer.Shimmer;
import com.facebook.shimmer.ShimmerFrameLayout;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import lombok.val;
import timber.log.Timber;


/**
 * Main Fragment for the price screen
 */
@AndroidEntryPoint

public class PriceListFragment extends Fragment
{
    private static final String TAG = "PriceListFragment";

    @Inject
    BannersViewModelFactory bannersViewModelFactory;
    @Inject
    CategoryViewModelFactory categoryViewModelFactory;
    @Inject
    NetworkConnection networkConnection;
    private FragmentPriceListBinding mBinding;
    private CategoryViewModel mCategoryViewModel;
    private BannersViewModel mBannersViewModel;
    private ShimmerFrameLayout mShimmerFrameLayout;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        mBinding = DataBindingUtil.inflate(inflater,
                R.layout.fragment_price_list, container, false);
        return mBinding.getRoot();

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        mBinding.setLifecycleOwner(this);

        setupUi();

    }

    private void setupUi()
    {

        setupShimmerAnimation();
        setupRefreshView();
        setupViewModel();
        setupBannersAdapter();
        setupCategoryAdapter();
        setupObserverBanners();
        setupCategoriesWithTwoPane();
        checkConnectionsToInitCategory();

    }


    private void setupShimmerAnimation()
    {
        var shimmer = new Shimmer.AlphaHighlightBuilder().setDuration(
                        500L) // how long the shimmering animation takes to do one full sweep
                .setRepeatMode(ValueAnimator.REVERSE)
                //  .setAutoStart(true)
                .build();


        mShimmerFrameLayout = mBinding.shPrice;
        mShimmerFrameLayout.setShimmer(shimmer);

    }

    private void setupRefreshView()
    {
        mBinding.swipePriceListRoot.setOnRefreshListener(() -> {
            reloadDataUI();
            mBinding.swipePriceListRoot.setRefreshing(false);
        });

    }

    /**
     * called when user swipe to refresh data in screen.
     */
    private void reloadDataUI()
    {
        mCategoryViewModel.refreshCategories();
        mBannersViewModel.reload();


    }


    private void setupViewModel()
    {
        setupCategoryViewModel();
        setupBannersViewModel();
    }

    private void setupCategoryViewModel()
    {
        NavController navController = NavHostFragment.findNavController(this);
        NavBackStackEntry backStackEntry = navController.getBackStackEntry(
                R.id.nav_home_priceList);

        mCategoryViewModel = new ViewModelProvider(backStackEntry,
                categoryViewModelFactory).get(CategoryViewModel.class);
    }

    private void setupBannersViewModel()
    {

        mBannersViewModel = new ViewModelProvider(this,
                bannersViewModelFactory).get(BannersViewModel.class);
    }

    /**
     *
     */
    private void setupBannersAdapter()
    {
        mBinding.setBannerAdapter(new BannersAdapter());

    }

    private void setupCategoryAdapter()
    {

        PriceAdapter priceAdapter = new PriceAdapter();
        priceAdapter.setClickListener(this::onItemClick);
        mBinding.setPriceAdapter(priceAdapter);
    }

    public void onItemClick(View view, int position)
    {
        fetchItemsCategory(position);

        if (AdaptiveUtils.compactScreen(requireActivity())) {
            NavDirections action =
                    PriceListFragmentDirections.actionPriceListFragmentToServicesItemFragment();
            Navigation.findNavController(view).navigate(action);
        }

    }


    private void setupObserverBanners()
    {
        mBannersViewModel.getBanners()
                .observe(getViewLifecycleOwner(), bannersObserve -> {
                    val tagObserverBanners = "setupObserverBanners()-> ";

                    switch (bannersObserve.mStatus) {
                        case SUCCESS -> {
                            Timber.tag(TAG).e("%sSUCCESS", tagObserverBanners);
                            mBinding.setBannerList(bannersObserve.getMData());
                        }
                        case LOADING -> Timber.tag(TAG)
                                .e("%sLOADING", tagObserverBanners);

                        case ERROR -> {
                            startAnimation();
                            Timber.tag(TAG).e("%sERROR", tagObserverBanners);
                        }
                        case NULL -> Timber.tag(TAG).e("%sNULL", tagObserverBanners);

                    }

                });
    }

    private void setupCategoriesWithTwoPane()
    {

        boolean isCompact = AdaptiveUtils.compactScreen(requireActivity());
        Timber.tag(TAG).e("setupTwoPane:  isCompact = %s", isCompact);
        setupObserverCategory(isCompact);

        if (!isCompact) {

            if (mBinding.splPrice != null) {

                requireActivity().getOnBackPressedDispatcher()
                        .addCallback(getViewLifecycleOwner(),
                                new PriceOnBackPressedCallback(
                                        mBinding.splPrice));

                mBinding.splPrice.open();
                mBinding.splPrice.setLockMode(
                        SlidingPaneLayout.LOCK_MODE_LOCKED);

            }
        } else {

            if (mBinding.splPrice != null) mBinding.splPrice.setLockMode(
                    SlidingPaneLayout.LOCK_MODE_LOCKED);


        }
    }

    private void setupObserverCategory(boolean isCompact)
    {
        mCategoryViewModel.getCategory()
                .observe(getViewLifecycleOwner(), categoryObserve -> {
                    val tagObserverCategory = "setupObserverCategory() ->";
                    switch (categoryObserve.mStatus) {

                        case SUCCESS -> {
                            Timber.tag(TAG)
                                    .e(tagObserverCategory + "SUCCESS, = " + categoryObserve.getMData());
                            stopAnimation();
                            fetchListCategories(categoryObserve.getMData());
                            if (!isCompact) {
                                mCategoryViewModel.setItemCategoryWithTwoPane();
                            }

                        }

                        case LOADING -> {
                            Timber.tag(TAG).e("%sLOADING", tagObserverCategory);
                            startAnimation();

                        }
                        case ERROR -> {


                            Timber.tag(TAG).e("%sERROR", tagObserverCategory);
                            startAnimation();
                        }

                        case NULL -> {
                            startAnimation();
                            Timber.tag(TAG).e("%sNULL", tagObserverCategory);

                        }
                    }
                });

    }


    private void startAnimation()
    {
        mShimmerFrameLayout.setVisibility(View.VISIBLE);
        mShimmerFrameLayout.startShimmer();
        mBinding.rvPriceList.setVisibility(View.GONE);
        mBinding.avfPriceListBanner.setVisibility(View.GONE);
    }

    private void stopAnimation()
    {

        mBinding.rvPriceList.setVisibility(View.VISIBLE);
        mBinding.avfPriceListBanner.setVisibility(View.VISIBLE);
        mShimmerFrameLayout.stopShimmer();

        mShimmerFrameLayout.setVisibility(View.GONE);
    }


    private void fetchListCategories(@NonNull List<Category> listCategory)
    {
        mBinding.setCategoryList(listCategory);
    }

    private void fetchItemsCategory(int position)
    {
        mCategoryViewModel.setItemCategory(position);
    }


    /**
     * called when network is connected and check if status of data is error return load data.
     * t
     */
    private void checkConnectionsToInitCategory()
    {

        networkConnection.observe(getViewLifecycleOwner(), isConnected -> {
            // if internet not connect, :

            if (isConnected) {


                mCategoryViewModel.hasData()
                        .observe(getViewLifecycleOwner(), hasDataCategory -> {
                            if (!hasDataCategory) {
                                Timber.tag(TAG)
                                        .e("hasDataCategory () ->Connected:ERROR %s",
                                                mCategoryViewModel.getCategory()
                                                        .getValue());
                                mCategoryViewModel.refreshCategories();


                            }
                        });


                mBannersViewModel.hasData()
                        .observe(getViewLifecycleOwner(), hasDataBanner -> {
                            if (!hasDataBanner) {
                                Timber.tag(TAG)
                                        .e("hasDataBanner () -> Connected:ERROR %s",
                                                mBannersViewModel.getBanners()
                                                        .getValue());
                                mBannersViewModel.reload();

                            }
                        });
            }

        });
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
        getViewModelStore().clear();
    }

    private static class PriceOnBackPressedCallback
            extends OnBackPressedCallback
            implements SlidingPaneLayout.PanelSlideListener
    {

        private final SlidingPaneLayout mSlidingPaneLayout;

        PriceOnBackPressedCallback(@NonNull SlidingPaneLayout slidingPaneLayout)
        {
            // Set the default 'enabled' state to true only if it is slideable (i.e., the panes
            // are overlapping) and open (i.e., the detail pane is visible).
            super(slidingPaneLayout.isSlideable() && slidingPaneLayout.isOpen());
            mSlidingPaneLayout = slidingPaneLayout;
            slidingPaneLayout.addPanelSlideListener(this);
        }

        @Override
        public void handleOnBackPressed()
        {
            // Return to the list pane when the system back button is pressed.
            mSlidingPaneLayout.closePane();
        }

        @Override
        public void onPanelSlide(@NonNull View panel, float slideOffset)
        {
            //NO thing.
        }

        @Override
        public void onPanelOpened(@NonNull View panel)
        {
            // Intercept the system back button when the detail pane becomes visible.
            setEnabled(true);
        }

        @Override
        public void onPanelClosed(@NonNull View panel)
        {
            // Disable intercepting the system back button when the user returns to the
            // list pane.
            setEnabled(false);
        }
    }
}