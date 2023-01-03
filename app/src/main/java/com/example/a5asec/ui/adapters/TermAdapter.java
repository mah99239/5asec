package com.example.a5asec.ui.adapters;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.a5asec.R;
import com.example.a5asec.data.model.api.Terms;
import com.example.a5asec.databinding.ListItemTermsBinding;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import lombok.val;

public class TermAdapter extends RecyclerView.Adapter<TermAdapter.TermViewHolder>
    {

    private final List<Terms> mTerms;

    public TermAdapter()
        {
        mTerms = new ArrayList<>();
        }

    @NotNull
    public TermViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
        {
        ListItemTermsBinding listItemBinding = DataBindingUtil.inflate(LayoutInflater
                        .from(parent.getContext()),
                R.layout.list_item_terms, parent, false);
        return new TermViewHolder(listItemBinding);
        }

    @Override
    public void onBindViewHolder(@NonNull TermViewHolder holder, int position)
        {

        if (getItemCount() == 0) return;
        Terms item = mTerms.get(position);
        holder.bind(item);
        }





    @Override
    public int getItemCount()
        {

        return mTerms.size();
        }

    @SuppressLint("NotifyDataSetChanged")
    public void addTerms(@NonNull List<Terms> terms)
        {
        mTerms.addAll(terms);
        notifyDataSetChanged();
        }

    public static class TermViewHolder extends RecyclerView.ViewHolder
        {

        ListItemTermsBinding mItemTermsBinding;

        public TermViewHolder(@NonNull ListItemTermsBinding itemTermsBinding)
            {
            super(itemTermsBinding.getRoot());
            this.mItemTermsBinding = itemTermsBinding;

            }

        private void bind(Terms terms)
            {
            val language = AppCompatDelegate.getApplicationLocales().toLanguageTags();

            String name = terms.getName(language);
            String content = terms.getContent(language);

            mItemTermsBinding.tvItemName.setText(name);
            mItemTermsBinding.tvItemContent.setText(content);
            }
        }
    }



