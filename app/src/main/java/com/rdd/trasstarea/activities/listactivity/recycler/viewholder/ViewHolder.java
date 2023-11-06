package com.rdd.trasstarea.activities.listactivity.recycler.viewholder;

import android.graphics.Color;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.rdd.trasstarea.model.Task;
import com.rdd.trasstarea.R;

public class ViewHolder extends RecyclerView.ViewHolder {
    private final TextView titulo, fecha, tiempoRestante;
    private final ImageView prioritaria;
    private final ProgressBar duracion;

    public ViewHolder(View view) {
        super(view);
        // Define click listener for the ViewHolder's View
        prioritaria = view.findViewById(R.id.imageView);
        titulo = view.findViewById(R.id.titulo);
        duracion = view.findViewById(R.id.progressBar);
        fecha = view.findViewById(R.id.fecha);
        tiempoRestante = view.findViewById(R.id.tiempoRestante);
    }

    public TextView getTitulo() {
        return titulo;
    }

    public ImageView getPrioritaria() {
        return prioritaria;
    }

    public ProgressBar getDuracion() {
        return duracion;
    }

    public TextView getFecha() {
        return fecha;
    }

    public TextView getTiempoRestante() {
        return tiempoRestante;
    }

    public void bindTask(Task c) {
        titulo.setText(c.getTitulo());
        if (c.isPrioritaria()) {
            prioritaria.setImageResource(R.drawable.baseline_stars_24);
        }
        fecha.setText(String.valueOf(c.getDateEnd()));
        changeColorDaysLeft(c);
        tiempoRestante.setText(String.valueOf(c.getDaysLeft()));
        duracion.setProgress(c.getState());
    }

    private void changeColorDaysLeft(Task c) {
        if (c.getDaysLeft() < 0) {
            tiempoRestante.setTextColor(Color.RED);
        }
    }


}

