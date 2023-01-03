package com.example.a5asec.ui.adapters;


import android.annotation.SuppressLint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.a5asec.R;
import com.example.a5asec.data.model.api.Category;
import com.example.a5asec.databinding.ListItemPriceBinding;
import com.facebook.shimmer.Shimmer;
import com.facebook.shimmer.ShimmerDrawable;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.shape.CornerFamily;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import lombok.val;


/**
 * Author MahmoudZ
 * The {@link PriceAdapter is adapte data in price api catagory}
 */
public class PriceAdapter extends RecyclerView.Adapter<PriceAdapter.PriceViewHolder>
    {
    private static final String TAG = "PriceAdapter";
    private final Fragment mFragment;
    ItemClickListener mClickListener;
    private List<Category> mCategoryList;

    public PriceAdapter(Fragment fragment)
        {
        this.mFragment = fragment;
        mCategoryList = new ArrayList<>();
        }

    @NotNull
    public PriceAdapter.PriceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
        {


        ListItemPriceBinding itemPriceBinding = DataBindingUtil.inflate(LayoutInflater
                        .from(parent.getContext()),
                R.layout.list_item_price, parent, false);

        return new PriceAdapter.PriceViewHolder(itemPriceBinding);
        }

    @Override
    public void onBindViewHolder(@NonNull PriceViewHolder holder, int position)
        {

        var item = mCategoryList.get(position);
        holder.bind(item);
        }

    public List<Category.ItemsEntity> getItemEntity(int position)
        {
        return mCategoryList.get(position).getItems();
        }

    public void addCategory(List<Category> category)
        {
        mCategoryList.addAll(category);
        notifyDataSetChanged();
        }

    @SuppressLint("NotifyDataSetChanged")
    public void clear()
        {
        mCategoryList.clear();
        notifyDataSetChanged();
        }

    @Override
    public int getItemCount()
        {
        if (mCategoryList.size() == 0) return 0;
        return mCategoryList.size();
        }

    public void setClickListener(ItemClickListener itemClickListener)
        {
        this.mClickListener = itemClickListener;
        }

    public interface ItemClickListener
        {
        void onItemClick(View view, int position);
        }

    public class PriceViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
        {

        private ListItemPriceBinding mItemPriceBinding;
        private TextView mNameCategoryTextView;
        private ShapeableImageView mShapeableImageView;

        public PriceViewHolder(@NonNull ListItemPriceBinding itemPriceBinding)
            {
            super(itemPriceBinding.getRoot());
            this.mItemPriceBinding = itemPriceBinding;
            itemPriceBinding.getRoot().setOnClickListener(this);

            mNameCategoryTextView = mItemPriceBinding.tvItemPrice;
            mShapeableImageView = mItemPriceBinding.ivItemPrice;
            var radius = itemPriceBinding.ivItemPrice.getContext().getResources()
                    .getDimension(R.dimen.default_corner_radius);
            mShapeableImageView.setShapeAppearanceModel(
                    mShapeableImageView.getShapeAppearanceModel()
                            .toBuilder()
                            .setBottomLeftCorner(CornerFamily.ROUNDED, radius)
                            .setBottomRightCorner(CornerFamily.ROUNDED, radius)
                            .build());
            }

        void bind(@NonNull final Category category)
            {
            val language = AppCompatDelegate.getApplicationLocales().toLanguageTags();
            var context = mItemPriceBinding.getRoot().getContext();
            var shimmer = new Shimmer.ColorHighlightBuilder()
                    .setHighlightColor(ContextCompat.getColor(context,
                            R.color.md_theme_light_inversePrimary))
                    .setBaseColor(ContextCompat.getColor(context,
                            R.color.md_theme_light_primaryContainer))

                    .setDuration(4000L) // how long the shimmering animation takes to do one full sweep
                    .setBaseAlpha(0.6f) //the alpha of the underlying children
                    .setHighlightAlpha(0.7f) // the shimmer alpha amount
                    .setDirection(Shimmer.Direction.LEFT_TO_RIGHT)
                    .setAutoStart(true)
                    .build();

            var shimmerDrawables = new ShimmerDrawable();
            shimmerDrawables.setShimmer(shimmer);

            String name = category.getName(language);
            try
                {
                String url = "https://" + category.getIconUrl() /* URL of Image */;

                Glide.with(mFragment)

                        .load(url)
                        .placeholder(shimmerDrawables)
                        .error(shimmerDrawables)
                        .fallback(shimmerDrawables)
                        .fitCenter()
                        .into(mShapeableImageView);
                } catch (Exception e)
                {
                e.printStackTrace();
                Log.e(TAG, e.getMessage());
                }
            mNameCategoryTextView.invalidateDrawable(shimmerDrawables);
            mNameCategoryTextView.setText(name);
            }

        @Override
        public void onClick(View v)
            {

            if (mClickListener != null) mClickListener.onItemClick(v, getLayoutPosition());


            }
        }


    }
