package com.rdd.trasstarea.comunicator;

import com.rdd.trasstarea.activities.listactivity.recycler.CustomAdapter;
import com.rdd.trasstarea.model.Task;

import java.util.List;

public class Comunicador implements IComunicator{


    private CustomAdapter customAdapter;
    private List<Task> listTareas;
    public Comunicador(CustomAdapter customAdapter, List<Task> listTareas) {
        this.customAdapter = customAdapter;
        this.listTareas = listTareas;
    }

    @Override
    public void deleteList(int position) {
        listTareas.remove(position); // Aunque hayamos borrado la tarea de la lista del viewholder, tenemos que borrarla de esta lista ya que no se guardaría los cambios
        customAdapter.notifyItemRemoved(position);
    }


    @Override
    public  void createTask(Task task) {
        listTareas.add(task);
        customAdapter.notifyItemInserted(customAdapter.getItemCount()+1);
    }
}
