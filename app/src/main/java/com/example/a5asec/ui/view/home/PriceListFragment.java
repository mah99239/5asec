package com.example.a5asec.ui.view.home;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ProcessLifecycleOwner;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavBackStackEntry;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.slidingpanelayout.widget.SlidingPaneLayout;
import androidx.window.layout.WindowMetrics;
import androidx.window.layout.WindowMetricsCalculator;

import com.bumptech.glide.Glide;
import com.example.a5asec.R;
import com.example.a5asec.data.model.api.Banners;
import com.example.a5asec.data.model.api.Category;
import com.example.a5asec.data.remote.api.BannersClient;
import com.example.a5asec.data.remote.api.BannersHelper;
import com.example.a5asec.data.remote.api.CategoryClient;
import com.example.a5asec.data.remote.api.CategoryHelper;
import com.example.a5asec.databinding.FragmentPriceListBinding;
import com.example.a5asec.ui.adapters.BannersAdapter;
import com.example.a5asec.ui.adapters.PriceAdapter;
import com.example.a5asec.ui.base.BannersViewModelFactory;
import com.example.a5asec.ui.base.CategoryViewModelFactory;
import com.example.a5asec.ui.view.viewmodel.BannersViewModel;
import com.example.a5asec.ui.view.viewmodel.CategoryViewModel;
import com.example.a5asec.utility.AdaptiveUtils;
import com.example.a5asec.utility.NetworkConnection;
import com.example.a5asec.utility.Resource;
import com.example.a5asec.utility.Status;
import com.facebook.shimmer.Shimmer;
import com.facebook.shimmer.ShimmerFrameLayout;

import java.util.List;
import java.util.Objects;

import lombok.val;
import timber.log.Timber;


/**
 * Main Fragment for the price screen
 */
public class PriceListFragment extends Fragment implements LifecycleObserver
    {
    private static final String TAG = "PriceListFragment";

    private FragmentPriceListBinding mBinding;
    private PriceAdapter mPriceAdapter;
    private BannersAdapter mBannersAdapter;
    private CategoryViewModel mCategoryViewModel;
    private BannersViewModel mBannersViewModel;
    private ShimmerFrameLayout mShimmerFrameLayout;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
        {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_price_list, container, false);

        return mBinding.getRoot();

        }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
        {
        super.onViewCreated(view, savedInstanceState);
        ProcessLifecycleOwner.get().getLifecycle().addObserver(this);
        setupShimmerAnimation();
        setupRefreshView();
        setupViewModel();
        setupBannersAdapter();
        setupObserverBanners();
        setupTwoPane();

        }

    private void setupTwoPane()
        {


        boolean isCompact = AdaptiveUtils.compactScreen(requireActivity());
        Timber.tag(TAG).e("setupTwoPane:  isCompact = %s", isCompact);
        setupUi(isCompact);
        if (!isCompact)
            {

            if (mBinding.splPrice != null)
                {

                requireActivity().getOnBackPressedDispatcher().addCallback(
                        getViewLifecycleOwner(),
                        new PriceOnBackPressedCallback(mBinding.splPrice));

                mBinding.splPrice.open();
                mBinding.splPrice.setLockMode(SlidingPaneLayout.LOCK_MODE_LOCKED);

                }
            } else
            {

            if (mBinding.splPrice != null)
                mBinding.splPrice.setLockMode(SlidingPaneLayout.LOCK_MODE_LOCKED);


            }
        }


    private void setupUi(boolean isCompact)
        {


        if (!isCompact)
            {
            setupCategoryAdapterWithTwoPane();
            setupObserverCategoryWithTwoPane();
            } else
            {
            setupCategoryAdapter();

            setupObserverCategory();
            checkConnectionsToInitCategory();

            }
        }

    private void setupShimmerAnimation()
        {
        var shimmer = new Shimmer.AlphaHighlightBuilder()
                .setDuration(1000L) // how long the shimmering animation takes to do one full sweep
                .setRepeatMode(ValueAnimator.REVERSE)
                //  .setAutoStart(true)
                .build();


        mShimmerFrameLayout = mBinding.shPrice;
        mShimmerFrameLayout.setShimmer(shimmer);

        }

    private void setupRefreshView()
        {
        mBinding.swipePriceListRoot.setOnRefreshListener(() ->
            {
            reloadDataUI();
            mBinding.swipePriceListRoot.setRefreshing(false);
            });

        }


    private void setupBannersAdapter()
        {
        mBannersAdapter = new BannersAdapter(getParentFragment());

        mBinding.avfPriceListBanner.setAdapter(mBannersAdapter);
        mBinding.avfPriceListBanner.setFlipInterval(4000);
        mBinding.avfPriceListBanner.startFlipping();
        mBinding.avfPriceListBanner.setHorizontalScrollBarEnabled(true);
        }

    private void setupCategoryAdapter()
        {

        mPriceAdapter = new PriceAdapter(this);
        mPriceAdapter.setClickListener(this::onItemClick);

        StaggeredGridLayoutManager gridLayoutManager =
                new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
        mBinding.rvPriceList.setLayoutManager(gridLayoutManager);
        mBinding.rvPriceList.setAdapter(mPriceAdapter);

        }

    public void onItemClick(View view, int position)
        {

        fetchItemsCategory(position);
        var action
                = PriceListFragmentDirections.actionPriceListFragmentToServicesItemFragment();
        Navigation.findNavController(view).navigate(action);


        }

    private void setupCategoryAdapterWithTwoPane()
        {
        var recyclerView = mBinding.rvPriceList;


        mPriceAdapter = new PriceAdapter(this);
        mPriceAdapter.setClickListener(this::onItemClickWithTwoPane);

        LinearLayoutManager linearLayoutManager =
                new LinearLayoutManager(recyclerView.getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(mPriceAdapter);

        }


    public void onItemClickWithTwoPane(View view, int position)
        {

        fetchItemsCategory(position);
        }



    private void setupObserverBanners()
        {
        mBannersViewModel.getBanners().observe(getViewLifecycleOwner(), bannersObserve ->
            {

            if (Objects.requireNonNull(bannersObserve.mStatus) == Status.SUCCESS)
                {
                Timber.tag(TAG).e("setupObserverBanners:" + "SUCCESS");
                renderListBanners(bannersObserve);
                } else if (bannersObserve.mStatus == Status.ERROR)
                {
                startAnimation();
                Timber.tag(TAG).e(bannersObserve.mMessage);
                }
            });
        }

    private void setupObserverCategory()
        {


        mCategoryViewModel.getCategory().observe(getViewLifecycleOwner(), categoryObserve ->
            {
            val tagObserverCategory = "setupObserverCategory";
            switch (categoryObserve.mStatus)
                {

                case SUCCESS ->
                    {
                    Timber.tag(TAG).e(tagObserverCategory + "SUCCESS, = " + categoryObserve.getMData());
                    stopAnimation();
                    renderListCategory(categoryObserve);

                    }

                case LOADING ->
                    {
                    Timber.tag(TAG).e("%sLOADING", tagObserverCategory);
                    startAnimation();

                    }
                case ERROR ->
                    {


                    Timber.tag(TAG).e("%sERROR", tagObserverCategory);
                    Timber.tag(TAG  ).e("%s%s", tagObserverCategory, categoryObserve.mMessage);
                    startAnimation();
                    }

                case NULL ->
                    {
                    startAnimation();
                    Timber.tag(TAG).e("%sNULL", tagObserverCategory);

                    }
                }
            });

        }

    private void setupObserverCategoryWithTwoPane()
        {

        mCategoryViewModel.getCategory().observe(getViewLifecycleOwner(), categoryObserve ->
            {
            switch (categoryObserve.mStatus)
                {

                case SUCCESS ->
                    {
                    Timber.tag(TAG).e("setupObserverCategoryWithTwoPane, SUCCESS = %s",
                            (categoryObserve.getMData()));

                    renderListCategory(categoryObserve);
                    mCategoryViewModel.setItemCategoryWithTwoPane();

                    stopAnimation();
                    }

                case LOADING ->
                    {
                    Timber.tag(TAG).e("setupObserverCategoryWithTwoPane, LOADING");
                    startAnimation();

                    }
                case ERROR ->
                    {
                    Timber.tag(TAG).e("setupObserverCategory:ERROR %s", categoryObserve.mMessage);
                    startAnimation();
                    }
                case NULL ->
                    {
                    startAnimation();
                    Timber.tag(TAG ).e( "setupObserverCategory:NULL%s",categoryObserve.mMessage);

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

    @SuppressLint("NotifyDataSetChanged")
    private void renderListBanners(Resource<List<Banners>> banners)
        {
        mBannersAdapter.addBanners(banners.getMData());

        mPriceAdapter.notifyDataSetChanged();

        }

    @SuppressLint("NotifyDataSetChanged")
    private void renderListCategory(@NonNull Resource<List<Category>> category)
        {
        mPriceAdapter.addCategory(category.getMData());
        mPriceAdapter.notifyDataSetChanged();
        }

    private void fetchItemsCategory(int position)
        {
        mCategoryViewModel.setItemCategory(position);
        }

    private void setupViewModel()
        {
        setupCategoryViewModel();
        setupBannersViewModel();
        }

    private void setupCategoryViewModel()
        {
        NavController navController = NavHostFragment.findNavController(this);
        NavBackStackEntry backStackEntry = navController.getBackStackEntry(R.id.nav_home_priceList);

        mCategoryViewModel = new ViewModelProvider(backStackEntry,
                new CategoryViewModelFactory(new CategoryHelper(new CategoryClient())))
                .get(CategoryViewModel.class);
        }

    private void setupBannersViewModel()
        {
        mBannersViewModel = new ViewModelProvider(this,
                new BannersViewModelFactory(new BannersHelper(new BannersClient())))
                .get(BannersViewModel.class);
        }

    /**
     * called when network is connected and check if status of data is error return load data.
     */
    private void checkConnectionsToInitCategory()
        {
        NetworkConnection networkConnection = new NetworkConnection(requireActivity());
        networkConnection.observe(getViewLifecycleOwner(), isConnected ->
            {
            // if internet not connect,
            if (isConnected)
                {


                mCategoryViewModel.hasData().observe(getViewLifecycleOwner(), hasData ->
                    {
                    if(!hasData)
                        {
                        Timber.tag(TAG).e("Connected:ERROR %s", mCategoryViewModel.getCategory().getValue());
                        reloadDataUI();
                        }
                    });
                  /*   if (mCategoryViewModel.getCategory().getValue().getMStatus().equals(Status.ERROR)
                            || mBannersViewModel.getBanners().getValue().getMStatus().equals(Status.ERROR))
                        {
                        Timber.tag(TAG).e("Connected:ERROR %s", mCategoryViewModel.getCategory().getValue());
                        reloadDataUI();
                        }

                    } catch (NullPointerException e)
                    {
                    Timber.tag(TAG).e("Connected:NullPointerException:%s", e.getMessage());
                    } */


                }

            });
        }

    private void reloadDataUI()
        {
        getViewModelStore().clear();
        mCategoryViewModel.reloadCategory();
        mBannersViewModel.reload();
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
        //  mShimmerFrameLayout = null;
        }

    static class PriceOnBackPressedCallback extends OnBackPressedCallback
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