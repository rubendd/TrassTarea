package com.rdd.trasstarea.activities.createtaskactivity;

import android.widget.EditText;

/**
 * Clase que contiene métodos estático reutilizables
 */
public class CheckTask {

    //Comprobar campos
    public static boolean checkEditText(EditText editText){
        String texto = editText.getText().toString();
        String titulo = "Título";
        String fechaCreacion = "FechaCreacion";
        String fechaObjetivo = "Fecha Objetivo";
        return texto.equalsIgnoreCase(titulo)
                || texto.equalsIgnoreCase(fechaCreacion)
                || texto.equalsIgnoreCase(fechaObjetivo)
                || texto.isEmpty() || estaVacio(texto);
    }

    private static boolean estaVacio(String string) {
        int espaciosEnBlanco = 0;
        for (int i = 0; i < string.length(); i++) {
            if (string.charAt(i) == ' ') {
                espaciosEnBlanco++;
            }
        }
        return espaciosEnBlanco == string.length();
    }


}
