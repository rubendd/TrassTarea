package com.rdd.trasstarea.listcontroller;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.webkit.MimeTypeMap;

import com.rdd.trasstarea.model.Task;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class FileHelper {



    public static void deleteAttachments(Task task) {
        List<String> attachmentUrls = new ArrayList<>();
        attachmentUrls.add(task.getURL_aud());
        attachmentUrls.add(task.getURL_doc());
        attachmentUrls.add(task.getURL_img());
        attachmentUrls.add(task.getURL_vid());

        for (String attachmentUrl : attachmentUrls) {
            if (attachmentUrl != null && !attachmentUrl.isEmpty()) {
                deleteFile(attachmentUrl);
            }
        }
    }

    private static void deleteFile(String filePath) {
        File file = new File(filePath);
        if (file.exists()) {
            boolean deleted = file.delete();
            if (deleted) {
                System.out.println("Archivo eliminado: " + filePath);
            } else {
                System.out.println("No se pudo eliminar el archivo: " + filePath);
            }
        } else {
            System.out.println("El archivo no existe: " + filePath);
        }
    }

}
