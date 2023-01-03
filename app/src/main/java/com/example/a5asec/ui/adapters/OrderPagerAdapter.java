package com.example.a5asec.ui.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.a5asec.ui.view.home.CurrentOrderFragment;
import com.example.a5asec.ui.view.home.HistoryOrderFragment;

public class OrderPagerAdapter extends FragmentStateAdapter
    {
    private final int ITEMS_PAGE_SIZE = 2;

    public OrderPagerAdapter(@NonNull Fragment fragment)
        {
        super(fragment);
        }


    @NonNull
    @Override
    public Fragment createFragment(int position)
        {
            switch(position)
                {
                case 1:
                    return new HistoryOrderFragment();
                    default:
                        return new CurrentOrderFragment();
                }

        }

    @Override
    public int getItemCount()
        {
        return ITEMS_PAGE_SIZE;
        }
    }
