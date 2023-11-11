package com.rdd.trasstarea.activities.createtaskactivity;

import android.widget.EditText;

public class CheckTask {


    public static boolean checkEditText(EditText editText){
        String texto = editText.getText().toString();
        String titulo = "Título";
        String fechaCreacion = "FechaCreacion";
        String fechaObjetivo = "Fecha Objetivo";
        return texto.equalsIgnoreCase(titulo)
                || texto.equalsIgnoreCase(fechaCreacion)
                || texto.equalsIgnoreCase(fechaObjetivo)
                || texto.isEmpty();
    }

}
