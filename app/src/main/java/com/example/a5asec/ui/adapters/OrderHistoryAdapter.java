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
import com.example.a5asec.data.model.api.Order;
import com.example.a5asec.databinding.ListItemOrderHistoryBinding;

import java.util.ArrayList;
import java.util.List;

public class OrderHistoryAdapter extends RecyclerView.Adapter<OrderHistoryAdapter.OrderHistoryViewHolder>
    {

    private static final String TAG = OrderHistoryAdapter.class.getSimpleName();

    private List<Order> mHistoryItem = new ArrayList<>();
    private OnItemClickListener onItemClickListener;

    public OrderHistoryAdapter()
        {
        }

    public OrderHistoryAdapter( OnItemClickListener onItemClickListener)
        {

        this.onItemClickListener = onItemClickListener;
        }

    public void setOnClickListener(OnItemClickListener listener)
        {
        this.onItemClickListener = listener;
        }

    @NonNull
    @Override
    public OrderHistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
        {

        ListItemOrderHistoryBinding ListItemOrderHistoryBinding = DataBindingUtil.inflate(LayoutInflater
                        .from(parent.getContext()),
                R.layout.list_item_order_history, parent, false);
        return new OrderHistoryViewHolder(ListItemOrderHistoryBinding);
        }

    @Override
    public void onBindViewHolder(@NonNull OrderHistoryViewHolder holder, int position)
        {
        Order item = mHistoryItem.get(position);
        holder.bind(item);
        }

    @Override
    public int getItemCount()
        {
        return mHistoryItem.size();
        }

    public void setData(List<Order> newList)
        {
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new DiffCallback(newList, this.mHistoryItem));
        this.mHistoryItem.clear();
        this.mHistoryItem.addAll(newList);
        diffResult.dispatchUpdatesTo(this);
        }
    public Order getHistoryOrderItem(int position)
        {
        return mHistoryItem.get(position);
        }
    public interface OnItemClickListener
        {
        void onItemClick(View view, int position);
        }

    public class OrderHistoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
        {

        ListItemOrderHistoryBinding mItemBinding;

        public OrderHistoryViewHolder(ListItemOrderHistoryBinding itemView)
            {
            super(itemView.getRoot());

            this.mItemBinding = itemView;
            }

        public void bind(final Order model)
            {
            mItemBinding.getRoot().setOnClickListener(this);
            var id =  model.getId();
            String idOrder = "#" +  String.valueOf(id);
            String date = model.getCreatedDate();
            var count =String.valueOf(model.getItemsCount());
            String status = model.getStatus();

            mItemBinding.tvOrderHistoryId.setText(idOrder);
            mItemBinding.tvOrderHistoryDate.setText(date);
            mItemBinding.tvOrderHistoryCount.setText(count);
            mItemBinding.tvOrderHistoryStatus.setText(status);



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
        List<Order> oldTodoList;
        List<Order> newTodoList;

        public DiffCallback(List<Order> newTodoList, List<Order> oldTodoList)
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