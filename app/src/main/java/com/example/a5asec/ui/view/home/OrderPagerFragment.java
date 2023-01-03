package com.example.a5asec.ui.view.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.example.a5asec.R;
import com.example.a5asec.databinding.FragmentOrderBinding;
import com.example.a5asec.ui.adapters.OrderPagerAdapter;
import com.google.android.material.tabs.TabLayout;

public class OrderPagerFragment extends Fragment
    {
    private FragmentOrderBinding mBinding;
    TabLayout mTabLayout;
    ViewPager2 mViewPager;
    OrderPagerAdapter mAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
        {
        // Inflate the layout for this fragment
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_order, container, false);
        View view = mBinding.getRoot();

        mTabLayout = mBinding.tlOrder;
        mViewPager = mBinding.vpOrder;
        mAdapter = new OrderPagerAdapter(this);
        mViewPager.setAdapter(mAdapter);

        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener()
            {
            @Override
            public void onTabSelected(TabLayout.Tab tab)
                {
                mViewPager.setCurrentItem(tab.getPosition());
                }

            @Override
            public void onTabUnselected(TabLayout.Tab tab)
                {

                }

            @Override
            public void onTabReselected(TabLayout.Tab tab)
                {

                }
            });
        mViewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback()
            {
            @Override
            public void onPageSelected(int position)
                {
                super.onPageSelected(position);
                mTabLayout.getTabAt(position).select();
                }
            });
        return view;
        }
    }