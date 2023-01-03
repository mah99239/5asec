package com.example.a5asec.ui.view.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.a5asec.R;


public class CurrentOrderFragment extends Fragment
    {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
        {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_current_order, container, false);
        }
    }