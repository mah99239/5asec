package com.example.a5asec.ui.adapters;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.selection.ItemDetailsLookup;
import androidx.recyclerview.selection.ItemKeyProvider;
import androidx.recyclerview.selection.SelectionTracker;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.a5asec.R;
import com.example.a5asec.data.model.db.LaundryService;
import com.example.a5asec.data.model.db.ServiceAndLaundryService;
import com.example.a5asec.databinding.ListItemCartBinding;

import lombok.Setter;


public class CartAdapter extends
                         ListAdapter<ServiceAndLaundryService, CartAdapter.CartViewHolder>
{
    @Setter
    private static SelectionTracker<Long> selectionTracker;


    public CartAdapter()
    {
        super(cartCallback());

    }

    @NonNull
    private static DiffUtil.ItemCallback<ServiceAndLaundryService> cartCallback()
    {
        return new DiffUtil.ItemCallback<>()
        {
            @Override
            public boolean areItemsTheSame(@NonNull ServiceAndLaundryService oldItem,
                                           @NonNull ServiceAndLaundryService newItem)
            {
                return oldItem.getService().getId() == newItem.getService().getId();
            }

            @Override
            public boolean areContentsTheSame(@NonNull ServiceAndLaundryService oldItem,
                                              @NonNull ServiceAndLaundryService newItem)
            {
                return oldItem.getLaundryServiceWithFlagZero()
                        .size() == newItem.getLaundryServiceWithFlagZero().size() &&
                        oldItem.getLaundryServiceWithFlagZero()
                                .equals(newItem.getLaundryServiceWithFlagZero());
            }

        };
    }


    public Integer getItemIdService(long position)
    {
        return getItem((int) position).getService().getId();
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {

        return CartViewHolder.from(parent);
    }


    @Override
    public void onBindViewHolder(@NonNull CartViewHolder viewHolder, int position)
    {
        ServiceAndLaundryService dataToBind = getItem(viewHolder.getAbsoluteAdapterPosition());
        viewHolder.bind(dataToBind);

    }

    /**
     * called when remove item to get id primary of item service.
     *
     * @param position of List Item service.
     * @return primary id in item service.
     */
    public int getItemServiceId(int position)
    {
        return getItem(position).getService().getIdItemService();
    }


    public static class CartDetailsLookup extends ItemDetailsLookup<Long>
    {
        private final RecyclerView recyclerView;

        public CartDetailsLookup(RecyclerView recyclerView)
        {
            this.recyclerView = recyclerView;
        }

        @Nullable
        @Override
        public ItemDetails<Long> getItemDetails(@NonNull MotionEvent e)
        {
            View view = recyclerView.findChildViewUnder(e.getX(), e.getY());
            if (view != null) {
                RecyclerView.ViewHolder viewHolder = recyclerView.getChildViewHolder(view);

                if (viewHolder instanceof CartAdapter.CartViewHolder itemViewHolder) {
                    return (itemViewHolder).getItemDetails();
                }
            }
            return null;
        }

    }

    public static class CartItemsKeyProvider extends ItemKeyProvider<Long>
    {
        CartAdapter mCartAdapter;

        public CartItemsKeyProvider(CartAdapter adapter)
        {
            super(ItemKeyProvider.SCOPE_CACHED);
            mCartAdapter = adapter;
        }

        @Nullable
        @Override
        public Long getKey(int position)
        {
            return (long) position;
        }

        @Override
        public int getPosition(@NonNull Long key)
        {
            long value = key;

            return (int) value;

        }
    }

    public static class CartViewHolder extends RecyclerView.ViewHolder
    {
        private final ListItemCartBinding binding;

        private CartViewHolder(ListItemCartBinding binding)
        {
            super(binding.getRoot());
            this.binding = binding;


        }

        private static CartViewHolder from(@NonNull ViewGroup parent)
        {
            boolean shouldAttachToParentImmediately = false;
            ListItemCartBinding listItemCartBinding = DataBindingUtil.inflate(LayoutInflater
                            .from(parent.getContext()),
                    R.layout.list_item_cart, parent, shouldAttachToParentImmediately);

            return new CartViewHolder(listItemCartBinding);
        }

        public void bind(ServiceAndLaundryService item)
        {


            String languageTags = AppCompatDelegate.getApplicationLocales().toLanguageTags();
            int total = getTotalItemService(item);
            binding.setLifecycleOwner(binding.getLifecycleOwner());

            binding.setLanguage(languageTags);

            binding.setItemService(item);
            binding.setTotalCostItem(total);
            setupNestedRecycleView(item);


            if (selectionTracker != null) {

                bindSelectedState();
            }

            binding.executePendingBindings();


        }

        private void bindSelectedState()
        {
            binding.cvCartContainer.setChecked(
                    selectionTracker.isSelected(getItemDetails().getSelectionKey()));

        }

        /**
         * sum total in item service Cart with Stream.
         *
         * @return sum total of item service and List LaundryService
         */
        private int getTotalItemService(ServiceAndLaundryService serviceAndLaundryService)
        {
            if (serviceAndLaundryService == null) return -1;

            int costItemService = serviceAndLaundryService.getService().getCostItemService();
            int totalCostListLaundryService = serviceAndLaundryService.getLaundryService().stream()
                    .mapToInt(LaundryService::getCost).sum();
            int total = costItemService + totalCostListLaundryService;
            total *= serviceAndLaundryService.getService().getCount();
            return total;

        }

        /**
         * initialize nested recycleView that bind ItemService as base and List of LaundryService
         */
        private void setupNestedRecycleView(ServiceAndLaundryService item)
        {
            RecyclerView.RecycledViewPool viewPool = new RecyclerView.RecycledViewPool();


            LinearLayoutManager layoutManager = new LinearLayoutManager(
                    binding.rvCartService.getContext(),
                    LinearLayoutManager.HORIZONTAL,
                    false);

            layoutManager.setInitialPrefetchItemCount(
                    item.getLaundryService().size());

            LaundryServiceCartAdapter laundryServiceCartAdapter =
                    new LaundryServiceCartAdapter();

            laundryServiceCartAdapter.setData(item);
            binding.rvCartService.setLayoutManager(layoutManager);
            binding.rvCartService.setAdapter(laundryServiceCartAdapter);

            binding.rvCartService.setRecycledViewPool(viewPool);

        }

        private ItemDetailsLookup.ItemDetails<Long> getItemDetails()
        {
            return new ItemDetailsLookup.ItemDetails<Long>()
            {
                @Override
                public int getPosition()
                {
                    return getAbsoluteAdapterPosition();
                }

                @Override
                public Long getSelectionKey()
                {
                    return (long) getAbsoluteAdapterPosition();
                }


                @Override
                public boolean inSelectionHotspot(@NonNull MotionEvent e)
                {
                    return false;
                }

                @Override
                public boolean inDragRegion(@NonNull MotionEvent e)
                {
                    return true;
                }
            };
        }


    }


}