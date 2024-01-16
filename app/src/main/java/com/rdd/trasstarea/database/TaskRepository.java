package com.rdd.trasstarea.database;

import android.annotation.SuppressLint;

import com.rdd.trasstarea.model.Task;

import java.util.List;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class TaskRepository {

    private final AppDataBase appDataBase;

    public TaskRepository(AppDataBase appDataBase) {
        this.appDataBase = appDataBase;
    }

    /**
     * En este metodo, Schedulers.io()
     * y AndroidSchedulers.mainThread() se utilizan para
     * realizar la operación de inserción en un hilo de fondo
     * y para manejar el resultado en el hilo principal, respectivamente.
     * @param task
     */
    @SuppressLint("CheckResult")
    public void insertarTask(Task task) {
        appDataBase.daoTask().insertTask(task)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> System.out.println("Inserción exitosa"),
                        throwable -> System.out.println("Error en la inserción: " + throwable.getMessage()));
    }

    public Single<List<Task>> obtenerSingle() {
        return appDataBase.daoTask().getAll()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @SuppressLint("CheckResult")
    public void actualizarTarea(Task task) {
        appDataBase.daoTask().updateTask(task)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> System.out.println("Actualización exitosa"),
                        throwable -> System.out.println("Error en la actualización: " + throwable.getMessage()));
    }

    @SuppressLint("CheckResult")
    public void deleteTask(Task task) {
        appDataBase.daoTask().delete(task)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> System.out.println("Eliminación exitosa"),
                        throwable -> System.out.println("Error en la eliminación: " + throwable.getMessage()));
    }


}