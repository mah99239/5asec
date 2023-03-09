package com.example.a5asec.ui.adapters.base;



import androidx.annotation.NonNull;
import androidx.databinding.ViewDataBinding;
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

    public final int getCurrentPosition()
        {

        return super.getAdapterPosition();
        }



    }
