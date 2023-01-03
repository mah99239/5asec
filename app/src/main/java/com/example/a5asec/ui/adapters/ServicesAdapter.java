package com.example.a5asec.ui.adapters;

import static androidx.databinding.DataBindingUtil.inflate;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;

import com.example.a5asec.R;
import com.example.a5asec.data.model.api.Category;
import com.example.a5asec.databinding.ListItemServicesBinding;

import java.util.ArrayList;
import java.util.List;

public class ServicesAdapter extends BaseAdapter
    {
    private List<Category.ItemsEntity> mItemsCategory;
    private Fragment mFragment;

    public ServicesAdapter(Fragment fragment)

        {
        mItemsCategory = new ArrayList<>();
        mFragment = fragment;
        }


    @Override
    public int getCount()
        {
        return mItemsCategory.size();
        }

    @Override
    public Category.ItemsEntity getItem(int position)
        {
        return mItemsCategory.get(position);
        }

    @Override
    public long getItemId(int position)
        {
        return position;
        }

    /**
     * called when item's clicked in list
     *
     * @param position pass item positions
     * @return item of services
     */
    public Category.ItemServicesEntity setItemBase(int position)
        {
        int id = mItemsCategory.get(position).getId();
        int cost = mItemsCategory.get(position).getCost();
        return new Category.ItemServicesEntity(id, cost, null);

        }

    public Category.ItemsEntity getItemService(int position)
        {
       /*  var mItemsBase = new ArrayList<Category.ItemServicesEntity>();
        mItemsBase.add(setItemBase(position));

        mItemsBase.addAll(mItemsCategory.get(position).getItemServices()); */

        return mItemsCategory.get(position);
        }

    public void addServices(@NonNull List<Category.ItemsEntity> category)
        {

        mItemsCategory.addAll(category);
        notifyDataSetChanged();
        }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
        {

        String mLanguage = AppCompatDelegate.getApplicationLocales().toLanguageTags();

        ListItemServicesBinding binding = inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.list_item_services, parent, false);
        convertView = binding.getRoot();

        if (mItemsCategory.size() == 0) return convertView;

        int costInt = mItemsCategory.get(position).getCost();
        String name = mItemsCategory.get(position).getName(mLanguage);
        String labelCost = mFragment.getContext().getString(R.string.all_cost_label);

        String cost = costInt + labelCost;

        if (!name.isEmpty() && !name.equals("string"))
            {
            binding.tvItemServicesName.setText(name);
            binding.tvItemServicesPrice.setText(cost);
            binding.ivItemServices.setImageResource(R.drawable.ic_menu_price);
            }

        return convertView;
        }

    }



