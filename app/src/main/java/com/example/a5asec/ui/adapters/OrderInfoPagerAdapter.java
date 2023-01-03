package com.example.a5asec.ui.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.a5asec.ui.view.home.OrderInfoFragment;
import com.example.a5asec.ui.view.home.OrderStatusFragment;

public class OrderInfoPagerAdapter extends FragmentStateAdapter
    {
    private final int ITEMS_PAGE_SIZE = 2;

    public OrderInfoPagerAdapter(@NonNull Fragment fragment)
        {
        super(fragment);
        }


    @NonNull
    @Override
    public Fragment createFragment(int position)
        {
        if (position == 1)
            {
            return new OrderInfoFragment();
            }
        return new OrderStatusFragment();

        }

    @Override
    public int getItemCount()
        {
        return ITEMS_PAGE_SIZE;
        }
    }


