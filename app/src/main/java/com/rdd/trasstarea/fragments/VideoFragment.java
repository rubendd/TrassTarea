package com.rdd.trasstarea.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.rdd.trasstarea.R;
import com.rdd.trasstarea.viewmodel.ComunicateFragments;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class VideoFragment extends Fragment {
    private ComunicateFragments videoViewModel;
    private VideoView contenedorVideo;
    private Handler handler;
    private Executor executor;
    private ProgressBar progressBar;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View videoview = inflater.inflate(R.layout.video_layout, container, false);

        videoViewModel = new ViewModelProvider(requireActivity()).get(ComunicateFragments.class);



         progressBar = videoview.findViewById(R.id.progressBarVideo);

        contenedorVideo = videoview.findViewById(R.id.contenedor_video);
        contenedorVideo.setVisibility(View.INVISIBLE);

        handler = new Handler(Looper.getMainLooper());
        executor = Executors.newSingleThreadExecutor();

        videoViewModel.getVideo().observe(getViewLifecycleOwner(), uri -> {
            contenedorVideo.setVideoURI(uri);
            progressBar.setMax(contenedorVideo.getDuration());
            contenedorVideo.setVisibility(View.VISIBLE);
        });



        final ImageButton ibPlay = videoview.findViewById(R.id.ibReproducir);
        ibPlay.setOnClickListener(v -> playVideo());

        final ImageButton ibPause = videoview.findViewById(R.id.ibPausar);
        ibPause.setOnClickListener(v -> pauseResumeVideo());

        final ImageButton ibRecord = videoview.findViewById(R.id.ibParar);
        ibRecord.setOnClickListener(v -> stopVideo());

        return videoview;
    }



    private void playVideo() {
        if (videoViewModel.getVideo().getValue() != null) {
            contenedorVideo.seekTo(0);
            executor.execute(() -> {
                if (!contenedorVideo.isPlaying())
                    contenedorVideo.start();
            });
            executor.execute(() -> {
                if (videoViewModel.getVideo().getValue() != null) {
                    int currentPosition = contenedorVideo.getCurrentPosition();
                    handler.postDelayed(() -> {
                        if (videoViewModel.getVideo().getValue() != null) {
                            int newPosition = contenedorVideo.getCurrentPosition();
                            if (newPosition == currentPosition) {
                                progressBar.setProgress(newPosition);
                            }
                        }
                    }, 500);
                }
            });
        } else {
            Toast.makeText(getContext(), "No hay vídeo para reproducir", Toast.LENGTH_SHORT).show();
        }
    }

    private void pauseResumeVideo() {
        if (videoViewModel.getVideo().getValue() != null) {
            if (contenedorVideo.isPlaying()) {
                contenedorVideo.pause();
            } else {
                contenedorVideo.start();
            }
        }
    }

    private void stopVideo() {
        if (videoViewModel.getVideo().getValue() != null) {
            contenedorVideo.stopPlayback();
            contenedorVideo.setVisibility(View.INVISIBLE);
        }
    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
