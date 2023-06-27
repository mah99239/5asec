package com.example.a5asec.ui.view.home;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavBackStackEntry;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.selection.SelectionPredicates;
import androidx.recyclerview.selection.SelectionTracker;
import androidx.recyclerview.selection.StorageStrategy;
import androidx.recyclerview.widget.GridLayoutManager;

import com.example.a5asec.R;
import com.example.a5asec.data.model.api.Category;
import com.example.a5asec.data.model.db.ItemService;
import com.example.a5asec.data.model.db.LaundryService;
import com.example.a5asec.databinding.FragmentLaundryServicesBinding;
import com.example.a5asec.ui.adapters.LaundryServicesAdapter;
import com.example.a5asec.ui.view.viewmodel.CartViewModel;
import com.example.a5asec.ui.view.viewmodel.CategoryViewModel;
import com.example.a5asec.utility.AdaptiveUtils;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Set;

import timber.log.Timber;

/**
 * Laundry Services for Selected Item Laundry service S
 */
public class LaundryServicesBottomSheet extends BottomSheetDialogFragment
{
    public static final String TAG = "LaundryServicesFragment";
    public static final String TAG_UPDATE = "LaundryServicesFragment-UPDATE";
    private CategoryViewModel mItemsCategory;
    private CartViewModel mCartViewModel;
    private boolean shouldCallSelectionObserver = false;

    private LaundryServicesAdapter mAdapter;
    private FragmentLaundryServicesBinding mBinding;
    private SelectionTracker<Long> selectionTracker;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState)
    {
        Timber.tag(TAG).e("tag = %s", getTag());

        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_laundry_services,
                container, false);
        return mBinding.getRoot();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState)
    {
        BottomSheetDialog dialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);

        dialog.setOnShowListener(dialog1 -> {
            BottomSheetDialog d = (BottomSheetDialog) dialog1;
            FrameLayout bottomSheet =
                    d.findViewById(com.google.android.material.R.id.design_bottom_sheet);
            if (bottomSheet != null) {
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

        setupViewModels();
        setupLifecycleOwner();
        setUpRecyclerView();
        observerCategory();


        if (Objects.equals(getTag(), TAG)) // Insert cart.
        {
            shouldCallSelectionObserver = true;
            setupClickedInsertCartButton();
            mCartViewModel.setCountObservable();
            mCartViewModel.clearSelected();

        } else // update cart.
        {
            final var labelButton = getString(R.string.laundry_service_content_cart_update_button);
            mBinding.iLaundryServiceLayout.btnLaundryServicesContentCart.setText(labelButton);

            setupClickedUpdateCartButton();

        }

    }

    private void setupViewModels()
    {
        mCartViewModel = new ViewModelProvider(requireActivity()).get(CartViewModel.class);
        mBinding.iLaundryServiceLayout.setModel(mCartViewModel);

        NavController navController = NavHostFragment.findNavController(this);
        NavBackStackEntry backStackEntry = navController.getBackStackEntry(R.id.nav_home_priceList);

        mItemsCategory = new ViewModelProvider(backStackEntry).get(CategoryViewModel.class);

    }

    private void setupLifecycleOwner()
    {
        mBinding.setLifecycleOwner(this);
        mBinding.iLaundryServiceLayout.setLifecycleOwner(this);
        mBinding.setFragment(this);
    }

    private void setUpRecyclerView()
    {
        var recyclerView = mBinding.iLaundryServiceLayout.rvLaundryServicesContent;
        mAdapter = new LaundryServicesAdapter();
        recyclerView.setAdapter(mAdapter);


        if (isCompact()) {
            recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));

        } else {
            recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));

        }
        setupSelectionTracker();


    }


    private void setupClickedInsertCartButton()
    {

        mBinding.iLaundryServiceLayout.btnLaundryServicesContentCart.setOnClickListener(v -> {


            mCartViewModel.setSelectionLaundryService(mItemsCategory.getItemService());


            addItemServiceToCart();
            addOrder(mCartViewModel.getSelectedItemService(),
                    mCartViewModel.getSelectionLaundryService());

            dismiss();
        });
    }

    private void setupClickedUpdateCartButton()
    {
        mBinding.iLaundryServiceLayout.btnLaundryServicesContentCart.setOnClickListener(v -> {
            int selectionSize = mCartViewModel.getSelectedItemServicesId().size();

            traceWithLog("selection size  =  %s" + selectionSize);


            var itemsServiceOld = mCartViewModel.getItemServiceSelectedToUpdate().getValue();
            int oldCount = Objects.requireNonNull(mCartViewModel.getItemInCartSelected().getValue())
                    .getService().getCount();

            if (!itemsServiceOld.equals(mCartViewModel.getSelectedItemServicesId()) || oldCount != mCartViewModel.getCountObservable()
                    .get())
            {

                if (oldCount != mCartViewModel.getCountObservable().get()) {
                    mCartViewModel.updateService();

                }
                String message =
                        requireContext().getString(R.string.laundry_service_update_item_succes);
                onActionSnackBar(message);
                traceWithLog("Update =  %s" + message);

            } else {
                String message =
                        requireContext().getString(R.string.laundry_service_update_item_fail);
                onActionSnackBar(message);
                traceWithLog("Update =  %s" + message);

            }

            dismiss();
        });
    }


    private void addItemServiceToCart()
    {
        var item = Objects.requireNonNull(mItemsCategory.getItem().getValue()).getMData();
        mCartViewModel.setSelectedItemService(item);

    }


    private void addOrder(ItemService service, List<LaundryService> laundryServices)
    {
        mCartViewModel.addService(service, laundryServices);

    }


    private void setupSelectionTracker()
    {
        selectionTracker = new SelectionTracker.Builder<>("card_selection",
                mBinding.iLaundryServiceLayout.rvLaundryServicesContent,
                new LaundryServicesAdapter.LaundryServicesKeyProvider(mAdapter),
                new LaundryServicesAdapter.LaundryServiceDetailsLookup(mBinding.iLaundryServiceLayout.rvLaundryServicesContent), StorageStrategy.createLongStorage()).withSelectionPredicate(SelectionPredicates.createSelectAnything())
                .build();
        if (Objects.equals(getTag(), TAG_UPDATE)) {
            setupSelectedItemWithUpdate();

        }
        setupSelectionTrackerObserver();
        mAdapter.setSelectionTracker(selectionTracker);

    }

    private void setupSelectionTrackerObserver()
    {
        selectionTracker.addObserver(new SelectionTracker.SelectionObserver<>()
        {


            @Override
            public void onItemStateChanged(@NonNull Long key, boolean selected)
            {
                if (shouldCallSelectionObserver) {
                    bindSelectedData(selected, key);
                    Timber.tag(TAG).e("item selected = " + key + " selected = " + selected);

                    bindUpdateSelectedLaundryService(key);
                    mCartViewModel.restCostSelectedWithService();

                    mCartViewModel.getSelectedItemServicesId().forEach(i -> {
                        Timber.tag(TAG).e("id = %s", i);
                        mCartViewModel.setCostSelectedWithService(mItemsCategory.getItemServiceById(i));

                    });

                    int itemCost = Objects.requireNonNull(mItemsCategory.getItem().getValue())
                            .getMData().getCost();
                    mCartViewModel.sumAllSelectionLaundryService(itemCost);

                } else shouldCallSelectionObserver = true;
            }


        });
    }

    private void bindSelectedData(boolean selected, long key)
    {
        try {
            if (selected && key > 0) {

                mCartViewModel.addSelectionLaundryService(mAdapter.getItemsId(key));
            } else if (key > 0) {
                mCartViewModel.removeSelectionLaundryService(mAdapter.getItemsId(key));

            }
        } catch (IndexOutOfBoundsException e) {
            Timber.tag(TAG).e(e);
        }


    }

    private void bindUpdateSelectedLaundryService(long key)
    {
        if (key > 0 && Objects.equals(getTag(), TAG_UPDATE)) {
            traceWithLog("id = " + mAdapter.getItemsId(key));

            mCartViewModel.getIdLaundryToUpdate(mAdapter.getItemsId(key));

        }
    }

    public Category.ItemServicesEntity getItemsIdService(int id) throws NoSuchElementException
    {
        return mItemsCategory.getItemService().stream()
                .filter(itemServicesEntity -> itemServicesEntity.getId() == id).findFirst().get();

    }

    public int getPosition(Category.ItemServicesEntity item)
    {

        return mItemsCategory.getItemService().indexOf(item);

    }

    private void observerCategory()
    {


        mItemsCategory.getItem().observe(getViewLifecycleOwner(), itemsEntities -> {
            switch (itemsEntities.mStatus) {

                case SUCCESS -> {
                    traceWithLog("SUCCESS%s" + itemsEntities.getMData());
                    renderList(itemsEntities.getMData());

                }
                case LOADING -> traceWithLog("LOADING");


                case NULL -> traceWithLog("NULL");


                case ERROR -> traceWithLog("ERROR");


            }
        });
    }

    private void setupSelectedItemWithUpdate()
    {

        mCartViewModel.getItemServiceSelectedToUpdate().observe(this, this::setupItemsSelected);


    }

    private void setupItemsSelected(Set<Integer> itemsId)
    {
        itemsId.stream().map(this::getItemsIdService).filter(Objects::nonNull)
                .map(this::getPosition).forEach(position -> {
                    selectionTracker.setItemsSelected(List.of((long) position), true);
                    Timber.tag(TAG).e("setupSelectedItemWithUpdate , id %d", position);
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


    private void onActionSnackBar(String message)
    {
        View parentView = requireActivity().findViewById(android.R.id.content);

        Snackbar snackbar = Snackbar.make(parentView, message, BaseTransientBottomBar.LENGTH_LONG);

        snackbar.show();

    }

    private void traceWithLog(String... message)
    {
        Timber.tag(TAG).d(Arrays.toString(message));
    }

    @Override
    public void onDestroyView()
    {
        super.onDestroyView();
        mBinding = null;
        selectionTracker = null;
    }


}