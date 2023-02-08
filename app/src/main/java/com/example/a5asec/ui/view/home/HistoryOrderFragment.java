package com.example.a5asec.ui.view.home;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.a5asec.R;
import com.example.a5asec.data.model.api.Order;
import com.example.a5asec.data.remote.api.OrderClient;
import com.example.a5asec.data.remote.api.OrderHelper;
import com.example.a5asec.databinding.FragmentHistoryOrderBinding;
import com.example.a5asec.ui.adapters.OrderHistoryAdapter;
import com.example.a5asec.ui.base.OrderViewModelFactory;
import com.example.a5asec.ui.view.viewmodel.OrderViewModel;
import com.example.a5asec.utility.Resource;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

/* renamed from: com.example.a5asec.ui.view.home.HistoryOrderFragment */
/* loaded from: classes3.dex */
public class HistoryOrderFragment extends Fragment implements OrderHistoryAdapter.OnItemClickListener
    {
    private static final String TAG = "HistoryOrderFragment";
    private FragmentHistoryOrderBinding mBinding;
    private OrderHistoryAdapter mAdapater;
    private OrderViewModel mOrderViewModel;


    @Override // androidx.fragment.app.Fragment
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
        {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_history_order, container, false);

        return mBinding.getRoot();
        }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
        {
        super.onViewCreated(view, savedInstanceState);
        setupUi();
        Snackbar.make(requireActivity().getCurrentFocus(), "hello", Snackbar.LENGTH_INDEFINITE).show();

        }

    private void setupUi()
        {

        setupAdapter();
        setupViewModel();
        setupObserver();

        }

    private void setupAdapter()
        {
        var recyclerView = mBinding.rvOrderHistory;


        mAdapater = new OrderHistoryAdapter(this);
        // mAdapater.setClickListener(this);

        LinearLayoutManager gridLayoutManager =
                new LinearLayoutManager(requireContext());
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(mAdapater);
        }


    private void setupObserver()
        {
        loadingData();

        mOrderViewModel.getOrderHistory().observe(getViewLifecycleOwner(), categoryObserve ->
            {

            switch (categoryObserve.mStatus)
                {

                case SUCCESS ->
                    {
                    Log.e(TAG, "setupObserver" + "SUCCESS, = " + categoryObserve.getMData());
                    showData();
                    renderlistHistoryOrder(categoryObserve);
                    }
                case NULL ->
                    {
                    Log.e(TAG, "setupObserver" + "NULL, = ");
                    showData();
                    emptyData();

                    }

                case LOADING ->
                    {
                    Log.e(TAG, "setupObserver" + "LOADING");
                    loadingData();

                    }
                case ERROR ->
                    {

                    Log.e(TAG, "setupObserver" + "ERROR");
                    Log.e(TAG + "setupObserver:", categoryObserve.mMessage);
                    }
                }
            });

        }

    private void loadingData()
        {
        mBinding.cpiOrderHistory.setVisibility(View.VISIBLE);
        mBinding.rvOrderHistory.setVisibility(View.GONE);
        }

    private void showData()
        {
        mBinding.cpiOrderHistory.setVisibility(View.GONE);
        mBinding.rvOrderHistory.setVisibility(View.VISIBLE);
        }
    private void emptyData()
        {
        mBinding.tvOrderHistoryEmpty.setVisibility(View.VISIBLE);
        mBinding.imvOrderHistoryEmpty.setVisibility(View.VISIBLE);

        }
    @SuppressLint("NotifyDataSetChanged")
    private void renderlistHistoryOrder(@NonNull Resource<List<Order>> category)
        {
        mAdapater.setData(category.getMData());
        mAdapater.notifyDataSetChanged();
        }


    private void setupViewModel()
        {
       // NavController navController = NavHostFragment.findNavController(this);
      //  NavBackStackEntry backStackEntry = navController.getBackStackEntry(R.id.nav_home_history_order);

        mOrderViewModel = new ViewModelProvider(this,
                new OrderViewModelFactory(new OrderHelper(new OrderClient())))
                .get(OrderViewModel.class);
        }


    @Override
    public void onItemClick(View view, int position)
        {
        fetchItemHistoryOrder(position);
        var action
                = HistoryOrderFragmentDirections.actionNavHomeHistoryOrderToNavHomeInfoOrderPager();
        Navigation.findNavController(view).navigate(action);

        }

    private void fetchItemHistoryOrder(int position)
        {
        mOrderViewModel.setItemHistoryOrder(Resource.success(mAdapater.getHistoryOrderItem(position)));
        }
    }