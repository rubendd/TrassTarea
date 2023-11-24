package com.rdd.trasstarea.activities.listactivity.recycler;

import android.graphics.Color;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;

import com.rdd.trasstarea.R;
import com.rdd.trasstarea.activities.listactivity.dialogs.AboutDialog;
import com.rdd.trasstarea.comunicator.IComunicator;
import com.rdd.trasstarea.model.Task;

import java.util.ArrayList;
import java.util.List;
public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder> {

    // Conjunto de datos de tareas
    private final List<Task> tasksDataSet;
    // Interfaz para comunicarse con la actividad principal
    private static IComunicator comunicator;
    // Flag para aceptar o rechazar acciones

    // Método para obtener el conjunto de datos de tareas
    public List<Task> getTasksDataSet() {
        return tasksDataSet;
    }

    // Clase interna que representa una vista de elemento de lista
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // Elementos de la vista
        private final TextView titulo, fecha, tiempoRestante;
        private final ImageView prioritaria;
        private final ProgressBar duracion;
        private final View view;

        // Constructor que inicializa los elementos de la vista
        public MyViewHolder(View view) {
            super(view);
            prioritaria = view.findViewById(R.id.imageView);
            titulo = view.findViewById(R.id.titulo);
            duracion = view.findViewById(R.id.progressBar);
            fecha = view.findViewById(R.id.fecha);
            tiempoRestante = view.findViewById(R.id.tiempoRestante);
            this.view = view;
        }

        // Método para enlazar una tarea a la vista
        public void bindTask(Task c) {
            titulo.setText(c.getTitulo());
            fecha.setText(c.calendar());
            tiempoRestante.setText(String.valueOf(c.getDaysLeft()));
            duracion.setProgress(c.getProgresState());

            //Metodo que cambia la decoración de la tarea
            cambiarDecoracion(c);

            // Configurar el listener de clic
            view.setOnClickListener(v -> {
                // Mostrar menú emergente al hacer clic en la tarea
                showPopupMenu(v, c);
            });
        }


        private void cambiarDecoracion(Task c){
            // Aplicar tachado al título si la tarea está completada
            if (c.getProgresState() == 100) {
                titulo.setPaintFlags(titulo.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            } else {
                // Quitar el tachado si la tarea no está completada
                titulo.setPaintFlags(titulo.getPaintFlags() & ~Paint.STRIKE_THRU_TEXT_FLAG);
            }
            if (c.isPrioritaria()) {
                prioritaria.setImageResource(R.drawable.baseline_stars_24);
            }
            if (c.getDaysLeft() < 0) {
                tiempoRestante.setTextColor(Color.RED);
            } else {
                // Restablecer el color a su valor predeterminado si no es negativo
                tiempoRestante.setTextColor(Color.BLACK); // o el color que desees
            }
        }

    }

    // Constructor del adaptador
    public CustomAdapter(List<Task> dataSet, IComunicator comunicator) {
        // Inicializar el conjunto de datos y la interfaz de comunicación
        tasksDataSet = new ArrayList<>(dataSet);
        CustomAdapter.comunicator = comunicator;
    }

    // Método para actualizar el conjunto de datos con nuevas tareas
    public void updateData(List<Task> newData) {
        tasksDataSet.clear();
        tasksDataSet.addAll(newData);
    }

    // Crear nuevas vistas (invocado por el administrador de diseño)
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Inflar el diseño de la lista para cada elemento
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.list, viewGroup, false);

        return new MyViewHolder(view);
    }

    // Reemplazar el contenido de una vista (invocado por el administrador de diseño)
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        // Vincular datos de tarea a la vista
        holder.bindTask(tasksDataSet.get(position));
        // Establecer la etiqueta de la vista con el titular
        holder.view.setTag(holder);
    }

    // Devolver el número total de elementos en el conjunto de datos
    @Override
    public int getItemCount() {
        return tasksDataSet.size();
    }

    // Devolver el identificador de elemento en una posición específica
    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    // Método para mostrar un menú emergente de opciones para una tarea
    private static void showPopupMenu(View view, Task task) {
        PopupMenu popup = new PopupMenu(view.getContext(), view);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.taskmenu, popup.getMenu());

        // Manejar las acciones del menú
        popup.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.description) {
                // Mostrar descripción de la tarea en un cuadro de diálogo
                new AboutDialog(view.getContext(), task.getDescription());
                return true;
            }
            if (item.getItemId() == R.id.delete) {
                // Obtener la posición del adaptador y llamar al método deleteList
                int position = ((MyViewHolder) view.getTag()).getAdapterPosition();
                CustomAdapter.comunicator.deleteList(position);
                return true;
            }
            if (item.getItemId() == R.id.edit) {
                // Obtener la posición del adaptador y llamar al método editTask
                int position = ((MyViewHolder) view.getTag()).getAdapterPosition();
                CustomAdapter.comunicator.editTask(task, position);
                return true;
            }
            return false;
        });
        popup.show();
    }

}

