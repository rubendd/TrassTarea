package com.rdd.trasstarea.fragments;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.github.barteksc.pdfviewer.PDFView;
import com.rdd.trasstarea.R;
import com.rdd.trasstarea.viewmodel.ComunicateFragments;

import java.io.File;
import java.util.Objects;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class DocumentoFragment extends Fragment {

    private PDFView pdfView;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View documentoFragment = inflater.inflate(R.layout.documento_fragment, container, false);

        ComunicateFragments documentoViewModel = new ViewModelProvider(requireActivity()).get(ComunicateFragments.class);

        pdfView = documentoFragment.findViewById(R.id.pdf);

        // Observa cambios en la URI del documento
        documentoViewModel.getDocumento().observe(getViewLifecycleOwner(), this::loadPdfFromUri);

        return documentoFragment;
    }

    // Método para cargar el PDF desde la URI
    private void loadPdfFromUri(Uri uri) {
        // Cargar el PDF desde la URI
        Log.d("Warning", Objects.requireNonNull(uri.getPath()));
        pdfView.fromFile(new File(uri.getPath()))
                .enableSwipe(true)
                .swipeHorizontal(false)
                .load();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
