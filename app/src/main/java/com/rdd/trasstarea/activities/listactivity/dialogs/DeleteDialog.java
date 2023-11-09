package com.rdd.trasstarea.activities.listactivity.dialogs;

import android.app.Dialog;
import android.content.Context;

import androidx.appcompat.app.AlertDialog;

import com.rdd.trasstarea.R;

public class DeleteDialog {
    AlertDialog.Builder builder;
    AlertDialog dialog;

    private boolean aceptar;

    public boolean isAceptar() {
        return aceptar;
    }

    public boolean showDelete(Context activity){
        aceptar = false;

        builder = new AlertDialog.Builder(activity);

        // 2. Chain together various setter methods to set the dialog characteristics.
        builder.setMessage("Seguro que quieres borrar la tarea?")
                .setTitle(R.string.borrar);

        builder.setPositiveButton(R.string.borrar,(dialog1, which) ->{
            aceptar = true;
        });

        builder.setNegativeButton(R.string.cancelar, (dialog1, which) ->
                dialog.dismiss());

        // 3. Get the AlertDialog.
        dialog = builder.create();
        dialog.show();

        return true;
    }



}
