package com.example.a5asec.ui.view.home;

import android.animation.ValueAnimator;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.view.ActionMode;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
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
import com.example.a5asec.utility.AdaptiveUtils;
import com.facebook.shimmer.Shimmer;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;


public class LaundryServicesBottomSheet extends BottomSheetDialogFragment implements ActionMode.Callback
    {
    public static final String TAG = "LaundryServicesFragment-INSERT";
    public static final String TAG_UPDATE = "LaundryServicesFragment-UPDATE";
    private CategoryViewModel mItemsCategory;
    private CartViewModel mCartViewModel;
    private LaundryServicesAdapter mAdapter;
    private FragmentLaundryServicesBinding mBinding;
    private SelectionTracker<Long> selectionTracker;
    private ActionMode actionMode;
    private ServiceAndLaundryService mItem;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
        {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_laundry_services, container, false);
        //Complete: move bottom sheet to dialog and change when use tablet
        Timber.i("tag = %s", getTag());
        return mBinding.getRoot();
        }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState)
        {
        BottomSheetDialog dialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);

        dialog.setOnShowListener((DialogInterface.OnShowListener) dialog1 ->
            {
            BottomSheetDialog d = (BottomSheetDialog) dialog1;
            FrameLayout bottomSheet = (FrameLayout) d.findViewById(com.google.android.material.R.id.design_bottom_sheet);
            if (bottomSheet != null)
                {
                BottomSheetBehavior.from(bottomSheet).setState(BottomSheetBehavior.STATE_EXPANDED);
                BottomSheetBehavior.from(bottomSheet).setSkipCollapsed(true);
                BottomSheetBehavior.from(bottomSheet).setFitToContents(true);
                BottomSheetBehavior.from(bottomSheet).setPeekHeight(1000);
                }
            });

        return dialog;
        }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
        {
        super.onViewCreated(view, savedInstanceState);

        setUpRecyclerView();
        setupViewModel();
        setupObserver();
        setupCartViewModel();
        setupLifecycleOwner();
        mCartViewModel.setCountObservable();
        if (getTag() == TAG) //if insert cart.
            {
            setupClickedInsertCartButton();

            } else // update cart.
            {
            var labelButton = getString(R.string.laundry_service_content_cart_update_button);
            mBinding.iLaundryServiceLayout.btnLaundryServicesContentCart.setText(labelButton);
            renderUpdateOrder();
            setupClickedUpdateCartButton();

            }

        }

    private void setupLifecycleOwner()
        {
        mBinding.setLifecycleOwner(this);
        mBinding.iLaundryServiceLayout.setLifecycleOwner(this);
        mBinding.iLaundryServiceLayout.setModel(mCartViewModel);

        mBinding.setFragment(this);
        }

    private void setUpRecyclerView()
        {
        var recyclerView = mBinding.iLaundryServiceLayout.rvLaundryServicesContent;
        mAdapter = new LaundryServicesAdapter( Glide.with(this));
        recyclerView.setAdapter(mAdapter);

        setupSelectionTracker();
        mAdapter.setSelectionTracker(selectionTracker);
        if (isCompact())
            {
            recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));

            } else
            {
            recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));

            }


        }

    private void setupClickedInsertCartButton()
        {

        mBinding.iLaundryServiceLayout.btnLaundryServicesContentCart.setOnClickListener(v ->
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
                    Timber.i("selection:%s", i);

                    });
                }


            addOrder(addItemServiceToCart(), laundryServices);
            dismiss();
            });
        }

    private void setupClickedUpdateCartButton()
        {

        mBinding.iLaundryServiceLayout.btnLaundryServicesContentCart.setOnClickListener(v ->
            {
            int oldCount = mItem.getService().getCount();
            //TODO: move to viewModel
            int newCount = mCartViewModel.getCountObservable().get();

            Timber.e("updateServices, oldCount =  " + oldCount + ", newCount = " + newCount);

            if (oldCount != newCount)
                {
                mCartViewModel.updateService(updateItemService());
                } else Timber.e("NOT update service");


            List<LaundryService> laundryServices = new ArrayList<>();
            mAdapter.getCheckedChipId().forEach(i ->
                {

                int id = i.getId();
                int cost = i.getCost();
                String url = i.getLaundryService().getIconUrl();
                String nameEN = i.getLaundryService().getNameEn();
                String nameAr = i.getLaundryService().getNameAr();
                laundryServices.add(new LaundryService(id, cost, url, nameEN, nameAr));
                Timber.e("update:" + i);

                });

            if (!mItem.getLaundryServices().equals(laundryServices) && !mItem.getLaundryServices().containsAll(laundryServices))
                {
                var itemLaundryService = mItem.getService().getId();

                mCartViewModel.removeLaundryService(itemLaundryService);
                updateOrder(laundryServices);
                Log.e(TAG, " update laundry service");

                } else
                {
                Log.e(TAG, "NOT update laundry service");
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
        int count = mCartViewModel.getCountObservable().get();

        return new ItemService(nameEn, nameAr, costItemService, idItemService, count);
        }

    private ItemService updateItemService()
        {
        var itemService = mItem.getService();
        //TODO: move to viewModel
        int count = 1;// mCountObservable.get();

        itemService.setCount(count);

        return itemService;
        }

    private void renderUpdateOrder()
        {
        mCartViewModel.getItemToUpdateService().observe(getViewLifecycleOwner(), serviceAndLaundryService ->
            {

            mItem = serviceAndLaundryService;
            //TODO: move to viewModel
            //   mCountObservable.set(mItem.getService().getCount());
            checkedItems();

            Timber.e("renderUpdateOrder = %s", serviceAndLaundryService.getService().getCount());
            });

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
        selectionTracker = new SelectionTracker.Builder<>("card_selection",
                mBinding.iLaundryServiceLayout.rvLaundryServicesContent
                , new LaundryServicesAdapter.ItemsKeyProvider(mAdapter),
                new LaundryServicesAdapter.ItemsDetailsLookup(mBinding.iLaundryServiceLayout.rvLaundryServicesContent),
                StorageStrategy.createLongStorage()).
                withSelectionPredicate(SelectionPredicates.createSelectAnything()).build();


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

                case SUCCESS ->
                    {

                    Timber.e("SUCCESS%s", itemsEntities.getMData());
                    renderList(itemsEntities.getMData());
                    }
                case LOADING ->
                    {

                    Timber.e("LOADING");

                    }

                case NULL ->
                    {
                    Timber.e("NULL");

                    }
                case ERROR ->
                    {
                    Timber.e("ERROR");

                    }


                }
            });
        }


    public void onDismissButton()
        {
        dismiss();
        }

    public boolean isCompact()
        {
        return AdaptiveUtils.compactScreen(requireActivity());
        }

    private void renderList(Category.ItemsEntity item)
        {
        mAdapter.addItemService(item);
        }


    private void checkedItems()
        {
        var checkedItem = mCartViewModel.getItemLaundryServiceChecked().getValue();
        Timber.i(String.valueOf(checkedItem));
        mAdapter.setCheckedChipId(checkedItem);
        }

    private void setupViewModel()
        {
        NavController navController = NavHostFragment.findNavController(this);
        NavBackStackEntry backStackEntry = navController.getBackStackEntry(R.id.nav_home_priceList);

        mItemsCategory = new ViewModelProvider(backStackEntry).get(CategoryViewModel.class);
        }

    private void setupCartViewModel()
        {
        mCartViewModel = new ViewModelProvider(requireActivity()).get(CartViewModel.class);
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
    public void onDetach()
        {
        super.onDetach();
        selectionTracker.clearSelection();

        }

    @Override
    public void onDestroy()
        {
        super.onDestroy();
        mBinding = null;
        }
    }