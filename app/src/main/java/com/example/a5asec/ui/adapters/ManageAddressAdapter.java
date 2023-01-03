package com.example.a5asec.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.a5asec.R;
import com.example.a5asec.data.model.api.Address;
import com.example.a5asec.databinding.ListItemAddressBinding;

import java.util.ArrayList;
import java.util.List;

public class ManageAddressAdapter extends RecyclerView.Adapter<ManageAddressAdapter.ManageAddressViewHolder>
    {

    private static final String TAG = ManageAddressAdapter.class.getSimpleName();

    private List<Address> list = new ArrayList<>();
    private OnItemClickListener onItemClickListener;

    public ManageAddressAdapter()
        {
        }

    public ManageAddressAdapter(List<Address> list, OnItemClickListener onItemClickListener)
        {

        this.list = list;
        this.onItemClickListener = onItemClickListener;
        }

    public void setOnClickListener(OnItemClickListener listener)
        {
        this.onItemClickListener = listener;
        }

    @Override
    public ManageAddressViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
        {

        ListItemAddressBinding ListItemAddressBinding = DataBindingUtil.inflate(LayoutInflater
                        .from(parent.getContext()),
                R.layout.list_item_address, parent, false);
        return new ManageAddressViewHolder(ListItemAddressBinding);
        }

    @Override
    public void onBindViewHolder(@NonNull ManageAddressViewHolder holder, int position)
        {
        Address item = list.get(position);
        holder.bind(item);
        }

    @Override
    public int getItemCount()
        {
        return list.size();
        }

    public void updateList(List<Address> newList)
        {
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new DiffCallback(newList, this.list));
        this.list.clear();
        this.list.addAll(newList);
        diffResult.dispatchUpdatesTo(this);
        }

    public interface OnItemClickListener
        {
        void onItemClick(View view, int position);
        }

    public class ManageAddressViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
        {

        ListItemAddressBinding mListItemAddressBinding;

        public ManageAddressViewHolder(ListItemAddressBinding itemView)
            {
            super(itemView.getRoot());

            this.mListItemAddressBinding = itemView;
            }

        public void bind(final Address model)
            {
            mListItemAddressBinding.getRoot().setOnClickListener(this);

            }

        @Override
        public void onClick(View v)
            {

            if (onItemClickListener != null)
                onItemClickListener.onItemClick(v, getLayoutPosition());


            }
        }

    public class DiffCallback extends DiffUtil.Callback
        {
        List<Address> oldTodoList;
        List<Address> newTodoList;

        public DiffCallback(List<Address> newTodoList, List<Address> oldTodoList)
            {
            this.newTodoList = newTodoList;
            this.oldTodoList = oldTodoList;
            }

        @Override
        public int getOldListSize()
            {
            return oldTodoList.size();
            }

        @Override
        public int getNewListSize()
            {
            return newTodoList.size();
            }

        @Override
        public boolean areItemsTheSame(int oldItemPosition, int newItemPosition)
            {
            return oldTodoList.get(oldItemPosition).equals(newTodoList.get(newItemPosition));
            }

        @Override
        public boolean areContentsTheSame(int oldItemPosition, int newItemPosition)
            {
            return oldTodoList.get(oldItemPosition) == newTodoList.get(newItemPosition);
            }

        @Nullable
        @Override
        public Object getChangePayload(int oldItemPosition, int newItemPosition)
            {
            //you can return particular field for changed item.
            return super.getChangePayload(oldItemPosition, newItemPosition);
            }
        }

    }