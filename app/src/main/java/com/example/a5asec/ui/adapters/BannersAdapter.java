package com.example.a5asec.ui.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import androidx.core.content.ContextCompat;
import androidx.databinding.BindingAdapter;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.a5asec.R;
import com.example.a5asec.data.model.api.Banners;
import com.example.a5asec.databinding.ListItemBannersBinding;
import com.facebook.shimmer.Shimmer;
import com.facebook.shimmer.ShimmerDrawable;

import java.util.ArrayList;
import java.util.List;

public class BannersAdapter extends BaseAdapter
    {
    private static final String TAG = "BannersAdapter";
    private Fragment mFragment;
    private ArrayList<Banners> mBanners;

    public BannersAdapter(Fragment fragment)
        {
        this.mFragment = fragment;
        mBanners = new ArrayList<>();
        }

    public void addBanners(List<Banners> banners)
        {

        mBanners.addAll(banners);
        notifyDataSetChanged();
        }

    @Override
    public int getCount()
        {
        return mBanners.size();
        }

    @Override
    public Object getItem(int position)
        {
        return null;
        }

    @Override
    public long getItemId(int position)
        {
        return 0;
        }

    @BindingAdapter("loadImage")
    public void loadImage(ImageView imageView, String url)
        {
        Glide
                .with(mFragment)
                .load(url)
                .fitCenter()
                .into(imageView);
        }
    @Override
    public View getView(int position, View convertView, ViewGroup parent)
        {

        Banners banners = mBanners.get(position);
        ListItemBannersBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.list_item_banners, parent, false);
        convertView = binding.getRoot();

     /*    var shimmer = new Shimmer.ColorHighlightBuilder()
                .setHighlightColor(ContextCompat.getColor(convertView.getContext(),
                        R.color.md_theme_light_tertiaryContainer))
                .setBaseColor(ContextCompat.getColor(convertView.getContext(),
                        R.color.md_theme_light_primaryContainer))

                .setDuration(4000L) // how long the shimmering animation takes to do one full sweep
                .setBaseAlpha(0.6f) //the alpha of the underlying children
                .setHighlightAlpha(0.7f) // the shimmer alpha amount
                .setDirection(Shimmer.Direction.LEFT_TO_RIGHT)
                .setAutoStart(true)
                .build();

        var shimmerDrawables = new ShimmerDrawable();
        shimmerDrawables.setShimmer(shimmer); */
        https://www.youtube.com/watch?v=mTdlhVZ_1DM
           // binding.se
        try
            {
            ImageView imageView = binding.imvBanners;
            String url = banners.getUrl();
         /*    Glide
                    .with(mFragment)
                    .load(url)
                    .fitCenter()
                    .placeholder(shimmerDrawables)
                    .error(shimmerDrawables)
                    .fallback(shimmerDrawables)
                    .into(imageView); */
            } catch (Exception e)
            {
            e.printStackTrace();
            Log.e(TAG, e.getMessage());
            }
        return convertView;
        }
    }
