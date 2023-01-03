package com.example.a5asec.ui.view.login;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.icu.util.GregorianCalendar;
import android.os.Bundle;
import android.widget.DatePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.android.gms.common.internal.Objects;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DataPickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener
    {
    private static final String BUNDLE_KEY_YEAR = "year";
    private static final String BUNDLE_KEY_MONTH = "monthOfYear";
    private static final String BUNDLE_KEY_DAY = "dayOfMonth";

    public static final Calendar calendar = Calendar.getInstance();

    public DataPickerFragment()
        {

        }

    public static DataPickerFragment newInstance(int year, int monthOfYear, int dayOfMonth)
        {
        DataPickerFragment dialog = new DataPickerFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(BUNDLE_KEY_YEAR, year);
        bundle.putInt(BUNDLE_KEY_MONTH, monthOfYear);
        bundle.putInt(BUNDLE_KEY_DAY, dayOfMonth);
        dialog.setArguments(bundle);
        return dialog;
        }


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState)
        {
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        // min is today
        // max is after 1 month
        GregorianCalendar maxDate = new GregorianCalendar();
        maxDate.set(year, month + 1, day);

        if (getArguments() != null) {
            year = getArguments().getInt(BUNDLE_KEY_YEAR);
            month = getArguments().getInt(BUNDLE_KEY_MONTH);
            day = getArguments().getInt(BUNDLE_KEY_DAY);
        }
        DatePickerDialog dialog = new DatePickerDialog(requireActivity()
                , this, year, month, day);


        dialog.getDatePicker().setMaxDate(maxDate.getTimeInMillis());

        return dialog;
        }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth)
        {

        //Do something with the date chosen by the user
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        String patternDate = "MM/dd/yyyy";


        DateFormat dateFormat = new SimpleDateFormat(patternDate, Locale.getDefault());
        var currentDate = dateFormat.format(calendar.getTime());

        Bundle selectedDateBundle = new Bundle();
        selectedDateBundle.putString("SELECTED_DATE", currentDate);
        requireActivity().getSupportFragmentManager().setFragmentResult(
                "REQUEST_KEY", selectedDateBundle);

        }

    public static String getSelectionDate()
        {
        //optimize check SDK
        String patternDate = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"; // 2021-04-27T19:07:05.297Z
        DateFormat dateTimeFormat = new SimpleDateFormat(patternDate, Locale.ENGLISH);
        Date currentDate;
        if(Objects.equal(calendar, null))
            {
            currentDate = new Date();
            }else  currentDate = calendar.getTime();

        return dateTimeFormat.format(currentDate);
        }

    }
