package com.example.a5asec.ui.view.home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.navigation.Navigation;

import com.example.a5asec.R;
import com.example.a5asec.databinding.FragmentSettingAddressBinding;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import lombok.val;

public class AddressSettingFragment extends BottomSheetDialogFragment
    {
    public static final String TAG = "AddressSettingFragment";
    private FragmentSettingAddressBinding mBinding;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
        {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_setting_address, container, false);
        View view = mBinding.getRoot();
        // Set up BottomSheetDialog
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(requireContext());


        bottomSheetDialog.setDismissWithAnimation(true);
        bottomSheetDialog.getBehavior().setState(BottomSheetBehavior.STATE_EXPANDED);

        Log.e(TAG, "my tag = " + getTag());
        return view;
        }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
        {
        super.onViewCreated(view, savedInstanceState);
        setupClickedButtons();


            }
    private void setupClickedButtons()
        {
        mBinding.btnSettingAddressEdit.setOnClickListener(v ->
            {
            showEditAddress(v);
            dismiss();

            });

        mBinding.btnSettingAddressDelete.setOnClickListener(v ->
            {
            dismiss();
            showDialogCheckRemoveItem();
            });

        mBinding.btnSettingAddressCancel.setOnClickListener(v ->
            {
            dismiss();
            });
        }
    private void showDialogCheckRemoveItem()
        {

        new MaterialAlertDialogBuilder(getContext())
                .setTitle(getString(R.string.setting_address_dialog_title))
                .setMessage(getString(R.string.setting_address_dialog_message))
                .setPositiveButton(getString(R.string.setting_address_dialog_ok), (dialog, which) ->
                    {

                    dialog.dismiss();

                    })
                .setNegativeButton(getString(R.string.setting_address_dialog_cancel), (dialog, which) ->
                    {
                    dialog.dismiss();
                    })
                .show();
        }

    private void showEditAddress(View view)
        {
        val EDIT_ADDRESS = 3;
        String  title = getString(R.string.add_address_update_tittle);

        var action
                = AddressSettingFragmentDirections.actionAddressSettingFragmentToNewAddressFragment(title);
        action.setNavHomeAddressArgCheck(EDIT_ADDRESS);

        Navigation.findNavController(getParentFragment().getView()).navigate(action);
        }
    }
