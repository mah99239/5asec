package com.example.a5asec.ui.main.view;


import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.widget.DatePicker;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.a5asec.R;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

public class DataPickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener
{
    public final Calendar calendar = Calendar.getInstance();
    static EditText mEditText;

    private static final String BUNDLE_KEY_YEAR = "year";
    private static final String BUNDLE_KEY_MONTH = "monthOfYear";
    private static final String BUNDLE_KEY_DAY = "dayOfMonth";

    public DataPickerFragment(EditText editText)
    {
        mEditText = editText;
    }

    public static DataPickerFragment newInstance(int year, int monthOfYear, int dayOfMonth)
    {
        DataPickerFragment dialog = new DataPickerFragment(mEditText);
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
        //create a new instance of DatePickerDialog and return it

        if (getArguments() != null)
        {
            year = getArguments().getInt(BUNDLE_KEY_YEAR);
            month = getArguments().getInt(BUNDLE_KEY_MONTH);
            day = getArguments().getInt(BUNDLE_KEY_DAY);
        }
        DatePickerDialog dialog = new DatePickerDialog(getActivity(), R.style.SpinnerDatePickerDialog
                , this, year, month, day);
        //dialog.setTitle();


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
        String currentDate;

        DateFormat dateFormat = new SimpleDateFormat(patternDate, Locale.getDefault());
        currentDate = dateFormat.format(calendar.getTime());

        mEditText.setText(currentDate);
    }

    public String getDate()
    {
        //optimize check SDK
        String patternDate = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"; // 2021-04-27T19:07:05.297Z
        DateFormat dateTimeFormat = new SimpleDateFormat(patternDate, Locale.ENGLISH);

        Date date = calendar.getTime();
        return dateTimeFormat.format(date);
    }

}
