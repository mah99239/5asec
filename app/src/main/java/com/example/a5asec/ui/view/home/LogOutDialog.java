package com.example.a5asec.ui.view.home;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.example.a5asec.R;
import com.example.a5asec.data.local.prefs.TokenPreferences;
import com.example.a5asec.ui.view.login.MainActivity;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint

public class LogOutDialog extends DialogFragment
{
    @Inject
    TokenPreferences tokenPreferences;

    public LogOutDialog()
    {
/*
        ((MvvmApp) getActivity().getApplication()).getPreferencesComponent().inject((MainActivity) getActivity());
*/

// Empty constructor required for DialogFragment
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        String tittle = getString(R.string.log_out_tittle);
        String message = getString(R.string.log_out_message);
        String positive = getString(R.string.log_out_positive);
        String negative = getString(R.string.log_out_Negative);

        MaterialAlertDialogBuilder alertDialogBuilder = new MaterialAlertDialogBuilder(
                requireContext());
        alertDialogBuilder.setTitle(tittle);
        alertDialogBuilder.setMessage(message);
        alertDialogBuilder.setPositiveButton(positive, (dialog, which) -> {
            tokenPreferences.resetToken();
            startActivity(new Intent(getContext(), MainActivity.class));
            requireActivity().finish();
        });
        alertDialogBuilder.setNegativeButton(negative, (dialog, which) -> {
            if (dialog != null) {
                dialog.dismiss();
            }
        });

        return alertDialogBuilder.create();
    }

}
