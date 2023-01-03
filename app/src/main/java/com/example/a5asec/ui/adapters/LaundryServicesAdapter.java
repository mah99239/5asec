package com.example.a5asec.ui.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.selection.ItemDetailsLookup;
import androidx.recyclerview.selection.ItemKeyProvider;
import androidx.recyclerview.selection.SelectionTracker;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.RequestManager;
import com.example.a5asec.R;
import com.example.a5asec.data.model.api.Category;
import com.example.a5asec.databinding.ListItemLaundryServicesBinding;
import com.facebook.shimmer.Shimmer;
import com.facebook.shimmer.ShimmerDrawable;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.shape.CornerFamily;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import lombok.val;


public class LaundryServicesAdapter extends RecyclerView.Adapter<LaundryServicesAdapter.LaundryServicesViewHolder>
    {
    private static final String TAG = "LaundryServicesAdapter";
    private final Set<Category.ItemServicesEntity> checkedChipId;
    private final RequestManager mGlide;
    private final Fragment mFragment;
    private SelectionTracker<Long> selectionTracker;
    private List<Category.ItemServicesEntity> mItemServiceEntity;

    public LaundryServicesAdapter(Fragment fragment, RequestManager glide)
        {
        mItemServiceEntity = new ArrayList<>();
        checkedChipId = new HashSet<>();
        this.mFragment = fragment;
        mGlide = glide;
        }


    public Set<Category.ItemServicesEntity> getCheckedChipId()
        {
        return checkedChipId;
        }

    public void setCheckedChipId(List<Integer> id)
        {
        try
            {

            id.forEach(integer ->
                {
                var item = getItemsIdService(integer);
                checkedChipId.add(item);
                selectionTracker.setItemsSelected(List.of((long) getPostion(item)), true);

                Log.e(TAG, String.valueOf(getItemsIdService(integer)));
                });

            }catch (Exception e)
            {
            e.getMessage();
            }
        }

    @NotNull
    public LaundryServicesAdapter.LaundryServicesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
        {
        ListItemLaundryServicesBinding itemLaundryServicesBinding =
                DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                        R.layout.list_item_laundry_services, parent, false);

        return new LaundryServicesViewHolder(itemLaundryServicesBinding);
        }


    @Override
    public void onBindViewHolder(@NonNull LaundryServicesAdapter.LaundryServicesViewHolder holder, int position)
        {

        var baseItem = mItemServiceEntity.get(position);
        holder.bind(baseItem, position);


        }

    public Iterator<Long> getSelectionTracker()
        {
        return selectionTracker.getSelection().iterator();
        }

    public void setSelectionTracker(SelectionTracker<Long> selectionTracker)
        {
        this.selectionTracker = selectionTracker;
        }

    public void addItemService(Category.ItemsEntity item)
        {
        setItemService(item);
        }

    private void setItemService(Category.ItemsEntity category)
        {
        mItemServiceEntity.add(new Category.ItemServicesEntity(category.getId(), category.getCost(), null));
        mItemServiceEntity.addAll(category.getItemServices());
        }

    @Override
    public int getItemCount()
        {
        if (mItemServiceEntity.size() == 0) return 0;
        return mItemServiceEntity.size();

        }

    public Category.ItemServicesEntity getItemsId(int position)
        {
        return mItemServiceEntity.get(position);
        }
    public Category.ItemServicesEntity getItemsIdService(int id)
        {
        return mItemServiceEntity.stream().skip(1).
                filter(itemServicesEntity -> itemServicesEntity.getId() == id).findFirst().get();

        }
    public int getPostion(Category.ItemServicesEntity item)
        {

        return mItemServiceEntity.indexOf(item);

        }

    public static class ItemsDetailsLookup extends ItemDetailsLookup<Long>
        {

        private final RecyclerView recyclerView;

        public ItemsDetailsLookup(RecyclerView recyclerView)
            {
            this.recyclerView = recyclerView;
            }

        @Nullable
        @Override
        public ItemDetails<Long> getItemDetails(@NonNull MotionEvent e)
            {
            View view = recyclerView.findChildViewUnder(e.getX(), e.getY());
            if (view != null)
                {
                RecyclerView.ViewHolder viewHolder = recyclerView.getChildViewHolder(view);
                if (viewHolder instanceof LaundryServicesViewHolder)
                    {
                    return ((LaundryServicesViewHolder) viewHolder).getItemDetails();
                    }
                }
            return null;
            }

        }

    public static class ItemsKeyProvider extends ItemKeyProvider<Long>
        {
        LaundryServicesAdapter mAdapter;

        /**
         * Creates a new provider with the given scope.
         *
         * @param adapter Scope can't be changed at runtime.
         */
        public ItemsKeyProvider(LaundryServicesAdapter adapter)
            {
            super(ItemKeyProvider.SCOPE_CACHED);
            mAdapter = adapter;
            }

        @Nullable
        @Override
        public Long getKey(int position)
            {
            return (long) position;

            }

        @Override
        public int getPosition(@NonNull Long key)
            {
            long value = key;
            return (int) value;
            }
        }

    static class Details extends ItemDetailsLookup.ItemDetails<Long>
        {

        long position;

        Details()
            {
            }

        @Override
        public int getPosition()
            {
            return (int) position;
            }

        @Nullable
        @Override
        public Long getSelectionKey()
            {
            return position;
            }

        @Override
        public boolean inSelectionHotspot(@NonNull MotionEvent e)
            {
            return false;
            }

        @Override
        public boolean inDragRegion(@NonNull MotionEvent e)
            {
            return true;
            }
        }

    public class LaundryServicesViewHolder extends RecyclerView.ViewHolder
        {
        public static final int BASE_SERVICE_ITEM = 0;
        private final Details details;
        private final MaterialCardView mCard;
        private final ShapeableImageView imageService;
        private final TextView textTitle;
        private final TextView textSalary;
        ListItemLaundryServicesBinding mItemsBinding;


        public LaundryServicesViewHolder(@NonNull ListItemLaundryServicesBinding itemLaundryServicesBinding)
            {
            super(itemLaundryServicesBinding.getRoot());
            this.mItemsBinding = itemLaundryServicesBinding;

            mCard = mItemsBinding.cvLaundryServicesServices;
            imageService = mItemsBinding.ivLaundryServices;
            textTitle = mItemsBinding.tvLaundryServicesTitle;
            textSalary = mItemsBinding.tvLaundryServicesSalary;

            details = new Details();


            }


        public void bind(Category.ItemServicesEntity item, int position)
            {
            val languageTags = AppCompatDelegate.getApplicationLocales().toLanguageTags();

            setupCornerImage();
            details.position = position;
       //     mCard.setTag(BASE_SERVICE_ITEM);

            String labelCost = mFragment.getString(R.string.all_cost_label);
            String cost;
            String name;

            if (position == BASE_SERVICE_ITEM)
                {

                imageService.setImageResource(R.drawable.ic_order);
                cost = item.getCost() + " " + labelCost;
                name = mFragment.getString(R.string.laundry_Service_base_name);
                } else
                {
                String getUrl = item.getLaundryService().getIconUrl();
                cost = item.getCost() + " " + labelCost;
                String url;
                if(!getUrl.contains("5asec-ksa.com/icons/laundry-service/"))
                    {
                    url = "https://5asec-ksa.com/icons/laundry-service/" + getUrl;
                    }else  url = "https://" + getUrl;
                name = item.getLaundryService().getName(languageTags);
                setupLoadImage(url);

                }
            textTitle.setText(name);
            textSalary.setText(cost);

            if (selectionTracker != null)
                {
                bindSelectedState();
                }

            selectionTracker.setItemsSelected(List.of((long) BASE_SERVICE_ITEM), true);

            bindSelectedData();


            }

        private void setupCornerImage()
            {
            var radius = mFragment.getResources().getDimension(R.dimen.default_corner_radius);
            imageService.setShapeAppearanceModel(imageService.getShapeAppearanceModel()
                    .toBuilder()
                    .setBottomLeftCorner(CornerFamily.ROUNDED, radius)
                    .setBottomRightCorner(CornerFamily.ROUNDED, radius).build());


            }

        private void setupLoadImage(String currentUrl)
            {
            var shimmer = new Shimmer.ColorHighlightBuilder()
                    .setHighlightColor(ContextCompat.getColor(mFragment.requireContext(),
                            R.color.md_theme_light_inversePrimary))
                    .setBaseColor(ContextCompat.getColor(mFragment.requireContext(),
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


            mGlide
                    .load(currentUrl)
                    .placeholder(shimmerDrawables)
                    .error(shimmerDrawables)
                    .fallback(shimmerDrawables)
                    .fitCenter()
                    .into(imageService);
            }

        private void bindSelectedState()
            {
            mCard.setChecked(selectionTracker.isSelected(details.getSelectionKey()));
            changeColorInView(details.getSelectionKey());

            }

        private void bindSelectedData()
            {
            if (selectionTracker.isSelected(details.getSelectionKey())
                    && details.getSelectionKey() > BASE_SERVICE_ITEM)
                {

                checkedChipId.add(getItemsId(details.getPosition()));
              //  mCard.setChecked(true);
                } else if (details.getPosition() > BASE_SERVICE_ITEM)
                {
             //   mCard.setChecked(false);
                checkedChipId.remove(getItemsId(details.getPosition()));

                }
            }

        private void changeColorInView(long position)
            {
            int colorChecked = ContextCompat.getColor(mCard.getContext(), R.color.md_theme_light_onPrimary);
            int colorNotChecked = ContextCompat.getColor(mCard.getContext(), R.color.md_theme_light_scrim);

            if (selectionTracker.isSelected(position))
                {
                imageService.setColorFilter(colorChecked);
                textTitle.setTextColor(colorChecked);
                textSalary.setTextColor(colorChecked);
                } else
                {
                imageService.setColorFilter(colorNotChecked);
                textTitle.setTextColor(colorNotChecked);
                textSalary.setTextColor(colorNotChecked);
                }

            }


        ItemDetailsLookup.ItemDetails<Long> getItemDetails()
            {
            return details;
            }

        }


    }
