package com.example.a5asec;


import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavBackStackEntry;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.slidingpanelayout.widget.SlidingPaneLayout;
import androidx.window.layout.WindowMetrics;
import androidx.window.layout.WindowMetricsCalculator;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.a5asec.data.remote.api.CategoryClient;
import com.example.a5asec.data.remote.api.CategoryHelper;
import com.example.a5asec.databinding.FragmentPriceAndServiceBinding;
import com.example.a5asec.ui.base.CategoryViewModelFactory;
import com.example.a5asec.ui.view.viewmodel.CategoryViewModel;


public class PriceAndServiceFragment extends Fragment
    {
    FragmentPriceAndServiceBinding mBinding;
    private CategoryViewModel mCategoryViewModel;
    private static final String TAG = "PriceAndServiceFragment";


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
        {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_price_and_service, container, false);
        setupTwoPane();
        return mBinding.getRoot();
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
                    new PriceAndServiceOnBackPressedCallback(mBinding.splPriceService));

            mBinding.splPriceService.open();
            } else
            {
             mBinding.splPriceService.setLockMode(SlidingPaneLayout.LOCK_MODE_LOCKED);

            }
        }




    class PriceAndServiceOnBackPressedCallback extends OnBackPressedCallback
            implements SlidingPaneLayout.PanelSlideListener
        {

        private final SlidingPaneLayout mSlidingPaneLayout;

        PriceAndServiceOnBackPressedCallback(@NonNull SlidingPaneLayout slidingPaneLayout)
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
    }