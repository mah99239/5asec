package com.example.a5asec.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.a5asec.R;
import com.example.a5asec.data.model.db.ServiceAndLaundryService;
import com.example.a5asec.databinding.ListItemCartBinding;

import java.util.ArrayList;
import java.util.List;

import lombok.val;


public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder>
    {
    private static final String TAG = "CartAdapter";
    private RvClickListener onClickListenerNested;
    private List<ServiceAndLaundryService> mItems = new ArrayList<>();
    private ArrayList<Integer> mTotalService;

    public CartAdapter()
        {
        }

    public void setOnclickListener(RvClickListener clickHandler)
        {
        this.onClickListenerNested = clickHandler;
        }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
        {
        int layoutIdForListItem = R.layout.list_item_cart;
        boolean shouldAttachToParentImmediately = false;
        ListItemCartBinding listItemCartBinding = DataBindingUtil.inflate(LayoutInflater
                        .from(parent.getContext()),
                layoutIdForListItem, parent, shouldAttachToParentImmediately);

        return new CartViewHolder(listItemCartBinding, onClickListenerNested);
        }


    @Override
    public void onBindViewHolder(CartViewHolder viewHolder, int position)
        {
        var dataToBind = getItemByPosition(position);
        viewHolder.bind(dataToBind, position);

        }


    @Override
    public int getItemCount()
        {
        return mItems.size();
        }



    public void setData(List<ServiceAndLaundryService> data)
        {
        mItems = data;
        notifyDataSetChanged();

        }

    public void removeItem(int position)
        {
        mItems.remove(position);
        notifyItemRemoved(position);
        }

    public void restoreItem(ServiceAndLaundryService item, int position)
        {
        mItems.add(position, item);
        notifyItemInserted(position);
        }

    public ServiceAndLaundryService getItemByPosition(int position)
        {
        return mItems.get(position);
        }


    public void setTotalService(ArrayList<Integer> total)
        {
        mTotalService = total;
        notifyDataSetChanged();
        }


    public interface RvClickListener
        {
        void onItemClick(View view, int position);

        void onItemLongClick(int position);

        }

    public class CartViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
        {
        private final ListItemCartBinding mBinding;
        private final RvClickListener mRvClickListener;


        public CartViewHolder(ListItemCartBinding itemView, RvClickListener recycleViewInterface)
            {
            super(itemView.getRoot());
            this.mBinding = itemView;
            mRvClickListener = recycleViewInterface;

            }


        public void bind(ServiceAndLaundryService item, int position)
            {
            mBinding.getRoot().setOnClickListener(CartViewHolder.this);
            setupOnLongClick();

            val languageTags = AppCompatDelegate.getApplicationLocales().toLanguageTags();

            var context = mBinding.getRoot().getContext();
            var nameTextView = mBinding.tvCartNameService;
            var countTextView = mBinding.tvCartCount;
            var totalTextView = mBinding.tvCartTotalService;


            var name = item.getService().getName(languageTags);

            var count = context.getString(R.string.cart_text_count) + " " + item.getService().getCount();
            var costService = getTotalService(position) + " " + context.getString(R.string.all_cost_label);

            setupNestedRecycleView(item);
            nameTextView.setText(name);
            countTextView.setText(count);
            totalTextView.setText(costService);

            }


        private void setupNestedRecycleView(ServiceAndLaundryService item)
            {
            RecyclerView.RecycledViewPool
                    viewPool
                    = new RecyclerView
                    .RecycledViewPool();
            var laundryServiceRecycleView = mBinding.rvCartService;
            RecyclerView.ItemDecoration itemDecoration = new
                    DividerItemDecoration(mBinding.rvCartService.getContext(), DividerItemDecoration.HORIZONTAL);
            laundryServiceRecycleView.addItemDecoration(itemDecoration);
            LinearLayoutManager layoutManager
                    = new LinearLayoutManager(
                    laundryServiceRecycleView.getContext(),
                    LinearLayoutManager.HORIZONTAL,
                    false);
            layoutManager
                    .setInitialPrefetchItemCount(
                            item.getLaundryServices().size());

            LaundryServiceCartAdapter laundryServiceCartAdapter =
                    new LaundryServiceCartAdapter(getAdapterPosition());
            laundryServiceCartAdapter.setClickListener(mRvClickListener);
            laundryServiceCartAdapter.setData(item);
            laundryServiceRecycleView.setLayoutManager(layoutManager);
            laundryServiceRecycleView.setAdapter(laundryServiceCartAdapter);
            laundryServiceRecycleView.setRecycledViewPool(viewPool);

            }

        private Integer getTotalService(int pos)
            {
            return mTotalService.get(pos);
            }

        public void onClick(View v)
            {
            if (mRvClickListener != null)
                {
                int pos = getAdapterPosition();
                if (pos != RecyclerView.NO_POSITION)
                    {
                    mRvClickListener.onItemClick(v, pos);
                    }
                }

            }

        private void setupOnLongClick()
            {
            mBinding.getRoot().setOnLongClickListener(v ->
                {
                if (mRvClickListener != null)
                    {
                    int pos = getAdapterPosition();
                    if (pos != RecyclerView.NO_POSITION)
                        {
                        mRvClickListener.onItemLongClick(pos);
                        }
                    }
                return true;
                });
            }
        }
    }