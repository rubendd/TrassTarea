package com.rdd.trasstarea.fragments;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.rdd.trasstarea.databinding.AudioBinding;
import com.rdd.trasstarea.viewmodel.ComunicateFragments;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class AudioFragment extends Fragment {

    private AudioBinding binding;
    private MediaPlayer mp;
    private ComunicateFragments audioViewModel;
    private int posicion = 0;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        audioViewModel =
                new ViewModelProvider(this).get(ComunicateFragments.class);

        binding = AudioBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final ProgressBar progressBar = binding.progressBar;

        //Con el liveData cada vez que se cargue una Uri en el ViewModel
        //creamos un MediaPlayer y seteamos el progressBar
        audioViewModel.getAudio().observe(getViewLifecycleOwner(), Uri -> {
            mp = MediaPlayer.create(getActivity(),Uri);
            progressBar.setMax(mp.getDuration());
        });

        //Handler para comunicar con el hilo principal
        final Handler handler = new Handler(Looper.getMainLooper());
        //Executor para ejecutar en hilos secundarios
        Executor executor = Executors.newSingleThreadExecutor();

        final Button btAudio = binding.btAudio;
        btAudio.setOnClickListener(v -> {
            if(mp!=null){
                if(mp.isPlaying())
                    mp.stop();
                mp.release();
                mp = null;
            }
            //Creamos un intent para ir a la carpeta de grabaciones
            Intent aGrabaciones = new Intent();
            aGrabaciones.setType("audio/*");
            aGrabaciones.setAction(Intent.ACTION_GET_CONTENT);
            //Creamos otro intent para ir a la aplicación grabadora
            Intent aGrabadora = new Intent(MediaStore.Audio.Media.RECORD_SOUND_ACTION);
            //Creamos un tercer intent que permitirá abrir un cuadro de diálogo para que
            //el usuario elija ir a la carpeta (opción principal) o la grabadora (opción secundaria)
            Intent chooser = new Intent(Intent.ACTION_CHOOSER);
            chooser.putExtra(Intent.EXTRA_TITLE, "Grabaciones"); //Título del diálogo
            chooser.putExtra(Intent.EXTRA_INTENT, aGrabaciones); //Opción principal
            Intent[] intentarray= {aGrabadora}; //Opciones secundarias, hay 1 sola
            chooser.putExtra(Intent.EXTRA_INITIAL_INTENTS,intentarray);
            lanzadorGrabadora.launch(chooser);
        });

        final ImageButton ibPlay = binding.ibReproducir;
        ibPlay.setOnClickListener(v -> {
            if (mp != null){
                //En un hilo secundario reproducimos el audio
                executor.execute(()->{
                    if(!mp.isPlaying()) {
                        mp.setLooping(false);
                        mp.start();
                    }
                });
                //En otro hilo secundario avanzamos el ProgressBar
                executor.execute(new Runnable() {
                    @Override
                    public void run() {
                        if (mp != null) {
                            int currentPosition = mp.getCurrentPosition();
                            progressBar.setProgress(currentPosition);
                        }
                        handler.postDelayed(this, 500);
                    }
                });
            }else{
                Toast.makeText(getContext(), "No hay grabación para reproducir", Toast.LENGTH_SHORT).show();
            }
        });

        final ImageButton ibPause = binding.ibPausar;
        ibPause.setOnClickListener( v -> {
            if(mp!=null){
                if(mp.isPlaying()){
                    posicion = mp.getCurrentPosition();
                    mp.pause();
                }
                else {
                    mp.seekTo(posicion);
                    mp.start();
                }

            }
        });

        final ImageButton ibRecord = binding.ibStop;
        ibRecord.setOnClickListener(v -> {
            if(mp!=null){
                if(mp.isPlaying()){
                    mp.stop();
                }
                mp.release();
            }
        });

        return root;
    }

    ActivityResultLauncher<Intent> lanzadorGrabadora = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult o) {
                    Uri audio = o.getData() != null ? o.getData().getData() : null;
                    if (audio != null)
                        //Metemos en el ViewModel la Uri del audio
                        audioViewModel.setAudio(audio);
                }
            }
    );

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
        if(mp != null)
            mp.release();
    }
}
