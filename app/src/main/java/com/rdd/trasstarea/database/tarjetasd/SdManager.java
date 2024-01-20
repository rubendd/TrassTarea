package com.rdd.trasstarea.database.tarjetasd;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.util.Log;
import android.widget.Toast;

import com.rdd.trasstarea.R;
import com.rdd.trasstarea.model.Task;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.List;

public class SdManager {


    /**
     * Método para verificar si la tarjeta SD está montada
     * @return  si la tarjeta montada
     */
    public static boolean isTarjetaSDMontada() {
        String estado = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(estado);
    }

    public static String obtenerNombreArchivoDesdeUri(Context context, Uri uri) {
        Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
        String nombreArchivo = "";

        try {
            if (cursor != null && cursor.moveToFirst()) {
                int nombreIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                nombreArchivo = cursor.getString(nombreIndex);
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return nombreArchivo;
    }

    public static boolean esTipoDocumento(Context context, Uri uri) {
        ContentResolver resolver = context.getContentResolver();
        String tipoContenido = resolver.getType(uri);

        return tipoContenido != null && (tipoContenido.startsWith("application/") || tipoContenido.startsWith("text/"));
    }

    public static boolean esTipoAudio(Context context, Uri uri) {
        ContentResolver resolver = context.getContentResolver();
        String tipoContenido = resolver.getType(uri);

        return tipoContenido != null && tipoContenido.startsWith("audio/");
    }

    public static boolean esTipoImagen(Context context, Uri uri) {
        ContentResolver resolver = context.getContentResolver();
        String tipoContenido = resolver.getType(uri);

        return tipoContenido != null && tipoContenido.startsWith("image/");
    }

    public static boolean esTipoVideo(Context context, Uri uri) {
        ContentResolver resolver = context.getContentResolver();
        String tipoContenido = resolver.getType(uri);

        return tipoContenido != null && tipoContenido.startsWith("video/");
    }

    /**
    public static void guardarArchivoEnDirectorio(Uri uri, Context context) {

        try {
            InputStream inputStream = context.getContentResolver().openInputStream(uri);

            // Define la ruta de destino en tu directorio específico
            File directorioApp = new File(context.getExternalFilesDir(null), "tus_archivos");
            if (!directorioApp.exists()) {
                if (directorioApp.mkdirs()) {
                    Log.d("Almacenamiento", "Directorio de la aplicación creado: " + directorioApp.getAbsolutePath());
                } else {
                    Log.e("Almacenamiento", "Error al crear el directorio de la aplicación");
                    return;
                }
            }

            String nombreArchivo = "archivo_" + System.currentTimeMillis(); // Puedes definir tu propia lógica para el nombre del archivo
            File archivoDestino = new File(directorioApp, nombreArchivo);

            OutputStream outputStream = new FileOutputStream(archivoDestino);

            byte[] buffer = new byte[4096];
            int bytesRead;

            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }

            inputStream.close();
            outputStream.close();

            Log.d("Almacenamiento", "Archivo guardado en: " + archivoDestino.getAbsolutePath());

        } catch (IOException e) {
            e.printStackTrace();
            Log.e("Almacenamiento", "Error al guardar el archivo: " + e.getMessage());
        }
    }

    /**
     * Este metodo no sirve, de nada es para guardar las tareas en fichero
     * public static void escribirSD(List<Task> listaTareas, Context context){
     *         File file = new File(context.getExternalFilesDir(null), "tareas.dat");
     *         try {
     *             FileOutputStream fo = new FileOutputStream (file);
     *             ObjectOutputStream oo = new ObjectOutputStream(fo);
     *
     *             oo.writeObject(listaTareas);
     *
     *             oo.close();
     *             fo.close();
     *         }
     *         catch(IOException ex){
     *             ex.printStackTrace();
     *         }
     *     }
     */

    /**
     *
     * public static List<Task> leerSD(Context context) {
     *         try {
     *             File file = new File(context.getExternalFilesDir(null), "tareas.dat");
     *             if (!file.exists()) {
     *                 // Manejar el caso en el que el archivo no existe
     *                 Toast.makeText(context, "El archivo no existe", Toast.LENGTH_SHORT).show();
     *                 return null;
     *             }
     *
     *             InputStream fileInputStream = new FileInputStream(file);
     *             InputStream buffer = new BufferedInputStream(fileInputStream);
     *             ObjectInput input = new ObjectInputStream(buffer);
     *
     *             return (List<Task>) input.readObject();
     *         } catch (ClassNotFoundException ex) {
     *             ex.printStackTrace();
     *             Toast.makeText(context, "Error al leer de la sd: Clase no encontrada", Toast.LENGTH_SHORT).show();
     *         } catch (IOException ex) {
     *             ex.printStackTrace();
     *             Toast.makeText(context, "Error al leer de la sd: IOException", Toast.LENGTH_SHORT).show();
     *         }
     *         return null;
     *     }
     */



    public static String getPathFromUri(Context context, Uri uri) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && DocumentsContract.isDocumentUri(context, uri)) {
            return getPathFromDocumentUri(context, uri);
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
            return getPathFromContentUri(context, uri);
        } else {
            Log.e("FileUtils", "Unsupported URI scheme");
            return null;
        }
    }

    private static String getPathFromDocumentUri(Context context, Uri uri) {
        String documentId = DocumentsContract.getDocumentId(uri);
        String[] split = documentId.split(":");
        if (split.length > 1) {
            String volumeId = split[0];
            String relativePath = split[1];
            return "/" + volumeId + "/" + relativePath;
        } else {
            Log.e("FileUtils", "Invalid document ID format");
            return null;
        }
    }

    private static String getPathFromContentUri(Context context, Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = null;
        try {
            cursor = context.getContentResolver().query(uri, projection, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                return cursor.getString(columnIndex);
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return null;
    }
}
