package com.example.a5asec.ui.adapters;

import static androidx.databinding.DataBindingUtil.inflate;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;

import com.example.a5asec.R;
import com.example.a5asec.data.model.api.Category;
import com.example.a5asec.databinding.ListItemServicesBinding;

import java.util.ArrayList;
import java.util.List;

public class ServicesAdapter extends BaseAdapter
    {
    private final List<Category.ItemsEntity> mItemsCategory = new ArrayList<>();


    public ServicesAdapter()
        {
        //Run
        }


    @Override
    public int getCount()
        {
        return mItemsCategory.size();
        }

    /**
     * Get the data item associated with the specified position in the data set.
     *
     * @param position Position of the item whose data we want within the adapter's
     *                 data set.
     * @return The data at the specified position.
     */
    @Override
    public Object getItem(int position)
        {
        return null;
        }


    @Override
    public long getItemId(int position)
        {
        return position;
        }


    public void updateServices(@NonNull List<Category.ItemsEntity> category)
        {
        mItemsCategory.clear();
        mItemsCategory.addAll(category);
        notifyDataSetChanged();
        }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
        {

        String languageTags = AppCompatDelegate.getApplicationLocales().toLanguageTags();

        ListItemServicesBinding binding = inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.list_item_services, parent, false);


        if (mItemsCategory.isEmpty()) return convertView;
        var itemCategory = mItemsCategory.get(position);
        binding.setItemCategory(itemCategory);
        binding.setLanguage(languageTags);

        return binding.getRoot();
        }

    }



