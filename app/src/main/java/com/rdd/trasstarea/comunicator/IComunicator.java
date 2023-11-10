package com.rdd.trasstarea.comunicator;

import com.rdd.trasstarea.model.Task;

import java.util.List;

public interface IComunicator {
    void deleteList(int position);

    void  addTask(Task task);
}
