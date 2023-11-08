package com.rdd.trasstarea.activities.listactivity.dialogs;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.rdd.trasstarea.R;

public class ExitDialog {
    AlertDialog.Builder builder;
    AlertDialog dialog;

    public ExitDialog(AppCompatActivity activity) {
        // 1. Instantiate an AlertDialog.Builder with its constructor.
        builder = new AlertDialog.Builder(activity);

        // 2. Chain together various setter methods to set the dialog characteristics.
        builder.setMessage("Seguro que quieres salir?")
                .setTitle(R.string.salir);

        builder.setPositiveButton(R.string.salir,(dialog1, which) ->
                activity.finishAffinity());

        builder.setNegativeButton(R.string.cancelar, (dialog1, which) ->
                dialog.hide());

        // 3. Get the AlertDialog.
        dialog = builder.create();
        dialog.show();
    }
}
