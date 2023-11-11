package com.rdd.trasstarea.activities.createtaskactivity.dialogs.datepicker;
import android.widget.EditText;
import androidx.fragment.app.FragmentManager;
import java.util.Locale;

public class DatePickerHandle {
    private final EditText editText;

    public DatePickerHandle(EditText editText, FragmentManager fragmentManager) {
        this.editText = editText;
        setUpClickListener(fragmentManager);
    }

    private void setUpClickListener(FragmentManager fragmentManager) {
        editText.setOnClickListener(v -> showDatePicker(fragmentManager));
    }

    public void showDatePicker(FragmentManager fragmentManager) {
        DatePicker datePicker = new DatePicker();
        datePicker.show(fragmentManager, "datePicker");
        datePicker.setListener((view, year, month, dayOfMonth) -> handleDatePickerResult(year, month, dayOfMonth));
    }

    private void handleDatePickerResult(int year, int month, int dayOfMonth) {
        // Formatea la fecha según sea necesario
        String formattedDate = String.format(Locale.getDefault(),"%02d/%02d/%04d", dayOfMonth, month + 1, year);

        // Establece la fecha en el EditText
        editText.setText(formattedDate);
    }
}
