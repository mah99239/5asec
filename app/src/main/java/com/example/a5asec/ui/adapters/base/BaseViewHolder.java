package com.example.a5asec.ui.adapters.base;



import android.view.MotionEvent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.selection.ItemDetailsLookup;
import androidx.recyclerview.widget.RecyclerView;

public class BaseViewHolder<Binding extends ViewDataBinding> extends RecyclerView.ViewHolder
    {


    Binding mBinder;

    public BaseViewHolder(@NonNull Binding binder)
        {
        super(binder.getRoot());

        this.mBinder = binder;
       // mBinder.getRoot().setOnClickListener(this);

        }

    public  int getCurrentPosition()
        {

        return super.getAdapterPosition();
        }






    }
