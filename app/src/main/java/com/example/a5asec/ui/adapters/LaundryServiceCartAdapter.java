package com.example.a5asec.ui.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.a5asec.R;
import com.example.a5asec.data.model.db.LaundryService;
import com.example.a5asec.data.model.db.ServiceAndLaundryService;
import com.example.a5asec.databinding.ListItemCartLaundryServicesBinding;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;


public class LaundryServiceCartAdapter extends
                                       ListAdapter<LaundryService,
                                               LaundryServiceCartAdapter.LaundryServiceCartViewHolder>
{

    private static final String TAG = "LaundryServiceCartAdapt";

    public LaundryServiceCartAdapter()
    {
        super(laundryServiceCartCallback());

    }

    @NonNull
    private static DiffUtil.ItemCallback<LaundryService> laundryServiceCartCallback()
    {
        return new DiffUtil.ItemCallback<>()
        {
            @Override
            public boolean areItemsTheSame(@NonNull LaundryService oldItem,
                                           @NonNull LaundryService newItem)
            {
                return oldItem.getId() == newItem.getId();
            }

            @Override
            public boolean areContentsTheSame(@NonNull LaundryService oldItem,
                                              @NonNull LaundryService newItem)
            {
                return oldItem.equals(newItem);
            }

        };
    }

    public void setData(ServiceAndLaundryService item)
    {
        setItemLaundryService(item);

    }


    private void setItemLaundryService(ServiceAndLaundryService item)
    {
        List<LaundryService> mItemLaundryService = new ArrayList<>();

        int id = item.getService().getIdItemService();
        int cost = item.getService().getCostItemService();
        var nameEn = item.getService().getNameEn();
        var nameAr = item.getService().getNameAr();

        mItemLaundryService.add(new LaundryService(id, cost, null, nameEn, nameAr));
        mItemLaundryService.addAll(item.getLaundryServiceWithFlagZero());
        Timber.tag(TAG).e("laundryServiceCart size =  " + mItemLaundryService.size());
        submitList(mItemLaundryService);
    }


    @NonNull
    public LaundryServiceCartViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                                           int viewType)
    {
        ListItemCartLaundryServicesBinding itemLaundryServicesBinding =
                DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                        R.layout.list_item_cart_laundry_services, parent, false);

        return new LaundryServiceCartViewHolder(itemLaundryServicesBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull LaundryServiceCartViewHolder holder, int position)
    {
        var baseItem = getItem(holder.getAbsoluteAdapterPosition());
        holder.bind(baseItem, position);

    }


    public final class LaundryServiceCartViewHolder extends RecyclerView.ViewHolder
    {
        private static final int BASE_SERVICE_ITEM = 0;
        private static final int ANY_SERVICE_ITEM = 1;

        ListItemCartLaundryServicesBinding mItemsBinding;


        public LaundryServiceCartViewHolder(
                @NonNull ListItemCartLaundryServicesBinding itemLaundryServicesBinding)
        {
            super(itemLaundryServicesBinding.getRoot());
            this.mItemsBinding = itemLaundryServicesBinding;

        }

        public void bind(LaundryService item, int position)
        {
            var languageTags = AppCompatDelegate.getApplicationLocales().toLanguageTags();
            mItemsBinding.setPosition(position);
            //  mItemsBinding.setListener(mClickListener);
            mItemsBinding.setItemService(item);
            mItemsBinding.setLanguage(languageTags);

            if (position == BASE_SERVICE_ITEM) {
                mItemsBinding.setPosition(BASE_SERVICE_ITEM);
            } else {
                mItemsBinding.setPosition(ANY_SERVICE_ITEM);
            }


        }


    }

}
