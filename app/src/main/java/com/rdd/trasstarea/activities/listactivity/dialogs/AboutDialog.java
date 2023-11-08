package com.rdd.trasstarea.activities.listactivity.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.rdd.trasstarea.R;

public class AboutDialog extends DialogFragment {
    AlertDialog.Builder builder;
    AlertDialog dialog;

    public AboutDialog(Context activity, String about) {
        // 1. Instantiate an AlertDialog.Builder with its constructor.
         builder = new AlertDialog.Builder(activity);

        // 2. Chain together various setter methods to set the dialog characteristics.
        builder.setMessage(about)
                .setTitle(R.string.app_name);

        builder.setPositiveButton(R.string.aceptar,(dialog1, which) -> {
            dialog.dismiss(); // Cierra el diálogo y libera los recursos asociados
        });

        // 3. Get the AlertDialog.
        dialog = builder.create();
        dialog.show();
    }


}

