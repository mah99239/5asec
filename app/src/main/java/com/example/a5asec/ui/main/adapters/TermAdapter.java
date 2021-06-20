package com.example.a5asec.ui.main.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;
import com.example.a5asec.R;
import com.example.a5asec.databinding.ListItemBinding;
import com.example.a5asec.data.model.Terms;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import org.jetbrains.annotations.NotNull;

public class TermAdapter extends RecyclerView.Adapter<TermAdapter.TermViewHolder>
{

  private List<Terms> mTerms = new ArrayList<>();

  @NotNull public TermViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
  {
    ListItemBinding listItemBinding = DataBindingUtil.inflate(LayoutInflater
                                                                  .from(parent.getContext()),
                                                              R.layout.list_item, parent, false);
    return new TermViewHolder(listItemBinding);
  }

  @Override public void onBindViewHolder(@NonNull TermViewHolder holder, int position)
  {

    holder.mListItemBinding.tvItemName.setText(getNameLanguage(position));
    holder.mListItemBinding.tvItemContent.setText(getContentsLanguage(position));
  }

  public String getContentsLanguage(int position)
  {
    if (Locale.getDefault().getLanguage().equals("ar"))
    {
      return mTerms.get(position).getContentsAr();
    } else
    {
      return mTerms.get(position).getContentsEn();
    }
  }

  public String getNameLanguage(int position)
  {
    if (Locale.getDefault().getLanguage().equals("ar"))
    {
      return mTerms.get(position).getNameAr();
    } else
    {
      return mTerms.get(position).getNameEn();
    }
  }

  /**
   * Returns the total number of items in the data set held by the adapter.
   *
   * @return The total number of items in this adapter.
   */
  @Override public int getItemCount()
  {
    if (mTerms == null) return 0;
    return mTerms.size();
  }

  public void setList(List<Terms> terms)
  {
    this.mTerms = terms;
    notifyDataSetChanged();
  }

  public static class TermViewHolder extends RecyclerView.ViewHolder
  {

    ListItemBinding mListItemBinding;

    public TermViewHolder(@NonNull ListItemBinding listItemBinding)
    {
      super(listItemBinding.getRoot());
      this.mListItemBinding = listItemBinding;

    }
  }
}

