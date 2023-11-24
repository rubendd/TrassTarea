package com.rdd.trasstarea.activities.listactivity.dialogs;

import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.rdd.trasstarea.R;

public class ExitDialog {
    AlertDialog.Builder builder;
    AlertDialog dialog;

    // Constructor de la clase ExitDialog
    public ExitDialog(AppCompatActivity activity) {
        // 1. Instancia un AlertDialog.Builder con su constructor.
        builder = new AlertDialog.Builder(activity);

        // 2. Encadena varios métodos setter para establecer las características del diálogo.
        builder.setMessage("¿Seguro que quieres salir?")
                .setTitle(R.string.salir);

        // Configura el botón positivo (aceptar) del diálogo.
        builder.setPositiveButton(R.string.salir, (dialog1, which) -> {
            activity.finishAffinity();  // Cierra todas las actividades de la aplicación
            Toast.makeText(activity, "Hasta pronto", Toast.LENGTH_SHORT).show();
        });

        // Configura el botón negativo (cancelar) del diálogo.
        builder.setNegativeButton(R.string.cancelar, (dialog1, which) ->
                dialog.hide());

        // 3. Obtiene el AlertDialog.
        dialog = builder.create();
        dialog.show();  // Muestra el diálogo.
    }
}

