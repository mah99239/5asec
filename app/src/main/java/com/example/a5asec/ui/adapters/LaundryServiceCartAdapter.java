package com.example.a5asec.ui.adapters;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.a5asec.R;
import com.example.a5asec.data.model.db.LaundryService;
import com.example.a5asec.data.model.db.ServiceAndLaundryService;
import com.example.a5asec.databinding.ListItemLaundryServicesBinding;
import com.facebook.shimmer.Shimmer;
import com.facebook.shimmer.ShimmerDrawable;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.shape.CornerFamily;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import lombok.val;

public class LaundryServiceCartAdapter extends RecyclerView.Adapter<LaundryServiceCartAdapter.LaundryServiceCartViewHolder>
    {
    private final List<LaundryService> mItemLaundryService;
    CartAdapter.RvClickListener mClickListener;
    int mPosition;
    public LaundryServiceCartAdapter(int position)
        {
        mItemLaundryService = new ArrayList<>();
        this.mPosition = position;
        }

    public void setData(ServiceAndLaundryService item)
        {
        setItemLaundryService(item);

        }

    private void setItemLaundryService(ServiceAndLaundryService item)
        {
        int id = item.getService().getIdItemService();
        int cost = item.getService().getCostItemService();
        var nameEn = item.getService().getNameEn();
        var nameAr = item.getService().getNameAr();

        mItemLaundryService.add(new LaundryService(id, cost, null, nameEn, nameAr));
        mItemLaundryService.addAll(item.getLaundryServices());
        }


    @NotNull
    public LaundryServiceCartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
        {
        ListItemLaundryServicesBinding itemLaundryServicesBinding =
                DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                        R.layout.list_item_laundry_services, parent, false);

        return new LaundryServiceCartViewHolder(itemLaundryServicesBinding);
        }


    public void setClickListener(CartAdapter.RvClickListener itemClickListener)
        {
        this.mClickListener = itemClickListener;
        }

    @Override
    public int getItemCount()
        {
        return mItemLaundryService.size();
        }


    @Override
    public void onBindViewHolder(@NonNull LaundryServiceCartViewHolder holder, int position)
        {

        var baseItem = mItemLaundryService.get(position);
        holder.bind(baseItem, position);


        }

    public  final class LaundryServiceCartViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
        {
        public static final int BASE_SERVICE_ITEM = 0;
        private final ShapeableImageView imageService;
        private final TextView textTitle;
        private final TextView textSalary;
        ListItemLaundryServicesBinding mItemsBinding;


        public LaundryServiceCartViewHolder(@NonNull ListItemLaundryServicesBinding itemLaundryServicesBinding)
            {
            super(itemLaundryServicesBinding.getRoot());
            this.mItemsBinding = itemLaundryServicesBinding;

            imageService = mItemsBinding.ivLaundryServices;
            textTitle = mItemsBinding.tvLaundryServicesContentTitle;
            textSalary = mItemsBinding.tvLaundryServicesSalary;


            }


        public void bind(LaundryService item, int position)
            {
            val density = Resources.getSystem().getDisplayMetrics().density;
            mItemsBinding.getRoot().setOnClickListener(this);

            var context = mItemsBinding.getRoot().getContext();
            var width = context.getResources().getDimension(R.dimen.height_item_laundry_services_card);

            mItemsBinding.cvLaundryServicesServices.getLayoutParams().width = (int) (width);
            val languageTags = AppCompatDelegate.getApplicationLocales().toLanguageTags();

            setupCornerImage(context);

            String labelCost = context.getString(R.string.all_cost_label);
            String cost;
            String name;
            if (position == BASE_SERVICE_ITEM)
                {

                imageService.setImageResource(R.drawable.ic_order);
                cost = item.getCost() + " " + labelCost;
                name = context.getString(R.string.laundry_Service_base_name);
                } else
                {
                String getUrl = item.getIconUrl();
                cost = item.getCost() + " " + labelCost;
                String url;
                if (!getUrl.contains("5asec-ksa.com/icons/laundry-service/"))
                    {
                    url = "https://5asec-ksa.com/icons/laundry-service/" + getUrl;
                    } else url = "https://" + getUrl;

                name = item.getName(languageTags);
                setupLoadImage(url, context);

                }
            textTitle.setText(name);
            textSalary.setText(cost);


            }

        private void setupCornerImage(Context context)
            {
            var radius = context.getResources().getDimension(R.dimen.default_corner_radius);
            imageService.setShapeAppearanceModel(imageService.getShapeAppearanceModel()
                    .toBuilder()
                    .setBottomLeftCorner(CornerFamily.ROUNDED, radius)
                    .setBottomRightCorner(CornerFamily.ROUNDED, radius).build());


            }

        private void setupLoadImage(String currentUrl, Context context)
            {
            var shimmer = new Shimmer.ColorHighlightBuilder()
                    .setHighlightColor(ContextCompat.getColor(context,
                            R.color.md_theme_light_inversePrimary))
                    .setBaseColor(ContextCompat.getColor(context,
                            R.color.md_theme_light_primaryContainer))

                    .setDuration(2000L) // how long the shimmering animation takes to do one full sweep
                    .setBaseAlpha(0.6f) //the alpha of the underlying children
                    .setHighlightAlpha(0.7f) // the shimmer alpha amount
                    .setDirection(Shimmer.Direction.LEFT_TO_RIGHT)
                    .setAutoStart(true)
                    .build();
            var shimmerDrawables = new ShimmerDrawable();
            shimmerDrawables.setShimmer(shimmer);
            shimmerDrawables.startShimmer();


            Glide.with(mItemsBinding.getRoot())
                    .load(currentUrl)
                    .placeholder(shimmerDrawables)
                    .error(shimmerDrawables)
                    .fallback(shimmerDrawables)
                    .fitCenter()
                    .into(imageService);
            }


        @Override
        public void onClick(View v)
            {
            if (mClickListener != null) mClickListener.onItemClick(v,mPosition );

            }
        }


    }
