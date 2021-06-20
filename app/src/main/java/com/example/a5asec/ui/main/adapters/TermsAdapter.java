package com.example.a5asec.ui.main.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import com.example.a5asec.R;
import com.example.a5asec.databinding.ListItemBinding;
import com.example.a5asec.data.model.Terms;
import java.util.List;

public class TermsAdapter extends ArrayAdapter<Terms>
{
  ListItemBinding mListItemBinding; // get view from layout.
  /**
   * Constructor
   *  @param context  The current context.
   * @param resource The resource ID for a layout file containing a TextView to use when
   */
  public TermsAdapter(@NonNull Context context, int resource, List<Terms>objects )
  {
    super(context, resource,objects);
  }

  @NonNull @Override public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent)
  {

    if(convertView == null)
    {
      //convertView = ((Activity) getContext()).getLayoutInflater().inflate(R.layout.list_item, parent, false);
      mListItemBinding = DataBindingUtil.setContentView((Activity) getContext(), R.layout.list_item);
    //  convertView=mListItemBinding.getRoot();
    }
    Terms terms = getItem(position);
    mListItemBinding.tvItemName.setText(terms.getNameEn());
    for(int i =0; i <= terms.getContentsSize(); i++)
      mListItemBinding.tvItemContent.setText((CharSequence) terms.contents.get(i));

    return mListItemBinding.getRoot();
  }
}
