package com.example.a5asec.ui.view.login;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.a5asec.R;
import com.example.a5asec.data.model.api.Terms;
import com.example.a5asec.data.remote.api.TermsClient;
import com.example.a5asec.data.remote.api.TermsHelper;
import com.example.a5asec.databinding.FragmentTermsBinding;
import com.example.a5asec.ui.adapters.TermAdapter;
import com.example.a5asec.ui.base.TermsViewModelFactory;
import com.example.a5asec.ui.view.viewmodel.TermsViewModel;
import com.example.a5asec.utility.NetworkConnection;
import com.example.a5asec.utility.Resource;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

public class TermsFragment extends Fragment implements LifecycleObserver
    {

    private static final String TAG = "TermsFragment";
  private   TermAdapter mAdapter;
    private TermsViewModel mTermsViewModel;
    private FragmentTermsBinding mBinding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
        {
        // Inflate the layout for this fragment
        mBinding =
                DataBindingUtil.inflate(inflater, R.layout.fragment_terms, container, false);

        return mBinding.getRoot();
        }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
        {
        super.onViewCreated(view, savedInstanceState);
        setupUI();
        }

    private void setupUI()
        {
        setupAdapter();
        checkConnectionsToInitTerms();
        }

    private void setupAdapter()
        {
        mAdapter = new TermAdapter();

        mBinding.rvTerms.setLayoutManager(new LinearLayoutManager(getContext()));
        mBinding.rvTerms.setAdapter(mAdapter);
        }

    private void checkConnectionsToInitTerms()
        {
        NetworkConnection networkConnection = new NetworkConnection( requireActivity());
        networkConnection.observe(getViewLifecycleOwner(), isConnected ->
            {
            // if internet not connect, Show message try connection internet
            if (!isConnected)
                {
                //TODO: ADD view if  Network error

                }
            // internet is connect, initialize terms.
            else
                {
                initTerms();

                }
            });
        }

    private void initTerms()
        {
        setupViewModel();
        setupObserver();
        }

    private void setupObserver()
        {


        mTermsViewModel.getTerms().observe(getViewLifecycleOwner(), termsObserve ->
            {
            Log.e(TAG, String.valueOf(termsObserve.mStatus));
            switch (termsObserve.mStatus)
                {

                case SUCCESS -> {
                Log.e(TAG, "SUCCESS");
                renderList(termsObserve);
                mBinding.cpiTerms.setVisibility(View.GONE);
                }
                case LOADING -> {
                Log.e(TAG, "LOADING");
                mBinding.cpiTerms.setVisibility(View.VISIBLE);
                }
                case ERROR -> {
                Log.e(TAG, "ERROR");
                //Handle Error
                //TODO: ADD view if  Terms retrun  error

                mBinding.cpiTerms.setVisibility(View.VISIBLE);
                onError("message : " + termsObserve.mMessage);
                }
                }
            });


        }

    @SuppressLint("NotifyDataSetChanged")
    private void renderList(@NonNull Resource<List<Terms>> terms)
        {
        Log.e(TAG, String.valueOf(terms.getMData()));
        mAdapter.addTerms(terms.getMData());
        mAdapter.notifyDataSetChanged();
        }


    private void setupViewModel()
        {
        mTermsViewModel = new ViewModelProvider(this,
                new TermsViewModelFactory(new TermsHelper(new TermsClient()))).get(TermsViewModel.class);
        }

    //TODO: improve UI with add view to show data is null and not connection internet
    public void onError(String message)
        {
        Snackbar.make(mBinding.getRoot(), message, BaseTransientBottomBar.LENGTH_LONG)
                //  .setAnchorView(getView().getBottom())
                .setAction(getResources().getString(R.string.all_network_button), v ->
                        restartFragment())
                .show();
        }

    private void restartFragment()
        {



        }

    @Override
    public void onDestroy()
        {
        super.onDestroy();
        mBinding = null;
        }

    }
