package com.rdd.trasstarea.activities.listactivity.dialogs;

import android.content.Context;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.rdd.trasstarea.R;

public class AboutDialog extends DialogFragment {
    AlertDialog.Builder builder;
    AlertDialog dialog;

    // Constructor que recibe el contexto de la actividad y la información "about" para mostrar
    public AboutDialog(Context activity, String about) {
        // 1. Instanciar un AlertDialog.Builder con su constructor.
         builder = new AlertDialog.Builder(activity);

        // 2. Encadenar varios métodos setter para establecer las características del diálogo.
        builder.setMessage(about)
                .setTitle(R.string.app_name);

        // Configurar un botón positivo (aceptar) y su acción
        builder.setPositiveButton(R.string.aceptar,(dialog1, which) -> {
            // Cerrar el diálogo y liberar los recursos asociados
            dialog.dismiss();
        });

        // 3. Obtiene el AlertDialog y lo muestra
        dialog = builder.create();
        dialog.show();
    }


}

