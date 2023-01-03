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
import androidx.navigation.NavBackStackEntry;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.a5asec.R;
import com.example.a5asec.data.model.api.Address;
import com.example.a5asec.data.remote.api.AddressClient;
import com.example.a5asec.data.remote.api.AddressHelper;
import com.example.a5asec.databinding.FragmentManageAddressBinding;
import com.example.a5asec.ui.adapters.AddressAdapter;
import com.example.a5asec.ui.base.AddressViewModelFactory;
import com.example.a5asec.ui.view.viewmodel.AddressViewModel;

import java.util.List;

import lombok.val;


public class ManageAddressFragment extends Fragment implements AddressAdapter.OnItemClickListener
    {
    private static final String TAG = "ManageAddressFragment";
    FragmentManageAddressBinding mBinding;
    AddressAdapter mAdapter;
    private AddressViewModel mAddressViewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
        {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_manage_address, container, false);

        return mBinding.getRoot();
        }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
        {
        super.onViewCreated(view, savedInstanceState);
        setupUI();
        }

    private void setupUI()
        {
        setupAdapter();
        initDataAddress();
        setupCLick();
        }


    private void setupAdapter()
        {
        mAdapter = new AddressAdapter();
        mAdapter.setOnClickListener(this);

        mBinding.rvMangeAddress.setLayoutManager(new LinearLayoutManager(getContext()));
        mBinding.rvMangeAddress.setAdapter(mAdapter);


        }

    @SuppressLint("NotifyDataSetChanged")
    private void renderList(@NonNull List<Address> address)
        {
        mAdapter.setNewData(address);

        mAdapter.notifyDataSetChanged();
        }

    private void initDataAddress()
        {
        setupViewModel();
        setupObserver();
        }

    private void setupObserver()
        {
        showLoading();
        mAddressViewModel.getResourceAddresses().observe(getViewLifecycleOwner(), addressResource ->
            {
            switch (addressResource.mStatus)
                {

                case SUCCESS -> {
                var address = addressResource.getMData();
                Log.e(TAG, "SUCCESS");
                Log.e(TAG, "suc:" + address);
                renderList(address);
                showList();

                }

                case LOADING -> {
                Log.e(TAG, "LOADING");
                showLoading();


                }
                case ERROR, NULL -> {

                Log.e(TAG, "ERROR");
                Log.e(TAG, "ERROR or null " + addressResource.getMMessage());
                emptyList();
                }

                }
            });
        }

    private void emptyList()
        {
        mBinding.tvManageAddressEmpty.setVisibility(View.VISIBLE);
        mBinding.rvMangeAddress.setVisibility(View.GONE);
        mBinding.imvManageAddressEmpty.setVisibility(View.VISIBLE);
        mBinding.cpiManageAddress.setVisibility(View.GONE);
        }

    private void showList()
        {
        mBinding.tvManageAddressEmpty.setVisibility(View.GONE);
        mBinding.rvMangeAddress.setVisibility(View.VISIBLE);
        mBinding.imvManageAddressEmpty.setVisibility(View.GONE);
        mBinding.cpiManageAddress.setVisibility(View.GONE);

        }

    private void showLoading()
        {
        mBinding.cpiManageAddress.setVisibility(View.VISIBLE);
        mBinding.tvManageAddressEmpty.setVisibility(View.GONE);
        mBinding.rvMangeAddress.setVisibility(View.GONE);
        mBinding.imvManageAddressEmpty.setVisibility(View.GONE);
        }

    private void setupViewModel()
        {
        NavController navController = NavHostFragment.findNavController(this);
        NavBackStackEntry backStackEntry = navController.getBackStackEntry(R.id.nav_home_manage_address);
        mAddressViewModel = new ViewModelProvider(backStackEntry,
                new AddressViewModelFactory(new AddressHelper(new AddressClient()))).get(AddressViewModel.class);

        }


    private void setupCLick()
        {
        mBinding.btnMangeAddress.setOnClickListener(v ->
            {
            val ADD_FRAGMENT = 1;
            Log.e(TAG, "setupCLick  position = " + v);
            String title = getString(R.string.add_address_tittle);

            var action
                    = ManageAddressFragmentDirections.actionManageAddressToNewAddress(title);
            action.setNavHomeAddressArgCheck(ADD_FRAGMENT);

            Navigation.findNavController(v).navigate(action);
            });
        }

    @Override
    public void onItemClick(View view, int position)
        {
        val INFO_ADDRESS = 2;
        fetchAddress(position);
        String title = getString(R.string.add_address_info_tittle);

        var action
                = ManageAddressFragmentDirections.actionManageAddressToNewAddress(title);
        action.setNavHomeAddressArgCheck(INFO_ADDRESS);

        Navigation.findNavController(view).navigate(action);
        }

    @Override
    public void onItemSettingClick(View view, int position)
        {
        fetchAddress(position);

        var action
                = ManageAddressFragmentDirections.actionNavHomeManageAddressToAddressSettingFragment();
        Navigation.findNavController(view).navigate(action);
        }

    @Override
    public void onItemShowAddressClick(View view, int position)
        {
        fetchAddress(position);

        var action
                = ManageAddressFragmentDirections.actionManageAddressToAddress();
        Navigation.findNavController(view).navigate(action);
        }


    private void fetchAddress(int position)
        {
        Log.e(TAG, "onItemClick, position = " + position);
        var address = mAdapter.getItemAddress(position);


        mAddressViewModel.setAddress(address);
        }

    }