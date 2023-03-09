package com.example.a5asec.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import androidx.databinding.DataBindingUtil;

import com.example.a5asec.R;
import com.example.a5asec.data.model.api.Banners;
import com.example.a5asec.databinding.ListItemBannersBinding;

import java.util.ArrayList;
import java.util.List;

public class BannersAdapter extends BaseAdapter
    {
    private final ArrayList<Banners> mBanners = new ArrayList<>();

    public BannersAdapter()
        {
       // mBanners = new ArrayList<>();
        }

    public void addBanners(List<Banners> banners)
        {

        if (banners != null)
            {
            mBanners.clear();
            mBanners.addAll(banners);
            notifyDataSetChanged();
            }
        }

    @Override
    public int getCount()
        {
        return mBanners.size();
        }

    @Override
    public Banners getItem(int position)
        {
        return mBanners.get(position);
        }

    @Override
    public long getItemId(int position)
        {
        return 0;
        }


    @Override
    public View getView(int position, View convertView, ViewGroup parent)
        {

        ListItemBannersBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.list_item_banners, parent, false);

        Banners banners = mBanners.get(position);
        binding.setBanner(banners);
        // binding.setVariable(BR.banner,banners);
        binding.executePendingBindings();

        return binding.getRoot();

        }
    }
