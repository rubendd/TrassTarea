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
import com.rdd.trasstarea.activities.listactivity.dialogs.DeleteDialog;
import com.rdd.trasstarea.comunicator.IComunicator;
import com.rdd.trasstarea.model.Task;

import java.util.ArrayList;
import java.util.List;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder> {

    private final List<Task> tasksDataSet;
    private static IComunicator comunicator;
    private boolean aceptar = false;

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        private final TextView titulo, fecha, tiempoRestante;
        private final ImageView prioritaria;
        private final ProgressBar duracion;
        private final View view;

        public MyViewHolder(View view) {
            super(view);
            prioritaria = view.findViewById(R.id.imageView);
            titulo = view.findViewById(R.id.titulo);
            duracion = view.findViewById(R.id.progressBar);
            fecha = view.findViewById(R.id.fecha);
            tiempoRestante = view.findViewById(R.id.tiempoRestante);
            this.view = view;
        }

        public void bindTask(Task c) {
            titulo.setText(c.getTitulo());
            fecha.setText(c.calendar());
            changeColorDaysLeft(c);
            tiempoRestante.setText(String.valueOf(c.getDaysLeft()));
            duracion.setProgress(c.getProgresState());

            //Comprobaciones
            if (c.getProgresState() == 100){
                titulo.setPaintFlags(titulo.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            }
            if (c.isPrioritaria()) {
                prioritaria.setImageResource(R.drawable.baseline_stars_24);
            }

            // Click listener
            view.setOnClickListener(v -> {
                showPopupMenu(v, c);
            });
        }

        private void changeColorDaysLeft(Task c) {
            if (c.getDaysLeft() < 0) {
                tiempoRestante.setTextColor(Color.RED);
            }
        }
    }

    public CustomAdapter(List<Task> dataSet, IComunicator comunicator) {
        tasksDataSet = new ArrayList<>(dataSet);
        CustomAdapter.comunicator = comunicator;
    }

    public void updateData(List<Task> newData) {
        tasksDataSet.clear();
        tasksDataSet.addAll(newData);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.list, viewGroup, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.bindTask(tasksDataSet.get(position));
        holder.view.setTag(holder);
    }

    @Override
    public int getItemCount() {
        return tasksDataSet.size();
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    private static void showPopupMenu(View view, Task task) {
        PopupMenu popup = new PopupMenu(view.getContext(), view);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.taskmenu, popup.getMenu());

        popup.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.description) {
                new AboutDialog(view.getContext(), task.getDescription());
                return true;
            }
            if (item.getItemId() == R.id.delete) {
                // Get the adapter position and pass it to the deleteList method
                int position = ((MyViewHolder) view.getTag()).getAdapterPosition();

                    CustomAdapter.comunicator.deleteList(position);


                return true;
            }
            if (item.getItemId() == R.id.edit){
                int position = ((MyViewHolder) view.getTag()).getAdapterPosition();
                    CustomAdapter.comunicator.editTask(task, position);

                return true;
            }
            return false;
        });
        popup.show();
    }


    private static boolean iniciarDelete(View view){

        DeleteDialog deleteDialog = new DeleteDialog();
        boolean userAccepted = deleteDialog.showDelete(view.getContext());

        return userAccepted;


    }
}
