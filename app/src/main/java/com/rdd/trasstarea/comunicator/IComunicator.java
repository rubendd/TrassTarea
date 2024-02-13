package com.rdd.trasstarea.comunicator;

import com.rdd.trasstarea.model.Task;

public interface IComunicator {
     void deleteList(Task task);
     void createTask();
     void editTask(Task task, int position);
     void detalles(Task task, int position);
}

