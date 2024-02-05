package com.rdd.trasstarea.fragments;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.rdd.trasstarea.R;
import com.rdd.trasstarea.viewmodel.ComunicateFragments;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class AudioFragment extends Fragment {


    private MediaPlayer mediaPlayer;
    private int currentPosition = 0;

    private ProgressBar progressBar;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View audioFragment = inflater.inflate(R.layout.audiolayout, container, false);


        ComunicateFragments audioViewModel = new ViewModelProvider(requireActivity()).get(ComunicateFragments.class);

        progressBar = audioFragment.findViewById(R.id.progressBar);
        ImageButton ibPlay = audioFragment.findViewById(R.id.ibPausar);
        ImageButton ibPause = audioFragment.findViewById(R.id.ibStop);
        ImageButton ibRecord = audioFragment.findViewById(R.id.ibReproducir);

        audioViewModel.getAudio().observe(getViewLifecycleOwner(), uri -> setupMediaPlayer(uri, progressBar));

        ibPlay.setOnClickListener(v -> playAudio());

        ibPause.setOnClickListener(v -> pauseResumeAudio());

        ibRecord.setOnClickListener(v -> stopAudio());

        return audioFragment;

    }

    private void setupMediaPlayer(Uri audioUri, ProgressBar progressBar) {
        if (audioUri != null) {
            if (mediaPlayer != null) {
                mediaPlayer.release();
            }
            //TODO solucionar uri.
            mediaPlayer = MediaPlayer.create(requireActivity(), audioUri);
            if (mediaPlayer != null) {
                progressBar.setMax(mediaPlayer.getDuration());
            } else {
                // Manejar el caso en que la creación del MediaPlayer falle
                Log.e("AudioFragment", "Error al crear MediaPlayer con la URI: " + audioUri);
            }
        } else {
            // Manejar el caso en que la URI sea nula
            Log.e("AudioFragment", "La URI del audio es nula");
        }
    }



    private void playAudio() {
        if (mediaPlayer != null) {
            if (!mediaPlayer.isPlaying()) {
                mediaPlayer.setLooping(false);
                mediaPlayer.start();
                updateProgressBar();
            }
        } else {
            Toast.makeText(getContext(), "No hay grabación para reproducir", Toast.LENGTH_SHORT).show();
        }
    }

    private void pauseResumeAudio() {
        if (mediaPlayer != null) {
            if (mediaPlayer.isPlaying()) {
                currentPosition = mediaPlayer.getCurrentPosition();
                mediaPlayer.pause();
            } else {
                mediaPlayer.seekTo(currentPosition);
                mediaPlayer.start();
            }
        }
    }

    private void stopAudio() {
        if (mediaPlayer != null) {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
            }
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    private void updateProgressBar() {
        final Handler handler = new Handler(Looper.getMainLooper());
        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(new Runnable() {
            @Override
            public void run() {
                while (mediaPlayer != null && mediaPlayer.isPlaying()) {
                    int currentPosition = mediaPlayer.getCurrentPosition();
                    progressBar.setProgress(currentPosition);
                    handler.postDelayed(this,500);
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mediaPlayer != null) {
            mediaPlayer.release();
        }
    }

}

