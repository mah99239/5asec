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
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.ProcessLifecycleOwner;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavBackStackEntry;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.slidingpanelayout.widget.SlidingPaneLayout;
import androidx.window.layout.WindowMetrics;
import androidx.window.layout.WindowMetricsCalculator;

import com.example.a5asec.PriceAndServiceFragment;
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
import com.example.a5asec.utility.NetworkConnection;
import com.example.a5asec.utility.Resource;
import com.example.a5asec.utility.Status;
import com.facebook.shimmer.Shimmer;
import com.facebook.shimmer.ShimmerFrameLayout;

import java.util.List;


/**
 * Main Fragment for the price screen
 */
public class PriceListFragment extends Fragment implements PriceAdapter.ItemClickListener, LifecycleObserver
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
        setupUi();
        setupTwoPane();

        }
    private void setupTwoPane()
        {
        WindowMetrics metrics = WindowMetricsCalculator.getOrCreate()
                .computeCurrentWindowMetrics(requireActivity());

        float widthDp = metrics.getBounds().width() /
                getResources().getDisplayMetrics().density;
        Log.e(TAG, "setupTwoPane:  widthDP = " + widthDp);
        if (widthDp >= 600f)
            {  requireActivity().getOnBackPressedDispatcher().addCallback(
                    getViewLifecycleOwner(),
                    new PriceOnBackPressedCallback(mBinding.splPrice));
           // mCategoryViewModel.setItemCategoryWithTwoPane();
            mBinding.splPrice.open();
            } else
            {
          //  mBinding.splPrice.setLockMode(SlidingPaneLayout.LOCK_MODE_LOCKED);

            }
        }

    class PriceOnBackPressedCallback extends OnBackPressedCallback
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

    @Override
    public void onPause()
        {
        mShimmerFrameLayout.stopShimmer();
        super.onPause();
        }



    private void setupUi()
        {
        setupShimmerAnimation();
        setupRefreshView();
        setupAdapter();
        initCategory();
        checkConnectionsToInitCategory();


        }

    private void setupShimmerAnimation()
        {
        var shimmer = new Shimmer.AlphaHighlightBuilder()
                .setDuration(4000L) // how long the shimmering animation takes to do one full sweep

                .setRepeatMode(ValueAnimator.REVERSE)
                //  .setAutoStart(true)
                .build();


        mShimmerFrameLayout = mBinding.shPrice;
        mShimmerFrameLayout.setShimmer(shimmer);

        }

    private void setupRefreshView()
        {
        mBinding.swipeContainer.setOnRefreshListener(() ->
            {
            reloadDataUI();
            mBinding.swipeContainer.setRefreshing(false);
            });

        }



    private void setupAdapter()
        {
        setupBannersAdapter();
        setupCategoryAdapter();

        }

    private void setupBannersAdapter()
        {
        mBannersAdapter = new BannersAdapter(getParentFragment());
        var flipper = mBinding.avfPriceListBanner;


        flipper.setAdapter(mBannersAdapter);
        flipper.setFlipInterval(3000);
        flipper.startFlipping();
        flipper.setHorizontalScrollBarEnabled(true);
        }

    private void setupCategoryAdapter()
        {
        var recyclerView = mBinding.rvPriceList;



      //  recyclerView.setItemAnimator(new Slide());

        mPriceAdapter = new PriceAdapter(this);
        mPriceAdapter.setClickListener(this);

        StaggeredGridLayoutManager gridLayoutManager =
                new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(mPriceAdapter);

        }


    @Override
    public void onItemClick(View view, int position)
        {

        fetchItemsCategory(position);
        var action
                = PriceListFragmentDirections.actionPriceListFragmentToServicesItemFragment();
        Navigation.findNavController(view).navigate(action);


        }



    private void initCategory()
        {
        setupViewModel();
        setupObserver();
        }


    private void setupObserver()
        {
        setupObserverBanners();
        setupObserverCategory();

        }

    private void setupObserverBanners()
        {
        startAnimation();
        mBannersViewModel.getBanners().observe(getViewLifecycleOwner(), bannersObserve ->
            {

            switch (bannersObserve.mStatus)
                {

                case SUCCESS -> {
                Log.e(TAG, "setupObserverBanners:" + "SUCCESS");
                renderListBanners(bannersObserve);
                stopAnimation();

                }
                case LOADING -> Log.e(TAG, "setupObserverBanners : " + "LOADING");

                case ERROR -> {
                Log.e(TAG, "setupObserverBanners : " + "ERROR");
                Log.e(TAG, bannersObserve.mMessage);


                }
                }
            });
        }

    private void setupObserverCategory()
        {

        startAnimation();

        mCategoryViewModel.getCategory().observe(getViewLifecycleOwner(), categoryObserve ->
            {

            switch (categoryObserve.mStatus)
                {

                case SUCCESS -> {
                Log.e(TAG, "Category" + "SUCCESS");
                Log.e(TAG, "Category" + "SUCCESS, = "  + categoryObserve.getMData());
                stopAnimation();
                renderListCategory(categoryObserve);

                }

                case LOADING -> {
                stopAnimation();
                Log.e(TAG, "Category" + "LOADING");
                }
                case ERROR -> {


                Log.e(TAG, "setupObserverCategory" + "ERROR");
                Log.e(TAG + "setupObserverCategory:", categoryObserve.mMessage);
                startAnimation();
                }
                }
            });

        }

    private void startAnimation()
        {
        mShimmerFrameLayout.startShimmer();
        mShimmerFrameLayout.setVisibility(View.VISIBLE);
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
        mCategoryViewModel.setItemCategory(Resource.success(mPriceAdapter.getItemEntity(position)));
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

        mCategoryViewModel = new ViewModelProvider(backStackEntry ,
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

                try
                    {

                    if (mCategoryViewModel.getCategory().getValue().getMStatus().equals(Status.ERROR)
                            || mBannersViewModel.getBanners().getValue().getMStatus().equals(Status.ERROR))
                        {
                        Log.e(TAG, "Connected:ERROR " + mCategoryViewModel.getCategory().getValue());
                        reloadDataUI();
                        }

                    } catch (NullPointerException e)
                    {
                    Log.e(TAG, "Connected:NullPointerException:" +e.getMessage());
                    }


                }

            });
        }
    private void reloadDataUI()
        {
        getViewModelStore().clear();
        mCategoryViewModel.reloadCategory();
        mBannersViewModel.reload();
        }

    }