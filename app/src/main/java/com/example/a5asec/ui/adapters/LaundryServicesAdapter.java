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
import androidx.recyclerview.widget.RecyclerView;

import com.example.a5asec.R;
import com.example.a5asec.data.model.api.Category;
import com.example.a5asec.databinding.ListItemLaundryServicesBinding;
import com.example.a5asec.databinding.ListItemPriceBinding;
import com.example.a5asec.ui.adapters.base.BaseAdapter;
import com.example.a5asec.ui.adapters.base.BaseViewHolder;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import lombok.val;
import timber.log.Timber;


public class LaundryServicesAdapter extends BaseAdapter<ListItemLaundryServicesBinding, Category.ItemServicesEntity>
    {
    private static final String TAG = "LaundryServicesAdapter";
    private final Set<Category.ItemServicesEntity> checkedChipId;
    private final List<Category.ItemServicesEntity> mItemServiceEntity = new ArrayList<>();
    private SelectionTracker<Long> selectionTracker;
    public static final int BASE_SERVICE_ITEM = 0;
    private final Details details;

    public LaundryServicesAdapter()
        {
        super.mLayoutId = R.layout.list_item_laundry_services;
        details = new Details();
        checkedChipId = new HashSet<>();
        }

    @Override
    public void updateData(List<Category.ItemServicesEntity> list)
        {
      //  mItemServiceEntity.clear();
       // mItemServiceEntity.addAll(list);
        super.updateData(list);
        Timber.tag(TAG).e("size of item = %s", getItemCount());

        }

    @Override
    public void bind(ListItemLaundryServicesBinding binding, Category.ItemServicesEntity item)
        {
        val languageTags = AppCompatDelegate.getApplicationLocales().toLanguageTags();
        details.position = position;

        binding.setItemService(item);
        binding.setLanguage(languageTags);
        if (position == BASE_SERVICE_ITEM)
            {
            binding.setPosition(BASE_SERVICE_ITEM);
            } else
            {
            binding.setPosition(1);
            }

        if (selectionTracker != null)
            {
            bindSelectedState(binding);
            }

        selectionTracker.setItemsSelected(List.of((long) BASE_SERVICE_ITEM), true);

        bindSelectedData();
        }
    private void bindSelectedState(ListItemLaundryServicesBinding binding)
        {
        binding.cvLaundryServicesServices.setChecked(selectionTracker.isSelected(details.getSelectionKey()));
        changeColorInView(details.getSelectionKey(),binding);

        }
    private void changeColorInView(long position, ListItemLaundryServicesBinding binding)
        {
        int colorChecked = ContextCompat.getColor(binding.cvLaundryServicesServices.getContext(), R.color.md_theme_light_onPrimary);
        int colorNotChecked = ContextCompat.getColor(binding.cvLaundryServicesServices.getContext(), R.color.md_theme_light_scrim);

        if (selectionTracker.isSelected(position))
            {
            binding.ivLaundryServices.setColorFilter(colorChecked);
            binding.tvLaundryServicesContentTitle.setTextColor(colorChecked);
            binding.tvLaundryServicesSalary.setTextColor(colorChecked);
            } else
            {
            binding.ivLaundryServices.setColorFilter(colorNotChecked);
            binding.tvLaundryServicesContentTitle.setTextColor(colorNotChecked);
            binding.tvLaundryServicesSalary.setTextColor(colorNotChecked);
            }

        }
    private void bindSelectedData()
        {
        if (selectionTracker.isSelected(details.getSelectionKey())
                && details.getSelectionKey() > BASE_SERVICE_ITEM)
            {

            checkedChipId.add(getItemsId(details.getPosition()));
            //  mCard.setChecked(true);
            } else if (details.getPosition() > BASE_SERVICE_ITEM)
            {
            //   mCard.setChecked(false);
            checkedChipId.remove(getItemsId(details.getPosition()));

            }
        }
    ItemDetailsLookup.ItemDetails<Long> getItemDetails()
        {
        return details;
        }
    public Set<Category.ItemServicesEntity> getCheckedChipId()
        {
        return checkedChipId;
        }

    public void setCheckedChipId(List<Integer> id)
        {
        try
            {
            id.forEach(integer ->
                {
                var item = getItemsIdService(integer);
                checkedChipId.add(item);
                selectionTracker.setItemsSelected(List.of((long) getPosition(item)), true);

                Timber.tag(TAG).e(String.valueOf(getItemsIdService(integer)));
                });

            } catch (Exception e)
            {
            Timber.tag(TAG).e(String.valueOf(e.getMessage()));
            }
        }

/*     @NotNull
    public LaundryServicesAdapter.LaundryServicesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
        {
        ListItemLaundryServicesBinding itemLaundryServicesBinding =
                DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                        R.layout.list_item_laundry_services, parent, false);

        return new LaundryServicesViewHolder(itemLaundryServicesBinding);
        } */


 /*    @Override
    public void onBindViewHolder(@NonNull LaundryServicesAdapter.LaundryServicesViewHolder holder, int position)
        {

        var baseItem = mItemServiceEntity.get(position);
        holder.bind(baseItem, position);


        } */

    public Iterator<Long> getSelectionTracker()
        {
        return selectionTracker.getSelection().iterator();
        }

    public void setSelectionTracker(SelectionTracker<Long> selectionTracker)
        {
        this.selectionTracker = selectionTracker;
        }

    public void addItemService(Category.ItemsEntity item)
        {
        setItemService(item);
        }

    private void setItemService(Category.ItemsEntity category)
        {
        mItemServiceEntity.add(new Category.ItemServicesEntity(category.getId(), category.getCost(), null));
        mItemServiceEntity.addAll(category.getItemServices());
        updateData(mItemServiceEntity);
        }

    @Override
    public int getItemCount()
        {
        if (mItemServiceEntity.size() == 0) return 0;
        return mItemServiceEntity.size();

        }

    public Category.ItemServicesEntity getItemsId(int position)
        {
        return mItemServiceEntity.get(position);
        }

    public Category.ItemServicesEntity getItemsIdService(int id)
        {
        return mItemServiceEntity.stream().skip(1).
                filter(itemServicesEntity -> itemServicesEntity.getId() == id).findFirst().get();

        }

    public int getPosition(Category.ItemServicesEntity item)
        {

        return mItemServiceEntity.indexOf(item);

        }

    public static class ItemsDetailsLookup extends ItemDetailsLookup<Long>
        {

        private final RecyclerView recyclerView;

        public ItemsDetailsLookup(RecyclerView recyclerView)
            {
            this.recyclerView = recyclerView;
            }

        @Nullable
        @Override
        public ItemDetails<Long> getItemDetails(@NonNull MotionEvent e)
            {
            View view = recyclerView.findChildViewUnder(e.getX(), e.getY());
            if (view != null)
                {
                RecyclerView.ViewHolder viewHolder = recyclerView.getChildViewHolder(view);
              /*   if (viewHolder instanceof BaseViewHolder<?>)
                    {
                    return ((LaundryServicesViewHolder) viewHolder).getItemDetails();
                    } */
                }
            return null;
            }

        }

    public static class ItemsKeyProvider extends ItemKeyProvider<Long>
        {
        LaundryServicesAdapter mAdapter;

        /**
         * Creates a new provider with the given scope.
         *
         * @param adapter Scope can't be changed at runtime.
         */
        public ItemsKeyProvider(LaundryServicesAdapter adapter)
            {
            super(ItemKeyProvider.SCOPE_CACHED);
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

    static class Details extends ItemDetailsLookup.ItemDetails<Long>
        {

        long position;

        Details()
            {
            }

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

 /*   class LaundryServicesViewHolder extends RecyclerView.ViewHolder
        {
        public static final int BASE_SERVICE_ITEM = 0;
        private final Details details;

        private final ListItemLaundryServicesBinding mItemsBinding;


        public LaundryServicesViewHolder(@NonNull ListItemLaundryServicesBinding itemLaundryServicesBinding)
            {
            super(itemLaundryServicesBinding.getRoot());
            this.mItemsBinding = itemLaundryServicesBinding;

            details = new Details();


            }


        public void bind(Category.ItemServicesEntity item, int position)
            {
            val languageTags = AppCompatDelegate.getApplicationLocales().toLanguageTags();
            details.position = position;

            mItemsBinding.setItemService(item);
            mItemsBinding.setLanguage(languageTags);
            if (position == BASE_SERVICE_ITEM)
                {
                mItemsBinding.setPosition(BASE_SERVICE_ITEM);
                } else
                {
                mItemsBinding.setPosition(1);
                }

            if (selectionTracker != null)
                {
                bindSelectedState();
                }

            selectionTracker.setItemsSelected(List.of((long) BASE_SERVICE_ITEM), true);

            bindSelectedData();


            }


        private void bindSelectedState()
            {
            mItemsBinding.cvLaundryServicesServices.setChecked(selectionTracker.isSelected(details.getSelectionKey()));
            changeColorInView(details.getSelectionKey());

            }

        private void bindSelectedData()
            {
            if (selectionTracker.isSelected(details.getSelectionKey())
                    && details.getSelectionKey() > BASE_SERVICE_ITEM)
                {

                checkedChipId.add(getItemsId(details.getPosition()));
                //  mCard.setChecked(true);
                } else if (details.getPosition() > BASE_SERVICE_ITEM)
                {
                //   mCard.setChecked(false);
                checkedChipId.remove(getItemsId(details.getPosition()));

                }
            }

        private void changeColorInView(long position)
            {
            int colorChecked = ContextCompat.getColor(mItemsBinding.cvLaundryServicesServices.getContext(), R.color.md_theme_light_onPrimary);
            int colorNotChecked = ContextCompat.getColor(mItemsBinding.cvLaundryServicesServices.getContext(), R.color.md_theme_light_scrim);

            if (selectionTracker.isSelected(position))
                {
                mItemsBinding.ivLaundryServices.setColorFilter(colorChecked);
                mItemsBinding.tvLaundryServicesContentTitle.setTextColor(colorChecked);
                mItemsBinding.tvLaundryServicesSalary.setTextColor(colorChecked);
                } else
                {
                mItemsBinding.ivLaundryServices.setColorFilter(colorNotChecked);
                mItemsBinding.tvLaundryServicesContentTitle.setTextColor(colorNotChecked);
                mItemsBinding.tvLaundryServicesSalary.setTextColor(colorNotChecked);
                }

            }


        ItemDetailsLookup.ItemDetails<Long> getItemDetails()
            {
            return details;
            }

        }  */


    }
