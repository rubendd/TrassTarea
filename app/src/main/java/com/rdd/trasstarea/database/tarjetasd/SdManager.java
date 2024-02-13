package com.rdd.trasstarea.database.tarjetasd;

import android.content.ContentResolver;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.OpenableColumns;
import android.util.Log;

import java.io.File;
import java.util.concurrent.TimeUnit;

public class SdManager {


    /**
     * **************** SISTEMA DE BORRADO DE ARCHIVOS POR TIEMPO ***********************
     */

    public static void borrarArchivosPorTiempo(Context context) {
        // Lógica para obtener la preferencia y borrar archivos según la fecha de creación
        SharedPreferences  preferences = PreferenceManager.getDefaultSharedPreferences(context);

        int numeroDias = preferences.getInt("borrado", 0);

        // Obtener el directorio donde se guardan los archivos
        File directorioApp = obtenerDirectorioApp(context);

        // Iterar sobre los archivos en el directorio y aplicar la lógica de borrado
        File[] archivos = directorioApp.listFiles();

        if (archivos != null && numeroDias != 0) {
            for (File archivo : archivos) {
                // Aplicar la lógica de borrado para cada archivo
                if (debeBorrarArchivo(archivo, numeroDias)) {
                    if (archivo.delete()) {
                        Log.d("borrarArchivo", "Archivo borrado: " + archivo.getAbsolutePath());
                    } else {
                        Log.e("borrarArchivo", "No se pudo borrar el archivo: " + archivo.getAbsolutePath());
                    }
                }
            }
        }
    }

    /**
     * Este método, llamado debeBorrarArchivo, determina si un archivo
     * debe ser eliminado según un número de días específico. Calcula
     * la diferencia en días entre la fecha de creación del archivo y
     * el tiempo actual, y devuelve true si esa diferencia es mayor o
     * igual al número de días proporcionado como parámetro. En resumen,
     * el método evalúa si el archivo es considerado "antiguo" según el
     * criterio de días establecido y devuelve un resultado booleano.
     * @param archivo
     * @param numeroDias
     * @return
     */
    private static boolean debeBorrarArchivo(File archivo, int numeroDias) {
        long fechaCreacion = archivo.lastModified();
        long diferenciaEnMillis = System.currentTimeMillis() - fechaCreacion;
        long diferenciaEnDias = TimeUnit.MILLISECONDS.toDays(diferenciaEnMillis);
        return diferenciaEnDias >= numeroDias;
    }

    private static File obtenerDirectorioApp(Context context) {
        SharedPreferences  preferences = PreferenceManager.getDefaultSharedPreferences(context);
        boolean guardarEnTarjetaSD = preferences.getBoolean("sd",false);  // Indica si se debe guardar en la tarjeta SD
        File directorioApp;

        if (guardarEnTarjetaSD && isTarjetaSDMontada()) {
            directorioApp = new File(Environment.getExternalStorageDirectory(), "archivos_adjuntos");
        } else {
            directorioApp = new File(context.getExternalFilesDir(null), "archivos_adjuntos");
        }

        if (!directorioApp.exists()) {
            if (directorioApp.mkdirs()) {
                Log.d("Almacenamiento", "Directorio de archivos adjuntos creado: " + directorioApp.getAbsolutePath());
            } else {
                Log.e("Almacenamiento", "Error al crear el directorio de archivos adjuntos");
            }
        }

        return directorioApp;
    }

    /**
     * ***************************************************************************************************
     */

    /**
     * Método que comprueba si la preferencia de la sdCard está activada
     * @param context
     * @return
     */
    public static boolean isSdChecked(Context context){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getBoolean("sd",false);
    }

    /**
     * Método para verificar si la tarjeta SD está montada
     * @return  si la tarjeta montada
     */
    public static boolean isTarjetaSDMontada() {
        String estado = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(estado);
    }

    /**
     * Método que obtiene el nombre del archivo.
     * @param context
     * @param uri
     * @return el nombre del archivo
     */
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


    public static boolean borrarArchivo(String rutaArchivo) {
        File archivo = new File(rutaArchivo);

        if (archivo.exists()) {
            try {
                if (archivo.delete()) {
                    Log.d("borrarArchivo", "Archivo borrado: " + rutaArchivo);
                    return true;
                } else {
                    Log.e("borrarArchivo", "No se pudo borrar el archivo: " + rutaArchivo);
                    return false;
                }
            } catch (SecurityException e) {
                Log.e("borrarArchivo", "Error de seguridad al borrar el archivo: " + e.getMessage());
                return false;
            }
        } else {
            Log.e("borrarArchivo", "El archivo no existe en la ruta proporcionada: " + rutaArchivo);
            return false;
        }
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


    /*
    public static String getPathFromUri(Context context, Uri uri) {
        if (uri == null) {
            return null;
        }

        if ("file".equalsIgnoreCase(uri.getScheme())) {
            // La URI ya es un archivo local, simplemente devuelve el path
            return uri.getPath();
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
            return getPathFromContentUri(context, uri);
        } else {
            Log.e("FileUtils", "Unsupported URI scheme");
            return null;
        }
    }

    /*
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
    /*
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
    */
}
