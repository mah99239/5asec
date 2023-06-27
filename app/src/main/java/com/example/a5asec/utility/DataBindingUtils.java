package com.example.a5asec.utility;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.AdapterViewFlipper;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.databinding.BindingAdapter;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.a5asec.R;
import com.example.a5asec.data.model.api.Banner;
import com.example.a5asec.data.model.api.Category;
import com.example.a5asec.ui.adapters.BannersAdapter;
import com.example.a5asec.ui.adapters.base.BaseAdapter;
import com.facebook.shimmer.Shimmer;
import com.facebook.shimmer.ShimmerDrawable;

import java.util.List;

import timber.log.Timber;

/**
 * @noinspection rawtypes
 */
public class DataBindingUtils
    {

    @BindingAdapter("adapterFlipper")
    public static void setAdapterFlipper(AdapterViewFlipper adapterFlipper, BannersAdapter adapter)
        {
        adapterFlipper.setAdapter(adapter);
        adapterFlipper.setFlipInterval(4000);
        adapterFlipper.startFlipping();
        adapterFlipper.setHorizontalScrollBarEnabled(true);

        }

    @BindingAdapter("dataFlipper")
    public static void setDataFlipper(AdapterViewFlipper adapterViewFlipper, @Nullable List<Banner> bannersList)
        {
        var adapter = (BannersAdapter) adapterViewFlipper.getAdapter();
        adapter.addBanners(bannersList);
        adapterViewFlipper.setAdapter(adapter);
        }

    @BindingAdapter(value = {"setAdapter"})
    public static void setAdapter(RecyclerView recyclerView,
                                  BaseAdapter<ViewDataBinding, Category> adapter)
        {

        recyclerView.setAdapter(adapter);

        }

    @BindingAdapter("setDataAtAdapter")
    public static void setDataAtAdapter(RecyclerView recyclerView, @Nullable List<Category> categoriesList)
        {
        var adapter = (BaseAdapter) recyclerView.getAdapter();
        adapter.updateData(categoriesList);
        }

    @BindingAdapter(value = {"loadImage", "baseImage"}, requireAll = false)
    public static void loadImage(@NonNull ImageView imageView, String imageURL, Drawable baseImage)
        {
        if (imageURL == null)
            {
            imageView.setImageDrawable(baseImage);
            } else
            {
            try
                {
                String url;
                if (!imageURL.contains("https://"))
                    {
                    url = "https://" + imageURL; /* URL of Image */

                    } else
                    {
                    url = imageURL;
                    }
                Glide.with(imageView)
                        .setDefaultRequestOptions(new RequestOptions()
                                .circleCrop())
                        .load(url)
                        .fitCenter()
                        .placeholder(getShimmerDrawables(imageView.getContext()))
                        .error(getShimmerDrawables(imageView.getContext()))
                        .fallback(getShimmerDrawables(imageView.getContext()))
                        .into(imageView);
                } catch (Exception e)
                {
                Timber.e(e);
                }
            }

        }

    private static ShimmerDrawable getShimmerDrawables(Context context)
        {
        var shimmer = new Shimmer.ColorHighlightBuilder()
                .setHighlightColor(ContextCompat.getColor(context,
                        R.color.md_theme_light_surfaceVariant))

                .setDuration(500L) // how long the shimmering animation takes to do one full sweep
                .setBaseAlpha(0.6f) //the alpha of the underlying children
                .setHighlightAlpha(0.7f) // the shimmer alpha amount
                .setDirection(Shimmer.Direction.LEFT_TO_RIGHT)
                .setAutoStart(true)
                .build();

        var shimmerDrawables = new ShimmerDrawable();
        shimmerDrawables.setShimmer(shimmer);
        return shimmerDrawables;
        }
    }
