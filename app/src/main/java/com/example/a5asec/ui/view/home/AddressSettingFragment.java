package com.example.a5asec.ui.view.home;

import static com.google.android.material.snackbar.BaseTransientBottomBar.LENGTH_LONG;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavBackStackEntry;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import com.example.a5asec.R;
import com.example.a5asec.databinding.FragmentSettingAddressBinding;
import com.example.a5asec.ui.view.viewmodel.AddressViewModel;
import com.example.a5asec.utility.Status;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;

import lombok.val;

public class AddressSettingFragment extends BottomSheetDialogFragment
    {
    public static final String TAG = "AddressSettingFragment";
    private BottomSheetDialog mBottomSheetDialog;
    private FragmentSettingAddressBinding mBinding;
    private AddressViewModel mAddressViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
        {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_setting_address, container, false);
        View view = mBinding.getRoot();
        // Set up BottomSheetDialog
        mBottomSheetDialog = (BottomSheetDialog) getDialog();
        mBottomSheetDialog.setDismissWithAnimation(true);
        mBottomSheetDialog.getBehavior().setHideable(true);//Important to add

        mBottomSheetDialog.getBehavior().setState(BottomSheetBehavior.STATE_EXPANDED);
        return view;
        }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
        {
        super.onViewCreated(view, savedInstanceState);

        setupClickedButtons();
        setupViewModel();

        }

    private void setupClickedButtons()
        {
        mBinding.btnSettingAddressEdit.setOnClickListener(v ->
            {
            showEditAddress();
            this.dismiss();

            });

        mBinding.btnSettingAddressDelete.setOnClickListener(v ->
            {
            mBottomSheetDialog.hide();

            showDialogCheckRemoveItem();
            });

        mBinding.btnSettingAddressCancel.setOnClickListener(v ->
                this.dismiss());
        }

    private void showEditAddress()
        {
        val EDIT_ADDRESS = 3;
        String title = getString(R.string.add_address_update_tittle);

        var action
                = AddressSettingFragmentDirections.actionAddressSettingFragmentToNewAddressFragment(title);
        action.setNavHomeAddressArgCheck(EDIT_ADDRESS);

        Navigation.findNavController(requireParentFragment().requireView()).navigate(action);
        }

    private void showDialogCheckRemoveItem()
        {

        new MaterialAlertDialogBuilder(requireContext())
                .setTitle(getString(R.string.setting_address_dialog_title))
                .setMessage(getString(R.string.setting_address_dialog_message))
                .setPositiveButton(getString(R.string.setting_address_dialog_ok), (dialog, which) ->
                        setupDeleteAddress())
                .setNegativeButton(getString(R.string.setting_address_dialog_cancel), (dialog, which) ->
                    {
                    dialog.dismiss();
                    mBottomSheetDialog.dismiss();
                    })
                .show();
        }


    private void setupDeleteAddress()
        {
        mAddressViewModel.deleteAddress().observe(this, responseEntityResource ->
            {
            if (responseEntityResource.mStatus == Status.SUCCESS)
                {
                String message = getString(R.string.setting_address_delete_message);
                messageSnackbar(message);
                Log.e(TAG, "SUCCESS = " + responseEntityResource.getMData());
                mBottomSheetDialog.dismiss();
                } else if (responseEntityResource.mStatus == Status.ERROR)
                {
                showDialogNotRemoveItem();
                Log.e(TAG, "ERROR" + responseEntityResource.getMMessage());
                }
            });

        }

    private void showDialogNotRemoveItem()
        {

        new MaterialAlertDialogBuilder(requireContext())
                .setTitle(getString(R.string.setting_address_error_dialog_title))
                .setMessage(getString(R.string.setting_address_error_dialog_message))
                .setPositiveButton(getString(R.string.setting_address_dialog_done), (dialog, which) ->
                    {
                    dialog.dismiss();
                    mBottomSheetDialog.dismiss();

                    })
                .show();
        }

    private void messageSnackbar(@NonNull String message)
        {

        Snackbar.make(requireParentFragment().requireView(), message, LENGTH_LONG)
                .show();


        }


    private void setupViewModel()
        {
        NavController navController = NavHostFragment.findNavController(this);
        NavBackStackEntry backStackEntry = navController.getBackStackEntry(R.id.nav_home_manage_address);
        mAddressViewModel = new ViewModelProvider(backStackEntry).get(AddressViewModel.class);

        }

    @Override
    public void onDestroy()
        {
        super.onDestroy();
        mBinding = null;
        mBottomSheetDialog = null;
        }
    }

