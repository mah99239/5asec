package com.example.a5asec.ui.adapters;


import android.annotation.SuppressLint;

import androidx.appcompat.app.AppCompatDelegate;

import com.example.a5asec.R;
import com.example.a5asec.data.model.api.Category;
import com.example.a5asec.databinding.ListItemPriceBinding;
import com.example.a5asec.ui.adapters.base.BaseAdapter;
import com.example.a5asec.ui.adapters.base.ItemClickListener;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;


/**
 * Author MahmoudZ
 * The {@link PriceAdapter is adapte data in price api catagory}
 */
public class PriceAdapter extends BaseAdapter<ListItemPriceBinding, Category>
    {
    private static final String TAG = "PriceAdapter";
    private final List<Category> mCategoryList = new ArrayList<>();//cannot refer to new ArrayList.

    public PriceAdapter()
        {
        super.mLayoutId = R.layout.list_item_price;
        }

    @Override
    public void updateData(List<Category> list)
        {
        if (list != null)
            {
            mCategoryList.clear();
            mCategoryList.addAll(list);
            super.updateData(mCategoryList);

            }
        Timber.tag(TAG).e("size of item = %s", getItemCount());
        }


    @SuppressLint("NotifyDataSetChanged")
    public void clear()
        {
        mCategoryList.clear();
        notifyDataSetChanged();
        }


    @Override
    public void bind(ListItemPriceBinding mItemPriceBinding, Category category)
        {
        var language = AppCompatDelegate.getApplicationLocales().toLanguageTags();
        mItemPriceBinding.setLanguage(language);
        mItemPriceBinding.setCategory(category);
        mItemPriceBinding.setListener(getItemClickListener());
        mItemPriceBinding.setPosition(getPosition());
        mItemPriceBinding.executePendingBindings();
        }

    @Override
    public int getItemCount()
        {
        if (mCategoryList.isEmpty()) return 0;
        return mCategoryList.size();
        }

    @Override
    public void setClickListener(ItemClickListener itemClickListener)
        {
        super.setClickListener(itemClickListener);
        }


    }
