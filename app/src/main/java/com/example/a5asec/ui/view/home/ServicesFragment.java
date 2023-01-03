package com.example.a5asec.ui.view.home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import com.example.a5asec.utility.Resource;

import java.util.List;

/**
 * Second fragment for the price screen, shows service items of Category price Screen.
 *
 */
public class ServicesFragment extends Fragment
    {
    private static final String TAG = "ServicesItemFragment";
    CategoryViewModel mItemsCategory;
    private FragmentServicesBinding mBinding;
    private ServicesAdapter mAdapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
        {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_services,
                container, false);

        return mBinding.getRoot();
        }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
        {
        super.onViewCreated(view, savedInstanceState);
        setupUi();

        }


    private void setupUi()
        {
        setupAdapter();
        setupViewModel();
        setupObserver();
        }

    private void setupAdapter()
        {
        var servicesListView = mBinding.lvServices;
        mAdapter = new ServicesAdapter(this);

        servicesListView.setAdapter(mAdapter);

        onCLickItem(servicesListView);

        }

    private void onCLickItem(@NonNull ListView listView)
        {
        listView.setOnItemClickListener((parent, view, position, id) ->
            {
            Log.e(TAG, "items: " + mAdapter.getItem(position));

            mItemsCategory.setItemService(Resource.success(mAdapter.getItemService(position)));
            LaundryServicesFragment laundryServicesFragment = new LaundryServicesFragment();
            laundryServicesFragment.show(getParentFragmentManager(), LaundryServicesFragment.TAG);

            });
        }

    private void setupObserver()
        {
        mItemsCategory.getItemCategory().observe(getViewLifecycleOwner(), itemsEntities ->
            {
            switch (itemsEntities.mStatus)
                {

                case SUCCESS -> {

                Log.i(TAG, "SUCCESS");
                Log.i(TAG, "suc:" + itemsEntities.getMData());
                renderList(itemsEntities);
                }

                case NULL -> {
                Log.i(TAG, "NULL");
                emptyList();

                }
                case ERROR -> {
                Log.e(TAG, "ERROR");
                emptyList();

                }
                }
            });
        }
    private void emptyList()
        {
        mBinding.lvServices.setVisibility(View.GONE);
        mBinding.imvServicesEmpty.setVisibility(View.VISIBLE);
        mBinding.tvServicesEmpty.setVisibility(View.VISIBLE);
        }

    private void renderList(Resource<List<Category.ItemsEntity>> category)
        {
        mAdapter.addServices(category.getMData());

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

    }