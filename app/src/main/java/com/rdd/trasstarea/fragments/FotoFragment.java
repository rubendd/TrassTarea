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
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.rdd.trasstarea.R;
import com.rdd.trasstarea.viewmodel.ComunicateFragments;

import java.io.FileNotFoundException;
import java.io.InputStream;
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
        fotoViewModel.getFoto().observe(getViewLifecycleOwner(), uri -> {
            executor.execute(() -> {
                Bitmap bitmap = cargarBitmapDesdeUri(uri);
                mostrarFotoEnContenedor(bitmap);
            });
        });
    }

    private Bitmap cargarBitmapDesdeUri(Uri uri) {
        try {
            return BitmapFactory.decodeFile(uri.getPath());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private void mostrarFotoEnContenedor(Bitmap bitmap) {
        handler.post(() -> {
            if (bitmap != null) {
                contenedorFoto.setImageBitmap(bitmap);
                contenedorFoto.setVisibility(View.VISIBLE);
            } else {
                // Manejar el caso de que la imagen no se cargue correctamente
                Toast.makeText(requireContext(), "No se pudo cargar la imagen", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        contenedorFoto = null;
        fotoViewModel = null;
    }
}
