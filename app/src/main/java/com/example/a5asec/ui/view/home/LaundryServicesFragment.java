package com.example.a5asec.ui.view.home;

import android.animation.ValueAnimator;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.view.ActionMode;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ObservableInt;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavBackStackEntry;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.selection.SelectionPredicates;
import androidx.recyclerview.selection.SelectionTracker;
import androidx.recyclerview.selection.StorageStrategy;
import androidx.recyclerview.widget.GridLayoutManager;

import com.bumptech.glide.Glide;
import com.example.a5asec.R;
import com.example.a5asec.data.model.api.Category;
import com.example.a5asec.data.model.db.ItemService;
import com.example.a5asec.data.model.db.LaundryService;
import com.example.a5asec.data.model.db.ServiceAndLaundryService;
import com.example.a5asec.databinding.FragmentLaundryServicesBinding;
import com.example.a5asec.ui.adapters.LaundryServicesAdapter;
import com.example.a5asec.ui.view.viewmodel.CartViewModel;
import com.example.a5asec.ui.view.viewmodel.CategoryViewModel;
import com.facebook.shimmer.Shimmer;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;
import java.util.List;


public class LaundryServicesFragment extends BottomSheetDialogFragment implements ActionMode.Callback
    {
    public static final String TAG = "LaundryServicesFragment_INSERT";
    public static final String TAG_UPDATE = "LaundryServicesFragment_UPDATA";
    public final ObservableInt mCountObservable = new ObservableInt(1);
    private CategoryViewModel mItemsCategory;
    private LaundryServicesAdapter mAdapter;
    private FragmentLaundryServicesBinding mBinding;
    private SelectionTracker<Long> selectionTracker;
    private ActionMode actionMode;
    private CartViewModel mCartViewModel;
    private ShimmerFrameLayout mShimmerFrameLayout;
    private ServiceAndLaundryService mItem;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
        {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_laundry_services, container, false);
        View view = mBinding.getRoot();
        // Set up BottomSheetDialog
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(requireContext());

        mBinding.setLifecycleOwner(this);
        mBinding.iLaundryServiceLayout.setLifecycleOwner(this);
        mBinding.iLaundryServiceLayout.setFragment(this);
        bottomSheetDialog.setDismissWithAnimation(true);
        bottomSheetDialog.getBehavior().setState(BottomSheetBehavior.STATE_EXPANDED);

        Log.e(TAG, "my tag = " + getTag());
        return view;
        }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
        {
        super.onViewCreated(view, savedInstanceState);

        setupShimmerAnimation();
        setUpRecyclerView();
        setupViewModel();
        setupObserver();
        setupCartViewModel();

        if (getTag() == TAG) //if insert cart.
            {
            setupClickedInsertCartButton();

            } else // update cart.
            {
            var labelButton = getString(R.string.laundry_service_content_cart_updata_button);
            mBinding.iLaundryServiceLayout.btnLaundryServicesCart.setText(labelButton);
            renderUpdateOrder();
            setupClickedUpdateCartButton();

            }

        }

    private void setupShimmerAnimation()
        {
        var shimmer = new Shimmer.AlphaHighlightBuilder()
                .setDuration(2000L) // how long the shimmering animation takes to do one full sweep

                .setRepeatMode(ValueAnimator.REVERSE)
                .setAutoStart(true)
                .build();

        mShimmerFrameLayout = mBinding.shLaundryService;
        mShimmerFrameLayout.setShimmer(shimmer);

        }

    private void startAnimation()
        {
        mShimmerFrameLayout.startShimmer();
        mShimmerFrameLayout.setVisibility(View.VISIBLE);
        mBinding.iLaundryServiceLayout.clLaundryServicesContentContainer.setVisibility(View.GONE);
        }

    private void stopAnimation()
        {
        mBinding.iLaundryServiceLayout.clLaundryServicesContentContainer.setVisibility(View.VISIBLE);

        mShimmerFrameLayout.stopShimmer();
        mShimmerFrameLayout.setVisibility(View.GONE);
        }

    private void setUpRecyclerView()
        {
        var recyclerView = mBinding.iLaundryServiceLayout.rvLaundryServices;
        mAdapter = new LaundryServicesAdapter(this, Glide.with(this));
        recyclerView.setAdapter(mAdapter);

        setupSelectionTracker();
        mAdapter.setSelectionTracker(selectionTracker);

        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));


        }

    private void setupClickedInsertCartButton()
        {

        mBinding.iLaundryServiceLayout.btnLaundryServicesCart.setOnClickListener(v ->
            {


            List<LaundryService> laundryServices = new ArrayList<>();
            int selectionSize = mAdapter.getCheckedChipId().size();

            if (selectionSize > 0)
                {

                mAdapter.getCheckedChipId().forEach(i ->
                    {

                    int id = i.getId();
                    int cost = i.getCost();
                    String url = i.getLaundryService().getIconUrl();
                    String nameEN = i.getLaundryService().getNameEn();
                    String nameAr = i.getLaundryService().getNameAr();
                    laundryServices.add(new LaundryService(id, cost, url, nameEN, nameAr));
                    Log.e(TAG, "selection:" + i);

                    });
                }


            addOrder(addItemServiceToCart(), laundryServices);
            dismiss();
            });
        }

    private void setupClickedUpdateCartButton()
        {

        mBinding.iLaundryServiceLayout.btnLaundryServicesCart.setOnClickListener(v ->
            {
            int oldCount = mItem.getService().getCount();
            int newCount = mCountObservable.get();

            Log.e(TAG, "updateServices, oldCount =  " + oldCount + ", newCount = " + newCount);

            if (oldCount != newCount)
                {
                mCartViewModel.updateService(updateItemService());
                } else             Log.e(TAG,"NOT update service");


            List<LaundryService> laundryServices = new ArrayList<>();
            mAdapter.getCheckedChipId().forEach(i ->
                {

                int id = i.getId();
                int cost = i.getCost();
                String url = i.getLaundryService().getIconUrl();
                String nameEN = i.getLaundryService().getNameEn();
                String nameAr = i.getLaundryService().getNameAr();
                laundryServices.add(new LaundryService(id, cost, url, nameEN, nameAr));
                Log.e(TAG, "update:" + i);

                });

                if(!mItem.getLaundryServices().equals(laundryServices) &&
                        !mItem.getLaundryServices().containsAll(laundryServices))
                    {
                    var itemLaundryService = mItem.getService().getId();

                    mCartViewModel.removeLaundryService(itemLaundryService);
                    updateOrder(laundryServices);
                    Log.e(TAG," update laundry service");

                    }
            else{
            Log.e(TAG,"NOT update laundry service");
                }



            dismiss();
            });
        }

    @NonNull
    private ItemService addItemServiceToCart()
        {
        var item = mItemsCategory.getItemService().getValue().getMData();
        String nameEn = item.getNameEn();
        String nameAr = item.getNameAr();
        int costItemService = item.getCost();
        int idItemService = item.getId();
        int count = mCountObservable.get();

        return new ItemService(nameEn, nameAr, costItemService, idItemService, count);
        }

    @NonNull
    private ItemService updateItemService()
        {
        var itemService = mItem.getService();
        int count = mCountObservable.get();

        itemService.setCount(count);

        return itemService;
        }

    private void renderUpdateOrder()
        {
        mCartViewModel.getItemToUpdateService().observe(getViewLifecycleOwner(), serviceAndLaundryService ->
            {

            mItem = serviceAndLaundryService;
            mCountObservable.set(mItem.getService().getCount());
            checkedItems();

            Log.e(TAG, "count = " + serviceAndLaundryService.getService().getCount());
            }
        );

        }

    private void addOrder(ItemService service, List<LaundryService> laundryServices)
        {


        mCartViewModel.addService(service, laundryServices);


        }

    private void updateOrder(List<LaundryService> laundryServices)
        {

        int idService = mItem.getService().getId();

        laundryServices.forEach(laundryService ->
            {
            laundryService.setIdService(idService);

            });

        mCartViewModel.updateLaundryService(laundryServices);
        mCartViewModel.refreshCart();
        }

    private void setupSelectionTracker()
        {
        selectionTracker = new SelectionTracker.Builder<>(
                "card_selection",
                mBinding.iLaundryServiceLayout.rvLaundryServices,
                new LaundryServicesAdapter.ItemsKeyProvider(mAdapter)
                , new LaundryServicesAdapter.ItemsDetailsLookup(mBinding.iLaundryServiceLayout.rvLaundryServices),
                StorageStrategy.createLongStorage())
                .withSelectionPredicate(SelectionPredicates.createSelectAnything())
                .build();


      /*   selectionTracker.addObserver(//show selected with action mode.
                new SelectionTracker.SelectionObserver<>()
                    {
                    @Override
                    public void onSelectionChanged()
                        {
                        if (selectionTracker.getSelection().size() > 0)
                            {
                            if (actionMode == null)
                                {
                                actionMode = ((AppCompatActivity) requireActivity()).startSupportActionMode(LaundryServicesFragment.this);
                                }
                            actionMode.setTitle(String.valueOf(selectionTracker.getSelection().size()));


                            } else if (actionMode != null)
                            {
                            actionMode.finish();
                            }
                        }
                    }); */
        }

    private void setupObserver()
        {

        mItemsCategory.getItemService().observe(getViewLifecycleOwner(), itemsEntities ->
            {
            switch (itemsEntities.mStatus)
                {

                case SUCCESS -> {

                Log.e(TAG, "SUCCESS" + itemsEntities.getMData());
                renderList(itemsEntities.getMData());
                stopAnimation();
                }
                case LOADING -> {
                startAnimation();

                Log.e(TAG, "LOADING");

                }

                case NULL -> {
                Log.e(TAG, "NULL");
                startAnimation();

                }
                case ERROR -> {
                Log.e(TAG, "ERROR");
                startAnimation();

                }


                }
            });
        }

    public void plusCount()
        {
        mCountObservable.set(mCountObservable.get() + 1);
        }

    public void minusCount()
        {
        if (mCountObservable.get() > 1) mCountObservable.set(mCountObservable.get() - 1);

        }

    private void renderList(Category.ItemsEntity item)
        {
        //mAdapter.addCategory(category);
        mAdapter.addItemService(item);


        }



    private void checkedItems()
        {
        var checkedItem = mCartViewModel.getItemLaundryServiceChecked().getValue();
        Log.e(TAG, String.valueOf(checkedItem));
        mAdapter.setCheckedChipId(checkedItem);
        }

    private void setupViewModel()
        {
        NavController navController = NavHostFragment.findNavController(this);
        NavBackStackEntry backStackEntry = navController.getBackStackEntry(R.id.nav_home_priceList);
        Log.e(TAG, "setupItemServiceViewModel, id = " + backStackEntry.getId());

        mItemsCategory = new ViewModelProvider(backStackEntry)
                .get(CategoryViewModel.class);
        }

    private void setupCartViewModel()
        {
        mCartViewModel = new ViewModelProvider(requireActivity())
                .get(CartViewModel.class);
        }


    @Override
    public boolean onCreateActionMode(ActionMode actionMode, Menu menu)
        {
        return true;
        }

    @Override
    public boolean onPrepareActionMode(ActionMode actionMode, Menu menu)
        {
        return false;
        }

    @Override
    public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem)
        {
        return false;
        }

    @Override
    public void onDestroyActionMode(ActionMode actionMode)
        {
        selectionTracker.clearSelection();
        this.actionMode = null;
        }

    @Override
    public void onPause()
        {
        mShimmerFrameLayout.stopShimmer();
        super.onPause();
        }

    @Override
    public void onDetach()
        {
        super.onDetach();
        selectionTracker.clearSelection();

        }
    }