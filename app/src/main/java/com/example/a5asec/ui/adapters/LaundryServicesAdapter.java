package com.example.a5asec.ui.adapters;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.selection.ItemDetailsLookup;
import androidx.recyclerview.selection.ItemKeyProvider;
import androidx.recyclerview.selection.SelectionTracker;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.a5asec.R;
import com.example.a5asec.data.model.api.Category;
import com.example.a5asec.databinding.ListItemLaundryServicesBinding;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import lombok.Setter;
import lombok.val;
import timber.log.Timber;


public class LaundryServicesAdapter
        extends ListAdapter<Category.ItemServicesEntity,
        LaundryServicesAdapter.LaundryServicesViewHolder>
{
    public static final String TAG = "LaundryServicesAdapter";
    @Setter
    private SelectionTracker<Long> selectionTracker;

    public LaundryServicesAdapter()
    {
        super(laundryServiceCallback());
    }

    private static DiffUtil.ItemCallback<Category.ItemServicesEntity> laundryServiceCallback()
    {
        return new DiffUtil.ItemCallback<>()
        {
            @Override
            public boolean areItemsTheSame(@NonNull Category.ItemServicesEntity oldItem,
                                           @NonNull Category.ItemServicesEntity newItem)
            {
                return oldItem.getId() == newItem.getId();
            }

            @Override
            public boolean areContentsTheSame(@NonNull Category.ItemServicesEntity oldItem,
                                              @NonNull Category.ItemServicesEntity newItem)
            {
                return oldItem.equals(newItem);
            }

        };
    }

    @NotNull
    public LaundryServicesAdapter.LaundryServicesViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent, int viewType)
    {
        ListItemLaundryServicesBinding itemLaundryServicesBinding =
                DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                        R.layout.list_item_laundry_services, parent, false);

        return new LaundryServicesViewHolder(itemLaundryServicesBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull LaundryServicesAdapter.LaundryServicesViewHolder holder,
                                 int position)
    {
        Timber.tag(TAG).d("Laundry Services size: %d", getCurrentList().size());
        var item = getItem(position);
        holder.bind(item, position);

    }


    public void addItemService(Category.ItemsEntity item)
    {
        setItemService(item);
    }

    private void setItemService(Category.ItemsEntity category)
    {
        List<Category.ItemServicesEntity> mItemServiceEntity = new ArrayList<>();

        mItemServiceEntity.add(new Category.ItemServicesEntity(null, category.getId(),
                category.getCost()));
        mItemServiceEntity.addAll(category.getItemServices());
        submitList(mItemServiceEntity);

    }


    public Integer getItemsId(long position)
    {
        if (getCurrentList().isEmpty()) {
            throw new IndexOutOfBoundsException("List is empty");
        }
        return getCurrentList().get((int) position).getId();
    }


    public static class LaundryServiceDetailsLookup extends ItemDetailsLookup<Long>
    {
        private final RecyclerView recyclerView;

        public LaundryServiceDetailsLookup(RecyclerView recyclerView)
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

                if (viewHolder instanceof LaundryServicesAdapter.LaundryServicesViewHolder itemViewHolder) {
                    return (itemViewHolder).getItemDetails();
                }
            }
            return null;
        }

    }

    public static class LaundryServicesKeyProvider extends ItemKeyProvider<Long>
    {
        LaundryServicesAdapter mAdapter;


        public LaundryServicesKeyProvider(LaundryServicesAdapter adapter)
        {
            super(ItemKeyProvider.SCOPE_MAPPED);
            mAdapter = adapter;
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

    class LaundryServicesViewHolder extends RecyclerView.ViewHolder
    {
        public static final int BASE_SERVICE_ITEM = 0;
        public static final int ANY_SERVICE_ITEM = 1;
        private final Details details;

        private final ListItemLaundryServicesBinding mItemsBinding;


        public LaundryServicesViewHolder(
                @NonNull ListItemLaundryServicesBinding itemLaundryServicesBinding)
        {
            super(itemLaundryServicesBinding.getRoot());
            mItemsBinding = itemLaundryServicesBinding;

            details = new Details();


        }


        public void bind(Category.ItemServicesEntity item, int position)
        {
            val languageTags = AppCompatDelegate.getApplicationLocales().toLanguageTags();
            details.position = position;

            mItemsBinding.setItemService(item);
            mItemsBinding.setLanguage(languageTags);

            if (position == BASE_SERVICE_ITEM) {
                mItemsBinding.setPosition(BASE_SERVICE_ITEM);
            } else {
                mItemsBinding.setPosition(ANY_SERVICE_ITEM);
            }

            if (selectionTracker != null) {
                Timber.tag(TAG).e("selectionTracker_LaundryServiceUpdate");
                bindSelectedState();
                selectionTracker.setItemsSelected(List.of((long) BASE_SERVICE_ITEM), true);
            } else {
                Timber.tag(TAG).e("selectionTracker equal to null");

            }


        }


        private void bindSelectedState()
        {
            mItemsBinding.cvLaundryServicesServices.setChecked(selectionTracker.isSelected(details.getSelectionKey()));
            changeColorInItemView(details.getSelectionKey());

        }


        /**
         * called when clicked item and change color in item.
         *
         * @param position of clicked item.
         */
        private void changeColorInItemView(long position)
        {
            int colorChecked =
                    ContextCompat.getColor(mItemsBinding.cvLaundryServicesServices.getContext(),
                            R.color.md_theme_light_onPrimary);
            int colorNotChecked =
                    ContextCompat.getColor(mItemsBinding.cvLaundryServicesServices.getContext(),
                            R.color.md_theme_light_scrim);

            if (selectionTracker.isSelected(position)) {
                mItemsBinding.ivLaundryServices.setColorFilter(colorChecked);
                mItemsBinding.tvLaundryServicesContentTitle.setTextColor(colorChecked);
                mItemsBinding.tvLaundryServicesSalary.setTextColor(colorChecked);
            } else {
                mItemsBinding.ivLaundryServices.setColorFilter(colorNotChecked);
                mItemsBinding.tvLaundryServicesContentTitle.setTextColor(colorNotChecked);
                mItemsBinding.tvLaundryServicesSalary.setTextColor(colorNotChecked);
            }

        }


        private ItemDetailsLookup.ItemDetails<Long> getItemDetails()
        {
            return details;
        }

    }


    class Details extends ItemDetailsLookup.ItemDetails<Long>
    {
        long position;


        @Override
        public int getPosition()
        {
            return (int) position;
        }

        @Nullable
        @Override
        public Long getSelectionKey()
        {
            return position;
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
    }

}
