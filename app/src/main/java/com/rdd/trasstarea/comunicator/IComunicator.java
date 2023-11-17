package com.rdd.trasstarea.comunicator;

import com.rdd.trasstarea.model.Task;

public interface IComunicator {
     void deleteList(int position);
     void createTask();

     void editTask(Task task, int position);
}

