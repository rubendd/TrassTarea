package com.rdd.trasstarea.activities.listactivity.recycler.viewholder;

import android.graphics.Color;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.PopupMenu.OnMenuItemClickListener;
import androidx.recyclerview.widget.RecyclerView;

import com.rdd.trasstarea.R;
import com.rdd.trasstarea.activities.listactivity.dialogs.AboutDialog;
import com.rdd.trasstarea.activities.listactivity.dialogs.DeleteDialog;
import com.rdd.trasstarea.comunicator.IComunicator;
import com.rdd.trasstarea.model.Task;

import java.util.List;

public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, OnMenuItemClickListener{
    private final TextView titulo, fecha, tiempoRestante;
    private final ImageView prioritaria;
    private final ProgressBar duracion;
    private Task actualTask;
    private final List<Task> taskList;
    private final View view;
    //La variable posición indicará la posición del viewholder que se ha hecho click.
    private int position;
    private final IComunicator comunicator;

    public ViewHolder(View view, List<Task> taskList, IComunicator comunicator) {
        super(view);
        // Define click listener for the ViewHolder's View
        prioritaria = view.findViewById(R.id.imageView);
        titulo = view.findViewById(R.id.titulo);
        duracion = view.findViewById(R.id.progressBar);
        fecha = view.findViewById(R.id.fecha);
        tiempoRestante = view.findViewById(R.id.tiempoRestante);
        this.taskList = taskList;
        this.view = view;
        this.comunicator = comunicator;
        view.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        // Acciones a realizar cuando se hace clic en el RelativeLayout
        position = getAdapterPosition();
        if (position != RecyclerView.NO_POSITION) {
            actualTask = taskList.get(position);
            // Si la posición es válida, puedes obtener el objeto Task correspondiente a la posición
            showPopup(v);
        }
    }

    /**
     * Menú contextual.
     * @param v
     */
    public void showPopup(View v) {
        PopupMenu popup = new PopupMenu(v.getContext(), v);
        popup.setOnMenuItemClickListener(this);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.taskmenu, popup.getMenu());
        popup.show();
    }

    /**
     * Controla los eventos del menú.
     * @param item the menu item that was clicked
     */
    @Override
    public boolean onMenuItemClick(MenuItem item) {
        if (item.getItemId() == R.id.description) {
            new AboutDialog(view.getContext(), actualTask.getDescription());
            return true;
        }
        if (item.getItemId() == R.id.delete) {
            deleteTask();
            return true;
        }
        return false;
    }

    /**
     * Método que se establece en la interfaz del deleteDialog para llamar
     * a la interfaz de comunicator.
     */
    private void deleteTask(){
        DeleteDialog deleteDialog = new DeleteDialog();
        deleteDialog.setDeleteDialogListener(() -> { // Para que sea sincrona tenemos que crear un listener.
            if (comunicator != null) {
                taskList.remove(position); // Borramos la tarea del viewholder.
                comunicator.deleteList(position); //Llamamos al deleteList del comunicador.
            }
        });
        deleteDialog.showDelete(view.getContext()); //Mostramos
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

