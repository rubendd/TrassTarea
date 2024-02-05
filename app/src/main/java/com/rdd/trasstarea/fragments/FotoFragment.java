package com.rdd.trasstarea.fragments;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.rdd.trasstarea.R;
import com.rdd.trasstarea.viewmodel.ComunicateFragments;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class FotoFragment extends Fragment {


    private ImageView contenedorFoto;
    private Executor executor;
    private Handler handler;
    private ComunicateFragments fotoViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View fotoFragment = inflater.inflate(R.layout.foto_layout, container, false);

        contenedorFoto = fotoFragment.findViewById(R.id.contenedor_foto);
        fotoViewModel = new ViewModelProvider(requireActivity()).get(ComunicateFragments.class);

        handler = new Handler(Looper.getMainLooper());
        executor = Executors.newSingleThreadExecutor();

        setupFotoViewModel();
        return fotoFragment;
    }

    private void setupFotoViewModel() {
        fotoViewModel.getFoto().observe(getViewLifecycleOwner(), uri -> executor.execute(() -> {
            String filePath = obtenerRutaDeArchivoDesdeUri(uri);
            Bitmap bitmap = cargarBitmapReducido(filePath);
            mostrarFotoEnContenedor(bitmap);
        }));
    }

    private Bitmap cargarBitmapReducido(String filePath) {
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, bmOptions);

        int anchoContenedor = contenedorFoto.getWidth();
        int altoContenedor = contenedorFoto.getHeight();

        int anchoFoto = bmOptions.outWidth;
        int altoFoto = bmOptions.outHeight;

        int scaleFactor = 1;
        if ((anchoContenedor > 0) || (altoContenedor > 0)) {
            scaleFactor = Math.min(anchoFoto / anchoContenedor, altoFoto / altoContenedor);
        }

        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;

        return BitmapFactory.decodeFile(filePath, bmOptions);
    }

    private void mostrarFotoEnContenedor(Bitmap bitmap) {
        handler.post(() -> {
            contenedorFoto.setImageBitmap(bitmap);
            contenedorFoto.setVisibility(View.VISIBLE);
        });
    }



    private String obtenerRutaDeArchivoDesdeUri(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = requireContext().getContentResolver().query(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String filePath = cursor.getString(column_index);
        cursor.close();
        return filePath;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        contenedorFoto = null;
        fotoViewModel = null;
    }
}
