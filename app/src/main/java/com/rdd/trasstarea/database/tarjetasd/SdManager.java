package com.rdd.trasstarea.database.tarjetasd;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import com.rdd.trasstarea.R;
import com.rdd.trasstarea.model.Task;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.util.List;

public class SdManager {



    public static void escribirSD(List<Task> listaTareas, Context context){

        File file = new File(context.getExternalFilesDir(null), "tareas.dat");
        try {
            FileOutputStream fo = new FileOutputStream (file);
            ObjectOutputStream oo = new ObjectOutputStream(fo);

            oo.writeObject(listaTareas);

            oo.close();
            fo.close();
        }
        catch(IOException ex){
            ex.printStackTrace();
        }

    }

    public List<Task> leerSD(Context context) {
        try {
            File file = new File(context.getExternalFilesDir(null), "tareas.dat");
            if (!file.exists()) {
                // Manejar el caso en el que el archivo no existe
                Toast.makeText(context, "El archivo no existe", Toast.LENGTH_SHORT).show();
                return null;
            }

            InputStream fileInputStream = new FileInputStream(file);
            InputStream buffer = new BufferedInputStream(fileInputStream);
            ObjectInput input = new ObjectInputStream(buffer);

            return (List<Task>) input.readObject();

        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
            Toast.makeText(context, "Error al leer de la sd: Clase no encontrada", Toast.LENGTH_SHORT).show();
        } catch (IOException ex) {
            ex.printStackTrace();
            Toast.makeText(context, "Error al leer de la sd: IOException", Toast.LENGTH_SHORT).show();
        }
        return null;
    }




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
