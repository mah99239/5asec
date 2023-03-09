package com.example.a5asec.ui.adapters.base;


import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;


import java.util.List;

import lombok.Getter;
import timber.log.Timber;

public abstract class BaseAdapter<Binding extends ViewDataBinding, T>
        extends RecyclerView.Adapter<BaseViewHolder<Binding>>
    {

    @Getter
    ItemClickListener itemClickListener;

    @LayoutRes
    public int mLayoutId;
    private List<T> data;
    @Getter public  int position;

    public BaseAdapter()
        {

        }
    public  void setClickListener(ItemClickListener itemClickListener)
        {
        this.itemClickListener = itemClickListener;
        }

    public abstract void bind(Binding binding, T item);

    @SuppressLint("NotifyDataSetChanged")
    public void updateData(List<T> list)
        {
        this.data = list;
        notifyDataSetChanged();

        }

    @Override
    public long getItemId(int position)
        {
        return super.getItemId(position);
        }

    @NonNull
    @Override
    public BaseViewHolder<Binding> onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
        {
        Timber.e("layout id = %s", mLayoutId);
        Binding binder =
                DataBindingUtil.inflate(LayoutInflater
                                .from(parent.getContext()),
                        mLayoutId, parent, false);
        return new BaseViewHolder<Binding>(binder);
        }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder<Binding> holder, int position)
        {
        bind(holder.mBinder, data.get(position));
        holder.mBinder.getRoot().setOnClickListener(v ->
            {


                this.position = holder.getAdapterPosition();
                if (this.position != RecyclerView.NO_POSITION)
                    {
                    itemClickListener.onItemClick(v, this.position);
                    }

            });
        }

    @Override
    public int getItemCount()
        {
        return data.size();
        }


    }
