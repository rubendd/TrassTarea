package com.rdd.trasstarea.database;

import android.content.Context;

import com.rdd.trasstarea.model.Task;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class TaskRepository {

    //TODO Hacerlo con future.
    private final DataBaseExecutor databaseExecutor;

    public TaskRepository(Context context) {
        this.databaseExecutor = new DataBaseExecutor(context);
    }

    public Future<Void> insertarTask(Task task) {
        return databaseExecutor.insertarTask(task);
    }

    public Future<List<Task>> obtenerTodasLasTareas() throws ExecutionException, InterruptedException {
        return databaseExecutor.obtenerTodasLasTareas();
    }

    public Future<Void> actualizarTarea(Task task) {
        return databaseExecutor.actualizarTarea(task);
    }

    public Future<Void> deleteTask(Task task) {
        return databaseExecutor.deleteTask(task);
    }

}