package com.rdd.trasstarea.activities.createtaskactivity.dialogs;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.time.LocalDate;

public class DatePicker extends DialogFragment {

    private IDatePicker datePicker;

    public void setDatePickerListener(IDatePicker listener){
        datePicker = listener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        LocalDate currentDate = LocalDate.now();
        int year = currentDate.getYear();
        int month = currentDate.getMonthValue();
        int day = currentDate.getDayOfMonth();

        return new DatePickerDialog(requireContext(), (view, year1, month1, dayOfMonth1) -> {
            if (datePicker != null) {
                datePicker.dateSelectedListener(year1, month1, dayOfMonth1);
            }
        }, year, month, day);
    }





}
