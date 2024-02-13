package com.rdd.trasstarea.database;

import android.content.Context;

import com.rdd.trasstarea.model.Task;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class DataBaseExecutor {
    private final AppDataBase appDataBase;
    private final ExecutorService executor;

    public DataBaseExecutor(Context context) {
        this.appDataBase = AppDataBase.getInstance(context);
        this.executor = Executors.newSingleThreadExecutor();
    }

    public Future<Void> insertarTask(final Task task) {
        return executor.submit(() -> {
            appDataBase.daoTask().insertTask(task);
            return null;
        });

    }

    public Future<List<Task>> obtenerTodasLasTareas() {
        return executor.submit(() -> {
            try {
                List<Task> tareas = appDataBase.daoTask().getAll();
                return tareas;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }


    public Future<Void> actualizarTarea(final Task task) {
        return executor.submit(() -> {
            appDataBase.daoTask().updateTask(task);
            return null;
        });
    }

    public Future<Void> deleteTask(final Task task) {
        return executor.submit((Callable<Void>) () -> {
            appDataBase.daoTask().delete(task);
            return null;
        });
    }
}
