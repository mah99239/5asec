package com.example.a5asec.ui.view.home;

import android.graphics.Color;
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
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.a5asec.R;
import com.example.a5asec.data.model.db.ServiceAndLaundryService;
import com.example.a5asec.databinding.FragmentCartBinding;
import com.example.a5asec.ui.adapters.CartAdapter;
import com.example.a5asec.ui.view.element.SwipeToDeleteCallback;
import com.example.a5asec.ui.view.viewmodel.CartViewModel;
import com.example.a5asec.ui.view.viewmodel.CategoryViewModel;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

/**
 * Cart Fragment for the Cart screen, shows add order and mange service to adding.
 */
public class CartFragment extends Fragment implements CartAdapter.RvClickListener
    {
    private static final String TAG = "CartFragment";
    private CartViewModel mCartViewModel;
    private CategoryViewModel mItemsCategory;
    private CartAdapter mAdapter;
    private FragmentCartBinding mBinding;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
        {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_cart, container, false);

        return mBinding.getRoot();
        }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
        {
        super.onViewCreated(view, savedInstanceState);
        setUpRecyclerView();
        setupCartViewModel();
        setupObserver();
        setupItemServiceViewModel();
        }


    private void setUpRecyclerView()
        {
        var recyclerView = mBinding.rvCart;
        mAdapter = new CartAdapter();
        mAdapter.setOnclickListener(this);
        recyclerView.setAdapter(mAdapter);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        }

    @Override
    public void onItemClick(View view, int position)
        {
//problem if data update no change in cart adapter
        Log.e(TAG, "onItemClick:" + position);
        var item = mAdapter.getItemByPosition(position);
        Log.e(TAG, "item = " + item);

        int id = item.getService().getIdItemService();

        mItemsCategory.setItemService(id);
        mCartViewModel.addItemToUpdateService(item);

        LaundryServicesBottomSheet laundryServicesFragment = new LaundryServicesBottomSheet();
        laundryServicesFragment.show(getParentFragmentManager(), LaundryServicesBottomSheet.TAG_UPDATE);
         mAdapter.notifyItemChanged(position);
        }


    private void setupObserver()
        {
        Log.e(TAG, "setupObserver");
        mBinding.cpiCart.setVisibility(View.VISIBLE);
        mBinding.rvCart.setVisibility(View.GONE);

        mCartViewModel.getAllOrder().observe(getViewLifecycleOwner(), order ->
            {
            switch (order.mStatus)
                {

                case SUCCESS -> {

                Log.e(TAG, "setupObserver SUCCESS");
                Log.e(TAG, "suc:" + order.getMData());
                fetchData(order.getMData());
                setupTotalItem();

                }

                case NULL -> {
                Log.e(TAG, "NULL");
                emptyCartOrder();
                mBinding.cpiCart.setVisibility(View.GONE);

                }
                case LOADING -> {
                Log.e(TAG, "LOADING");

                }
                case ERROR -> {
                Log.e(TAG, "ERROR");
                mBinding.cpiCart.setVisibility(View.GONE);

                Log.e(TAG, " setupObserver, ERROR = " + order.getMMessage());
                emptyCartOrder();

                }
                }
            });


        }

    private void setupTotalItem()
        {
        mCartViewModel.getSumService().observe(getViewLifecycleOwner(),
                data ->
                    {
                    mBinding.cpiCart.setVisibility(View.VISIBLE);

                    switch (data.getMStatus())
                        {
                        case SUCCESS -> {
                        mAdapter.setTotalService(data.getMData());
                        mBinding.cpiCart.setVisibility(View.GONE);
                        mBinding.rvCart.setVisibility(View.VISIBLE);
                        mCartViewModel.setTotalAllOrder();
                        setupAllTotal();
                        }
                        case NULL -> {
                        Log.e(TAG, "getSumService, NULL");
                        mBinding.cpiCart.setVisibility(View.GONE);

                        }
                        case LOADING -> {
                        mBinding.rvCart.setVisibility(View.GONE);

                        mBinding.cpiCart.setVisibility(View.VISIBLE);

                        Log.e(TAG, "LOADING");
                        }
                        }
                    });
        }

    private void setupAllTotal()
        {
        mCartViewModel.getTotalAllOrder().observe(getViewLifecycleOwner(), totalAllOrder ->
            {
            switch (totalAllOrder.mStatus)
                {

                case SUCCESS -> {
                var total = getString(R.string.all_total_service) + " " + totalAllOrder.getMData()
                        + " " + getString(R.string.all_cost_label);
                mBinding.tvCartTotal.setVisibility(View.VISIBLE);

                mBinding.tvCartTotal.setText(total);
                Log.e(TAG, "totalAllOrder, SUCCESS");

                }

                case NULL -> {
                Log.e(TAG, "totalAllOrder, NULL");
                mBinding.tvCartTotal.setVisibility(View.GONE);

                }
                case ERROR -> {
                Log.e(TAG, "totalAllOrder, ERROR");
                mBinding.tvCartTotal.setVisibility(View.GONE);

                }
                }

            });
        }

    private void fetchData(List<ServiceAndLaundryService> data)
        {
        mAdapter.setData(data);
        }


    @Override
    public void onItemLongClick(int position)
        {
        final ServiceAndLaundryService item = mAdapter.getItemByPosition(position);
        mAdapter.removeItem(position);
        item.getService().setFlag(1);
        mCartViewModel.removeItemService(item.getService());

        Snackbar snackbar = Snackbar.make(mBinding.getRoot(),
                "Item cart was removed from the list.", BaseTransientBottomBar.LENGTH_LONG);
        snackbar.setAction("UNDO", view ->
            {

            mAdapter.restoreItem(item, position);
            mBinding.rvCart.scrollToPosition(position);
            item.getService().setFlag(0);
            mCartViewModel.removeItemService(item.getService());

            });

        snackbar.setActionTextColor(Color.YELLOW);
        snackbar.show();
        }

    private void setupItemServiceViewModel()
        {
        NavController navController = NavHostFragment.findNavController(this);
        NavBackStackEntry backStackEntry = navController.getBackStackEntry(R.id.nav_home_priceList);
        mItemsCategory = new ViewModelProvider(backStackEntry)

                .get(CategoryViewModel.class);
        }


    private void enableSwipeToDeleteAndUndo()
        {
        SwipeToDeleteCallback swipeToDeleteCallback = new SwipeToDeleteCallback(requireContext())
            {
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction)
                {
                final int position = viewHolder.getAdapterPosition();
                final ServiceAndLaundryService item = mAdapter.getItemByPosition(position);
                mAdapter.removeItem(position);
                item.getService().setFlag(1);
                mCartViewModel.removeItemService(item.getService());

                Snackbar snackbar = Snackbar
                        .make(mBinding.getRoot(), "Item was removed from the list.", BaseTransientBottomBar.LENGTH_LONG);
                snackbar.setAction("UNDO", view ->
                    {

                    mAdapter.restoreItem(item, position);
                    mBinding.rvCart.scrollToPosition(position);
                    item.getService().setFlag(0);
                    mCartViewModel.removeItemService(item.getService());
                    });

                snackbar.setActionTextColor(Color.YELLOW);
                snackbar.show();
                }
            };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(swipeToDeleteCallback);
        itemTouchHelper.attachToRecyclerView(mBinding.rvCart);

        }

    private void setupCartViewModel()
        {
        mCartViewModel = new ViewModelProvider(requireActivity())
                .get(CartViewModel.class);
        }

    private void emptyCartOrder()
        {
        mBinding.rvCart.setVisibility(View.GONE);
        mBinding.tvCartTotal.setVisibility(View.GONE);

        mBinding.tvCartEmpty.setVisibility(View.VISIBLE);
        mBinding.imvCartEmpty.setVisibility(View.VISIBLE);
        }
    }