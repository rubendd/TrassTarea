package com.rdd.trasstarea.tarjetasd;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;

import java.io.File;

public class SdManager {

/*
    public void guardarEnSd(){
        File file = new File(this.getExternalFilesDir(null), fichero);

//        //Para directorio público compartido
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) //Posterior a Android 11
//            file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), fichero);
//        else
//            file = new File(Environment.getExternalStorageDirectory(), fichero);

        OutputStreamWriter osw = null;
        try {
            osw = new OutputStreamWriter(new FileOutputStream(file));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            osw.write(texto);
            osw.flush();
            osw.close();
        } catch (IOException | NullPointerException e) {
            Toast.makeText(this, R.string.msg_readwrite_error, Toast.LENGTH_SHORT).show();
        }
        Toast.makeText(this, R.string.msg_save_ok, Toast.LENGTH_SHORT).show();
    }*/


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
