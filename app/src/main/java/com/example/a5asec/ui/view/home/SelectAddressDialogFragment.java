package com.example.a5asec.ui.view.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.a5asec.databinding.FragmentSelectAddressDialogListDialogBinding;
import com.example.a5asec.databinding.ListItemAddressBinding;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;


/**
 * <p>A fragment that shows a list of items as a modal bottom sheet.</p>
 * <p>You can show this modal bottom sheet from your activity like this:</p>
 * <pre>
 *     SelectAddressDialogFragment.newInstance(30).show(getSupportFragmentManager(), "dialog");
 * </pre>
 */
public class SelectAddressDialogFragment extends BottomSheetDialogFragment
    {

    private static final String ARG_ITEM_COUNT = "item_count";
    private FragmentSelectAddressDialogListDialogBinding binding;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState)
        {

        binding = FragmentSelectAddressDialogListDialogBinding.inflate(inflater, container, false);
        return binding.getRoot();

        }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
        {
        final RecyclerView recyclerView = (RecyclerView) view;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(new ItemAdapter(getArguments().getInt(ARG_ITEM_COUNT)));
        }

    @Override
    public void onDestroyView()
        {
        super.onDestroyView();
        binding = null;
        }

    private class ViewHolder extends RecyclerView.ViewHolder
        {


        ViewHolder(ListItemAddressBinding binding)
            {
            super(binding.getRoot());
            }

        }

    private class ItemAdapter extends RecyclerView.Adapter<ViewHolder>
        {

        private final int mItemCount;

        ItemAdapter(int itemCount)
            {
            mItemCount = itemCount;
            }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
            {

            return new ViewHolder(ListItemAddressBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));

            }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position)
            {
            }

        @Override
        public int getItemCount()
            {
            return mItemCount;
            }

        }
    }