package com.example.a5asec.ui.view.home;

import android.os.Bundle;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ActionMode;
import androidx.core.view.GestureDetectorCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavBackStackEntry;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.selection.SelectionPredicates;
import androidx.recyclerview.selection.SelectionTracker;
import androidx.recyclerview.selection.StorageStrategy;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.a5asec.R;
import com.example.a5asec.data.model.db.ServiceAndLaundryService;
import com.example.a5asec.databinding.FragmentCartBinding;
import com.example.a5asec.ui.adapters.CartAdapter;
import com.example.a5asec.ui.view.viewmodel.CartViewModel;
import com.example.a5asec.ui.view.viewmodel.CategoryViewModel;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import java.util.Arrays;
import java.util.List;

import timber.log.Timber;

/**
 * Cart Fragment for the Cart screen, shows add order and mange service to adding.
 */
public class CartFragment extends Fragment {
    private static final String TAG = "CartFragment";
    private CartViewModel mCartViewModel;
    private CategoryViewModel mCategoryViewModel;
    private CartAdapter mAdapter;
    private SelectionTracker<Long> selectionTracker;

    private FragmentCartBinding mBinding;
    private ActionMode mActionMode;
    private GestureDetectorCompat gestureDetector;

    private boolean isLongClick;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = FragmentCartBinding.inflate(inflater, container, false);
        //   DataBindingUtil.inflate(inflater, R.layout.fragment_cart, container, false);

        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setUpRecyclerView();
        setupCartViewModel();
        setupCategoryViewModel();
        setupObserver();
    }

    private void setUpRecyclerView() {
        mAdapter = new CartAdapter();
        mBinding.rvCart.setAdapter(mAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        mBinding.rvCart.setLayoutManager(layoutManager);

        setupSelectionTracker();

    }

    private void setupSelectionTracker() {
        selectionTracker = new SelectionTracker.Builder<>("card_selection",
                mBinding.rvCart
                , new CartAdapter.CartItemsKeyProvider(mAdapter),
                new CartAdapter.CartDetailsLookup(mBinding.rvCart),
                StorageStrategy.createLongStorage())
                .withSelectionPredicate(SelectionPredicates.createSelectAnything())
                .build();

        handleTouchItem();
        setupSelectionTrackerObserver();
        CartAdapter.setSelectionTracker(selectionTracker);

    }

    private void handleTouchItem() {

        gestureDetector = new GestureDetectorCompat(
                requireContext(),
                new GestureDetector.SimpleOnGestureListener() {
                    @Override
                    public boolean onSingleTapConfirmed(MotionEvent e) {
                        isLongClick = selectionTracker.getSelection().size() > 0;
                        Timber.tag(TAG).e("isLongClick = %s", isLongClick);

                        if (isLongClick) {
                            return false;
                        } else // if no long click.
                        {
                            View view = mBinding.rvCart.findChildViewUnder(e.getX(), e.getY());
                            if (view != null) {
                                RecyclerView.ViewHolder holder = mBinding.rvCart.getChildViewHolder(view);
                                if (holder instanceof CartAdapter.CartViewHolder) {
                                    int position = holder.getAbsoluteAdapterPosition();

                                    onItemClick(position);

                                }
                            }
                        }

                        return true;

                    }


                });


        itemTouchListener();
    }

    private void onItemClick(int position) {
        int itemId = mAdapter.getItemServiceId(position);
        traceWithLog("item = %s", itemId + ", position = " + position);


        mCategoryViewModel.setItemServiceWithId(itemId); // IMPROVE: move this line to laundrySerivce.
        mCartViewModel.addItemToUpdateService(itemId);

        LaundryServicesBottomSheet laundryServicesFragment = new LaundryServicesBottomSheet();
        laundryServicesFragment.show(getParentFragmentManager(), LaundryServicesBottomSheet.TAG_UPDATE);

    }

    private void itemTouchListener() {
        mBinding.rvCart.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {
                return gestureDetector.onTouchEvent(e);

            }

            @Override
            public void onTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {
                gestureDetector.onTouchEvent(e);

            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
                // Do nothing
            }
        });
    }

    private void setupSelectionTrackerObserver() {

        selectionTracker.addObserver(//show selected with action mode.
                new SelectionTracker.SelectionObserver<>() {
                    @Override
                    public void onItemStateChanged(@NonNull Long key, boolean selected) {
                        super.onItemStateChanged(key, selected);
                        int count = selectionTracker.getSelection().size();
                        traceWithLog("item selected = %s" + selected);

                        if (count > 0 && mActionMode == null) {
                            mActionMode = ((AppCompatActivity) requireActivity()).startSupportActionMode(actionModeCallback());

                        } else if (mActionMode != null && count == 0) {
                            mActionMode.finish();
                        }

                        if (mActionMode != null) {
                            updateActionModeTitle(mActionMode);

                        }

                        bindSelectedData(selected, key);


                    }


                });
    }

    private ActionMode.Callback actionModeCallback() {
        return new ActionMode.Callback() {
            @Override
            public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
                MenuInflater inflater = actionMode.getMenuInflater();
                inflater.inflate(R.menu.action_mode_menu, menu);

                return true;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
                return false;
            }

            @Override
            public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
                final int itemId = R.id.action_mode_delete;
                return menuItem.getItemId() == itemId && deleteSelectedItem();

            }

            @Override
            public void onDestroyActionMode(ActionMode actionMode) {


                selectionTracker.clearSelection();
                mActionMode = null;


            }
        };
    }

    private void updateActionModeTitle(ActionMode actionMode) {
        if (selectionTracker.hasSelection()) {
            int count = selectionTracker.getSelection().size();
            actionMode.setTitle("Selected remove Items: " + count);

        }
    }

    private void bindSelectedData(boolean selected, long key) {
        if (selected && key > -1) {
            mCartViewModel.addSelectedCartWithId(mAdapter.getItemIdService(key));
        } else if (key > -1) {
            mCartViewModel.removeSelectedCartId(mAdapter.getItemIdService(key));

        }

    }

    public boolean deleteSelectedItem() {

        mCartViewModel.removeItemSelected();
        mCartViewModel.removeOrUndoItemService();
        createSnackBar();

        return true;
    }

    private void createSnackBar() {
        Snackbar snackbar = Snackbar.make(mBinding.getRoot(),
                requireContext().getString(R.string.cart_message_delete_item),
                BaseTransientBottomBar.LENGTH_LONG);

        onActionSnackBar(snackbar);
        onCallbackSnackBar(snackbar);
        snackbar.show();
        mActionMode.finish();


    }

    private void onActionSnackBar(Snackbar snackbar) {
        snackbar.setAction(requireContext().getString(R.string.cart_undo_button), view ->
        {
            traceWithLog("Selected item undo  size  = %s" + mCartViewModel.getSelectedCartRemoveId().size());
            mCartViewModel.removeOrUndoItemService();
            mCartViewModel.clearItemDeletedSelection();
            mBinding.rvCart.smoothScrollToPosition(0);

        });
        snackbar.setActionTextColor(requireContext().getColor(R.color.md_theme_light_secondary));

    }

    private void onCallbackSnackBar(Snackbar snackbar) {
        snackbar.addCallback(new Snackbar.Callback() {
            @Override
            public void onDismissed(Snackbar transientBottomBar, int event) {
                super.onDismissed(transientBottomBar, event);
                if (event != DISMISS_EVENT_ACTION) {
                    traceWithLog("Clear item selected    ");

                    mCartViewModel.clearItemDeletedSelection();

                }
            }
        });
    }

    private void setupCartViewModel() {
        mCartViewModel = new ViewModelProvider(requireActivity())
                .get(CartViewModel.class);
    }

    private void setupCategoryViewModel() {
        NavController navController = NavHostFragment.findNavController(this);
        NavBackStackEntry backStackEntry = navController.getBackStackEntry(R.id.nav_home_priceList);
        mCategoryViewModel = new ViewModelProvider(backStackEntry)
                .get(CategoryViewModel.class);
    }


    private void setupObserver() {

        mCartViewModel.getListOrder().observe(getViewLifecycleOwner(), order ->
        {
            switch (order.mStatus) {
                case SUCCESS -> {
                    traceWithLog("setupObserver SUCCESS");
                    fetchTotalItems();
                    fetchData(order.getMData());

                    hideLoading();

                }

                case NULL -> {
                    traceWithLog("NULL");
                    emptyCartOrder();
                    mBinding.cpiCart.setVisibility(View.GONE);

                }
                case LOADING -> {
                    traceWithLog("LOADING");
                    showLoading();
                }

                case ERROR -> {
                    traceWithLog("ERROR");
                    mBinding.cpiCart.setVisibility(View.GONE);

                    traceWithLog(" setupObserver, ERROR = %s", order.getMMessage());
                    emptyCartOrder();

                }
            }
        });


    }

    private void fetchData(List<ServiceAndLaundryService> data) {
        mAdapter.submitList(data);
        mBinding.rvCart.smoothScrollToPosition(0);


    }


    private void fetchTotalItems() {
        mCartViewModel.getTotalAllOrder().observe(getViewLifecycleOwner(), totalAllOrder ->
        {
            String total = getString(R.string.all_total_service) + " " + totalAllOrder
                    + " " + getString(R.string.all_cost_label);

            mBinding.tvCartTotal.setText(total);
            traceWithLog("totalAllOrder, SUCCESS");


        });
    }

    private void traceWithLog(String... message) {
        Timber.tag(TAG).e(Arrays.toString(message));
    }

    private void hideLoading() {
        mBinding.cpiCart.setVisibility(View.GONE);
        mBinding.tvCartEmpty.setVisibility(View.GONE);
        mBinding.imvCartEmpty.setVisibility(View.GONE);

        mBinding.rvCart.setVisibility(View.VISIBLE);
        mBinding.tvCartTotal.setVisibility(View.VISIBLE);
        mBinding.btnCartCheckOut.setVisibility(View.VISIBLE);

    }

    private void showLoading() {
        mBinding.cpiCart.setVisibility(View.VISIBLE);
        mBinding.rvCart.setVisibility(View.GONE);
    }

    private void emptyCartOrder() {
        mBinding.rvCart.setVisibility(View.GONE);
        mBinding.tvCartTotal.setVisibility(View.GONE);
        mBinding.btnCartCheckOut.setVisibility(View.GONE);

        mBinding.tvCartEmpty.setVisibility(View.VISIBLE);
        mBinding.imvCartEmpty.setVisibility(View.VISIBLE);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        mBinding = null;
        selectionTracker.clearSelection();
        selectionTracker = null;

    }
}

/*
private void enableSwipeToDeleteAndUndo()
    {
    SwipeToDeleteCallback swipeToDeleteCallback = new SwipeToDeleteCallback(requireContext())
        {
        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction)
            {
            final int position = viewHolder.getAdapterPosition();
            // final ServiceAndLaundryService item = mAdapter.getItemByPosition(position);
            //  mAdapter.removeItem(position);
            //mCartViewModel.removeOrUndoItemService(item.getService());

            Snackbar snackbar = Snackbar
                    .make(mBinding.getRoot(), "Item was removed from the list.", BaseTransientBottomBar.LENGTH_LONG);
            snackbar.setAction("UNDO", view ->
                {

                //mAdapter.restoreItem(item, position);
                mBinding.rvCart.scrollToPosition(position);
                //  item.getService().setFlag(0);
                // mCartViewModel.removeOrUndoItemService(item.getService());
                });

            snackbar.setActionTextColor(Color.YELLOW);
            snackbar.show();
            }
        };
    ItemTouchHelper itemTouchHelper = new ItemTouchHelper(swipeToDeleteCallback);
    itemTouchHelper.attachToRecyclerView(mBinding.rvCart);

    }*/
