package com.example.a5asec.ui.adapters.base;


import android.annotation.SuppressLint;
import android.view.LayoutInflater;
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

    @LayoutRes
    public int mLayoutId;
    @Getter
    public int position;
    @Getter
    ItemClickListener itemClickListener;
    private List<T> listData;
    private T data;

    public BaseAdapter()
        {

        }

    public void setClickListener(ItemClickListener itemClickListener)
        {
        this.itemClickListener = itemClickListener;
        }

    public abstract void bind(Binding binding, T item);

    @SuppressLint("NotifyDataSetChanged")
    public void updateData(List<T> list)
        {
        this.listData = list;
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
        return new BaseViewHolder<>(binder);
        }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder<Binding> holder, int position)
        {
        bind(holder.mBinder, listData.get(position));
        holder.mBinder.getRoot().setOnClickListener(v ->
            {


            this.position = holder.getCurrentPosition();
            if (this.position != RecyclerView.NO_POSITION)
                {
                itemClickListener.onItemClick(v, holder.getCurrentPosition());
                }

            });
        }

    @Override
    public int getItemCount()
        {
        return listData.size();
        }


    }
