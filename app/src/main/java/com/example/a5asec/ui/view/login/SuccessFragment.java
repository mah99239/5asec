package com.example.a5asec.ui.view.login;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import com.example.a5asec.R;
import com.example.a5asec.databinding.FragmentSuccessBinding;

/**
 *
 * second UI for success register user.
 */
public class SuccessFragment extends Fragment {


FragmentSuccessBinding mBinding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
    mBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_success, container, false);

    return mBinding.getRoot();
    }

@Override
public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
    super.onViewCreated(view, savedInstanceState);
    mBinding.btnSuccess.setOnClickListener(v ->
        {

        NavDirections action =
                SuccessFragmentDirections.actionNavLoginSuccessFragmentToForgetPasswordFragment();

        Navigation.findNavController(requireView()).navigate(action);
        });
    }
@Override
public void onDestroy()
    {
    super.onDestroy();
    mBinding = null;
    }

}