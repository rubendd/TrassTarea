package com.rdd.trasstarea.activities.listactivity.recycler.viewholder;

import android.graphics.Color;
import android.text.Layout;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.PopupMenu.OnMenuItemClickListener;
import androidx.recyclerview.widget.RecyclerView;

import com.rdd.trasstarea.activities.listactivity.ListActivity;
import com.rdd.trasstarea.activities.listactivity.dialogs.AboutDialog;
import com.rdd.trasstarea.activities.listactivity.dialogs.DeleteDialog;
import com.rdd.trasstarea.model.Task;
import com.rdd.trasstarea.R;

import java.util.List;

public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, OnMenuItemClickListener{
    private final TextView titulo, fecha, tiempoRestante;
    private final ImageView prioritaria;
    private final ProgressBar duracion;
    private Task actualTask;
    private List<Task> taskList;

    private View view;

    public ViewHolder(View view, List<Task> taskList) {
        super(view);
        // Define click listener for the ViewHolder's View
        prioritaria = view.findViewById(R.id.imageView);
        titulo = view.findViewById(R.id.titulo);
        duracion = view.findViewById(R.id.progressBar);
        fecha = view.findViewById(R.id.fecha);
        tiempoRestante = view.findViewById(R.id.tiempoRestante);
        this.taskList = taskList;
        this.view = view;
        view.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        // Acciones a realizar cuando se hace clic en el RelativeLayout
        int position = getAdapterPosition();
        if (position != RecyclerView.NO_POSITION) {
            actualTask = taskList.get(position);
            // Si la posición es válida, puedes obtener el objeto Task correspondiente a la posición
            showPopup(v);
        }
    }

    public void showPopup(View v) {
        PopupMenu popup = new PopupMenu(v.getContext(), v);
        popup.setOnMenuItemClickListener(this);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.taskmenu, popup.getMenu());
        popup.show();
    }


    @Override
    public boolean onMenuItemClick(MenuItem item) {
        if (item.getItemId() == R.id.description) {
            new AboutDialog(view.getContext(), actualTask.getDescription());
            return true;
        }
        if (item.getItemId() == R.id.delete) {
            if (new DeleteDialog(view.getContext()).isAceptar()){
              taskList.remove(actualTask);
            }
            return true;
        }
        return false;
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

