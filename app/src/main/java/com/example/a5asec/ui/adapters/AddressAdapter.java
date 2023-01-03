package com.example.a5asec.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.a5asec.R;
import com.example.a5asec.data.model.api.Address;
import com.example.a5asec.databinding.ListItemAddressBinding;

import java.util.ArrayList;
import java.util.List;

import lombok.val;

public class AddressAdapter extends RecyclerView.Adapter<AddressAdapter.AddressViewHolder>
    {

    private static final String TAG = AddressAdapter.class.getSimpleName();

    private List<Address> mListAddress = new ArrayList<>();
    private OnItemClickListener onItemClickListener;

    public AddressAdapter()
        {
        }


    @NonNull
    @Override
    public AddressViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
        {

        ListItemAddressBinding ListItemAddressBinding = DataBindingUtil.inflate(LayoutInflater
                        .from(parent.getContext()),
                R.layout.list_item_address, parent, false);
        return new AddressViewHolder(ListItemAddressBinding);
        }

    @Override
    public void onBindViewHolder(@NonNull AddressViewHolder holder, int position)
        {
        Address item = mListAddress.get(position);

        holder.bind(item, position);

        }
    public Address getItemAddress(int position)
        {
        return mListAddress.get(position);
        }
    @Override
    public int getItemCount()
        {
        return mListAddress.size();
        }


    public void setOnClickListener(OnItemClickListener listener)
        {
        this.onItemClickListener = listener;
        }

    public void setNewData(List<Address> newList)
        {
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new DiffCallback(newList, this.mListAddress));
        this.mListAddress.clear();
        setData(newList);
        diffResult.dispatchUpdatesTo(this);
        }

    public void setData(List<Address> list)
        {
        var citySupport = "Jeddah";
        list.forEach(address ->
            {
            String city = address.getArea().getCity().getNameEn();

            if (city.equals(citySupport))
                {
                mListAddress.add(address);
                }
            });

        }

    public interface OnItemClickListener
        {
        void onItemClick(View view, int position);
        void onItemSettingClick(View view, int position);
        void onItemShowAddressClick(View view, int position);
        }
    private int getIdAddress(int pos)
        {
      return   mListAddress.get(pos).getId();
        }
    public class AddressViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
        {

        ListItemAddressBinding mItemBinding;

        public AddressViewHolder(@NonNull ListItemAddressBinding itemView)
            {
            super(itemView.getRoot());

            this.mItemBinding = itemView;
            }

        public void bind(final Address model, int position)
            {
            mItemBinding.getRoot().setOnClickListener(this);

            var context = mItemBinding.getRoot().getContext();
            val language = AppCompatDelegate.getApplicationLocales().toLanguageTags();
            String address;
            String city = model.getArea().getCity().getName(language);
            String area = model.getArea().getName(language);
            String street = model.getStreet();
            var isPrimary = model.isPrimary();

                if (isPrimary)
                    {
                    address = context.getString(R.string.address_primary_address);
                    } else
                    {
                    address = context.getString(R.string.address_other_address) + position;
                    }

                mItemBinding.tvAddressTittle.setText(address);
                mItemBinding.tvAddressCity.setText(city);
                mItemBinding.tvAddressArea.setText(area);
                mItemBinding.tvAddressStreet.setText(street);

            setupClick();

            }

        private void setupClick()
            {
         //   mItemBinding.btnAddressSetting.setOnClickListener(se);

            mItemBinding.btnAddressSetting.setOnClickListener(v ->
                    onItemClickListener.onItemSettingClick(v,getLayoutPosition()));

            mItemBinding.btnAddressLocation.setOnClickListener(v ->

                onItemClickListener.onItemShowAddressClick(v,getLayoutPosition()));





            }

        @Override
        public void onClick(View v)
            {

            if (onItemClickListener != null)
                {
                onItemClickListener.onItemClick(v, getLayoutPosition());
                }
            }








        }

     class DiffCallback extends DiffUtil.Callback
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