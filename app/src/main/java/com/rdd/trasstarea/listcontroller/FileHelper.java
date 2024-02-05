package com.rdd.trasstarea.listcontroller;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.webkit.MimeTypeMap;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

public class FileHelper {

    public static File fileFromContentUri(Context context, Uri contentUri) {
        // Preparando el nombre del archivo temporal
        String fileName = "temp_file.ogg";

        // Creando el archivo temporal
        File tempFile = new File(context.getCacheDir(), fileName);
        try {
            tempFile.createNewFile();

            FileOutputStream oStream = new FileOutputStream(tempFile);
            InputStream inputStream = context.getContentResolver().openInputStream(contentUri);

            if (inputStream != null) {
                copy(inputStream, oStream);
            }

            oStream.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return tempFile;
    }

    private static String getFileExtension(Context context, Uri uri) {
        String fileType = context.getContentResolver().getType(uri);
        return MimeTypeMap.getSingleton().getExtensionFromMimeType(fileType);
    }

    private static void copy(InputStream source, OutputStream target) throws IOException {
        byte[] buf = new byte[8192];
        int length;
        while ((length = source.read(buf)) > 0) {
            target.write(buf, 0, length);
        }
    }

    public static Uri convertToNormalUri(String contentProviderUri) {
        Uri uri = Uri.parse(contentProviderUri);

        // Extraer el ID del último segmento de la URI
        String lastSegment = uri.getLastPathSegment();
        long id = Long.parseLong(lastSegment);

        // Obtener la parte base de la URI (sin el último segmento)
        Uri baseUri = Uri.parse(contentProviderUri).buildUpon().path("").build();

        // Construir la URI normal utilizando ContentUris
        return ContentUris.withAppendedId(baseUri, id);
    }
    public static String getRealPathFromURI(Uri contentUri, Context context) {
        String[] proj = {MediaStore.Audio.Media.DATA};

        // Utiliza ContentResolver en lugar de managedQuery
        ContentResolver contentResolver = context.getContentResolver();

        // Realiza una consulta en el ContentResolver
        Cursor cursor = contentResolver.query(contentUri, proj, null, null, null);

        if (cursor != null) {
            try {
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA);
                cursor.moveToFirst();
                return cursor.getString(column_index);
            } finally {
                // Asegúrate de cerrar el cursor cuando hayas terminado con él
                cursor.close();
            }
        }

        return null; // Manejar el caso en el que la consulta no fue exitosa
    }

    public static String readInputStream(InputStream inputStream) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

        String line;
        while ((line = reader.readLine()) != null) {
            stringBuilder.append(line);
        }

        return stringBuilder.toString();
    }


}
